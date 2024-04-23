package utils;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DB {
    private static final String URL = "jdbc:mysql://localhost:3306/pidev_symf";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static final DB instance = new DB();
    private BasicDataSource dataSource;

    private DB() {
        dataSource = new BasicDataSource();
        dataSource.setUrl(URL);
        dataSource.setUsername(USER);
        dataSource.setPassword(PASSWORD);
        dataSource.setMinIdle(5); // Nombre minimum de connexions dans le pool
        dataSource.setMaxIdle(10); // Nombre maximum de connexions dans le pool
        dataSource.setMaxOpenPreparedStatements(100); // Nombre maximum de déclarations préparées dans le pool
    }

    public static DB getInstance() {
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
