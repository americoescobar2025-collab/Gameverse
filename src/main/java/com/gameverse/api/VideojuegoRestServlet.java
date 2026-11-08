package com.gameverse.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gameverse.inventory.InventoryPersistenceException;
import com.gameverse.inventory.InventoryService;
import com.gameverse.model.dao.VideojuegoDAO;
import com.gameverse.model.dto.Videojuego;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * API REST independiente de las vistas JSF/JSP.
 *
 * <p>La URL base es {@code /api/videojuegos}. La persistencia y las reglas de
 * validación se delegan en {@link InventoryService}; este servlet solamente
 * traduce HTTP a operaciones de negocio y devuelve JSON.</p>
 */
@WebServlet(name = "VideojuegoRestServlet", urlPatterns = "/api/videojuegos/*")
public class VideojuegoRestServlet extends HttpServlet {

    private static final String JSON_CONTENT_TYPE = "application/json;charset=UTF-8";
    private static final String ALLOWED_METHODS = "GET, POST, PUT, PATCH, DELETE, OPTIONS";

    private static final ObjectMapper JSON = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

    private final InventoryService inventoryService;

    public VideojuegoRestServlet() {
        this(new InventoryService(new VideojuegoDAO()));
    }

    VideojuegoRestServlet(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ("PATCH".equalsIgnoreCase(request.getMethod())) {
            doPatch(request, response);
            return;
        }
        super.service(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String path = pathInfo(request);
            if (isCollection(path)) {
                List<VideojuegoResponse> videojuegos = inventoryService.obtenerInventario()
                        .stream()
                        .map(VideojuegoResponse::from)
                        .toList();
                writeJson(response, HttpServletResponse.SC_OK, videojuegos);
                return;
            }

            int id = parseId(path);
            Videojuego videojuego = findById(id);
            writeJson(response, HttpServletResponse.SC_OK, VideojuegoResponse.from(videojuego));
        } catch (Exception e) {
            writeError(response, request, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            requireCollection(pathInfo(request));
            requireJsonContentType(request);
            Videojuego videojuego = readBody(request, VideojuegoRequest.class).toVideojuego(0);
            inventoryService.guardar(videojuego);

            response.setHeader("Location", request.getRequestURL().append('/').append(videojuego.getId()).toString());
            writeJson(response, HttpServletResponse.SC_CREATED, VideojuegoResponse.from(videojuego));
        } catch (Exception e) {
            writeError(response, request, e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            requireJsonContentType(request);
            int id = parseId(pathInfo(request));
            ensureExists(id);

            Videojuego videojuego = readBody(request, VideojuegoRequest.class).toVideojuego(id);
            inventoryService.guardar(videojuego);
            writeJson(response, HttpServletResponse.SC_OK, VideojuegoResponse.from(videojuego));
        } catch (Exception e) {
            writeError(response, request, e);
        }
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            requireJsonContentType(request);
            String path = pathInfo(request);
            if (!path.endsWith("/stock")) {
                throw badRequest("PATCH solamente admite el recurso /{id}/stock.");
            }

            int id = parseId(path.substring(0, path.length() - "/stock".length()));
            ensureExists(id);
            StockAdjustmentRequest payload = readBody(request, StockAdjustmentRequest.class);
            if (payload.getCambio() == null) {
                throw badRequest("El campo cambio es obligatorio.");
            }

            inventoryService.ajustarStock(id, payload.getCambio());
            Videojuego videojuego = findById(id);
            writeJson(response, HttpServletResponse.SC_OK, VideojuegoResponse.from(videojuego));
        } catch (Exception e) {
            writeError(response, request, e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = parseId(pathInfo(request));
            ensureExists(id);
            inventoryService.eliminar(id);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (Exception e) {
            writeError(response, request, e);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Allow", ALLOWED_METHODS);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private Videojuego findById(int id) {
        Optional<Videojuego> videojuego = inventoryService.buscarPorId(id);
        if (videojuego.isEmpty()) {
            throw new ApiException(HttpServletResponse.SC_NOT_FOUND, "No encontrado",
                    "No existe un videojuego con el identificador indicado.");
        }
        return videojuego.get();
    }

    private void ensureExists(int id) {
        findById(id);
    }

    private <T> T readBody(HttpServletRequest request, Class<T> type) throws IOException {
        try {
            T body = JSON.readValue(request.getReader(), type);
            if (body == null) {
                throw badRequest("El cuerpo JSON no puede estar vacío.");
            }
            return body;
        } catch (JsonProcessingException e) {
            throw badRequest("El cuerpo de la solicitud no contiene JSON válido.");
        }
    }

    private void requireJsonContentType(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType == null || !contentType.toLowerCase().startsWith("application/json")) {
            throw new ApiException(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,
                    "Tipo de contenido no soportado",
                    "Las operaciones de escritura requieren Content-Type: application/json.");
        }
    }

    private void requireCollection(String path) {
        if (!isCollection(path)) {
            throw badRequest("La operación de creación solamente admite la colección /api/videojuegos.");
        }
    }

    private String pathInfo(HttpServletRequest request) {
        String path = request.getPathInfo();
        return path == null || path.isBlank() ? "/" : path;
    }

    private boolean isCollection(String path) {
        return "/".equals(path) || path.isBlank();
    }

    private int parseId(String path) {
        if (path == null || !path.matches("/\\d+/?")) {
            throw badRequest("La ruta debe incluir un identificador numérico positivo.");
        }
        String value = path.substring(1).replace("/", "");
        try {
            int id = Integer.parseInt(value);
            if (id <= 0) {
                throw badRequest("El identificador debe ser mayor que cero.");
            }
            return id;
        } catch (NumberFormatException e) {
            throw badRequest("El identificador no es válido.");
        }
    }

    private ApiException badRequest(String message) {
        return new ApiException(HttpServletResponse.SC_BAD_REQUEST, "Solicitud inválida", message);
    }

    private void writeJson(HttpServletResponse response, int status, Object body) throws IOException {
        response.setStatus(status);
        response.setContentType(JSON_CONTENT_TYPE);
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("X-Content-Type-Options", "nosniff");
        JSON.writeValue(response.getWriter(), body);
    }

    private void writeError(HttpServletResponse response, HttpServletRequest request, Exception exception)
            throws IOException {
        ApiException apiException = toApiException(exception);
        response.setHeader("Allow", ALLOWED_METHODS);
        writeJson(response, apiException.status, new ApiError(
                apiException.status,
                apiException.error,
                apiException.getMessage(),
                request.getRequestURI(),
                OffsetDateTime.now(ZoneOffset.UTC).toString()
        ));
    }

    private ApiException toApiException(Exception exception) {
        if (exception instanceof ApiException apiException) {
            return apiException;
        }
        if (exception instanceof IllegalArgumentException) {
            return badRequest(exception.getMessage());
        }
        if (exception instanceof InventoryPersistenceException persistenceException) {
            if (containsMessage(persistenceException, "categoría indicada no existe")) {
                return new ApiException(422, "Entidad no procesable",
                        "La categoría indicada no existe en la base de datos.");
            }
            return new ApiException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error interno", "No fue posible completar la operación sobre videojuegos.");
        }
        return new ApiException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "Error interno", "No fue posible completar la solicitud.");
    }

    private boolean containsMessage(Throwable throwable, String expected) {
        return Stream.iterate(throwable, current -> current != null, Throwable::getCause)
                .map(Throwable::getMessage)
                .anyMatch(message -> message != null && message.contains(expected));
    }

    private static class ApiException extends RuntimeException {
        private final int status;
        private final String error;

        private ApiException(int status, String error, String message) {
            super(message);
            this.status = status;
            this.error = error;
        }
    }
}
