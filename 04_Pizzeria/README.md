# Pizzería con validación en servidor y enumerados

Solución del ejercicio de la pizzería, conservada en `04_Pizzeria`. Cada envío corresponde a una pizza. Se mantiene la ampliación con los enumerados `Tamanio` e `Ingrediente`: el gestor valida, calcula y asigna el precio; los dos servlets preparan la petición y eligen la JSP.

## Qué vas a aprender

- Recibir parámetros simples y múltiples de un formulario.
- Centralizar la validación en `GestorPizza` y utilizar sus códigos de resultado.
- Convertir códigos externos a valores de enumerados con tarifas.
- Mostrar datos con JSTL y escapar entradas mediante `c:out`.
- Separar los datos de cada petición y calcular un precio con IVA.

## Requisitos previos

Java 25, Maven, Jakarta Servlet 6.1, JSTL y Tomcat 11. Conocimientos de servlets, JSP, listas y enumerados.

## Estructura y conceptos clave

```text
src/main/java/
  servlets/PizzaServlet.java       GET del formulario
  servlets/PedidoServlet.java      POST del pedido y selección de vista
  modelo/Pizza.java                Datos recibidos y pedido validado
  modelo/{Tamanio,Ingrediente}.java Códigos y tarifas
  servicios/GestorPizza.java       Validación y cálculo
src/main/webapp/
  css/estilos.css
  WEB-INF/vistas/{formulario,pedido,error}.jsp
```

`Pizza` guarda los textos personales, los códigos recibidos y, tras validar, el tamaño y los ingredientes convertidos a enumerados. Los códigos externos permiten detectar valores desconocidos y repetidos antes de convertirlos. Así se conserva `procesar(Pizza pizza)` y se mantienen los enumerados de la versión ampliada.

## Rutas

Con contexto `/pizzeria`, la entrada es `http://localhost:8080/pizzeria/formulario`. El contexto no se repite en las anotaciones: los servlets tienen las rutas `/formulario` y `/pedido`.

| Petición | Resultado |
| --- | --- |
| GET `/formulario` | 200 y `formulario.jsp` mediante forward |
| POST `/pedido` válido | 200 y `pedido.jsp` mediante forward |
| POST `/pedido` inválido | 400 y `error.jsp` mediante forward |
| GET `/pedido` | 405: no se implementa `doGet()` |
| Acceso directo a `/WEB-INF/vistas/pedido.jsp` | 404 |

El formulario no contiene JavaScript ni validación del navegador, tampoco preselecciona radios o casillas. Correo y teléfono usan `type="text"`.

## Contrato del gestor

`procesar(Pizza pizza)` recibe un objeto no nulo y valida en este orden. Devuelve el primer error y no calcula ni modifica el precio si falla algún dato.

| Constante | Código | Regla |
| --- | --- | --- |
| `PEDIDO_CORRECTO` | 0 | Todo válido; enumerados y precio asignados |
| `ERROR_NOMBRE` | 1 | Obligatorio tras quitar espacios exteriores; máximo 60 caracteres |
| `ERROR_DIRECCION` | 2 | Obligatoria tras quitar espacios exteriores; máximo 150 caracteres |
| `ERROR_TELEFONO` | 3 | Exactamente nueve dígitos ASCII del 0 al 9 |
| `ERROR_EMAIL` | 4 | Obligatorio, máximo 100 caracteres, una @, sin espacios y un punto interior en el dominio |
| `ERROR_TAMANO` | 5 | Debe recibirse exactamente un valor: `pequena`, `mediana` o `grande` |
| `ERROR_INGREDIENTES` | 6 | Al menos uno; códigos permitidos, sin repetidos |

El servlet utiliza `strip()` y prepara valores vacíos para parámetros ausentes. El gestor realiza todas las validaciones, incluidas las de parámetros manipulados. El enunciado alterna `procesar` y `procesarPedido`; esta solución utiliza `procesar`, conforme a la firma solicitada.

## Tarifas de la ampliación e IVA

Se conserva la ampliación solicitada: los ingredientes tienen tarifas distintas, en lugar de costar todos 1 €.

| Tamaño | Base sin IVA | Ingrediente | Precio sin IVA |
| --- | --- | --- | --- |
| Pequeña | 5 € | Queso | 1 € |
| Mediana | 10 € | Jamón | 2 € |
| Grande | 15 € | Champiñones | 3 € |
| — | — | Aceitunas | 4 € |

| Pedido | Base con ingredientes | Total con IVA del 21 % |
| --- | --- | --- |
| Pequeña con queso | 6 € | **7,26 €** |
| Mediana con jamón y champiñones | 15 € | **18,15 €** |
| Grande con los cuatro ingredientes | 25 € | **30,25 €** |

Los ejemplos de 6 €, 12 € y 19 € del cuerpo principal del enunciado son anteriores al IVA y usan la tarifa uniforme de 1 € por ingrediente. Los dos últimos cambian al conservar esta ampliación.

`calcularPrecio()` es privado y solo se llama después de validar. Devuelve céntimos: como todas las tarifas son euros enteros, multiplicar la suma por 121 incluye exactamente el IVA. `Pizza` guarda esos céntimos y `getPrecio()` devuelve euros; `fmt:formatNumber` muestra dos decimales. El parámetro externo `precio` no se utiliza.

## Explicación guiada

1. `PizzaServlet` muestra la JSP del formulario con forward.
2. `PedidoServlet` lee el POST en UTF-8, limpia los textos y obtiene `tamano` e `ingredientes` con `getParameterValues()`. Para tamaño se conserva el array completo: una petición manipulada podría enviar varios valores aunque el formulario use radios.
3. Crea una `Pizza` local con los datos y llama a `gestorPizza.procesar(pizza)`.
4. El gestor valida los datos personales, busca los códigos en los enumerados y rechaza selecciones desconocidas o repetidas. Solo al terminar asigna el tamaño, los ingredientes y el total.
5. El `switch` del servlet traduce los códigos en 200 con el resumen o 400 con un mensaje de error. La JSP de resumen utiliza `c:out` para mostrar el texto sin interpretarlo como HTML.

El gestor puede ser un atributo del servlet porque no almacena clientes ni pedidos entre llamadas. No necesita métodos `synchronized`: las variables de cada cálculo son locales y cada petición crea su propia `Pizza`. Las tarifas de los enumerados son inmutables. No se utilizan sesiones ni se conserva un pedido anterior.

## Cómo comprobar que funciona

Para las pruebas inválidas, cambia solo el dato indicado y conserva válidos los demás. Puedes manipular parámetros desde Postman con un POST de tipo `application/x-www-form-urlencoded`.

| Caso | Resultado esperado |
| --- | --- |
| Los tres pedidos de la tabla de precios | 200 y totales 7,26 €, 18,15 € y 30,25 € |
| Nombre o dirección vacíos, en blanco o demasiado largos | 400 y mensaje del campo |
| Dirección de 150 caracteres / 151 caracteres | Se acepta / 400 |
| Teléfono de 8 o 10 dígitos, o con letras | 400; nueve dígitos se aceptan |
| Correo sin @, con dos @, espacios o sin punto en el dominio | 400 |
| Sin tamaño, `tamano=familiar` o dos parámetros `tamano` | 400 y error de tamaño |
| Sin ingredientes, `ingredientes=trufa` o `queso` dos veces | 400 y error de ingredientes |
| Nombre vacío y correo inválido a la vez | 400 y mensaje del nombre |
| Nombre `<strong>Ana</strong>` y resto válido | 200; aparece el texto literal |
| Añadir `precio=0` a un pedido válido | El total calculado no cambia |
| GET `/pedido` / acceso directo a una JSP de `WEB-INF` | 405 / 404 |
| Dos pedidos válidos consecutivos con clientes distintos | Cada resumen contiene solo sus propios datos |

Comprueba las tildes, los enlaces de vuelta y que los errores se puedan provocar desde el formulario sin que el navegador bloquee el envío.

## Errores frecuentes y práctica

- Repetir `/pizzeria` en la ruta del servlet y en el contexto produce una URL distinta de la esperada.
- Los códigos HTML son minúsculos (`champinones`), aunque las constantes Java sean mayúsculas (`CHAMPINIONES`). El método `buscarPorCodigo()` realiza esa correspondencia.
- Las tarifas del formulario son sin IVA; el resumen presenta el total con IVA.
- No se debe leer un precio enviado por el cliente ni guardar la pizza actual en un campo del servlet.

Como práctica, sigue un POST válido y uno inválido y localiza qué datos prepara el servlet, cuál es el primer error del gestor y qué atributos recibe la JSP.
