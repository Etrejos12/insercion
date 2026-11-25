# Inserción y comparación de algoritmos de ordenamiento

Proyecto Java de escritorio (Swing) para comparar cuatro algoritmos de ordenamiento sobre datos leídos desde CSV:

- Burbuja
- Rápido (Quick Sort)
- Inserción
- Mezcla (Merge Sort)

## Estructura

```
src/
├── Main.java
├── controladores/
│   └── OrdenamientoControlador.java
├── modelos/
│   └── Documento.java
├── servicios/
│   ├── CronometroServicio.java
│   └── DocumentoServicio.java
├── vistas/
│   └── OrdenamientoVista.java
└── datos/
    └── Datos.csv
```

## Ejecución

Desde la raíz del repositorio, compila y ejecuta:

```bash
javac -encoding UTF-8 -d out src/Main.java src/modelos/Documento.java src/servicios/CronometroServicio.java src/servicios/DocumentoServicio.java src/vistas/OrdenamientoVista.java src/controladores/OrdenamientoControlador.java
java -cp out Main
```

El combo permite escoger la columna por la cual ordenar. Los valores numéricos se comparan numéricamente y los demás como texto, sin distinguir mayúsculas y minúsculas.

## Datos

El archivo de entrada está en `src/datos/Datos.csv`. La primera fila debe contener los encabezados. Los campos se separan por comas y se admiten valores entre comillas dobles.

> Esta rama contiene una implementación base autónoma porque la rama original solo tenía el README y no incluía las clases del proyecto anterior. Sustituye el CSV de ejemplo por los datos de la actividad si corresponde.
