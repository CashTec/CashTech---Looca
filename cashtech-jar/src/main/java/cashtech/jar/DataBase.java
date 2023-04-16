/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cashtech.jar;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author murilo
 */
public class DataBase {

    private String ambiente = "desenvolvimento";

    private String porta = "3307";

    private String servidorNuvem = "";

    private String bancoDeDados = "cashtech";

    private String login = "root";

    private String senha = "root";

    private JdbcTemplate connection;

    public DataBase() {
        BasicDataSource dataSource = new BasicDataSource();

        if (ambiente.equals("producao")) {
            dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            dataSource.setUrl(String.format("jdbc:sqlserver://%s/%s", servidorNuvem, bancoDeDados));

        } else {
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

            dataSource.setUrl(String.format("jdbc:mysql://localhost:%s/%s?useTimezone=true&serverTimezone=UTC", porta, bancoDeDados));

        }

        dataSource.setUsername(login);
        dataSource.setPassword(senha);


        this.connection = new JdbcTemplate(dataSource);

    }

    public JdbcTemplate getConnection() {

        return connection;

    }
}
