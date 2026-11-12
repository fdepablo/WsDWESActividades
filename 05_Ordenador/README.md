# Actividad 05 Crea tu ordenador

Un único servlet permite configurar un ordenador mediante un formulario y mostrar su precio. Sigue la separación de responsabilidades de la pizzería con menos datos y reglas. Los extras son opcionales y el cálculo no lleva IVA.

## Qué vas a aprender

- Atender GET y POST en un mismo servlet.
- Leer radios y casillas con `getParameter()` y `getParameterValues()`.
- Convertir textos a enumerados con `valueOf()` y recorrer una lista con un bucle.
- Separar modelo, validación y vistas JSP.
- Volver al formulario original cuando hay un error.

## Requisitos previos

Java 25, Maven, Tomcat 11, Jakarta Servlet 6.1 y JSTL. Conocimientos de formularios, JSP y enumerados como los de la pizzería.

## Conceptos clave y estructura

```text
src/main/java/
  modelo/Ordenador.java              Datos de una configuración
  modelo/{Procesador,Extra}.java     Opciones permitidas y precios
  servicios/GestorOrdenador.java     Validación y cálculo
  servlets/ServletOrdenador.java     GET y POST del formulario
src/main/webapp/WEB-INF/vistas/
  formulario.jsp                    Formulario y errores
  resumen.jsp                       Configuración aceptada y precio
```

`Ordenador` contiene nombre, procesador, lista de extras y precio total. La lista se inicializa vacía para representar una configuración sin extras. Los enumerados guardan precios inmutables en euros enteros.

## Tarifas

| Componente | Valor enviado | Precio añadido |
| --- | --- | --- |
| Base del equipo | — | 300 € |
| Procesador básico | `BASICO` | 100 € |
| Procesador potente | `POTENTE` | 200 € |
| Memoria adicional | `RAM` | 50 € |
| Disco adicional | `SSD` | 80 € |
| Conexión inalámbrica | `WIFI` | 20 € |

Total = 300 € + precio del procesador + suma de extras. No se añade IVA.

## Explicación guiada

1. GET `/configurar` llama a `doGet()`, que muestra el formulario mediante forward. Las JSP están en `WEB-INF` y no pueden abrirse directamente.
2. El formulario envía un POST a la misma ruta. No se preselecciona procesador ni se usa `required`, para que se puedan comprobar los errores en el servidor.
3. El servlet lee el nombre en UTF-8 y aplica `strip()`. Crea un `Ordenador` local para esa petición.
4. `Procesador.valueOf()` convierte `BASICO` o `POTENTE` al enum correspondiente. Si no se selecciona ninguno, el procesador queda en `null` para que lo detecte el gestor.
5. `getParameterValues("extras")` devuelve un array o `null` si no hay casillas marcadas. El servlet convierte los valores con `Extra.valueOf()` dentro de un bucle y los añade a una lista. No se comprueban extras repetidos, conforme al alcance del enunciado.
6. Si un valor manipulado no existe en el enum, `valueOf()` lanza `IllegalArgumentException`; el servlet la captura y vuelve al formulario con un mensaje, en lugar de producir un error 500.
7. `GestorOrdenador.procesar()` comprueba primero el nombre y después el procesador. Devuelve 1 o 2 ante esos errores sin asignar precio. Si todo es correcto, suma la base y los componentes, asigna el precio y devuelve 0.
8. El servlet hace forward al formulario con HTTP 400 si hay un error. Conserva el nombre introducido mediante un atributo de request; el procesador y los extras se seleccionan de nuevo. Si todo es correcto, pasa el ordenador a `resumen.jsp` con HTTP 200.

Las JSP utilizan `c:out` para mostrar texto escapado, `c:if` para los mensajes y `c:forEach` para la lista de extras. El gestor no guarda pedidos: puede ser un atributo del servlet sin compartir la configuración de cada cliente. No se utilizan sesiones ni base de datos.

## Cómo comprobar que funciona

Ruta de entrada: `/configurar` dentro del contexto de la aplicación; por ejemplo, `http://localhost:8080/ordenador/configurar`. GET muestra el formulario y POST procesa los datos en esa misma ruta.

| Caso | Resultado esperado |
| --- | --- |
| Nombre válido, básico sin extras | 200; **400 €**, con el mensaje «Sin extras» |
| Nombre válido, potente con RAM y SSD | 200; **630 €** |
| Nombre válido, básico con todos los extras | 200; **550 €** |
| Nombre vacío o solo espacios | 400; error en el formulario original |
| Nombre válido sin procesador | 400; error en el formulario, conservando el nombre |
| Valor externo `procesador=OTRO` o `extras=OTRO` | 400; aviso de opción no válida |
| Nombre `<strong>Equipo</strong>` | Se muestra como texto literal, sin generar negrita |
| Acceso directo a `/WEB-INF/vistas/resumen.jsp` | 404 |
| Dos configuraciones con nombres distintos | Cada resumen contiene únicamente sus propios datos |

Comprueba también las tildes, el enlace de vuelta y que un equipo sin extras se acepte. Los campos enviados son `nombre`, `procesador` y `extras`; no se utiliza un precio recibido del navegador.

## Errores frecuentes

- `valueOf()` distingue mayúsculas: los valores HTML deben coincidir con las constantes del enum.
- No se puede recorrer directamente el resultado de `getParameterValues()` sin comprobar si es `null`.
- Tener cero extras es correcto; tener cero procesadores no lo es.
- El precio base se suma una vez y el IVA no forma parte de este ejercicio.
- Tras un forward, la URL continúa en `/configurar`. Actualizar el resumen puede reenviar el POST; no se guarda ningún pedido.

## Para practicar

Calcula a mano los tres ejemplos del enunciado y sigue el bucle del gestor para comprobar qué cantidades suma. Después compara lo que devuelve `getParameterValues()` sin extras y con varias casillas marcadas.
