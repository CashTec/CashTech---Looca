/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositories;

import cashtech.jar.DataBase;
import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.processos.Processo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

/**
 *
 * @author murilo
 */
public class ProcessosRepository {

    DataBase conexao = new DataBase();

    JdbcTemplate con = conexao.getConnection();

    Looca looca = new Looca();
    Processo processo;

    public List<String> processosPermitidos() {

        List<String> processosPermitidos
                = con.query("select nome from ProcessoPermitido",
                        new SingleColumnRowMapper(String.class));

        return processosPermitidos;
    }

    public void cadastrarProcessoKilled(Integer idAtm, Processo processo, LocalDateTime dataHora) {
        con.update("INSERT INTO `cashtech`.`Processo` (`id`, `caixa_eletronico_id`, `nome`, "
                + "`pid`,`uso_cpu`, `uso_memoria`, `byte_utilizado`, `memoria_virtual_ultilizada`,"
                + " `id_dead`, `dt_processo`) "
                + "VALUES (NULL,?,?, ?, ?, ?, ?, ?, 1, ?)",
                idAtm, processo.getNome(), processo.getPid(), processo.getUsoCpu(), processo.getUsoMemoria(),
                processo.getBytesUtilizados(), processo.getMemoriaVirtualUtilizada(),
                dataHora);
    }
}
