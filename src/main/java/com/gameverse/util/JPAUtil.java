package com.gameverse.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.InputStream;
import java.util.Properties;

public class JPAUtil {

    private static final EntityManagerFactory emf = crearEntityManagerFactory();

    private static EntityManagerFactory crearEntityManagerFactory() {

        try {
            Properties dbProperties = new Properties();

            try (InputStream input = JPAUtil.class
                    .getClassLoader()
                    .getResourceAsStream("db.properties")) {

                if (input == null) {
                    throw new RuntimeException(
                            "No se encontró el archivo db.properties"
                    );
                }

                dbProperties.load(input);
            }

            Properties jpaProperties = new Properties();

            jpaProperties.setProperty(
                    "jakarta.persistence.jdbc.driver",
                    dbProperties.getProperty("db.driver")
            );

            jpaProperties.setProperty(
                    "jakarta.persistence.jdbc.url",
                    dbProperties.getProperty("db.url")
            );

            jpaProperties.setProperty(
                    "jakarta.persistence.jdbc.user",
                    dbProperties.getProperty("db.user")
            );

            jpaProperties.setProperty(
                    "jakarta.persistence.jdbc.password",
                    dbProperties.getProperty("db.password")
            );

            return Persistence.createEntityManagerFactory(
                    "gameversePU",
                    jpaProperties
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al crear EntityManagerFactory",
                    e
            );
        }
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void cerrar() {
        if (emf.isOpen()) {
            emf.close();
        }
    }
}