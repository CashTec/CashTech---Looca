/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositories;

import cashtech.jar.DataBase;
import models.Parametrizacao;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author kanaiama
 */
public class ParametrizarRepository {
    DataBase conexao = new DataBase();

    JdbcTemplate con = conexao.getConnection();

//    public Parametrizacao verParametrizacao(Integer empresaID){
//        return con.queryForObject(
//                "select qtd_cpu_max, "
//                        + "qtd_bytes_enviado_max, "
//                        + "qtd_bytes_recebido_max, "
//                        + "qtd_memoria_max, "
//                        + "qtd_disco_max "
//                        + "from parametrizacao where empresa_id = ?",
//                new BeanPropertyRowMapper<>(Parametrizacao.class), empresaID);
//    }
//    
    public void atualizarParametrizacao
        (Integer empresaID, Integer qtdCpuMax, Long qtdBytesEnviadoMax, 
                Long qtdBytesRecebidoMax, Long qtdMemoriaMax, Long qtdDiscoMax) {
    con.update(
        "update parametrizacao set qtd_cpu_max = ?, qtd_bytes_enviado_max = ?,"
                + " qtd_bytes_recebido_max = ?, qtd_memoria_max = ?, "
                + "qtd_disco_max = ? where empresa_id = ?",
        qtdCpuMax, qtdBytesEnviadoMax, qtdBytesRecebidoMax, 
        qtdMemoriaMax, qtdDiscoMax, empresaID
    );
}

}
