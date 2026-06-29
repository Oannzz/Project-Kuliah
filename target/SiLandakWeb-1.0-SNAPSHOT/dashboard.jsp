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
    <title>SILANDAK - Dashboard Petugas</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body class="bg-gray-100 font-sans min-h-screen flex justify-center items-start py-6 px-4">

    <div class="relative w-full max-w-md bg-white rounded-2xl shadow-xl border border-gray-200 overflow-hidden flex flex-col min-h-[800px]">
        
        <div id="sidebarOverlay" onclick="toggleSidebar()" class="hidden absolute inset-0 bg-black/40 z-40 transition-opacity"></div>
        <div id="sidebarMenu" class="absolute top-0 left-0 h-full w-[260px] bg-white shadow-2xl z-50 transform -translate-x-full transition-transform duration-300 ease-in-out flex flex-col border-r border-gray-200">
            <div class="p-4 flex justify-end bg-gray-50 border-b border-gray-100">
                <button onclick="toggleSidebar()" class="text-gray-500 hover:text-red-600 p-1">
                    <i class="fa-solid fa-xmark text-lg"></i>
                </button>
            </div>
           <div class="flex-1 flex flex-col items-center pt-8 px-6 text-center">
    <div class="w-32 h-32 border-2 border-gray-400 bg-gray-50 flex items-center justify-center mb-4 rounded-md overflow-hidden text-gray-300">
    <i class="fa-regular fa-user text-5xl text-gray-400"></i>
</div>
    
    <h3 class="text-lg font-bold text-gray-900 tracking-wide">
        <%= session.getAttribute("nama_petugas") != null ? session.getAttribute("nama_petugas") : "Nama Petugas" %>
    </h3>
    <p class="text-sm text-gray-500 mt-1">
        <%= session.getAttribute("area_tugas") != null ? session.getAttribute("area_tugas") : "Area Tugas" %>
    </p>
</div>

<div class="p-6 border-t border-gray-100 bg-gray-50 flex flex-col items-center gap-3">
    <img src="images/icon_logout.png" alt="Logout Icon" class="w-8 h-8 object-contain opacity-70">
    
    <button onclick="prosesLogout()" class="px-5 py-1.5 border border-red-400 rounded-lg text-xs font-bold text-red-600 bg-white shadow-sm hover:bg-red-50 transition-colors w-full text-center">
        Log OutPetugas Lapangan
    </button>
</div>
        </div>

        <header class="bg-red-600 text-white p-4 shadow-md flex items-center gap-3">
            <button onclick="toggleSidebar()" class="p-2 -ml-2 rounded-lg hover:bg-red-700 active:bg-red-800 transition-colors focus:outline-none">
                <i class="fa-solid fa-bars text-lg"></i>
            </button>
            <div>
                <h1 class="text-xl font-black tracking-wider flex items-center gap-2">SILANDAK</h1>
                <p class="text-[10px] text-red-100 font-medium tracking-wide">Sistem Layanan Dan Pengaduan Damkar</p>
            </div>
        </header>

        <main class="p-4 flex-1 flex flex-col gap-6 overflow-y-auto">

            <section class="bg-gray-50 p-3 rounded-xl border border-gray-200 shadow-sm">
                <div class="flex justify-between items-center mb-3 gap-2">
                    <h2 class="text-sm font-bold text-gray-800 tracking-wide">Pengaduan Terbaru</h2>
                    <div class="relative flex-1 max-w-[160px]">
                        <input type="text" id="searchTerbaru" onkeyup="filterTable('searchTerbaru', 'tableTerbaru')" 
                            placeholder="Cari Pengaduan" 
                            class="w-full pl-7 pr-2 py-1 text-xs border border-gray-300 rounded-lg focus:outline-none focus:ring-1 focus:ring-red-500">
                        <i class="fa-solid fa-magnifying-glass absolute left-2 top-2 text-gray-400 text-[10px]"></i>
                    </div>
                </div>

                <div class="overflow-x-auto">
                    <table id="tableTerbaru" class="min-w-full table-auto text-left text-xs text-gray-600">
                        <thead class="bg-gray-100 text-[10px] uppercase text-gray-700 font-bold border-b border-gray-200">
                            <tr>
                                <th class="px-2 py-2">Pengaduan</th>
                                <th class="px-2 py-2">Tanggal</th>
                                <th class="px-2 py-2">Kode</th>
                                <th class="px-2 py-2 text-center">Aksi</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                // Query data pengaduan yang aktif/sedang berjalan
                                String sqlTerbaru = "SELECT * FROM form_pengaduan WHERE status = 'masuk_sistem' ORDER BY waktu_kejadian DESC";
                                try (Connection conn = Koneksi.getConnection();
                                     PreparedStatement ps = conn.prepareStatement(sqlTerbaru);
                                     ResultSet rs = ps.executeQuery()) {
                                    
                                    boolean adaData = false;
                                    while (rs.next()) {
                                        adaData = true;
                                        // Format tanggal ke DD-MM-YYYY secara ringkas
                                        java.sql.Timestamp ts = rs.getTimestamp("waktu_kejadian");
                                        String tanggalFormated = new java.text.SimpleDateFormat("dd-MM-yyyy").format(ts);
                            %>
                            <tr class="border-b border-gray-200 hover:bg-gray-50 transition">
                                <td class="px-2 py-2.5 font-medium text-gray-900 max-w-[100px] truncate"><%= rs.getString("deskripsi") %></td>
                                <td class="px-2 py-2.5 whitespace-nowrap"><%= tanggalFormated %></td>
                                <td class="px-2 py-2.5 font-mono text-red-600 font-bold"><%= rs.getString("kode_pengaduan") %></td>
                                <td class="px-2 py-2.5 text-center">
                                   <a href="detailpengaduan.jsp?id=<%= rs.getInt("id_form_pengaduan") %>" class="inline-block hover:scale-110 transition-transform p-1">
    <img src="images/icon_search.png" alt="Detail" class="w-5 h-5 object-contain">
</a>
                                </td>
                            </tr>
                            <%
                                    }
                                    if(!adaData) {
                                        out.println("<tr><td colspan='4' class='text-center py-4 text-gray-400 italic'>Tidak ada pengaduan aktif</td></tr>");
                                    }
                                } catch (Exception e) {
                                    out.println("<tr><td colspan='4' class='text-red-500 py-2'>Error: " + e.getMessage() + "</td></tr>");
                                }
                            %>
                        </tbody>
                    </table>
                </div>
            </section>

            <section class="bg-gray-50 p-3 rounded-xl border border-gray-200 shadow-sm flex-1 flex flex-col">
                <div class="flex justify-between items-center mb-3 gap-2">
                    <h2 class="text-sm font-bold text-gray-800 tracking-wide">Riwayat Pengaduan</h2>
                    <div class="relative flex-1 max-w-[160px]">
                        <input type="text" id="searchRiwayat" onkeyup="filterTable('searchRiwayat', 'tableRiwayat')" 
                            placeholder="Cari Pengaduan" 
                            class="w-full pl-7 pr-2 py-1 text-xs border border-gray-300 rounded-lg focus:outline-none focus:ring-1 focus:ring-red-500">
                        <i class="fa-solid fa-magnifying-glass absolute left-2 top-2 text-gray-400 text-[10px]"></i>
                    </div>
                </div>

                <div class="overflow-x-auto">
                    <table id="tableRiwayat" class="min-w-full table-auto text-left text-xs text-gray-600">
                        <thead class="bg-gray-100 text-[10px] uppercase text-gray-700 font-bold border-b border-gray-200">
                            <tr>
                                <th class="px-2 py-2">Pengaduan</th>
                                <th class="px-2 py-2">Tanggal</th>
                                <th class="px-2 py-2">Kode</th>
                                <th class="px-2 py-2 text-center">Aksi</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                // Query data riwayat pengaduan yang sudah final selesai/ditolak
                                String sqlRiwayat = "SELECT * FROM form_pengaduan WHERE status = 'ditolak' OR status = 'ditangani' OR status = 'selesai' ORDER BY waktu_kejadian DESC";
                                try (Connection conn = Koneksi.getConnection();
                                     PreparedStatement ps = conn.prepareStatement(sqlRiwayat);
                                     ResultSet rs = ps.executeQuery()) {
                                    
                                    boolean adaRiwayat = false;
                                    while (rs.next()) {
                                        adaRiwayat = true;
                                        java.sql.Timestamp ts = rs.getTimestamp("waktu_kejadian");
                                        String tanggalFormated = new java.text.SimpleDateFormat("dd-MM-yyyy").format(ts);
                            %>
                            <tr class="border-b border-gray-200 hover:bg-gray-50 transition">
                                <td class="px-2 py-2.5 font-medium text-gray-900 max-w-[100px] truncate"><%= rs.getString("deskripsi") %></td>
                                <td class="px-2 py-2.5 whitespace-nowrap"><%= tanggalFormated %></td>
                                <td class="px-2 py-2.5 font-mono text-gray-500 font-semibold"><%= rs.getString("kode_pengaduan") %></td>
                                <td class="px-2 py-2.5 text-center">
                                   <a href="detailpengaduan.jsp?id=<%= rs.getInt("id_form_pengaduan") %>" class="inline-block hover:scale-110 transition-transform p-1">
    <img src="images/icon_search.png" alt="Detail" class="w-5 h-5 object-contain">
</a>
                                </td>
                            </tr>
                            <%
                                    }
                                    if(!adaRiwayat) {
                                        out.println("<tr><td colspan='4' class='text-center py-4 text-gray-400 italic'>Belum ada riwayat laporan</td></tr>");
                                    }
                                } catch (Exception e) {
                                    out.println("<tr><td colspan='4' class='text-red-500 py-2'>Error: " + e.getMessage() + "</td></tr>");
                                }
                            %>
                        </tbody>
                    </table>
                </div>
            </section>

        </main>

        <footer class="bg-gray-50 border-t border-gray-200 p-3 text-center text-[10px] text-gray-400">
            &copy; 2026 SILANDAK Mobile Web App
        </footer>
    </div>

    <script>
        function toggleSidebar() {
            const sidebar = document.getElementById('sidebarMenu');
            const overlay = document.getElementById('sidebarOverlay');
            if (sidebar.classList.contains('-translate-x-full')) {
                sidebar.classList.remove('-translate-x-full');
                overlay.classList.remove('hidden');
            } else {
                sidebar.classList.add('-translate-x-full');
                overlay.classList.add('hidden');
            }
        }

        function prosesLogout() {
            Swal.fire({
                title: 'Keluar Aplikasi?',
                text: "Apakah Anda yakin ingin keluar dari aplikasi SILANDAK?",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#b91c1c',
                cancelButtonColor: '#6b7280',
                confirmButtonText: 'Ya, Keluar',
                cancelButtonText: 'Batal'
            }).then((result) => {
                if (result.isConfirmed) {
                    window.location.href = 'index.jsp';
                }
            });
        }

        // Live Search Filter untuk masing-masing tabel
        function filterTable(inputId, tableId) {
            let input = document.getElementById(inputId);
            let filter = input.value.toUpperCase();
            let table = document.getElementById(tableId);
            let tr = table.getElementsByTagName("tr");
            
            for (let i = 1; i < tr.length; i++) {
                let tdPengaduan = tr[i].getElementsByTagName("td")[0];
                let tdKode = tr[i].getElementsByTagName("td")[2];
                if (tdPengaduan || tdKode) {
                    let txtValuePengaduan = tdPengaduan.textContent || tdPengaduan.innerText;
                    let txtValueKode = tdKode.textContent || tdKode.innerText;
                    
                   if (txtValuePengaduan.toUpperCase().indexOf(filter) > -1 || txtValueKode.toUpperCase().indexOf(filter) > -1) {
    tr[i].style.display = ""; // <--- BENAR
} else {
    tr[i].style.display = "none"; // <--- BENAR
}
                }
            }
        }
    </script>
</body>
</html>