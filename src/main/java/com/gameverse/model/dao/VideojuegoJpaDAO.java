package com.gameverse.model.dao;

import com.gameverse.model.entity.VideojuegoEntity;
import com.gameverse.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class VideojuegoJpaDAO {

    // READ - Obtener todos
    public List<VideojuegoEntity> obtenerTodos() {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT v FROM VideojuegoEntity v",
                    VideojuegoEntity.class
            ).getResultList();

        } finally {
            em.close();
        }
    }

    // READ - Obtener por ID
    public VideojuegoEntity obtenerPorId(Integer id) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.find(VideojuegoEntity.class, id);
        } finally {
            em.close();
        }
    }

    // CREATE - Guardar videojuego
    public void guardar(VideojuegoEntity videojuego) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(videojuego);
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }

    // UPDATE - Actualizar videojuego
    public void actualizar(VideojuegoEntity videojuego) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();
            em.merge(videojuego);
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }

    // DELETE - Eliminar videojuego
    public void eliminar(Integer id) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            VideojuegoEntity videojuego = em.find(VideojuegoEntity.class, id);

            if (videojuego != null) {
                em.remove(videojuego);
            }

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }
}