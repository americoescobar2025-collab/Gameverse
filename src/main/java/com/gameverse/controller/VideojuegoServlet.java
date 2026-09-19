package com.gameverse.controller;

import com.gameverse.model.dao.VideojuegoDAO;
import com.gameverse.model.dto.Videojuego;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/videojuegos")
public class VideojuegoServlet extends HttpServlet {

    private final VideojuegoDAO videojuegoDAO = new VideojuegoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/formulario.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String titulo = texto(request, "titulo");
        String descripcion = texto(request, "descripcion");
        String imagenUrl = texto(request, "imagenUrl");
        String categoria = texto(request, "categoria");
        String precioTexto = texto(request, "precio");
        String stockTexto = texto(request, "stock");

        String error = validarTexto(titulo, descripcion, categoria, imagenUrl, precioTexto, stockTexto);
        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("/WEB-INF/views/formulario.jsp").forward(request, response);
            return;
        }

        try {
            double precio = Double.parseDouble(precioTexto);
            int stock = Integer.parseInt(stockTexto);

            if (!Double.isFinite(precio) || precio < 0 || stock < 0) {
                request.setAttribute("error", "El precio y el stock deben ser valores válidos iguales o mayores que cero.");
                request.getRequestDispatcher("/WEB-INF/views/formulario.jsp").forward(request, response);
                return;
            }

            Videojuego videojuego = new Videojuego(
                    0, titulo, descripcion, precio, stock, imagenUrl, categoria
            );
            videojuegoDAO.insertar(videojuego);

            response.sendRedirect(request.getContextPath()
                    + "/catalogo?mensaje=Videojuego%20registrado%20correctamente");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "El precio debe ser decimal y el stock debe ser un número entero.");
            request.getRequestDispatcher("/WEB-INF/views/formulario.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "No se pudo registrar el videojuego: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/formulario.jsp").forward(request, response);
        }
    }

    private String texto(HttpServletRequest request, String nombre) {
        String valor = request.getParameter(nombre);
        return valor == null ? "" : valor.trim();
    }

    private String validarTexto(String titulo, String descripcion, String categoria,
                                String imagenUrl, String precio, String stock) {
        if (titulo.length() < 2 || titulo.length() > 100) {
            return "El título es obligatorio y debe tener entre 2 y 100 caracteres.";
        }
        if (descripcion.length() < 10 || descripcion.length() > 500) {
            return "La descripción debe tener entre 10 y 500 caracteres.";
        }
        if (categoria.length() < 2 || categoria.length() > 50) {
            return "La categoría es obligatoria y debe tener entre 2 y 50 caracteres.";
        }
        if (precio.isBlank() || stock.isBlank()) {
            return "El precio y el stock son obligatorios.";
        }
        if (!imagenUrl.isBlank()
                && !imagenUrl.startsWith("http://")
                && !imagenUrl.startsWith("https://")) {
            return "La URL de imagen debe comenzar con http:// o https://.";
        }
        return null;
    }
}
