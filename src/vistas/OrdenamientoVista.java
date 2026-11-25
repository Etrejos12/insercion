package vistas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/** Interfaz Swing para seleccionar criterio y algoritmo de ordenamiento. */
public class OrdenamientoVista extends JFrame {
    private final JComboBox<String> comboCriterio = new JComboBox<>();
    private final JTable tabla = new JTable();
    private final JLabel etiquetaTiempo = new JLabel("Tiempo: 0.000 ms");
    private final JButton botonBurbuja = new JButton("Burbuja");
    private final JButton botonRapido = new JButton("Rápido");
    private final JButton botonInsercion = new JButton("Inserción");
    private final JButton botonMezcla = new JButton("Mezcla");

    public OrdenamientoVista() {
        super("Comparación de algoritmos de ordenamiento");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JPanel superior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        superior.add(new JLabel("Ordenar por:"));
        superior.add(comboCriterio);
        superior.add(botonBurbuja);
        superior.add(botonRapido);
        superior.add(botonInsercion);
        superior.add(botonMezcla);
        add(superior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        JPanel inferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        inferior.add(etiquetaTiempo);
        add(inferior, BorderLayout.SOUTH);

        setSize(900, 550);
        setLocationRelativeTo(null);
    }

    public void setOrdenarBurbuja(ActionListener listener) { botonBurbuja.addActionListener(listener); }
    public void setOrdenarRapido(ActionListener listener) { botonRapido.addActionListener(listener); }
    public void setOrdenarInsercion(ActionListener listener) { botonInsercion.addActionListener(listener); }
    public void setOrdenarMezcla(ActionListener listener) { botonMezcla.addActionListener(listener); }

    public int getCriterio() { return comboCriterio.getSelectedIndex(); }

    public void setTiempo(String tiempo) {
        SwingUtilities.invokeLater(() -> etiquetaTiempo.setText("Tiempo: " + tiempo));
    }

    public void mostrarDatos(String[] encabezados, String[][] datos) {
        SwingUtilities.invokeLater(() -> {
            comboCriterio.removeAllItems();
            for (String encabezado : encabezados) comboCriterio.addItem(encabezado);
            tabla.setModel(new DefaultTableModel(datos, encabezados) {
                @Override public boolean isCellEditable(int row, int column) { return false; }
            });
        });
    }
}
