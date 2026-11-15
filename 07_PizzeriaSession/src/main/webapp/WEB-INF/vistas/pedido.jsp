<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="es_ES"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tu pedido</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<main>
    <h1>Tu pedido</h1>
    <c:choose>
        <c:when test="${empty sessionScope.cliente.pizzas}">
            <p>No hay pizzas en tu pedido.</p>
            <p><a href="${pageContext.request.contextPath}/formulario">Ir al formulario</a></p>
        </c:when>
        <c:otherwise>
    <h2>Cliente</h2>
    <p><strong>Nombre:</strong> <c:out value="${sessionScope.cliente.nombre}"/></p>
    <p><strong>Dirección:</strong> <c:out value="${sessionScope.cliente.direccion}"/></p>
    <p><strong>Teléfono:</strong> <c:out value="${sessionScope.cliente.telefono}"/></p>
    <p><strong>Correo:</strong> <c:out value="${sessionScope.cliente.email}"/></p>
    <table>
        <caption>Pizzas de la compra</caption>
        <thead>
            <tr>
                <th scope="col">Tamaño</th>
                <th scope="col">Ingredientes</th>
                <th scope="col">Precio con IVA</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${sessionScope.cliente.pizzas}" var="pizza">
                <tr>
                    <td><c:out value="${pizza.tamano}"/></td>
                    <td>
                        <ul>
                            <c:forEach items="${pizza.ingredientes}" var="ingrediente">
                                <li><c:out value="${ingrediente}"/></li>
                            </c:forEach>
                        </ul>
                    </td>
                    <!-- formatNumber nos sirve para dar un formato más preciso a nuestro precio -->
                    <td><fmt:formatNumber value="${pizza.precio}" minFractionDigits="2" maxFractionDigits="2"/> €</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <p><strong>Total de la compra (IVA incluido):</strong>
        <fmt:formatNumber value="${sessionScope.totalCompra}" minFractionDigits="2" maxFractionDigits="2"/> €
    </p>
            <p><a href="${pageContext.request.contextPath}/formulario">Seguir comprando</a></p>
            <form action="${pageContext.request.contextPath}/pedido" method="post">
                <p><button type="submit">Realizar compra</button></p>
            </form>
            <form action="${pageContext.request.contextPath}/logout" method="post">
                <p><button type="submit">Terminar sesión</button></p>
            </form>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
