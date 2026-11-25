package servicios;

/** Cronometro sencillo basado en nanosegundos. */
public class CronometroServicio {
    private static volatile long inicio;
    private static volatile long tiempoFinal;
    private static volatile boolean activo;

    public static void iniciarCronometro() {
        inicio = System.nanoTime();
        tiempoFinal = 0;
        activo = true;
    }

    public static void detenerCronometro() {
        if (activo) {
            tiempoFinal = System.nanoTime() - inicio;
            activo = false;
        }
    }

    public static void pausarMilisegundos(long milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static String getTextoTiempoCronometro() {
        long nanos = activo ? System.nanoTime() - inicio : tiempoFinal;
        return String.format("%.3f ms", nanos / 1_000_000.0);
    }
}
