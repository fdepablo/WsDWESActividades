package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Pizza;
import servicios.GestorPizza;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/pizzeria/pedido")
public class PedidoServlet extends HttpServlet {

    private GestorPizza gestorPizza = new GestorPizza();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String nombre = limpiar(request.getParameter("nombre"));
        String direccion = limpiar(request.getParameter("direccion"));
        String telefono = limpiar(request.getParameter("telefono"));
        String email = limpiar(request.getParameter("email"));

        String tamanio = limpiar(request.getParameter("tamanio"));
        String[] arrayIngredientes = request.getParameterValues("ingredientes");

        // Si no se marca ninguna casilla, getParameterValues devuelve null y usamos
        // una lista vacía. Si se marca alguna, convertimos el array recibido en una lista.
        List<String> intereses = arrayIngredientes == null
                ? List.of()
                : Arrays.asList(arrayIngredientes);

        Pizza pizza = new Pizza();
        pizza.setNombre(nombre);
        pizza.setDireccion(direccion);
        pizza.setTelefono(telefono);
        pizza.setEmail(email);

        int resultado = gestorPizza.procesar(pizza, tamanio, intereses);

        switch (resultado) {
            case GestorPizza.PEDIDO_CORRECTO:
                break;
            case GestorPizza.ERROR_NOMBRE:
                mostrarError(request, response, HttpServletResponse.SC_BAD_REQUEST,
                        "Debes indicar un nombre válido. No puede superar los 60 caracteres");
                return;
            case GestorPizza.ERROR_DIRECCION:
                mostrarError(request, response, HttpServletResponse.SC_BAD_REQUEST,
                        "Debes indicar una dirección válida. No puede superar los 120 caracteres");
                return;
            case GestorPizza.ERROR_TELEFONO:
                mostrarError(request, response, HttpServletResponse.SC_BAD_REQUEST,
                        "Debes indicar un teléfono correcto. Deben ser 9 números");
                return;
            case GestorPizza.ERROR_EMAIL:
                mostrarError(request, response, HttpServletResponse.SC_BAD_REQUEST,
                        "Debes indicar un email válido.");
                return;
            case GestorPizza.ERROR_TAMANIO:
                mostrarError(request, response, HttpServletResponse.SC_BAD_REQUEST,
                        "Debes indiciar un tamaño correcto");
                return;
            case GestorPizza.ERROR_INGREDIENTES:
                mostrarError(request, response, HttpServletResponse.SC_BAD_REQUEST,
                        "Debes indicar unos ingredientes correctos");
                return;
            default:
                mostrarError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "No se ha podido completar el pedido.");
                return;
        }

        request.setAttribute("pizza", pizza);
        request.getRequestDispatcher("/WEB-INF/vistas/pedido.jsp").forward(request, response);
    }

    private String limpiar(String valor) {
        return valor == null ? "" : valor.strip();
    }

    private void mostrarError(HttpServletRequest request, HttpServletResponse response,
                              int estado, String mensaje) throws ServletException, IOException {
        response.setStatus(estado);
        request.setAttribute("error", mensaje);
        request.getRequestDispatcher("/WEB-INF/vistas/error.jsp").forward(request, response);
    }


}
