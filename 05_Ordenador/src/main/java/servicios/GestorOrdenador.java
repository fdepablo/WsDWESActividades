package servicios;

import modelo.Extra;
import modelo.Ordenador;

/** Valida una configuración y calcula su precio sin guardar pedidos. */
public class GestorOrdenador {
    public static final int CONFIGURACION_CORRECTA = 0;
    public static final int ERROR_NOMBRE = 1;
    public static final int ERROR_PROCESADOR = 2;

    private static final int PRECIO_BASE = 300;

    /**
     * Comprueba primero el nombre y después el procesador. Los extras son opcionales.
     *
     * @param ordenador objeto no nulo con una lista de extras inicializada, aunque esté vacía
     * @return 0 si calcula y asigna el precio; 1 si falta el nombre; 2 si falta el procesador
     */
    public int procesar(Ordenador ordenador) {
        if (ordenador.getNombre() == null || ordenador.getNombre().isBlank()) {
            return ERROR_NOMBRE;
        }
        if (ordenador.getProcesador() == null) {
            return ERROR_PROCESADOR;
        }

        int precioTotal = PRECIO_BASE + ordenador.getProcesador().getPrecio();
        for (Extra extra : ordenador.getExtras()) {
            precioTotal += extra.getPrecio();
        }
        ordenador.setPrecioTotal(precioTotal);
        return CONFIGURACION_CORRECTA;
    }
}
