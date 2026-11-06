
package com.gameverse.service;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.gameverse.model.dao.UsuarioDAO;
import com.gameverse.model.dto.Usuario;

/**
 * Logica de negocio del modulo de usuarios.
 * Valida datos, protege contrasenas y permite autenticar usuarios.
 */
public class UsuarioService {

    private final UsuarioDAO dao = new UsuarioDAO();
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int ITERACIONES = 600000;

    private static final Pattern CORREO =
        Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    /** Obtiene el listado de usuarios. */
    public List<Usuario> listar() throws SQLException {
        return dao.obtenerTodos();
    }

    /** Busca un usuario por ID. */
    public Optional<Usuario> buscar(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID invalido.");
        }
        return dao.obtenerPorId(id);
    }

    /** Valida los datos y registra un usuario. */
    public int registrar(String nombre, String correo, String password)
            throws SQLException {

        if (nombre == null || nombre.isBlank() || nombre.length() > 100) {
            throw new IllegalArgumentException("Nombre invalido.");
        }

        if (correo == null || correo.length() > 150 ||
                !CORREO.matcher(correo).matches()) {
            throw new IllegalArgumentException("Correo invalido.");
        }

        if (password == null || password.length() < 12) {
            throw new IllegalArgumentException(
                "La contrasena debe tener al menos 12 caracteres."
            );
        }

        return dao.registrar(
            nombre.trim(),
            correo.trim().toLowerCase(),
            generarHash(password)
        );
    }

    /** Autentica al usuario mediante correo y contrasena. */
    public Optional<Usuario> autenticar(String correo, String password)
            throws SQLException {

        if (correo == null || correo.isBlank() ||
                !CORREO.matcher(correo.trim()).matches() ||
                password == null || password.isEmpty()) {
            return Optional.empty();
        }

        Optional<Usuario> resultado =
            dao.obtenerPorCorreo(correo.trim().toLowerCase());

        if (resultado.isEmpty()) {
            return Optional.empty();
        }

        Usuario usuario = resultado.get();

        if (!usuario.isActivo() ||
                !verificarPassword(password, usuario.getPasswordHash())) {
            return Optional.empty();
        }

        // No devolver el hash de la contrasena al exterior.
        usuario.setPasswordHash(null);

        return Optional.of(usuario);
    }

    /** Genera un hash PBKDF2 con salt aleatorio. */
    private String generarHash(String password) {

        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);

        PBEKeySpec spec = new PBEKeySpec(
            password.toCharArray(), salt, ITERACIONES, 256
        );

        try {
            byte[] hash = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA256")
                .generateSecret(spec)
                .getEncoded();

            return "pbkdf2_sha256$" + ITERACIONES + "$" +
                Base64.getEncoder().encodeToString(salt) + "$" +
                Base64.getEncoder().encodeToString(hash);

        } catch (Exception e) {
            throw new IllegalStateException(
                "Error al proteger la contrasena.", e
            );
        } finally {
            spec.clearPassword();
        }
    }

    /** Verifica una contrasena comparandola con su hash almacenado. */
    private boolean verificarPassword(String password, String hashAlmacenado) {

        if (password == null || hashAlmacenado == null) {
            return false;
        }

        String[] partes = hashAlmacenado.split("\\$");

        if (partes.length != 4 ||
                !"pbkdf2_sha256".equals(partes[0])) {
            return false;
        }

        try {
            int iteraciones = Integer.parseInt(partes[1]);

            if (iteraciones <= 0 || iteraciones > 1000000) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(partes[2]);
            byte[] hashOriginal = Base64.getDecoder().decode(partes[3]);

            if (salt.length != 16 || hashOriginal.length != 32) {
                return false;
            }

            PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                iteraciones,
                hashOriginal.length * 8
            );

            try {
                byte[] hashCalculado = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec)
                    .getEncoded();

                return MessageDigest.isEqual(
                    hashOriginal, hashCalculado
                );

            } finally {
                spec.clearPassword();
            }

        } catch (IllegalArgumentException e) {
            return false;
        } catch (Exception e) {
            throw new IllegalStateException(
                "Error al verificar la contrasena.", e
            );
        }
    }
}
