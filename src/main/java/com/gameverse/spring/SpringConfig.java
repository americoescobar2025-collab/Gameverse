package com.gameverse.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.gameverse.spring")
public class SpringConfig {

    // FASE 4: Configuracion central de Spring MVC.
    // Separamos los componentes Spring de los beans CDI
    // utilizados en las fases anteriores.

}
