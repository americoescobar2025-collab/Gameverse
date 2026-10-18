package com.gameverse.controller;

import com.gameverse.inventory.InventoryPersistenceException;
import com.gameverse.inventory.InventoryService;
import com.gameverse.model.dto.Videojuego;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("inventarioBean")
@ViewScoped
public class InventarioBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(InventarioBean.class.getName());

    private InventoryService inventoryService;

    private List<Videojuego> videojuegos = new ArrayList<>();
    private Videojuego videojuego = new Videojuego();

    public InventarioBean() {
        /* CDI requires a no-argument constructor to proxy a JSF view-scoped bean. */
    }

    @Inject
    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostConstruct
    public void inicializar() {
        cargarInventario();
    }

    public void cargarInventario() {
        try {
            videojuegos = inventoryService.obtenerInventario();
        } catch (InventoryPersistenceException e) {
            registrarError("No se pudo cargar el inventario.", e);
        }
    }

    public void prepararNuevo() {
        videojuego = new Videojuego();
    }

    public void prepararEdicion(int id) {
        try {
            videojuego = inventoryService.buscarPorId(id)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No se encontró el videojuego que deseas editar."
                    ));
        } catch (IllegalArgumentException e) {
            mostrarMensaje(FacesMessage.SEVERITY_WARN, e.getMessage());
        } catch (InventoryPersistenceException e) {
            registrarError("No se pudo cargar el videojuego para editarlo.", e);
        }
    }

    public void guardar() {
        try {
            inventoryService.guardar(videojuego);
            videojuego = new Videojuego();
            cargarInventario();
            mostrarMensaje(FacesMessage.SEVERITY_INFO, "El videojuego se guardó correctamente.");
        } catch (IllegalArgumentException e) {
            mostrarMensaje(FacesMessage.SEVERITY_WARN, e.getMessage());
        } catch (InventoryPersistenceException e) {
            registrarError("No se pudo guardar el videojuego.", e);
        }
    }

    public void eliminar(int id) {
        try {
            inventoryService.eliminar(id);
            cargarInventario();
            mostrarMensaje(FacesMessage.SEVERITY_INFO, "El videojuego se eliminó correctamente.");
        } catch (IllegalArgumentException e) {
            mostrarMensaje(FacesMessage.SEVERITY_WARN, e.getMessage());
        } catch (InventoryPersistenceException e) {
            registrarError("No se pudo eliminar el videojuego.", e);
        }
    }

    public void ajustarStock(int id, int cambio) {
        try {
            inventoryService.ajustarStock(id, cambio);
            cargarInventario();
            mostrarMensaje(FacesMessage.SEVERITY_INFO, "El stock se actualizó correctamente.");
        } catch (IllegalArgumentException e) {
            mostrarMensaje(FacesMessage.SEVERITY_WARN, e.getMessage());
        } catch (InventoryPersistenceException e) {
            registrarError("No se pudo actualizar el stock.", e);
        }
    }

    public List<Videojuego> getVideojuegos() {
        return videojuegos;
    }

    public Videojuego getVideojuego() {
        return videojuego;
    }

    public void setVideojuego(Videojuego videojuego) {
        this.videojuego = videojuego;
    }

    private void mostrarMensaje(FacesMessage.Severity severity, String mensaje) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, mensaje, null));
    }

    private void registrarError(String mensaje, InventoryPersistenceException e) {
        LOGGER.log(Level.SEVERE, mensaje, e);
        mostrarMensaje(FacesMessage.SEVERITY_ERROR, mensaje);
    }
}
