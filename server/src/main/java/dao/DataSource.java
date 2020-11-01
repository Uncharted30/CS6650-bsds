package dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    private static final String MYSQL_URL = System.getProperty("MYSQL_URL");
    private static final String USERNAME = System.getProperty("DB_USERNAME");
    private static final String PASSWORD = System.getProperty("DB_PASSWORD");

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
