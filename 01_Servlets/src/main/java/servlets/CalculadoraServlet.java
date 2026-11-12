package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/** Recibe dos números del formulario y muestra sus cuatro operaciones básicas. */
@WebServlet("/calcular")
public class CalculadoraServlet extends HttpServlet {

    /** Lee los parámetros de la petición GET y genera una página con los resultados o un error. */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter salida = response.getWriter();

        double numero1 = 0;
        double numero2 = 0;

        String error = null;
        String texto1 = request.getParameter("numero1");
        String texto2 = request.getParameter("numero2");

        // El navegador valida el formulario, pero también se puede llamar directamente a la URL.
        if (texto1 == null || texto2 == null || texto1.isBlank() || texto2.isBlank()) {
            error = "Debes introducir los dos números.";
        } else {
            try {
                numero1 = Double.parseDouble(texto1);
                numero2 = Double.parseDouble(texto2);
                if (!Double.isFinite(numero1) || !Double.isFinite(numero2)) {
                    error = "Introduce números finitos.";
                }
            } catch (NumberFormatException e) {
                error = "Los dos valores deben ser números. Usa un punto para los decimales en la URL.";
            }
        }

        if (error != null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        salida.println("<!DOCTYPE html>");
        salida.println("<title>Resultado de la calculadora</title></head><body>");
        salida.println("<h1>Resultado de la calculadora</h1>");

        if (error != null) {
            salida.println("<p>" + error + "</p>");
        } else {
            // Solo se imprimen valores convertidos a double, nunca el texto recibido sin validar.
            salida.println("<p>Primer número: " + numero1 + ". Segundo número: " + numero2 + ".</p>");
            salida.println("<ul>");
            salida.println("<li>Suma: " + (numero1 + numero2) + "</li>");
            salida.println("<li>Resta: " + (numero1 - numero2) + "</li>");
            salida.println("<li>Multiplicación: " + (numero1 * numero2) + "</li>");
            if (numero2 == 0) {
                salida.println("<li>División: no se puede dividir entre cero.</li>");
            } else {
                salida.println("<li>División: " + (numero1 / numero2) + "</li>");
            }
            salida.println("</ul>");
        }

        salida.println("<p><a href=\"index.html\">Volver al formulario</a></p>");
        salida.println("</body></html>");
    }
}
