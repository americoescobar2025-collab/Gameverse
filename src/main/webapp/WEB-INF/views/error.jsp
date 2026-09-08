<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Error - Gameverse</title>
</head>
<body>

<h1>Ocurrió un error</h1>

<p>${error}</p>

<a href="${pageContext.request.contextPath}/catalogo">
    Volver al catálogo
</a>

</body>
</html>