<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Gameverse - Registro</title>

    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: Arial, sans-serif;
            background: #0f172a;
            color: white;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }

        .contenedor {
            width: 100%;
            max-width: 430px;
            background: #1e293b;
            padding: 35px;
            border-radius: 15px;
            border: 1px solid #334155;
            box-shadow: 0 10px 30px rgba(0,0,0,0.3);
        }

        h1 {
            text-align: center;
            margin-bottom: 10px;
            color: #60a5fa;
        }

        .subtitulo {
            text-align: center;
            color: #cbd5e1;
            margin-bottom: 25px;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
        }

        input {
            width: 100%;
            padding: 12px;
            margin-bottom: 20px;
            border: 1px solid #475569;
            border-radius: 8px;
            background: #0f172a;
            color: white;
            font-size: 15px;
        }

        input:focus {
            outline: 2px solid #3b82f6;
        }

        button {
            width: 100%;
            padding: 13px;
            background: #2563eb;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
        }

        button:hover {
            background: #1d4ed8;
        }

        .error {
            background: #7f1d1d;
            color: #fecaca;
            padding: 12px;
            border-radius: 8px;
            margin-bottom: 20px;
            text-align: center;
        }

        .enlace {
            text-align: center;
            margin-top: 22px;
            color: #cbd5e1;
        }

        .enlace a {
            color: #60a5fa;
            text-decoration: none;
            font-weight: bold;
        }

        .enlace a:hover {
            text-decoration: underline;
        }
    </style>
</head>

<body>

<div class="contenedor">

    <h1>🎮 Gameverse</h1>

    <p class="subtitulo">Crear una nueva cuenta</p>

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

        <button type="submit">
            Registrarme
        </button>

    </form>

    <div class="enlace">
        ¿Ya tienes una cuenta?
        <a href="${pageContext.request.contextPath}/login">
            Iniciar sesión
        </a>
    </div>

</div>

</body>
</html>