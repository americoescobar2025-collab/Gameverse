package com.gameverse.model.dao;

import com.gameverse.config.DatabaseConfig;
import com.gameverse.inventory.InventoryPersistenceException;
import com.gameverse.inventory.InventoryRepository;
import com.gameverse.model.dto.Videojuego;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class VideojuegoDAO implements InventoryRepository {

    private static final String CONSULTA_INVENTARIO =
            "SELECT v.id_videojuego, v.titulo, v.descripcion, v.precio, v.stock, v.imagen_url, c.nombre AS categoria "
                    + "FROM videojuegos v LEFT JOIN categorias c ON v.id_categoria = c.id_categoria";

    public List<Videojuego> obtenerTodos() throws SQLException {
        List<Videojuego> lista = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(CONSULTA_INVENTARIO);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearVideojuego(rs));
            }
        }
        return lista;
    }

    /**
     * Registra un videojuego asociándolo con una categoría existente.
     * La búsqueda de la categoría no depende de mayúsculas/minúsculas.
     */
    public void insertar(Videojuego videojuego) throws SQLException {
        String sql = "INSERT INTO videojuegos "
                + "(titulo, descripcion, precio, stock, imagen_url, id_categoria) "
                + "SELECT ?, ?, ?, ?, ?, c.id_categoria "
                + "FROM categorias c "
                + "WHERE LOWER(TRIM(c.nombre)) = LOWER(TRIM(?))";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, videojuego.getTitulo());
            ps.setString(2, videojuego.getDescripcion());
            ps.setDouble(3, videojuego.getPrecio());
            ps.setInt(4, videojuego.getStock());
            ps.setString(5, videojuego.getImagenUrl() == null || videojuego.getImagenUrl().isBlank()
                    ? null : videojuego.getImagenUrl());
            ps.setString(6, videojuego.getCategoria());

            if (ps.executeUpdate() == 0) {
                throw new SQLException("La categoría indicada no existe en la base de datos.");
            }
        }
    }

    @Override
    public List<Videojuego> obtenerInventario() {
        try {
            return obtenerTodos();
        } catch (SQLException e) {
            throw new InventoryPersistenceException("No se pudo consultar el inventario.", e);
        }
    }

    @Override
    public Optional<Videojuego> buscarPorId(int id) {
        String sql = CONSULTA_INVENTARIO + " WHERE v.id_videojuego = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapearVideojuego(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new InventoryPersistenceException("No se pudo consultar el videojuego.", e);
        }
    }

    @Override
    public void guardar(Videojuego videojuego) {
        try {
            if (videojuego.getId() <= 0) {
                insertar(videojuego);
                return;
            }

            String sql = "UPDATE videojuegos v "
                    + "INNER JOIN categorias c ON LOWER(TRIM(c.nombre)) = LOWER(TRIM(?)) "
                    + "SET v.titulo = ?, v.descripcion = ?, v.precio = ?, v.stock = ?, "
                    + "v.imagen_url = ?, v.id_categoria = c.id_categoria "
                    + "WHERE v.id_videojuego = ?";
            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, videojuego.getCategoria());
                ps.setString(2, videojuego.getTitulo());
                ps.setString(3, videojuego.getDescripcion());
                ps.setDouble(4, videojuego.getPrecio());
                ps.setInt(5, videojuego.getStock());
                ps.setString(6, videojuego.getImagenUrl());
                ps.setInt(7, videojuego.getId());
                if (ps.executeUpdate() == 0) {
                    throw new InventoryPersistenceException(
                            "No se guardó el videojuego: verifica su identificador y que la categoría exista."
                    );
                }
            }
        } catch (SQLException e) {
            throw new InventoryPersistenceException("No se pudo guardar el videojuego.", e);
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM videojuegos WHERE id_videojuego = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                throw new InventoryPersistenceException("No se encontró el videojuego que deseas eliminar.");
            }
        } catch (SQLException e) {
            throw new InventoryPersistenceException("No se pudo eliminar el videojuego.", e);
        }
    }

    @Override
    public boolean ajustarStock(int id, int cambio) {
        String sql = "UPDATE videojuegos SET stock = stock + ? "
                + "WHERE id_videojuego = ? "
                + "AND CAST(stock AS SIGNED) + ? BETWEEN 0 AND 2147483647";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cambio);
            ps.setInt(2, id);
            ps.setInt(3, cambio);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new InventoryPersistenceException("No se pudo ajustar el stock del videojuego.", e);
        }
    }

    private Videojuego mapearVideojuego(ResultSet rs) throws SQLException {
        Videojuego videojuego = new Videojuego();
        videojuego.setId(rs.getInt("id_videojuego"));
        videojuego.setTitulo(rs.getString("titulo"));
        videojuego.setDescripcion(rs.getString("descripcion"));
        videojuego.setPrecio(rs.getDouble("precio"));
        videojuego.setStock(rs.getInt("stock"));
        videojuego.setImagenUrl(rs.getString("imagen_url"));
        videojuego.setCategoria(rs.getString("categoria"));
        return videojuego;
    }
}
