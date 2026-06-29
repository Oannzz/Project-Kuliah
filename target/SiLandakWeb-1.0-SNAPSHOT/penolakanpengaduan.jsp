<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SILANDAK - Alasan Penolakan</title>
    <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>
        body { font-family: 'Arial', sans-serif; background-color: #f3f4f6; }
    </style>
</head>
<body>

    <div class="max-w-md mx-auto bg-white min-h-screen shadow-lg flex flex-col justify-between">
        <div>
            <!-- HEADER -->
            <header class="bg-[#b91c1c] text-white p-4 flex items-center shadow-md">
                <a href="detailpengaduan.jsp?id=<%= request.getParameter("id") %>" class="mr-4 text-xl hover:opacity-80 transition flex items-center justify-center">
    <i class="fas fa-arrow-left"></i>
</a>
                <div>
                    <h1 class="text-xl font-bold tracking-wider">SILANDAK</h1>
                    <p class="text-xs opacity-90">Sistem Layanan Dan Pengaduan Damkar</p>
                </div>
            </header>

            <!-- KONTEN UTAMA -->
            <main class="p-4 space-y-4">
                <div class="text-lg font-mono font-black text-gray-800 border-b border-gray-200 pb-1">
                    ${not empty pengaduan.kode_pengaduan ? pengaduan.kode_pengaduan : 'NEM - 0001'}
<!-- Sesuaikan 'kode_pengaduan' dengan nama atribut di class Java Anda -->
                </div>

                <div class="flex justify-between items-center">
                    <h2 class="text-base font-bold text-gray-800">Alasan Penolakan</h2>
                    <span class="...">
    <c:choose>
        <c:when test="${not empty pengaduan.tanggal}">
            <fmt:formatDate value="${pengaduan.tanggal}" pattern="dd - MM - yyyy" />
        </c:when>
        <c:otherwise>
            05 - 05 - 2026
        </c:otherwise>
    </c:choose>
</span>
                </div>

                <!-- INPUT TEXTAREA ALASAN -->
                <div>
                    <textarea id="inputAlasan" rows="7" placeholder="Tuliskan alasan penolakan laporan di sini secara jelas..." class="w-full p-3 text-sm border border-gray-300 rounded-lg bg-gray-50 focus:bg-white focus:ring-2 focus:ring-red-500 focus:outline-none transition leading-relaxed" required></textarea>
                </div>

                <!-- BAGIAN BUKTI & ALAMAT (2 KOLOM) -->
                <div class="grid grid-cols-2 gap-4 pt-2">
                    <!-- KODE BARU -->
<div class="space-y-1">
    <label class="text-sm font-bold text-gray-800 text-center block">Bukti</label>
    
    <!-- Input File Tersembunyi -->
    <input type="file" id="fileBukti" accept="image/*,video/*" class="hidden" onchange="previewFile()">
    
    <!-- Kotak yang Bisa Diklik -->
    <div onclick="pilihFile()" id="boxPreview" class="w-full h-28 border border-gray-300 rounded-lg flex flex-col items-center justify-center bg-gray-100 text-gray-400 p-2 text-center cursor-pointer hover:bg-gray-200 hover:text-gray-600 transition shadow-sm">
        <i class="fas fa-image text-xl mb-1 text-red-500"></i>
        <span id="textPreview" class="text-[9px] font-semibold">Upload Gambar/Video</span>
    </div>
</div>

                    <div class="space-y-1 flex flex-col justify-center">
                        <label class="text-sm font-bold text-gray-800 flex items-center">
                            <i class="fas fa-map-marker-alt text-red-600 mr-1 text-xs"></i> Alamat
                        </label>
                        <p class="text-xs text-gray-600 leading-normal font-medium">
                            Jl Rayang Gotong No. 128
                        </p>
                    </div>
                </div>
            </main>
        </div>

        <!-- TOMBOL AKSI -->
        <footer class="p-4 bg-gray-50 border-t border-gray-200 grid grid-cols-2 gap-4">
            <button onclick="kirimPenolakan()" class="w-full py-2.5 bg-black text-white font-bold rounded border border-black uppercase tracking-wide hover:bg-gray-800 active:scale-95 transition">
                Kirim
            </button>
            <!-- Menggunakan tag <a> agar navigasi langsung berjalan tanpa interupsi form -->
<!-- Menggunakan scriptlet JSP agar link otomatis membawa ID laporan kembali ke detail -->
<a href="detailpengaduan.jsp?id=<%= request.getParameter("id") %>" class="w-full py-2.5 border border-gray-400 rounded text-xs font-bold text-gray-700 bg-white hover:bg-gray-100 transition flex items-center justify-center text-center">
    BATAL
</a>
        </footer>
    </div>

    <script>
    // 1. FUNGSI UNTUK MENGIRIM DATA PENOLAKAN
    function kirimPenolakan() {
        const alasan = document.getElementById('inputAlasan').value.trim();
        
        if (alasan === "") {
            Swal.fire({
                icon: 'warning',
                title: 'Perhatian',
                text: 'Alasan penolakan tidak boleh kosong! Mohon diisi terlebih dahulu.',
                confirmButtonColor: '#b91c1c'
            });
            return;
        }
        
        const urlParams = new URLSearchParams(window.location.search);
        const idLaporan = urlParams.get('id');
        
        if (!idLaporan) {
            Swal.fire('Error', 'ID Pengaduan tidak ditemukan!', 'error');
            return;
        }

        Swal.fire({
            title: 'Tolak Pengaduan?',
            text: "Apakah Anda yakin resmi menolak pengaduan ini?",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#b91c1c',
            cancelButtonColor: '#6b7280',
            confirmButtonText: 'Ya, Tolak',
            cancelButtonText: 'Batal'
        }).then((result) => {
            if (result.isConfirmed) {
                fetch('ProsesPengaduanServlet?aksi=tolak&id=' + idLaporan, { method: 'GET' })
                .then(response => response.text())
                .then(data => {
                    if (data.trim() === "SUCCESS") {
                        Swal.fire({
                            icon: 'success',
                            title: 'Sukses!',
                            text: 'Pengaduan resmi ditolak.',
                            confirmButtonColor: '#b91c1c'
                        }).then(() => {
                            window.location.href = 'dashboard.jsp';
                        });
                    } else {
                        Swal.fire('Gagal', 'Gagal memperbarui status penolakan di database.', 'error');
                    }
                })
                .catch(err => Swal.fire('Error', 'Terjadi kesalahan koneksi server: ' + err, 'error'));
            }
        });
    } // <--- Tanda kurung kurawal penutup kirimPenolakan() SEHARUSNYA DI SINI

    // 2. FUNGSI UNTUK MEMICU JENDELA PILIH FILE (BERDIRI SENDIRI)
    function pilihFile() {
        const fileInput = document.getElementById('fileBukti');
        if (fileInput) {
            fileInput.click();
        }
    }

    // 3. FUNGSI UNTUK MENAMPILKAN PRATINJAU / NAMA FILE SETELAH TERPILIH
    function previewFile() {
        const fileInput = document.getElementById('fileBukti');
        const textPreview = document.getElementById('textPreview');
        const boxPreview = document.getElementById('boxPreview');
        
        if (fileInput && fileInput.files && fileInput.files[0]) {
            const namaFile = fileInput.files[0].name;
            
            // Ubah teks menjadi nama file
            textPreview.innerText = "Terpilih: " + namaFile;
            textPreview.className = "text-[10px] font-bold text-green-600 break-all p-1";
            
            // Ubah warna kotak menjadi hijau tanda sukses
            boxPreview.className = "w-full h-28 border-2 border-green-500 rounded-lg flex flex-col items-center justify-center bg-green-50 text-green-600 p-2 text-center cursor-pointer transition shadow-sm";
        }
    }
</script>
</body>
</html>