package com.gameverse.inventory;

import com.gameverse.model.dto.Videojuego;
import com.gameverse.service.VideojuegoService;
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
    void guardarRechazaStockNegativo() {
        Videojuego videojuego = juegoValido();
        videojuego.setStock(-1);

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

    @Test
    void actualizarStockValidaIdYRechazaValoresNegativos() {
        assertThrows(IllegalArgumentException.class, () -> service.actualizarStock(0, 1));
        assertThrows(IllegalArgumentException.class, () -> service.actualizarStock(1, -1));
        assertFalse(repository.actualizarStockLlamado);
    }

    @Test
    void actualizarStockDelegaElValorAbsolutoAlRepositorio() {
        service.actualizarStock(1, 12);

        assertEquals(12, repository.stockActualizado);
        assertEquals(1, repository.idStockActualizado);
    }

    @Test
    void videojuegoServiceExponeLasOperacionesDelInventario() {
        VideojuegoService videojuegoService = new VideojuegoService(service);
        Videojuego videojuego = juegoValido();

        assertEquals(List.of(), videojuegoService.listar());
        assertEquals(videojuego, videojuegoService.crear(videojuego));
        assertEquals(videojuego, repository.guardado);
    }

    @Test
    void videojuegoServiceRechazaIdsInvalidosParaCrearYActualizar() {
        VideojuegoService videojuegoService = new VideojuegoService(service);
        Videojuego videojuego = juegoValido();
        videojuego.setId(-1);

        assertThrows(IllegalArgumentException.class, () -> videojuegoService.crear(videojuego));
        assertThrows(IllegalArgumentException.class, () -> videojuegoService.actualizar(videojuego));
        assertFalse(repository.guardarLlamado);
    }

    @Test
    void videojuegoServiceBuscaActualizaYEliminaPorId() {
        Videojuego existente = new Videojuego(
                7, "Juego existente", "Descripción existente", 19.99, 4, null, "Acción"
        );
        repository.videojuegos.add(existente);
        VideojuegoService videojuegoService = new VideojuegoService(service);

        assertEquals(Optional.of(existente), videojuegoService.buscarPorId(7));
        videojuegoService.actualizar(existente);
        assertEquals(existente, repository.guardado);

        videojuegoService.eliminar(7);
        assertEquals(List.of(), videojuegoService.listar());
    }

    private Videojuego juegoValido() {
        return new Videojuego(0, "Juego válido", "Descripción válida", 19.99, 4, null, "Acción");
    }

    private static class InMemoryInventoryRepository implements InventoryRepository {

        private final List<Videojuego> videojuegos = new ArrayList<>();
        private Videojuego guardado;
        private boolean guardarLlamado;
        private boolean stockAjustable = true;
        private boolean actualizarStockLlamado;
        private int stockActualizado;
        private int idStockActualizado;

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

        @Override
        public boolean actualizarStock(int id, int nuevoStock) {
            actualizarStockLlamado = true;
            idStockActualizado = id;
            stockActualizado = nuevoStock;
            return stockAjustable;
        }
    }
}
