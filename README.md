# Actividades de DWES

Soluciones de actividades de Desarrollo Web en Entorno Servidor. Cada carpeta es una aplicación web independiente.

## Actividades

| Módulo | Qué hace | Ruta de entrada dentro de la aplicación |
| --- | --- | --- |
| [05_Pizzeria](05_Pizzeria/) | Formulario, validación y cálculo del precio de una pizza | `/pizzeria/formulario` |

## Cómo ejecutar

Necesitas Java 25, Maven y Tomcat 11.

1. Abre el `pom.xml` raíz en IntelliJ y carga los proyectos Maven.
2. Compila la actividad que quieras ejecutar. Por ejemplo:

   ```bash
   mvn -pl 05_Pizzeria -am package
   ```

3. En la configuración de Tomcat, añade el artefacto WAR de esa actividad en **Deployment** y arranca el servidor.
4. Abre la dirección que combina el contexto configurado con la ruta de entrada de la tabla.

Por ejemplo, si el contexto es `/05_Pizzeria_war`, abre:

http://localhost:8080/05_Pizzeria_war/pizzeria/formulario

Con IntelliJ Community puedes usar Smart Tomcat: selecciona `src/main/webapp` de la actividad como directorio de despliegue y el mismo módulo como classpath.

## Si aparece un 404

Comprueba que el despliegue haya terminado sin errores y que la URL incluya el contexto y la ruta de entrada. Las JSP de `WEB-INF` se muestran a través de los servlets; no se abren directamente.
