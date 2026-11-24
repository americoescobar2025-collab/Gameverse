<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gameverse | Ofertas y promociones</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/gameverse.css">

    <style>
        /* Fondo independiente de promociones */
        .ofertas-hero {
            min-height: 420px;
            display: flex;
            align-items: center;
            padding: 60px 7%;
            background:
                linear-gradient(90deg,
                    rgba(8,10,15,.90),
                    rgba(8,10,15,.30)),
                url("assets/img/ofertas-background.jpg")
                center top / cover no-repeat,
                #141820;
        }

        .ofertas-hero h1 {
            font-size: clamp(32px, 5vw, 60px);
            color: var(--amarillo);
            margin-bottom: 20px;
        }

        .ofertas-hero p {
            max-width: 550px;
            line-height: 1.7;
            margin-bottom: 25px;
        }

        .sin-ofertas {
            background: var(--panel);
            border: 1px solid #34404e;
            border-top: 2px solid var(--cian);
            padding: 45px 25px;
            text-align: center;
        }

        .sin-ofertas p {
            color: var(--secundario);
            margin: 20px 0;
        }
    </style>
</head>

<body>

<!-- Navegacion principal -->
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

    <!-- Presentacion de promociones comerciales -->
    <section class="ofertas-hero">
        <div>
            <div class="eyebrow">GAMEVERSE DEALS</div>

            <h1>Grandes juegos.<br>Mejores precios.</h1>

            <p>
                Descubre nuestras próximas ofertas y promociones
                especiales para ampliar tu colección de videojuegos.
            </p>

            <a class="btn" href="#promociones">
                EXPLORAR OFERTAS
            </a>
        </div>
    </section>

    <!-- Espacio reservado para descuentos reales del sistema -->
    <section class="seccion" id="promociones">

        <h2 class="section-title">Ofertas disponibles</h2>

                <!-- Promociones cargadas desde Spring REST -->
        <div class="secciones" id="lista-ofertas">
            <p>Cargando ofertas disponibles...</p>
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
                        juego.fecha_lanzamiento ||
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