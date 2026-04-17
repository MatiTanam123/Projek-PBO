package com.pos.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/tokobuku";
    private static final String USER = "root";
    private static final String PASS = ""; 

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static String checkLogin(String username, String password) {
    try (Connection conn = getConnection()) {
        String sql = "SELECT role FROM user WHERE username=? AND password=?";
        var ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        var rs = ps.executeQuery();
        
        if (rs.next()) {
            return rs.getString("role"); 
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null; 
}
}
