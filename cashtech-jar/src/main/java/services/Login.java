/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.util.List;
import models.Usuario;
import repositories.LoginRepository;

/**
 *
 * @author murilo
 */
public class Login {

    LoginRepository executarLogin = new LoginRepository();

    public List<Usuario> verificarLogin(String usuario, String senha) {
        return executarLogin.verificarExisteUsuario(usuario, senha);
    }

}
