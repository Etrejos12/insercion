package servicios;

import java.text.DecimalFormat;

public class CronometroServicio {
    private static volatile long inicio;
    private static volatile long tiempoFinal;
    private static volatile boolean activo;

    public static void iniciarCronometro() {
        inicio = System.currentTimeMillis();
        tiempoFinal = 0;
        activo = true;
    }

    public static void detenerCronometro() {
        if (activo) {
            tiempoFinal = System.currentTimeMillis() - inicio;
            activo = false;
        }
    }

    public static long getTiempoCronometro() {
        return activo ? System.currentTimeMillis() - inicio : tiempoFinal;
    }

    public static String getTextoTiempoCronometro() {
        long tiempo = getTiempoCronometro();
        long ms = tiempo % 1000;
        tiempo = (tiempo - ms) / 1000;
        long s = tiempo % 60;
        tiempo = (tiempo - s) / 60;
        long m = tiempo % 60;
        tiempo = (tiempo - m) / 60;
        long h = tiempo % 24;
        DecimalFormat df = new DecimalFormat("00");
        DecimalFormat dfMS = new DecimalFormat("000");
        return df.format(h) + ":" + df.format(m) + ":" + df.format(s) + "." + dfMS.format(ms);
    }

    public static void pausarMilisegundos(int milisegundos) {
        try { Thread.sleep(milisegundos); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}