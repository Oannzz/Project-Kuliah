package com.mycompany.silandak_user.resources;

import com.silandak.config.DBConnection;
import com.mycompany.silandak.model.pengaduan;
import com.mycompany.silandak.model.Laporan;
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
    
    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public pengaduan
            getStatus(@QueryParam("kode") String kode){
                pengaduan p = null;
                String query=
                        "SELECT F.*, p.nama_pelapor " +
                        "FROM form_pengaduan f " +
                        "JOIN pelapor p ON f.id_pelapor = p.id_pelapor " +
                        "WHERE REPLACE(REPLACE(UPPER(f.kode_pengaduan),'-',''),'_','') = " +
                        "REPLACE(REPLACE(UPPER(?),'-',''),'_','')";
                
                try(Connection conn = DBConnection.getConnection();
                    PreparedStatement ps = conn.prepareStatement(query)){
                        kode = kode.toUpperCase().trim();
                        
                        System.out.println("Kode diterima =" +kode +"]");
                    ps.setString(1, kode);
                    
                    ResultSet rs= ps.executeQuery();
                    System.out.println("kode =" + kode);
                    if(rs.next()){
                        System.out.println("Data Ditemukan");
                        p = new pengaduan();
                        
                        p.setid_form_pengaduan(rs.getInt("id_form_pengaduan"));
                        p.setid_pelapor(rs.getInt("id_pelapor"));
                        p.setkode_pengaduan(rs.getString("kode_pengaduan"));
                        p.setnama_pelapor(rs.getString("nama_pelapor"));
                        p.setklasifikasi(rs.getString("klasifikasi"));
                        p.setdeskripsi(rs.getString("deskripsi"));
                        p.setalamat_kejadian(rs.getString("alamat_kejadian"));
                        p.setstatus(rs.getString("status"));
                        p.setwaktu_kejadian(rs.getString("waktu_kejadian"));
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                return p;
            }
    
    @POST
    @Path("/tambah")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String tambahPengaduan(
            @FormParam("nama_pelapor")
                String nama,
            @FormParam("no_telepon")
                String telepon,
            @FormParam("alamat_pelapor")
                String alamatPelapor,
            @FormParam("deskripsi")
                String deskripsi,
            @FormParam("waktu_kejadian")
                String waktuKejadian,
            @FormParam("alamat_kejadian")
                String alamat,
            @FormParam("latitude")
                String latitude,
            @FormParam("longitude")
                String longitude,
            @FormParam("foto_bukti")
                String foto
    ){
        Connection conn = null;
        try{
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            //Simpan Pelapor
            String sqlPelapor =
                    "INSERT INTO pelapor(nama_pelapor, no_telepon, alamat) VALUES (?,?,?)";
            PreparedStatement psPelapor = conn.prepareStatement(sqlPelapor, Statement.RETURN_GENERATED_KEYS);
                psPelapor.setString(1, nama);
                psPelapor.setString(2, telepon);
                psPelapor.setString(3, alamatPelapor);  
                psPelapor.executeUpdate();            
           
                ResultSet rs = psPelapor.getGeneratedKeys();
                rs.next();                
                int idPelapor = rs.getInt(1);
            
                //Generate Kode RNG
                String kode = "FRM-0001";
                String sqlKode = 
                        "SELECT kode_pengaduan " +
                        "FROM form_pengaduan " +
                        "WHERE kode_pengaduan LIKE 'FRM-%' " +
                        "ORDER BY id_form_pengaduan DESC LIMIT 1";
                
                PreparedStatement psKode = 
                        conn.prepareStatement(sqlKode);
                ResultSet rsKode =
                        psKode.executeQuery();
                
                if(rsKode.next()){
                    String kodeTerakhir = rsKode.getString("kode_pengaduan");
                    int nomor =
                            Integer.parseInt(kodeTerakhir.substring(4));
                    nomor++;
                    kode = String.format ("FRM-%04d", nomor);
                }
                
                //Simpan Pengaduan
                String sqlform = 
                        "INSERT INTO form_pengaduan(" +
                        "kode_pengaduan, " +
                        "id_pelapor, " +
                        "deskripsi, " +
                        "alamat_kejadian, " +
                        "latitude, " +
                        "longitude, " +
                        "foto_bukti, " +
                        "waktu_kejadian" +
                        ") VALUES (?,?,?,?,?,?,?,?)";
                
                PreparedStatement psForm = conn.prepareStatement(sqlform);
                    psForm.setString(1, kode);
                    psForm.setInt(2, idPelapor);
                    psForm.setString(3, deskripsi);
                    psForm.setString(4, alamat);
                    
                    if(latitude == null||latitude.trim().isEmpty()){
                        psForm.setNull(5, java.sql.Types.DOUBLE);
                    }else{
                        psForm.setDouble(5, Double.parseDouble(latitude));
                    }
                    if(longitude == null||longitude.trim().isEmpty()){
                        psForm.setNull(6, java.sql.Types.DOUBLE);
                    }else{
                        psForm.setDouble(6, Double.parseDouble(longitude));
                    }    
                    psForm.setString(7, foto);
                    
                    waktuKejadian=
                            waktuKejadian.replace("T", " ");
                    if(waktuKejadian.length() == 16){
                        waktuKejadian += ":00";
                    }
                    psForm.setString(8, waktuKejadian);
                    
                    psForm.executeUpdate();
                    conn.commit();
                    return "Laporan Berhasil Dibuat Dengan Kode :" + kode;
        }
        catch(Exception e){
            e.printStackTrace();
            
            if(conn != null){
                try{
                    conn.rollback();
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            return "Gagal Menyimpan Laporan";
        }
        finally{
            if(conn != null){
                try{
                    conn.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    
            
    @GET
    @Path("/laporan")
    @Produces(MediaType.APPLICATION_JSON)
    public Laporan getLaporan(@QueryParam("kode") String kode) {
        System.out.println("=== MASUK GET LAPORAN ===");
        System.out.println("kode = " + kode);

        if (kode == null || kode.trim().isEmpty()) {
        return null;
        }

        Laporan l = null;

        String query =
            "SELECT " +
            "lp.kode_laporan, " +
            "fp.kode_pengaduan, " +
            "lp.deskripsi_penanganan, " +
            "lp.alamat, " +
            "lp.foto_media, " +
            "o.nama_operator, " +
            "i.nama_instansi, " +
            "fp.klasifikasi, " +
            "pl.nama_petugas " +
            "FROM laporan_penanganan lp " +
            "LEFT JOIN form_pengaduan fp ON lp.id_form_pengaduan = fp.id_form_pengaduan " +
            "LEFT JOIN operator o ON lp.id_operator = o.id_operator " +
            "LEFT JOIN instansi i ON lp.id_instansi = i.id_instansi " +
            "LEFT JOIN verifikasi_petugas vp ON fp.id_form_pengaduan = vp.id_form_pengaduan " +
            "LEFT JOIN petugas_lapangan pl ON vp.id_petugas = pl.id_petugas " +
            "WHERE REPLACE(REPLACE(UPPER(fp.kode_pengaduan),'-',''),'_','') = " +
            "REPLACE(REPLACE(UPPER(?),'-',''),'_','')";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, kode.trim());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                l = new Laporan();

                l.setKode_laporan(rs.getString("kode_laporan"));
                l.setKode_pengaduan(rs.getString("kode_pengaduan"));
                l.setNama_operator(rs.getString("nama_operator"));
                l.setNama_instansi(rs.getString("nama_instansi"));
                l.setNama_petugas(rs.getString("nama_petugas"));
                l.setDeskripsi_penanganan(rs.getString("deskripsi_penanganan"));
                l.setAlamat(rs.getString("alamat"));
                l.setKlasifikasi(rs.getString("klasifikasi"));

                byte[] foto = rs.getBytes("foto_media");
                if (foto != null) {
                    l.setFoto_media(java.util.Base64.getEncoder().encodeToString(foto));
                }

                System.out.println("DATA DITEMUKAN");
            } else {
                System.out.println("DATA TIDAK DITEMUKAN");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }
}
