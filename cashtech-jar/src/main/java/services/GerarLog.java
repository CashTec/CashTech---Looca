/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import cashtech.jar.LoginSwing;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PC
 */
public class GerarLog {

    public void login(String usuario, Boolean isErro) {

        File login = new File("log.txt");

        //throws IOException e try e catch 
        if (!login.exists()) {
            try {
                login.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(LoginSwing.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        String frase = "O usuário --" + usuario + "-- fez login";

        if (isErro) {
            frase = "Tentativa de login do usuário --" + usuario + "-- ";
        }

        //Criar uma arraylist que vai armazenar o registro em si dos logins
        List<String> lista = new ArrayList<>();
        lista.add(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss ")) + frase);

        //A classe Formatter permite que a saída de dados formatados seja
        //enviada para qualquer fluxo baseado em texto de uma maneira semelhante ao método System.out.printf.
        //Classe files é adicionada para manipulação de arquivos e diretórios
        //StandardOpenOption especifica como o arquivo deve ser aberto, 
        //no caso do APPEND Os dados são gravados no final do arquivo.       
        try {
            Files.write(Paths.get(login.getPath()), lista, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            Logger.getLogger(LoginSwing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
