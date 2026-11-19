
package com.gameverse.service;

import com.gameverse.model.dao.UsuarioDAO;
import com.gameverse.model.dto.Usuario;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.security.MessageDigest;
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
 * Lógica de negocio del módulo de usuarios de Gameverse.
 *
 * INTEGRACIÓN FASE 3:
 *
 * - Módulo de usuarios: consultas, registro y validaciones.
 * - Actividad 2A: métodos compatibles con la API REST.
 * - Actividad 3: autenticación y protección de contraseñas.
 *
 * Se resolvieron los conflictos conservando la inyección
 * de dependencias y las funcionalidades de ambas ramas.
 */
@ApplicationScoped
public class UsuarioService {

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Número de iteraciones utilizadas al generar
     * nuevas contraseñas mediante PBKDF2.
     */
    private static final int ITERACIONES = 600000;

    private static final Pattern CORREO =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private final UsuarioDAO dao;

    /**
     * Constructor sin argumentos.
     *
     * INTEGRACIÓN:
     * Se conserva para permitir el uso del servicio
     * fuera de un contenedor CDI cuando sea necesario.
     */
    public UsuarioService() {
        this(new UsuarioDAO());
    }

    /**
     * Constructor con inyección de dependencias.
     *
     * INTEGRACIÓN:
     * Permite que CDI proporcione el DAO y facilita
     * la utilización de implementaciones de prueba.
     */
    @Inject
    public UsuarioService(UsuarioDAO dao) {
        this.dao = dao;
    }

    /**
     * Obtiene el listado de usuarios.
     */
    public List<Usuario> listar() throws SQLException {
        return dao.obtenerTodos();
    }

    /**
     * Método de compatibilidad con los consumidores
     * que utilizan el nombre listarUsuarios().
     *
     * INTEGRACIÓN ACTIVIDAD 2A:
     * Se conserva para evitar romper llamadas existentes.
     */
    public List<Usuario> listarUsuarios() throws SQLException {
        return listar();
    }

    /**
     * Busca un usuario por su identificador.
     */
    public Optional<Usuario> buscar(int id) throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "ID inválido."
            );
        }

        return dao.obtenerPorId(id);
    }

    /**
     * Método de compatibilidad con los consumidores
     * que utilizan el nombre buscarUsuario().
     *
     * INTEGRACIÓN ACTIVIDAD 2A:
     * Se conserva para evitar romper llamadas existentes.
     */
    public Optional<Usuario> buscarUsuario(int id)
            throws SQLException {

        return buscar(id);
    }

    /**
     * Valida los datos y registra un usuario.
     *
     * INTEGRACIÓN MÓDULO DE USUARIOS + SEGURIDAD:
     *
     * - Elimina espacios innecesarios del nombre.
     * - Normaliza el correo electrónico.
     * - Exige contraseñas de al menos 12 caracteres.
     * - Almacena únicamente el hash de la contraseña.
     */
    public int registrar(
            String nombre,
            String correo,
            String password
    ) throws SQLException {

        String nombreNormalizado =
                nombre == null ? null : nombre.trim();

        String correoNormalizado =
                correo == null ? null : correo.trim();

        if (nombreNormalizado == null
                || nombreNormalizado.isBlank()
                || nombreNormalizado.length() > 100) {

            throw new IllegalArgumentException(
                    "Nombre inválido."
            );
        }

        if (correoNormalizado == null
                || correoNormalizado.length() > 150
                || !CORREO.matcher(correoNormalizado).matches()) {

            throw new IllegalArgumentException(
                    "Correo inválido."
            );
        }

        if (password == null || password.length() < 12) {

            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 12 caracteres."
            );
        }

        return dao.registrar(
                nombreNormalizado,
                correoNormalizado.toLowerCase(Locale.ROOT),
                generarHash(password)
        );
    }

    /**
     * Autentica un usuario mediante correo y contraseña.
     *
     * ACTIVIDAD 3 - SEGURIDAD:
     *
     * - Comprueba los datos recibidos.
     * - Busca el usuario por correo.
     * - Rechaza usuarios inactivos.
     * - Verifica la contraseña mediante PBKDF2.
     * - Elimina el hash del objeto antes de devolverlo.
     */
    public Optional<Usuario> autenticar(
            String correo,
            String password
    ) throws SQLException {

        if (correo == null
                || correo.isBlank()
                || !CORREO.matcher(correo.trim()).matches()
                || password == null
                || password.isEmpty()) {

            return Optional.empty();
        }

        Optional<Usuario> resultado =
                dao.obtenerPorCorreo(
                        correo.trim().toLowerCase(Locale.ROOT)
                );

        if (resultado.isEmpty()) {
            return Optional.empty();
        }

        Usuario usuario = resultado.get();

        if (!usuario.isActivo()
                || !verificarPassword(
                password,
                usuario.getPasswordHash()
        )) {

            return Optional.empty();
        }

        /*
         * SEGURIDAD:
         * El hash se utiliza únicamente para comprobar
         * la contraseña y no debe devolverse al exterior.
         */
        usuario.setPasswordHash(null);

        return Optional.of(usuario);
    }

    /**
     * Genera un hash PBKDF2WithHmacSHA256.
     *
     * Se utiliza un salt aleatorio de 16 bytes,
     * 600,000 iteraciones y una clave de 256 bits.
     *
     * Formato almacenado:
     *
     * pbkdf2_sha256$iteraciones$salt$hash
     */
    private String generarHash(String password) {

        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);

        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                ITERACIONES,
                256
        );

        try {

            byte[] hash = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec)
                    .getEncoded();

            return "pbkdf2_sha256$"
                    + ITERACIONES
                    + "$"
                    + Base64.getEncoder().encodeToString(salt)
                    + "$"
                    + Base64.getEncoder().encodeToString(hash);

        } catch (InvalidKeySpecException
                 | NoSuchAlgorithmException e) {

            throw new IllegalStateException(
                    "Error al proteger la contraseña.",
                    e
            );

        } finally {
            spec.clearPassword();
        }
    }

    /**
     * Verifica una contraseña contra el hash almacenado.
     *
     * ACTIVIDAD 3 - SEGURIDAD:
     *
     * Se conserva la comparación mediante
     * MessageDigest.isEqual() para evitar utilizar
     * una comparación directa de cadenas.
     */
    private boolean verificarPassword(
            String password,
            String hashAlmacenado
    ) {

        if (password == null || hashAlmacenado == null) {
            return false;
        }

        String[] partes = hashAlmacenado.split("\\$");

        if (partes.length != 4
                || !"pbkdf2_sha256".equals(partes[0])) {

            return false;
        }

        try {

            int iteraciones = Integer.parseInt(partes[1]);

            if (iteraciones <= 0 || iteraciones > 1000000) {
                return false;
            }

            byte[] salt =
                    Base64.getDecoder().decode(partes[2]);

            byte[] hashOriginal =
                    Base64.getDecoder().decode(partes[3]);

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
                        hashOriginal,
                        hashCalculado
                );

            } finally {
                spec.clearPassword();
            }

        } catch (IllegalArgumentException e) {

            /*
             * Un hash mal formado o incompatible
             * no debe permitir la autenticación.
             */
            return false;

        } catch (InvalidKeySpecException
                 | NoSuchAlgorithmException e) {

            throw new IllegalStateException(
                    "Error al verificar la contraseña.",
                    e
            );
        }
    }
}
