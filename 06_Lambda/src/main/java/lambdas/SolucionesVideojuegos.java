package lambdas;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/** Resuelve los doce ejercicios del catálogo mediante funciones lambda y operaciones sobre streams. */
public class SolucionesVideojuegos {

    /** Ficha inmutable que permite crear catálogos rebajados conservando el original. */
    record Videojuego(String nombre, double precio, double valoracion, String genero) {}

    /** Muestra en consola los resultados de los doce apartados con el catálogo del enunciado. */
    public static void main(String[] args) {
        List<Videojuego> videojuegos = List.of(
            new Videojuego("Minecraft", 30, 9, "Sandbox"),
            new Videojuego("Hollow Knight", 15, 9.5, "Metroidvania"),
            new Videojuego("Stardew Valley", 14, 9, "Simulación"),
            new Videojuego("EA Sports FC 24", 40, 7, "Deportes"),
            new Videojuego("Elden Ring", 60, 9.5, "Rol"),
            new Videojuego("Terraria", 10, 8.5, "Sandbox")
        );


        // 1. Presentar un videojuego con su nombre, precio y valoración. Probar con dos juegos.
        // Las funciones lambda necesitan una interfaz funcional, depende el enunciado necesitaremos
        // una apropiada.
        Function<Videojuego, String> presentar = juego ->
            juego.nombre() + " cuesta " + juego.precio()
                + " € y tiene una valoración de " + juego.valoracion()
                + " sobre 10";

        System.out.println(presentar.apply(videojuegos.get(0)));
        System.out.println(presentar.apply(videojuegos.get(1)));


        // 2.a. Predicate recibe el juego y captura un presupuesto definido fuera de la lambda.
        double presupuestoDisponible = 30.0;
        Predicate<Videojuego> puedeComprarConPresupuesto = juego ->
            juego.precio() <= presupuestoDisponible;

        // El presupuesto capturado debe ser final o efectivamente final: no se reasigna.
        System.out.println(puedeComprarConPresupuesto.test(videojuegos.get(0)));
        System.out.println(puedeComprarConPresupuesto.test(videojuegos.get(4)));

        // 2.b. BiPredicate recibe el juego y el presupuesto en cada llamada.
        BiPredicate<Videojuego, Double> puedeComprar = (juego, presupuesto) ->
            juego.precio() <= presupuesto;

        System.out.println(puedeComprar.test(videojuegos.get(0), 35.0));
        System.out.println(puedeComprar.test(videojuegos.get(0), 20.0));
        System.out.println(puedeComprar.test(videojuegos.get(0), 30.0));


        // 3. Elegir el juego mejor valorado entre dos. Indicar si hay empate.
        BiFunction<Videojuego, Videojuego, String> elegirMejor = (juegoA, juegoB) -> {
            if (juegoA.valoracion() == juegoB.valoracion()) {
                return "Tienen la misma valoración";
            }
            return juegoA.valoracion() > juegoB.valoracion()
                ? juegoA.nombre()
                : juegoB.nombre();
        };

        System.out.println(elegirMejor.apply(videojuegos.get(0), videojuegos.get(1)));
        System.out.println(elegirMejor.apply(videojuegos.get(0), videojuegos.get(2)));


        // 4.a. collect(Collectors.toList()) recoge los nombres en el orden del catálogo.
        // Su contrato no garantiza que la lista sea modificable ni que sea un ArrayList.
        List<String> nombresConCollect = videojuegos.stream()
            .map(juego -> juego.nombre())
            .collect(Collectors.toList());

        System.out.println(nombresConCollect);

        // 4.b. toList() crea una lista no modificable con los mismos nombres.
        List<String> nombres = videojuegos.stream()
            .map(juego -> juego.nombre())
            .toList();

        System.out.println(nombres);

        // add() no está permitido. Capturamos la excepción para continuar con los ejercicios.
        try {
            nombres.add("Otro juego");
        } catch (UnsupportedOperationException e) {
            System.out.println("La lista creada con toList() no permite añadir elementos.");
        }


        // 5. Obtener las fichas completas de los juegos del género Sandbox.
        List<Videojuego> juegosSandbox = videojuegos.stream()
            .filter(juego -> juego.genero().equals("Sandbox"))
            .collect(Collectors.toList());

        System.out.println(juegosSandbox);


        // 6.a. Un solo filtro combina las dos condiciones mediante &&.
        List<Videojuego> buenasCompras = videojuegos.stream()
            .filter(juego -> juego.precio() <= 20 && juego.valoracion() >= 9)
            .toList();

        System.out.println(buenasCompras);

        // 6.b. Dos filtros consecutivos aplican las mismas condiciones por separado.
        List<Videojuego> buenasComprasConDosFiltros = videojuegos.stream()
            .filter(juego -> juego.precio() <= 20)
            .filter(juego -> juego.valoracion() >= 9)
            .toList();

        System.out.println(buenasComprasConDosFiltros);


        // 7. Mostrar los juegos numerados desde 1, con su nombre y precio.
        IntStream.range(0, videojuegos.size()).forEach(indice -> {
            Videojuego juego = videojuegos.get(indice);
            System.out.println((indice + 1) + ". " + juego.nombre()
                + " — " + juego.precio() + " €");
        });


        // 8. Crear un catálogo con un descuento del 25 %, conservando los demás datos.
        List<Videojuego> catalogoRebajado = videojuegos.stream()
            .map(juego -> new Videojuego(
                juego.nombre(),
                juego.precio() * 0.75,
                juego.valoracion(),
                juego.genero()
            ))
            .collect(Collectors.toList());

        System.out.println(catalogoRebajado);


        // 9. Calcular el coste del catálogo.
        // 9.a. Con una lambda y sum().
        double total = videojuegos.stream()
            .mapToDouble(juego -> juego.precio())
            .sum();

        System.out.println("Comprar todo el catálogo cuesta " + total + " €");

        // 9.b. Acumular los precios con un bucle for, sin funciones lambda.
        double totalConBucle = 0;
        for (Videojuego juego : videojuegos) {
            totalConBucle += juego.precio();
        }

        System.out.println("Comprar todo el catálogo cuesta " + totalConBucle + " €");


        // 10. Obtener los nombres de los juegos que cuestan entre 15 y 40 €, ambos incluidos.
        List<String> nombresEnIntervalo = videojuegos.stream()
            .filter(juego -> juego.precio() >= 15 && juego.precio() <= 40)
            .map(juego -> juego.nombre())
            .collect(Collectors.toList());

        System.out.println(nombresEnIntervalo);


        // 11. Buscar juegos por presupuesto y devolver mensajes con su nombre y precio.
        // 11.a. Function recibe el presupuesto y utiliza el catálogo videojuegos del entorno.
        Function<Double, List<String>> buscarEnVideojuegos = presupuesto -> videojuegos.stream()
            .filter(juego -> juego.precio() <= presupuesto)
            .map(juego -> juego.nombre() + ": " + juego.precio() + " €")
            .toList();

        System.out.println("Con 15 €:");
        System.out.println(buscarEnVideojuegos.apply(15.0));

        System.out.println("Con 5 €:");
        System.out.println(buscarEnVideojuegos.apply(5.0));

        // 11.b. BiFunction recibe el catálogo y el presupuesto en cada llamada.
        BiFunction<List<Videojuego>, Double, List<String>> buscarPorPresupuesto =
            (catalogo, presupuesto) -> catalogo.stream()
                .filter(juego -> juego.precio() <= presupuesto)
                .map(juego -> juego.nombre() + ": " + juego.precio() + " €")
                .toList();

        System.out.println("Con 15 €:");
        System.out.println(buscarPorPresupuesto.apply(videojuegos, 15.0));

        System.out.println("Con 5 €:");
        System.out.println(buscarPorPresupuesto.apply(videojuegos, 5.0));


        // 12. Mostrar juegos con valoración de al menos 9 que cuesten hasta 15 € tras rebajarlos un 10 %.
        videojuegos.stream()
            .filter(juego -> juego.valoracion() >= 9)
            .map(juego -> new Videojuego(
                juego.nombre(),
                juego.precio() * 0.9,
                juego.valoracion(),
                juego.genero()
            ))
            .filter(juego -> juego.precio() <= 15)
            .forEach(juego -> System.out.println(
                juego.nombre() + ": " + juego.precio() + " €"
            ));
    }
}
