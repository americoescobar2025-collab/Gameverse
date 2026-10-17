package com.gameverse.converter;

import com.gameverse.model.dto.Categoria;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter("categoriaConverter")
public class CategoriaConverter implements Converter<Categoria> {

    @Override
    public Categoria getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            int id = Integer.parseInt(value);
            Categoria cat = new Categoria();
            cat.setId(id);
            switch (id) {
                case 1: cat.setNombre("Acción"); break;
                case 2: cat.setNombre("Aventura"); break;
                case 3: cat.setNombre("RPG"); break;
                default: cat.setNombre("Categoría " + id); break;
            }
            return cat;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Categoria value) {
        if (value == null) {
            return "";
        }
        return String.valueOf(value.getId());
    }
}