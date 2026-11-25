package servicios;

import modelos.Documento;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Carga los documentos del CSV y ofrece cuatro algoritmos de ordenamiento.
 * El criterio corresponde al indice de la columna seleccionada en la vista.
 */
public class DocumentoServicio {
    private static final List<Documento> documentos = new ArrayList<>();
    private static String[] encabezados = new String[0];

    public static void cargarDatos(String nombreArchivo) {
        documentos.clear();
        encabezados = new String[0];

        try (BufferedReader lector = Files.newBufferedReader(Path.of(nombreArchivo), StandardCharsets.UTF_8)) {
            String linea = lector.readLine();
            if (linea == null) return;

            encabezados = separarCSV(linea);
            while ((linea = lector.readLine()) != null) {
                if (!linea.isBlank()) {
                    String[] valores = separarCSV(linea);
                    String[] fila = new String[encabezados.length];
                    for (int i = 0; i < fila.length; i++) {
                        fila[i] = i < valores.length ? valores[i] : "";
                    }
                    documentos.add(new Documento(fila));
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("No fue posible cargar el archivo: " + nombreArchivo, e);
        }
    }

    private static String[] separarCSV(String linea) {
        List<String> campos = new ArrayList<>();
        StringBuilder campo = new StringBuilder();
        boolean entreComillas = false;

        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);
            if (c == '"') {
                if (entreComillas && i + 1 < linea.length() && linea.charAt(i + 1) == '"') {
                    campo.append('"');
                    i++;
                } else {
                    entreComillas = !entreComillas;
                }
            } else if (c == ',' && !entreComillas) {
                campos.add(campo.toString().trim());
                campo.setLength(0);
            } else {
                campo.append(c);
            }
        }
        campos.add(campo.toString().trim());
        return campos.toArray(new String[0]);
    }

    public static String[] getEncabezados() {
        return encabezados.clone();
    }

    public static String[][] getDatos() {
        String[][] datos = new String[documentos.size()][encabezados.length];
        for (int i = 0; i < documentos.size(); i++) {
            datos[i] = documentos.get(i).getValores();
        }
        return datos;
    }

    private static boolean esMayor(Documento d1, Documento d2, int criterio) {
        String a = d1.getValor(criterio).trim();
        String b = d2.getValor(criterio).trim();
        try {
            return Double.parseDouble(a.replace(",", ".")) > Double.parseDouble(b.replace(",", "."));
        } catch (NumberFormatException ignored) {
            return a.compareToIgnoreCase(b) > 0;
        }
    }

    public static void ordenarBurbuja(int criterio) {
        for (int i = 0; i < documentos.size() - 1; i++) {
            for (int j = 0; j < documentos.size() - 1 - i; j++) {
                if (esMayor(documentos.get(j), documentos.get(j + 1), criterio)) {
                    Documento temporal = documentos.get(j);
                    documentos.set(j, documentos.get(j + 1));
                    documentos.set(j + 1, temporal);
                }
            }
        }
    }

    public static void ordenarRapido(int criterio) {
        if (documentos.size() > 1) {
            rapido(0, documentos.size() - 1, criterio);
        }
    }

    private static void rapido(int inicio, int fin, int criterio) {
        int i = inicio;
        int j = fin;
        Documento pivote = documentos.get((inicio + fin) / 2);

        while (i <= j) {
            while (esMenor(documentos.get(i), pivote, criterio)) i++;
            while (esMayor(documentos.get(j), pivote, criterio)) j--;
            if (i <= j) {
                Documento temporal = documentos.get(i);
                documentos.set(i, documentos.get(j));
                documentos.set(j, temporal);
                i++;
                j--;
            }
        }
        if (inicio < j) rapido(inicio, j, criterio);
        if (i < fin) rapido(i, fin, criterio);
    }

    private static boolean esMenor(Documento d1, Documento d2, int criterio) {
        return !esMayor(d1, d2, criterio) && !sonIguales(d1, d2, criterio);
    }

    private static boolean sonIguales(Documento d1, Documento d2, int criterio) {
        return d1.getValor(criterio).trim().compareToIgnoreCase(d2.getValor(criterio).trim()) == 0
                || (!esMayor(d1, d2, criterio) && !esMayor(d2, d1, criterio));
    }

    /********** Inserción **********/
    public static void ordenarInsercion(int criterio) {
        for (int i = 1; i < documentos.size(); i++) {
            Documento clave = documentos.get(i);
            int j = i - 1;
            // desplaza hacia la derecha los elementos mayores que la clave
            while (j >= 0 && esMayor(documentos.get(j), clave, criterio)) {
                documentos.set(j + 1, documentos.get(j));
                j--;
            }
            documentos.set(j + 1, clave);
        }
    }

    /********** Mezcla (Merge Sort) **********/
    public static void ordenarMezcla(int criterio) {
        if (documentos.size() < 2)
            return;
        Documento[] arr = documentos.toArray(new Documento[0]);
        Documento[] aux = new Documento[arr.length];
        mezclaRec(arr, aux, 0, arr.length - 1, criterio);
        // copia el resultado de vuelta a la lista
        for (int i = 0; i < arr.length; i++) {
            documentos.set(i, arr[i]);
        }
    }

    private static void mezclaRec(Documento[] arr, Documento[] aux, int izq, int der, int criterio) {
        if (izq >= der)
            return;
        int medio = (izq + der) / 2;
        mezclaRec(arr, aux, izq, medio, criterio);
        mezclaRec(arr, aux, medio + 1, der, criterio);
        merge(arr, aux, izq, medio, der, criterio);
    }

    private static void merge(Documento[] arr, Documento[] aux, int izq, int medio, int der, int criterio) {
        for (int k = izq; k <= der; k++) {
            aux[k] = arr[k];
        }
        int i = izq, j = medio + 1, k = izq;
        while (i <= medio && j <= der) {
            // si no es mayor, se toma primero el de la izquierda (mantiene la estabilidad)
            if (!esMayor(aux[i], aux[j], criterio))
                arr[k++] = aux[i++];
            else
                arr[k++] = aux[j++];
        }
        while (i <= medio)
            arr[k++] = aux[i++];
        while (j <= der)
            arr[k++] = aux[j++];
    }
}
