package com.gameverse.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.gameverse.service.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/registro.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String password = request.getParameter("password");

        try {
            usuarioService.registrar(nombre, correo, password);

            response.sendRedirect(
                    request.getContextPath() + "/login?registro=exitoso"
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute("error", e.getMessage());

            request.getRequestDispatcher("/WEB-INF/views/registro.jsp")
                    .forward(request, response);

        } catch (SQLException e) {

            getServletContext().log("Error al registrar usuario.", e);

            request.setAttribute(
                    "error",
                    "No fue posible completar el registro. Verifica los datos."
            );

            request.getRequestDispatcher("/WEB-INF/views/registro.jsp")
                    .forward(request, response);
        }
    }
}