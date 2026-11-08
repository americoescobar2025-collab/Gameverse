package com.gameverse.api;

import com.gameverse.model.dto.Videojuego;

import java.math.BigDecimal;

/**
 * Cuerpo JSON aceptado al crear o actualizar un videojuego.
 *
 * <p>El ID no forma parte del cuerpo: en una actualización se toma de la
 * ruta para evitar inconsistencias entre URL y payload.</p>
 */
public class VideojuegoRequest {

    private String titulo;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private String imagenUrl;
    private String categoria;

    public VideojuegoRequest() {
    }

    public Videojuego toVideojuego(int id) {
        if (precio == null) {
            throw new IllegalArgumentException("El precio es obligatorio.");
        }
        if (stock == null) {
            throw new IllegalArgumentException("El stock es obligatorio.");
        }

        return new Videojuego(
                id,
                titulo,
                descripcion,
                precio.doubleValue(),
                stock,
                imagenUrl,
                categoria
        );
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
