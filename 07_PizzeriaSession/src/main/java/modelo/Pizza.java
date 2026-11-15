package modelo;

import java.util.List;

/** Tamaño, ingredientes y precio con IVA de una pizza de la compra. */
public class Pizza {
    private final String tamano;
    private final List<String> ingredientes;
    private double precio; // Euros, incluido el IVA.

    public Pizza(String tamano, List<String> ingredientes) {
        this.tamano = tamano;
        this.ingredientes = ingredientes;
    }

    public String getTamano() {
        return tamano;
    }

    public List<String> getIngredientes() {
        return ingredientes;
    }

    /** Devuelve el precio en euros para mostrarlo en la JSP. */
    public double getPrecio() {
        return precio;
    }

    /** Recibe el precio con IVA en euros, calculado por el gestor. */
    public void setPrecio(double precio) {
        this.precio = precio;
    }
}