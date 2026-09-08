package com.gameverse.model.dao;

import com.gameverse.config.DatabaseConfig;
import com.gameverse.model.dto.Videojuego;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VideojuegoDAO {

    public List<Videojuego> obtenerTodos() throws SQLException {
        List<Videojuego> lista = new ArrayList<>();
        String sql = "SELECT v.id_videojuego, v.titulo, v.descripcion, v.precio, v.stock, v.imagen_url, c.nombre AS categoria " +
                "FROM videojuegos v LEFT JOIN categorias c ON v.id_categoria = c.id_categoria";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Videojuego juego = new Videojuego();
                juego.setId(rs.getInt("id_videojuego"));
                juego.setTitulo(rs.getString("titulo"));
                juego.setDescripcion(rs.getString("descripcion"));
                juego.setPrecio(rs.getDouble("precio"));
                juego.setStock(rs.getInt("stock"));
                juego.setImagenUrl(rs.getString("imagen_url"));
                juego.setCategoria(rs.getString("categoria"));
                lista.add(juego);
            }
        }
        return lista;
    }
}