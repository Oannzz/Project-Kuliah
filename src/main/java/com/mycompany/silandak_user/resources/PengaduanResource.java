package com.mycompany.silandak_user.resources;

import com.silandak.config.DBConnection;
import com.mycompany.silandak.model.pengaduan;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author A Ok
 */

@Path("/pengaduan")
public class PengaduanResource {
    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test(){
        return "API BERHASIL";
    }
    @GET
    @Path("/riwayat")
    @Produces(MediaType.APPLICATION_JSON)
    public List<pengaduan> getRiwayat(){
        List<pengaduan> list = new ArrayList<>();
        
        //Query SQL data kejadian digabung nama pelapor
        String query = 
                "SELECT f.*, p.nama_pelapor FROM form_pengaduan f " + 
                "JOIN pelapor p ON f.id_pelapor = p.id_pelapor";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery()){
            
            while (rs.next()){
                pengaduan p = new pengaduan();
                
                p.setid_form_pengaduan(rs.getInt("id_form_pengaduan"));
                p.setid_pelapor(rs.getInt("id_pelapor"));
                p.setkode_pengaduan(rs.getString("kode_pengaduan"));
                p.setnama_pelapor(rs.getString("nama_pelapor"));
                p.setklasifikasi(rs.getString("klasifikasi"));
                p.setdeskripsi(rs.getString("deskripsi"));
                p.setalamat_kejadian(rs.getString("alamat_kejadian"));
                p.setstatus(rs.getString("status"));
                p.setwaktu_kejadian(rs.getString("waktu_kejadian"));
                
                list.add(p);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
