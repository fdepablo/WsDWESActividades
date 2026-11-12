package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Muestra cómo indicar al cliente que debe realizar otra petición. */
@WebServlet("/inicio")
public class InicioServlet extends HttpServlet {
    /** Devuelve una redirección temporal 307 que conserva el método HTTP. */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        response.setHeader("Location", request.getContextPath() + "/estudiantes/15");
    }
}
