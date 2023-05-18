package slack.configuration;

import cashtech.jar.DataBase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.util.List;

public class SlackRepository {
    DataBase conexao = new DataBase();
    JdbcTemplate con = conexao.getConnection();

    public void pegarUrl(){
        List<String> query = con.query("select link from urlSlack", new SingleColumnRowMapper<>(String.class));
        Slack.setUrl(query.get(0));
    }
}
