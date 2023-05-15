/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositories;

import cashtech.jar.DataBase;
import com.github.britooo.looca.api.group.rede.RedeParametros;
import com.github.britooo.looca.api.group.sistema.Sistema;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

/**
 *
 * @author murilo
 */
public class MonitorarRepository {

    DataBase conexao = new DataBase();
    RedeParametros redeParametros;

    public JdbcTemplate con = conexao.getConnection();

    public List<Integer> verIdComponente(Integer idAtm, String tipoComponente) {
        return con.query("select id from Componente where caixa_eletronico_id = ? and tipo = ?",
                new SingleColumnRowMapper(Integer.class), idAtm, tipoComponente);
    }

    public List<Integer> verIdSistema(Integer idAtm) {
        return con.query("select s.id from Sistema s join CaixaEletronico ce"
                + " on ce.sistema_id  = s.id where ce.id = ?", new SingleColumnRowMapper(Integer.class), idAtm);
    }

    public List<Map<String, Object>> verIdComponenteVolume(Integer idAtm) {
        return con.queryForList("select id, ponto_montagem from Componente where caixa_eletronico_id = ? and tipo = 'disco' ", idAtm);
    }

    public List<Integer> verIdRede(Integer idAtm) {
        return con.query("select id from NetworkInterface where caixa_eletronico_id = ?",
                new SingleColumnRowMapper(Integer.class), idAtm);
    }

    public void enviarSistema(Integer idAtm, Sistema sistema, LocalDateTime dtMetrica, Integer idSistema) {
        LocalDateTime inicializado = LocalDateTime.ofInstant(sistema.getInicializado(),
                ZoneId.systemDefault());
        con.update("insert into MetricaSistema (iniciado,tempo_atividade,dt_Metrica,sistema_id)"
                + " values (?,?,?,?)",
                inicializado, sistema.getTempoDeAtividade(), dtMetrica, idSistema);
    }

    public void enviarMetrica(Integer componenteId, LocalDateTime dtMetrica, Double qtdConsumido) {
        con.update("insert into MetricaComponente (qtd_consumido,dt_metrica,componente_id) values (?,?,?)",
                qtdConsumido, dtMetrica, componenteId);
    }

    public void enviarMetrica(Integer componenteId, LocalDateTime dtMetrica, Long qtdConsumido) {
        con.update("insert into MetricaComponente (qtd_consumido,dt_metrica,componente_id) values (?,?,?)",
                qtdConsumido, dtMetrica, componenteId);
    }

    public void enviarMetrica(Integer componenteId, LocalDateTime dtMetrica, Integer qtdConsumido) {
        con.update("insert into MetricaComponente (qtd_consumido,dt_metrica,componente_id) values (?,?,?)",
                qtdConsumido, dtMetrica, componenteId);
    }

    public void enviarMetricaRede(Integer redeId, Long bytesRecebidosSegundo,
            Long bytesEnviadosSegundo, LocalDateTime dtMetrica) {
        con.update("insert into MetricaRedeInterface (bytes_recebidos_segundo,"
                + "bytes_enviados_segundo,dt_metrica,network_interface_id) values (?,?,?,?)",
                bytesRecebidosSegundo, bytesEnviadosSegundo, dtMetrica, redeId);
    }
}
