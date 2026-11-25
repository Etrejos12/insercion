package servicios;

import javax.swing.*;
import java.io.*;

public class ArchivoServicio {
    public static String elegirArchivo() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(new JFrame()) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            return f.getAbsolutePath();
        }
        return "";
    }

    public static BufferedReader abrirArchivo(String nombreArchivo) {
        File f = new File(nombreArchivo);
        if (!f.exists()) return null;
        try {
            return new BufferedReader(new FileReader(f));
        } catch (IOException e) {
            return null;
        }
    }

    public static boolean guardarArchivo(String nombreArchivo, String[] lineas) {
        if (lineas == null) return false;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (String linea : lineas) {
                bw.write(linea);
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}