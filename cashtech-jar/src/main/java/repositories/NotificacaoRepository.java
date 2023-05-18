/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositories;

import cashtech.jar.DataBase;
import cashtech.jar.DataBaseDocker;
import java.time.LocalDateTime;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author PC
 */
public class NotificacaoRepository {

    DataBase dbAzure = new DataBase();
    DataBaseDocker dbDocker = new DataBaseDocker();

    JdbcTemplate conAzure = dbAzure.getConnection();
    JdbcTemplate conDocker = dbDocker.getConnection();

    public void enviarNotificacao(String frase, Integer empresaId, LocalDateTime dtNotificacao) {

        String script = "insert into Notificacao values (?, ? ,?)";

        conAzure.update(script,
                frase, dtNotificacao, empresaId);

        conDocker.update(script,
                frase, dtNotificacao, empresaId);

    }

}
