package com.gameverse.model.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "categorias")
public class CategoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "categoria")
    private List<VideojuegoEntity> videojuegos;

    public CategoriaEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<VideojuegoEntity> getVideojuegos() {
        return videojuegos;
    }

    public void setVideojuegos(List<VideojuegoEntity> videojuegos) {
        this.videojuegos = videojuegos;
    }
}