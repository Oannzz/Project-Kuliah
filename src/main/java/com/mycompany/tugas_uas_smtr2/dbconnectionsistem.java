/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tugas_uas_smtr2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public static void unduhBlobKePng(String namaTabel, String namaKolomBlob, String kolomKunci, String nilaiKunci, String namaFileTarget) {
        String sql = "SELECT " + namaKolomBlob + " FROM " + namaTabel + " WHERE " + kolomKunci + " = ?";
        String url = "jdbc:mysql://localhost:3306/silandak";
        
        try (Connection conn = DriverManager.getConnection(url, "root", "");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nilaiKunci);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    InputStream input = rs.getBinaryStream(namaKolomBlob);
                    
                    if (input != null) {
                        File fileOutput = new File(namaFileTarget);
                        try (FileOutputStream output = new FileOutputStream(fileOutput)) {
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = input.read(buffer)) != -1) {
                                output.write(buffer, 0, bytesRead);
                            }
                        }
                        System.out.println("Gambar berhasil dimuat sementara: " + fileOutput.getAbsolutePath());
                    } else {
                        System.out.println("Kolom blob kosong di database.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    
    
    
}
