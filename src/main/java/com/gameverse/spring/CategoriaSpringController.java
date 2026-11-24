package com.gameverse.spring;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categorias")
public class CategoriaSpringController {

    private final CategoriaSpringService service;

    // FASE 4: Inyeccion del servicio mediante Spring.
    public CategoriaSpringController(CategoriaSpringService service) {
        this.service = service;
    }

    // Consulta las categorias y devuelve el resultado en JSON.
    @GetMapping
    public List<String> listarCategorias() {
        return service.listarCategorias();
    }
}
