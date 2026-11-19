
package com.gameverse.inventory;

import com.gameverse.model.dao.InventoryDAO;
import com.gameverse.model.dto.Videojuego;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para el catálogo e inventario de Gameverse.
 *
 * INTEGRACIÓN FASE 3:
 * - Actividad 2A: compatibilidad con la API REST.
 * - Actividad 2B: operaciones JDBC mediante InventoryRepository.
 * - Actividad 3: validaciones adicionales de seguridad.
 *
 * CORRECCIÓN DE INTEGRACIÓN:
 * Se especifica InventoryDAO como dependencia CDI para evitar
 * la ambigüedad entre InventoryDAO y VideojuegoDAO.
 *
 * Se conserva el constructor con InventoryRepository para
 * permitir la inyección de implementaciones en las pruebas.
 */
@Dependent
public class InventoryService implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final BigDecimal PRECIO_MAXIMO =
            new BigDecimal("99999999.99");

    private final InventoryRepository repository;

    /**
     * Constructor utilizado por CDI durante el despliegue.
     *
     * Se utiliza InventoryDAO explícitamente porque existen
     * dos implementaciones de InventoryRepository.
     */
    @Inject
    public InventoryService(InventoryDAO repository) {
        this.repository = repository;
    }

    /**
     * Constructor para pruebas y uso con InventoryRepository.
     *
     * No lleva @Inject para evitar la ambigüedad de CDI.
     */
    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    /**
     * Obtiene todos los videojuegos registrados.
     */
    public List<Videojuego> obtenerInventario() {
        return repository.obtenerInventario();
    }

    /**
     * Busca un videojuego por su identificador.
     */
    public Optional<Videojuego> buscarPorId(int id) {
        validarId(id);
        return repository.buscarPorId(id);
    }

    /**
     * Crea o actualiza un videojuego.
     *
     * INTEGRACIÓN 2A + 2B + 3:
     * Se conservan las validaciones de datos y la
     * comprobación de identificadores negativos.
     */
    public void guardar(Videojuego videojuego) {
        validarVideojuego(videojuego);

        if (videojuego.getId() < 0) {
            throw new IllegalArgumentException(
                    "El identificador del videojuego no es válido."
            );
        }

        videojuego.setTitulo(videojuego.getTitulo().trim());
        videojuego.setDescripcion(videojuego.getDescripcion().trim());
        videojuego.setCategoria(videojuego.getCategoria().trim());
        videojuego.setImagenUrl(
                limpiarImagenUrl(videojuego.getImagenUrl())
        );

        repository.guardar(videojuego);
    }

    /**
     * Elimina un videojuego existente.
     */
    public void eliminar(int id) {
        validarId(id);
        repository.eliminar(id);
    }

    /**
     * Aumenta o disminuye el stock de un videojuego.
     *
     * INTEGRACIÓN 2A + 2B:
     * Se conserva la comprobación del resultado JDBC.
     */
    public void ajustarStock(int id, int cambio) {
        validarId(id);

        if (cambio == 0) {
            throw new IllegalArgumentException(
                    "El ajuste de stock debe ser distinto de cero."
            );
        }

        if (!repository.ajustarStock(id, cambio)) {
            throw new IllegalArgumentException(
                    "No se pudo ajustar el stock: verifica que el videojuego exista " +
                            "y que el stock no quede negativo ni exceda el límite permitido."
            );
        }
    }

    /**
     * Establece un valor absoluto de stock.
     *
     * ACTIVIDAD 2B:
     * Se conserva este método como parte de las
     * operaciones de inventario integradas.
     */
    public void actualizarStock(int id, int nuevoStock) {
        validarId(id);

        if (nuevoStock < 0) {
            throw new IllegalArgumentException(
                    "El stock no puede ser negativo."
            );
        }

        if (!repository.actualizarStock(id, nuevoStock)) {
            throw new IllegalArgumentException(
                    "No se encontró el videojuego que deseas actualizar."
            );
        }
    }

    /**
     * Valida los datos de un videojuego antes de guardarlo.
     *
     * ACTIVIDAD 3 - SEGURIDAD:
     * El precio debe ser estrictamente mayor que cero.
     */
    private void validarVideojuego(Videojuego videojuego) {

        if (videojuego == null) {
            throw new IllegalArgumentException(
                    "Debes proporcionar los datos del videojuego."
            );
        }

        if (videojuego.getTitulo() == null
                || videojuego.getTitulo().trim().length() < 2
                || videojuego.getTitulo().trim().length() > 100) {
            throw new IllegalArgumentException(
                    "El título debe tener entre 2 y 100 caracteres."
            );
        }

        if (videojuego.getDescripcion() == null
                || videojuego.getDescripcion().trim().length() < 10
                || videojuego.getDescripcion().trim().length() > 500) {
            throw new IllegalArgumentException(
                    "La descripción debe tener entre 10 y 500 caracteres."
            );
        }

        if (videojuego.getCategoria() == null
                || videojuego.getCategoria().trim().length() < 2
                || videojuego.getCategoria().trim().length() > 50) {
            throw new IllegalArgumentException(
                    "La categoría debe tener entre 2 y 50 caracteres."
            );
        }

        double precio = videojuego.getPrecio();

        if (!Double.isFinite(precio)
                || precio <= 0
                || BigDecimal.valueOf(precio).compareTo(PRECIO_MAXIMO) > 0
                || BigDecimal.valueOf(precio).scale() > 2) {
            throw new IllegalArgumentException(
                    "El precio debe ser mayor que 0 y no superar " +
                            "99,999,999.99 con máximo dos decimales."
            );
        }

        if (videojuego.getStock() < 0) {
            throw new IllegalArgumentException(
                    "El stock no puede ser negativo."
            );
        }

        validarImagenUrl(videojuego.getImagenUrl());
    }

    /**
     * Comprueba que la URL de imagen utilice HTTP o HTTPS.
     */
    private void validarImagenUrl(String imagenUrl) {

        if (imagenUrl == null || imagenUrl.isBlank()) {
            return;
        }

        try {
            URI uri = URI.create(imagenUrl.trim());
            String esquema = uri.getScheme();

            if (uri.getHost() == null
                    || (!"http".equalsIgnoreCase(esquema)
                    && !"https".equalsIgnoreCase(esquema))) {

                throw new IllegalArgumentException(
                        "La URL de imagen debe ser una dirección HTTP o HTTPS válida."
                );
            }

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "La URL de imagen debe ser una dirección HTTP o HTTPS válida.",
                    e
            );
        }
    }

    /**
     * Elimina espacios innecesarios de la URL de imagen.
     */
    private String limpiarImagenUrl(String imagenUrl) {
        return imagenUrl == null || imagenUrl.isBlank()
                ? null
                : imagenUrl.trim();
    }

    /**
     * Impide operaciones con identificadores inválidos.
     */
    private void validarId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "El identificador del videojuego no es válido."
            );
        }
    }
}
