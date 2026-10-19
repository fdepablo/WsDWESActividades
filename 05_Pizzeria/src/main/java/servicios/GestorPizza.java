package servicios;

import modelo.Ingrediente;
import modelo.Pizza;
import modelo.Tamanio;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class GestorPizza {

    // Códigos propios del registro; el servlet decide la respuesta HTTP.
    public static final int PEDIDO_CORRECTO = 0;
    public static final int ERROR_NOMBRE = 1;
    public static final int ERROR_DIRECCION = 2;
    public static final int ERROR_TELEFONO = 3;
    public static final int ERROR_EMAIL = 4;
    public static final int ERROR_TAMANIO = 5;
    public static final int ERROR_INGREDIENTES = 6;

    private static final Pattern FORMATO_EMAIL =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private static final Pattern FORMATO_TELEFONO =
            Pattern.compile("^\\d{9}$");

    /**
     * @param pizza
     * @param tamanio
     * @param listaIngredientes
     * @return
     *      PEDIDO_CORRECTO	0 · Validación y cálculo correctos
     *      ERROR_NOMBRE	1 · Nombre vacío o demasiado largo
     *      ERROR_DIRECCION	2 · Dirección vacía o demasiado larga
     *      ERROR_TELEFONO	3 · No contiene exactamente 9 dígitos
     *      ERROR_EMAIL	4 · Correo vacío, largo o con formato inválido
     *      ERROR_TAMANIO	5 · Tamaño ausente o desconocido\n
     *      ERROR_INGREDIENTES	6 · Lista vacía, valor desconocido o repetido\n
     */
    public int procesar(Pizza pizza, String tamanio, List<String> listaIngredientes){
        if (pizza.getNombre() == null || pizza.getNombre().isBlank()
                || pizza.getNombre().length() > 60) {
            return ERROR_NOMBRE;
        }
        if (pizza.getDireccion() == null || pizza.getDireccion().isBlank()
                || pizza.getDireccion().length() > 120) {
            return ERROR_DIRECCION;
        }
        if (pizza.getTelefono() == null || pizza.getTelefono().isBlank()
                || !FORMATO_TELEFONO.matcher(pizza.getTelefono()).matches()) {
            return ERROR_TELEFONO;
        }
        if (pizza.getEmail() == null || pizza.getEmail().length() > 100
                || !FORMATO_EMAIL.matcher(pizza.getEmail()).matches()) {
            return ERROR_EMAIL;
        }

        //Validamos el tamanio
        if (tamanio == null){
            return ERROR_TAMANIO;
        }

        Tamanio tamanioSeleccionado;
        try {
            //Si no existe ningún objeto en el enumerado que se llame igual que el valor
            //que el tamanio que nos han pasado, entonces da una excepción
            tamanioSeleccionado = Tamanio.valueOf(tamanio);
        } catch (IllegalArgumentException e) {
            return ERROR_TAMANIO;
        }

        if (listaIngredientes == null || listaIngredientes.isEmpty()) {
            return ERROR_INGREDIENTES;
        }

        //Validamos la lista de ingredientes, mismo caso que con el tamaño
        //pero con una lista
        List<Ingrediente> listaIngredientesEnumerado = new ArrayList<>();
        for (String ingrediente : listaIngredientes) {
            if (ingrediente == null) {
                return ERROR_INGREDIENTES;
            }
            try {
                Ingrediente ingredienteSeleccionado = Ingrediente.valueOf(ingrediente);
                if (listaIngredientesEnumerado.contains(ingredienteSeleccionado)) {
                    return ERROR_INGREDIENTES;
                }
                listaIngredientesEnumerado.add(ingredienteSeleccionado);
            } catch (IllegalArgumentException e) {
                return ERROR_INGREDIENTES;
            }
        }

        pizza.setTamanio(tamanioSeleccionado);
        pizza.setListaIngredientes(listaIngredientesEnumerado);
        //Fin de validaciones
        pizza.setPrecio(calcularPrecio(pizza));

        return PEDIDO_CORRECTO;
    }

    private int calcularPrecio(Pizza pizza){
        int precioTotal = pizza.getTamanio().getPrecio();

        for(Ingrediente ingrediente : pizza.getListaIngredientes()){
            precioTotal += ingrediente.getPrecio();
        }

        return precioTotal;
    }
}
