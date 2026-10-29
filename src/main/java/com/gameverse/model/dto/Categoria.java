
package com.gameverse.model.dto;

import java.io.Serializable;

public class Categoria implements Serializable {

    private int id;
    private String nombre;
    private String descripcion;

    public Categoria() {
    }

    public Categoria(int id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /*
     * CORRECCIÓN DE INTEGRACIÓN - SPRINT II
     *
     * Se agregan equals() y hashCode() para que dos
     * categorías con el mismo ID sean consideradas iguales.
     *
     * Esto permite que JSF compare correctamente la categoría
     * seleccionada con las opciones del formulario.
     *
     * Error que buscamos resolver:
     * "Error de validación: el valor no es válido".
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Categoria)) {
            return false;
        }

        Categoria otra = (Categoria) obj;
        return this.id == otra.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
