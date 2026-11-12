<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pizzería</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<main>
    <h1>Pedido de la pizza</h1>
    <p>Completa los datos. Los campos marcados con * son obligatorios.</p>

    <p>Los precios de tamaños e ingredientes no incluyen el IVA del 21 %, que se añadirá al total.</p>

    <form accept-charset="UTF-8" action="${pageContext.request.contextPath}/pedido" method="post">
        <p>
            <label for="nombre">Nombre *</label><br>
            <input id="nombre" name="nombre" type="text">
        </p>

        <p>
            <label for="direccion">Dirección *</label><br>
            <input id="direccion" name="direccion" type="text">
        </p>
        <p>
            <label for="telefono">Teléfono *</label><br>
            <input id="telefono" name="telefono" type="text">
        </p>
        <p>
            <label for="email">Correo electrónico *</label><br>
            <input id="email" name="email" type="text">
        </p>

        <fieldset>
            <legend>Tamaño de la pizza *</legend>
            <label><input type="radio" name="tamano" value="pequena"> Pequeña (5€) </label><br>
            <label><input type="radio" name="tamano" value="mediana"> Mediana (10€) </label>
            <label><input type="radio" name="tamano" value="grande"> Grande (15€) </label>
        </fieldset>

        <fieldset>
            <legend>Ingredientes (puedes marcar varios) *</legend>
            <label><input type="checkbox" name="ingredientes" value="queso"> Queso (1€) </label><br>
            <label><input type="checkbox" name="ingredientes" value="jamon"> Jamón (2€) </label><br>
            <label><input type="checkbox" name="ingredientes" value="champinones"> Champiñones (3€)</label>
            <label><input type="checkbox" name="ingredientes" value="aceitunas"> Aceitunas (4€)</label>
        </fieldset>

        <p><button type="submit">Pedir pizza</button></p>
    </form>
</main>
</body>
</html>
