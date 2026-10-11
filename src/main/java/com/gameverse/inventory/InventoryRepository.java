package com.gameverse.inventory;

import com.gameverse.model.dto.Videojuego;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository {

    List<Videojuego> obtenerInventario();

    Optional<Videojuego> buscarPorId(int id);

    void guardar(Videojuego videojuego);

    void eliminar(int id);

    boolean ajustarStock(int id, int cambio);
}
