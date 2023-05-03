/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositories;

import cashtech.jar.DataBase;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.discos.Volume;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import com.github.britooo.looca.api.group.rede.Rede;
import com.github.britooo.looca.api.group.rede.RedeInterface;
import com.github.britooo.looca.api.group.rede.RedeInterfaceGroup;
import com.github.britooo.looca.api.group.rede.RedeParametros;
import com.github.britooo.looca.api.group.sistema.Sistema;

import java.util.ArrayList;
import java.util.List;

import com.github.britooo.looca.api.util.Conversor;
import models.Componente;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

/**
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


    public void cadastrarInterfaceRede(RedeInterface redeDado) {
        String script;
        if (conexao.getAmbiente().equals("producao")) {
            script = "INSERT INTO NetworkInterface(nome,nome_exibicao,ipv4,ipv6,mac,caixa_eletronico_id) VALUES(?,?,?,?,?,(SELECT TOP 1 id  FROM CaixaEletronico ORDER BY id ))";

        } else {
            script = "INSERT INTO NetworkInterface(nome,nome_exibicao,ipv4,ipv6,mac,caixa_eletronico_id) VALUES(?,?,?,?,?,(SELECT id FROM CaixaEletronico ORDER BY id DESC LIMIT 1))";
        }
        con.update(
                script,
                redeDado.getNome(),
                redeDado.getNomeExibicao(),
                redeDado.getEnderecoIpv4().get(0),
                redeDado.getEnderecoIpv6().get(0),
                redeDado.getEnderecoMac()
        );
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


    public void cadastrarComponente(Processador processador, Memoria memoria, DiscoGrupo discoGrupo, String tipo) {
        String script;
        if (conexao.getAmbiente().equals("producao")) {
            script = "INSERT INTO Componente" +
                    "(tipo,nome,modelo,serie,frequencia,qtd_cpu_logica," +
                    "qtd_cpu_fisica,qtd_maxima,qtd_disponivel,ponto_montagem,sistema_arquivos,caixa_eletronico_id)" +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,(SELECT TOP 1 id FROM CaixaEletronico ORDER BY id DESC))";
        } else {
            script = "INSERT INTO Componente" +
                    "(tipo,nome,modelo,serie,frequencia,qtd_cpu_logica," +
                    "qtd_cpu_fisica,qtd_maxima,qtd_disponivel,ponto_montagem,sistema_arquivos,caixa_eletronico_id)" +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,(SELECT id FROM CaixaEletronico ORDER BY id DESC LIMIT 1))";
        }

        if (tipo.equals("processador")) {
            System.out.println(processador.getNumeroCpusFisicas());
            con.update(script,
                    tipo,// tipo
                    processador.getNome(), //nome
                    null,// modelo
                    processador.getId(),//serie
                    processador.getFrequencia(),//frequencia
                    processador.getNumeroCpusLogicas(),// qtd_cpu_logica
                    processador.getNumeroCpusFisicas(),// qtd_cpu_fisica
                    null,// qtd_maxima
                    null,//qtd_disponivel
                    null,//ponto_montagem
                    null);// sistemas_arquivos
        } else if (tipo.equals("memoria")) {

            con.update(script,
                    tipo,// tipo
                    null,// nome
                    null,// modelo
                    null,// serie
                    null,// frequencia
                    null,// q
                    null,
                    memoria.getTotal(),
                    null,
                    null,
                    null
            );
        } else if (tipo.equals("disco")) {

            List<Volume> volumes = discoGrupo.getVolumes();
            for (int i = 0; i < volumes.size(); i++) {
                con.update(script,
                        tipo, // tipo
                        volumes.get(i).getNome(), //nome
                        null,//modelo
                        null, // serie
                        null, // frequencia
                        null,//qtd_cpu_logica
                        null,// qtd_cpu_fisica
                        volumes.get(i).getTotal(),//qtd_maxima
                        volumes.get(i).getDisponivel(),// qtd_disponivel
                        volumes.get(i).getPontoDeMontagem(), // ponto_montagem
                        volumes.get(i).getTipo() // sistema_arquivos
                );
            }
        } else {
            System.out.println("Componente Inválido");
        }
    }
}
