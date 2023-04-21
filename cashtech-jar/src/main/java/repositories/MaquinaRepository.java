/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositories;

import cashtech.jar.DataBase;
import com.github.britooo.looca.api.group.rede.RedeParametros;
import com.github.britooo.looca.api.group.sistema.Sistema;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

/**
 *
 * @author murilo
 */
public class MaquinaRepository {

    DataBase conexao = new DataBase();
    RedeParametros redeParametros;

    public JdbcTemplate con = conexao.getConnection();

    public void cadastrarSistema(Sistema sistema) {
        if (conexao.getAmbiente().equals("producao")) {
            // Query do SQL
        } else {
            con.update("INSERT INTO `cashtech`.`Sistema` (`id`, `nome`, `fabricante`, `arquitetura`)"
                    + " VALUES (null,?, ?, ?);",
                    sistema.getSistemaOperacional(), sistema.getFabricante(), sistema.getArquitetura());
        }
    }

    public void cadastrarEndereco() {
        con.update("INSERT INTO `cashtech`.`Endereco` (`id`, `rua`, `bairro`, `numero`, `cep`) "
                + "VALUES (NULL, NULL, NULL, NULL, NULL)");
    }

    public void cadastrarMaquina(RedeParametros parametros, Integer empresa_id) {

        con.update("INSERT INTO `cashtech`.`CaixaEletronico` "
                + "(`id`, `identificador`, `situacao`, `empresa_id`, `endereco_id`, `sistema_id`) "
                + "VALUES (NULL,?,'ativo', ?, (select id from endereco order by id desc limit 1),"
                + "(select id from sistema order by id desc limit 1));",
                parametros.getHostName(), empresa_id);

    }

    public List<Integer> buscarIdMaquina(String nomeMaquina) {
        return con.query("select id from caixaeletronico", new SingleColumnRowMapper(Integer.class));
    }
}
