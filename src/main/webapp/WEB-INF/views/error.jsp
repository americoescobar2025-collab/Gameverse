<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Gameverse - Error</title>

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
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .error-container {
            width: 100%;
            max-width: 550px;
            background: #1e293b;
            border: 1px solid #334155;
            border-radius: 15px;
            padding: 40px;
            text-align: center;
        }

        .icono {
            font-size: 55px;
            margin-bottom: 20px;
        }

        h1 {
            margin-bottom: 15px;
        }

        .mensaje {
            color: #cbd5e1;
            line-height: 1.6;
            margin-bottom: 25px;
        }

        .boton {
            display: inline-block;
            padding: 12px 22px;
            background: #334155;
            color: #ffffff;
            text-decoration: none;
            border-radius: 8px;
        }

        .boton:hover {
            background: #475569;
        }
    </style>
</head>

<body>

<div class="error-container">

    <div class="icono">⚠️</div>

    <h1>Ocurrió un error</h1>

    <p class="mensaje">
        ${not empty error ? error : 'No fue posible completar la solicitud.'}
    </p>

    <a class="boton"
       href="${pageContext.request.contextPath}/catalogo">
        ← Volver al catálogo
    </a>

</div>

</body>
</html>