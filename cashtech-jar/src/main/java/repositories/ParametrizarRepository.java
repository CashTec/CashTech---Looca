/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositories;

import cashtech.jar.DataBase;
import com.github.britooo.looca.api.core.Looca;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author kanaiama
 */
public class ParametrizarRepository {
    DataBase conexao = new DataBase();

    JdbcTemplate con = conexao.getConnection();

    Looca looca = new Looca();
}
