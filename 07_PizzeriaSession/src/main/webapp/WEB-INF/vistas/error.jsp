<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Revisa el pedido</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<main>
    <h1>Revisa el pedido</h1>
    <p role="alert"><c:out value="${mensajeError}"/></p>
    <p><a href="${pageContext.request.contextPath}/formulario">Volver al formulario</a></p>
    <p><a href="${pageContext.request.contextPath}/pedido">Ver pedido</a></p>
</main>
</body>
</html>