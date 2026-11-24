package com.gameverse.spring;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CategoriaSpringRepository {

    private final JdbcTemplate jdbcTemplate;

    // FASE 4: Inyeccion de dependencias mediante Spring.
    public CategoriaSpringRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Consulta las categorias registradas en MySQL.
    public List<String> listarCategorias() {

        String sql = "SELECT nombre FROM categorias ORDER BY nombre";

        return jdbcTemplate.queryForList(sql, String.class);
    }
}
