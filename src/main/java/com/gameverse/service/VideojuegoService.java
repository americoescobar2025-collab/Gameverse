package com.gameverse.service;

import com.gameverse.inventory.InventoryService;
import com.gameverse.model.dto.Videojuego;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class VideojuegoService {

    private final InventoryService inventoryService;

    @Inject
    public VideojuegoService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public List<Videojuego> listar() {
        return inventoryService.obtenerInventario();
    }

    public Optional<Videojuego> buscarPorId(int id) {
        return inventoryService.buscarPorId(id);
    }

    public Videojuego crear(Videojuego videojuego) {
        if (videojuego == null || videojuego.getId() != 0) {
            throw new IllegalArgumentException("Para crear un videojuego, el identificador debe estar vacío.");
        }
        inventoryService.guardar(videojuego);
        return videojuego;
    }

    public void actualizar(Videojuego videojuego) {
        if (videojuego == null || videojuego.getId() <= 0) {
            throw new IllegalArgumentException("El identificador del videojuego no es válido.");
        }
        inventoryService.guardar(videojuego);
    }

    public void eliminar(int id) {
        inventoryService.eliminar(id);
    }
}
