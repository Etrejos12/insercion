package controladores;

import servicios.CronometroServicio;
import servicios.DocumentoServicio;
import vistas.OrdenamientoVista;
import javax.swing.SwingUtilities;

public class OrdenamientoControlador {
    private final OrdenamientoVista vista;
    private volatile boolean ejecutando;

    public OrdenamientoControlador(OrdenamientoVista vista) {
        this.vista = vista;

        this.vista.setOrdenarBurbuja(evento -> ordenarBurbuja());
        this.vista.setOrdenarRapido(evento -> ordenarRapido());
        this.vista.setOrdenarInsercion(evento -> ordenarInsercion());
        this.vista.setOrdenarMezcla(evento -> ordenarMezcla());

        String nombreArchivo = System.getProperty("user.dir") + "/src/datos/Datos.csv";
        DocumentoServicio.cargarDatos(nombreArchivo);
        mostrar();
    }

    private void mostrar() {
        vista.mostrarDatos(DocumentoServicio.getEncabezados(), DocumentoServicio.getDatos());
    }

    private void ordenarBurbuja() {
        if (vista.getCriterio() < 0) return;
        ejecutarProceso(() -> DocumentoServicio.ordenarBurbuja(vista.getCriterio()));
    }

    private void ordenarRapido() {
        if (vista.getCriterio() < 0) return;
        ejecutarProceso(() -> DocumentoServicio.ordenarRapido(vista.getCriterio()));
    }

    private void ordenarInsercion() {
        if (vista.getCriterio() < 0) return;
        ejecutarProceso(() -> DocumentoServicio.ordenarInsercion(vista.getCriterio()));
    }

    private void ordenarMezcla() {
        if (vista.getCriterio() < 0) return;
        ejecutarProceso(() -> DocumentoServicio.ordenarMezcla(vista.getCriterio()));
    }

    // Ejecuta el ordenamiento en un hilo y actualiza el cronometro en otro.
    private void ejecutarProceso(Runnable accion) {
        if (ejecutando) return;
        ejecutando = true;
        CronometroServicio.iniciarCronometro();

        Thread hiloOrdenamiento = new Thread(() -> {
            try {
                accion.run();
                mostrar();
            } finally {
                CronometroServicio.detenerCronometro();
                ejecutando = false;
                SwingUtilities.invokeLater(() ->
                    vista.setTiempo(CronometroServicio.getTextoTiempoCronometro()));
            }
        }, "hilo-ordenamiento");
        hiloOrdenamiento.start();

        Thread hiloCronometro = new Thread(() -> {
            while (ejecutando) {
                CronometroServicio.pausarMilisegundos(50);
                vista.setTiempo(CronometroServicio.getTextoTiempoCronometro());
            }
        }, "hilo-cronometro");
        hiloCronometro.start();
    }
}
