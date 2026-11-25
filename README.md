# Taller 5 - Recursividad y Ordenamiento

Proyecto Java Swing basado en el proyecto de ordenamiento entregado por el profesor.

## Funcionalidades

- Carga de los registros de clientes desde `src/datos/Datos.csv`.
- Dos criterios compuestos:
  1. Nombre completo y luego tipo de documento.
  2. Tipo de documento y luego nombre completo.
- Cinco algoritmos: Burbuja, Rápido (QuickSort), Inserción, Selección y Mezcla (Merge Sort).
- Visualización del tiempo de ejecución y de los registros ordenados.

## Estructura

```
src/
  App.java
  controladores/OrdenamientoControlador.java
  modelos/Documento.java
  servicios/ArchivoServicio.java
  servicios/CronometroServicio.java
  servicios/DocumentoServicio.java
  vistas/OrdenamientoVista.java
  datos/Datos.csv
  datos/Datos_02.csv ... Datos_06.csv
```

El archivo de datos suministrado contiene 100000 líneas contando el encabezado. Para conservar todos los registros en el repositorio, se guardó en seis partes consecutivas. La aplicación las carga como un solo conjunto: no se deben ordenar por separado.

## Ejecución

Desde la raíz del proyecto:

```bash
javac -encoding UTF-8 -d out src/App.java src/modelos/Documento.java src/servicios/ArchivoServicio.java src/servicios/CronometroServicio.java src/servicios/DocumentoServicio.java src/vistas/OrdenamientoVista.java src/controladores/OrdenamientoControlador.java
java -cp out App
```

Los algoritmos se ejecutan en un hilo de trabajo para mantener la ventana receptiva. Burbuja, Inserción y Selección pueden tardar bastante con 99999 registros debido a su complejidad cuadrática; Rápido y Mezcla son más adecuados para conjuntos grandes.
