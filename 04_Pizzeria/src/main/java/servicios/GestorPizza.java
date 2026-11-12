package servicios;

import modelo.Ingrediente;
import modelo.Pizza;
import modelo.Tamanio;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/** Valida un pedido y calcula su precio; no guarda pedidos entre llamadas. */
public class GestorPizza {
    public static final int PEDIDO_CORRECTO = 0;
    public static final int ERROR_NOMBRE = 1;
    public static final int ERROR_DIRECCION = 2;
    public static final int ERROR_TELEFONO = 3;
    public static final int ERROR_EMAIL = 4;
    public static final int ERROR_TAMANO = 5;
    public static final int ERROR_INGREDIENTES = 6;

    private static final Pattern FORMATO_EMAIL =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final Pattern FORMATO_TELEFONO = Pattern.compile("^[0-9]{9}$");

    /**
     * Valida en el orden de los códigos 1 a 6 y devuelve el primer error.
     * Solo si todo es correcto asigna los enumerados y el precio con IVA.
     * Ante un error no calcula ni modifica el precio de la pizza recibida.
     *
     * <p>Pizza mantiene los códigos externos separados de sus enumerados
     * para poder rechazar valores desconocidos antes de convertirlos.</p>
     *
     * @param pizza pedido no nulo con datos personales y códigos recibidos;
     *              para datos ausentes se usan textos, arrays o listas vacíos
     * @return 0: pedido correcto; 1: nombre vacío o mayor de 60 caracteres;
     *         2: dirección vacía o mayor de 150; 3: teléfono sin exactamente nueve dígitos ASCII;
     *         4: email vacío, mayor de 100 o de formato inválido;
     *         5: tamaño ausente, múltiple o desconocido;
     *         6: ingredientes ausentes, desconocidos o repetidos
     */
    public int procesar(Pizza pizza) {
        if (pizza.getNombre() == null || pizza.getNombre().strip().isEmpty()
                || pizza.getNombre().strip().length() > 60) {
            return ERROR_NOMBRE;
        }
        if (pizza.getDireccion() == null || pizza.getDireccion().strip().isEmpty()
                || pizza.getDireccion().strip().length() > 150) {
            return ERROR_DIRECCION;
        }
        if (pizza.getTelefono() == null || !FORMATO_TELEFONO.matcher(pizza.getTelefono()).matches()) {
            return ERROR_TELEFONO;
        }
        if (pizza.getEmail() == null || pizza.getEmail().length() > 100
                || !FORMATO_EMAIL.matcher(pizza.getEmail()).matches()) {
            return ERROR_EMAIL;
        }

        String[] tamanios = pizza.getTamaniosRecibidos();
        if (tamanios == null || tamanios.length != 1) {
            return ERROR_TAMANO;
        }
        Tamanio tamanioSeleccionado = Tamanio.buscarPorCodigo(tamanios[0]);
        if (tamanioSeleccionado == null) {
            return ERROR_TAMANO;
        }
        List<String> codigosIngredientes = pizza.getIngredientesRecibidos();
        if (codigosIngredientes == null || codigosIngredientes.isEmpty()) {
            return ERROR_INGREDIENTES;
        }

        List<Ingrediente> ingredientesSeleccionados = new ArrayList<>();
        for (String codigo : codigosIngredientes) {
            Ingrediente ingrediente = Ingrediente.buscarPorCodigo(codigo);
            if (ingrediente == null || ingredientesSeleccionados.contains(ingrediente)) {
                return ERROR_INGREDIENTES;
            }
            ingredientesSeleccionados.add(ingrediente);
        }

        pizza.setTamanio(tamanioSeleccionado);
        pizza.setListaIngredientes(ingredientesSeleccionados);
        pizza.setPrecio(calcularPrecio(pizza));
        return PEDIDO_CORRECTO;
    }

    /**
     * Suma las tarifas de los enumerados y añade el IVA del 21 %.
     * Precondición: todos los datos han sido validados por procesar.
     *
     * @param pizza pedido válido con tamaño e ingredientes asignados
     * @return total con IVA en céntimos; las tarifas base son euros enteros
     */
    private int calcularPrecio(Pizza pizza) {
        int precioSinIva = pizza.getTamanio().getPrecio();
        for (Ingrediente ingrediente : pizza.getListaIngredientes()) {
            precioSinIva += ingrediente.getPrecio();
        }
        // Cada euro de base se convierte en 121 céntimos al incluir el 21 % de IVA.
        return precioSinIva * 121;
    }
}
