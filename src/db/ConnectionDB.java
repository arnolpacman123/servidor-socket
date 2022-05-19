package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    String url = "jdbc:mysql://localhost:3306/distribuidos";
    String driver = "com.mysql.cj.jdbc.Driver";
    String user = "root";
    String password = "password";

    private static Connection connection = null;

    public ConnectionDB() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        if (connection == null)
            new ConnectionDB();
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {
                System.out.println("No se pudo cerrar conexion");
            }
        }
    }
}
