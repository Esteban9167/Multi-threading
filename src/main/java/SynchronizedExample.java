/**
 * Ejemplo 3: sincronización con synchronized.
 * Dos hilos incrementan un contador compartido 1000 veces cada uno.
 */
public class SynchronizedExample {

    // Variable compartida por todos los hilos (zona crítica si no se protege)
    private static int counter = 0;

    // synchronized: solo un hilo a la vez puede ejecutar este método
    public static synchronized void increment() {
        counter++;
    }

    public static void main(String[] args) {
        // Misma tarea para ambos hilos: llamar increment() 1000 veces
        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                increment();
            }
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);

        thread1.start();
        thread2.start();

        try {
            // join(): el main espera a que cada hilo termine antes de imprimir counter
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Con synchronized debe ser 2000; sin él suele ser menor por condiciones de carrera
        System.out.println("Counter: " + counter);
    }
}
