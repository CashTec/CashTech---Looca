/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositories;

import cashtech.jar.DataBase;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

/**
 *
 * @author kanaiama
 */
public class ParametrizarRepository {
    DataBase conexao = new DataBase();

    JdbcTemplate con = conexao.getConnection();

    
    public List<Integer> verParametrizacao(Integer empresa_id){
        return con.query("select * from parametrizacao where empresa_id = ?",
                new SingleColumnRowMapper(Integer.class), empresa_id);
    }
}
