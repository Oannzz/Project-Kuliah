package com.mycompany.silandak.model;

/**
 *
 * @author A Ok
 */
public class pengaduan {
    private int id_form_pengaduan;
    private String kode_pengaduan;
    private int id_pelapor;
    private String nama_pelapor;
    private String no_telepon;
    private String alamat_pelapor;
    private String klasifikasi;
    private String deskripsi;
    private String alamat_kejadian;
    private String status;
    private String waktu_kejadian;
    
    public pengaduan(){}
    
    //getter + setter variabel
    public int getid_form_pengaduan(){
        return id_form_pengaduan;}
    public void setid_form_pengaduan(int id_form_pengaduan){
        this.id_form_pengaduan = id_form_pengaduan;
    } 
    public String getkode_pengaduan(){
        return kode_pengaduan;}
    public void setkode_pengaduan(String kode_pengaduan){
        this.kode_pengaduan = kode_pengaduan;
    }
    public int getid_pelapor(){
        return id_pelapor;}
    public void setid_pelapor(int id_pelapor){
        this.id_pelapor = id_pelapor;
    }
    public String getnama_pelapor(){
        return nama_pelapor;}
    public void setnama_pelapor(String nama_pelapor){
        this.nama_pelapor = nama_pelapor;
    }
    public String getno_telepon(){
        return no_telepon;}
    public void setno_telepon(String no_telepon){
        this.no_telepon = no_telepon;
    }
    public String getalamat_pelapor(){
        return alamat_pelapor;}
    public void setalamat_pelapor(String alamat_pelapor){
        this.alamat_pelapor = alamat_pelapor;
    }
    public String getklasifikasi(){
        return klasifikasi;}
    public void setklasifikasi(String klasifikasi){
        this.klasifikasi = klasifikasi;
    }
    public String getdeskripsi(){
        return deskripsi;}
    public void setdeskripsi(String deskripsi){
        this.deskripsi = deskripsi;
    }
    public String getalamat_kejadian(){
        return alamat_kejadian;}
    public void setalamat_kejadian(String alamat_kejadian){
        this.alamat_kejadian = alamat_kejadian;
    }
    public String getstatus(){
        return status;}
    public void setstatus(String status){
        this.status = status;
    }
    public String getwaktu_kejadian(){
        return waktu_kejadian;}
    public void setwaktu_kejadian(String waktu_kejadian){
        this.waktu_kejadian = waktu_kejadian;
    }
}
