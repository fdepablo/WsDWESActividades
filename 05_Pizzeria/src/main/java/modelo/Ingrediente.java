package modelo;

public enum Ingrediente {
    QUESO(1),JAMON(2),CHAMPINIONES(3),ACEITUNAS(4);

    private int precio;

    private Ingrediente(int precio){
        this.precio = precio;
    }

    public int getPrecio(){
        return this.precio;
    }
}
