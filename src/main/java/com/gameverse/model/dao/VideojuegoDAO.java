
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

/**
 * Acceso JDBC al catálogo y al inventario de videojuegos.
 *
 * INTEGRACIÓN FASE 3 (2A + 2B):
 * Se unifican las operaciones CRUD, la creación automática
 * de categorías y los métodos de inventario.
 *
 * CORRECCIÓN DE COMPILACIÓN:
 * Se implementa InventoryRepository para que InventoryService
 * pueda recibir una instancia de VideojuegoDAO.
 */
@ApplicationScoped
public class VideojuegoDAO implements InventoryRepository {

    private static final String CONSULTA_INVENTARIO =
            "SELECT v.id_videojuego, v.titulo, v.descripcion, " +
                    "v.precio, v.stock, v.imagen_url, " +
                    "c.nombre AS categoria " +
                    "FROM videojuegos v " +
                    "LEFT JOIN categorias c " +
                    "ON v.id_categoria = c.id_categoria";

    /**
     * Obtiene todos los videojuegos registrados.
     */
    public List<Videojuego> obtenerTodos() throws SQLException {
        List<Videojuego> lista = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps =
                     conn.prepareStatement(CONSULTA_INVENTARIO);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearVideojuego(rs));
            }
        }

        return lista;
    }

    /**
     * Alias utilizado por los servicios JDBC de la Actividad 2B.
     */
    public List<Videojuego> listar() throws SQLException {
        return obtenerTodos();
    }

    /**
     * Registra un videojuego utilizando el método crear().
     */
    public void insertar(Videojuego videojuego) throws SQLException {
        crear(videojuego);
    }

    /**
     * INTEGRACIÓN 2A + 2B:
     *
     * Se conserva la creación automática de categorías de 2A
     * y el identificador generado que necesitan los servicios.
     *
     * La operación utiliza una transacción para evitar
     * registros parcialmente guardados.
     */
    public int crear(Videojuego videojuego) throws SQLException {

        String sql =
                "INSERT INTO videojuegos " +
                        "(titulo, descripcion, precio, stock, imagen_url, id_categoria) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection()) {

            boolean autoCommit = conn.getAutoCommit();

            try {
                conn.setAutoCommit(false);

                int idCategoria =
                        obtenerOCrearCategoria(conn, videojuego.getCategoria());

                int id;

                try (PreparedStatement ps =
                             conn.prepareStatement(
                                     sql, Statement.RETURN_GENERATED_KEYS)) {

                    ps.setString(1, videojuego.getTitulo());
                    ps.setString(2, videojuego.getDescripcion());
                    ps.setDouble(3, videojuego.getPrecio());
                    ps.setInt(4, videojuego.getStock());

                    ps.setString(5,
                            videojuego.getImagenUrl() == null ||
                                    videojuego.getImagenUrl().isBlank()
                                    ? null
                                    : videojuego.getImagenUrl());

                    ps.setInt(6, idCategoria);

                    if (ps.executeUpdate() == 0) {
                        throw new SQLException(
                                "No se pudo registrar el videojuego.");
                    }

                    try (ResultSet rs = ps.getGeneratedKeys()) {

                        if (!rs.next()) {
                            throw new SQLException(
                                    "No se pudo obtener el identificador generado.");
                        }

                        id = rs.getInt(1);
                    }
                }

                conn.commit();
                videojuego.setId(id);

                return id;

            } catch (SQLException | RuntimeException e) {

                // INTEGRACIÓN: revertir la operación si ocurre un error.
                try {
                    conn.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                throw e;

            } finally {
                conn.setAutoCommit(autoCommit);
            }
        }
    }

    /**
     * INTEGRACIÓN 2A + 2B:
     *
     * Actualiza un videojuego y permite registrar
     * una categoría nueva si todavía no existe.
     *
     * Se utiliza una transacción JDBC.
     */
    public void actualizar(Videojuego videojuego) throws SQLException {

        String sql =
                "UPDATE videojuegos SET titulo = ?, descripcion = ?, " +
                        "precio = ?, stock = ?, imagen_url = ?, id_categoria = ? " +
                        "WHERE id_videojuego = ?";

        try (Connection conn = DatabaseConfig.getConnection()) {

            boolean autoCommit = conn.getAutoCommit();

            try {
                conn.setAutoCommit(false);

                int idCategoria =
                        obtenerOCrearCategoria(conn, videojuego.getCategoria());

                try (PreparedStatement ps = conn.prepareStatement(sql)) {

                    ps.setString(1, videojuego.getTitulo());
                    ps.setString(2, videojuego.getDescripcion());
                    ps.setDouble(3, videojuego.getPrecio());
                    ps.setInt(4, videojuego.getStock());
                    ps.setString(5, videojuego.getImagenUrl());
                    ps.setInt(6, idCategoria);
                    ps.setInt(7, videojuego.getId());

                    if (ps.executeUpdate() == 0 &&
                            !existe(conn, videojuego.getId())) {

                        throw new SQLException(
                                "No se encontró el videojuego a actualizar.");
                    }
                }

                conn.commit();

            } catch (SQLException | RuntimeException e) {

                // INTEGRACIÓN: revertir cambios ante cualquier fallo.
                try {
                    conn.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                throw e;

            } finally {
                conn.setAutoCommit(autoCommit);
            }
        }
    }

    /**
     * Devuelve el inventario y adapta los errores JDBC.
     *
     * Implementación del contrato InventoryRepository.
     */
    @Override
    public List<Videojuego> obtenerInventario() {

        try {
            return obtenerTodos();

        } catch (SQLException e) {
            throw new InventoryPersistenceException(
                    "No se pudo consultar el inventario.", e);
        }
    }

    /**
     * Busca un videojuego mediante su identificador.
     */
    @Override
    public Optional<Videojuego> buscarPorId(int id) {

        String sql =
                CONSULTA_INVENTARIO + " WHERE v.id_videojuego = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next()
                        ? Optional.of(mapearVideojuego(rs))
                        : Optional.empty();
            }

        } catch (SQLException e) {
            throw new InventoryPersistenceException(
                    "No se pudo consultar el videojuego.", e);
        }
    }

    /**
     * INTEGRACIÓN:
     * Se evita duplicar la lógica SQL de actualización.
     *
     * Si el videojuego es nuevo, se crea.
     * Si ya existe, se actualiza.
     */
    @Override
    public void guardar(Videojuego videojuego) {

        try {
            if (videojuego.getId() <= 0) {
                crear(videojuego);
            } else {
                actualizar(videojuego);
            }

        } catch (SQLException e) {
            throw new InventoryPersistenceException(
                    "No se pudo guardar el videojuego.", e);
        }
    }

    /**
     * Elimina un videojuego mediante su identificador.
     */
    @Override
    public void eliminar(int id) {

        String sql =
                "DELETE FROM videojuegos WHERE id_videojuego = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            if (ps.executeUpdate() == 0) {
                throw new InventoryPersistenceException(
                        "No se encontró el videojuego a eliminar.");
            }

        } catch (SQLException e) {
            throw new InventoryPersistenceException(
                    "No se pudo eliminar el videojuego.", e);
        }
    }

    /**
     * Ajusta el stock sumando o restando unidades.
     * No permite cantidades finales negativas.
     */
    @Override
    public boolean ajustarStock(int id, int cambio) {

        String sql =
                "UPDATE videojuegos SET stock = stock + ? " +
                        "WHERE id_videojuego = ? " +
                        "AND CAST(stock AS SIGNED) + ? BETWEEN 0 AND 2147483647";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cambio);
            ps.setInt(2, id);
            ps.setInt(3, cambio);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new InventoryPersistenceException(
                    "No se pudo ajustar el stock.", e);
        }
    }

    /**
     * ACTIVIDAD 2B:
     * Establece un valor absoluto de stock.
     *
     * Se conserva la comprobación de existencia
     * cuando el valor almacenado ya es igual.
     */
    @Override
    public boolean actualizarStock(int id, int nuevoStock) {

        if (id <= 0 || nuevoStock < 0) {
            return false;
        }

        String sql =
                "UPDATE videojuegos SET stock = ? " +
                        "WHERE id_videojuego = ? AND ? >= 0";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, nuevoStock);
            ps.setInt(2, id);
            ps.setInt(3, nuevoStock);

            return ps.executeUpdate() > 0 || existe(conn, id);

        } catch (SQLException e) {
            throw new InventoryPersistenceException(
                    "No se pudo actualizar el stock.", e);
        }
    }

    /**
     * Comprueba si un videojuego existe en la base de datos.
     */
    private boolean existe(Connection connection, int id)
            throws SQLException {

        String sql =
                "SELECT 1 FROM videojuegos WHERE id_videojuego = ?";

        try (PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * ACTIVIDAD 2A:
     *
     * Busca una categoría existente por nombre.
     * Si no existe, la crea automáticamente.
     *
     * INTEGRACIÓN:
     * Se conserva la protección frente a inserciones
     * concurrentes de la misma categoría.
     */
    private int obtenerOCrearCategoria(Connection conn, String nombre)
            throws SQLException {

        String buscarSql =
                "SELECT id_categoria FROM categorias " +
                        "WHERE LOWER(TRIM(nombre)) = LOWER(TRIM(?))";

        try (PreparedStatement ps = conn.prepareStatement(buscarSql)) {

            ps.setString(1, nombre);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_categoria");
                }
            }
        }

        String insertarSql =
                "INSERT INTO categorias (nombre) VALUES (?)";

        try (PreparedStatement ps =
                     conn.prepareStatement(
                             insertarSql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nombre);

            try {
                ps.executeUpdate();

            } catch (SQLIntegrityConstraintViolationException e) {

                // Otra solicitud pudo crear la categoría primero.
                try (PreparedStatement buscar =
                             conn.prepareStatement(buscarSql)) {

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

        throw new SQLException(
                "No se pudo crear la categoría indicada.");
    }

    /**
     * Convierte una fila SQL en un objeto Videojuego.
     */
    private Videojuego mapearVideojuego(ResultSet rs)
            throws SQLException {

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
