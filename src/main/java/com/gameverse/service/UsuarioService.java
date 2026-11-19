package com.gameverse.service;

import com.gameverse.model.dao.UsuarioDAO;
import com.gameverse.model.dto.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Logica de negocio del modulo de usuarios.
 * Valida datos de registro y genera hashes PBKDF2.
 * Delega la persistencia de usuarios a UsuarioDAO.
 */
@ApplicationScoped
public class UsuarioService {
    private final UsuarioDAO dao;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int ITERACIONES = 600000;
    private static final Pattern CORREO =
        Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    public UsuarioService() {
        this(new UsuarioDAO());
    }

    @Inject
    public UsuarioService(UsuarioDAO dao) {
        this.dao = dao;
    }

    /** Obtiene el listado de usuarios mediante el DAO. */
    public List<Usuario> listar() throws SQLException {
        return dao.obtenerTodos();
    }

    public List<Usuario> listarUsuarios() throws SQLException {
        return listar();
    }

    /** Valida el ID y consulta el usuario correspondiente. */
    public Optional<Usuario> buscar(int id) throws SQLException {
        if (id <= 0) throw new IllegalArgumentException("ID invalido.");
        return dao.obtenerPorId(id);
    }

    public Optional<Usuario> buscarUsuario(int id) throws SQLException {
        return buscar(id);
    }

    /** Valida los datos, protege la contrasena y registra al usuario. */
    public int registrar(String nombre, String correo, String password)
            throws SQLException {
        String nombreNormalizado = nombre == null ? null : nombre.trim();
        String correoNormalizado = correo == null ? null : correo.trim();
        if (nombreNormalizado == null || nombreNormalizado.isBlank()
                || nombreNormalizado.length() > 100)
            throw new IllegalArgumentException("Nombre invalido.");
        if (correoNormalizado == null || correoNormalizado.length() > 150 ||
                !CORREO.matcher(correoNormalizado).matches())
            throw new IllegalArgumentException("Correo invalido.");
        if (password == null || password.length() < 12)
            throw new IllegalArgumentException("La contrasena debe tener al menos 12 caracteres.");

        return dao.registrar(nombreNormalizado, correoNormalizado.toLowerCase(Locale.ROOT),
                             generarHash(password));
    }

    /** Genera un hash PBKDF2 con salt aleatorio para almacenar la contrasena. */
    private String generarHash(String password) {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERACIONES, 256);
        try {
            byte[] hash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                .generateSecret(spec).getEncoded();
            return "pbkdf2_sha256$" + ITERACIONES + "$" +
                Base64.getEncoder().encodeToString(salt) + "$" +
                Base64.getEncoder().encodeToString(hash);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new IllegalStateException("Error al proteger la contrasena.", e);
        } finally {
            spec.clearPassword();
        }
    }
}
