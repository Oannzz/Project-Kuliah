/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.tugas_uas_smtr2;

/**
 *
 * @author MSI Modern
 */
public class DetailLaporanHasilPenangananOperator extends javax.swing.JFrame {

    private int idInstansiDinamis;
    private double currentLat;
    private double currentLng;

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DetailLaporanHasilPenangananOperator.class.getName());

    /**
     * Creates new form DetailLaporanHasilPenanganan
     */
    public DetailLaporanHasilPenangananOperator() {
        initComponents();

        jPanel1.removeAll();
        jPanel1.setLayout(new java.awt.BorderLayout());
        lbl_foto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_foto.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(lbl_foto, java.awt.BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

    }

    public DetailLaporanHasilPenangananOperator(String kodePengaduan, String deskripsi,
            String alamat, String tanggal, String namaInstansi, String shift, String lokasiMaps) {
        initComponents();
        setLocationRelativeTo(null);

        field_kd.setText(kodePengaduan);
        field_tgl.setText(tanggal != null ? tanggal.substring(0, 10) : "-");
        jTextArea1.setText(deskripsi != null ? deskripsi : "-");
        jTextArea2.setText(alamat != null ? alamat : "-");
        jLabel9.setText(namaInstansi != null ? namaInstansi : "Nama Operator");
        jLabel10.setText("Shift: " + (shift != null ? shift : "-"));

        jButton1.addActionListener(e -> dispose());
        jPanel1.removeAll();
        jPanel1.setLayout(new java.awt.BorderLayout());
        lbl_foto.setPreferredSize(new java.awt.Dimension(300, 300));
        lbl_foto.setMinimumSize(new java.awt.Dimension(300, 300));
        lbl_foto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_foto.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(lbl_foto, java.awt.BorderLayout.CENTER);

        jButton1.addActionListener(e -> dispose());
        jButton3.addActionListener(e -> cetakLaporan(kodePengaduan, deskripsi, alamat, tanggal, namaInstansi));

        // Pindahkan semua ke setelah frame visible agar ukuran label sudah ada
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        javax.swing.SwingUtilities.invokeLater(() -> {
            loadFotoBukti(kodePengaduan);

            if (lokasiMaps != null && !lokasiMaps.isEmpty()) {
                try {
                    String[] parts = lokasiMaps.split(",");
                    double lat = Double.parseDouble(parts[0].trim());
                    double lng = Double.parseDouble(parts[1].trim());

                    // Reset layout jPanel2
                    jPanel2.setLayout(new java.awt.BorderLayout(10, 10));
                    jPanel2.removeAll();

                    // Set ukuran peta eksplisit
                    jLabel3.setPreferredSize(new java.awt.Dimension(440, 250));
                    jLabel3.setMinimumSize(new java.awt.Dimension(440, 250));
                    jLabel3.setMaximumSize(new java.awt.Dimension(440, 250));
                    jLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.GRAY));
                    jPanel2.add(jLabel3, java.awt.BorderLayout.NORTH);

                    // Panel info bawah
                    javax.swing.JPanel infoPanel = new javax.swing.JPanel();
                    infoPanel.setLayout(new javax.swing.BoxLayout(infoPanel, javax.swing.BoxLayout.Y_AXIS));
                    infoPanel.setBackground(java.awt.Color.WHITE);

                    jButton2.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
                    jLabel7.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                    jLabel9.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                    jLabel10.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

                    infoPanel.add(javax.swing.Box.createVerticalStrut(15));
                    infoPanel.add(jButton2);
                    infoPanel.add(javax.swing.Box.createVerticalStrut(10));
                    infoPanel.add(jLabel7);
                    infoPanel.add(javax.swing.Box.createVerticalStrut(5));
                    infoPanel.add(jScrollPane2);
                    infoPanel.add(javax.swing.Box.createVerticalStrut(15));
                    infoPanel.add(jLabel9);
                    infoPanel.add(javax.swing.Box.createVerticalStrut(5));
                    infoPanel.add(jLabel10);

                    jPanel2.add(infoPanel, java.awt.BorderLayout.CENTER);
                    jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    jPanel2.revalidate();
                    jPanel2.repaint();

                    // Override tombol
                    for (java.awt.event.ActionListener al : jButton2.getActionListeners()) {
                        jButton2.removeActionListener(al);
                    }
                    jButton2.addActionListener(e -> {
                        Util.bukaMapsDiBrowser(
                                String.format(java.util.Locale.US,
                                        "https://www.google.com/maps?q=%f,%f", lat, lng)
                        );
                    });

                    // Load peta SETELAH layout selesai dirender
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        jLabel3.setSize(440, 250); // paksa ukuran setelah render
                        Util.tampilkanMapsPreview(jLabel3, lat, lng);
                    });

                } catch (Exception ex) {
                    jLabel3.setText("Koordinat tidak valid");
                    ex.printStackTrace();
                }
            } else {
                jLabel3.setText("Lokasi tidak tersedia");
            }
        });
    }

    private void loadFotoBukti(String kodePengaduan) {
        // Load foto dari laporan_penanganan
        System.out.println("=== CEK DB UNTUK KODE: [" + kodePengaduan + "]");
        try {
            java.sql.Connection conn = java.sql.DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/silandak", "root", ""
            );

            String sqlFoto = "SELECT lp.foto_media FROM laporan_penanganan lp "
                    + "JOIN form_pengaduan fp ON lp.id_form_pengaduan = fp.id_form_pengaduan "
                    + "WHERE fp.kode_pengaduan = ? "
                    + "ORDER BY lp.created_at DESC LIMIT 1";

            java.sql.PreparedStatement ps = conn.prepareStatement(sqlFoto);
            ps.setString(1, kodePengaduan);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String base64Data = rs.getString("foto_media");
                System.out.println("=== FOTO_MEDIA: " + (base64Data != null ? "ADA" : "NULL"));
                if (base64Data != null && !base64Data.isEmpty()) {
                    String dataStr = base64Data.contains(",") ? base64Data.split(",")[1] : base64Data;
                    byte[] imageBytes = java.util.Base64.getDecoder().decode(dataStr);
                    java.awt.Image img = new javax.swing.ImageIcon(imageBytes)
                            .getImage().getScaledInstance(300, 300, java.awt.Image.SCALE_SMOOTH);
                    lbl_foto.setIcon(new javax.swing.ImageIcon(img));
                    lbl_foto.setIcon(new javax.swing.ImageIcon(img));
                    lbl_foto.setIcon(new javax.swing.ImageIcon(img));
                    lbl_foto.setIcon(new javax.swing.ImageIcon(img));
                    lbl_foto.setSize(jPanel1.getWidth(), jPanel1.getHeight()); // tambah ini
                    lbl_foto.setPreferredSize(new java.awt.Dimension(jPanel1.getWidth(), jPanel1.getHeight()));
                    jPanel1.revalidate();
                    jPanel1.repaint();
                    System.out.println("=== lbl_foto size: " + lbl_foto.getWidth() + "x" + lbl_foto.getHeight());
                    lbl_foto.setPreferredSize(new java.awt.Dimension(300, 300));
                    lbl_foto.setMinimumSize(new java.awt.Dimension(300, 300));
                    jPanel1.revalidate();
                    jPanel1.repaint();
                    jPanel1.revalidate();
                    jPanel1.repaint();
                } else {
                    lbl_foto.setText("Belum ada foto penanganan");
                }
            } else {
                System.out.println("=== TIDAK ADA ROW");
                lbl_foto.setText("Belum ada laporan penanganan");
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("=== ERROR: " + e.getMessage());
            e.printStackTrace();
            lbl_foto.setText("Tidak ada foto");
        }
    }

    private void loadFotoAndMaps(String kodePengaduan) {
        try (java.sql.Connection conn = dbconnectionsistem.getKoneksi()) {

            String sql = "SELECT foto_bukti, latitude, longitude FROM form_pengaduan WHERE kode_pengaduan = ?";
            java.sql.PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, kodePengaduan);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                currentLat = rs.getDouble("latitude");   // ← simpan ke field
                currentLng = rs.getDouble("longitude");  // ← simpan ke field

                Util.tampilkanMapsPreview(jLabel3, currentLat, currentLng);

                // Aktifkan tombol setelah data tersedia
                jButton2.setEnabled(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            jButton2.setEnabled(false);
        }
    }

    private void cetakLaporan(String kode, String deskripsi, String alamat, String tanggal, String namaInstansi) {
        java.awt.print.PrinterJob job = java.awt.print.PrinterJob.getPrinterJob();
        job.setPrintable((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) {
                return java.awt.print.Printable.NO_SUCH_PAGE;
            }

            java.awt.Graphics2D g2 = (java.awt.Graphics2D) graphics;
            g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

            int y = 40;
            g2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
            g2.drawString("SILANDAK - Laporan Hasil Penanganan", 50, y);
            y += 30;
            g2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
            g2.drawString("Kode Pengaduan : " + kode, 50, y);
            y += 20;
            g2.drawString("Tanggal        : " + tanggal, 50, y);
            y += 20;
            g2.drawString("Alamat         : " + alamat, 50, y);
            y += 20;
            g2.drawString("Instansi       : " + namaInstansi, 50, y);
            y += 30;
            g2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
            g2.drawString("Deskripsi Penanganan:", 50, y);
            y += 20;
            g2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));

            for (String line : deskripsi.split("\n")) {
                g2.drawString(line, 50, y);
                y += 15;
            }

            return java.awt.print.Printable.PAGE_EXISTS;
        });

        if (job.printDialog()) {
            try {
                job.print();
            } catch (java.awt.print.PrinterException e) {
                javax.swing.JOptionPane.showMessageDialog(null, "Gagal cetak: " + e.getMessage());
            }
        }
    }

    // Misal variabel kodeLaporan didapat dari lemparan data dasbor (Contoh: "EM-0001")
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jButton3 = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator9 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        field_tgl = new javax.swing.JTextField();
        field_kd = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        lbl_foto = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1920, 1080));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 0, 0));
        jButton3.setText("Cetak Laporan");
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 782, -1, 50));
        getContentPane().add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 1594, 13));
        getContentPane().add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 110, 10, 1370));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Laporan Hasil Pengaduan");

        field_tgl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        field_tgl.addActionListener(this::field_tglActionPerformed);

        field_kd.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(lbl_foto, new org.netbeans.lib.awtextra.AbsoluteConstraints(207, 144, -1, -1));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Bukti");

        jTextArea1.setBackground(new java.awt.Color(255, 255, 255));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(field_kd, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(125, 125, 125)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(field_tgl, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(237, 237, 237)
                                .addComponent(jLabel11)))))
                .addContainerGap(157, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(field_kd, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(jLabel5))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(field_tgl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, 1110, 560));

        jPanel3.setBackground(new java.awt.Color(255, 51, 51));

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setText("Kembali");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("SILANDAK");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Sistem Layanan Pengaduan Damkar");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(569, 569, 569)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap(602, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1930, 110));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton2.setText("Lihat lokasi");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Alamat");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Shift");

        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextArea2.setLineWrap(true);
        jTextArea2.setRows(5);
        jTextArea2.setWrapStyleWord(true);
        jScrollPane2.setViewportView(jTextArea2);

        jLabel3.setText("jLabel3");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Nama Operator");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(109, 109, 109)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(179, 179, 179)
                        .addComponent(jLabel7))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(216, 216, 216)
                        .addComponent(jLabel3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(220, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(116, 116, 116)
                .addComponent(jButton2)
                .addGap(34, 34, 34)
                .addComponent(jLabel7)
                .addGap(36, 36, 36)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addGap(57, 57, 57))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1430, 110, 500, 930));

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(1092, Short.MAX_VALUE)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(327, 327, 327))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(95, Short.MAX_VALUE)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 925, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 916, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1430, 1030));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void field_tglActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_field_tglActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_field_tglActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
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
        java.awt.EventQueue.invokeLater(() -> new DetailLaporanHasilPenangananOperator().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField field_kd;
    private javax.swing.JTextField field_tgl;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JLabel lbl_foto;
    // End of variables declaration//GEN-END:variables
}
