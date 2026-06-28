/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.tugas_uas_smtr2;

import com.mycompany.tugas_uas_smtr2.Util;
import com.mycompany.tugas_uas_smtr2.Util;
import com.mycompany.tugas_uas_smtr2.dbconnectionsistem;
import com.mycompany.tugas_uas_smtr2.dbconnectionsistem;

/**
 *
 * @author MSI Modern
 */
public class DetailPengaduanTerbaruInstansi extends javax.swing.JFrame {
    private int idInstansiDinamis;
    private double currentLat;  
    private double currentLng;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DetailPengaduanTerbaruInstansi.class.getName());

    /**
     * Creates new form DetailPengaduanTerbaruInstansi
     */
    public DetailPengaduanTerbaruInstansi() {
        initComponents();
       
    }    
    
    private String kodePengaduanData;
    private String deskripsiData;
    private String alamatData;
    private String waktuData;
    private String namaInstansiData;
    private String shiftData;
    
   
    
    
    public DetailPengaduanTerbaruInstansi(String kodePengaduan, String deskripsi, String alamat,
                       String waktuKejadian, int idIns,String nama, String shift
                       ) {
        initComponents();
        field_kode.setText(kodePengaduan);
        area_detail.setText(deskripsi);
        area_alamat.setText(alamat);
        field_tgl.setText(waktuKejadian);
        
        this.kodePengaduanData = kodePengaduan;
        this.deskripsiData = deskripsi;
        this.alamatData = alamat;
        this.waktuData = waktuKejadian;
        this.idInstansiDinamis = idIns;
        this.namaInstansiData   = nama;   // ✅ simpan
        this.shiftData          = shift;
        
        setInfoInstansi(nama, shift);
        
        btn_lihat_lokasi.setEnabled(false);
        loadFotoPengaduan(kodePengaduan);
        javax.swing.SwingUtilities.invokeLater(() -> loadFotoAndMaps(kodePengaduan));
}
    private void loadFotoPengaduan(String kodePengaduan) {
    try  {
            // Buat koneksi BARU, jangan pakai getKoneksi() yang shared
            java.sql.Connection conn = java.sql.DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/silandak", "root", ""
            );

            String sql = "SELECT foto_bukti FROM form_pengaduan WHERE kode_pengaduan = ?";
            java.sql.PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, kodePengaduan);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String base64Data = rs.getString("foto_bukti");
                System.out.println("Data foto: " + (base64Data != null ? base64Data.substring(0, 30) : "NULL"));

                if (base64Data != null && base64Data.startsWith("data:image")) {
                    String base64 = base64Data.substring(base64Data.indexOf(",") + 1);
                    int embeddedDataUrl = base64.indexOf("data:");
                    if (embeddedDataUrl > 0) {
                        base64 = base64.substring(0, embeddedDataUrl);
                    }
                    byte[] imageBytes = java.util.Base64.getMimeDecoder().decode(base64);
                    java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(
                            new java.io.ByteArrayInputStream(imageBytes)
                    );
                    if (img != null) {
                        java.awt.Image scaled = img.getScaledInstance(300, 250, java.awt.Image.SCALE_SMOOTH);
                        lbl_foto.setIcon(new javax.swing.ImageIcon(scaled));
                        lbl_foto.setText("");
                    } else {
                        lbl_foto.setText("Gambar rusak");
                    }
                } else {
                    lbl_foto.setText("Foto tidak tersedia");
                }
            } else {
                lbl_foto.setText("Data tidak ditemukan");
            }
            rs.close();
            ps.close();
            conn.close(); // tutup koneksi baru ini

        } catch (Exception e) {
            lbl_foto.setText("Error: " + e.getMessage());
            System.out.println("ERROR loadFotoBukti: " + e.getMessage());
            e.printStackTrace();
        }
        
        
}
    private void loadFotoAndMaps(String kodePengaduan){
         String url = "jdbc:mysql://localhost:3306/silandak";
    try (java.sql.Connection conn = java.sql.DriverManager.getConnection(url, "root", "")) {

        String sql = "SELECT foto_bukti, latitude, longitude FROM form_pengaduan WHERE kode_pengaduan = ?";
        java.sql.PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, kodePengaduan);
        java.sql.ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            

            currentLat = rs.getDouble("latitude");   // ← simpan ke field
            currentLng = rs.getDouble("longitude");  // ← simpan ke field

            Util.tampilkanMapsPreview(lbl_maps, currentLat, currentLng);

            // Aktifkan tombol setelah data tersedia
            btn_lihat_lokasi.setEnabled(true);
        }

    } catch (Exception e) {
        e.printStackTrace();
        btn_lihat_lokasi.setEnabled(false);
    }
}
    
    public void setInfoInstansi(String nama, String shift) {
        jLabel8.setText(nama);
        jLabel9.setText("Shift: " + shift);
    }
    
    public void updateStatusDitangani(String kodePengaduan) {
    // Menggunakan URL koneksi manual mandiri sesuai database Anda
    String url = "jdbc:mysql://localhost:3306/silandak";
    
    try (java.sql.Connection conn = java.sql.DriverManager.getConnection(url, "root", "")) {
        
        // Query SQL untuk mengubah status pengaduan
        String sql = "UPDATE form_pengaduan SET status = ? WHERE kode_pengaduan = ?";
        java.sql.PreparedStatement ps = conn.prepareStatement(sql);
        
        ps.setString(1, "ditangani");
        ps.setString(2, kodePengaduan);
        
        int barisTerupdate = ps.executeUpdate();
        
        if (barisTerupdate > 0) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                    "Berhasil! Status pengaduan " + kodePengaduan + " kini menjadi Ditangani.");
            // Tidak ada syntax dispose() atau pindah halaman di sini, user tetap berada di form ini
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Gagal memperbarui status. Kode pengaduan tidak ditemukan.");
        }
        
        ps.close();
    } catch (Exception e) {
        e.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, "Error Database: " + e.getMessage());
    }
}
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        area_detail = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        lbl_foto = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btn_lihat_lokasi = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        area_alamat = new javax.swing.JTextArea();
        lbl_maps = new javax.swing.JLabel();
        btn_buat = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        field_kode = new javax.swing.JTextField();
        field_tgl = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btn_respon = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1920, 1080));
        setResizable(false);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setText("Deskripsi Pengaduan");

        area_detail.setColumns(20);
        area_detail.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        area_detail.setLineWrap(true);
        area_detail.setRows(5);
        area_detail.setWrapStyleWord(true);
        jScrollPane1.setViewportView(area_detail);

        jPanel1.setPreferredSize(new java.awt.Dimension(400, 400));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(lbl_foto)
                .addContainerGap(331, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(316, Short.MAX_VALUE)
                .addComponent(lbl_foto)
                .addGap(84, 84, 84))
        );

        jPanel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        btn_lihat_lokasi.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_lihat_lokasi.setText("Lihat Lokasi");
        btn_lihat_lokasi.addActionListener(this::btn_lihat_lokasiActionPerformed);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel6.setText("Alamat");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel8.setText("Nama ");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel9.setText("Shift");

        area_alamat.setColumns(20);
        area_alamat.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        area_alamat.setLineWrap(true);
        area_alamat.setRows(5);
        area_alamat.setWrapStyleWord(true);
        jScrollPane2.setViewportView(area_alamat);

        lbl_maps.setPreferredSize(new java.awt.Dimension(300, 300));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(156, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(163, 163, 163))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(lbl_maps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(125, 125, 125)
                        .addComponent(btn_lihat_lokasi))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(59, Short.MAX_VALUE)
                .addComponent(lbl_maps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_lihat_lokasi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addGap(24, 24, 24)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addGap(35, 35, 35))
        );

        btn_buat.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn_buat.setText("Buat Laporan");
        btn_buat.addActionListener(this::btn_buatActionPerformed);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setText("Bukti");

        field_kode.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        field_kode.addActionListener(this::field_kodeActionPerformed);

        field_tgl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        field_tgl.addActionListener(this::field_tglActionPerformed);

        jPanel3.setBackground(new java.awt.Color(237, 28, 36));

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jButton1.setText("Kembali");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("SILANDAK");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Sistem Layanan Dan Pengaduan Damkar");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jButton1)
                .addGap(519, 519, 519)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        btn_respon.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn_respon.setText("Respon Pengaduan");
        btn_respon.addActionListener(this::btn_responActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(11, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(field_kode, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(field_tgl, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 860, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(243, 243, 243)
                                .addComponent(jLabel10))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(68, 68, 68)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addComponent(btn_buat)
                        .addGap(297, 297, 297)
                        .addComponent(btn_respon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(125, 125, 125))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 123, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(field_kode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(field_tgl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(50, 50, 50)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_respon, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_buat, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(144, 144, 144))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void field_kodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_field_kodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_field_kodeActionPerformed

    private void field_tglActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_field_tglActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_field_tglActionPerformed

    private void btn_buatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buatActionPerformed
        //// 1. Buat objek dari class halaman tujuan yang ingin dibuka
        String kodePengaduanData = field_kode.getText();
        String deskripsiData = area_detail.getText();
        String alamatData = area_alamat.getText();
        String waktuData = field_tgl.getText();
        
        
        int idInstansi = this.idInstansiDinamis;
        
        
        isilaporanhasilpenangananInstansi detailForm = new isilaporanhasilpenangananInstansi(kodePengaduanData, deskripsiData, alamatData, waktuData, idInstansi,this.namaInstansiData, // ✅ ikut dikirim
        this.shiftData); // Ganti dengan nama class Frame tujuanmu

// 2. Tampilkan halaman tujuan tersebut ke layar
    detailForm.setVisible(true);

// 3. Tutup dan hancurkan (close/destroy) halaman yang sekarang sedang aktif
    this.dispose();
        
    }//GEN-LAST:event_btn_buatActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        DasborInstansi1 keluar = new DasborInstansi1( idInstansiDinamis);
        keluar.setVisible(true);
        keluar.setInfoInstansi(this.namaInstansiData, this.shiftData);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btn_lihat_lokasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_lihat_lokasiActionPerformed
        // TODO add your handling code here:
        
        Util.bukaMapsDiBrowser(
        String.format(java.util.Locale.US,
            "https://www.google.com/maps?q=%f,%f", 
            currentLat, currentLng)
    );
    }//GEN-LAST:event_btn_lihat_lokasiActionPerformed

    private void btn_responActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_responActionPerformed
        // TODO add your handling code here:
                                                 
    // Mengambil nilai kode pengaduan langsung dari field_kode di layar
    String kodePengaduan = field_kode.getText().trim();
    
    if (!kodePengaduan.isEmpty()) {
        // Tampilkan dialog konfirmasi tindakan
        int konfirmasi = javax.swing.JOptionPane.showConfirmDialog(this, 
                "Apakah Anda yakin ingin merespon dan menangani pengaduan dengan kode: " + kodePengaduan + "?", 
                "Konfirmasi Tindakan", javax.swing.JOptionPane.YES_NO_OPTION);
                
        if (konfirmasi == javax.swing.JOptionPane.YES_OPTION) {
            // Jalankan fungsi update status
            updateStatusDitangani(kodePengaduan);
        }
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "Kode pengaduan tidak ditemukan atau kosong!");
    }

    }//GEN-LAST:event_btn_responActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new DetailPengaduanTerbaruInstansi().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea area_alamat;
    private javax.swing.JTextArea area_detail;
    private javax.swing.JButton btn_buat;
    private javax.swing.JButton btn_lihat_lokasi;
    private javax.swing.JButton btn_respon;
    private javax.swing.JTextField field_kode;
    private javax.swing.JTextField field_tgl;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_foto;
    private javax.swing.JLabel lbl_maps;
    // End of variables declaration//GEN-END:variables
}
