package modelo;

import java.util.List;
import java.util.ArrayList;

/** Datos de un pedido y selecciones validadas mediante enumerados. */
public class Pizza {
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private int precio; // Céntimos, incluido el IVA.
    private Tamanio tamanio;
    private List<Ingrediente> listaIngredientes;
    private String[] tamaniosRecibidos = new String[0];
    private List<String> ingredientesRecibidos = new ArrayList<>();

    /** Valores externos pendientes de validar; un array permite detectar tamaños repetidos. */
    public String[] getTamaniosRecibidos() {
        return tamaniosRecibidos;
    }

    public void setTamaniosRecibidos(String[] tamaniosRecibidos) {
        this.tamaniosRecibidos = tamaniosRecibidos;
    }

    /** Códigos externos pendientes de convertir a los enumerados permitidos. */
    public List<String> getIngredientesRecibidos() {
        return ingredientesRecibidos;
    }

    public void setIngredientesRecibidos(List<String> ingredientesRecibidos) {
        this.ingredientesRecibidos = ingredientesRecibidos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Tamanio getTamanio() {
        return tamanio;
    }

    public void setTamanio(Tamanio tamanio) {
        this.tamanio = tamanio;
    }

    public List<Ingrediente> getListaIngredientes() {
        return listaIngredientes;
    }

    public void setListaIngredientes(List<Ingrediente> listaIngredientes) {
        this.listaIngredientes = listaIngredientes;
    }

    /** Devuelve el total en euros para mostrarlo en la JSP. */
    public double getPrecio() {
        return precio / 100.0;
    }

    /** Recibe el total en céntimos calculado por el gestor. */
    public void setPrecio(int precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", precio='" + precio + '\'' +
                ", tamanio=" + tamanio +
                ", listaIngredientes=" + listaIngredientes +
                '}';
    }
}
