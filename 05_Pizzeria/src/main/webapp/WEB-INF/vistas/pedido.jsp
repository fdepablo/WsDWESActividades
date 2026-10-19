<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pedido completado</title>
</head>
<body>
<main>
    <h1>Usuario registrado</h1>

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

    <p>El precio total de la pizza es: <c:out value="${pizza.precio}"/></p>
    <p><a href="${pageContext.request.contextPath}/pizzeria/formulario">Volver al formulario de la pizzería</a></p>
</main>
</body>
</html>
