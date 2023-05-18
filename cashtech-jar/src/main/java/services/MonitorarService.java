/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import Util.ComparadorUsoProcesso;
import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.discos.Volume;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import com.github.britooo.looca.api.group.processos.Processo;
import com.github.britooo.looca.api.group.processos.ProcessoGrupo;
import com.github.britooo.looca.api.group.rede.Rede;
import com.github.britooo.looca.api.group.rede.RedeInterface;
import com.github.britooo.looca.api.group.rede.RedeInterfaceGroup;
import com.github.britooo.looca.api.group.sistema.Sistema;

import java.time.LocalDateTime;
import java.util.Collections;

import com.github.britooo.looca.api.util.Conversor;
import org.json.JSONObject;
import repositories.MonitorarRepository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import repositories.ParametrizarRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import repositories.ProcessosRepository;
import models.Parametrizacao;
import repositories.NotificacaoRepository;
import slack.configuration.Slack;
import slack.configuration.SlackRepository;

/**
 * @author murilo
 */
public class MonitorarService {

    MonitorarRepository monitorarRepository = new MonitorarRepository();
    ProcessosRepository processosRepository = new ProcessosRepository();
    ParametrizarRepository parametrizarRepository = new ParametrizarRepository();
    NotificacaoRepository notificacaoRepository = new NotificacaoRepository();

    private Integer countSeconds = 6;

    public void monitorarHardware(Integer idAtm, Integer idEmpresaUsuario) {

        Integer idMemoria = monitorarRepository.verIdComponente(idAtm, "memoria").get(0);
        Integer idProcessador = monitorarRepository.verIdComponente(idAtm, "processador").get(0);
        Integer idRede = monitorarRepository.verIdRede(idAtm).get(0);
        Integer idSistema = monitorarRepository.verIdSistema(idAtm).get(0);
        List<Map<String, Object>> idsVolume = monitorarRepository.verIdComponenteVolume(idAtm);
        SlackRepository slackRepository = new SlackRepository();
        slackRepository.pegarUrl();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            Long bytesRecebidosAntigo = null;
            Long bytesEnviadosAntigo = null;

            @Override
            public void run() {
                Looca looca = new Looca();
                Sistema sistema = looca.getSistema();
                Memoria memoria = looca.getMemoria();
                Processador processador = looca.getProcessador();
                DiscoGrupo grupoDeDiscos = looca.getGrupoDeDiscos();
                List<Volume> volumes = grupoDeDiscos.getVolumes();
                Rede rede = looca.getRede();
                ProcessoGrupo processoGrupo = looca.getGrupoDeProcessos();
                List<Processo> processos = processoGrupo.getProcessos();
                RedeInterfaceGroup redeInterfaceGroup = rede.getGrupoDeInterfaces();
                List<RedeInterface> redeInterfaces = redeInterfaceGroup.getInterfaces();

                ZonedDateTime horarioBrasilia = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
                LocalDateTime dtMetrica = horarioBrasilia.toLocalDateTime();

                monitorarRepository.enviarSistema(idAtm, sistema, dtMetrica, idSistema);

                monitorarRepository.enviarMetrica(idMemoria, dtMetrica, memoria.getEmUso());
                monitorarRepository.enviarMetrica(idProcessador, dtMetrica, processador.getUso());

                Volume volumeMonitorado = null;
                for (Map<String, Object> volume : idsVolume) {
                    Integer idVolume = (Integer) volume.get("id");
                    String pontoMontagemTodo = (String) volume.get("ponto_montagem");
                    String pontoMontagem = pontoMontagemTodo.endsWith("\\") ? pontoMontagemTodo.substring(0, pontoMontagemTodo.lastIndexOf("\\")) : pontoMontagemTodo;

                    Optional<Volume> volumeOptional = volumes.stream().filter(v -> v.getPontoDeMontagem().equals(pontoMontagem)).findFirst();
                    volumeMonitorado = volumeOptional.get();
                    monitorarRepository.enviarMetrica(idVolume, dtMetrica, volumeMonitorado.getDisponivel());
                }

                Optional<RedeInterface> optRedeInterface = redeInterfaces.stream().filter(
                        r -> r.getBytesEnviados() > 0 || r.getBytesRecebidos() > 0).findFirst();

                RedeInterface redeInterface = optRedeInterface.get();

                Collections.sort(processos, new ComparadorUsoProcesso());

                List<Processo> topVinteProcessos = new ArrayList<>();

                for (int i = 0; i < processos.size(); i++) {
                    if (i < 20) {
                        topVinteProcessos.add(processos.get(i));
                    } else {
                        break;
                    }
                }

                processosRepository.cadastrarProcessosAgora(idAtm, topVinteProcessos, dtMetrica);
                monitorarRepository.enviarMetricaRede(idRede,
                        bytesRecebidosAntigo == null ? 0 : redeInterface.getBytesRecebidos() - bytesRecebidosAntigo,
                        bytesEnviadosAntigo == null ? 0 : redeInterface.getBytesEnviados() - bytesEnviadosAntigo, dtMetrica);

                verificarMetricas(memoria, processador, volumeMonitorado,
                        redeInterface, dtMetrica, idEmpresaUsuario);

                bytesRecebidosAntigo = redeInterface.getBytesRecebidos();
                bytesEnviadosAntigo = redeInterface.getBytesEnviados();
            }

        }, 0, 3000);
    }

    public void verificarMetricas(Memoria memoria, Processador processador,
            Volume volume, RedeInterface redeInterface, LocalDateTime dtNotificacao, Integer idEmpresaUsuario) {
        if (isTwentySeconds()) {

            List<Parametrizacao> parametrizacao
                    = parametrizarRepository.verParametrizacao(idEmpresaUsuario);

            Parametrizacao usuario = parametrizacao.get(0);

            Boolean isAchado = false;
            String frase = "";
            String memoriaConvertida = Conversor.formatarBytes(memoria.getDisponivel());
            String volumeConvertido = Conversor.formatarBytes(volume.getDisponivel());
            String bytesRecebidosConvertido = Conversor.formatarBytes(redeInterface.getBytesRecebidos());
            String bytesEnviadosConvertido = Conversor.formatarBytes(redeInterface.getBytesEnviados());

            //Verificando métricas de Memória
            if (memoria.getDisponivel() >= (usuario.getQtd_memoria_max() * 0.75)) {
                isAchado = true;
                frase += ("\nUso de memória atingindo o limite! Disponível: " + memoriaConvertida);
            } else if (memoria.getDisponivel() >= (usuario.getQtd_memoria_max() * 0.50)) {
                frase += ("\n\nUso de memória na metade da capacidade total! Disponível: " + memoriaConvertida);
            }

            //Verificando métricas de CPU
            if (processador.getUso() >= (usuario.getQtd_cpu_max() * 0.75)) {
                isAchado = true;
                frase += ("\nUso de processador atingindo o limite! Disponível: " + processador.getUso());
            } else if (processador.getUso() >= (usuario.getQtd_cpu_max() * 0.5)) {
                frase += ("\n\nUso de processador na metade da capacidade total! Disponível: " + processador.getUso());
            }

            //Verificando métricas de Disco/Volume
            if (volume.getDisponivel() >= (usuario.getQtd_disco_max() * 0.75)) {
                isAchado = true;
                frase += ("\n Uso de disco/volume atingindo o limite! Disponível: " + volumeConvertido);
            } else if (volume.getDisponivel() >= (usuario.getQtd_disco_max() * 0.5)) {
                frase += ("\n\nUso de disco/volume na metade da capacidade total! Disponível: " + volumeConvertido);
            }

            //Verificando métricas de bytes enviados de Rede
            if (redeInterface.getBytesEnviados() >= (usuario.getQtd_bytes_enviado_max() * 0.75)) {
                isAchado = true;
                frase += ("\n Uso de bytes enviados atingindo o limite! Disponível: " + bytesEnviadosConvertido);
            } else if (redeInterface.getBytesEnviados() >= (usuario.getQtd_bytes_enviado_max() * 0.5)) {
                frase += ("\n\nUso de bytes enviados na metade da capacidade total! Disponível: " + bytesEnviadosConvertido);
            }

            //Verificando métricas de bytes recebidos da Rede
            if (redeInterface.getBytesRecebidos() >= (usuario.getQtd_bytes_recebido_max() * 0.75)) {
                isAchado = true;
                frase += ("\n Uso de bytes recebidos atingindo o limite! " + bytesRecebidosConvertido);
            } else if (redeInterface.getBytesRecebidos() >= (usuario.getQtd_bytes_recebido_max() * 0.5)) {
                frase += ("\n\nUso de bytes recebidos na metade da capacidade total! " + bytesRecebidosConvertido);
            }

            System.out.println(frase);

            notificacaoRepository.enviarNotificacao(frase, idEmpresaUsuario, dtNotificacao);
            if(isAchado) {
                //adicionar texto antes da frase
                frase = ":warning::alphabet-yellow-a::alphabet-yellow-l::alphabet-yellow-e::alphabet-yellow-r::alphabet-yellow-t::alphabet-yellow-a::warning:\n" + frase;
            }

            try {
                JSONObject json = new JSONObject();
                json.put("text", frase);
                Slack.sendMessage(json);
            } catch (Exception e) {
                System.out.println("Erro ao enviar mensagem para o Slack");
            }
        }

    }

    public Boolean isTwentySeconds() {
        countSeconds++;
        if (countSeconds == 7) {
            countSeconds = 0;
            return true;
        }
        return false;
    }
}
