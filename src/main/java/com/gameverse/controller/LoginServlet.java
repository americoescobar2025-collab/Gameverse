package com.gameverse.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.gameverse.model.dto.Usuario;
import com.gameverse.service.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if ("exitoso".equals(request.getParameter("registro"))) {
            request.setAttribute(
                    "mensaje",
                    "Usuario registrado correctamente. Ahora puedes iniciar sesión."
            );
        }

        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String correo = request.getParameter("correo");
        String password = request.getParameter("password");

        if (correo == null || correo.isBlank() ||
                password == null || password.isEmpty()) {

            request.setAttribute("error", "Debes ingresar correo y contraseña.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                    .forward(request, response);
            return;
        }

        try {
            var resultado = usuarioService.autenticar(correo, password);

            if (resultado.isEmpty()) {
                request.setAttribute("error", "Correo o contraseña incorrectos.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                        .forward(request, response);
                return;
            }

            Usuario usuario = resultado.get();

            HttpSession sesionAnterior = request.getSession(false);

            if (sesionAnterior != null) {
                sesionAnterior.invalidate();
            }

            HttpSession sesion = request.getSession(true);
            sesion.setAttribute("usuario", usuario);
            sesion.setMaxInactiveInterval(15 * 60);

            response.sendRedirect(request.getContextPath() + "/catalogo");

        } catch (SQLException e) {

            getServletContext().log("Error al autenticar usuario.", e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "No fue posible procesar el inicio de sesión."
            );
        }
    }
}