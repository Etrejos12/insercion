package vistas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.net.URL;

public class OrdenamientoVista extends JFrame {
    private JButton btnOrdenarBurbuja;
    private JButton btnOrdenarRapido;
    private JButton btnOrdenarInsercion;
    private JButton btnOrdenarSeleccion;
    private JButton btnOrdenarMezcla;
    private JToolBar tbOrdenamiento;
    private JComboBox<String> cmbCriterio;
    private JTextField txtTiempo;
    private JButton btnBuscar;
    private JTextField txtBuscar;
    private JTable tblDocumentos;

    public OrdenamientoVista() {
        tbOrdenamiento = new JToolBar();
        btnOrdenarBurbuja = crearBoton("Ordenar.png", "Ordenar Burbuja", "Burbuja");
        btnOrdenarRapido = crearBoton("OrdenarRapido.png", "Ordenar Rápido", "Rápido");
        btnOrdenarInsercion = crearBoton("OrdenarInsercion.png", "Ordenar Inserción", "Inserción");
        btnOrdenarSeleccion = new JButton("Selección");
        btnOrdenarSeleccion.setToolTipText("Ordenar por selección directa");
        btnOrdenarMezcla = crearBoton("OrdenarMezcla.png", "Ordenar Mezcla", "Mezcla");
        cmbCriterio = new JComboBox<>(new String[]{
                "Nombre Completo, Tipo de Documento",
                "Tipo de Documento, Nombre Completo"
        });
        txtTiempo = new JTextField("00:00:00.000", 12);
        txtTiempo.setEditable(false);
        btnBuscar = crearBoton("Buscar.png", "Buscar", "Buscar");
        txtBuscar = new JTextField(10);
        tblDocumentos = new JTable();

        tbOrdenamiento.add(btnOrdenarBurbuja);
        tbOrdenamiento.add(btnOrdenarRapido);
        tbOrdenamiento.add(btnOrdenarInsercion);
        tbOrdenamiento.add(btnOrdenarSeleccion);
        tbOrdenamiento.add(btnOrdenarMezcla);
        tbOrdenamiento.add(cmbCriterio);
        tbOrdenamiento.add(txtTiempo);
        tbOrdenamiento.add(btnBuscar);
        tbOrdenamiento.add(txtBuscar);

        setLayout(new BorderLayout());
        setSize(950, 600);
        setTitle("Ordenamiento de Documentos");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(tbOrdenamiento, BorderLayout.NORTH);
        add(new JScrollPane(tblDocumentos), BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }

    private JButton crearBoton(String recurso, String ayuda, String texto) {
        JButton boton = new JButton(texto);
        boton.setToolTipText(ayuda);
        URL url = getClass().getResource("/iconos/" + recurso);
        if (url != null) {
            boton.setIcon(new ImageIcon(url));
            boton.setText("");
        }
        return boton;
    }

    public int getCriterio() { return cmbCriterio.getSelectedIndex(); }
    public void setOrdenarBurbuja(ActionListener e) { btnOrdenarBurbuja.addActionListener(e); }
    public void setOrdenarRapido(ActionListener e) { btnOrdenarRapido.addActionListener(e); }
    public void setOrdenarInsercion(ActionListener e) { btnOrdenarInsercion.addActionListener(e); }
    public void setOrdenarSeleccion(ActionListener e) { btnOrdenarSeleccion.addActionListener(e); }
    public void setOrdenarMezcla(ActionListener e) { btnOrdenarMezcla.addActionListener(e); }
    public void setBuscar(ActionListener e) { btnBuscar.addActionListener(e); }

    public void mostrarTiempo(String tiempo) {
        SwingUtilities.invokeLater(() -> txtTiempo.setText(tiempo));
    }

    public void mostrarDatos(String[] encabezados, String[][] datos) {
        SwingUtilities.invokeLater(() -> {
            DefaultTableModel modelo = new DefaultTableModel(datos, encabezados) {
                @Override public boolean isCellEditable(int fila, int columna) { return false; }
            };
            tblDocumentos.setModel(modelo);
        });
    }
}