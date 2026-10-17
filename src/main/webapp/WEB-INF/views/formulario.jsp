<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Gameverse - Registrar videojuego</title>

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
            max-width: 650px;
            margin: 0 auto;
        }

        .encabezado {
            text-align: center;
            margin-bottom: 30px;
        }

        .encabezado h1 {
            font-size: 32px;
            margin-bottom: 10px;
        }

        .encabezado p {
            color: #cbd5e1;
        }

        .formulario {
            background: #1e293b;
            border: 1px solid #334155;
            border-radius: 15px;
            padding: 30px;
        }

        .mensaje-error,
        .mensaje-exito {
            padding: 14px;
            border-radius: 8px;
            margin-bottom: 20px;
            line-height: 1.5;
        }

        .mensaje-error {
            background: #3f1d1d;
            border: 1px solid #7f1d1d;
            color: #fecaca;
        }

        .mensaje-exito {
            background: #143524;
            border: 1px solid #166534;
            color: #bbf7d0;
        }

        .campo {
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
        }

        input,
        textarea {
            width: 100%;
            padding: 12px;
            border: 1px solid #475569;
            border-radius: 8px;
            background: #0f172a;
            color: #ffffff;
            font-size: 15px;
        }

        textarea {
            min-height: 120px;
            resize: vertical;
        }

        input:focus,
        textarea:focus {
            outline: 2px solid #64748b;
        }

        input:invalid:not(:placeholder-shown),
        textarea:invalid:not(:placeholder-shown) {
            border-color: #ef4444;
        }

        input:valid:not(:placeholder-shown),
        textarea:valid:not(:placeholder-shown) {
            border-color: #22c55e;
        }

        .ayuda {
            display: block;
            margin-top: 5px;
            color: #94a3b8;
            font-size: 13px;
        }

        .botones {
            display: flex;
            gap: 12px;
            margin-top: 25px;
        }

        button,
        .volver {
            flex: 1;
            padding: 12px;
            border: none;
            border-radius: 8px;
            font-size: 15px;
            cursor: pointer;
            text-align: center;
            text-decoration: none;
        }

        button {
            background: #475569;
            color: #ffffff;
        }

        button:hover {
            background: #64748b;
        }

        .volver {
            background: #334155;
            color: #ffffff;
        }

        .volver:hover {
            background: #475569;
        }

        @media (max-width: 600px) {
            body {
                padding: 15px;
            }

            .formulario {
                padding: 20px;
            }

            .botones {
                flex-direction: column;
            }
        }
    </style>
</head>

<body>

<div class="contenedor">

    <div class="encabezado">
        <h1>🎮 Registrar videojuego</h1>
        <p>Completa los datos del videojuego</p>
    </div>

    <form class="formulario"
          action="${pageContext.request.contextPath}/videojuegos"
          method="post">

        <!-- Mensaje de error -->
        <c:if test="${not empty error}">
            <div class="mensaje-error">
                ⚠️ ${error}
            </div>
        </c:if>

        <!-- Mensaje de éxito -->
        <c:if test="${not empty mensaje}">
            <div class="mensaje-exito">
                ✅ ${mensaje}
            </div>
        </c:if>

        <div class="campo">
            <label for="titulo">Título *</label>

            <input type="text"
                   id="titulo"
                   name="titulo"
                   required
                   minlength="2"
                   maxlength="100"
                   placeholder="Ej. Minecraft">

            <small class="ayuda">
                El título es obligatorio y debe tener entre 2 y 100 caracteres.
            </small>
        </div>

        <div class="campo">
            <label for="descripcion">Descripción *</label>

            <textarea id="descripcion"
                      name="descripcion"
                      required
                      minlength="10"
                      maxlength="500"
                      placeholder="Describe el videojuego"></textarea>

            <small class="ayuda">
                La descripción debe tener entre 10 y 500 caracteres.
            </small>
        </div>

        <div class="campo">
            <label for="precio">Precio *</label>

            <input type="number"
                   id="precio"
                   name="precio"
                   required
                   min="0"
                   step="0.01"
                   placeholder="Ej. 29.99">

            <small class="ayuda">
                El precio debe ser igual o mayor a 0.
            </small>
        </div>

        <div class="campo">
            <label for="stock">Stock *</label>

            <input type="number"
                   id="stock"
                   name="stock"
                   required
                   min="0"
                   step="1"
                   placeholder="Ej. 10">

            <small class="ayuda">
                El stock debe ser un número entero igual o mayor a 0.
            </small>
        </div>

        <div class="campo">
            <label for="imagenUrl">URL de imagen</label>

            <input type="url"
                   id="imagenUrl"
                   name="imagenUrl"
                   placeholder="https://ejemplo.com/imagen.jpg">

            <small class="ayuda">
                Campo opcional. Si se utiliza, debe ser una URL válida.
            </small>
        </div>

        <div class="campo">
            <label for="categoria">Categoría *</label>

            <input type="text"
                   id="categoria"
                   name="categoria"
                   required
                   minlength="2"
                   maxlength="50"
                   placeholder="Ej. Acción">

            <small class="ayuda">
                La categoría es obligatoria y debe tener entre 2 y 50 caracteres.
            </small>
        </div>

        <div class="botones">

            <button type="submit">
                Guardar videojuego
            </button>

            <a class="volver"
               href="${pageContext.request.contextPath}/catalogo">
                Cancelar
            </a>

        </div>

    </form>

</div>

</body>
</html>