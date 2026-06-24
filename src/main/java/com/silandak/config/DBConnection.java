package com.silandak.config;

/**
 *
 * @author A Ok
 */

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/silandak";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    public static Connection getConnection(){
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                System.out.println("Driver OK");
                    Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                    System.out.println("Koneksi Berhasil");
                    return conn;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
    }
}

    

