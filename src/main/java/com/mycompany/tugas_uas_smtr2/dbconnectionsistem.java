/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tugas_uas_smtr2;

import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author MSI Modern
 */
public class dbconnectionsistem {

    private static final String URL      = "jdbc:mysql://127.0.0.1:3306/silandak";
    private static final String USER     = "root";
    private static final String PASSWORD = "";
 
    // ✅ Setiap pemanggilan getKoneksi() selalu menghasilkan koneksi BARU
    // Sehingga tidak ada masalah "connection closed" antar form
    public static Connection getKoneksi() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error membuat koneksi: " + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("Driver MySQL tidak ditemukan: " + e.getMessage());
            return null;
        }
    }
    
    
    
    
    
    
    
    
}
