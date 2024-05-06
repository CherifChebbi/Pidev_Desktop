package services;

import java.sql.*;

public class DatabaseService {
    private Connection connection;

    public DatabaseService() {
        // Initialize connection in the constructor
        initializeConnection();
    }

    private void initializeConnection() {
        // Open database connection
        // Handle any exceptions that may occur
        try {
            // Your code to open a database connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/database", "username", "password");
        } catch (SQLException e) {
            // Handle connection initialization failure
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        // Close database connection
        // Handle any exceptions that may occur
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            // Handle connection closure failure
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        // Execute SQL query
        // Ensure connection is open before executing query
        if (connection == null || connection.isClosed()) {
            initializeConnection();
        }

        // Execute query and return result set
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }

    // Other database operations methods can be added here
}
