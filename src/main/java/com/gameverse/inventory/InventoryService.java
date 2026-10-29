package com.gameverse.inventory;

import com.gameverse.model.dto.Videojuego;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Dependent
public class InventoryService implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final BigDecimal PRECIO_MAXIMO = new BigDecimal("99999999.99");

    private final InventoryRepository repository;

    @Inject
    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    public List<Videojuego> obtenerInventario() {
        return repository.obtenerInventario();
    }

    public Optional<Videojuego> buscarPorId(int id) {
        validarId(id);
        return repository.buscarPorId(id);
    }

    public void guardar(Videojuego videojuego) {
        validarVideojuego(videojuego);
        videojuego.setTitulo(videojuego.getTitulo().trim());
        videojuego.setDescripcion(videojuego.getDescripcion().trim());
        videojuego.setCategoria(videojuego.getCategoria().trim());
        videojuego.setImagenUrl(limpiarImagenUrl(videojuego.getImagenUrl()));
        repository.guardar(videojuego);
    }

    public void eliminar(int id) {
        validarId(id);
        repository.eliminar(id);
    }

    public void ajustarStock(int id, int cambio) {
        validarId(id);
        if (cambio == 0) {
            throw new IllegalArgumentException("El ajuste de stock debe ser distinto de cero.");
        }
        if (!repository.ajustarStock(id, cambio)) {
            throw new IllegalArgumentException(
                    "No se pudo ajustar el stock: verifica que el videojuego exista y que el stock no quede negativo ni exceda el límite permitido."
            );
        }
    }

    private void validarVideojuego(Videojuego videojuego) {
        if (videojuego == null) {
            throw new IllegalArgumentException("Debes proporcionar los datos del videojuego.");
        }
        if (videojuego.getTitulo() == null
                || videojuego.getTitulo().trim().length() < 2
                || videojuego.getTitulo().trim().length() > 100) {
            throw new IllegalArgumentException("El título debe tener entre 2 y 100 caracteres.");
        }
        if (videojuego.getDescripcion() == null
                || videojuego.getDescripcion().trim().length() < 10
                || videojuego.getDescripcion().trim().length() > 500) {
            throw new IllegalArgumentException("La descripción debe tener entre 10 y 500 caracteres.");
        }
        if (videojuego.getCategoria() == null
                || videojuego.getCategoria().trim().length() < 2
                || videojuego.getCategoria().trim().length() > 50) {
            throw new IllegalArgumentException("La categoría debe tener entre 2 y 50 caracteres.");
        }

        double precio = videojuego.getPrecio();
        if (!Double.isFinite(precio)
                || precio < 0
                || BigDecimal.valueOf(precio).compareTo(PRECIO_MAXIMO) > 0
                || BigDecimal.valueOf(precio).scale() > 2) {
            throw new IllegalArgumentException(
                    "El precio debe ser un valor entre 0 y 99,999,999.99 con máximo dos decimales."
            );
        }
        if (videojuego.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        validarImagenUrl(videojuego.getImagenUrl());
    }

    private void validarImagenUrl(String imagenUrl) {
        if (imagenUrl == null || imagenUrl.isBlank()) {
            return;
        }
        try {
            URI uri = URI.create(imagenUrl.trim());
            String esquema = uri.getScheme();
            if (uri.getHost() == null
                    || (!"http".equalsIgnoreCase(esquema) && !"https".equalsIgnoreCase(esquema))) {
                throw new IllegalArgumentException(
                        "La URL de imagen debe ser una dirección HTTP o HTTPS válida."
                );
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "La URL de imagen debe ser una dirección HTTP o HTTPS válida.", e
            );
        }
    }

    private String limpiarImagenUrl(String imagenUrl) {
        return imagenUrl == null || imagenUrl.isBlank() ? null : imagenUrl.trim();
    }

    private void validarId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador del videojuego no es válido.");
        }
    }
}
