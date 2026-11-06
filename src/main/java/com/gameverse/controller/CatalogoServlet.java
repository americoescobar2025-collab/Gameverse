
package com.gameverse.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gameverse.model.dao.VideojuegoDAO;
import com.gameverse.model.dto.Videojuego;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/catalogo")
public class CatalogoServlet extends HttpServlet {

    private static final Logger LOGGER =
            Logger.getLogger(CatalogoServlet.class.getName());

    private final VideojuegoDAO videojuegoDAO = new VideojuegoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);

        // Verificar que el usuario haya iniciado sesión.
        if (sesion == null || sesion.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            List<Videojuego> lista = videojuegoDAO.obtenerTodos();

            request.setAttribute("listaJuegos", lista);
            request.setAttribute("mensaje", request.getParameter("mensaje"));

            request.getRequestDispatcher(
                    "/WEB-INF/views/catalogo.jsp"
            ).forward(request, response);

        } catch (SQLException e) {

            LOGGER.log(Level.SEVERE,
                    "Error al consultar el catálogo de videojuegos.", e);

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            request.setAttribute("error",
                    "No fue posible cargar el catálogo. Inténtalo más tarde.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/error.jsp"
            ).forward(request, response);
        }
    }
}
