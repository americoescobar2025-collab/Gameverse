<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gameverse | Tu próximo universo</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/gameverse.css">
</head>

<body>

<header>
    <a class="logo" href="${pageContext.request.contextPath}/">
        GAME<span>VERSE</span>
    </a>

    <nav>
    <a href="${pageContext.request.contextPath}/">Inicio</a>
    <a href="${pageContext.request.contextPath}/catalogo">Catálogo</a>
    <a href="${pageContext.request.contextPath}/#modulos">Módulos</a>
    <a href="${pageContext.request.contextPath}/proximamente.jsp">Próximamente</a>
    <a href="${pageContext.request.contextPath}/ofertas.jsp">Ofertas</a>
    <a href="${pageContext.request.contextPath}/login">Iniciar sesión</a>
</nav>
</header>

<main>

    <!-- Presentacion principal -->
    <section class="hero">
        <div class="hero-content">

            <div class="eyebrow">BIENVENIDO A GAMEVERSE</div>

            <h1>
                Tu próximo <span>universo</span>
                comienza aquí.
            </h1>

            <p>
                Descubre nuevos mundos, explora nuestro catálogo
                y encuentra tu próxima aventura.
            </p>

            <a class="btn"
               href="${pageContext.request.contextPath}/catalogo">
                EXPLORAR CATÁLOGO
            </a>

        </div>
    </section>

    <!-- Modulos del sistema -->
    <section class="seccion" id="modulos">

        <h2 class="section-title">Explora Gameverse</h2>

        <div class="secciones">

            <article class="card">
                <div class="icon">🎮</div>
                <h2>Catálogo</h2>
                <p>Descubre los videojuegos disponibles.</p>
                <a href="${pageContext.request.contextPath}/catalogo">
                    Explorar catálogo →
                </a>
            </article>

            <article class="card">
                <div class="icon">👤</div>
                <h2>Usuarios</h2>
                <p>Administración de usuarios y cuentas.</p>
                <span class="pendiente">En desarrollo</span>
            </article>

            <article class="card">
                <div class="icon">📦</div>
                <h2>Inventario</h2>
                <p>Control de videojuegos y existencias.</p>
                <a href="${pageContext.request.contextPath}/inventario.xhtml">
                    Abrir inventario →
                </a>
            </article>

            <article class="card">
                <div class="icon">🛒</div>
                <h2>Compras</h2>
                <p>Gestiona tus compras y pedidos.</p>
                <span class="pendiente">En desarrollo</span>
            </article>

        </div>

    </section>

    <!-- Presentacion de proximos lanzamientos -->
    <section class="proximamente" id="proximamente">

        <div>
            <div class="eyebrow">PRÓXIMAMENTE EN GAMEVERSE</div>

            <h2>Una nueva aventura te espera.</h2>

            <p>
                Nuevos mundos, nuevas historias y experiencias
                que estarán por llegar a nuestro universo.
            </p>

            <a class="btn" href="${pageContext.request.contextPath}/proximamente.jsp">VER PRÓXIMOS LANZAMIENTOS</a>
        </div>

    </section>

</main>

<footer>
    <p>© 2026 Gameverse | Todos los derechos reservados.</p>
</footer>

</body>
</html>