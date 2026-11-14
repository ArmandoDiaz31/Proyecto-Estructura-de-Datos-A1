/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package urbanroute.archivos;

/**
 *
 * @author Armando Diaz
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import urbanroute.modelo.Estacion;
import TDA.Grafo;

/**
 * Frontera con el sistema de archivos: lee y escribe los CSV de estaciones y
 * de conexiones de UrbanRoute.
 * <p>
 * Al leer, salta la primera línea (la cabecera), valida cada línea restante y
 * anota los errores sin detener la carga. Lee y escribe en UTF-8 para conservar
 * tildes y la Ñ. El separador es la coma.
 * </p>
 * <p>
 * Los contadores y el texto de errores se acumulan entre cargas; llame a
 * {@link #reiniciarResultados()} antes de empezar una carga nueva. Si se carga
 * un archivo que solo trae conexiones, las estaciones que falten se crean
 * automáticamente con el ID como nombre.
 * </p>
 */
public class GestorArchivo {

    private static final String CABECERA_ESTACIONES =
            "ID_Estacion,Nombre,Linea,Zona,Capacidad_Pasajeros_Hora";
    private static final String CABECERA_CONEXIONES =
            "Estacion_Origen,Estacion_Destino,Tiempo_Minutos";

    private StringBuilder errores;
    private int lineasValidas;
    private int lineasConError;
    private int estacionesCreadas;
    private String archivoActual;

    /**
     * Crea un gestor sin errores ni contadores acumulados.
     */
    public GestorArchivo() {
        reiniciarResultados();
    }

    /**
     * Indica dónde está el archivo maestro de estaciones que se carga al iniciar.
     *
     * @return archivo datos/estaciones.csv, relativo a la carpeta del proyecto
     */
    public File getArchivoEstacionesMaestro() {
        return new File("datos", "estaciones.csv");
    }

    /**
     * Indica dónde está el archivo maestro de conexiones que se carga al iniciar.
     *
     * @return archivo datos/conexiones.csv, relativo a la carpeta del proyecto
     */
    public File getArchivoConexionesMaestro() {
        return new File("datos", "conexiones.csv");
    }

    /**
     * Pone en cero los contadores y borra los errores anotados.
     */
    public void reiniciarResultados() {
        errores = new StringBuilder();
        lineasValidas = 0;
        lineasConError = 0;
        estacionesCreadas = 0;
        archivoActual = "";
    }

    /**
     * Lee un CSV de estaciones (ID, nombre, línea, zona, capacidad) y agrega al
     * grafo cada estación válida. Las líneas inválidas (vacías, con columnas de
     * más o de menos, capacidad no numérica, ID repetido...) se anotan en
     * {@link #getErrores()} y se omiten.
     *
     * @param archivo CSV de estaciones
     * @param g       grafo donde se agregan las estaciones
     * @throws IOException si el archivo no existe o no se puede leer
     */
    public void cargarEstaciones(File archivo, Grafo g) throws IOException {
        archivoActual = archivo.getName();
        try (BufferedReader lector = abrirLector(archivo)) {
            String texto;
            int numero = 0;
            while ((texto = lector.readLine()) != null) {
                numero++;
                if (numero == 1) {
                    continue; // primera línea: cabecera
                }
                if (texto.trim().isEmpty()) {
                    registrarError(numero, "linea vacia");
                    continue;
                }
                String[] c = texto.split(",", -1);
                if (c.length != 5) {
                    registrarError(numero, "se esperaban 5 columnas y hay " + c.length);
                    continue;
                }
                String capacidad = c[4].trim();
                if (!esEnteroNoNegativo(capacidad)) {
                    registrarError(numero, "capacidad invalida '" + capacidad + "'");
                    continue;
                }
                try {
                    g.AgregarEstacion(new Estacion(c[0], c[1], c[2], c[3],
                            Integer.parseInt(capacidad)));
                    lineasValidas++;
                } catch (IllegalArgumentException ex) {
                    registrarError(numero, ex.getMessage());
                }
            }
        }
    }

    /**
     * Lee un CSV de conexiones y agrega al grafo cada conexión válida.
     * Si el grafo está vacío (no se cargaron estaciones antes), las estaciones
     * que aparezcan en las conexiones se crean automáticamente; si ya hay
     * estaciones, una conexión con un ID inexistente se rechaza.
     *
     * @param archivo CSV de conexiones
     * @param g       grafo donde se agregan las conexiones
     * @throws IOException si el archivo no existe o no se puede leer
     * @see #cargarConexiones(File, Grafo, boolean)
     */
    public void cargarConexiones(File archivo, Grafo g) throws IOException {
        cargarConexiones(archivo, g, g.getCantidadEstaciones() == 0);
    }

    /**
     * Lee un CSV de conexiones (origen, destino, tiempo) y agrega al grafo cada
     * conexión válida. Se anotan como errores las líneas vacías, con columnas
     * de más o de menos, tiempo no numérico, cero o negativo, estaciones
     * iguales y conexiones repetidas. Con crearFaltantes en false también se
     * rechazan los IDs inexistentes; con true se crea la estación (ID como
     * nombre, sin línea, zona ni capacidad) y se cuenta en
     * {@link #getEstacionesCreadas()}.
     *
     * @param archivo        CSV de conexiones
     * @param g              grafo donde se agregan las conexiones
     * @param crearFaltantes si es true, crea las estaciones que no existan
     * @throws IOException si el archivo no existe o no se puede leer
     */
    public void cargarConexiones(File archivo, Grafo g, boolean crearFaltantes) throws IOException {
        archivoActual = archivo.getName();
        try (BufferedReader lector = abrirLector(archivo)) {
            String texto;
            int numero = 0;
            while ((texto = lector.readLine()) != null) {
                numero++;
                if (numero == 1) {
                    continue; // primera línea: cabecera
                }
                if (texto.trim().isEmpty()) {
                    registrarError(numero, "linea vacia");
                    continue;
                }
                String[] c = texto.split(",", -1);
                if (c.length != 3) {
                    registrarError(numero, "se esperaban 3 columnas y hay " + c.length);
                    continue;
                }
                String origen = c[0].trim();
                String destino = c[1].trim();
                String tiempo = c[2].trim();
                if (!esEnteroPositivo(tiempo)) {
                    registrarError(numero, "tiempo invalido '" + tiempo + "'");
                    continue;
                }
                if (origen.isEmpty() || destino.isEmpty()) {
                    registrarError(numero, "ID de estacion vacio");
                    continue;
                }
                if (origen.equals(destino)) {
                    registrarError(numero, "origen y destino son la misma estacion '" + origen + "'");
                    continue;
                }
                try {
                    if (crearFaltantes) {
                        crearSiNoExiste(g, origen);
                        crearSiNoExiste(g, destino);
                    }
                    if (g.ExisteConexion(origen, destino)) {
                        registrarError(numero, "conexion repetida " + origen + " - " + destino);
                        continue;
                    }
                    g.AgregarConexion(origen, destino, Integer.parseInt(tiempo));
                    lineasValidas++;
                } catch (IllegalArgumentException ex) {
                    registrarError(numero, ex.getMessage());
                }
            }
        }
    }

    /**
     * Escribe en un CSV todas las estaciones del grafo, en UTF-8.
     * Las comas dentro de un texto se reemplazan por espacios para no romper el formato.
     *
     * @param g       grafo con las estaciones a guardar
     * @param archivo archivo de destino (se sobrescribe)
     * @throws IOException si no se puede escribir el archivo
     */
    public void guardarEstaciones(Grafo g, File archivo) throws IOException {
        try (BufferedWriter salida = abrirEscritor(archivo)) {
            salida.write(CABECERA_ESTACIONES);
            salida.newLine();
            for (int i = 0; i < g.getCantidadEstaciones(); i++) {
                Estacion e = g.ObtenerEstacionPorIndice(i);
                salida.write(limpiarCampo(e.getId()) + "," + limpiarCampo(e.getNombre()) + ","
                        + limpiarCampo(e.getLinea()) + "," + limpiarCampo(e.getZona()) + ","
                        + e.getCapacidad());
                salida.newLine();
            }
        }
    }

    /**
     * Escribe en un CSV todas las conexiones del grafo, sin repetirlas
     * (solo se recorren los pares de estaciones donde j es mayor que i).
     *
     * @param g       grafo con las conexiones a guardar
     * @param archivo archivo de destino (se sobrescribe)
     * @throws IOException si no se puede escribir el archivo
     */
    public void guardarConexiones(Grafo g, File archivo) throws IOException {
        try (BufferedWriter salida = abrirEscritor(archivo)) {
            salida.write(CABECERA_CONEXIONES);
            salida.newLine();
            int n = g.getCantidadEstaciones();
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    int duracion = g.getDuracion(i, j);
                    if (duracion > 0) {
                        salida.write(limpiarCampo(g.ObtenerEstacionPorIndice(i).getId()) + ","
                                + limpiarCampo(g.ObtenerEstacionPorIndice(j).getId()) + "," + duracion);
                        salida.newLine();
                    }
                }
            }
        }
    }

    /**
     * Devuelve los errores anotados, uno por línea, por ejemplo
     * "[conexiones.csv] Linea 12: tiempo invalido 'abc'".
     *
     * @return texto con los errores; vacío si no hubo ninguno
     */
    public String getErrores() {
        return errores.toString();
    }

    /**
     * Devuelve cuántas líneas se cargaron correctamente (estaciones y conexiones).
     *
     * @return cantidad de líneas válidas
     */
    public int getLineasValidas() {
        return lineasValidas;
    }

    /**
     * Devuelve cuántas líneas se rechazaron por algún error.
     *
     * @return cantidad de líneas con error
     */
    public int getLineasConError() {
        return lineasConError;
    }

    /**
     * Devuelve cuántas estaciones se crearon automáticamente a partir de las
     * conexiones. Si es mayor que cero, el archivo cargado no traía estaciones
     * y al guardar en ese archivo fuente basta con {@link #guardarConexiones}.
     *
     * @return cantidad de estaciones creadas automáticamente
     */
    public int getEstacionesCreadas() {
        return estacionesCreadas;
    }

    /**
     * Crea una estación básica (ID como nombre) si el grafo no la tiene.
     *
     * @param g  grafo donde se busca y se agrega
     * @param id ID de la estación
     */
    private void crearSiNoExiste(Grafo g, String id) {
        if (g.ObtenerEstacion(id) == null) {
            g.AgregarEstacion(new Estacion(id, id, "", "", 0));
            estacionesCreadas++;
        }
    }

    /**
     * Indica si un texto es un entero mayor que cero.
     *
     * @param s texto a revisar
     * @return true si es un entero positivo
     */
    private boolean esEnteroPositivo(String s) {
        try {
            return Integer.parseInt(s) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Indica si un texto es un entero mayor o igual a cero.
     *
     * @param s texto a revisar
     * @return true si es un entero no negativo
     */
    private boolean esEnteroNoNegativo(String s) {
        try {
            return Integer.parseInt(s) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Anota un error con el nombre del archivo y el número de línea.
     *
     * @param numeroLinea número de línea con el problema
     * @param mensaje     descripción del error
     */
    private void registrarError(int numeroLinea, String mensaje) {
        errores.append("[").append(archivoActual).append("] Linea ")
                .append(numeroLinea).append(": ").append(mensaje).append("\n");
        lineasConError++;
    }

    /**
     * Abre un archivo para leerlo en UTF-8.
     *
     * @param archivo archivo a leer
     * @return lector del archivo
     * @throws IOException si no se puede abrir
     */
    private static BufferedReader abrirLector(File archivo) throws IOException {
        return new BufferedReader(new InputStreamReader(
                new FileInputStream(archivo), StandardCharsets.UTF_8));
    }

    /**
     * Abre un archivo para escribirlo en UTF-8 (se sobrescribe).
     *
     * @param archivo archivo a escribir
     * @return escritor del archivo
     * @throws IOException si no se puede abrir
     */
    private static BufferedWriter abrirEscritor(File archivo) throws IOException {
        return new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(archivo), StandardCharsets.UTF_8));
    }

    /**
     * Cambia comas y saltos de línea por espacios para no romper el CSV.
     *
     * @param s texto a limpiar
     * @return texto sin comas ni saltos de línea
     */
    private static String limpiarCampo(String s) {
        return s.replace(',', ' ').replace('\n', ' ').replace('\r', ' ');
    }
}