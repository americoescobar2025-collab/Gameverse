<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>

<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar sesión - Gameverse</title>

<style>
    * {
        box-sizing: border-box;
        margin: 0;
        padding: 0;
        font-family: Arial, sans-serif;
    }

    body {
        min-height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
        background: linear-gradient(135deg, #101827, #253b63);
        padding: 20px;
    }

    .login-container {
        width: 100%;
        max-width: 420px;
        background: white;
        padding: 35px;
        border-radius: 15px;
        box-shadow: 0 10px 30px rgba(0,0,0,0.3);
    }

    h1 {
        text-align: center;
        color: #172554;
        margin-bottom: 10px;
    }

    .subtitle {
        text-align: center;
        color: #64748b;
        margin-bottom: 25px;
    }

    label {
        display: block;
        margin-bottom: 7px;
        color: #334155;
        font-weight: bold;
    }

    input {
        width: 100%;
        padding: 12px;
        margin-bottom: 18px;
        border: 1px solid #cbd5e1;
        border-radius: 7px;
        outline: none;
    }

    input:focus {
        border-color: #2563eb;
    }

    button {
        width: 100%;
        padding: 13px;
        background: #2563eb;
        color: white;
        border: none;
        border-radius: 7px;
        font-size: 16px;
        font-weight: bold;
        cursor: pointer;
    }

    button:hover {
        background: #1d4ed8;
    }

    .error {
        background: #fee2e2;
        color: #b91c1c;
        padding: 12px;
        border-radius: 7px;
        margin-bottom: 18px;
        text-align: center;
    }

    .exito {
        background: #dcfce7;
        color: #166534;
        padding: 12px;
        border-radius: 7px;
        margin-bottom: 18px;
        text-align: center;
    }

    .registro {
        text-align: center;
        margin-top: 22px;
        color: #475569;
    }

    .registro a,
    .back {
        color: #2563eb;
        text-decoration: none;
        font-weight: bold;
    }

    .registro a:hover,
    .back:hover {
        text-decoration: underline;
    }

    .back {
        display: block;
        text-align: center;
        margin-top: 20px;
    }
</style>

</head>

<body>

<div class="login-container">

<h1>GAMEVERSE</h1>
<p class="subtitle">Bienvenido de nuevo</p>

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

    <button type="submit">Iniciar sesión</button>

</form>

<div class="registro">
    ¿No tienes una cuenta?
    <a href="${pageContext.request.contextPath}/registro">
        Crear cuenta
    </a>
</div>

<a class="back" href="${pageContext.request.contextPath}/">
    Volver al inicio
</a>

</div>

</body>
</html>
