/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cashtech.jar;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.rede.Rede;
import com.github.britooo.looca.api.group.rede.RedeInterface;
import com.github.britooo.looca.api.group.rede.RedeInterfaceGroup;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author murilo
 */
public class DataBaseDocker {

        Looca looca = new Looca();

        Rede rede = looca.getRede();

        RedeInterfaceGroup redes = rede.getGrupoDeInterfaces();



    private String porta = "3306";

    private String servidor = "localhost";

    private String bancoDeDados = "cashtech";

    private String login = "root";

    private String senha = "3db01194";

    private JdbcTemplate connection;

    public DataBaseDocker() {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        dataSource.setUrl(String.format("jdbc:mysql://%s:%s/%s?useTimezone=true&serverTimezone=UTC", servidor, porta, bancoDeDados));

        dataSource.setUsername(login);
        dataSource.setPassword(senha);

        this.connection = new JdbcTemplate(dataSource);

    }

    public JdbcTemplate getConnection() {

        return connection;

    }
}
