package tn.esprit.application.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class myDatabase {
    private final String URL="jdbc:mysql://localhost:3306/nada";
    private final String USER="root";
    private final String PASSWORD="";
    private Connection connection;
    private static myDatabase instance;
    private myDatabase(){
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("connection established ");
        }catch (SQLException e){
            System.err.println(e.getMessage());

        }
    }
    public static myDatabase getInstance(){
        if(instance==null )
            instance = new myDatabase();

        return instance;

    }
    public Connection getConnection(){
        return connection;
    }
}
