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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author murilo
 */
public class MonitorarService {

    MonitorarRepository monitorarRepository = new MonitorarRepository();

    public void monitorarHardware(Integer idAtm) {

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
                List<Disco> disco = grupoDeDiscos.getDiscos();
                Rede rede = looca.getRede();
                RedeInterfaceGroup redeInterfaceGroup = rede.getGrupoDeInterfaces();
                List<RedeInterface> redeInterfaces = redeInterfaceGroup.getInterfaces();

                LocalDateTime dtMetrica = LocalDateTime.now();

                monitorarRepository.enviarSistema(idAtm, sistema, dtMetrica, idSistema);

                monitorarRepository.enviarMetrica(idMemoria, dtMetrica, memoria.getEmUso());
                monitorarRepository.enviarMetrica(idProcessador, dtMetrica, processador.getUso());

                for (Map<String, Object> volume : idsVolume) {
                    Integer idVolume = (Integer) volume.get("id");
                    String pontoMontagem = (String) volume.get("ponto_montagem");
                    Optional<Volume> volumeOptional = volumes.stream().filter(v -> v.getPontoDeMontagem().equals(pontoMontagem)).findFirst();
                    Volume volumeMonitorado = volumeOptional.get();
                    monitorarRepository.enviarMetrica(idVolume, dtMetrica, volumeMonitorado.getDisponivel());
                }

                Optional<RedeInterface> optRedeInterface = redeInterfaces.stream().filter(
                        r -> r.getBytesEnviados() > 0 || r.getBytesRecebidos() > 0).findFirst();

                RedeInterface redeInterface = optRedeInterface.get();

                monitorarRepository.enviarMetricaRede(idRede, redeInterface.getBytesRecebidos(), redeInterface.getBytesEnviados(), dtMetrica);;
            }

        }, 0, 3000);
    }

    public void verificarMetricas(Memoria memoria, Processador processador, Disco disco, RedeInterface redeInterface) {
        // lógica para parametrização, enviar notifcação e mensagem do slack
    }

}
