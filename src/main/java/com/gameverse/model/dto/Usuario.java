
package com.gameverse.model.dto;

/**
 * Modelo de usuario de Gameverse.
 * Representa los datos publicos de una cuenta.
 */
public class Usuario {

    private int id;
    private String nombre;
    private String correo;
    private boolean activo;
    private String passwordHash;

    public Usuario() {}

    public Usuario(int id, String nombre, String correo, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.activo = activo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
