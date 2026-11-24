<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>

<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar sesión - Gameverse</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/gameverse.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css">

</head>

<body class="auth-page login-page">

<div class="auth-panel">

<h1 class="logo">GAME<span>VERSE</span></h1>
<p class="auth-subtitle">Bienvenido de nuevo</p>

<c:if test="${not empty error}">
    <div class="error">
        <c:out value="${error}" />
    </div>
</c:if>

<c:if test="${not empty mensaje}">
    <div class="exito">
        <c:out value="${mensaje}" />
    </div>
</c:if>

<form action="${pageContext.request.contextPath}/login" method="POST">

    <label for="correo">Correo electrónico</label>

    <input
        type="email"
        id="correo"
        name="correo"
        placeholder="Ingresa tu correo"
        maxlength="150"
        autocomplete="username"
        required>

    <label for="password">Contraseña</label>

    <input
        type="password"
        id="password"
        name="password"
        placeholder="Ingresa tu contraseña"
        autocomplete="current-password"
        required>

    <button type="submit" class="btn">Iniciar sesión</button>

</form>

<div class="auth-links">
    ¿No tienes una cuenta?
    <a href="${pageContext.request.contextPath}/registro">
        Crear cuenta
    </a>
</div>

<a class="auth-back" href="${pageContext.request.contextPath}/">
    Volver al inicio
</a>

</div>

</body>
</html>
