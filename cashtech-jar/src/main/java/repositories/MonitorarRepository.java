/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositories;

import cashtech.jar.DataBase;
import com.github.britooo.looca.api.group.rede.RedeParametros;
import com.github.britooo.looca.api.group.sistema.Sistema;
import java.time.LocalDateTime;
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
        return con.query("select id from componente where caixa_eletronico_id = ? and tipo = ?",
                new SingleColumnRowMapper(Integer.class), idAtm, tipoComponente);
    }

    public List<Map<String, Object>> verIdComponenteVolume(Integer idAtm) {
        return con.queryForList("select id, ponto_montagem from componente where caixa_eletronico_id = ? and tipo = ", idAtm);
    }

    public List<Integer> verIdRede(Integer idAtm) {
        return con.query("select id from networkinterface where caixa_eletronico_id = ?",
                new SingleColumnRowMapper(Integer.class), idAtm);
    }

    public void enviarSistema(Integer idAtm, Sistema sistema, LocalDateTime dtMetrica) {
        con.update("insert into metricasistema (iniciado,tempo_atividade,dt_Metrica) values (?,?,?)",
                sistema.getInicializado(), sistema.getTempoDeAtividade(), dtMetrica);
    }

    public void enviarMetrica(Integer componenteId, LocalDateTime dtMetrica, Double qtdConsumido) {
        con.update("insert into metricacomponente (qtd_consumido,dt_metrica,componente_id) values");
    }

    public void enviarMetrica(Integer componenteId, LocalDateTime dtMetrica, Long qtdConsumido) {
        con.update("insert into metricacomponente (qtd_consumido,dt_metrica,componente_id) values");
    }

    public void enviarMetrica(Integer componenteId, LocalDateTime dtMetrica, Integer qtdConsumido) {
        con.update("insert into metricacomponente (qtd_consumido,dt_metrica,componente_id) values");
    }

    public void enviarMetricaRede(Integer redeId, Long bytesRecebidosSegundo,
            Long bytesEnviadosSegundo, LocalDateTime dtMetrica) {
        con.update("insert into metricaredeinterface (bytes_recebidos_segundo,"
                + "bytes_enviados_segundo,dt_metrica,network_interface_id) values (?,?,?,?)",
                bytesRecebidosSegundo, bytesEnviadosSegundo, dtMetrica, redeId);
    }
}
