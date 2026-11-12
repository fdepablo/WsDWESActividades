<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Crea tu ordenador</title>
</head>
<body>
    <h1>Crea tu ordenador</h1>
    <p>Precio base: 300 €. Elige un procesador y, si quieres, añade extras. Precios sin IVA.</p>

    <c:if test="${not empty error}">
        <p role="alert"><c:out value="${error}"/></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/configurar" method="post" accept-charset="UTF-8">
        <p>
            <label for="nombre">Nombre del equipo:</label>
            <input type="text" id="nombre" name="nombre" value="<c:out value='${nombre}'/>">
        </p>
        <fieldset>
            <legend>Procesador</legend>
            <label><input type="radio" name="procesador" value="BASICO"> Básico (+100 €)</label>
            <label><input type="radio" name="procesador" value="POTENTE"> Potente (+200 €)</label>
        </fieldset>
        <fieldset>
            <legend>Extras opcionales</legend>
            <label><input type="checkbox" name="extras" value="RAM"> Memoria adicional (+50 €)</label>
            <label><input type="checkbox" name="extras" value="SSD"> Disco adicional (+80 €)</label>
            <label><input type="checkbox" name="extras" value="WIFI"> Conexión inalámbrica (+20 €)</label>
        </fieldset>
        <p><button type="submit">Calcular precio</button></p>
    </form>
</body>
</html>
