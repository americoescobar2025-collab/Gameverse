package com.gameverse.spring;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class SpringWebInitializer
        extends AbstractAnnotationConfigDispatcherServletInitializer {

    // FASE 4: No se modifica el contexto existente de CDI y JSF.
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    // Registra la configuracion de Spring MVC.
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{SpringConfig.class};
    }

    // Separa las rutas Spring de los servlets existentes.
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/spring/*"};
    }
}
