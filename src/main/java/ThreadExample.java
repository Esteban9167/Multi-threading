/**
 * Ejemplo 1: crear un hilo con la clase Thread.
 * Imprime un contador del 0 al 4 con pausa de 1 segundo entre cada número.
 */
public class ThreadExample {

    public static void main(String[] args) {
        // Se crea un Thread pasando un Runnable (aquí una lambda con la tarea del hilo)
        Thread thread = new Thread(() -> {
            // El bucle va de 0 a 4 (i < 5 significa 5 iteraciones)
            for (int i = 0; i < 5; i++) {
                System.out.println("Thread: " + i);
                try {
                    // Pausa el hilo 1000 ms = 1 segundo (simula trabajo o espera)
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // Si otro hilo interrumpe este, se captura la excepción
                    e.printStackTrace();
                }
            }
        });

        // start() INICIA el hilo en paralelo; run() NO lo haría en un hilo nuevo
        thread.start();
    }
}
