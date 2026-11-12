# Actividad 01 Calculadora con un servlet

Un formulario envía dos números a un servlet, que muestra su suma, resta, multiplicación y división en una página HTML. Es una introducción a los servlets sin JSP, frameworks ni clases auxiliares.

## Qué vas a aprender

- Enviar datos de un formulario mediante GET.
- Relacionar los atributos `name` con `request.getParameter()`.
- Convertir texto a números y realizar operaciones aritméticas.
- Generar una respuesta HTML desde un servlet y controlar entradas incorrectas.

## Requisitos previos

Java 25, Maven y Tomcat 11. Conocimientos básicos de HTML, variables, condicionales y métodos Java.

## Conceptos clave

`@WebServlet` asocia una URL con una clase. Tomcat llama a `doGet()` cuando recibe una petición GET. Los parámetros llegan como texto y se convierten a `double` para admitir decimales. `PrintWriter` permite escribir la página que recibe el navegador.

## Estructura relevante

```text
src/main/java/servlets/CalculadoraServlet.java
src/main/webapp/index.html
src/main/webapp/WEB-INF/web.xml
```

El HTML contiene el formulario; el servlet valida y calcula; `web.xml` establece `index.html` como página de bienvenida.

## Cómo comprobar que funciona

Introduce los valores del cuadro y pulsa **Calcular**. La resta y la división utilizan el primer número como primer operando.

| Primer número | Segundo número | Suma | Resta | Multiplicación | División |
| --- | --- | --- | --- | --- | --- |
| 8 | 2 | 10.0 | 6.0 | 16.0 | 4.0 |
| 5 | 2 | 7.0 | 3.0 | 10.0 | 2.5 |
| -3 | 2 | -1.0 | -5.0 | -6.0 | -1.5 |
| 1.5 | 0.5 | 2.0 | 1.0 | 0.75 | 3.0 |
| 8 | 0 | 8.0 | 8.0 | 0.0 | Aviso de división entre cero |

También puedes probar directamente el servlet:

```text
http://localhost:8080/01_Servlets/calcular?numero1=8&numero2=2
```

Comprueba estos errores editando la URL, porque el navegador impide enviar algunos desde el formulario:

| Parámetros después de `/calcular` | Resultado esperado |
| --- | --- |
| Sin parámetros | HTTP 400 y aviso de que faltan números |
| `?numero1=8` | HTTP 400 y aviso de que faltan números |
| `?numero1=&numero2=2` | HTTP 400 y aviso de que faltan números |
| `?numero1=hola&numero2=2` | HTTP 400 y aviso de valores no numéricos |
| `?numero1=NaN&numero2=2` | HTTP 400 y aviso de números finitos |

Las peticiones válidas responden con HTTP 200, incluso cuando el segundo número es cero: se muestran las tres operaciones posibles y un aviso en la división. Puedes ver el estado y `Content-Type: text/html;charset=UTF-8` en la pestaña Red de las herramientas del navegador. Revisa que «Multiplicación» y «División» se lean correctamente y que el enlace permita volver al formulario.

## Explicación guiada

1. El formulario utiliza `action="calcular"` y `method="get"`. El navegador añade `numero1` y `numero2` a la URL. GET es adecuado porque solo consultamos un cálculo y no modificamos datos.
2. La anotación `@WebServlet("/calcular")` permite a Tomcat encontrar el servlet. `getParameter()` recupera cada valor por su nombre.
3. Se comprueba que los valores existan y no estén vacíos. `Double.parseDouble()` convierte el texto. El bloque `try/catch` controla los valores que no se pueden convertir, y `Double.isFinite()` rechaza infinito y NaN.
4. Si hay un error se responde con estado 400. Si los números son válidos, se calculan directamente las cuatro operaciones. Los paréntesis en las sumas evitan concatenar los operandos como texto.
5. Antes de dividir se comprueba `numero2 == 0`. El resto de operaciones se muestra con normalidad.
6. Se establece UTF-8 antes de obtener el escritor de la respuesta. Los datos del usuario se muestran solo después de convertirlos a números, evitando insertar texto arbitrario en el HTML.

Todas las variables del cálculo son locales a `doGet()`: cada petición tiene sus propios valores. Se escribe HTML desde Java para centrarse en el primer servlet; en ejemplos posteriores se puede separar la presentación mediante JSP.

## Errores frecuentes y límites del ejemplo

- **404:** revisa el contexto y que el WAR esté desplegado en Tomcat 11.
- **Faltan números:** los nombres de los parámetros deben coincidir con los `name` del formulario.
- **Decimales en la URL:** utiliza punto, por ejemplo `1.5`. La presentación del campo del formulario puede depender del idioma del navegador.
- **Resultados con muchos decimales:** `double` usa representación binaria y puede mostrar aproximaciones, por ejemplo al sumar 0.1 y 0.2. Para dinero se suele utilizar `BigDecimal`; aquí se prioriza aprender el servlet.
- **Números extremadamente grandes:** aunque las entradas sean finitas, una operación puede desbordar el rango de `double` y mostrar `Infinity`. Este ejemplo se centra en cálculos sencillos y no implementa aritmética de precisión arbitraria.

Para practicar, explica el recorrido desde el botón **Calcular** hasta `doGet()` y comprueba los casos de la tabla.
