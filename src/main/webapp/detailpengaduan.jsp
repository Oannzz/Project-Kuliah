<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.mycompany.silandakweb.Koneksi"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SILANDAK - Detail Laporan</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body class="bg-gray-100 font-sans min-h-screen flex justify-center items-start py-6 px-4">

    <div class="relative w-full max-w-md bg-white rounded-2xl shadow-xl border border-gray-200 overflow-hidden flex flex-col min-h-[820px]">
        
        <header class="bg-red-600 text-white p-4 shadow-md flex items-center justify-between">
            <div class="flex items-center gap-3">
                <a href="dashboard.jsp" class="w-8 h-8 rounded-lg flex items-center justify-center hover:bg-red-700 active:bg-red-800 transition-colors">
                    <i class="fa-solid fa-arrow-left text-lg"></i>
                </a>
                <div>
                    <h1 class="text-lg font-black tracking-wider">SILANDAK</h1>
                    <p class="text-[9px] text-red-100 font-medium">Sistem Layanan Dan Pengaduan Damkar</p>
                </div>
            </div>
            <div class="w-8 h-8 rounded-lg bg-white/10 flex items-center justify-center">
                <i class="fa-solid fa-fire-extinguisher text-sm"></i>
            </div>
        </header>

        <main class="p-4 flex-1 flex flex-col gap-4 overflow-y-auto">
            
            <%
                String idParam = request.getParameter("id");
                if (idParam != null && !idParam.isEmpty()) {
                    
                    String sql = "SELECT fp.*, p.nama_pelapor, p.no_telepon AS telp_pelapor " +
                                 "FROM form_pengaduan fp " +
                                 "JOIN pelapor p ON fp.id_pelapor = p.id_pelapor " +
                                 "WHERE fp.id_form_pengaduan = ?";
                                 
                    try (Connection conn = Koneksi.getConnection();
                         PreparedStatement ps = conn.prepareStatement(sql)) {
                        
                        ps.setInt(1, Integer.parseInt(idParam));
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                java.sql.Timestamp ts = rs.getTimestamp("waktu_kejadian");
                                String tanggalFormatted = new java.text.SimpleDateFormat("dd - MM - yyyy").format(ts);
                                String code = rs.getString("kode_pengaduan");
                                String deskripsi = rs.getString("deskripsi");
                                String alamat = rs.getString("alamat_kejadian");
                                
                                // Mengambil koordinat dan membuat link mapsUrl dengan aman
                                double latitude = rs.getDouble("latitude");
                                double longitude = rs.getDouble("longitude");
                                String mapsUrl = "https://www.google.com/maps?q=" + latitude + "," + longitude;
            %>
            
            <div class="flex justify-between items-baseline border-b border-gray-100 pb-1 mt-1">
                <h2 class="text-sm font-bold text-gray-800 tracking-wide">Pengaduan</h2>
                <span class="text-sm font-black font-mono text-gray-900 tracking-wider"><%= code %></span>
            </div>

            <div class="border border-gray-400 p-3 rounded bg-white min-h-[100px] text-xs text-gray-700 leading-relaxed shadow-sm">
                <%= deskripsi %>
            </div>

            <div class="border-b border-gray-100 pb-1 pt-1">
                <h2 class="text-sm font-bold text-gray-800 tracking-wide">Alamat</h2>
            </div>

            <div class="border border-gray-400 p-3 rounded bg-white min-h-[100px] text-xs text-gray-700 leading-relaxed shadow-sm">
                <strong>Pelapor:</strong> <%= rs.getString("nama_pelapor") %> (<%= rs.getString("telp_pelapor") %>)<br>
                <strong>Lokasi Kejadian:</strong> <%= alamat %>
            </div>

            <div class="text-right text-sm font-bold text-gray-800 tracking-wide pr-1 pt-1">
                <%= tanggalFormatted %>
            </div>

            <div class="grid grid-cols-2 gap-4">
                
                <div class="flex flex-col gap-2">
                    <!-- KODE BARU (Peta Aktif Berdasarkan Koordinat Database) -->
<div class="relative w-full border border-gray-300 rounded overflow-hidden aspect-video bg-gray-50 shadow-sm">
    <iframe 
        width="100%" 
        height="100%" 
        style="border:0;" 
        loading="lazy" 
        allowfullscreen 
        src="https://maps.google.com/maps?q=<%= latitude %>,<%= longitude %>&output=embed">
    </iframe>
</div>
                    <% if (mapsUrl != null && !mapsUrl.isEmpty()) { %>
                        <a href="<%= mapsUrl %>" target="_blank" class="w-full py-1.5 border border-gray-400 rounded bg-white text-center text-[10px] font-bold text-gray-700 hover:bg-gray-100 transition active:scale-[0.97] block">
                            <i class="fa-solid fa-location-crosshairs mr-1"></i> LIHAT LOKASI
                        </a>
                    <% } else { %>
                        <button disabled class="w-full py-1.5 border border-gray-300 rounded bg-gray-50 text-center text-[10px] font-bold text-gray-400 cursor-not-allowed">
                            LOKASI TIDAK ADA
                        </button>
                    <% } %>
                </div>

                <div class="flex flex-col gap-2 text-center">
                    <span class="text-xs font-bold text-gray-800 block">Bukti</span>
                    <div class="w-full border border-gray-400 rounded aspect-square bg-gray-50 flex flex-col items-center justify-center p-2 text-center">
                        <i class="fa-regular fa-image text-3xl text-gray-400 mb-2"></i>
                        <p class="text-[9px] text-gray-500 leading-tight">Tidak ada lampiran bukti gambar/video</p>
                    </div>
                </div>

            </div> 
            
            <div class="border-t border-gray-100 pt-5 mt-auto mb-2">
                <% 
                    String statusLaporan = rs.getString("status");
                    if ("masuk_sistem".equals(statusLaporan)) { 
                %>
                    <div class="grid grid-cols-2 gap-4">
                        <button type="button" onclick="aksiPenanganan('terima', '<%= idParam %>')" class="py-2.5 border-2 border-green-700 rounded text-xs font-black tracking-widest text-white bg-green-600 hover:bg-green-700 transition shadow-sm active:scale-[0.97]">
                            TERIMA
                        </button>
                        <button type="button" onclick="aksiPenanganan('tolak', '<%= idParam %>')" class="py-2.5 border-2 border-red-700 rounded text-xs font-black tracking-widest text-white bg-red-600 hover:bg-red-700 transition shadow-sm active:scale-[0.97]">
                            TOLAK
                        </button>
                    </div>
                <% 
                    } else if ("ditangani".equals(statusLaporan) || "selesai".equals(statusLaporan)) { 
                %>
                    <div class="w-full py-3 bg-green-100 border border-green-400 text-green-800 rounded-lg text-center font-bold text-xs tracking-wider uppercase shadow-sm">
                        <i class="fas fa-check-circle mr-1"></i> Pengaduan Telah Diterima / Ditangani
                    </div>
                <% 
                    } else if ("ditolak".equals(statusLaporan)) { 
                %>
                    <div class="w-full py-3 bg-red-100 border border-red-400 text-red-800 rounded-lg text-center font-bold text-xs tracking-wider uppercase shadow-sm">
                        <i class="fas fa-times-circle mr-1"></i> Pengaduan Telah Ditolak
                    </div>
                <% 
                    } 
                %>
            </div>
            <%
                        } else {
                            out.println("<div class='text-center py-8 text-gray-500 text-sm'>Data tidak ditemukan.</div>");
                        }
                    }
                } catch (Exception e) {
                    out.println("<div class='text-red-500 text-xs p-3 border border-red-200 bg-red-50 rounded'>Error: " + e.getMessage() + "</div>");
                }
            }
            %>
        </main>
        
        <div id="modalKonfirmasi" class="hidden absolute inset-0 bg-black/50 z-50 flex items-center justify-center p-4">
            <div class="bg-white rounded-xl shadow-2xl p-6 w-full max-w-sm text-center space-y-4">
                <div class="text-5xl mx-auto text-red-500">
                    <i class="fas fa-times-circle"></i>
                </div>
                <h3 class="text-lg font-bold text-gray-900">Tolak Pengaduan?</h3>
                <p class="text-sm text-gray-500">
                    Apakah Anda yakin? Anda akan diarahkan ke halaman pengisian alasan penolakan.
                </p>
                <div class="flex gap-3 pt-2">
                    <button onclick="tutupModal()" class="w-full py-2 bg-gray-200 text-gray-700 font-semibold rounded-lg hover:bg-gray-300 transition">
                        Batal
                    </button>
                    <button id="btnKonfirmasiTolak" class="w-full py-2 bg-red-600 text-white font-semibold rounded-lg hover:bg-red-700 transition">
                        Ya, Lanjutkan
                    </button>
                </div>
            </div>
        </div>
        
        <footer class="bg-gray-50 border-t border-gray-200 p-2 text-center text-[9px] text-gray-400">
            &copy; 2026 SILANDAK Mobile Web App
        </footer>
    </div>

    <script>
    let idLaporanTerpilih = "";
    function aksiPenanganan(tipe, id) {
        if (tipe === 'terima') {
            Swal.fire({
                title: 'Terima Pengaduan?',
                text: "Apakah Anda yakin ingin MENERIMA pengaduan ini?",
                icon: 'question',
                showCancelButton: true,
                confirmButtonColor: '#16a34a',
                cancelButtonColor: '#6b7280',
                confirmButtonText: 'Ya, Terima',
                cancelButtonText: 'Batal'
            }).then((result) => {
                if (result.isConfirmed) {
                    fetch('ProsesPengaduanServlet?aksi=terima&id=' + id, { method: 'GET' })
                    .then(response => response.text())
                    .then(data => {
                        if (data.trim() === "SUCCESS") {
                            Swal.fire({
                                icon: 'success',
                                title: 'Sukses!',
                                text: 'Pengaduan berhasil diterima.',
                                confirmButtonColor: '#16a34a'
                            }).then(() => {
                                window.location.href = 'dashboard.jsp';
                            });
                        } else {
                            Swal.fire('Gagal', 'Gagal memperbarui status di database.', 'error');
                        }
                    })
                    .catch(err => Swal.fire('Error', 'Terjadi kesalahan koneksi: ' + err, 'error'));
                }
            });
        } else if (tipe === 'tolak') {
            idLaporanTerpilih = id;
            bukaModal();
        }
    }

    function bukaModal() {
        const modal = document.getElementById('modalKonfirmasi');
        const btnKonfirmasi = document.getElementById('btnKonfirmasiTolak');
        
        btnKonfirmasi.onclick = function() {
            tutupModal();
            window.location.href = 'penolakanpengaduan.jsp?id=' + idLaporanTerpilih;
        };
        
        modal.classList.remove('hidden');
    }

    function tutupModal() {
        document.getElementById('modalKonfirmasi').classList.add('hidden');
    }
    </script>
</body>
</html>