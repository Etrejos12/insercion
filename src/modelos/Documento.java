package modelos;

/**
 * Representa una fila del archivo CSV. Los valores se conservan como texto
 * para permitir ordenar tanto columnas numericas como columnas alfabeticas.
 */
public class Documento {
    private final String[] valores;

    public Documento(String[] valores) {
        this.valores = valores.clone();
    }

    public String getValor(int indice) {
        return indice >= 0 && indice < valores.length ? valores[indice] : "";
    }

    public String[] getValores() {
        return valores.clone();
    }
}
