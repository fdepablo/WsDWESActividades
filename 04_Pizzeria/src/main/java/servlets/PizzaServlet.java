package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Muestra el formulario de una pizza; la validación corresponde al gestor. */
@WebServlet("/formulario")
public class PizzaServlet extends HttpServlet {

    /** Atiende GET y muestra la JSP protegida mediante forward. */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/vistas/formulario.jsp")
                .forward(req, resp);
    }
}
