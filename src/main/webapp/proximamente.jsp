<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gameverse | Próximos lanzamientos</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/gameverse.css">
</head>
<body>

<!-- Navegacion principal del sistema -->
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

    <!-- Presentacion de futuros lanzamientos -->
    <section class="proximamente">
        <div>
            <div class="eyebrow">EL FUTURO DE GAMEVERSE</div>
            <h2>La próxima generación de aventuras.</h2>
            <p>
                Descubre los nuevos mundos que próximamente
                podrán formar parte de nuestro catálogo.
            </p>
            <a class="btn" href="#lanzamientos">
                VER LANZAMIENTOS
            </a>
        </div>
    </section>

    <!-- Espacio preparado para futuros videojuegos -->
    <section class="seccion" id="lanzamientos">
        <h2 class="section-title">Próximos lanzamientos</h2>

                <!-- Lanzamientos cargados desde Spring REST -->
        <div class="secciones" id="lista-lanzamientos">
            <p>Cargando proximos lanzamientos...</p>
        </div>
    </section>

</main>

<footer>
    © 2026 Gameverse | Todos los derechos reservados.
</footer>

<script>
    // Crear elementos seguros sin insertar HTML desde la base de datos.
    function agregarElemento(padre, etiqueta, contenido, clase) {
        const elemento = document.createElement(etiqueta);
        elemento.textContent = contenido;

        if (clase) {
            elemento.className = clase;
        }

        padre.appendChild(elemento);
        return elemento;
    }

    // Formatear los precios en dolares.
    function dinero(valor) {
        return '$' + Number(valor).toFixed(2);
    }

    // Consultar los endpoints REST de Spring.
    async function cargarVideojuegos() {
        const ofertas = document.getElementById('lista-ofertas');
        const contenedor = ofertas ||
            document.getElementById('lista-lanzamientos');

        const endpoint = ofertas ? 'ofertas' : 'lanzamientos';

        try {
            const respuesta = await fetch(
                '${pageContext.request.contextPath}/spring/' + endpoint
            );

            if (!respuesta.ok) {
                throw new Error('Error HTTP ' + respuesta.status);
            }

            const datos = await respuesta.json();
            contenedor.replaceChildren();

            if (datos.length === 0) {
                agregarElemento(contenedor, 'p',
                    'No hay videojuegos disponibles por el momento.');
                return;
            }

            datos.forEach(function(juego) {
                const tarjeta = document.createElement('article');
                tarjeta.className = 'card';

                agregarElemento(tarjeta, 'div',
                    ofertas ? '-' + juego.descuento + '%' : 'PROXIMAMENTE',
                    'eyebrow');

                agregarElemento(tarjeta, 'h2', juego.titulo);

                agregarElemento(tarjeta, 'p',
                    juego.plataforma || 'Plataforma por confirmar');

                if (ofertas) {
                    agregarElemento(tarjeta, 'del',
                        dinero(juego.precio_original));

                    const precio = agregarElemento(tarjeta, 'h2',
                        dinero(juego.precio_oferta));

                    precio.style.color = 'var(--amarillo)';

                    agregarElemento(tarjeta, 'p',
                        'Existencias: ' + juego.stock);

                    const enlace = agregarElemento(tarjeta, 'a',
                        'VER CATALOGO', 'btn');

                    enlace.href =
                        '${pageContext.request.contextPath}/catalogo';
                } else {
                    agregarElemento(tarjeta, 'p', juego.descripcion);

                    agregarElemento(tarjeta, 'span',
                        juego.fecha_lanzamiento ? new Date(juego.fecha_lanzamiento).toLocaleDateString('es-SV', {day: 'numeric', month: 'long', year: 'numeric', timeZone: 'UTC'}) :
                        'Fecha por confirmar', 'pendiente');
                }

                contenedor.appendChild(tarjeta);
            });

        } catch (error) {
            contenedor.replaceChildren();

            agregarElemento(contenedor, 'p',
                'No se pudieron cargar los videojuegos. Intenta nuevamente.');

            console.error(error);
        }
    }

    cargarVideojuegos();
</script>
</body>
</html>