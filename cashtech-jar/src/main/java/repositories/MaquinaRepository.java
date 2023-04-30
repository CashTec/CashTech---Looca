/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositories;

import cashtech.jar.DataBase;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import com.github.britooo.looca.api.group.rede.RedeParametros;
import com.github.britooo.looca.api.group.sistema.Sistema;

import java.util.ArrayList;
import java.util.List;

import models.Componente;
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
        String script;
        if (conexao.getAmbiente().equals("producao")) {
            // Query do SQL
            script = "INSERT INTO Sistema (nome,fabricante,arquitetura) VALUES"
                    + "(?,?,?)";
        } else {
            script = "INSERT INTO `cashtech`.`Sistema` (`id`, `nome`, `fabricante`, `arquitetura`)"
                    + " VALUES (null,?, ?, ?)";
        }
        con.update(script,
                sistema.getSistemaOperacional(), sistema.getFabricante(), sistema.getArquitetura());
    }

    public void cadastrarEndereco() {
        String script;
        if (conexao.getAmbiente().equals("producao")) {
            // Query do SQL
            script = "INSERT INTO Endereco (rua, bairro, numero, cep) "
                    + "VALUES (NULL, NULL, NULL, NULL)";
        } else {
            script = "INSERT INTO `cashtech`.`Endereco` (`id`, `rua`, `bairro`, `numero`, `cep`) "
                    + "VALUES (NULL, NULL, NULL, NULL, NULL)";
        }
        con.update(script);
    }

    public void cadastrarMaquina(RedeParametros parametros, Integer empresa_id) {
        String script;
        if (conexao.getAmbiente().equals("producao")) {
            // Query do SQL
            script = "INSERT INTO CaixaEletronico (identificador, situacao, empresa_id, endereco_id, sistema_id)"
                    + "VALUES (?, 'ativo', ?, (SELECT TOP 1 id FROM endereco ORDER BY id DESC),"
                    + "(SELECT TOP 1 id FROM Sistema ORDER BY id DESC))";
        } else {
            script = "INSERT INTO `cashtech`.`CaixaEletronico` "
                    + "(`id`, `identificador`, `situacao`, `empresa_id`, `endereco_id`, `sistema_id`) "
                    + "VALUES (NULL,?,'ativo', ?, (select id from Endereco order by id desc limit 1),"
                    + "(select id from Sistema order by id desc limit 1));";
        }
        con.update(script,
                parametros.getHostName(), empresa_id);

    }

    public List<Integer> buscarIdMaquina(String nomeMaquina) {
        return con.query("select id from CaixaEletronico", new SingleColumnRowMapper(Integer.class));
    }


    public void cadastrarComponenteProcessador(Processador processador, Memoria memoria, DiscoGrupo discoGrupo,String tipo){

        String script = "INSERT INTO Componente" +
                "(tipo,modelo,serie,frequencia,qtd_cpu_logica," +
                "qtd_cpu_fisica,qtd_maxima,caixa_eletronico_id)" +
                "VALUES(?,?,?,?,?,?,?,(SELECT id FROM CaixaEletronico ORDER BY id DESC LIMIT 1))";

        if(tipo.equals("processador")){

            con.update(script,tipo,processador.getNome(), processador.getId(),
                                    processador.getFrequencia(),processador.getNumeroCpusLogicas(),
                                    processador.getNumeroCpusFisicas(),null);
        }
        else if(tipo.equals("memoria")){

            con.update(script,tipo,null, null,
                    null,null,
                    null,memoria.getTotal());
        }

        else if(tipo.equals("disco")){
            List<Disco> discos = discoGrupo.getDiscos();
            for(int i = 0; i< discos.size();i++){
                con.update(script,tipo,discos.get(i).getNome(), discos.get(i).getModelo(),
                       discos.get(i).getTempoDeTransferencia(), null
                        ,null,discos.get(i).getTamanho());
            }
        }
        else{
            System.out.println("Componente Inválido");
        }
    }
}
