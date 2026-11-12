# Actividades de DWES

Soluciones de actividades de Desarrollo Web en Entorno Servidor. Cada carpeta es una aplicación web independiente.

## Actividades

| Módulo | Qué hace | Ruta de entrada dentro de la aplicación |
| --- | --- | --- |
| [01_Servlets](01_Servlets/) | Calcula las cuatro operaciones básicas con dos números | `/` |
| [02_JSP](02_JSP/) | Acceso simulado y perfil con atributos de petición y JSP | `/` |
| [03_HTTP](03_HTTP/) | Peticiones HTTP sobre estudiantes y escrituras simuladas | `/estudiantes/15` o `/inicio` |
| [04_Pizzeria](04_Pizzeria/) | Formulario, validación y cálculo del precio de una pizza | `/formulario` |
| [05_Ordenador](05_Ordenador/) | Configura un ordenador con procesador y extras opcionales | `/configurar` |

## Si aparece un 404

Comprueba que el despliegue haya terminado sin errores y que la URL incluya el contexto y la ruta de entrada. Las JSP de `WEB-INF` se muestran a través de los servlets; no se abren directamente.
