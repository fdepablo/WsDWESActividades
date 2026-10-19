<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pizzeria</title>
</head>
<body>
<main>
    <h1>Pedido de la pizza</h1>
    <p>Completa los datos. Los campos marcados con * son obligatorios.</p>

    <form action="${pageContext.request.contextPath}/pizzeria/pedido" method="post">
        <p>
            <label for="nombre">Nombre *</label><br>
            <input id="nombre" name="nombre" type="text">
        </p>

        <p>
            <label for="direccion">Dirección *</label><br>
            <input id="direccion" name="direccion" type="text">
        </p>
        <p>
            <label for="telefono">Telefono *</label><br>
            <input id="telefono" name="telefono" type="text">
        </p>
        <p>
            <label for="email">Correo electrónico *</label><br>
            <input id="email" name="email" type="text">
        </p>

        <fieldset>
            <legend>Tipo de cuenta *</legend>
            <label><input type="radio" name="tamanio" value="PEQUENIA" required> Pequeña (5€) </label><br>
            <label><input type="radio" name="tamanio" value="MEDIANA"> Mediana (10€) </label>
            <label><input type="radio" name="tamanio" value="GRANDE"> Grande (15€) </label>
        </fieldset>

        <fieldset>
            <legend>Ingredientes (puedes marcar varios) *</legend>
            <label><input type="checkbox" name="ingredientes" value="QUESO"> Queso (1€) </label><br>
            <label><input type="checkbox" name="ingredientes" value="JAMON"> Jamon (2€) </label><br>
            <label><input type="checkbox" name="ingredientes" value="CHAMPINIONES"> Champiñones (3€)</label>
            <label><input type="checkbox" name="ingredientes" value="ACEITUNAS"> Aceitunas (4€)</label>
        </fieldset>

        <p><button type="submit">Perdir Pizza :)</button></p>
    </form>
</main>
</body>
</html>
