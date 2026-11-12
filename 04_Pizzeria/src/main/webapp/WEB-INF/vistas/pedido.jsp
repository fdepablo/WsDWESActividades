<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pedido completado</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<main>
    <h1>Pedido de pizza aceptado</h1>

    <p><strong>Nombre:</strong> <c:out value="${pizza.nombre}"/></p>
    <p><strong>Dirección:</strong> <c:out value="${pizza.direccion}"/></p>
    <p><strong>Teléfono:</strong> <c:out value="${pizza.telefono}"/></p>
    <p><strong>Correo:</strong> <c:out value="${pizza.email}"/></p>
    <p><strong>Tamaño:</strong> <c:out value="${pizza.tamanio}"/></p>

    <p><strong>Ingredientes:</strong></p>
    <ul>
    <c:forEach items="${pizza.listaIngredientes}" var="ingredientes">
        <li><c:out value="${ingredientes}"/></li>
    </c:forEach>
    </ul>

    <fmt:setLocale value="es_ES"/>
    <p><strong>Total con IVA del 21 %:</strong> <fmt:formatNumber value="${pizza.precio}" minFractionDigits="2" maxFractionDigits="2"/> €</p>
    <p><a href="${pageContext.request.contextPath}/formulario">Volver al formulario de la pizzería</a></p>
</main>
</body>
</html>
