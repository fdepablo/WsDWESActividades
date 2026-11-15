package modelo;

import java.util.ArrayList;
import java.util.List;

/** Datos del cliente y pizzas de su compra actual. Este objeto se guarda en la sesión. */
public class Cliente {
    private final String nombre;
    private final String direccion;
    private final String telefono;
    private final String email;
    private final List<Pizza> pizzas = new ArrayList<>();

    public Cliente(String nombre, String direccion, String telefono, String email) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
    }

    /** Añade una pizza nueva cuyo precio ya ha validado y calculado el gestor. */
    public void agregarPizza(Pizza pizza) {
        pizzas.add(pizza);
    }

    /** Devuelve las pizzas de la compra del cliente. */
    public List<Pizza> getPizzas() {
        return pizzas;
    }

    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
}
