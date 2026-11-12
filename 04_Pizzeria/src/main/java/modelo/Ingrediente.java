package modelo;

/** Ingredientes de la ampliación: código del formulario y precio sin IVA en euros. */
public enum Ingrediente {
    QUESO("queso", 1), JAMON("jamon", 2), CHAMPINIONES("champinones", 3), ACEITUNAS("aceitunas", 4);

    private final String codigo;
    private final int precio;

    private Ingrediente(String codigo, int precio){
        this.codigo = codigo;
        this.precio = precio;
    }

    public String getCodigo() {
        return codigo;
    }

    /** Busca un código externo; devuelve null cuando no es un ingrediente permitido. */
    public static Ingrediente buscarPorCodigo(String codigo) {
        for (Ingrediente ingrediente : values()) {
            if (ingrediente.getCodigo().equals(codigo)) {
                return ingrediente;
            }
        }
        return null;
    }

    public int getPrecio(){
        return this.precio;
    }
}
