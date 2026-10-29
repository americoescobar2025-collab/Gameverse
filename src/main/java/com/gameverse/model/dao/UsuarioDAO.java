package com.gameverse.model.dao;

import com.gameverse.config.DatabaseConfig;
import com.gameverse.model.dto.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos de usuarios mediante JDBC y MySQL.
 * Permite listar, buscar por ID y registrar usuarios.
 * Utiliza DatabaseConfig para obtener conexiones.
 */
public class UsuarioDAO {

    /** Consulta todos los usuarios registrados en MySQL. */
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

    /** Busca un usuario por su identificador. Devuelve Optional.empty si no existe. */
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

    /** Inserta un usuario con su hash de contrasena y devuelve el ID generado. */
    public int registrar(String nombre, String correo, String passwordHash) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, correo, password_hash) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.setString(2, correo);
            ps.setString(3, passwordHash);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            throw new SQLException("No se pudo obtener el ID del usuario registrado.");
        }
    }

    /** Convierte una fila de MySQL en un objeto Usuario, sin exponer el hash. */
    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("id_usuario"),
            rs.getString("nombre"),
            rs.getString("correo"),
            rs.getBoolean("activo")
        );
    }
}
