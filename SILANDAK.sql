-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 26, 2026 at 11:50 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `silandak`
--

-- --------------------------------------------------------

--
-- Table structure for table `ditugaskan`
--

CREATE TABLE `ditugaskan` (
  `id_ditugaskan` int(11) NOT NULL,
  `id_form_pengaduan` int(11) NOT NULL,
  `id_petugas` int(11) NOT NULL,
  `id_operator` int(11) NOT NULL,
  `catatan_tugas` text DEFAULT NULL,
  `tanggal_tugas` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ditugaskan`
--

INSERT INTO `ditugaskan` (`id_ditugaskan`, `id_form_pengaduan`, `id_petugas`, `id_operator`, `catatan_tugas`, `tanggal_tugas`) VALUES
(1, 1, 1, 1, 'Segera ke lokasi, bawa peralatan pemadaman lengkap', '2026-06-09 16:24:20'),
(2, 1, 2, 1, 'Backup unit, bantu evakuasi warga sekitar gedung', '2026-06-09 16:24:20'),
(3, 3, 3, 2, 'Tangkap dan evakuasi ular, pastikan warga aman sebelum masuk lokasi', '2026-06-09 16:24:20');

-- --------------------------------------------------------

--
-- Table structure for table `form_pengaduan`
--

CREATE TABLE `form_pengaduan` (
  `id_form_pengaduan` int(11) NOT NULL,
  `kode_pengaduan` varchar(20) NOT NULL,
  `id_pelapor` int(11) DEFAULT NULL,
  `id_operator` int(11) DEFAULT NULL,
  `klasifikasi` enum('emergency','non_emergency') DEFAULT NULL,
  `deskripsi` text NOT NULL,
  `alamat_kejadian` varchar(255) NOT NULL,
  `foto_bukti` longtext NOT NULL,
  `status` enum('masuk_sistem','diproses','ditangani','selesai','ditolak') NOT NULL DEFAULT 'masuk_sistem',
  `waktu_kejadian` datetime NOT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `latitude` double NOT NULL,
  `longitude` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `form_pengaduan`
--

INSERT INTO `form_pengaduan` (`id_form_pengaduan`, `kode_pengaduan`, `id_pelapor`, `id_operator`, `klasifikasi`, `deskripsi`, `alamat_kejadian`, `foto_bukti`, `status`, `waktu_kejadian`, `created_at`, `updated_at`, `latitude`, `longitude`) VALUES
(1, 'EMR-0001', 1, 1, 'emergency', 'Ada gedung terbakar kira-kira 20 lantai, asap sangat tebal terlihat dari kejauhan', 'Jl. Gotong Royong No. 29, Jakarta Pusat', '', 'ditangani', '2026-05-06 08:30:00', '2026-06-09 09:24:19', '2026-06-24 13:00:10', 123, 2345),
(2, 'NEM-0001', 2, 1, 'non_emergency', 'Kunci masuk ke selokan di depan rumah sudah rusak 3 hari, berbahaya untuk anak', 'Jl. Merpati No. 15, Jakarta Barat', '', 'ditolak', '2026-05-03 14:00:00', '2026-06-09 09:24:19', '2026-06-24 13:00:14', 212, 222),
(3, 'EMR-0002', 3, 2, 'emergency', 'Ular besar masuk ke dalam rumah melalui saluran pembuangan, warga panik', 'Jl. Srikandi No. 7, Jakarta Selatan', '', 'selesai', '2026-05-02 10:15:00', '2026-06-09 09:24:19', '2026-06-24 13:00:20', 33, 134),
(4, 'EMR-0003', 1, 1, 'emergency', 'Kebakaran di warung makan pinggir jalan, api sudah merambat ke kios sebelah', 'Jl. Pahlawan No. 44, Jakarta Pusat', '', 'masuk_sistem', '2026-05-07 07:45:00', '2026-06-09 09:24:19', '2026-06-24 13:00:25', 111, 78);

-- --------------------------------------------------------

--
-- Table structure for table `instansi`
--

CREATE TABLE `instansi` (
  `id_instansi` int(11) NOT NULL,
  `nama_instansi` varchar(150) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `shift` enum('pagi','siang','malam') NOT NULL DEFAULT 'pagi',
  `alamat` varchar(255) DEFAULT NULL,
  `no_telepon` varchar(20) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `instansi`
--

INSERT INTO `instansi` (`id_instansi`, `nama_instansi`, `username`, `password`, `shift`, `alamat`, `no_telepon`, `created_at`, `updated_at`) VALUES
(1, 'Silandak', 'damkar1', '12345', 'pagi', 'Jl. Merdeka No. 1 Jakarta Pusat', '021-1234567', '2026-06-09 09:24:19', '2026-06-10 04:05:21'),
(2, 'Silandak', 'damkar2', '123456', 'siang', 'Jl. Sudirman No. 10 Jakarta', '021-7654321', '2026-06-09 09:24:19', '2026-06-10 04:05:29');

-- --------------------------------------------------------

--
-- Table structure for table `laporan_penanganan`
--

CREATE TABLE `laporan_penanganan` (
  `id_laporan` int(11) NOT NULL,
  `kode_laporan` varchar(20) NOT NULL,
  `id_form_pengaduan` int(11) NOT NULL,
  `id_instansi` int(11) NOT NULL,
  `id_operator` int(11) NOT NULL,
  `deskripsi_penanganan` text NOT NULL,
  `alamat` varchar(255) DEFAULT NULL,
  `lokasi_maps` varchar(512) DEFAULT NULL,
  `foto_media` varchar(512) DEFAULT NULL,
  `status_penanganan` enum('dalam_proses','selesai','eskalasi') DEFAULT 'dalam_proses',
  `tanggal_laporan` datetime NOT NULL DEFAULT current_timestamp(),
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `laporan_penanganan`
--

INSERT INTO `laporan_penanganan` (`id_laporan`, `kode_laporan`, `id_form_pengaduan`, `id_instansi`, `id_operator`, `deskripsi_penanganan`, `alamat`, `lokasi_maps`, `foto_media`, `status_penanganan`, `tanggal_laporan`, `created_at`, `updated_at`) VALUES
(1, 'EM-0001', 1, 1, 1, 'Kebakaran berhasil dipadamkan dalam 45 menit menggunakan 3 unit mobil pemadam. Tidak ada korban jiwa. Area telah diamankan.', 'Jl. Gotong Royong No. 29, Jakarta Pusat', NULL, NULL, 'selesai', '2026-05-06 10:30:00', '2026-06-09 09:24:20', '2026-06-09 09:24:20'),
(2, 'EM-0002', 3, 2, 2, 'Ular sanca batik sepanjang 3 meter berhasil ditangkap. Diserahkan ke kebun binatang. Warga dinyatakan aman.', 'Jl. Srikandi No. 7, Jakarta Selatan', NULL, NULL, 'selesai', '2026-05-02 13:00:00', '2026-06-09 09:24:20', '2026-06-09 09:24:20');

-- --------------------------------------------------------

--
-- Table structure for table `operator`
--

CREATE TABLE `operator` (
  `id_operator` int(11) NOT NULL,
  `nama_operator` varchar(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `shift` enum('pagi','siang','malam') NOT NULL DEFAULT 'pagi',
  `no_telepon` varchar(20) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `operator`
--

INSERT INTO `operator` (`id_operator`, `nama_operator`, `username`, `password`, `shift`, `no_telepon`, `created_at`, `updated_at`) VALUES
(1, 'Budi Santoso', 'budi.op', '123456', 'pagi', '081234567890', '2026-06-09 09:24:19', '2026-06-09 09:24:54'),
(2, 'Dewi Rahayu', 'dewi.op', '12345', 'siang', '081234567891', '2026-06-09 09:24:19', '2026-06-09 09:25:11'),
(3, 'Agus Pratama', 'agus.op', '1234', 'malam', '081234567892', '2026-06-09 09:24:19', '2026-06-09 09:25:27');

-- --------------------------------------------------------

--
-- Table structure for table `pelapor`
--

CREATE TABLE `pelapor` (
  `id_pelapor` int(11) NOT NULL,
  `nama_pelapor` varchar(100) NOT NULL,
  `no_telepon` varchar(20) NOT NULL,
  `alamat` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pelapor`
--

INSERT INTO `pelapor` (`id_pelapor`, `nama_pelapor`, `no_telepon`, `alamat`, `created_at`) VALUES
(1, 'Hendra Kusuma', '085678901234', 'Jl. Anggrek No. 5, Jakarta Pusat', '2026-06-09 09:24:19'),
(2, 'Maya Sari', '085678901235', 'Jl. Merpati No. 12, Jakarta Barat', '2026-06-09 09:24:19'),
(3, 'Joko Susilo', '085678901236', 'Jl. Srikandi No. 3, Jakarta Selatan', '2026-06-09 09:24:19'),
(4, 'TEST', '1234567890', 'JL.TEST DUMMY', '2026-06-24 19:24:29'),
(5, 'TEST', '1234567890', 'JL.TEST DUMMY', '2026-06-24 19:38:48'),
(6, '', '', '', '2026-06-25 03:59:11'),
(7, '', '', '', '2026-06-25 04:03:27'),
(8, '', '', '', '2026-06-25 04:04:16'),
(9, '', '', '', '2026-06-25 04:06:26'),
(10, '', '', '', '2026-06-25 04:10:55'),
(11, '', '', '', '2026-06-25 04:18:03'),
(12, '', '', '', '2026-06-25 04:22:32'),
(13, '', '', '', '2026-06-25 04:23:42'),
(14, '', '', '', '2026-06-25 04:26:58'),
(15, 'dfasfdasdfasfd', '75846578567856', '', '2026-06-25 04:27:26'),
(16, 'Test_2', '1234', 'Jl. Test_2 Dimana', '2026-06-25 04:33:52'),
(17, 'Test__Input_Gambar', '1234567890', 'JL.TEST DUMMY', '2026-06-26 09:41:21'),
(18, 'Test__Redirect', '1234567890', 'JL.TEST DUMMY', '2026-06-26 10:01:18'),
(19, 'Test__Redirect', '1234567890', 'JL.TEST DUMMY', '2026-06-26 10:02:56'),
(20, 'Test__Redirect', '1234567890', 'JL.TEST DUMMY', '2026-06-26 10:03:02'),
(21, 'Test__Redirect', '1234567890', 'JL.TEST DUMMY', '2026-06-26 10:04:02'),
(22, 'Test__Redirect', '1234567890', 'JL.TEST DUMMY', '2026-06-26 10:05:21'),
(23, 'Test__Redirect', '1234567890', 'JL.TEST DUMMY', '2026-06-26 10:05:52'),
(24, 'Test_Redirect+base64', '1234567890', 'JL.TEST DUMMY', '2026-06-26 10:17:39'),
(25, 'Test_Redirect+base64', '1234567890', 'JL.TEST DUMMY', '2026-06-26 10:18:56'),
(26, 'Test_Redirect+base64', '1234567890', 'JL.TEST DUMMY', '2026-06-26 10:26:07'),
(27, 'Test_Redirect+base64', '1234567890', 'JL.TEST DUMMY', '2026-06-26 10:28:02'),
(28, 'Test_Redirect+base64', '1234567890', 'JL.TEST DUMMY', '2026-06-26 10:32:03'),
(29, 'Test-Kode', '1234567890', 'JL.TEST DUMMY', '2026-06-26 15:11:47'),
(30, 'Test-Kode', '1234567890', 'JL.TEST DUMMY', '2026-06-26 15:14:46'),
(31, 'Test-Kode', '1234567890', 'JL.TEST DUMMY', '2026-06-26 15:19:12'),
(32, 'Test-Kode', '1234567890', 'JL.TEST DUMMY', '2026-06-26 15:21:39'),
(33, 'Test-Kode(2)', '1234567890', 'JL.TEST DUMMY', '2026-06-26 15:25:27'),
(34, 'Test-Kode(3)', '1234567890', 'JL.TEST DUMMY', '2026-06-26 15:29:36'),
(35, 'Test-Kode(4)', '1234567890', 'JL.TEST DUMMY', '2026-06-26 15:31:44');

-- --------------------------------------------------------

--
-- Table structure for table `petugas_lapangan`
--

CREATE TABLE `petugas_lapangan` (
  `id_petugas` int(11) NOT NULL,
  `nama_petugas` varchar(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `area_tugas` varchar(150) NOT NULL,
  `id_operator` int(11) NOT NULL,
  `no_telepon` varchar(20) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `petugas_lapangan`
--

INSERT INTO `petugas_lapangan` (`id_petugas`, `nama_petugas`, `username`, `password`, `area_tugas`, `id_operator`, `no_telepon`, `created_at`, `updated_at`) VALUES
(1, 'Rizky Firmansyah', 'rizky.pl', '1234', 'Jakarta Pusat', 1, '082345678901', '2026-06-09 09:24:19', '2026-06-18 10:05:25'),
(2, 'Andi Wijaya', 'andi.pl', '12345', 'Jakarta Barat', 1, '082345678902', '2026-06-09 09:24:19', '2026-06-18 10:05:32'),
(3, 'Siti Nurhaliza', 'siti.pl', '123456', 'Jakarta Selatan', 2, '082345678903', '2026-06-09 09:24:19', '2026-06-18 10:05:38');

-- --------------------------------------------------------

--
-- Table structure for table `verifikasi_petugas`
--

CREATE TABLE `verifikasi_petugas` (
  `id_verifikasi` int(11) NOT NULL,
  `id_form_pengaduan` int(11) NOT NULL,
  `id_petugas` int(11) NOT NULL,
  `keputusan` enum('diterima','ditolak') NOT NULL,
  `alasan_penolakan` text DEFAULT NULL,
  `alamat_verifikasi` varchar(255) DEFAULT NULL,
  `foto_bukti` varchar(512) DEFAULT NULL,
  `lokasi_maps` varchar(512) DEFAULT NULL,
  `waktu_verifikasi` datetime NOT NULL DEFAULT current_timestamp(),
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `verifikasi_petugas`
--

INSERT INTO `verifikasi_petugas` (`id_verifikasi`, `id_form_pengaduan`, `id_petugas`, `keputusan`, `alasan_penolakan`, `alamat_verifikasi`, `foto_bukti`, `lokasi_maps`, `waktu_verifikasi`, `created_at`) VALUES
(1, 1, 1, 'diterima', NULL, 'Jl. Gotong Royong No. 29, Jakarta Pusat', NULL, NULL, '2026-05-06 09:00:00', '2026-06-09 09:24:20'),
(2, 2, 2, 'ditolak', 'Selokan bukan wewenang Damkar, harap hubungi Dinas PU setempat', 'Jl. Merpati No. 15, Jakarta Barat', NULL, NULL, '2026-05-03 15:00:00', '2026-06-09 09:24:20'),
(3, 3, 3, 'diterima', NULL, 'Jl. Srikandi No. 7, Jakarta Selatan', NULL, NULL, '2026-05-02 11:00:00', '2026-06-09 09:24:20');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ditugaskan`
--
ALTER TABLE `ditugaskan`
  ADD PRIMARY KEY (`id_ditugaskan`),
  ADD KEY `fk_dt_form` (`id_form_pengaduan`),
  ADD KEY `fk_dt_petugas` (`id_petugas`),
  ADD KEY `fk_dt_operator` (`id_operator`);

--
-- Indexes for table `form_pengaduan`
--
ALTER TABLE `form_pengaduan`
  ADD PRIMARY KEY (`id_form_pengaduan`),
  ADD UNIQUE KEY `kode_pengaduan` (`kode_pengaduan`),
  ADD KEY `fk_fp_pelapor` (`id_pelapor`),
  ADD KEY `fk_fp_operator` (`id_operator`);

--
-- Indexes for table `instansi`
--
ALTER TABLE `instansi`
  ADD PRIMARY KEY (`id_instansi`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `laporan_penanganan`
--
ALTER TABLE `laporan_penanganan`
  ADD PRIMARY KEY (`id_laporan`),
  ADD UNIQUE KEY `kode_laporan` (`kode_laporan`),
  ADD KEY `fk_lp_form` (`id_form_pengaduan`),
  ADD KEY `fk_lp_instansi` (`id_instansi`),
  ADD KEY `fk_lp_operator` (`id_operator`);

--
-- Indexes for table `operator`
--
ALTER TABLE `operator`
  ADD PRIMARY KEY (`id_operator`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `pelapor`
--
ALTER TABLE `pelapor`
  ADD PRIMARY KEY (`id_pelapor`);

--
-- Indexes for table `petugas_lapangan`
--
ALTER TABLE `petugas_lapangan`
  ADD PRIMARY KEY (`id_petugas`),
  ADD UNIQUE KEY `username` (`username`),
  ADD KEY `fk_pl_operator` (`id_operator`);

--
-- Indexes for table `verifikasi_petugas`
--
ALTER TABLE `verifikasi_petugas`
  ADD PRIMARY KEY (`id_verifikasi`),
  ADD KEY `fk_vp_form` (`id_form_pengaduan`),
  ADD KEY `fk_vp_petugas` (`id_petugas`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `ditugaskan`
--
ALTER TABLE `ditugaskan`
  MODIFY `id_ditugaskan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `form_pengaduan`
--
ALTER TABLE `form_pengaduan`
  MODIFY `id_form_pengaduan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT for table `instansi`
--
ALTER TABLE `instansi`
  MODIFY `id_instansi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `laporan_penanganan`
--
ALTER TABLE `laporan_penanganan`
  MODIFY `id_laporan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `operator`
--
ALTER TABLE `operator`
  MODIFY `id_operator` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `pelapor`
--
ALTER TABLE `pelapor`
  MODIFY `id_pelapor` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

--
-- AUTO_INCREMENT for table `petugas_lapangan`
--
ALTER TABLE `petugas_lapangan`
  MODIFY `id_petugas` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `verifikasi_petugas`
--
ALTER TABLE `verifikasi_petugas`
  MODIFY `id_verifikasi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `ditugaskan`
--
ALTER TABLE `ditugaskan`
  ADD CONSTRAINT `fk_dt_form` FOREIGN KEY (`id_form_pengaduan`) REFERENCES `form_pengaduan` (`id_form_pengaduan`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_dt_operator` FOREIGN KEY (`id_operator`) REFERENCES `operator` (`id_operator`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_dt_petugas` FOREIGN KEY (`id_petugas`) REFERENCES `petugas_lapangan` (`id_petugas`) ON UPDATE CASCADE;

--
-- Constraints for table `form_pengaduan`
--
ALTER TABLE `form_pengaduan`
  ADD CONSTRAINT `fk_fp_operator` FOREIGN KEY (`id_operator`) REFERENCES `operator` (`id_operator`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_fp_pelapor` FOREIGN KEY (`id_pelapor`) REFERENCES `pelapor` (`id_pelapor`) ON UPDATE CASCADE;

--
-- Constraints for table `laporan_penanganan`
--
ALTER TABLE `laporan_penanganan`
  ADD CONSTRAINT `fk_lp_form` FOREIGN KEY (`id_form_pengaduan`) REFERENCES `form_pengaduan` (`id_form_pengaduan`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_lp_instansi` FOREIGN KEY (`id_instansi`) REFERENCES `instansi` (`id_instansi`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_lp_operator` FOREIGN KEY (`id_operator`) REFERENCES `operator` (`id_operator`) ON UPDATE CASCADE;

--
-- Constraints for table `petugas_lapangan`
--
ALTER TABLE `petugas_lapangan`
  ADD CONSTRAINT `fk_pl_operator` FOREIGN KEY (`id_operator`) REFERENCES `operator` (`id_operator`) ON UPDATE CASCADE;

--
-- Constraints for table `verifikasi_petugas`
--
ALTER TABLE `verifikasi_petugas`
  ADD CONSTRAINT `fk_vp_form` FOREIGN KEY (`id_form_pengaduan`) REFERENCES `form_pengaduan` (`id_form_pengaduan`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_vp_petugas` FOREIGN KEY (`id_petugas`) REFERENCES `petugas_lapangan` (`id_petugas`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
