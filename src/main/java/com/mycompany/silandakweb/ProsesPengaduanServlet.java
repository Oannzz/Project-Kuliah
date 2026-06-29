package com.mycompany.silandakweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ProsesPengaduanServlet", urlPatterns = {"/ProsesPengaduanServlet"})
public class ProsesPengaduanServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String aksi = request.getParameter("aksi");
        String idParam = request.getParameter("id");
        
        // LOG UNTUK KONSOL NETBEANS (Uji apakah data fetch masuk ke Servlet)
        System.out.println("========== SILANDAK SERVLET DEBUG ==========");
        System.out.println("Aksi yang diterima: " + aksi);
        System.out.println("ID Laporan yang diterima: " + idParam);
        
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        if (idParam != null && aksi != null && !idParam.isEmpty()) {
            try {
                int idForm = Integer.parseInt(idParam);
                String query = "";
                
                if ("terima".equals(aksi)) {
                    query = "UPDATE form_pengaduan SET status = 'ditangani' WHERE id_form_pengaduan = ?";
                } else if ("tolak".equals(aksi)) {
                    query = "UPDATE form_pengaduan SET status = 'ditolak' WHERE id_form_pengaduan = ?";
                }
                
                try (Connection conn = Koneksi.getConnection();
                     PreparedStatement ps = conn.prepareStatement(query)) {
                    
                    ps.setInt(1, idForm);
                    int rowsUpdated = ps.executeUpdate();
                    
                    System.out.println("Jumlah baris database yang ter-update: " + rowsUpdated);
                    
                    if (rowsUpdated > 0) {
                        out.print("SUCCESS");
                        System.out.println("Status: Berhasil mengirim respon SUCCESS");
                    } else {
                        out.print("FAILED_NO_ROWS");
                        System.out.println("Status: Gagal, tidak ada baris ID tersebut di database");
                    }
                }
            } catch (NumberFormatException e) {
                out.print("ERROR_INVALID_ID");
                System.out.println("Status Error: ID bukan angka valid");
            } catch (SQLException e) {
                out.print("ERROR_DATABASE: " + e.getMessage());
                System.out.println("Status Error SQL: " + e.getMessage());
            }
        } else {
            out.print("ERROR_MISSING_PARAMS");
            System.out.println("Status Error: Parameter aksi atau ID kosong!");
        }
        out.close();
        System.out.println("============================================");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}