/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import com.github.britooo.looca.api.core.Looca;
import java.util.List;
import models.Usuario;
import repositories.LoginRepository;
import com.github.britooo.looca.api.group.rede.Rede;
import com.github.britooo.looca.api.group.rede.RedeParametros;
import java.util.Map;

/**
 *
 * @author murilo
 */
public class Login {

    Looca looca = new Looca();
    Rede rede = looca.getRede();
    RedeParametros parametros = rede.getParametros();
    
    String hostName = rede.getParametros().getHostName();

    LoginRepository executarLogin = new LoginRepository();

    public List<Usuario> verificarLogin(String usuario, String senha) {
        return executarLogin.verificarExisteUsuario(usuario, senha);
    }

    public Boolean hasMaquina() {
        List<Map<String, Object>> listaMaquina = executarLogin.verificarMaquina(hostName);
        return !listaMaquina.isEmpty();
    }

    public Integer identificarMaquina() {
        List<Integer> listaID = executarLogin.buscarIdMaquina(rede.getParametros().getHostName());
        return listaID.get(0);
    }
}
