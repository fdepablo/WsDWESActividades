package servicios;

import modelo.Cliente;
import modelo.Pizza;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/** Valida cliente y pizza, calcula el precio y suma la compra. */
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
    private static final List<String> INGREDIENTES_PERMITIDOS =
            List.of("queso", "jamon", "champinones", "aceitunas");

    /**
     * Valida en el orden de los códigos 1 a 6 y devuelve el primer error.
     * Solo asigna el precio si todos los datos son correctos.
     *
     * @param cliente datos personales, no nulo
     * @param pizza pizza nueva con lista de ingredientes no nula
     * @return 0 si calcula el precio; 1 si el nombre está vacío o supera 60 caracteres;
     *         2 si la dirección está vacía o supera 150; 3 si el teléfono no tiene nueve dígitos;
     *         4 si el correo es inválido o supera 100 caracteres; 5 si el tamaño no está permitido;
     *         6 si faltan ingredientes, son desconocidos o se repiten
     */
    public int procesar(Cliente cliente, Pizza pizza) {
        if (cliente.getNombre() == null || cliente.getNombre().isBlank()
                || cliente.getNombre().strip().length() > 60) {
            return ERROR_NOMBRE;
        }
        if (cliente.getDireccion() == null || cliente.getDireccion().isBlank()
                || cliente.getDireccion().strip().length() > 150) {
            return ERROR_DIRECCION;
        }
        if (cliente.getTelefono() == null || !FORMATO_TELEFONO.matcher(cliente.getTelefono()).matches()) {
            return ERROR_TELEFONO;
        }
        if (cliente.getEmail() == null || cliente.getEmail().length() > 100
                || !FORMATO_EMAIL.matcher(cliente.getEmail()).matches()) {
            return ERROR_EMAIL;
        }

        String tamano = pizza.getTamano();
        if (!"pequena".equals(tamano) && !"mediana".equals(tamano) && !"grande".equals(tamano)) {
            return ERROR_TAMANO;
        }
        if (pizza.getIngredientes().isEmpty()) {
            return ERROR_INGREDIENTES;
        }
        List<String> elegidos = new ArrayList<>();
        for (String ingrediente : pizza.getIngredientes()) {
            if (!INGREDIENTES_PERMITIDOS.contains(ingrediente) || elegidos.contains(ingrediente)) {
                return ERROR_INGREDIENTES;
            }
            elegidos.add(ingrediente);
        }

        pizza.setPrecio(calcularPrecio(pizza));
        return PEDIDO_CORRECTO;
    }

    /** Suma los precios en euros de las pizzas, que ya incluyen el IVA. */
    public double calcularTotal(Cliente cliente) {
        double total = 0;
        for (Pizza pizza : cliente.getPizzas()) {
            total += pizza.getPrecio();
        }
        return total;
    }

    /** Calcula el precio con IVA en euros de una pizza previamente validada. */
    private double calcularPrecio(Pizza pizza) {
        double precioSinIva = 0;
        switch (pizza.getTamano()) {
            case "pequena":
                precioSinIva = 5;
                break;
            case "mediana":
                precioSinIva = 10;
                break;
            case "grande":
                precioSinIva = 15;
                break;
        }
        precioSinIva += pizza.getIngredientes().size();
        // Al precio en euros se le añade el IVA del 21 %.
        return precioSinIva * 1.21;
    }
}