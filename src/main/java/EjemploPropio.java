import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Ejemplo propio del taller: una cafetería con 3 meseros que atienden
 * 3 mesas al mismo tiempo usando ExecutorService + Callable + Future.
 */
public class EjemploPropio {

    /**
     * Crea una tarea (Callable) que simula a un mesero atendiendo una mesa.
     * Callable se usa porque la tarea debe DEVOLVER un resultado al terminar.
     */
    private static Callable<String> crearTareaMesero(final int numeroMesa, final int segundosAtencion) {
        return () -> {
            // Nombre del hilo del pool (pool-1-thread-1, etc.) — útil para ver paralelismo
            String nombreHilo = Thread.currentThread().getName();

            System.out.println("[INICIO] Mesa " + numeroMesa + " — mesero (" + nombreHilo + ") comienza a atender.");

            try {
                // Simula el tiempo que tarda tomar pedido, cocina, cobrar, etc.
                Thread.sleep(segundosAtencion * 1000L);
            } catch (InterruptedException e) {
                // Buena práctica: restaurar la bandera de interrupción del hilo
                Thread.currentThread().interrupt();
                throw new RuntimeException("Atención de mesa " + numeroMesa + " interrumpida", e);
            }

            System.out.println("[FIN]    Mesa " + numeroMesa + " — mesero (" + nombreHilo + ") terminó.");

            // Valor que recuperaremos después con Future.get()
            return "Mesa " + numeroMesa + " atendida en " + segundosAtencion + " s";
        };
    }

    public static void main(String[] args) {
        System.out.println("=== Cafetería: atención de mesas en paralelo ===\n");

        // Lista de 3 tareas: una por mesa (mínimo 3 hilos en paralelo)
        List<Callable<String>> tareas = new ArrayList<>();
        tareas.add(crearTareaMesero(1, 3)); // Mesa 1: 3 segundos
        tareas.add(crearTareaMesero(2, 2)); // Mesa 2: 2 segundos
        tareas.add(crearTareaMesero(3, 4)); // Mesa 3: 4 segundos

        // Pool de exactamente 3 hilos = 3 meseros trabajando a la vez
        ExecutorService poolMeseros = Executors.newFixedThreadPool(3);

        // Marca de tiempo para comparar paralelo vs secuencial al final
        long inicio = System.currentTimeMillis();

        try {
            // Envía TODAS las tareas al pool y espera a que terminen (bloquea el main)
            List<Future<String>> resultados = poolMeseros.invokeAll(tareas);

            System.out.println("\n--- Resultados de cada mesa ---");
            for (Future<String> futuro : resultados) {
                try {
                    // get() obtiene el String que devolvió cada Callable
                    System.out.println("  • " + futuro.get());
                } catch (ExecutionException e) {
                    // La tarea falló dentro del hilo (por ejemplo, interrupción)
                    System.err.println("  • Error en una mesa: " + e.getCause().getMessage());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("El programa principal fue interrumpido.");
            e.printStackTrace();
        } finally {
            // Siempre cerrar el pool para liberar hilos
            poolMeseros.shutdown();
        }

        long fin = System.currentTimeMillis();
        long tiempoTotalMs = fin - inicio;

        System.out.println("\n--- Resumen ---");
        System.out.println("Tiempo total (paralelo): ~" + (tiempoTotalMs / 1000) + " s");
        System.out.println("Si fuera secuencial (3+2+4): ~9 s");
        System.out.println("Los 3 meseros trabajaron a la vez gracias al pool de hilos.");
    }
}
