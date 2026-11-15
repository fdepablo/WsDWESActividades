package modelo;

/**
 * Representa los dos datos de un estudiante utilizados en esta actividad.
 * Notese que no tenemos setters, por lo que una vez inicializado el objeto
 * no se puede cambiar su estado. Este concepto se llama inmutabilidad
 * */
public class Estudiante {
    private final int identificador;
    private final String nombre;

    /** Crea un estudiante con su identificador y su nombre. */
    public Estudiante(int identificador, String nombre) {
        this.identificador = identificador;
        this.nombre = nombre;
    }

    /** Devuelve el identificador que se usa como clave en el mapa. */
    public int getIdentificador() {
        return identificador;
    }

    /** Devuelve el nombre del estudiante. */
    public String getNombre() {
        return nombre;
    }
}
