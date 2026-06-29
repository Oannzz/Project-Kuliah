<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SILANDAK - Login</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="bg-gray-100 font-sans min-h-screen flex justify-center items-start py-6 px-4">

    <div class="relative w-full max-w-md bg-white rounded-2xl shadow-xl border border-gray-200 overflow-hidden flex flex-col p-6 space-y-6 mt-12">
        
        <div class="text-center space-y-2">
            <h1 class="text-2xl font-black tracking-wider text-red-600">SILANDAK</h1>
            <p class="text-xs text-gray-500 font-medium">Sistem Layanan Dan Pengaduan Damkar</p>
        </div>

        <div class="flex justify-center py-2">
            <img src="images/logo.png" alt="Logo Silandak" class="w-28 h-auto object-contain dropdown-shadow">
        </div>

        <form action="LoginServlet" method="POST" class="space-y-4">
            <div class="space-y-1">
                <div class="relative flex items-center">
                    <span class="absolute left-3 text-gray-400 text-sm">
                        <i class="fa-solid fa-user"></i>
                    </span>
                    <input type="text" name="username" placeholder="Username" required
                           class="w-full pl-9 pr-3 py-2.5 text-sm border border-gray-300 rounded-lg bg-gray-50 focus:bg-white focus:ring-2 focus:ring-red-500 focus:outline-none transition">
                </div>
            </div>

            <div class="space-y-1">
                <div class="relative flex items-center">
                    <span class="absolute left-3 text-gray-400 text-sm">
                        <i class="fa-solid fa-lock"></i>
                    </span>
                    <input type="password" name="password" placeholder="Password" required
                           class="w-full pl-9 pr-3 py-2.5 text-sm border border-gray-300 rounded-lg bg-gray-50 focus:bg-white focus:ring-2 focus:ring-red-500 focus:outline-none transition">
                </div>
            </div>

            <div class="pt-2">
                <button type="submit" 
                        class="w-full py-2.5 bg-red-600 text-white font-bold rounded-lg border-2 border-red-700 uppercase tracking-wide hover:bg-red-700 active:scale-[0.98] transition shadow-sm">
                    Masuk
                </button>
            </div>
        </form>
        <div style="text-align: center; margin-top: 20px; font-size: 14px;">
        <span style="color: #666;">Belum punya akun?</span> 
        <a href="register.jsp" style="color: #d9383a; text-decoration: none; font-weight: bold; margin-left: 5px;">Daftar di sini</a>
    </div>

        <div class="text-center text-[10px] text-gray-400 pt-4 border-t border-gray-100">
            &copy; 2026 SILANDAK Mobile Web App
        </div>
    </div>

</body>
</html>