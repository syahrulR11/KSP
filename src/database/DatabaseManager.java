/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author syahrul
 */
public class DatabaseManager {
    private Connection connection;
    private String url = "jdbc:mysql://127.0.0.1:3306/simpanpinjam";
    private String username = "root";
    private String password = "12345678";

    public Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(this.url, this.username, this.password);
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            throw e; // Propagate the exception to the caller
        }
    }

    public void close() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
        }
    }
}
