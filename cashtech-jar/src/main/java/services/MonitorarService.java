/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import Util.ComparadorUsoProcesso;
import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
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
import repositories.MonitorarRepository;
import java.text.DecimalFormat;
import java.util.ArrayList;
import repositories.ParametrizarRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import repositories.ProcessosRepository;
import models.Parametrizacao;

/**
 *
 * @author murilo
 */
public class MonitorarService {

    MonitorarRepository monitorarRepository = new MonitorarRepository();
    ProcessosRepository processosRepository = new ProcessosRepository();
    ParametrizarRepository parametrizarRepository = new ParametrizarRepository();

    public void monitorarHardware(Integer idAtm, Integer idEmpresaUsuario) {

        Integer idMemoria = monitorarRepository.verIdComponente(idAtm, "memoria").get(0);
        Integer idProcessador = monitorarRepository.verIdComponente(idAtm, "processador").get(0);
        Integer idRede = monitorarRepository.verIdRede(idAtm).get(0);
        Integer idSistema = monitorarRepository.verIdSistema(idAtm).get(0);
        List<Map<String, Object>> idsVolume = monitorarRepository.verIdComponenteVolume(idAtm);

        new Timer().scheduleAtFixedRate(new TimerTask() {
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

                LocalDateTime dtMetrica = LocalDateTime.now();

                monitorarRepository.enviarSistema(idAtm, sistema, dtMetrica, idSistema);

                monitorarRepository.enviarMetrica(idMemoria, dtMetrica, memoria.getEmUso());
                monitorarRepository.enviarMetrica(idProcessador, dtMetrica, processador.getUso());

                Volume volumeMonitorado = null;
                for (Map<String, Object> volume : idsVolume) {
                    Integer idVolume = (Integer) volume.get("id");
                    String pontoMontagem = (String) volume.get("ponto_montagem");
                    Optional<Volume> volumeOptional = volumes.stream().filter(v -> v.getPontoDeMontagem().equals(pontoMontagem)).findFirst();
                    volumeMonitorado = volumeOptional.get();
                    monitorarRepository.enviarMetrica(idVolume, dtMetrica, volumeMonitorado.getDisponivel());
                }

                Optional<RedeInterface> optRedeInterface = redeInterfaces.stream().filter(
                        r -> r.getBytesEnviados() > 0 || r.getBytesRecebidos() > 0).findFirst();

                RedeInterface redeInterface = optRedeInterface.get();

                monitorarRepository.enviarMetricaRede(idRede, redeInterface.getBytesRecebidos(), redeInterface.getBytesEnviados(), dtMetrica);
                Collections.sort(processos, new ComparadorUsoProcesso());

                List<Processo> topVinteProcessos = new ArrayList<>();

                for (int i = 0; i < processos.size(); i++) {
                    if (i < 20) {
                        topVinteProcessos.add(processos.get(i));
                    } else {
                        break;
                    }
                }

                for (int i = 0; i < topVinteProcessos.size(); i++) {
                    Processo processo = topVinteProcessos.get(i);
                    System.out.println(i + ", Processo: " + processo.getNome() + " uso: " + df.format(processo.getUsoCpu())
                    );
                }
                System.out.println("-".repeat(200));

                processosRepository.cadastrarProcessosAgora(idAtm, topVinteProcessos, dtMetrica);
                monitorarRepository.enviarMetricaRede(idRede,
                        redeInterface.getBytesRecebidos(), 
                        redeInterface.getBytesEnviados(), dtMetrica);;

                verificarMetricas(memoria, processador, volumeMonitorado,
                        redeInterface, idEmpresaUsuario);
            }

        }, 0, 3000);    
    }

    public void verificarMetricas(Memoria memoria, Processador processador, 
            Volume volume, RedeInterface redeInterface, Integer idEmpresaUsuario) {
        List<Parametrizacao> parametrizacao = 
                parametrizarRepository.verParametrizacao(idEmpresaUsuario);
        
        Parametrizacao usuario = parametrizacao.get(0);
        
        //Verificando métricas de Memória

        if(memoria.getDisponivel() >= (usuario.getQtd_memoria_max() * 0.75)) {
            System.out.println("ALERTA!!! ALERTA!!!"
                    + " Uso de memória atingindo o limite!");
        } else if(memoria.getDisponivel() >= (usuario.getQtd_memoria_max()* 0.50)) {
            System.out.println("Uso de memória na metade da capacidade total!");
        } else {
            System.out.println("Uso de memória na capacidade ideal!");
        }
        
        //Verificando métricas de CPU
        
        if(processador.getUso() >= (usuario.getQtd_cpu_max() * 0.75)) {
            System.out.println("ALERTA!!! ALERTA!!!"
                    + " Uso de processador atingindo o limite!");
        } else if (processador.getUso() >= (usuario.getQtd_cpu_max() * 0.5)) {
            System.out.println("Uso de processador na metade da capacidade total!");
        } else {
            System.out.println("Uso de processador na capacidade ideal!");
        }
        
        //Verificando métricas de Disco/Volume
        
        if(volume.getDisponivel() >= (usuario.getQtd_disco_max() * 0.75)) {
            System.out.println("ALERTA!!! ALERTA!!!"
                    + " Uso de disco/volume atingindo o limite!");
        } else if (volume.getDisponivel() >= (usuario.getQtd_disco_max() * 0.5)) {
            System.out.println("Uso de disco/volume na metade da capacidade total!");
        } else {
            System.out.println("Uso de disco/volume na capacidade ideal!");
        }
        
        //Verificando métricas de bytes enviados de Rede
        
        if(redeInterface.getBytesEnviados() >= (usuario.getQtd_bytes_enviado_max() * 0.75)) {
            System.out.println("ALERTA!!! ALERTA!!!"
                    + " Uso de bytes enviados atingindo o limite!");
        } else if(redeInterface.getBytesEnviados() >= (usuario.getQtd_bytes_enviado_max() * 0.5)) {
            System.out.println("Uso de bytes enviados na metade da capacidade total!");
        } else {
            System.out.println("Uso de bytes enviados na capacidade ideal!");
        }
        
        //Verificando métricas de bytes recebidos da Rede
        
         if(redeInterface.getBytesRecebidos() >= (usuario.getQtd_bytes_recebido_max() * 0.75)) {
            System.out.println("ALERTA!!! ALERTA!!!"
                    + " Uso de bytes recebidos atingindo o limite!");
        } else if(redeInterface.getBytesRecebidos()>= (usuario.getQtd_bytes_recebido_max() * 0.5)) {
            System.out.println("Uso de bytes recebidos na metade da capacidade total!");
        } else {
            System.out.println("Uso de bytes recebidos na capacidade ideal!");
        }
         
         
        
    }
}
