
package com.gameverse.model.dao;

import com.gameverse.config.DatabaseConfig;
import com.gameverse.model.dto.Usuario;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos de usuarios mediante JDBC y MySQL.
 *
 * INTEGRACIÓN FASE 3:
 * - Módulo de usuarios: consultas y registro.
 * - Actividad 3: autenticación y manejo de correos duplicados.
 *
 * Se resolvió el conflicto del método registrar()
 * conservando el registro seguro y el manejo de errores.
 */
@ApplicationScoped
public class UsuarioDAO {

    /**
     * Consulta todos los usuarios registrados.
     *
     * No devuelve hashes de contraseñas.
     */
    public List<Usuario> obtenerTodos() throws SQLException {

        List<Usuario> usuarios = new ArrayList<>();

        String sql =
                "SELECT id_usuario, nombre, correo, activo " +
                        "FROM usuarios ORDER BY id_usuario";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapear(rs));
            }
        }

        return usuarios;
    }

    /**
     * Busca un usuario por su identificador.
     *
     * No recupera el hash porque no es necesario
     * para una consulta ordinaria.
     */
    public Optional<Usuario> obtenerPorId(int id) throws SQLException {

        String sql =
                "SELECT id_usuario, nombre, correo, activo " +
                        "FROM usuarios WHERE id_usuario = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next()
                        ? Optional.of(mapear(rs))
                        : Optional.empty();
            }
        }
    }

    /**
     * Busca un usuario por correo electrónico.
     *
     * ACTIVIDAD 3 - SEGURIDAD:
     * Recupera el hash de contraseña para permitir
     * que el servicio de autenticación lo verifique.
     *
     * No se recupera una contraseña en texto plano.
     */
    public Optional<Usuario> obtenerPorCorreo(String correo)
            throws SQLException {

        String sql =
                "SELECT id_usuario, nombre, correo, activo, password_hash " +
                        "FROM usuarios WHERE correo = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return Optional.empty();
                }

                Usuario usuario = mapear(rs);

                usuario.setPasswordHash(
                        rs.getString("password_hash")
                );

                return Optional.of(usuario);
            }
        }
    }

    /**
     * Registra un usuario con su contraseña protegida mediante hash.
     *
     * INTEGRACIÓN FASE 3 - CONFLICTO RESUELTO:
     *
     * Se conserva el INSERT preparado y la recuperación
     * del identificador generado.
     *
     * También se conserva el manejo específico del error
     * MySQL 1062, correspondiente a una clave duplicada.
     *
     * Esto permite informar cuando un correo ya existe,
     * sin eliminar el control de errores JDBC.
     *
     * @param nombre nombre del usuario
     * @param correo correo electrónico
     * @param passwordHash hash generado por el servicio
     * @return identificador generado por MySQL
     * @throws SQLException si falla el registro
     */
    public int registrar(
            String nombre,
            String correo,
            String passwordHash
    ) throws SQLException {

        String sql =
                "INSERT INTO usuarios " +
                        "(nombre, correo, password_hash) " +
                        "VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS
             )) {

            ps.setString(1, nombre);
            ps.setString(2, correo);
            ps.setString(3, passwordHash);

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                throw new SQLException(
                        "No se pudo registrar el usuario."
                );
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            throw new SQLException(
                    "No se pudo obtener el ID del usuario registrado."
            );

        } catch (SQLException e) {

            /*
             * INTEGRACIÓN - SEGURIDAD:
             *
             * MySQL utiliza el código 1062 cuando se intenta
             * insertar un valor duplicado en una clave única.
             *
             * Se conserva la excepción original como causa
             * para facilitar el diagnóstico técnico.
             */
            if (e.getErrorCode() == 1062
                    && "23000".equals(e.getSQLState())) {

                throw new SQLException(
                        "Ya existe un usuario registrado con ese correo.",
                        e.getSQLState(),
                        e.getErrorCode(),
                        e
                );
            }

            throw e;
        }
    }

    /**
     * Convierte una fila de MySQL en un objeto Usuario.
     *
     * Se reutiliza para evitar duplicar el mapeo
     * de datos entre los diferentes métodos.
     */
    private Usuario mapear(ResultSet rs) throws SQLException {

        return new Usuario(
                rs.getInt("id_usuario"),
                rs.getString("nombre"),
                rs.getString("correo"),
                rs.getBoolean("activo")
        );
    }
}
