package com.gameverse.controller;

import com.gameverse.model.dao.VideojuegoDAO;
import com.gameverse.model.dto.Categoria;
import com.gameverse.model.dto.Videojuego;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Named("videojuegoBean")
@RequestScoped
public class VideojuegoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Videojuego videojuego;
    private List<Videojuego> listaVideojuegos;
    private List<Categoria> listaCategorias;
    private VideojuegoDAO videojuegoDAO;

    @PostConstruct
    public void init() {
        videojuego = new Videojuego();
        videojuego.setCategoriaObjeto(new Categoria());
        videojuegoDAO = new VideojuegoDAO();
        cargarDatos();
    }

    public void cargarDatos() {
        try {
            listaVideojuegos = videojuegoDAO.obtenerTodos();
        } catch (SQLException e) {
            listaVideojuegos = new ArrayList<>();
            e.printStackTrace();
        }

        // Categorías de ejemplo para el combo box
        listaCategorias = new ArrayList<>();

        Categoria c1 = new Categoria();
        c1.setId(1);
        c1.setNombre("Acción");

        Categoria c2 = new Categoria();
        c2.setId(2);
        c2.setNombre("Aventura");

        Categoria c3 = new Categoria();
        c3.setId(3);
        c3.setNombre("RPG");

        listaCategorias.add(c1);
        listaCategorias.add(c2);
        listaCategorias.add(c3);
    }

    public void guardar() {
        try {
            if (videojuego.getCategoriaObjeto() != null) {
                videojuego.setCategoria(videojuego.getCategoriaObjeto().getNombre());
            }
            videojuegoDAO.insertar(videojuego);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito:", "Videojuego registrado correctamente."));

            videojuego = new Videojuego();
            videojuego.setCategoriaObjeto(new Categoria());
            cargarDatos();
        } catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage()));
        }
    }

    public Videojuego getVideojuego() { return videojuego; }
    public void setVideojuego(Videojuego videojuego) { this.videojuego = videojuego; }
    public List<Videojuego> getListaVideojuegos() { return listaVideojuegos; }
    public void setListaVideojuegos(List<Videojuego> listaVideojuegos) { this.listaVideojuegos = listaVideojuegos; }
    public List<Categoria> getListaCategorias() { return listaCategorias; }
    public void setListaCategorias(List<Categoria> listaCategorias) { this.listaCategorias = listaCategorias; }
}