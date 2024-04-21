package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static final String URL = "jdbc:mysql://localhost:3306/pidev_symf";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Instance unique de la classe DB
    private static final DB instance = new DB();

    private Connection connection;

    // Constructeur privé pour empêcher l'instanciation directe
    private DB() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to DB");
        } catch (SQLException e) {
            System.out.println("Failed to connect to DB: " + e.getMessage());
        }
    }

    // Méthode statique pour obtenir l'instance unique de DB
    public static DB getInstance() {
        return instance;
    }

    // Méthode pour récupérer la connexion à la base de données
    public Connection getConnection() {
        return connection;
    }

    // Méthode pour fermer la connexion à la base de données
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection to DB closed");
            } catch (SQLException e) {
                System.out.println("Failed to close connection to DB: " + e.getMessage());
            }
        }
    }
}
