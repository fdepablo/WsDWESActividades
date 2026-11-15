# Actividad 07 Una compra con varias pizzas

La pizzería permite añadir varias pizzas antes de confirmar una compra. Un `Cliente` guarda los datos personales y una lista de pizzas en la sesión. Los servlets siguen el estilo de las actividades 04 y 05, con lectura de parámetros, llamada al gestor y elección de la JSP.

## Qué vas a aprender

- Guardar y recuperar un objeto `Cliente` mediante `HttpSession`.
- Relacionar un cliente con varias pizzas mediante `List<Pizza>`.
- Validar en el gestor y traducir sus códigos de resultado a respuestas HTTP.
- Utilizar redirect después de añadir una pizza y forward para mostrar las vistas.
- Pasar datos a request antes de invalidar la sesión.

## Requisitos previos

Java 25, Maven, Tomcat 11, Jakarta Servlet 6.1 y JSTL. Conocimientos de la pizzería 04, formularios, listas y atributos de petición.

## Estructura y responsabilidades

```text
src/main/java/
  modelo/Cliente.java
  modelo/Pizza.java
  servicios/GestorPizza.java
  servlets/PizzaServlet.java
  servlets/PedidoServlet.java
  servlets/LogoutServlet.java
src/main/webapp/
  css/estilos.css
  WEB-INF/web.xml
  WEB-INF/vistas/{formulario,pedido,confirmacion,error}.jsp
```

| Componente | Responsabilidad |
| --- | --- |
| `Cliente` | Datos personales y lista de pizzas de la compra |
| `Pizza` | Tres campos, tamaño, ingredientes y precio con IVA |
| `GestorPizza` | Valida cliente y pizza, calcula cada precio y suma el total |
| `PizzaServlet` | Muestra el formulario y añade una pizza válida a la compra |
| `PedidoServlet` | Muestra el pedido con GET y confirma la compra con POST |
| `LogoutServlet` | Invalida la sesión y redirige al formulario |

El precio se guarda y se calcula directamente en euros con `double`. Las JSP utilizan `fmt:formatNumber` para mostrarlo con dos decimales.

## Rutas y flujo

La entrada es `/formulario` dentro del contexto de la aplicación. El WAR se llama `pizzeria07`, por lo que puede comprobarse en `http://localhost:8080/pizzeria07/formulario`. El enunciado utiliza el contexto `/pizzeria`. Las rutas se adaptan al contexto del IDE mediante `request.getContextPath()`.

| Petición | Resultado |
| --- | --- |
| GET `/formulario` | Formulario con campos vacíos al empezar o con los datos del cliente de sesión |
| POST `/formulario` | Valida y añade una pizza. Si es correcta, redirige a `/pedido` |
| GET `/pedido` | Muestra cliente, pizzas y total. Si la compra está vacía, muestra un aviso |
| POST `/pedido` | Prepara los datos en request, invalida la sesión y muestra la confirmación |
| POST `/logout` | Invalida la sesión y redirige al formulario |

Al volver al formulario, los cuatro campos personales aparecen rellenos con los datos de sesión. Cada envío posterior reutiliza el cliente de sesión y añade una nueva pizza. Al finalizar la compra o cerrar sesión, el formulario vuelve a permitir introducir los datos de un cliente.

## Explicación guiada

1. `PizzaServlet.doGet()` muestra el formulario. La JSP consulta directamente `${sessionScope.cliente}` y rellena los campos con `c:out` para escapar caracteres HTML.
2. `doPost()` recupera el cliente guardado. Si todavía no existe, lo crea con los datos del formulario. Lee los ingredientes con `getParameterValues()` y crea una `Pizza` para ese envío.
3. `GestorPizza.procesar(cliente, pizza)` valida los datos en orden y calcula el precio únicamente si todo es correcto. Conserva los códigos 0 a 6 y las expresiones regulares de teléfono y correo del 04.
4. El `switch` del servlet devuelve HTTP 400 y `error.jsp` cuando falla una regla. Un pedido inválido conserva las pizzas anteriores. Al principio del POST se obtiene la sesión con `getSession()` y se consulta su atributo `cliente`. Una pizza válida se añade al cliente y este se guarda en la misma sesión. Las siguientes pizzas reutilizan ese cliente.
5. El redirect a `/pedido` genera una nueva petición GET. Actualizar el resumen vuelve a consultarlo y conserva el número de pizzas.
6. `PedidoServlet.doGet()` calcula el total y lo guarda como atributo de sesión `totalCompra`. La JSP lee `${sessionScope.cliente}`, `${sessionScope.cliente.pizzas}` y `${sessionScope.totalCompra}`.
7. Al confirmar, guarda cliente, pizzas y total en request antes de `sesion.invalidate()`. `confirmacion.jsp` los consulta con `requestScope` durante el forward.
8. `LogoutServlet` invalida la sesión existente y redirige al formulario para empezar de nuevo.

Los métodos del gestor utilizan variables locales, de modo que puede ser un atributo del servlet. Cada cliente pertenece a su sesión y `getPizzas()` devuelve la lista de su compra. Este ejemplo sigue un flujo de peticiones sucesivas. El tratamiento de operaciones simultáneas sobre una misma sesión queda fuera del ejercicio.

El formulario, el resumen y la página de error utilizan el valor predeterminado `session="true"`. La confirmación declara `session="false"`. `c:forEach` recorre las pizzas e ingredientes, `c:out` muestra textos escapados y `fmt:formatNumber` presenta los precios con dos decimales. `web.xml` configura 15 minutos de inactividad y seguimiento mediante cookie.

## Sesión y request en las JSP

El formulario y el resumen omiten el atributo `session` de la directiva `page`, por lo que se aplica `session="true"`. La JSP puede crear una sesión vacía al abrir el formulario, antes de que exista un cliente. El formulario y el resumen acceden directamente a los datos guardados en sesión mediante `sessionScope`.

`PizzaServlet.doPost()` llama una sola vez a `getSession()`. Recupera la sesión que normalmente ya ha creado la JSP o crea una si ha caducado. Después utiliza esa variable para consultar y guardar el cliente. La cookie `JSESSIONID` identifica la sesión durante la compra. Un envío inválido muestra su error y permite corregir los datos.

La confirmación declara `session="false"` y utiliza request porque el servlet invalida la sesión de la compra antes del forward. Así muestra los datos preparados por el servlet y finaliza la compra sin crear otra sesión. El mensaje de error también es un atributo de request porque pertenece a ese envío. Una sesión nueva se creará al volver a una JSP con la sesión habilitada, como el formulario.

## Validación y tarifas

| Código | Regla |
| --- | --- |
| 0 | Datos correctos y precio calculado |
| 1 | Nombre obligatorio y de hasta 60 caracteres |
| 2 | Dirección obligatoria y de hasta 150 caracteres |
| 3 | Teléfono con exactamente nueve dígitos ASCII |
| 4 | Email obligatorio, de hasta 100 caracteres y con formato básico válido |
| 5 | Tamaño `pequena`, `mediana` o `grande` |
| 6 | Al menos un ingrediente permitido, sin repetidos |

Se utilizan las tarifas base del ejercicio de sesión. La pequeña cuesta 5 €, la mediana 10 € y la grande 15 €, más 1 € por ingrediente. Se aplica el IVA del 21 %. La actividad 04 conserva su ampliación con precios distintos por ingrediente.

El gestor calcula el precio en euros con `(precio del tamaño + número de ingredientes) * 1.21`. `calcularTotal()` recorre las pizzas con un bucle y acumula sus precios en una variable `double total`.

| Pizza | Precio con IVA |
| --- | --- |
| Pequeña con queso | 7,26 € |
| Mediana con jamón y champiñones | 14,52 € |
| Grande con los cuatro ingredientes | 22,99 € |
| Compra con las dos primeras pizzas | 21,78 € |

El enunciado conserva referencias a la antigua numeración de la pizzería como actividad 05. En este workspace la pizzería corresponde al módulo `04_Pizzeria`.

## Cómo comprobar que funciona

1. Abre `/pedido` con un navegador sin sesión. Aparece el aviso de compra vacía. Un POST a esa ruta devuelve 400.
2. Introduce Ana García, Calle Mayor 1, 612345678 y ana@ejemplo.com. Añade una pequeña con queso. El resumen muestra una pizza de 7,26 €.
3. Pulsa Seguir comprando. Los cuatro campos personales aparecen rellenos. Añade una mediana con jamón y champiñones. El resumen muestra las dos pizzas y un total de 21,78 €.
4. Actualiza el resumen. La compra sigue teniendo dos pizzas.
5. Envía una pizza sin ingredientes o con un tamaño inválido. Aparece el error 400 y el pedido mantiene sus dos pizzas y su total.
6. Pulsa Realizar compra. La confirmación muestra los datos del cliente, sus pizzas y 21,78 €. Volver al formulario muestra los campos vacíos. Repetir el POST de confirmación devuelve 400.
7. Empieza otra compra y pulsa Terminar sesión. Vuelves al formulario y el pedido queda vacío.
8. Comprueba que otra pestaña comparte la compra y una ventana privada tiene su propio cliente.
9. Prueba las tildes y un nombre que incluya `<strong>Ana</strong>`. Las etiquetas se muestran como texto literal.
10. Solicita una JSP de `WEB-INF` directamente. La respuesta es 404. GET `/logout` devuelve 405.

## Errores frecuentes

- Guardar el cliente en un campo del servlet mezclaría compras de distintos usuarios. El cliente se guarda como atributo de sesión.
- Crear un cliente nuevo en cada envío perdería la lista de pizzas anterior.
- Hacer redirect a una JSP de `WEB-INF` impediría abrirla. Las JSP se muestran con forward.
- Invalidar la sesión antes de preparar los atributos de la confirmación perdería el acceso a los datos necesarios.
- La cookie `JSESSIONID` identifica la sesión. Los objetos del cliente y las pizzas se guardan en el servidor.
- El botón Atrás puede mostrar una página anterior del navegador. Para comprobar el cierre, solicita de nuevo el formulario o el pedido.