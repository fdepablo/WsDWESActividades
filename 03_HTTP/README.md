# Actividad 03 Servidor HTTP de estudiantes

Solución sencilla con dos servlets y un `HashMap`. Permite consultar estudiantes y simular su creación, actualización y eliminación. Las respuestas son texto plano; no hay formularios, JSP ni base de datos.

## Qué vas a aprender

- Relacionar GET, POST, PUT y DELETE con las operaciones sobre un recurso.
- Leer un identificador de la ruta y un nombre del cuerpo de la petición.
- Buscar objetos por su clave en un `HashMap`.
- Responder con estados HTTP, texto UTF-8 y la cabecera `Location`.
- Distinguir una respuesta normal de una redirección 307.

## Requisitos previos

Java 25, Maven, Tomcat 11 y Postman. Conviene conocer clases, métodos, condicionales y bucles.

## Conceptos clave y estructura

```text
src/main/java/
  modelo/Estudiante.java           Identificador y nombre
  servlets/EstudianteServlet.java  GET, POST, PUT y DELETE
  servlets/InicioServlet.java      Redirección de /inicio
src/main/webapp/WEB-INF/web.xml    Descriptor de la aplicación
```

Las anotaciones `@WebServlet` asignan las rutas. `getPathInfo()` obtiene la parte que sigue a `/estudiantes`, por ejemplo `/15`. `getContextPath()` obtiene el contexto del despliegue, para construir un `Location` válido.

## Cómo comprobar que funciona

En Postman, utiliza la URL base de la aplicación configurada en el IDE seguida de cada ruta. Los ejemplos de cabeceras suponen el contexto `/03_HTTP`; adáptalo al contexto de tu IDE. Para POST y PUT selecciona **Body → raw → Text** y comprueba `Content-Type: text/plain`. Se admite `text/plain; charset=UTF-8`. En los casos de tipo incorrecto utiliza `application/json`.

| Método | Ruta | Cuerpo | Estado esperado |
| --- | --- | --- | --- |
| GET | `/estudiantes/15` | — | 200, `15: Ana García` |
| GET | `/estudiantes/27` | — | 200, `27: Luis Pérez` |
| GET | `/estudiantes/99` | — | 404 |
| GET | `/estudiantes/abc` | — | 400 |
| GET | `/estudiantes` | — | 400, falta identificador |
| POST | `/estudiantes` | `Marta López` | 201, creación simulada |
| POST | `/estudiantes` | Solo espacios o vacío | 400 |
| POST | `/estudiantes` | `Ana García` | 409 |
| POST | `/estudiantes` | `Luis Pérez` | 409 |
| POST | `/estudiantes` | `{}` con `application/json` | 415 |
| PUT | `/estudiantes/15` | `Ana López` | 200, actualización simulada |
| PUT | `/estudiantes/99` | `Ana López` | 404 |
| PUT | `/estudiantes/abc` | `Ana López` | 400 |
| PUT | `/estudiantes/15` | Solo espacios o vacío | 400 |
| PUT | `/estudiantes/15` | `{}` con `application/json` | 415 |
| DELETE | `/estudiantes/15` | — | 200, eliminación simulada |
| DELETE | `/estudiantes/99` | — | 404 |
| DELETE | `/estudiantes/abc` | — | 400 |
| GET | `/inicio` | — | 307, sin cuerpo |

Todas las respuestas con texto incluyen `Content-Type: text/plain;charset=UTF-8` (el formato de la cabecera puede variar ligeramente según Tomcat).

El POST correcto incluye `Location: /03_HTTP/estudiantes/28`. Es una simulación: consultar después ese recurso devuelve 404, porque no se ha insertado en el mapa.

En la petición a `/inicio`, desactiva **Automatically follow redirects** en la configuración de la petición de Postman. Debes ver 307 y `Location: /03_HTTP/estudiantes/15`. Con el seguimiento activado verás finalmente el 200 de Ana García.

Después de PUT y DELETE, repite GET `/estudiantes/15`: debe seguir devolviendo `15: Ana García`. Explica por qué conserva el nombre original y comprueba las tildes de García, Pérez y del nombre enviado. Para recoger las pruebas hechas, puedes registrar el estado obtenido junto a cada caso de la tabla.

## Explicación guiada del código

1. `Estudiante` guarda identificador y nombre. No necesita setters porque este ejercicio no modifica los objetos.
2. Tomcat llama a `init()` una vez al crear el servlet. Allí se crean primero los dos objetos y después se insertan en el mapa con las claves 15 y 27. Los datos quedan preparados antes de atender peticiones y después solo se consultan.
3. `buscarId()` extrae el identificador como texto: `/estudiantes/15` devuelve `"15"` y una ruta sin identificador devuelve `null`, como en el ejemplo de clase. `buscarEstudiante()` llama a ese método, convierte el texto a entero y consulta `estudiantes.get(identificador)`. Un identificador ausente o de formato incorrecto produce 400; una clave sin objeto produce 404. Devuelve `null` si ya ha respondido con un error, y el método HTTP que la llama termina con `return`.
4. POST y PUT muestran directamente las comprobaciones: `esTextoPlano()` valida el tipo de contenido, `leerCuerpo()` lee texto UTF-8 con un `BufferedReader` y un bucle `readLine()`, y después se eliminan los espacios exteriores y se rechaza el nombre vacío. Cada error se responde en el propio método HTTP y termina con `return`. La comparación de nombres duplicados es exacta, distinguiendo mayúsculas y tildes.
5. POST recorre los estudiantes para detectar duplicados y devuelve 201 con `Location`. PUT y DELETE buscan el objeto y construyen un mensaje. Ninguno cambia el mapa ni sus objetos: esa es la razón de que GET siga devolviendo los datos iniciales.
6. `enviarTexto()` reúne las tres instrucciones repetidas: estado, tipo de contenido y escritura del mensaje. La codificación se establece antes de obtener el escritor. Al usar texto plano, el nombre recibido se muestra como texto, sin interpretarlo como HTML.
7. `InicioServlet` devuelve explícitamente 307 y `Location`; no utiliza la redirección 302 predeterminada de `sendRedirect`. El cliente decide si sigue la nueva dirección.

Los estados se escriben con constantes de `HttpServletResponse`, como `SC_OK` (200), `SC_BAD_REQUEST` (400) y `SC_NOT_FOUND` (404), siguiendo el ejemplo de clase. `esRutaDeColeccion()` comprueba que la ruta sea `/estudiantes` o `/estudiantes/`, sin identificador. Al principio de POST, si esa comprobación devuelve `false`, se responde con 400.

`esTextoPlano()` comprueba que exista la cabecera `Content-Type` y que empiece por `text/plain`. Es una comprobación simplificada como la del ejemplo de clase: permite observar el error 415 y acepta también `text/plain; charset=UTF-8`, sin analizar los parámetros de la cabecera.

Los datos de cada petición son variables locales. El mapa es un atributo compartido por las peticiones, pero aquí no se modifica después de `init()`. Es una simplificación didáctica: una aplicación con escrituras reales necesitaría resolver la persistencia y el acceso concurrente a sus datos.

## Errores frecuentes

- **404 en todas las rutas:** revisa que hayas desplegado el WAR y utilizado su contexto real.
- **415 inesperado:** selecciona Text en Postman; raw por sí solo no garantiza `text/plain`.
- **Ves 200 en lugar de 307:** desactiva el seguimiento automático de redirecciones.
- **Tildes incorrectas:** envía el cuerpo en UTF-8 y revisa la cabecera de respuesta.
- **PUT y DELETE no cambian nada:** es el comportamiento solicitado, no un fallo.
- **GET sin identificador:** esta actividad no implementa el listado; utiliza `/estudiantes/15` o `/estudiantes/27`.

No se incluyen ampliaciones opcionales. La tabla de comprobaciones permite practicar y explicar cada caso del enunciado.
