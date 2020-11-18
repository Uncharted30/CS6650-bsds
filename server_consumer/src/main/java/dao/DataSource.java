package dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import utils.ConsumerProperties;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    private static final String MYSQL_URL = ConsumerProperties.DB_URL;
    private static final String USERNAME = ConsumerProperties.DB_USERNAME;
    private static final String PASSWORD = ConsumerProperties.DB_PASSWORD;

    static {
        System.out.println(MYSQL_URL);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl(MYSQL_URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        ds = new HikariDataSource(config);
    }

    private DataSource() {
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
