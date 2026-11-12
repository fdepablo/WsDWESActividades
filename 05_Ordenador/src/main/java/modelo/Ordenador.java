package modelo;

import java.util.ArrayList;
import java.util.List;

/** Datos de una configuración; se crea un objeto nuevo para cada pedido. */
public class Ordenador {
    private String nombre;
    private Procesador procesador;
    private List<Extra> extras = new ArrayList<>();
    private int precioTotal;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Procesador getProcesador() {
        return procesador;
    }

    public void setProcesador(Procesador procesador) {
        this.procesador = procesador;
    }

    public List<Extra> getExtras() {
        return extras;
    }

    public void setExtras(List<Extra> extras) {
        this.extras = extras;
    }

    /** Devuelve el precio completo en euros sin IVA. */
    public int getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(int precioTotal) {
        this.precioTotal = precioTotal;
    }
}
