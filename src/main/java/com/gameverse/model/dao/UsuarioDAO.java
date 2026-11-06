
package com.gameverse.model.dao;

import com.gameverse.config.DatabaseConfig;
import com.gameverse.model.dto.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos de usuarios mediante JDBC y MySQL.
 * Permite listar, buscar por ID, buscar por correo y registrar usuarios.
 */
public class UsuarioDAO {

    /** Consulta todos los usuarios registrados. */
    public List<Usuario> obtenerTodos() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id_usuario, nombre, correo, activo FROM usuarios ORDER BY id_usuario";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapear(rs));
            }
        }
        return usuarios;
    }

    /** Busca un usuario por su identificador. */
    public Optional<Usuario> obtenerPorId(int id) throws SQLException {
        String sql = "SELECT id_usuario, nombre, correo, activo FROM usuarios WHERE id_usuario = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        }
    }

    /** Busca un usuario por correo y recupera su hash de contraseña. */
    public Optional<Usuario> obtenerPorCorreo(String correo) throws SQLException {
        String sql = "SELECT id_usuario, nombre, correo, activo, password_hash " +
                     "FROM usuarios WHERE correo = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }

                Usuario usuario = mapear(rs);
                usuario.setPasswordHash(rs.getString("password_hash"));

                return Optional.of(usuario);
            }
        }
    }

    /** Registra un usuario con su contraseña protegida mediante hash. */
    public int registrar(String nombre, String correo, String passwordHash) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, correo, password_hash) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nombre);
            ps.setString(2, correo);
            ps.setString(3, passwordHash);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            throw new SQLException("No se pudo obtener el ID del usuario registrado.");
        }
    }

    /** Convierte una fila de MySQL en un objeto Usuario. */
    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("id_usuario"),
            rs.getString("nombre"),
            rs.getString("correo"),
            rs.getBoolean("activo")
        );
    }
}
