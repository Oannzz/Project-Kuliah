package com.mycompany.silandakweb; // <--- SESUAIKAN dengan package Anda

import com.mycompany.silandakweb.Koneksi; // <--- SESUAIKAN dengan package Koneksi Anda
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Mengatur encoding agar karakter teks tidak rusak
        request.setCharacterEncoding("UTF-8");
        
        // 2. Mengambil data yang diinput oleh user dari register.jsp
        String nama = request.getParameter("nama");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Connection conn = null;
        PreparedStatement ps = null;

        try {
    // 1. Memanggil koneksi database
    conn = Koneksi.getConnection(); 
    
    // 2. QUERY YANG SUDAH DISESUAIKAN DENGAN TABEL ANDA:
    // Kolom id_petugas tidak perlu dimasukkan karena biasanya Auto Increment.
    // Kolom area_tugas dan id_operator kita beri nilai default atau kosong dahulu saat mendaftar.
    String sql = "INSERT INTO petugas_lapangan (nama_petugas, username, password, area_tugas, id_operator) VALUES (?, ?, ?, '-', 1)";
    
    ps = conn.prepareStatement(sql);
    ps.setString(1, nama);          // Mengisi nama_petugas
    ps.setString(2, username);      // Mengisi username
    ps.setString(3, password);      // Mengisi password
    
    // 3. Eksekusi query ke database
    int rowsInserted = ps.executeUpdate();
    
    if (rowsInserted > 0) {
        // Jika sukses, arahkan kembali ke login
        response.sendRedirect("index.jsp?status=registersuccess");
    } else {
        response.sendRedirect("register.jsp?error=failed");
    }
    
} catch (SQLException e) {
    e.printStackTrace(); 
    response.sendRedirect("register.jsp?error=db_error");
}
         finally {
            // 6. Menutup resource database secara aman agar server tidak leak/berat
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}