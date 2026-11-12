package modelo;

/** Extras opcionales y su precio añadido, en euros sin IVA. */
public enum Extra {
    RAM(50), SSD(80), WIFI(20);

    private final int precio;

    Extra(int precio) {
        this.precio = precio;
    }

    public int getPrecio() {
        return precio;
    }
}
