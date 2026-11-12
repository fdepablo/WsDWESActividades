package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Extra;
import modelo.Ordenador;
import modelo.Procesador;
import servicios.GestorOrdenador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Un único servlet muestra el formulario con GET y procesa sus datos con POST. */
@WebServlet("/configurar")
public class ServletOrdenador extends HttpServlet {
    private final GestorOrdenador gestor = new GestorOrdenador();

    /** Muestra la JSP del formulario, protegida dentro de WEB-INF. */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/vistas/formulario.jsp").forward(request, response);
    }

    /** Convierte los valores del formulario a enumerados y pide al gestor que valide y calcule. */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String nombre = request.getParameter("nombre");
        if (nombre == null) {
            nombre = "";
        }
        nombre = nombre.strip();
        request.setAttribute("nombre", nombre);

        Ordenador ordenador = new Ordenador();
        ordenador.setNombre(nombre);

        String procesadorRecibido = request.getParameter("procesador");
        String[] extrasRecibidos = request.getParameterValues("extras");
        List<Extra> extras = new ArrayList<>();

        try {
            if (procesadorRecibido != null && !procesadorRecibido.isBlank()) {
                ordenador.setProcesador(Procesador.valueOf(procesadorRecibido));
            }
            // Sin casillas marcadas se recibe null: la lista debe quedar vacía.
            if (extrasRecibidos != null) {
                for (String valor : extrasRecibidos) {
                    Extra extra = Extra.valueOf(valor);
                    extras.add(extra);
                }
            }
        } catch (IllegalArgumentException e) {
            mostrarError(request, response, "El procesador o alguno de los extras no es válido.");
            return;
        }
        ordenador.setExtras(extras);

        int resultado = gestor.procesar(ordenador);
        if (resultado == GestorOrdenador.ERROR_NOMBRE) {
            mostrarError(request, response, "Debes introducir un nombre para el equipo.");
            return;
        }
        if (resultado == GestorOrdenador.ERROR_PROCESADOR) {
            mostrarError(request, response, "Debes seleccionar un procesador.");
            return;
        }

        request.setAttribute("ordenador", ordenador);
        request.getRequestDispatcher("/WEB-INF/vistas/resumen.jsp").forward(request, response);
    }

    /** Vuelve al formulario con el mensaje y el nombre introducido en la misma petición. */
    private void mostrarError(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        request.setAttribute("error", mensaje);
        request.getRequestDispatcher("/WEB-INF/vistas/formulario.jsp").forward(request, response);
    }
}
