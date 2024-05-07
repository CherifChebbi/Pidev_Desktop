package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {

    private static MyDatabase instance;
    private final String URL = "jdbc:mysql://localhost:3306/nadajava";
    private final String USER = "root";
    private final String PASSWORD = "";
    private Connection connection;

    private MyDatabase () {
     try{
        connection= DriverManager.getConnection(URL,USER,PASSWORD);

        System.out.println("Connection etablished");

     } catch (SQLException e) {
         System.err.println(e.getMessage());
     }
    }
public static MyDatabase getInstance(){
        if(instance == null)
            instance = new MyDatabase();
        return instance;
}


    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
