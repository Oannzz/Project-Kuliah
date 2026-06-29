package com.mycompany.silandak.model;
/**
 *
 * @author A Ok
 */
public class Laporan {
    private String kode_pengaduan;
    private String kode_laporan;
    private String nama_operator;
    private String nama_instansi;
    private String nama_petugas;
    private String deskripsi_penanganan;
    private String alamat;
    private String foto_media;
    private String klasifikasi;
    
    public String getKode_pengaduan(){
        return kode_pengaduan;
    }
    public void setKode_pengaduan(
        String kode_pengaduan){
        this.kode_pengaduan=kode_pengaduan;
    }
        public String getKode_laporan(){
        return kode_laporan;
    }
    public void setKode_laporan(
        String kode_laporan){
        this.kode_laporan=kode_laporan;
    }
    public String getNama_operator(){
        return nama_operator;
    }
    public void setNama_operator(
        String nama_operator){
        this.nama_operator=nama_operator;
    }
    public String getNama_instansi(){
        return nama_instansi;
    }
    public void setNama_instansi(
        String nama_instansi){
        this.nama_instansi=nama_instansi;
    }
    public String getNama_petugas(){
        return nama_petugas;
    }
    public void setNama_petugas(
        String nama_petugas){
        this.nama_petugas=nama_petugas;
    }
    public String getDeskripsi_penanganan(){
        return deskripsi_penanganan;
    }
    public void setDeskripsi_penanganan(
        String deskripsi_penanganan){
        this.deskripsi_penanganan=deskripsi_penanganan;
    }
    public String getAlamat(){
        return alamat;
    }
    public void setAlamat(
        String alamat){
        this.alamat=alamat;
    }
    public String getFoto_media(){
        return foto_media;
    }
    public void setFoto_media(
        String foto_media){
        this.foto_media=foto_media;
    }
    public String getKlasifikasi(){
        return klasifikasi;
    }
    public void setKlasifikasi(
        String klasifikasi){
        this.klasifikasi=klasifikasi;
    }
}
