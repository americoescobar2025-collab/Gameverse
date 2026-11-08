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
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
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
     * Registra un videojuego. Si la categoría todavía no existe, la crea
     * automáticamente para que el formulario y la API acepten categorías
     * nuevas sin una operación previa adicional.
     */
    public void insertar(Videojuego videojuego) throws SQLException {
        crear(videojuego);
    }

    /**
     * Inserta un videojuego y devuelve el identificador generado por MySQL.
     * El identificador también se copia al DTO para que la API REST pueda
     * devolver el recurso creado en la respuesta 201.
     */
    public int crear(Videojuego videojuego) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO videojuegos "
                             + "(titulo, descripcion, precio, stock, imagen_url, id_categoria) "
                             + "VALUES (?, ?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            boolean autoCommit = conn.getAutoCommit();
            try {
                conn.setAutoCommit(false);
                int idCategoria = obtenerOCrearCategoria(conn, videojuego.getCategoria());
                ps.setString(1, videojuego.getTitulo());
                ps.setString(2, videojuego.getDescripcion());
                ps.setDouble(3, videojuego.getPrecio());
                ps.setInt(4, videojuego.getStock());
                ps.setString(5, videojuego.getImagenUrl() == null || videojuego.getImagenUrl().isBlank()
                        ? null : videojuego.getImagenUrl());
                ps.setInt(6, idCategoria);

                if (ps.executeUpdate() == 0) {
                    throw new SQLException("No se pudo registrar el videojuego.");
                }
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new SQLException("No se pudo obtener el identificador del videojuego creado.");
                    }
                    int id = rs.getInt(1);
                    conn.commit();
                    videojuego.setId(id);
                    return id;
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(autoCommit);
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

            String sql = "UPDATE videojuegos SET titulo = ?, descripcion = ?, precio = ?, "
                    + "stock = ?, imagen_url = ?, id_categoria = ? WHERE id_videojuego = ?";
            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                boolean autoCommit = conn.getAutoCommit();
                try {
                    conn.setAutoCommit(false);
                    int idCategoria = obtenerOCrearCategoria(conn, videojuego.getCategoria());
                    ps.setString(1, videojuego.getTitulo());
                    ps.setString(2, videojuego.getDescripcion());
                    ps.setDouble(3, videojuego.getPrecio());
                    ps.setInt(4, videojuego.getStock());
                    ps.setString(5, videojuego.getImagenUrl());
                    ps.setInt(6, idCategoria);
                    ps.setInt(7, videojuego.getId());
                    if (ps.executeUpdate() == 0) {
                        throw new SQLException("No se encontró el videojuego que deseas actualizar.");
                    }
                    conn.commit();
                } catch (SQLException e) {
                    conn.rollback();
                    throw e;
                } finally {
                    conn.setAutoCommit(autoCommit);
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

    private int obtenerOCrearCategoria(Connection conn, String nombre) throws SQLException {
        String buscarSql = "SELECT id_categoria FROM categorias "
                + "WHERE LOWER(TRIM(nombre)) = LOWER(TRIM(?))";
        try (PreparedStatement ps = conn.prepareStatement(buscarSql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_categoria");
                }
            }
        }

        String insertarSql = "INSERT INTO categorias (nombre) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(insertarSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            try {
                ps.executeUpdate();
            } catch (SQLIntegrityConstraintViolationException e) {
                // Otra solicitud pudo crearla después de la primera consulta.
                try (PreparedStatement buscar = conn.prepareStatement(buscarSql)) {
                    buscar.setString(1, nombre);
                    try (ResultSet rs = buscar.executeQuery()) {
                        if (rs.next()) {
                            return rs.getInt("id_categoria");
                        }
                    }
                }
                throw e;
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("No se pudo crear la categoría indicada.");
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
