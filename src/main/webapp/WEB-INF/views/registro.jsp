<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Gameverse - Registro</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/gameverse.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css">
</head>

<body class="auth-page registro-page">

<div class="auth-panel">

    <h1 class="logo">GAME<span>VERSE</span></h1>

    <p class="auth-subtitle">Crear una nueva cuenta</p>

    <c:if test="${not empty error}">
        <div class="error">
            <c:out value="${error}" />
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/registro"
          method="POST">

        <label for="nombre">Nombre completo</label>

        <input type="text"
               id="nombre"
               name="nombre"
               maxlength="100"
               required>

        <label for="correo">Correo electrónico</label>

        <input type="email"
               id="correo"
               name="correo"
               maxlength="150"
               required>

        <label for="password">Contraseña</label>

        <input type="password"
               id="password"
               name="password"
               minlength="12"
               required>

        <button type="submit" class="btn">
            Registrarme
        </button>

    </form>

    <div class="auth-links">
        ¿Ya tienes una cuenta?
        <a href="${pageContext.request.contextPath}/login">
            Iniciar sesión
        </a>
    </div>

</div>

</body>
</html>