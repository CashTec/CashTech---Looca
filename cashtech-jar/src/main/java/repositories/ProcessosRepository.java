/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositories;

import cashtech.jar.DataBase;
import cashtech.jar.DataBaseDocker;
import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.processos.Processo;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

/**
 * @author murilo
 */
public class ProcessosRepository {

    DataBase conexao = new DataBase();
    DataBaseDocker baseDocker = new DataBaseDocker();

    JdbcTemplate con = conexao.getConnection();
    JdbcTemplate conDocker = baseDocker.getConnection();

    Looca looca = new Looca();
    Processo processo;

    public void cadastrarProcessosPermitidosPadrao(List<String> processos, Integer empresaId) {
        String script;
        String scriptDocker;

        // Query do SQL
        script = "INSERT INTO ProcessoPermitido (nome, empresa_id) VALUES ";
        for (int i = 0; i < processos.size(); i++) {
            String processo = processos.get(i);
            if (i == processos.size() - 1) {
                script += String.format("('%s',%d)", processo, empresaId);
            } else {
                script += String.format("('%s',%d), ", processo, empresaId);
            }
        }

        con.update(script);
    }

    public List<String> processosPermitidos(Integer empresaId) {
        List<String> processosPermitidos
                = con.query("select nome from ProcessoPermitido where empresa_id = ?",
                new SingleColumnRowMapper(String.class), empresaId);

        return processosPermitidos;
    }

    public void cadastrarProcessoKilled(Integer idAtm, Processo processo, LocalDateTime dataHora) {
        String script;
        String scriptDocker;
        // Query do SQL
        script = "INSERT INTO Processo (caixa_eletronico_id, nome, "
                + "pid,uso_cpu, uso_memoria, byte_utilizado, memoria_virtual_ultilizada,"
                + " id_dead, dt_processo) "
                + "VALUES (?,?, ?, ?, ?, ?, ?, 1, ?)";

        conDocker.update(script,
                idAtm, processo.getNome(), processo.getPid(), processo.getUsoCpu(), processo.getUsoMemoria(),
                processo.getBytesUtilizados(), processo.getMemoriaVirtualUtilizada(),
                dataHora);

        con.update(script,
                idAtm, processo.getNome(), processo.getPid(), processo.getUsoCpu(), processo.getUsoMemoria(),
                processo.getBytesUtilizados(), processo.getMemoriaVirtualUtilizada(),
                dataHora);
    }

    public void cadastrarProcessosAgora(Integer idAtm, List<Processo> processos, LocalDateTime dataHora) {
        String script;
        String values = "";

        // criar um DateTimeFormatter com o padrão desejado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        // criar uma string formatada usando o DateTimeFormatter
        String formattedDateTime = dataHora.format(formatter);

        // analisar a string formatada em um novo LocalDateTime
        LocalDateTime sqlDate = LocalDateTime.parse(formattedDateTime, formatter);

        for (int i = 0; i < processos.size(); i++) {
            Processo processo = processos.get(i);

            if (i == processos.size() - 1) {
                values += String.format("(%s,'%s',%s,%s,%s,%s,%s,0, '%s')", idAtm, processo.getNome(),
                        processo.getPid(), processo.getUsoCpu(), processo.getUsoMemoria(),
                        processo.getBytesUtilizados(), processo.getMemoriaVirtualUtilizada(),
                        sqlDate);
            } else {
                values += String.format("(%s,'%s',%s,%s,%s,%s,%s,0,'%s'), ", idAtm, processo.getNome(),
                        processo.getPid(), processo.getUsoCpu(), processo.getUsoMemoria(),
                        processo.getBytesUtilizados(), processo.getMemoriaVirtualUtilizada(),
                        sqlDate);
            }
        }

        // Query do SQL
        script = String.format("INSERT INTO Processo (caixa_eletronico_id, nome, "
                + "pid,uso_cpu, uso_memoria, byte_utilizado, memoria_virtual_ultilizada,"
                + " id_dead, dt_processo) "
                + "VALUES %s", values);


        conDocker.update(script);
        con.update(script);
    }
}
