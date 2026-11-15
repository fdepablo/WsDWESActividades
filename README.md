# Actividades de DWES

Soluciones de actividades de Desarrollo Web en Entorno Servidor. Cada carpeta es un módulo independiente. Las actividades web y los ejercicios de consola comparten el agregador Maven.

## Actividades

| Módulo | Qué hace | Entrada |
| --- | --- | --- |
| [01_Servlets](01_Servlets/) | Calcula las cuatro operaciones básicas con dos números | `/` |
| [02_JSP](02_JSP/) | Acceso simulado y perfil con atributos de petición y JSP | `/` |
| [03_HTTP](03_HTTP/) | Peticiones HTTP sobre estudiantes y escrituras simuladas | `/estudiantes/15` o `/inicio` |
| [04_Pizzeria](04_Pizzeria/) | Formulario, validación y cálculo del precio de una pizza | `/formulario` |
| [05_Ordenador](05_Ordenador/) | Configura un ordenador con procesador y extras opcionales | `/configurar` |
| [06_Lambda](06_Lambda/) | Doce ejercicios de funciones lambda con un catálogo de videojuegos | `lambdas.SolucionesVideojuegos.main()` |

## Si aparece un 404

Comprueba que el despliegue haya terminado sin errores y que la URL incluya el contexto y la ruta de entrada. Las JSP de `WEB-INF` se muestran a través de los servlets; no se abren directamente.
