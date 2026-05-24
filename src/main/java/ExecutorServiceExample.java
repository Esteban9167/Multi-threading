import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Ejemplo 4: ExecutorService con pool de 2 hilos.
 * Dos tareas Runnable se ejecutan en paralelo en el pool.
 */
public class ExecutorServiceExample {

    public static void main(String[] args) {
        // Pool fijo de 2 hilos reutilizables (no crear Thread a mano)
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable task1 = () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Task 1: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable task2 = () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Task 2: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        // submit envía cada tarea al pool; el pool asigna un hilo libre
        executor.submit(task1);
        executor.submit(task2);

        // shutdown: no acepta tareas nuevas; termina cuando las actuales acaban
        executor.shutdown();
    }
}
