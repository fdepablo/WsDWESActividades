package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.Cliente;
import modelo.Pizza;
import servicios.GestorPizza;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/** Muestra el formulario y añade una pizza nueva a la compra del cliente en sesión. */
@WebServlet("/formulario")
public class PizzaServlet extends HttpServlet {
    // El gestor no guarda clientes ni pedidos: puede compartirse entre peticiones.
    private final GestorPizza gestorPizza = new GestorPizza();

    /** Muestra el formulario, que consulta directamente el cliente de la sesión. */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/vistas/formulario.jsp").forward(request, response);
    }

    /** Los datos personales se reciben solo al empezar; los precios siempre los calcula Java. */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        //La JSP crea la sessión, por lo que para saber si hemos metido pizzas al cliente consultamos
        //el atributo cliente de la session
        HttpSession sesion = request.getSession();
        // Tratamos de recuperar el cliente de la sesión
        Cliente cliente = (Cliente) sesion.getAttribute("cliente");

        if (cliente == null) {
            cliente = new Cliente(limpiar(request.getParameter("nombre")),
                    limpiar(request.getParameter("direccion")),
                    limpiar(request.getParameter("telefono")),
                    limpiar(request.getParameter("email")));
        }

        String[] recibidos = request.getParameterValues("ingredientes");
        List<String> ingredientes = recibidos == null ? List.of() : Arrays.asList(recibidos);

        Pizza pizza = new Pizza(limpiar(request.getParameter("tamano")), ingredientes);
        int resultado = gestorPizza.procesar(cliente, pizza);
        switch (resultado) {
            case GestorPizza.PEDIDO_CORRECTO:
                break;
            case GestorPizza.ERROR_NOMBRE:
                mostrarError(request, response, HttpServletResponse.SC_BAD_REQUEST,
                        "Debes indicar un nombre válido de hasta 60 caracteres.");
                return;
            case GestorPizza.ERROR_DIRECCION:
                mostrarError(request, response, HttpServletResponse.SC_BAD_REQUEST,
                        "Debes indicar una dirección válida de hasta 150 caracteres.");
                return;
            case GestorPizza.ERROR_TELEFONO:
                mostrarError(request, response, HttpServletResponse.SC_BAD_REQUEST,
                        "El teléfono debe contener exactamente nueve dígitos del 0 al 9.");
                return;
            case GestorPizza.ERROR_EMAIL:
                mostrarError(request, response, HttpServletResponse.SC_BAD_REQUEST,
                        "Debes indicar un correo electrónico válido de hasta 100 caracteres.");
                return;
            case GestorPizza.ERROR_TAMANO:
                mostrarError(request, response, HttpServletResponse.SC_BAD_REQUEST,
                        "Selecciona una pizza pequeña, mediana o grande.");
                return;
            case GestorPizza.ERROR_INGREDIENTES:
                mostrarError(request, response, HttpServletResponse.SC_BAD_REQUEST,
                        "Selecciona al menos un ingrediente permitido y no lo repitas.");
                return;
            default:
                mostrarError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "No se ha podido procesar el pedido.");
                return;
        }


        cliente.agregarPizza(pizza);
        // Al no tener autenticación no hace falta renovar el id de sessión ni crear una nueva
        // con autenticación sería obligatorio. Símplemente reutilizamos la session que se creo
        // en la jsp para meter el atributo de cliente.
        sesion.setAttribute("cliente", cliente);

        // Nueva petición GET para mostrar el pedido que llevamos.
        response.sendRedirect(request.getContextPath() + "/pedido");
    }

    /** Normaliza los textos; las reglas de negocio siguen perteneciendo al gestor
     * A diferencia de trim(), strip() reconoce espacios Unicode y es más moderno
     * Habría que hacer una limpieza más profunda realmente.
     */
    private String limpiar(String valor) {
        return valor == null ? "" : valor.strip();
    }

    /** Muestra el error mediante forward */
    private void mostrarError(HttpServletRequest request, HttpServletResponse response,
                              int estado, String mensaje) throws ServletException, IOException {
        response.setStatus(estado);
        request.setAttribute("mensajeError", mensaje);
        request.getRequestDispatcher("/WEB-INF/vistas/error.jsp").forward(request, response);
    }
}
