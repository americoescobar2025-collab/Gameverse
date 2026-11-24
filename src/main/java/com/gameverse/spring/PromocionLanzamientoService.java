package com.gameverse.spring;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class PromocionLanzamientoService {

    private final PromocionLanzamientoRepository repository;

    // Inyeccion de dependencias mediante constructor.
    public PromocionLanzamientoService(
            PromocionLanzamientoRepository repository) {
        this.repository = repository;
    }

    public List<Map<String, Object>> listarOfertas() {
        return repository.listarOfertas();
    }

    public List<Map<String, Object>> listarLanzamientos() {
        return repository.listarLanzamientos();
    }
}