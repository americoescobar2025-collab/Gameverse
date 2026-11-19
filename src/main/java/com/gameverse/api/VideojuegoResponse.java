package com.gameverse.api;

import com.gameverse.model.dto.Videojuego;

/**
 * Representación pública de un videojuego. Mantiene la respuesta REST
 * independiente de los detalles internos del DTO usado por JSF.
 */
public class VideojuegoResponse {

    private final int id;
    private final String titulo;
    private final String descripcion;
    private final double precio;
    private final int stock;
    private final String imagenUrl;
    private final String categoria;

    private VideojuegoResponse(Videojuego videojuego) {
        this.id = videojuego.getId();
        this.titulo = videojuego.getTitulo();
        this.descripcion = videojuego.getDescripcion();
        this.precio = videojuego.getPrecio();
        this.stock = videojuego.getStock();
        this.imagenUrl = videojuego.getImagenUrl();
        this.categoria = videojuego.getCategoria();
    }

    public static VideojuegoResponse from(Videojuego videojuego) {
        return new VideojuegoResponse(videojuego);
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public String getCategoria() {
        return categoria;
    }
}
