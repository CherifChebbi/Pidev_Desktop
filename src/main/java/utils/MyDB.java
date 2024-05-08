package utils;
import java.sql.*;

public class MyDB {
    static String user = "root";
    static String password = "";
    static String url = "jdbc:mysql://localhost/pidevsymfony";
    static String driver = "com.mysql.cj.jdbc.Driver";

    public static Connection getCon(){
        Connection con = null;
        try {
            con = DriverManager.getConnection(url,user,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return con;
    }


}