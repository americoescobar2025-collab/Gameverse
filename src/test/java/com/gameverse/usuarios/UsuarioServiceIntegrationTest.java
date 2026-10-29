package com.gameverse.usuarios;

import com.gameverse.config.DatabaseConfig;
import com.gameverse.model.dto.Usuario;
import com.gameverse.service.UsuarioService;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba de integracion del modulo de usuarios.
 * Comprueba el registro real en MySQL y la consulta por ID.
 * Elimina el usuario temporal al finalizar.
 */
public class UsuarioServiceIntegrationTest {

    /**
     * Registra un usuario temporal, comprueba que exista y
     * verifica que la contrasena se almacene como hash.
     */
    @Test
    void registrarYConsultarUsuario() throws Exception {
        UsuarioService servicio = new UsuarioService();
        String correo = "prueba-" + UUID.randomUUID() + "@gameverse.test";
        int id = 0;

        try {
            // Registra el usuario mediante el servicio real.
            id = servicio.registrar(
                "Usuario de Prueba",
                correo,
                "ContrasenaSegura2026!"
            );

            // Comprueba que el usuario se puede consultar por ID.
            Optional<Usuario> resultado = servicio.buscar(id);
            assertTrue(resultado.isPresent());
            assertEquals(correo, resultado.get().getCorreo());
            assertEquals("Usuario de Prueba", resultado.get().getNombre());

            // Verifica que MySQL no almacene la contrasena original.
            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                     "SELECT password_hash FROM usuarios WHERE id_usuario = ?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next());
                    String hash = rs.getString("password_hash");
                    assertTrue(hash.startsWith("pbkdf2_sha256$"));
                    assertNotEquals("ContrasenaSegura2026!", hash);
                }
            }
        } finally {
            // Limpia el usuario temporal aunque falle una comprobacion.
            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM usuarios WHERE correo = ?")) {
                ps.setString(1, correo);
                ps.executeUpdate();
            }
        }
    }
}