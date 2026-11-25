package controladores;

import servicios.CronometroServicio;
import servicios.DocumentoServicio;
import vistas.OrdenamientoVista;
import javax.swing.SwingUtilities;
import java.util.function.IntFunction;

public class OrdenamientoControlador {
    private final OrdenamientoVista vista;
    private volatile boolean ejecutando;

    public OrdenamientoControlador(OrdenamientoVista vista) {
        this.vista = vista;
        this.vista.setOrdenarBurbuja(evento -> ordenarBurbuja());
        this.vista.setOrdenarRapido(evento -> ordenarRapido());
        this.vista.setOrdenarInsercion(evento -> ordenarInsercion());
        this.vista.setOrdenarSeleccion(evento -> ordenarSeleccion());
        this.vista.setOrdenarMezcla(evento -> ordenarMezcla());

        String rutaArchivo = System.getProperty("user.dir") + "/src/datos/Datos.csv";
        DocumentoServicio.cargar(rutaArchivo);
        mostrar();
    }

    private void mostrar() {
        vista.mostrarDatos(DocumentoServicio.getEncabezados(), DocumentoServicio.getDatosDocumentos());
    }

    private void ordenarBurbuja() { ejecutarProceso(DocumentoServicio::ordenarBurbuja); }
    private void ordenarRapido() { ejecutarProceso(DocumentoServicio::ordenarRapido); }
    private void ordenarInsercion() { ejecutarProceso(DocumentoServicio::ordenarInsercion); }
    private void ordenarSeleccion() { ejecutarProceso(DocumentoServicio::ordenarSeleccion); }
    private void ordenarMezcla() { ejecutarProceso(DocumentoServicio::ordenarMezcla); }

    private void ejecutarProceso(IntFunction<String> algoritmo) {
        int criterio = vista.getCriterio();
        if (criterio < 0 || ejecutando) return;

        ejecutando = true;
        CronometroServicio.iniciarCronometro();

        Thread hiloOrdenamiento = new Thread(() -> {
            try {
                String tiempo = algoritmo.apply(criterio);
                SwingUtilities.invokeLater(() -> {
                    mostrar();
                    vista.mostrarTiempo(tiempo);
                });
            } finally {
                ejecutando = false;
            }
        }, "hilo-ordenamiento");
        hiloOrdenamiento.start();

        Thread hiloCronometro = new Thread(() -> {
            while (ejecutando) {
                vista.mostrarTiempo(CronometroServicio.getTextoTiempoCronometro());
                CronometroServicio.pausarMilisegundos(50);
            }
        }, "hilo-cronometro");
        hiloCronometro.setDaemon(true);
        hiloCronometro.start();
    }
}