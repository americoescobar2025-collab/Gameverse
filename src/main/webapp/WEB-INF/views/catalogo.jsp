<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Gameverse - Catálogo</title>

    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: Arial, sans-serif;
            background: #0f172a;
            color: #ffffff;
            padding: 30px;
        }

        .contenedor {
            max-width: 1200px;
            margin: 0 auto;
        }

        .encabezado {
            text-align: center;
            margin-bottom: 35px;
        }

        .encabezado h1 {
            font-size: 36px;
            margin-bottom: 10px;
        }

        .encabezado p {
            color: #cbd5e1;
            font-size: 16px;
        }

        .mensaje-vacio {
            background: #1e293b;
            border: 1px solid #334155;
            border-radius: 12px;
            padding: 30px;
            text-align: center;
            color: #cbd5e1;
        }

        .catalogo {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
            gap: 25px;
        }

        .tarjeta {
            background: #1e293b;
            border: 1px solid #334155;
            border-radius: 15px;
            overflow: hidden;
            transition: transform 0.2s ease;
        }

        .tarjeta:hover {
            transform: translateY(-5px);
        }

        .imagen-juego {
            width: 100%;
            height: 190px;
            object-fit: cover;
            display: block;
        }

        .sin-imagen {
            height: 190px;
            display: flex;
            align-items: center;
            justify-content: center;
            background: #334155;
            color: #94a3b8;
        }

        .contenido {
            padding: 20px;
        }

        .contenido h2 {
            font-size: 21px;
            margin-bottom: 10px;
        }

        .descripcion {
            color: #cbd5e1;
            line-height: 1.5;
            margin-bottom: 15px;
        }

        .dato {
            margin: 8px 0;
            color: #e2e8f0;
        }

        .precio {
            font-size: 20px;
            font-weight: bold;
            margin-top: 15px;
        }

        .stock {
            color: #cbd5e1;
        }

        .categoria {
            display: inline-block;
            margin-top: 10px;
            padding: 6px 10px;
            border-radius: 20px;
            background: #334155;
            color: #ffffff;
            font-size: 13px;
        }

        .botones-navegacion {
            display: flex;
            justify-content: center;
            gap: 15px;
            margin-top: 35px;
            flex-wrap: wrap;
        }

        .volver,
        .nuevo {
            display: inline-block;
            padding: 12px 20px;
            border-radius: 8px;
            background: #334155;
            color: #ffffff;
            text-decoration: none;
            font-weight: bold;
        }

        .volver:hover,
        .nuevo:hover {
            background: #475569;
        }

        .nuevo {
            background: #475569;
        }

        @media (max-width: 600px) {
            body {
                padding: 15px;
            }

            .encabezado h1 {
                font-size: 28px;
            }

            .botones-navegacion {
                flex-direction: column;
            }

            .volver,
            .nuevo {
                text-align: center;
                width: 100%;
            }
        }
    </style>
</head>

<body>

<div class="contenedor">

    <div class="encabezado">
        <h1>🎮 Catálogo de videojuegos</h1>
        <p>Explora los videojuegos disponibles en Gameverse</p>
    </div>

    <c:choose>

        <c:when test="${empty listaJuegos}">

            <div class="mensaje-vacio">
                <h2>No hay videojuegos registrados</h2>
                <p>
                    Actualmente no existen videojuegos disponibles en el catálogo.
                </p>
            </div>

        </c:when>

        <c:otherwise>

            <div class="catalogo">

                <c:forEach var="juego" items="${listaJuegos}">

                    <div class="tarjeta">

                        <c:choose>

                            <c:when test="${not empty juego.imagenUrl}">

                                <img class="imagen-juego"
                                     src="${juego.imagenUrl}"
                                     alt="Imagen de ${juego.titulo}">

                            </c:when>

                            <c:otherwise>

                                <div class="sin-imagen">
                                    Sin imagen disponible
                                </div>

                            </c:otherwise>

                        </c:choose>

                        <div class="contenido">

                            <h2>${juego.titulo}</h2>

                            <p class="descripcion">
                                ${juego.descripcion}
                            </p>

                            <p class="dato precio">
                                Precio: $${juego.precio}
                            </p>

                            <p class="dato stock">
                                Stock disponible: ${juego.stock}
                            </p>

                            <c:if test="${not empty juego.categoria}">

                                <span class="categoria">
                                    ${juego.categoria}
                                </span>

                            </c:if>

                        </div>

                    </div>

                </c:forEach>

            </div>

        </c:otherwise>

    </c:choose>

    <div class="botones-navegacion">

        <a class="volver"
           href="${pageContext.request.contextPath}/">
            ← Volver al inicio
        </a>

        <span class="nuevo">
            + Registrar videojuego
        </span>

    </div>

</div>

</body>
</html>