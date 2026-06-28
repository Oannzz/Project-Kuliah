/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.tugas_uas_smtr2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;

/**
 *
 * @author MSI Modern
 */
public class detailpengaduanterbaruOperator extends javax.swing.JFrame {

    private int idInstansiDinamis;
    private double currentLat;
    private double currentLng;

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(detailpengaduanterbaruOperator.class.getName());

    /**
     * Creates new form login
     */
    private String kodePengaduanData;
    private String deskripsiData;
    private String alamatData;
    private String waktuData;

    public detailpengaduanterbaruOperator(String kodePengaduan, String deskripsi, String alamat,
            String waktuKejadian, String namaOperator, String shift) {
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setResizable(false);

        jPanel2.setLayout(new java.awt.BorderLayout());
        lbl_foto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_foto.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel2.add(lbl_foto, java.awt.BorderLayout.CENTER);

        field_kd.setText(kodePengaduan != null ? kodePengaduan : "");
        area_detail.setText(deskripsi != null ? deskripsi : "");
        area_alamat.setText(alamat != null ? alamat : "");
        field_tgl.setText(waktuKejadian != null ? waktuKejadian : "");
        jLabel12.setText(namaOperator != null ? namaOperator : "Nama Operator");
        jLabel13.setText(shift != null ? shift : "Shift");

        this.kodePengaduanData = kodePengaduan;
        this.deskripsiData = deskripsi;
        this.alamatData = alamat;
        this.waktuData = waktuKejadian;

        // Load foto
        try {
            String base64Data = dbconnectionsistem.getFotoBase64(kodePengaduan);
            if (base64Data != null && !base64Data.isEmpty()) {
                String dataStr = base64Data.contains(",") ? base64Data.split(",")[1] : base64Data;
                byte[] imageBytes = java.util.Base64.getDecoder().decode(dataStr);
                java.awt.Image img = new javax.swing.ImageIcon(imageBytes)
                        .getImage().getScaledInstance(300, 300, java.awt.Image.SCALE_SMOOTH);
                lbl_foto.setIcon(new javax.swing.ImageIcon(img));
            } else {
                lbl_foto.setText("Tidak ada foto");
                lbl_foto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                lbl_foto.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.GRAY));
            }
        } catch (Exception e) {
            lbl_foto.setText("Tidak ada foto");
            lbl_foto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lbl_foto.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.GRAY));

        }
        // Reset layout jPanel1
        jPanel1.setLayout(new java.awt.BorderLayout(0, 10));
        jPanel1.removeAll();

// Bungkus jLabel6 dalam panel peta
        javax.swing.JPanel petaPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        petaPanel.setBackground(java.awt.Color.WHITE);
        petaPanel.setPreferredSize(new java.awt.Dimension(350, 220));
        jLabel6.setPreferredSize(new java.awt.Dimension(350, 220));
        jLabel6.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.GRAY));
        petaPanel.add(jLabel6, java.awt.BorderLayout.CENTER);

// Panel info bawah
        javax.swing.JPanel infoPanel = new javax.swing.JPanel();
        infoPanel.setLayout(new javax.swing.BoxLayout(infoPanel, javax.swing.BoxLayout.Y_AXIS));
        infoPanel.setBackground(java.awt.Color.WHITE);

        jButton1.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        jLabel2.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        jLabel12.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        jLabel13.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        infoPanel.add(javax.swing.Box.createVerticalStrut(10));
        infoPanel.add(jButton1);
        infoPanel.add(javax.swing.Box.createVerticalStrut(8));
        infoPanel.add(jLabel2);
        infoPanel.add(javax.swing.Box.createVerticalStrut(5));
        infoPanel.add(jScrollPane2);
        infoPanel.add(javax.swing.Box.createVerticalStrut(15));
        infoPanel.add(jLabel12);
        infoPanel.add(javax.swing.Box.createVerticalStrut(5));
        infoPanel.add(jLabel13);

        jPanel1.add(petaPanel, java.awt.BorderLayout.NORTH);
        jPanel1.add(infoPanel, java.awt.BorderLayout.CENTER);
        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel1.revalidate();
        jPanel1.repaint();

        setVisible(true);
        javax.swing.SwingUtilities.invokeLater(() -> {
            javax.swing.SwingUtilities.invokeLater(() -> {
                jLabel6.setSize(350, 220);
                loadFotoAndMaps(kodePengaduan);
            });
        });

        // Load peta — WAJIB di luar try-catch foto, selalu dipanggil
        setVisible(true);
        javax.swing.SwingUtilities.invokeLater(() -> {
            javax.swing.SwingUtilities.invokeLater(() -> {
                jLabel6.setSize(350, 220);
                jLabel6.setPreferredSize(new java.awt.Dimension(350, 220));
                jLabel6.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.GRAY));
                jPanel1.revalidate();
                jPanel1.repaint();
                loadFotoAndMaps(kodePengaduan);
            });
        });

    }

    private void loadFotoAndMaps(String kodePengaduan) {
        try {
            // Buat koneksi BARU, jangan pakai getKoneksi() yang shared
            java.sql.Connection conn = java.sql.DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/silandak", "root", ""
            );

            String sql = "SELECT latitude, longitude FROM form_pengaduan WHERE kode_pengaduan = ?";
            java.sql.PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, kodePengaduan);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                currentLat = rs.getDouble("latitude");
                currentLng = rs.getDouble("longitude");

                System.out.println("LAT: " + currentLat + ", LNG: " + currentLng);
                System.out.println("jLabel6 size: " + jLabel6.getWidth() + "x" + jLabel6.getHeight());

                Util.tampilkanMapsPreview(jLabel6, currentLat, currentLng);
                jButton1.setEnabled(true);
            } else {
                System.out.println("DATA TIDAK DITEMUKAN untuk kode: " + kodePengaduan);
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            jButton1.setEnabled(false);
        }
    }

    public detailpengaduanterbaruOperator() {
        this("", "", "", "", "", "");
    }

    // Aksi 1: Update klasifikasi di database menj
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btn_nonemergency = new javax.swing.JButton();
        btn_emergency = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        area_alamat = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lbl_foto = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        area_detail = new javax.swing.JTextArea();
        field_kd = new javax.swing.JTextField();
        field_tgl = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1920, 1080));
        setMinimumSize(new java.awt.Dimension(1920, 1080));
        setPreferredSize(new java.awt.Dimension(1920, 1080));

        btn_nonemergency.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn_nonemergency.setForeground(new java.awt.Color(0, 0, 0));
        btn_nonemergency.setText("NON EMERGENCY");
        btn_nonemergency.addActionListener(this::btn_nonemergencyActionPerformed);

        btn_emergency.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn_emergency.setForeground(new java.awt.Color(0, 0, 0));
        btn_emergency.setText("EMERGENCY");
        btn_emergency.addActionListener(this::btn_emergencyActionPerformed);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jButton1.setText("Lihat lokasi");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Alamat");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("Nama Operator");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("Shift");

        area_alamat.setColumns(20);
        area_alamat.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        area_alamat.setLineWrap(true);
        area_alamat.setRows(5);
        area_alamat.setWrapStyleWord(true);
        jScrollPane2.setViewportView(area_alamat);

        jLabel6.setText("jLabel6");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(144, 144, 144)
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(128, 128, 128)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(137, 137, 137)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jLabel12)
                .addGap(18, 18, 18)
                .addComponent(jLabel13)
                .addGap(128, 128, 128))
        );

        jPanel5.setBackground(new java.awt.Color(255, 51, 51));

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton2.setText("kembali");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Sistem Layanan Dan Pengaduan Damkar");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("SILANDAK");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addGap(441, 441, 441))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addGap(17, 17, 17))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addContainerGap())))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 1548, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 997, Short.MAX_VALUE)
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("bukti");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        lbl_foto.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(238, 238, 238)
                .addComponent(lbl_foto))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(198, 198, 198)
                .addComponent(lbl_foto))
        );

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Detail Pengaduan");

        area_detail.setColumns(20);
        area_detail.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        area_detail.setLineWrap(true);
        area_detail.setRows(5);
        area_detail.setWrapStyleWord(true);
        jScrollPane1.setViewportView(area_detail);

        field_kd.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        field_tgl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(field_kd, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(172, 172, 172)
                        .addComponent(field_tgl, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 718, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(152, 152, 152)
                        .addComponent(jLabel3)))
                .addContainerGap(181, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(field_kd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(field_tgl, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(61, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 1753, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(734, 734, 734)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(726, 726, 726)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(46, 46, 46)
                                                .addComponent(btn_emergency)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btn_nonemergency)
                                        .addGap(130, 130, 130)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 264, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 363, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(133, 133, 133)
                                .addComponent(jLabel4))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(112, 112, 112)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(35, 35, 35)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(37, 37, 37)
                                        .addComponent(jLabel1)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(32, 32, 32)
                                        .addComponent(btn_nonemergency))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(24, 24, 24)
                                        .addComponent(btn_emergency)))))
                        .addGap(0, 189, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(77, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_nonemergencyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nonemergencyActionPerformed
        // TODO add your handling code here:

        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Tandai pengaduan ini sebagai NON-EMERGENCY?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (konfirmasi != JOptionPane.YES_OPTION) {
            return;
        }

        Connection conn = dbconnectionsistem.getKoneksi();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Koneksi database gagal!");
            return;
        }

        try {
            String sqlUpdate = "UPDATE form_pengaduan SET klasifikasi = 'non_emergency' WHERE kode_pengaduan = ?";
            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
            psUpdate.setString(1, kodePengaduanData);
            psUpdate.executeUpdate();

            for (java.awt.Window window : java.awt.Window.getWindows()) {
                if (window instanceof C2dasbor_operator && window.isVisible()) {
                    C2dasbor_operator operator = (C2dasbor_operator) window;
                    operator.hapusBarisDariTabel(kodePengaduanData);
                    break;
                }
            }

            JOptionPane.showMessageDialog(this, "Berhasil diupdate ke NON-EMERGENCY.");
            this.dispose();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal: " + e.getMessage());
        }

    }//GEN-LAST:event_btn_nonemergencyActionPerformed

    private void btn_emergencyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_emergencyActionPerformed
        // TODO add your handling code here:
        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Tandai pengaduan ini sebagai EMERGENCY?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (konfirmasi != JOptionPane.YES_OPTION) {
            return;
        }

        Connection conn = dbconnectionsistem.getKoneksi();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Koneksi database gagal!");
            return;
        }

        try {
            String sqlUpdate = "UPDATE form_pengaduan SET klasifikasi = 'emergency' WHERE kode_pengaduan = ?";
            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
            psUpdate.setString(1, kodePengaduanData);
            psUpdate.executeUpdate();

            for (java.awt.Window window : java.awt.Window.getWindows()) {
                if (window instanceof C2dasbor_operator && window.isVisible()) {
                    C2dasbor_operator operator = (C2dasbor_operator) window;
                    operator.hapusBarisDariTabel(kodePengaduanData);
                    break;
                }
            }

            JOptionPane.showMessageDialog(this, "Berhasil diupdate ke EMERGENCY.");
            this.dispose();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal: " + e.getMessage());
        }


    }//GEN-LAST:event_btn_emergencyActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (currentLat != 0 && currentLng != 0) {
            Util.bukaMapsDiBrowser(
                    String.format(java.util.Locale.US,
                            "https://www.google.com/maps?q=%f,%f", currentLat, currentLng)
            );
        } else {
            try {
                String url = "https://www.google.com/maps/search/"
                        + java.net.URLEncoder.encode(alamatData != null ? alamatData : "", "UTF-8");
                java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Tidak bisa buka maps: " + e.getMessage());
            }
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        dispose();


    }//GEN-LAST:event_jButton2ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new detailpengaduanterbaruOperator().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea area_alamat;
    private javax.swing.JTextArea area_detail;
    private javax.swing.JButton btn_emergency;
    private javax.swing.JButton btn_nonemergency;
    private javax.swing.JTextField field_kd;
    private javax.swing.JTextField field_tgl;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lbl_foto;
    // End of variables declaration//GEN-END:variables
}
