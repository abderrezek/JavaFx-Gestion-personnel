package gestion.personnel.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private String DB_URL = "jdbc:mysql://localhost:3306/crud";
    private String DB_USER = "root";
    private String DB_PASSWORD = "";
    
    private DatabaseConnection() throws SQLException  {
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException ex) {
            System.out.println("Database Connection Creation Failed : " + ex.getMessage());
        }
    }
    
    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }

        return instance;
    }
    
    public Connection getConnection() {
        return connection;
    }
}
