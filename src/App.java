import controladores.OrdenamientoControlador;
import vistas.OrdenamientoVista;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OrdenamientoVista vista = new OrdenamientoVista();
            new OrdenamientoControlador(vista);
            vista.setVisible(true);
        });
    }
}