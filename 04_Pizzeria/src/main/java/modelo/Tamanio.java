package modelo;

/** Tamaños permitidos con el código del formulario y su precio base en euros. */
public enum Tamanio {
    PEQUENIA("pequena", 5), MEDIANA("mediana", 10), GRANDE("grande", 15);

    private final String codigo;
    private final int precio;

    private Tamanio(String codigo, int precio){
        this.codigo = codigo;
        this.precio = precio;
    }

    public String getCodigo() {
        return codigo;
    }

    /** Busca un código externo; devuelve null cuando no es un tamaño permitido. */
    public static Tamanio buscarPorCodigo(String codigo) {
        for (Tamanio tamanio : values()) {
            if (tamanio.getCodigo().equals(codigo)) {
                return tamanio;
            }
        }
        return null;
    }

    public int getPrecio(){
        return this.precio;
    }
}
