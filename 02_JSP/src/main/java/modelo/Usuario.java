package modelo;

/** Datos de un usuario de prueba; la contraseña en texto plano solo se usa en esta simulación. */
public class Usuario {
    private final String nombreUsuario;
    private final String contrasenia;
    private final String nombreCompleto;
    private final int edad;
    private final String correoElectronico;
    private final String cicloFormativo;

    /** Crea un usuario con sus credenciales y los datos que se mostrarán en el perfil. */
    public Usuario(String nombreUsuario, String contrasenia, String nombreCompleto, int edad,
                   String correoElectronico, String cicloFormativo) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.nombreCompleto = nombreCompleto;
        this.edad = edad;
        this.correoElectronico = correoElectronico;
        this.cicloFormativo = cicloFormativo;
    }

    /** Devuelve el nombre que se usa como clave en el mapa. */
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    /** Devuelve la contraseña de prueba para que el servlet compruebe el acceso. */
    public String getContrasenia() {
        return contrasenia;
    }

    /** Devuelve el nombre completo del perfil. */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /** Devuelve la edad en años. */
    public int getEdad() {
        return edad;
    }

    /** Devuelve el correo electrónico del perfil. */
    public String getCorreoElectronico() {
        return correoElectronico;
    }

    /** Devuelve el ciclo formativo que cursa el usuario. */
    public String getCicloFormativo() {
        return cicloFormativo;
    }
}
