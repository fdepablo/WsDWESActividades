package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.Cliente;
import servicios.GestorPizza;

import java.io.IOException;

/** Consulta la compra con GET y la confirma con POST. */
@WebServlet("/pedido")
public class PedidoServlet extends HttpServlet {
    private final GestorPizza gestorPizza = new GestorPizza();

    /** Calcula el total y lo guarda en sesión para mostrar el resumen. */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sesion = request.getSession(false);
        if (sesion != null) {
            Cliente cliente = (Cliente) sesion.getAttribute("cliente");
            if (cliente != null) {
                sesion.setAttribute("totalCompra", gestorPizza.calcularTotal(cliente));
            }
        }
        request.getRequestDispatcher("/WEB-INF/vistas/pedido.jsp").forward(request, response);
    }

    /** Comprueba la compra y prepara sus datos antes de terminar la sesión. */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sesion = request.getSession(false);
        Cliente cliente = null;
        if (sesion != null) {
            cliente = (Cliente) sesion.getAttribute("cliente");
        }

        if (cliente == null || cliente.getPizzas().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.setAttribute("mensajeError", "No hay pizzas para confirmar la compra.");
            request.getRequestDispatcher("/WEB-INF/vistas/error.jsp").forward(request, response);
            return;
        }

        prepararConfirmacion(request, cliente);
        sesion.invalidate();
        request.getRequestDispatcher("/WEB-INF/vistas/confirmacion.jsp").forward(request, response);
    }

    /**
     * Conserva en request los datos de la confirmación antes de invalidar la sesión.
     * Como vamos a invalidar la session vamos a guardar antes el pedido en la request
     * para poder mostrarlo en confirmación.jsp, además, este último tiene session=false
     * por lo que no podemos acceder a la session
     * */
    private void prepararConfirmacion(HttpServletRequest request, Cliente cliente) {
        request.setAttribute("cliente", cliente);
        request.setAttribute("pizzas", cliente.getPizzas());
        request.setAttribute("totalCompra", gestorPizza.calcularTotal(cliente));
    }
}