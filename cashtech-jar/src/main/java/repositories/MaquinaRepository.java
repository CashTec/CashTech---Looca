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

        } else {
            con.update("INSERT INTO `cashtech`.`Sistema` (`id`, `nome`, `fabricante`, `arquitetura`)"
                    + " VALUES (null,?, ?, ?);",
                    sistema.getSistemaOperacional(), sistema.getFabricante(), sistema.getArquitetura());
        }
    }

    public void cadastrarEndereco() {
        con.update("INSERT INTO `cashtech`.`Endereco` (`id`, `rua`, `bairro`, `numero`, `cep`, `complemento`) "
                + "VALUES (NULL, NULL, NULL, NULL, NULL, NULL)");
    }

    public Integer cadastrarMaquina(RedeParametros parametros) {

        con.update("INSERT INTO `cashtech`.`CaixaEletronico` "
                + "(`id`, `identificador`, `situacao`, `empresa_id`, `endereco_id`, `sistema_id`) "
                + "VALUES (NULL,?,'ativo', 1, (select id from endereco order by id desc limit 1),"
                + "(select id from sistema order by id desc limit 1));",
                parametros.getHostName());

        return 1;
    }

    public void cadastrarProcessosPermitidosPadrao(List<String> processos) {

        String sql = "INSERT INTO `cashtech`.`ProcessoPermitido` (`id`, `nome`, `empresa_id`) VALUES ";
        for (int i = 0; i < processos.size(); i++) {
            String processo = processos.get(i);
            if (i == processos.size() - 1) {
                sql += String.format("(null,'%s',1)", processo);
            } else {
                sql += String.format("(null,'%s',1), ", processo);
            }
        }
        con.update(sql);
    }

}
