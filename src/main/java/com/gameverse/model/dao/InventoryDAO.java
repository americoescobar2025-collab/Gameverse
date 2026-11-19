
package com.gameverse.model.dao;

import com.gameverse.inventory.InventoryRepository;
import com.gameverse.model.dto.Videojuego;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

/**
 * DAO de inventario de Gameverse.
 *
 * INTEGRACIÓN FASE 3:
 * - Conserva las operaciones JDBC de la Actividad 2B.
 * - Mantiene la compatibilidad con InventoryRepository.
 *
 * CORRECCIÓN CDI:
 * Se agrega un constructor sin argumentos para que Weld
 * pueda crear el proxy de este bean @ApplicationScoped.
 *
 * El constructor @Inject continúa siendo el utilizado
 * para inyectar VideojuegoDAO durante la ejecución.
 */
@ApplicationScoped
public class InventoryDAO implements InventoryRepository {

    private VideojuegoDAO videojuegoDAO;

    /**
     * Constructor requerido para la creación del proxy CDI.
     */
    protected InventoryDAO() {
    }

    /**
     * Constructor utilizado por CDI para inyectar VideojuegoDAO.
     */
    @Inject
    public InventoryDAO(VideojuegoDAO videojuegoDAO) {
        this.videojuegoDAO = videojuegoDAO;
    }

    @Override
    public List<Videojuego> obtenerInventario() {
        return videojuegoDAO.obtenerInventario();
    }

    @Override
    public Optional<Videojuego> buscarPorId(int id) {
        return videojuegoDAO.buscarPorId(id);
    }

    @Override
    public void guardar(Videojuego videojuego) {
        videojuegoDAO.guardar(videojuego);
    }

    @Override
    public void eliminar(int id) {
        videojuegoDAO.eliminar(id);
    }

    @Override
    public boolean ajustarStock(int id, int cambio) {
        return videojuegoDAO.ajustarStock(id, cambio);
    }

    @Override
    public boolean actualizarStock(int id, int nuevoStock) {
        return videojuegoDAO.actualizarStock(id, nuevoStock);
    }
}
