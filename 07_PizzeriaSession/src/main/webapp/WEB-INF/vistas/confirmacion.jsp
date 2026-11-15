<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="es_ES"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Compra confirmada</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<main>
    <h1>Compra confirmada</h1>
    <p>Tu compra ha sido confirmada y la sesión ha terminado.</p>
    <h2>Cliente</h2>
    <p><strong>Nombre:</strong> <c:out value="${requestScope.cliente.nombre}"/></p>
    <p><strong>Dirección:</strong> <c:out value="${requestScope.cliente.direccion}"/></p>
    <p><strong>Teléfono:</strong> <c:out value="${requestScope.cliente.telefono}"/></p>
    <p><strong>Correo:</strong> <c:out value="${requestScope.cliente.email}"/></p>
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
            <c:forEach items="${requestScope.pizzas}" var="pizza">
                <tr>
                    <td><c:out value="${pizza.tamano}"/></td>
                    <td>
                        <ul>
                            <c:forEach items="${pizza.ingredientes}" var="ingrediente">
                                <li><c:out value="${ingrediente}"/></li>
                            </c:forEach>
                        </ul>
                    </td>
                    <td><fmt:formatNumber value="${pizza.precio}" minFractionDigits="2" maxFractionDigits="2"/> €</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <p><strong>Total de la compra (IVA incluido):</strong>
        <fmt:formatNumber value="${requestScope.totalCompra}" minFractionDigits="2" maxFractionDigits="2"/> €
    </p>
    <p><a href="${pageContext.request.contextPath}/formulario">Volver a empezar</a></p>
</main>
</body>
</html>
