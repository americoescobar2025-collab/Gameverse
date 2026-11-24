package com.gameverse.spring;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public class PromocionLanzamientoRepository {

    private final JdbcTemplate jdbc;

    public PromocionLanzamientoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // Consultar ofertas validas vinculadas al inventario.
    public List<Map<String, Object>> listarOfertas() {
        return jdbc.queryForList("""
            SELECT v.id_videojuego, v.titulo,
                   v.descripcion, v.plataforma,
                   v.precio AS precio_original,
                   p.precio_oferta, v.stock,
                   v.imagen_url,
                   ROUND((1 - p.precio_oferta /
                   NULLIF(v.precio, 0)) * 100) AS descuento
            FROM promociones p
            INNER JOIN videojuegos v
                ON p.id_videojuego = v.id_videojuego
            WHERE p.activo = 1
              AND v.activo = 1
              AND v.stock > 0
              AND p.precio_oferta < v.precio
            ORDER BY v.titulo
            """);
    }

    // Consultar futuros lanzamientos sin afectar el inventario.
    public List<Map<String, Object>> listarLanzamientos() {
        return jdbc.queryForList("""
            SELECT id_lanzamiento, titulo, descripcion,
                   plataforma, fecha_lanzamiento, imagen_url
            FROM proximos_lanzamientos
            WHERE activo = 1
              AND (fecha_lanzamiento IS NULL
                   OR fecha_lanzamiento >= CURRENT_DATE)
            ORDER BY fecha_lanzamiento IS NULL,
                     fecha_lanzamiento, titulo
            """);
    }
}