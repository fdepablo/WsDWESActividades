package modelo;

/** Procesadores permitidos y su precio añadido, en euros sin IVA. */
public enum Procesador {
    BASICO(100), POTENTE(200);

    private final int precio;

    Procesador(int precio) {
        this.precio = precio;
    }

    public int getPrecio() {
        return precio;
    }
}
