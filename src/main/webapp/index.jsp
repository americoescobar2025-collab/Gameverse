<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Gameverse - Inicio</title>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: Arial, sans-serif;
        }

        body {
            background: #111827;
            color: white;
            min-height: 100vh;
        }

        /* Barra superior */
        header {
            background: #1f2937;
            padding: 20px 60px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.4);
        }

        .logo {
            font-size: 28px;
            font-weight: bold;
            color: #60a5fa;
        }

        .logo span {
            color: white;
        }

        nav a {
            color: white;
            text-decoration: none;
            margin-left: 25px;
            font-size: 16px;
        }

        nav a:hover {
            color: #60a5fa;
        }

        /* Contenido principal */
        .hero {
            text-align: center;
            padding: 80px 20px 50px;
        }

        .hero h1 {
            font-size: 48px;
            margin-bottom: 15px;
        }

        .hero h1 span {
            color: #60a5fa;
        }

        .hero p {
            font-size: 20px;
            color: #d1d5db;
            margin-bottom: 35px;
        }

        .btn {
            display: inline-block;
            background: #2563eb;
            color: white;
            padding: 14px 30px;
            border-radius: 8px;
            text-decoration: none;
            font-weight: bold;
            transition: 0.3s;
        }

        .btn:hover {
            background: #1d4ed8;
            transform: translateY(-2px);
        }

        /* Tarjetas */
        .secciones {
            max-width: 1000px;
            margin: 20px auto 60px;
            padding: 0 20px;

            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 25px;
        }

        .card {
            background: #1f2937;
            padding: 30px 20px;
            border-radius: 12px;
            text-align: center;
            box-shadow: 0 4px 15px rgba(0,0,0,0.25);
        }

        .card .icon {
            font-size: 40px;
            margin-bottom: 15px;
        }

        .card h2 {
            margin-bottom: 10px;
        }

        .card p {
            color: #9ca3af;
            margin-bottom: 20px;
        }

        .card a {
            color: #60a5fa;
            text-decoration: none;
            font-weight: bold;
        }

        .card a:hover {
            text-decoration: underline;
        }

        /* Pie de página */
        footer {
            background: #0f172a;
            text-align: center;
            padding: 20px;
            color: #9ca3af;
        }
    </style>
</head>

<body>

<header>

    <div class="logo">
        🎮 Game<span>verse</span>
    </div>

    <nav>
        <a href="${pageContext.request.contextPath}/">Inicio</a>
        <a href="${pageContext.request.contextPath}/catalogo">Catálogo</a>
    </nav>

</header>


<section class="hero">

    <h1>Bienvenido a <span>Gameverse</span></h1>

    <p>
        Tu espacio para descubrir y disfrutar de los mejores videojuegos.
    </p>

    <a class="btn"
       href="${pageContext.request.contextPath}/catalogo">
        🎮 Ver catálogo
    </a>

</section>


<section class="secciones">

    <div class="card">

        <div class="icon">🎮</div>

        <h2>Catálogo</h2>

        <p>
            Explora nuestro catálogo de videojuegos disponibles.
        </p>

        <a href="${pageContext.request.contextPath}/catalogo">
            Ver catálogo →
        </a>

    </div>


    <div class="card">

        <div class="icon">👤</div>

        <h2>Usuarios</h2>

        <p>
            Gestión de usuarios del sistema.
        </p>

        <a href="#">
            Próximamente
        </a>

    </div>


    <div class="card">

        <div class="icon">📦</div>

        <h2>Inventario</h2>

        <p>
            Control y administración del inventario.
        </p>

        <a href="#">
            Próximamente
        </a>

    </div>


    <div class="card">

        <div class="icon">🛒</div>

        <h2>Compras</h2>

        <p>
            Gestiona las compras y pedidos de videojuegos.
        </p>

        <a href="#">
            Próximamente
        </a>

    </div>

</section>


<footer>

    © 2026 Gameverse - Sistema de gestión de videojuegos

</footer>

</body>
</html>