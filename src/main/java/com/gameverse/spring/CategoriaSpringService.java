package com.gameverse.spring;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaSpringService {

    private final CategoriaSpringRepository repository;

    // FASE 4: Inyeccion del repositorio mediante Spring.
    public CategoriaSpringService(CategoriaSpringRepository repository) {
        this.repository = repository;
    }

    // Consulta de categorias mediante una transaccion de lectura.
    @Transactional(readOnly = true)
    public List<String> listarCategorias() {
        return repository.listarCategorias();
    }
}
