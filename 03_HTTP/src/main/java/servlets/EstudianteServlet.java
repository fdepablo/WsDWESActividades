package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Estudiante;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/** Atiende peticiones HTTP con datos de ejemplo y simula las escrituras. */
@WebServlet("/estudiantes/*")
public class EstudianteServlet extends HttpServlet {
    private final HashMap<Integer, Estudiante> estudiantes = new HashMap<>();

    /** Tomcat prepara los datos una vez, antes de atender peticiones. */
    @Override
    public void init() {
        Estudiante ana = new Estudiante(15, "Ana García");
        Estudiante luis = new Estudiante(27, "Luis Pérez");

        estudiantes.put(ana.getIdentificador(), ana);
        estudiantes.put(luis.getIdentificador(), luis);
    }

    /** Consulta el estudiante indicado en la ruta. */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Estudiante estudiante = buscarEstudiante(request, response);
        if (estudiante == null) {
            return;
        }
        enviarTexto(response, HttpServletResponse.SC_OK, estudiante.getIdentificador() + ": " + estudiante.getNombre());
    }

    /** Valida el nombre y simula una creación, sin añadir nada al mapa. */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!esRutaDeColeccion(request)) {
            enviarTexto(response, HttpServletResponse.SC_BAD_REQUEST, "Para crear utiliza /estudiantes sin identificador.");
            return;
        }

        if (!esTextoPlano(request)) {
            enviarTexto(response, HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,
                    "El cuerpo debe enviarse como text/plain.");
            return;
        }

        String nombre = leerCuerpo(request).strip();
        if (nombre.isBlank()) {
            enviarTexto(response, HttpServletResponse.SC_BAD_REQUEST,
                    "El nombre no puede estar vacío.");
            return;
        }
        for (Estudiante estudiante : estudiantes.values()) {
            if (estudiante.getNombre().equals(nombre)) {
                enviarTexto(response, HttpServletResponse.SC_CONFLICT, "Ya existe un estudiante con ese nombre.");
                return;
            }
        }
        response.setHeader("Location", request.getContextPath() + "/estudiantes/28");
        enviarTexto(response, HttpServletResponse.SC_CREATED, "Se crearía el estudiante 28: " + nombre + ". Creación simulada.");
    }

    /** Busca el estudiante y simula el cambio de nombre sin modificarlo. */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Estudiante estudiante = buscarEstudiante(request, response);
        if (estudiante == null) {
            return;
        }
        if (!esTextoPlano(request)) {
            enviarTexto(response, HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,
                    "El cuerpo debe enviarse como text/plain.");
            return;
        }

        String nombre = leerCuerpo(request).strip();
        if (nombre.isBlank()) {
            enviarTexto(response, HttpServletResponse.SC_BAD_REQUEST,
                    "El nombre no puede estar vacío.");
            return;
        }
        enviarTexto(response, HttpServletResponse.SC_OK, "Se actualizaría el estudiante " + estudiante.getIdentificador()
                + " (" + estudiante.getNombre() + ") con el nombre " + nombre + ". Actualización simulada.");
    }

    /** Busca el estudiante y simula su eliminación sin quitarlo del mapa. */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Estudiante estudiante = buscarEstudiante(request, response);
        if (estudiante == null) {
            return;
        }
        enviarTexto(response, HttpServletResponse.SC_OK, "Se eliminaría el estudiante " + estudiante.getIdentificador()
                + ": " + estudiante.getNombre() + ". Eliminación simulada.");
    }

    /** Devuelve el objeto del mapa, o null tras responder con un error 400 o 404. */
    private Estudiante buscarEstudiante(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String id = buscarId(request);
        if (id == null) {
            enviarTexto(response, HttpServletResponse.SC_BAD_REQUEST, "Indica un identificador, por ejemplo /estudiantes/15.");
            return null;
        }
        int identificador;
        try {
            identificador = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            enviarTexto(response, HttpServletResponse.SC_BAD_REQUEST, "El identificador debe ser un número entero.");
            return null;
        }
        Estudiante estudiante = estudiantes.get(identificador);
        if (estudiante == null) {
            enviarTexto(response, HttpServletResponse.SC_NOT_FOUND, "No existe el estudiante " + identificador + ".");
        }
        return estudiante;
    }

    /**
     * Extrae el identificador de la ruta, como en el ejemplo de clase.
     *
     * @param request petición cuya ruta adicional se consulta
     * @return "15" para /estudiantes/15, o null si no hay identificador
     */
    private String buscarId(HttpServletRequest request) {
        String ruta = request.getPathInfo();
        if (ruta == null || ruta.equals("/") || ruta.length() < 2) {
            return null;
        }
        return ruta.substring(1);
    }

    /** Comprueba si la ruta apunta a /estudiantes o /estudiantes/, sin identificador. */
    private boolean esRutaDeColeccion(HttpServletRequest request) {
        String ruta = request.getPathInfo();
        return ruta == null || ruta.equals("/");
    }

    /** Acepta text/plain, incluido cuando va acompañado de un parámetro como charset=UTF-8. */
    private boolean esTextoPlano(HttpServletRequest request) {
        String tipoContenido = request.getContentType();
        if (tipoContenido == null) {
            return false;
        }
        return tipoContenido.toLowerCase().startsWith("text/plain");
    }

    /**
     * Lee el cuerpo como UTF-8, conservando los saltos entre líneas.
     *
     * @param request petición cuyo cuerpo se lee
     * @return contenido completo del cuerpo
     * @throws IOException si falla la lectura
     */
    private String leerCuerpo(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        StringBuilder cuerpo = new StringBuilder();

        try (BufferedReader lector = request.getReader()) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (!cuerpo.isEmpty()) {
                    cuerpo.append(System.lineSeparator());
                }
                cuerpo.append(linea);
            }
        }
        return cuerpo.toString();
    }

    /** Escribe una respuesta de texto con su estado y codificación antes de abrir el escritor. */
    private void enviarTexto(HttpServletResponse response, int estado, String mensaje) throws IOException {
        response.setStatus(estado);
        response.setContentType("text/plain;charset=UTF-8");
        try (PrintWriter salida = response.getWriter()) {
            salida.println(mensaje);
        }
    }
}
