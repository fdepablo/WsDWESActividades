package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Usuario;

import java.io.IOException;
import java.util.HashMap;

/** Comprueba unas credenciales de prueba y pasa a las JSP los datos de la petición. */
@WebServlet("/acceso")
public class AccesoServlet extends HttpServlet {
    private final HashMap<String, Usuario> usuarios = new HashMap<>();

    /** Prepara una vez los tres usuarios; el mapa solo se consulta durante las peticiones. */
    @Override
    public void init() {
        Usuario ana = new Usuario("ana", "ana123", "Ana García", 17,
                "ana@ejemplo.com", "Desarrollo de Aplicaciones Web");
        Usuario luis = new Usuario("luis", "luis123", "Luis Pérez", 18,
                "luis@ejemplo.com", "Desarrollo de Aplicaciones Multiplataforma");
        Usuario marta = new Usuario("marta", "marta123", "Marta López", 25,
                "marta@ejemplo.com", "Administración de Sistemas Informáticos en Red");

        usuarios.put(ana.getNombreUsuario(), ana);
        usuarios.put(luis.getNombreUsuario(), luis);
        usuarios.put(marta.getNombreUsuario(), marta);
    }

    /** Valida los datos del POST y hace forward al formulario o al perfil con atributos de request. */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Debe indicarse la codificación antes de leer los parámetros del cuerpo.
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String nombreUsuario = request.getParameter("nombreUsuario");
        String contrasenia = request.getParameter("contrasenia");
        // Protegemos el valor que se volverá a mostrar en el formulario y evitar inyecciones HTML
        request.setAttribute("nombreUsuario", escaparHtml(nombreUsuario));

        if (nombreUsuario == null || nombreUsuario.isBlank()
                || contrasenia == null || contrasenia.isBlank()) {
            request.setAttribute("error", "Debes introducir el nombre de usuario y la contraseña.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        Usuario usuario = usuarios.get(nombreUsuario);
        if (usuario == null || !usuario.getContrasenia().equals(contrasenia)) {
            request.setAttribute("error", "Usuario o contraseña incorrectos");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        String mayoriaEdad;
        if (usuario.getEdad() >= 18) {
            mayoriaEdad = "Mayor de edad";
        } else {
            mayoriaEdad = "Menor de edad";
        }
        request.setAttribute("usuario", usuario);
        request.setAttribute("mayoriaEdad", mayoriaEdad);
        request.getRequestDispatcher("/WEB-INF/perfil.jsp").forward(request, response);
    }

    /** Escapa el texto del usuario para mostrarlo dentro de un atributo HTML entre comillas. */
    private String escaparHtml(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
