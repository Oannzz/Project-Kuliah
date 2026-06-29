package com.mycompany.silandakweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String user = request.getParameter("username");
        String pass = request.getParameter("password");
        
        try (PrintWriter out = response.getWriter()) {
            
            String sql = "SELECT * FROM petugas_lapangan WHERE username = ? AND password = ?";
            
            try (Connection conn = Koneksi.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                
                ps.setString(1, user);
                ps.setString(2, pass);
                
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        HttpSession session = request.getSession();
                        session.setAttribute("id_petugas", rs.getInt("id_petugas"));
                        session.setAttribute("nama_petugas", rs.getString("nama_petugas"));
                        session.setAttribute("area_tugas", rs.getString("area_tugas"));
                        
                        out.println("<script>");
                        out.println("window.location='dashboard.jsp';");
                        out.println("</script>");
                    } else {
                        out.println("<script src='https://cdn.jsdelivr.net/npm/sweetalert2@11'></script>");
                        out.println("<script>");
                        out.println("window.onload = function() {");
                        out.println("    Swal.fire({");
                        out.println("        icon: 'error',");
                        out.println("        title: 'Login Gagal',");
                        out.println("        text: 'Username atau Password Salah!',");
                        out.println("        confirmButtonColor: '#b91c1c'");
                        out.println("    }).then(() => {");
                        out.println("        window.location='index.jsp';");
                        out.println("    });");
                        out.println("};");
                        out.println("</script>");
                    }
                }
                
            } catch (SQLException e) {
                out.println("<script src='https://cdn.jsdelivr.net/npm/sweetalert2@11'></script>");
                out.println("<script>");
                out.println("window.onload = function() {");
                out.println("    Swal.fire({");
                out.println("        icon: 'error',");
                out.println("        title: 'Kesalahan Database',");
                out.println("        text: '" + e.getMessage().replace("'", "\'") + "',");
                out.println("        confirmButtonColor: '#b91c1c'");
                out.println("    }).then(() => {");
                out.println("        window.location='index.jsp';");
                out.println("    });");
                out.println("};");
                out.println("</script>");
            }
        }
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

    @Override
    public String getServletInfo() {
        return "Servlet untuk menangani login aplikasi SILANDAK menggunakan MySQL";
    }
}
