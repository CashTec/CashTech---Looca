/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.discos.Volume;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import com.github.britooo.looca.api.group.rede.Rede;
import com.github.britooo.looca.api.group.rede.RedeInterface;
import com.github.britooo.looca.api.group.rede.RedeInterfaceGroup;
import com.github.britooo.looca.api.group.sistema.Sistema;
import java.time.LocalDateTime;
import repositories.MonitorarRepository;
import repositories.ParametrizarRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import models.Parametrizacao;

/**
 *
 * @author murilo
 */
public class MonitorarService {

    MonitorarRepository monitorarRepository = new MonitorarRepository();
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
        
        if(usuario.getQtd_memoria_max() >= (memoria.getDisponivel() * 0.75)) {
            System.out.println("");
        } else if(usuario.getQtd_memoria_max() >= (memoria.getDisponivel()) * 0.50) {
            System.out.println("");
        } else {
            System.out.println("");
        }
        
        //Verificando métricas de CPU
        
        if(usuario.getQtd_cpu_max() >= (processador.getUso() * 0.75)) {
            System.out.println("");
        } else if (usuario.getQtd_cpu_max() >= (processador.getUso() * 0.5)) {
            System.out.println("");
        } else {
            System.out.println("");
        }
        
        //Verificando métricas de Disco/Volume
        
        if(usuario.getQtd_disco_max() >= (volume.getDisponivel() * 0.75)) {
            System.out.println("");
        } else if (usuario.getQtd_disco_max() >= (volume.getDisponivel() * 0.5)) {
            System.out.println("");
        } else {
            System.out.println("");
        }
        
        //Verificando métricas de bytes enviados de Rede
        
        if(usuario.getQtd_bytes_enviado_max() >= (redeInterface.getBytesEnviados() * 0.75)) {
            System.out.println("");
        } else if(usuario.getQtd_bytes_enviado_max() >= (redeInterface.getBytesEnviados() * 0.5)) {
            System.out.println("");
        } else {
            System.out.println("");
        }
        
        //Verificando métricas de bytes recebidos da Rede
        
         if(usuario.getQtd_bytes_recebido_max() >= (redeInterface.getBytesRecebidos()* 0.75)) {
            System.out.println("");
        } else if(usuario.getQtd_bytes_recebido_max() >= (redeInterface.getBytesRecebidos() * 0.5)) {
            System.out.println("");
        } else {
            System.out.println("");
        }
        
    }
}
