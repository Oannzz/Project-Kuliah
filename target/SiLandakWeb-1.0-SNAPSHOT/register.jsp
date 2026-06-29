<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Daftar Akun - SILANDAK</title>
    <!-- 
      Kombinasi CSS internal yang mereplikasi persis tampilan login Anda:
      Latar belakang abu-abu terang, card putih melengkung shadow halus, dan tombol merah khas Damkar
    -->
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #eef2f5;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .login-container {
            background: #ffffff;
            padding: 40px 30px;
            border-radius: 20px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
            width: 100%;
            max-width: 420px;
            text-align: center;
            box-sizing: border-box;
        }
        .title {
            color: #d9383a;
            font-size: 28px;
            font-weight: bold;
            letter-spacing: 1px;
            margin-bottom: 5px;
            text-transform: uppercase;
        }
        .subtitle {
            color: #555;
            font-size: 14px;
            margin-top: 0;
            margin-bottom: 25px;
        }
        .logo-damkar {
            width: 100px;
            height: auto;
            margin-bottom: 30px;
        }
        .form-group {
            margin-bottom: 18px;
            position: relative;
            text-align: left;
        }
        .form-group input {
            width: 100%;
            padding: 12px 15px;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            background-color: #f9f9f9;
            font-size: 14px;
            color: #333;
            box-sizing: border-box;
            transition: border 0.3s ease;
        }
        .form-group input:focus {
            border-color: #d9383a;
            outline: none;
            background-color: #fff;
        }
        .btn-masuk {
            width: 100%;
            background-color: #d9383a;
            color: white;
            border: none;
            padding: 14px;
            font-size: 15px;
            font-weight: bold;
            border-radius: 10px;
            cursor: pointer;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            margin-top: 10px;
            transition: background-color 0.2s ease;
        }
        .btn-masuk:hover {
            background-color: #b82b2d;
        }
        .footer-link {
            text-align: center;
            margin-top: 25px;
            font-size: 13px;
        }
        .footer-link a {
            color: #d9383a;
            text-decoration: none;
            font-weight: bold;
            margin-left: 5px;
        }
        .copyright {
            font-size: 11px;
            color: #999;
            margin-top: 35px;
        }
    </style>
</head>
<body>

    <div class="login-container">
        <!-- Judul Aplikasi -->
        <div class="title">SILANDAK</div>
        <div class="subtitle">Sistem Layanan Dan Pengaduan Damkar</div>
        
        <!-- Logo Damkar Resmi Aplikasi Anda -->
        <img src="images/logo.png" alt="Logo Damkar" class="logo-damkar">
        
        <!-- Form Pendaftaran -->
        <form action="RegisterServlet" method="POST">
            
            <div class="form-group">
                <input type="text" name="nama" placeholder="Nama Lengkap" required>
            </div>
            
            <div class="form-group">
                <input type="text" name="username" placeholder="Username Baru" required>
            </div>
            
            <div class="form-group">
                <input type="password" name="password" placeholder="Password" required>
            </div>
            
            <!-- Tombol Submit Merah Khas SILANDAK -->
            <button type="submit" class="btn-masuk">DAFTAR</button>
            
        </form>

        <!-- Link Navigasi Kembali Switched -->
        <div class="footer-link">
            <span style="color: #666;">Sudah memiliki akun?</span> 
            <a href="index.jsp">Masuk di sini</a>
        </div>

        <!-- Atribusi Bawah -->
        <div class="copyright">© 2026 SILANDAK Mobile Web App</div>
    </div>

</body>
</html>