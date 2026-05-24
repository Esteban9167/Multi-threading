/**
 * Ejemplo 2: crear un hilo usando la interfaz Runnable.
 * Separa la definición de la tarea de la creación del Thread.
 */
public class RunnableExample {

    public static void main(String[] args) {
        // Runnable: contrato "ejecutar código" sin devolver resultado
        Runnable task = () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Runnable: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        // El Thread recibe el Runnable y ejecutará task.run() en su propio hilo
        Thread thread = new Thread(task);
        thread.start();
    }
}
