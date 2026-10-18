package com.gameverse.inventory;

import com.gameverse.model.dto.Videojuego;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InventoryServiceTest {

    private InMemoryInventoryRepository repository;
    private InventoryService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryInventoryRepository();
        service = new InventoryService(repository);
    }

    @Test
    void guardarNormalizaCamposYDelegaEnRepositorio() {
        Videojuego videojuego = new Videojuego(
                0, "  Juego  ", "  Descripción válida  ", 19.99, 4, " https://example.com/game.jpg ", " Acción "
        );

        service.guardar(videojuego);

        assertEquals("Juego", repository.guardado.getTitulo());
        assertEquals("Descripción válida", repository.guardado.getDescripcion());
        assertEquals("Acción", repository.guardado.getCategoria());
        assertEquals("https://example.com/game.jpg", repository.guardado.getImagenUrl());
    }

    @Test
    void guardarRechazaPrecioConMasDeDosDecimales() {
        Videojuego videojuego = juegoValido();
        videojuego.setPrecio(12.345);

        assertThrows(IllegalArgumentException.class, () -> service.guardar(videojuego));
        assertFalse(repository.guardarLlamado);
    }

    @Test
    void guardarRechazaUrlQueNoSeaHttp() {
        Videojuego videojuego = juegoValido();
        videojuego.setImagenUrl("javascript:alert(1)");

        assertThrows(IllegalArgumentException.class, () -> service.guardar(videojuego));
        assertFalse(repository.guardarLlamado);
    }

    @Test
    void ajustarStockRechazaCuandoElResultadoSeriaNegativo() {
        repository.stockAjustable = false;

        assertThrows(IllegalArgumentException.class, () -> service.ajustarStock(1, -1));
    }

    @Test
    void ajustarStockRechazaIdentificadoresInvalidosYCambiosCero() {
        assertThrows(IllegalArgumentException.class, () -> service.ajustarStock(0, 1));
        assertThrows(IllegalArgumentException.class, () -> service.ajustarStock(1, 0));
    }

    private Videojuego juegoValido() {
        return new Videojuego(0, "Juego válido", "Descripción válida", 19.99, 4, null, "Acción");
    }

    private static class InMemoryInventoryRepository implements InventoryRepository {

        private final List<Videojuego> videojuegos = new ArrayList<>();
        private Videojuego guardado;
        private boolean guardarLlamado;
        private boolean stockAjustable = true;

        @Override
        public List<Videojuego> obtenerInventario() {
            return List.copyOf(videojuegos);
        }

        @Override
        public Optional<Videojuego> buscarPorId(int id) {
            return videojuegos.stream().filter(juego -> juego.getId() == id).findFirst();
        }

        @Override
        public void guardar(Videojuego videojuego) {
            guardado = videojuego;
            guardarLlamado = true;
        }

        @Override
        public void eliminar(int id) {
            videojuegos.removeIf(juego -> juego.getId() == id);
        }

        @Override
        public boolean ajustarStock(int id, int cambio) {
            return stockAjustable;
        }
    }
}
