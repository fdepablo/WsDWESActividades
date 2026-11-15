<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Formulario de la pizzería</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<main>
    <h1>Tu compra de pizzas</h1>
    <p>Introduce tus datos y elige una pizza. Al volver, aparecerán los datos del cliente de la sesión.</p>
    <p><a href="${pageContext.request.contextPath}/pedido">Ver pedido</a></p>

    <form action="${pageContext.request.contextPath}/formulario" method="post" accept-charset="UTF-8">
        <p>
            <label for="nombre">Nombre</label><br>
            <input id="nombre" name="nombre" type="text" value="<c:out value='${sessionScope.cliente.nombre}'/>">
        </p>
        <p>
            <label for="direccion">Dirección</label><br>
            <input id="direccion" name="direccion" type="text" value="<c:out value='${sessionScope.cliente.direccion}'/>">
        </p>
        <p>
            <label for="telefono">Teléfono</label><br>
            <input id="telefono" name="telefono" type="text" value="<c:out value='${sessionScope.cliente.telefono}'/>">
        </p>
        <p>
            <label for="email">Correo electrónico</label><br>
            <input id="email" name="email" type="text" value="<c:out value='${sessionScope.cliente.email}'/>">
        </p>

        <fieldset>
            <legend>Tamaño, precio sin IVA</legend>
            <label><input type="radio" name="tamano" value="pequena"> Pequeña, 5 €</label><br>
            <label><input type="radio" name="tamano" value="mediana"> Mediana, 10 €</label><br>
            <label><input type="radio" name="tamano" value="grande"> Grande, 15 €</label>
        </fieldset>
        <fieldset>
            <legend>Ingredientes, 1 € cada uno sin IVA</legend>
            <label><input type="checkbox" name="ingredientes" value="queso"> Queso</label><br>
            <label><input type="checkbox" name="ingredientes" value="jamon"> Jamón</label><br>
            <label><input type="checkbox" name="ingredientes" value="champinones"> Champiñones</label><br>
            <label><input type="checkbox" name="ingredientes" value="aceitunas"> Aceitunas</label>
        </fieldset>
        <p>Al precio se le añade un 21 % de IVA.</p>
        <button type="submit">Añadir pizza</button>
    </form>
</main>
</body>
</html>