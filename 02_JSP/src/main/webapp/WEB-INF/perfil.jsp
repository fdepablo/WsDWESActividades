<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Perfil del usuario</title>
</head>
<body>
    <h1>Perfil del usuario</h1>
    <table>
        <caption>Datos del usuario</caption>
        <tbody>
            <tr><th scope="row">Nombre completo</th><td>${requestScope.usuario.nombreCompleto}</td></tr>
            <tr><th scope="row">Nombre de usuario</th><td>${requestScope.usuario.nombreUsuario}</td></tr>
            <tr><th scope="row">Edad</th><td>${requestScope.usuario.edad}</td></tr>
            <tr><th scope="row">Correo electrónico</th><td>${requestScope.usuario.correoElectronico}</td></tr>
            <tr><th scope="row">Ciclo formativo</th><td>${requestScope.usuario.cicloFormativo}</td></tr>
            <tr><th scope="row">Mayoría de edad</th><td>${requestScope.mayoriaEdad}</td></tr>
        </tbody>
    </table>
    <p><a href="${pageContext.request.contextPath}/index.jsp">Volver al inicio</a></p>
</body>
</html>
