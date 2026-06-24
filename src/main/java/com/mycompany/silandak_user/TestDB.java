package com.mycompany.silandak_user;

import
        com.silandak.config.DBConnection;
import java.sql.Connection;
/**
 *
 * @author A Ok
 */
public class TestDB {
    
    public static void main(String[]args){
        Connection conn = DBConnection.getConnection();
        if(conn != null) {
            System.out.println("Koneksi Berhasil");
        }else{
            System.out.println("Koneksi Gagal");
        }
        System.out.println(conn);
    }
}
