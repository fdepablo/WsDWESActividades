package modelo;

public enum Tamanio {
    PEQUENIA(5),MEDIANA(10), GRANDE(15);

    private int precio;

    private Tamanio(int precio){
        this.precio = precio;
    }

    public int getPrecio(){
        return this.precio;
    }
}
