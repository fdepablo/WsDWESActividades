# Actividad 02 Acceso simulado a un perfil de usuario

Un formulario JSP envía un nombre de usuario y una contraseña a un servlet. El servlet consulta un `HashMap` y hace `forward` al formulario con un error o a una JSP con el perfil. Los usuarios están preparados en memoria y no se modifican.

## Qué vas a aprender

- Enviar credenciales mediante POST y leer parámetros.
- Buscar un objeto por su nombre de usuario en un `HashMap`.
- Pasar datos del servlet a una JSP con atributos de la petición.
- Mantener el nombre introducido al mostrar un error.
- Mostrar atributos y propiedades de un objeto mediante expresiones EL `${...}`.
- Entender el alcance de request y la diferencia entre forward y redirect.

## Requisitos previos

Java 25, Maven, Tomcat 11 e IntelliJ IDEA. Conocimientos de clases, formularios HTML, condicionales y servlets. Las JSP muestran los datos con expresiones EL, sin librerías de etiquetas.

## Conceptos clave y estructura

```text
src/main/java/
  modelo/Usuario.java           Datos y getters del usuario
  servlets/AccesoServlet.java   Usuarios en memoria y comprobación del acceso
src/main/webapp/
  index.jsp                    Formulario y mensajes de error
  WEB-INF/perfil.jsp           Tabla del perfil
  WEB-INF/web.xml              Página de bienvenida
```

Los parámetros son los textos enviados por el navegador. Los atributos son datos que el servlet añade a la petición para que la JSP pueda utilizarlos. `requestScope` indica explícitamente que se consultan atributos de esa petición.

## Usuarios de prueba

| Usuario | Contraseña | Nombre completo | Edad | Resultado |
| --- | --- | --- | --- | --- |
| `ana` | `ana123` | Ana García | 17 | Menor de edad |
| `luis` | `luis123` | Luis Pérez | 18 | Mayor de edad |
| `marta` | `marta123` | Marta López | 25 | Mayor de edad |

Son credenciales ficticias de esta simulación. Las contraseñas se guardan en texto plano porque así lo solicita el ejercicio. En una aplicación real se utilizarían hashes adecuados para contraseñas y HTTPS. El perfil nunca muestra la contraseña.

## Cómo comprobar que funciona

Abre `/` o `/index.jsp` dentro del contexto configurado en el IDE, por ejemplo `http://localhost:8080/02_JSP/`. El formulario envía un POST a `/acceso` dentro de ese mismo contexto.

| Comprobación | Resultado esperado |
| --- | --- |
| Enviar ambos campos vacíos | Mensaje que pide usuario y contraseña |
| Escribir `ana` y dejar la contraseña vacía | Mensaje de datos incompletos y nombre `ana` conservado |
| Dejar el usuario vacío y escribir una contraseña | Mensaje de datos incompletos |
| Introducir solo espacios en un campo | Mensaje de datos incompletos |
| Usuario `nadie` con cualquier contraseña | «Usuario o contraseña incorrectos» y usuario conservado |
| Usuario `ana` con contraseña incorrecta | El mismo mensaje y usuario conservado |
| Acceder con cada usuario de la tabla | Su perfil completo y la mayoría de edad correcta |
| Acceder con `luis`, de 18 años | «Mayor de edad», comprobando el límite |
| Solicitar `/WEB-INF/perfil.jsp` directamente | HTTP 404 |
| Volver al inicio desde el perfil | Formulario sin datos de la petición anterior |

Tras cada error, la contraseña no se rellena desde el servidor. Los campos no usan `required` para poder comprobar la validación del servlet. También se controlan parámetros ausentes si se envía un POST sin alguno de ellos.

Comprueba que la tabla incluye nombre completo, usuario, edad, correo, ciclo y mayoría de edad, pero no contraseña. Revisa las tildes de García, Pérez y Administración. Los nombres y contraseñas se comparan exactamente, distinguiendo mayúsculas y sin eliminar espacios exteriores.

Después de enviar el formulario, la URL sigue siendo `/acceso`: el forward ocurre dentro del servidor. Tanto el formulario con error como el perfil responden con HTTP 200; no hay una cabecera `Location` ni una redirección.

## Explicación guiada del código

1. `Usuario` contiene los seis datos solicitados, un constructor y getters. Los campos son `final` porque no necesitamos modificarlos.
2. Tomcat llama a `AccesoServlet.init()` antes de atender peticiones. Cada nombre se usa como clave de un `HashMap<String, Usuario>` compartido que después solo se consulta.
3. `doPost()` establece UTF-8 antes de leer los parámetros. Guarda el nombre introducido en el atributo `nombreUsuario` y comprueba los datos vacíos o ausentes.
4. Si falta un dato, añade `error` a la request y hace `forward` a `/index.jsp`. El `return` evita seguir ejecutando la comprobación del acceso después del forward.
5. `usuarios.get(nombreUsuario)` devuelve el objeto o `null`. El operador `||` evita consultar la contraseña de un objeto nulo. Usuario desconocido y contraseña incorrecta comparten mensaje para no revelar qué usuarios existen.
6. Con credenciales correctas, el servlet calcula el texto de mayoría de edad con `edad >= 18`. Añade los atributos `usuario` y `mayoriaEdad`, y hace `forward` a `/WEB-INF/perfil.jsp`.
7. EL permite escribir `${requestScope.usuario.nombreCompleto}` para consultar `getNombreCompleto()`. `${error}` muestra el mensaje y `${nombreUsuario}` recupera el nombre para el formulario. Si el atributo no existe, EL muestra una cadena vacía. Por eso no necesitamos una condición para mostrar el mensaje. EL no escapa HTML automáticamente: el servlet escapa el nombre introducido antes de pasarlo a la vista; los datos del perfil y los mensajes son textos fijos preparados en el servidor.

Un **forward** utiliza la misma petición y conserva sus atributos. Un **redirect** responde al navegador para que realice otra petición: los atributos de la anterior no se conservan. El flujo concreto del enunciado utiliza forward en todos los casos, por lo que no se añade una redirección artificial.

Las JSP declaran `session="false"`. No se crea una sesión ni se mantiene un acceso entre peticiones: se consulta el perfil únicamente en el POST con credenciales correctas. `WEB-INF` impide solicitar la JSP de perfil directamente desde el navegador; no sustituye un sistema de autenticación real.

## Expresiones EL con y sin requestScope

En `index.jsp` se utilizan `${error}` y `${nombreUsuario}`, sin indicar el ámbito. EL busca los atributos automáticamente en este orden: **page → request → session → application**. En esta actividad los encuentra en la petición porque el servlet los guarda con `request.setAttribute()`.

En `perfil.jsp` se mantiene el ámbito explícito, por ejemplo `${requestScope.usuario.nombreCompleto}` y `${requestScope.mayoriaEdad}`. Así, EL consulta únicamente los atributos de la petición, sin buscar en otros ámbitos.

Las dos formas funcionan igual en este ejercicio. Se muestran ambas para aprender la diferencia: la forma abreviada es más sencilla y la explícita evita ambigüedades si existen atributos con el mismo nombre en varios ámbitos. Esta búsqueda se refiere a atributos, no a los parámetros del formulario; para estos últimos existe `${param.nombreUsuario}`.

## El atributo role en HTML

En `index.jsp`, el mensaje de error utiliza `role="alert"`. El atributo `role` describe la función de un elemento para las tecnologías de asistencia, como los lectores de pantalla. No cambia su aspecto visual ni muestra una ventana emergente. `alert` resulta especialmente útil cuando un mensaje importante aparece dinámicamente sin recargar la página.

| Valor | Significado |
| --- | --- |
| `alert` | Mensaje importante, como un error urgente. |
| `status` | Información de estado, como «Datos guardados». |
| `dialog` | Ventana de diálogo. |
| `navigation` | Zona de navegación. |
| `main` | Contenido principal de la página. |
| `search` | Zona de búsqueda. |
| `button` | Elemento que actúa como botón. |
| `tab` | Pestaña de una interfaz. |
| `tooltip` | Texto de ayuda asociado a un elemento. |
| `presentation` o `none` | Elimina la semántica del elemento, conservando su contenido. |

Es preferible utilizar etiquetas HTML que ya tengan esa función, como `<nav>`, `<main>` y `<button>`. El atributo `role` no añade comportamiento: poner `role="button"` en un `<div>` no hace que responda al teclado ni funcione automáticamente como un botón.

## Errores frecuentes

- Confundir `getParameter()` con `getAttribute()`: el primero lee el formulario y el segundo los datos añadidos en el servidor.
- Cambiar los `name` del formulario sin cambiar los parámetros del servlet.
- Usar un redirect y esperar conservar los atributos de request.
- Acceder al perfil directamente, ignorando que está dentro de `WEB-INF`.
- Guardar el usuario actual en un campo del servlet: las peticiones comparten la instancia. Aquí el usuario consultado es una variable local.
- Actualizar el perfil puede reenviar el POST, porque se ha usado forward. El navegador puede pedir confirmación; no se modifica ningún dato.

## Para practicar

Sigue una petición con credenciales incorrectas y otra correcta. Localiza dónde se crean los atributos, qué JSP los consulta y por qué el nombre se conserva tras un error pero no al volver al inicio con una nueva petición.
