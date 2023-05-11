/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import com.github.britooo.looca.api.group.processos.Processo;
import com.github.britooo.looca.api.group.processos.ProcessoGrupo;
import com.github.britooo.looca.api.group.rede.Rede;
import com.github.britooo.looca.api.group.rede.RedeInterface;
import com.github.britooo.looca.api.group.rede.RedeInterfaceGroup;
import com.github.britooo.looca.api.group.rede.RedeParametros;
import com.github.britooo.looca.api.group.sistema.Sistema;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import repositories.MaquinaRepository;
import repositories.ProcessosRepository;

/**
 *
 * @author murilo
 */
public class MaquinaService {

    Looca looca = new Looca();
    Sistema sistema = looca.getSistema();
    Memoria memoria = looca.getMemoria();
    Processador processador = looca.getProcessador();
    DiscoGrupo grupoDeDiscos = looca.getGrupoDeDiscos();
    Rede rede = looca.getRede();
    ProcessoGrupo grupoDeProcessos = looca.getGrupoDeProcessos();
    RedeParametros parametros = rede.getParametros();
    RedeInterfaceGroup redeInterfaceGroup = rede.getGrupoDeInterfaces();
    List<RedeInterface> redeInterfaces = redeInterfaceGroup.getInterfaces();

    MaquinaRepository executar = new MaquinaRepository();
    ProcessosRepository executarProcesso = new ProcessosRepository();

    public void executarCadastro(Integer empresaId) {
        executar.cadastrarSistema(sistema);
        executar.cadastrarEndereco();
        executar.cadastrarMaquina(parametros, empresaId);
        executar.cadastrarComponente(processador, memoria, grupoDeDiscos, "processador");
        executar.cadastrarComponente(processador, memoria, grupoDeDiscos, "memoria");
        executar.cadastrarComponente(processador, memoria, grupoDeDiscos, "disco");

        //CADASTRAR INTERFACE DA REDE
//        List<RedeInterface> redes = rede.getGrupoDeInterfaces().getInterfaces();;
//        RedeInterface redeDado = null;
//        for (RedeInterface redeItem : redes) {
//            if (redeItem.getBytesEnviados() > 0 || redeItem.getBytesRecebidos() > 0) {
//                redeDado = redeItem;
//            }
//        }
//        if (redeDado != null) {
//        }

        Optional<RedeInterface> optRedeInterface = redeInterfaces.stream().filter(
                r -> r.getBytesEnviados() > 0 || r.getBytesRecebidos() > 0).findFirst();

        RedeInterface redeInterface = optRedeInterface.get();

        executar.cadastrarInterfaceRede(redeInterface);

        // =============== Cadastrar Processos permitidos ================
        // Lista de processos permitidos são os primeiros processos que carrega
        // Passar esses processos para apenas o nome
        List<String> nomeProcessosPermitidos = grupoDeProcessos.getProcessos().stream()
                .map(Processo::getNome)
                .distinct()
                .toList();

        // Verificar se já está cadastrado:
        List<String> processosPermitidosBanco = executarProcesso.processosPermitidos(empresaId);

        List<String> processosFiltrados = nomeProcessosPermitidos.stream()
                .filter(processo -> !processosPermitidosBanco.contains(processo))
                .toList();

        executarProcesso.cadastrarProcessosPermitidosPadrao(processosFiltrados, empresaId);
        // ==================================================================
    }

    public Integer identificarMaquina() {
        List<Integer> listaID = executar.buscarIdMaquina(rede.getParametros().getHostName());
        return listaID.get(0);
    }
}
