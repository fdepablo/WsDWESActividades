<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Acceso al perfil</title>
</head>
<body>
    <h1>Acceso al perfil</h1>
    <p>Introduce las credenciales de un usuario de prueba para consultar su perfil.</p>

    <!-- role="alert" ayuda a las tecnologías de asistencia a identificar como un mensaje importante
         Ver README.md
     -->
    <p role="alert">${error}</p>

    <form action="${pageContext.request.contextPath}/acceso" method="post" accept-charset="UTF-8">
        <p>
            <label for="nombreUsuario">Nombre de usuario:</label>
            <input type="text" id="nombreUsuario" name="nombreUsuario"
                   value="${nombreUsuario}">
        </p>
        <p>
            <label for="contrasenia">Contraseña:</label>
            <input type="password" id="contrasenia" name="contrasenia">
        </p>
        <button type="submit">Acceder</button>
    </form>
</body>
</html>
