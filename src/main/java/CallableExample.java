import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Ejemplo 5: Callable y Future.
 * Una tarea calcula una suma y devuelve el resultado de forma asíncrona.
 */
public class CallableExample {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Callable<V> es como Runnable pero RETORNA un valor (aquí Integer)
        Callable<Integer> task = () -> {
            int sum = 0;
            for (int i = 0; i < 5; i++) {
                sum += i;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return sum; // 0+1+2+3+4 = 10
        };

        // submit devuelve un Future para leer el resultado más tarde
        Future<Integer> future = executor.submit(task);

        try {
            // get() bloquea hasta que la tarea termina y devuelve el Integer
            Integer result = future.get();
            System.out.println("Sum: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdown();
    }
}
