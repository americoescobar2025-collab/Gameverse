<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gameverse - Catálogo</title>
</head>
<body>

<h1>Catálogo de videojuegos</h1>

<c:choose>

    <c:when test="${empty listaJuegos}">
        <p>No hay videojuegos registrados.</p>
    </c:when>

    <c:otherwise>

        <c:forEach var="juego" items="${listaJuegos}">

            <div>
                <h2>${juego.titulo}</h2>

                <p>${juego.descripcion}</p>

                <p>Precio: $${juego.precio}</p>

                <p>Stock: ${juego.stock}</p>

                <p>Categoría: ${juego.categoria}</p>

                <c:if test="${not empty juego.imagenUrl}">
                    <img src="${juego.imagenUrl}"
                         alt="${juego.titulo}"
                         width="200">
                </c:if>

                <hr>
            </div>

        </c:forEach>

    </c:otherwise>

</c:choose>

</body>
</html>