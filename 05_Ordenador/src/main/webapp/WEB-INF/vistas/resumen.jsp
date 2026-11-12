<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Resumen del ordenador</title>
</head>
<body>
    <h1>Resumen del ordenador</h1>
    <p><strong>Nombre:</strong> <c:out value="${ordenador.nombre}"/></p>
    <p><strong>Procesador:</strong> <c:out value="${ordenador.procesador}"/></p>
    <h2>Extras</h2>
    <c:if test="${empty ordenador.extras}">
        <p>Sin extras.</p>
    </c:if>
    <ul>
        <c:forEach items="${ordenador.extras}" var="extra">
            <li><c:out value="${extra}"/></li>
        </c:forEach>
    </ul>
    <p><strong>Precio total sin IVA:</strong> <c:out value="${ordenador.precioTotal}"/> €</p>
    <p><a href="${pageContext.request.contextPath}/configurar">Volver al formulario</a></p>
</body>
</html>
