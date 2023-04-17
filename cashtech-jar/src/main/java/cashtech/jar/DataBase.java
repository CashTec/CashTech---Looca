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

    // Desenvolvimento ou producao
    private String ambiente = "desenvolvimento";

    // Porta padrão 3306
    private String porta = "3307";

    // Servidor da Azure
    private String servidorNuvem = "svr-cashtech.database.windows.net";

    // Banco da Azure = bd-cashtech
    private String bancoDeDados = "cashtech";

    // Login da Azure = admin-cashtech
    private String login = "root";

    // Senha da Azure = #Gfgrupo10
    private String senha = "root";

    private JdbcTemplate connection;

    public DataBase() {
        BasicDataSource dataSource = new BasicDataSource();

        if (ambiente.equals("producao")) {

            dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            dataSource.setUrl(String.format("jdbc:sqlserver://"
                    + "%s:1433;"
                    + "database=%s;"
                    + "encrypt=true;"
                    + "trustServerCertificate=false;"
                    + "hostNameInCertificate=*.database.windows.net;", servidorNuvem, bancoDeDados));

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

    public String getAmbiente() {
        return ambiente;
    }

}
