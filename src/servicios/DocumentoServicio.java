package servicios;

import modelos.Documento;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DocumentoServicio {
    private static final String[] encabezados = {
            "#", "Primer Apellido", "Segundo Apellido", "Nombres", "Documento"
    };
    private static List<Documento> documentos = new ArrayList<>();

    public static String[] getEncabezados() { return encabezados.clone(); }

    public static void cargar(String nombreArchivo) {
        try (Stream<String> lineas = Files.lines(Paths.get(nombreArchivo), StandardCharsets.UTF_8)) {
            documentos = lineas.skip(1)
                    .filter(linea -> linea != null && !linea.isBlank())
                    .map(linea -> linea.split(";", -1))
                    .filter(textos -> textos.length >= 4)
                    .map(textos -> new Documento(textos[0].trim(), textos[1].trim(),
                            textos[2].trim(), textos[3].trim()))
                    .collect(Collectors.toCollection(ArrayList::new));
            System.out.println("Documentos cargados: " + documentos.size());
        } catch (Exception e) {
            documentos = new ArrayList<>();
            throw new IllegalStateException("No fue posible cargar el archivo " + nombreArchivo, e);
        }
    }

    public static String[][] getDatosDocumentos() {
        String[][] datos = new String[documentos.size()][encabezados.length];
        for (int i = 0; i < documentos.size(); i++) {
            Documento d = documentos.get(i);
            datos[i] = new String[]{String.valueOf(i + 1), d.getApellido1(),
                    d.getApellido2(), d.getNombre(), d.getDocumento()};
        }
        return datos;
    }

    private static boolean esMayor(Documento dI, Documento dJ, int criterio) {
        int comparacion;
        if (criterio == 0) {
            comparacion = dI.getNombreCompleto().compareToIgnoreCase(dJ.getNombreCompleto());
            if (comparacion == 0)
                comparacion = dI.getDocumento().compareToIgnoreCase(dJ.getDocumento());
        } else {
            comparacion = dI.getDocumento().compareToIgnoreCase(dJ.getDocumento());
            if (comparacion == 0)
                comparacion = dI.getNombreCompleto().compareToIgnoreCase(dJ.getNombreCompleto());
        }
        return comparacion > 0;
    }

    private static void intercambiar(int origen, int destino) {
        Documento temporal = documentos.get(origen);
        documentos.set(origen, documentos.get(destino));
        documentos.set(destino, temporal);
    }

    private static void iniciarMedicion() { CronometroServicio.iniciarCronometro(); }
    private static String terminarMedicion() {
        CronometroServicio.detenerCronometro();
        return CronometroServicio.getTextoTiempoCronometro();
    }

    // Ordenamiento por intercambio, conservando el recorrido del ejemplo del taller.
    public static String ordenarBurbuja(int criterio) {
        iniciarMedicion();
        for (int i = 0; i < documentos.size() - 1; i++) {
            for (int j = i + 1; j < documentos.size(); j++) {
                if (esMayor(documentos.get(i), documentos.get(j), criterio))
                    intercambiar(i, j);
            }
        }
        return terminarMedicion();
    }

    public static String ordenarRapido(int criterio) {
        iniciarMedicion();
        _ordenarRapido(0, documentos.size() - 1, criterio);
        return terminarMedicion();
    }

    private static void _ordenarRapido(int inicio, int fin, int criterio) {
        if (inicio >= fin) return;
        int posicionPivote = ubicarPivote(inicio, fin, criterio);
        _ordenarRapido(inicio, posicionPivote - 1, criterio);
        _ordenarRapido(posicionPivote + 1, fin, criterio);
    }

    private static int ubicarPivote(int inicio, int fin, int criterio) {
        Documento pivote = documentos.get(inicio);
        int posicionPivote = inicio;
        for (int i = inicio + 1; i <= fin; i++) {
            if (esMayor(pivote, documentos.get(i), criterio)) {
                posicionPivote++;
                if (i != posicionPivote) intercambiar(i, posicionPivote);
            }
        }
        if (inicio != posicionPivote) intercambiar(inicio, posicionPivote);
        return posicionPivote;
    }

    // Inserción directa: desplaza los mayores para ubicar la clave.
    public static String ordenarInsercion(int criterio) {
        iniciarMedicion();
        for (int i = 1; i < documentos.size(); i++) {
            Documento clave = documentos.get(i);
            int j = i - 1;
            while (j >= 0 && esMayor(documentos.get(j), clave, criterio)) {
                documentos.set(j + 1, documentos.get(j));
                j--;
            }
            documentos.set(j + 1, clave);
        }
        return terminarMedicion();
    }

    // Selección directa: ubica el menor elemento restante en cada posición.
    public static String ordenarSeleccion(int criterio) {
        iniciarMedicion();
        for (int i = 0; i < documentos.size() - 1; i++) {
            int menor = i;
            for (int j = i + 1; j < documentos.size(); j++)
                if (esMayor(documentos.get(menor), documentos.get(j), criterio)) menor = j;
            if (menor != i) intercambiar(i, menor);
        }
        return terminarMedicion();
    }

    // Mezcla recursiva: divide, ordena las mitades y las combina de forma estable.
    public static String ordenarMezcla(int criterio) {
        iniciarMedicion();
        if (documentos.size() > 1) {
            Documento[] arreglo = documentos.toArray(new Documento[0]);
            Documento[] auxiliar = new Documento[arreglo.length];
            mezclaRec(arreglo, auxiliar, 0, arreglo.length - 1, criterio);
            for (int i = 0; i < arreglo.length; i++) documentos.set(i, arreglo[i]);
        }
        return terminarMedicion();
    }

    private static void mezclaRec(Documento[] arreglo, Documento[] auxiliar,
                                  int inicio, int fin, int criterio) {
        if (inicio >= fin) return;
        int medio = inicio + (fin - inicio) / 2;
        mezclaRec(arreglo, auxiliar, inicio, medio, criterio);
        mezclaRec(arreglo, auxiliar, medio + 1, fin, criterio);
        merge(arreglo, auxiliar, inicio, medio, fin, criterio);
    }

    private static void merge(Documento[] arreglo, Documento[] auxiliar,
                              int inicio, int medio, int fin, int criterio) {
        System.arraycopy(arreglo, inicio, auxiliar, inicio, fin - inicio + 1);
        int i = inicio, j = medio + 1, k = inicio;
        while (i <= medio && j <= fin) {
            if (!esMayor(auxiliar[i], auxiliar[j], criterio)) arreglo[k++] = auxiliar[i++];
            else arreglo[k++] = auxiliar[j++];
        }
        while (i <= medio) arreglo[k++] = auxiliar[i++];
        while (j <= fin) arreglo[k++] = auxiliar[j++];
    }
}