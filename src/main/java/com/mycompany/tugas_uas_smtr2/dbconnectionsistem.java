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

    private static Connection koneksi;

    public static Connection getKoneksi() {
        // cek apakah koneksi
        if (koneksi == null) {

            try {
                String url = "jdbc:mysql://127.0.0.1:3306/silandak";
                String user = "root";
                String password = "";
                Class.forName("com.mysql.cj.jdbc.Driver");
                koneksi = DriverManager.getConnection(url, user, password);
            } catch (SQLException t) {
                System.out.println("Error Membuat Koneksi");
            } catch (ClassNotFoundException ex) {
                System.getLogger(dbconnectionsistem.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
        return koneksi;
    }
    public static ImageIcon ambilGambarLangsung(String namaTabel, String namaKolomBlob, String kolomKunci, String nilaiKunci) {
        String sql = "SELECT " + namaKolomBlob + " FROM " + namaTabel + " WHERE " + kolomKunci + " = ?";
        String url = "jdbc:mysql://localhost:3306/silandak";
        
        try (Connection conn = DriverManager.getConnection(url, "root", "");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nilaiKunci);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    InputStream input = rs.getBinaryStream(namaKolomBlob);
                    
                    if (input != null) {
                        // Membaca aliran biner database LANGSUNG menjadi objek gambar di memori RAM
                        Image gambar = ImageIO.read(input);
                        
                        if (gambar != null) {
                            return new ImageIcon(gambar); // Sukses jadi objek gambar GUI
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Kembalikan null jika gagal atau gambar kosong
    }
    
    
    
    
    
    
    
}
