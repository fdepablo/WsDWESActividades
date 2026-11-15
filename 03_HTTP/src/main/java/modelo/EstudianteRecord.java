package modelo;

/**
 * Los records crean clases con atributos, constructor y getters de manera simple
 *
 * Esto sería equivalente a la clase Estudiante que esta implementada
 * Los records son INMUTABLES
 */
public record EstudianteRecord(int identificador, String nombre) {
    /*
    public String saludar(){
        return "hola me llamo " + nombre;
    }*/
}
