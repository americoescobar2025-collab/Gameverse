package com.gameverse.service;

import com.gameverse.model.dao.UsuarioDAO;
import com.gameverse.model.dto.Usuario;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsuarioServiceTest {

    @Test
    void registrarNormalizaLosDatosYAlmacenaUnHash() throws SQLException {
        InMemoryUsuarioDAO dao = new InMemoryUsuarioDAO();
        UsuarioService service = new UsuarioService(dao);

        int id = service.registrar("  Ana  ", "  ANA@example.com ", "ContrasenaSegura2026!");

        assertEquals(1, id);
        assertEquals("Ana", dao.nombreRegistrado);
        assertEquals("ana@example.com", dao.correoRegistrado);
        assertTrue(dao.hashRegistrado.startsWith("pbkdf2_sha256$"));
        assertFalse(dao.hashRegistrado.contains("ContrasenaSegura2026!"));
    }

    @Test
    void aliasDeListadoYBusquedaMantienenCompatibilidad() throws SQLException {
        InMemoryUsuarioDAO dao = new InMemoryUsuarioDAO();
        Usuario usuario = new Usuario(3, "Ana", "ana@example.com", true);
        dao.usuarios = List.of(usuario);
        UsuarioService service = new UsuarioService(dao);

        assertEquals(List.of(usuario), service.listarUsuarios());
        assertEquals(Optional.of(usuario), service.buscarUsuario(3));
    }

    @Test
    void registrarRechazaCorreoInvalidoAntesDeAccederAlDao() {
        InMemoryUsuarioDAO dao = new InMemoryUsuarioDAO();
        UsuarioService service = new UsuarioService(dao);

        assertThrows(IllegalArgumentException.class,
                () -> service.registrar("Ana", "correo-invalido", "ContrasenaSegura2026!"));
        assertFalse(dao.registroLlamado);
    }

    @Test
    void buscarRechazaIdsNoPositivos() {
        UsuarioService service = new UsuarioService(new InMemoryUsuarioDAO());

        assertThrows(IllegalArgumentException.class, () -> service.buscarUsuario(0));
        assertThrows(IllegalArgumentException.class, () -> service.buscarUsuario(-1));
    }

    private static class InMemoryUsuarioDAO extends UsuarioDAO {

        private List<Usuario> usuarios = List.of();
        private String nombreRegistrado;
        private String correoRegistrado;
        private String hashRegistrado;
        private boolean registroLlamado;

        @Override
        public List<Usuario> obtenerTodos() {
            return usuarios;
        }

        @Override
        public Optional<Usuario> obtenerPorId(int id) {
            return usuarios.stream().filter(usuario -> usuario.getId() == id).findFirst();
        }

        @Override
        public int registrar(String nombre, String correo, String passwordHash) {
            registroLlamado = true;
            nombreRegistrado = nombre;
            correoRegistrado = correo;
            hashRegistrado = passwordHash;
            return 1;
        }
    }
}
