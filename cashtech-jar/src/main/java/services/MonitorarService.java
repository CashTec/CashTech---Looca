/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import com.github.britooo.looca.api.group.rede.Rede;
import com.github.britooo.looca.api.group.rede.RedeInterface;
import com.github.britooo.looca.api.group.rede.RedeInterfaceGroup;
import com.github.britooo.looca.api.group.sistema.Sistema;
import java.time.LocalDateTime;
import repositories.MonitorarRepository;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author murilo
 */
public class MonitorarService {

    Looca looca = new Looca();
    Sistema sistema = looca.getSistema();
    Memoria memoria = looca.getMemoria();
    Processador processador = looca.getProcessador();
    DiscoGrupo grupoDeDiscos = looca.getGrupoDeDiscos();
    List<Disco> disco = grupoDeDiscos.getDiscos();
    Rede rede = looca.getRede();
    RedeInterfaceGroup redeInterfaceGroup = rede.getGrupoDeInterfaces();
    List<RedeInterface> redeInterfaces = redeInterfaceGroup.getInterfaces();

    MonitorarRepository monitorarRepository = new MonitorarRepository();

    public void monitorarHardware(Integer idAtm) {
        Integer idMemoria = monitorarRepository.verIdComponente(idAtm, "memoria").get(0);
        Integer idProcessador = monitorarRepository.verIdComponente(idAtm, "processador").get(0);
        Integer idRede = monitorarRepository.verIdRede(idAtm).get(0);
        List<Integer> idsDisco = monitorarRepository.verIdComponente(idAtm, "disco");


        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                LocalDateTime agora = LocalDateTime.now();

                monitorarRepository.enviarSistema(idAtm, sistema, agora);

                monitorarRepository.enviarMetrica(idMemoria, agora, Double.parseDouble((memoria.getEmUso()).toString()));
                monitorarRepository.enviarMetrica(idProcessador, agora, processador.getUso());
                
                for (Integer idDisco : idsDisco) {
                    monitorarRepository.enviarMetrica(idDisco, agora, Double.parseDouble((disco.get(0).getEscritas()).toString()));
                }
                
//                monitorarRepository.enviarMetrica(idRede, agora, Double.parseDouble((redeInterfaces.get(0).getPacotesRecebidos()).toString()));

            }

        }, 0, 2000);

    }

}
