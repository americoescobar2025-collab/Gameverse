package com.gameverse.model.dao;

import com.gameverse.inventory.InventoryRepository;
import com.gameverse.model.dto.Videojuego;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class InventoryDAO implements InventoryRepository {

    private final VideojuegoDAO videojuegoDAO;

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
