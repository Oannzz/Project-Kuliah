/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tugas_uas_smtr2;

import java.sql.Connection;
import java.sql.DriverManager;
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
}
