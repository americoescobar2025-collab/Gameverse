# API REST de videojuegos

La API es independiente de las vistas JSP/JSF y está disponible bajo la ruta:

```text
/api/videojuegos
```

Todas las respuestas usan `application/json; charset=UTF-8`. Las operaciones
de escritura requieren `Content-Type: application/json`.

## Recurso videojuego

El JSON de entrada y salida usa los siguientes campos:

```json
{
  "id": 1,
  "titulo": "The Last Frontier",
  "descripcion": "Juego de acción post-apocalíptico",
  "precio": 59.99,
  "stock": 25,
  "imagenUrl": "https://example.com/cover.jpg",
  "categoria": "Acción"
}
```

En `POST`, `id` no se envía. En `PUT`, el identificador se toma de la URL y
cualquier valor enviado en el cuerpo se rechaza como campo desconocido.

## Endpoints

| Método | Ruta | Éxito | Descripción |
|---|---|---:|---|
| GET | `/api/videojuegos` | 200 | Lista el inventario completo. |
| GET | `/api/videojuegos/{id}` | 200 | Consulta un videojuego. |
| POST | `/api/videojuegos` | 201 | Registra un videojuego y devuelve `Location`. |
| PUT | `/api/videojuegos/{id}` | 200 | Reemplaza los datos editables del videojuego. |
| PATCH | `/api/videojuegos/{id}/stock` | 200 | Ajusta el stock con un cambio relativo. |
| DELETE | `/api/videojuegos/{id}` | 204 | Elimina el videojuego. |

Ejemplo de creación:

```bash
curl -i -X POST http://localhost:8080/Gameverse/api/videojuegos \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Nuevo juego",
    "descripcion": "Descripción válida de al menos diez caracteres",
    "precio": 29.99,
    "stock": 10,
    "imagenUrl": "https://example.com/juego.jpg",
    "categoria": "Acción"
  }'
```

Ejemplo de ajuste de stock:

```bash
curl -i -X PATCH http://localhost:8080/Gameverse/api/videojuegos/1/stock \
  -H "Content-Type: application/json" \
  -d '{"cambio": -1}'
```

## Validaciones y errores

Las reglas de título, descripción, precio, stock, URL de imagen y categoría
se delegan en `InventoryService`, que es la misma capa usada por el módulo de
inventario. La categoría debe existir en la base de datos.

Los errores se devuelven con un formato uniforme:

```json
{
  "status": 400,
  "error": "Solicitud inválida",
  "message": "El stock no puede ser negativo.",
  "path": "/Gameverse/api/videojuegos",
  "timestamp": "2026-11-07T23:00:00Z"
}
```

Se utilizan estos códigos principales:

- `400`: ruta, JSON o datos inválidos.
- `404`: videojuego inexistente.
- `415`: el cuerpo no usa `application/json`.
- `422`: la categoría indicada no existe.
- `500`: fallo inesperado de persistencia o del servidor.

La API no devuelve contraseñas, hashes ni otros datos del módulo de usuarios.
