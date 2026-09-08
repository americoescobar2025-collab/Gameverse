package com.gameverse.controller;

import com.gameverse.model.dao.VideojuegoDAO;
import com.gameverse.model.dto.Videojuego;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/catalogo")
public class CatalogoServlet extends HttpServlet {

    private final VideojuegoDAO videojuegoDAO = new VideojuegoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Videojuego> lista = videojuegoDAO.obtenerTodos();
            request.setAttribute("listaJuegos", lista);
            request.getRequestDispatcher("/WEB-INF/views/catalogo.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Error en la base de datos: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}
