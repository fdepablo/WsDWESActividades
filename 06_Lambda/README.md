# Actividad 06 Funciones lambda con videojuegos

Solución Java de los doce apartados de `Actividad_06_Lambda`. Una clase con `main()` trabaja con el catálogo y muestra los resultados en consola.

## Qué vas a aprender

- Definir funciones lambda con uno y dos parámetros.
- Utilizar `Function`, `Predicate`, `BiPredicate` y `BiFunction` con `apply()` y `test()`.
- Transformar y filtrar un catálogo con `map()` y `filter()`.
- Recorrer resultados con `forEach()` y recogerlos con `toList()`.
- Crear objetos con precios rebajados y calcular un total con `mapToDouble().sum()`.

## Requisitos previos

Java 25, Maven e IntelliJ IDEA. Conocimientos de listas, condiciones, funciones lambda y operaciones básicas de streams.

## Estructura y conceptos clave

```text
pom.xml
src/main/java/lambdas/SolucionesVideojuegos.java
```

Un `record` es un tipo especial de clase Java pensado para agrupar datos con poco código. Java genera automáticamente el constructor, los métodos de acceso y los métodos `equals()`, `hashCode()` y `toString()`. Sus campos son finales, por lo que no se pueden reasignar después de crear el objeto.

En este ejemplo, `record Videojuego` reúne nombre, precio, valoración y género. Sus datos se consultan con métodos como `nombre()` y `precio()`. Para rebajar el precio se crea otro `Videojuego` con el nuevo importe.

## Explicación guiada de los doce apartados

1. `Function<Videojuego, String>` recibe una ficha y devuelve su presentación. `apply()` la evalúa para Minecraft y Hollow Knight.
2. El apartado **2.a** utiliza `Predicate<Videojuego>` con un presupuesto de 30 € definido fuera de la lambda. Recibe solo el juego y captura `presupuestoDisponible`, que debe ser final o efectivamente final. El apartado **2.b** utiliza `BiPredicate<Videojuego, Double>` y recibe juego y presupuesto en cada llamada. Ambas versiones se evalúan con `test()` e incluyen el precio exacto mediante `<=`. La versión 2.b cumple la petición del enunciado de recibir los dos datos como parámetros.
3. `BiFunction<Videojuego, Videojuego, String>` compara dos valoraciones. Primero detecta el empate y después devuelve el nombre del mejor valorado.
4. Las variantes **4.a** y **4.b** usan `map()` para obtener los nombres en el orden del catálogo. La 4.a termina con `collect(Collectors.toList())`. La 4.b utiliza `toList()` y muestra que `add()` lanza `UnsupportedOperationException`.
5. `filter()` conserva las fichas completas del género Sandbox.
6. La variante **6.a** combina presupuesto de hasta 20 € y valoración de al menos 9 en un solo `filter()` mediante `&&`. La **6.b** aplica dos filtros consecutivos, primero por precio y después por valoración. Ambas obtienen las mismas fichas y conservan su orden.
7. `IntStream.range(0, videojuegos.size())` proporciona índices desde cero. `forEach()` muestra cada ficha con su índice más uno, de modo que la numeración empieza en 1.
8. `map()` crea una ficha nueva por juego con el precio multiplicado por `0.75`. Conserva el nombre, la valoración y el género. El catálogo original mantiene sus precios.
9. La variante **9.a** utiliza una lambda en `mapToDouble()` para extraer los precios y los suma con `sum()`. La **9.b** inicializa `totalConBucle` a cero y recorre la lista con un bucle `for`, acumulando cada precio mediante `+=`. Ambas calculan el coste de una copia de cada juego.
10. `filter()` selecciona precios entre 15 y 40 €, con ambos límites incluidos. Después, `map()` obtiene solo los nombres.
11. La variante **11.a** usa `Function<Double, List<String>>` para recibir solo el presupuesto y captura el catálogo `videojuegos` del entorno. La **11.b** usa `BiFunction<List<Videojuego>, Double, List<String>>` para recibir catálogo y presupuesto en cada llamada, tal como pide el enunciado. Ambas filtran las fichas, devuelven mensajes con nombre y precio y se prueban con 15 € y con 5 €.
12. Se filtra por valoración, se crean fichas con un descuento del 10 %, se filtra por el precio rebajado y se muestra cada recomendación. La condición de 15 € se comprueba después del descuento.

Cada llamada a `stream()` inicia un recorrido del catálogo. Las operaciones intermedias como `map()` y `filter()` se completan al llegar a una operación terminal como `toList()`, `sum()` o `forEach()`.

## Cómo comprobar que funciona

El punto de entrada es `lambdas.SolucionesVideojuegos.main()`. La consola muestra los apartados en el mismo orden que sus comentarios numerados en el código.

| Apartado | Resultado esperado |
| --- | --- |
| 1 | Presentaciones de Minecraft, 30 € y valoración 9, y Hollow Knight, 15 € y valoración 9,5 |
| 2.a | Con presupuesto fijo de 30 €, Minecraft devuelve `true` y Elden Ring devuelve `false` |
| 2.b | Minecraft con presupuestos de 35, 20 y 30 € devuelve `true`, `false`, `true` |
| 3 | Minecraft frente a Hollow Knight devuelve `Hollow Knight`. Minecraft frente a Stardew Valley devuelve `Tienen la misma valoración` |
| 4.a | Los seis nombres en orden, recogidos con `collect(Collectors.toList())` |
| 4.b | Los mismos seis nombres con `toList()`. Después, el mensaje que indica que no permite añadir elementos |
| 5 | Fichas completas de Minecraft y Terraria |
| 6.a y 6.b | Ambas muestran las fichas completas de Hollow Knight y Stardew Valley |
| 7 | Seis líneas desde `1. Minecraft` hasta `6. Terraria`, con sus precios originales |
| 8 | Precios rebajados de 22,50 €, 11,25 €, 10,50 €, 30 €, 45 € y 7,50 €, en el orden original |
| 9.a y 9.b | Ambas muestran un total de **169 €** |
| 10 | Minecraft, Hollow Knight y EA Sports FC 24 |
| 11.a y 11.b | Con 15 €, Hollow Knight, Stardew Valley y Terraria. Con 5 €, una lista vacía `[]` |
| 12 | Hollow Knight con **13,50 €** y Stardew Valley con **12,60 €** |

La consola utiliza el formato habitual de `double`, por ejemplo `30.0`, `9.5` y `13.5`. Las listas de fichas muestran la representación generada por el record. En el apartado 8 se imprime el catálogo rebajado y los apartados posteriores consultan los precios originales.

## Diferencia entre toList y collect

`stream().toList()` devuelve una lista no modificable, también llamada inmutable respecto a sus elementos y posiciones. No permite usar `add()`, `remove()` ni `set()`. En el apartado 4.b se intenta añadir un nombre y se captura `UnsupportedOperationException` para mostrar el aviso y continuar el programa.

`stream().collect(Collectors.toList())` recoge los elementos en una lista. Aunque la implementación habitual devuelve un `ArrayList` modificable, su contrato no garantiza el tipo ni la posibilidad de modificarla. Por eso el apartado 4.a solo muestra el resultado.

Si necesitas garantizar que se pueda añadir un elemento, puedes solicitar expresamente un `ArrayList` mediante `collect(Collectors.toCollection(ArrayList::new))`, importando `java.util.ArrayList`. La posibilidad de modificar una lista y la de modificar los objetos que contiene son cuestiones distintas.

Los apartados 4.a, 5, 8 y 10 utilizan `collect(Collectors.toList())`. Los apartados 4.b, 6.a, 6.b, 11.a y 11.b utilizan `toList()`.

## Revisión de la solución previa

Las condiciones, el orden de las listas, el empate, los presupuestos inclusivos, los descuentos y el total son correctos. El archivo de origen se llamaba `Solucion_Funciones_Flecha.java`, pero declaraba la clase pública `SolucionesVideojuegos`. En el módulo se guarda como `SolucionesVideojuegos.java`, conforme a las reglas de Java, y se añade el paquete `lambdas`.

## Errores frecuentes

- Una lambda necesita un tipo de interfaz funcional. `Function` y `BiFunction` se llaman con `apply()`, mientras que `Predicate` y `BiPredicate` se llaman con `test()`.
- Los valores capturados por una lambda deben ser finales o efectivamente finales. Por eso una variable local `double total` no se puede ir modificando directamente dentro de `forEach()`.
- El último índice de `IntStream.range()` queda excluido, por lo que se usa el tamaño completo de la lista.
- Cambiar `<=` por `<` excluiría el precio exacto y los límites que el ejercicio pide incluir.
- Las listas recogidas con `toList()` son no modificables. Para cambiar precios se crea un catálogo nuevo.
- `double` puede producir pequeñas aproximaciones en otros cálculos. Aquí se conserva el tipo de la solución previa para practicar lambdas con los importes del enunciado.
