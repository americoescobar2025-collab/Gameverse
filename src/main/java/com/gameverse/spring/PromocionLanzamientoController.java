package com.gameverse.spring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
public class PromocionLanzamientoController {

    private final PromocionLanzamientoService service;

    public PromocionLanzamientoController(
            PromocionLanzamientoService service) {
        this.service = service;
    }

    // Endpoint REST para ofertas disponibles.
    @GetMapping("/ofertas")
    public List<Map<String, Object>> ofertas() {
        return service.listarOfertas();
    }

    // Endpoint REST para proximos lanzamientos.
    @GetMapping("/lanzamientos")
    public List<Map<String, Object>> lanzamientos() {
        return service.listarLanzamientos();
    }
}