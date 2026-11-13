package urbanroute.archivos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import urbanroute.modelo.Estacion;
import TDA.Grafo;

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

    public GestorArchivo() {
        reiniciarResultados();
    }

    public File getArchivoEstacionesMaestro() {
        return new File("datos", "estaciones.csv");
    }

    public File getArchivoConexionesMaestro() {
        return new File("datos", "conexiones.csv");
    }

    public void reiniciarResultados() {
        errores = new StringBuilder();
        lineasValidas = 0;
        lineasConError = 0;
        estacionesCreadas = 0;
        archivoActual = "";
    }

    public void cargarEstaciones(File archivo, Grafo g) throws IOException {
        archivoActual = archivo.getName();
        try (BufferedReader lector = abrirLector(archivo)) {
            String texto;
            int numero = 0;
            String separador = null;
            boolean primera = true;
            int columnas = 5;
            int cId = 0, cNombre = 1, cLinea = 2, cZona = 3, cCapacidad = 4;

            while ((texto = lector.readLine()) != null) {
                numero++;
                if (numero == 1) {
                    texto = quitarBom(texto);
                }
                if (texto.trim().isEmpty()) {
                    registrarError(numero, "línea vacía");
                    continue;
                }
                if (separador == null) {
                    separador = detectarSeparador(texto);
                }
                String[] c = texto.split(separador, -1);

                if (primera) {
                    primera = false;
                    if (esCabecera(c)) {
                        columnas = c.length;
                        cId = buscarColumna(c, 0, "idestacion");
                        cNombre = buscarColumna(c, 1, "nombre");
                        cLinea = buscarColumna(c, 2, "linea");
                        cZona = buscarColumna(c, 3, "zona");
                        cCapacidad = buscarColumna(c, 4, "capacidad");
                        continue;
                    }
                }

                if (c.length != columnas || maximo(cId, cNombre, cLinea, cZona, cCapacidad) >= columnas) {
                    registrarError(numero, "se esperaban " + columnas + " columnas y hay " + c.length);
                    continue;
                }
                String capacidad = c[cCapacidad].trim();
                if (!esEnteroNoNegativo(capacidad)) {
                    registrarError(numero, "capacidad inválida '" + capacidad + "'");
                    continue;
                }
                try {
                    Estacion e = new Estacion(c[cId], c[cNombre], c[cLinea], c[cZona],
                            Integer.parseInt(capacidad));
                    g.AgregarEstacion(e);
                    lineasValidas++;
                } catch (IllegalArgumentException ex) {
                    registrarError(numero, ex.getMessage());
                }
            }
        }
    }

    public void cargarConexiones(File archivo, Grafo g) throws IOException {
        cargarConexiones(archivo, g, g.getCantidadEstaciones() == 0);
    }

    public void cargarConexiones(File archivo, Grafo g, boolean crearFaltantes) throws IOException {
        archivoActual = archivo.getName();
        try (BufferedReader lector = abrirLector(archivo)) {
            String texto;
            int numero = 0;
            String separador = null;
            boolean primera = true;
            int columnas = 3;
            int cOrigen = 0, cDestino = 1, cTiempo = 2;

            while ((texto = lector.readLine()) != null) {
                numero++;
                if (numero == 1) {
                    texto = quitarBom(texto);
                }
                if (texto.trim().isEmpty()) {
                    registrarError(numero, "línea vacía");
                    continue;
                }
                if (separador == null) {
                    separador = detectarSeparador(texto);
                }
                String[] c = texto.split(separador, -1);

                if (primera) {
                    primera = false;
                    if (esCabecera(c)) {
                        columnas = c.length;
                        cOrigen = buscarColumna(c, 0, "origen");
                        cDestino = buscarColumna(c, 1, "destino");
                        cTiempo = buscarColumna(c, 2, "tiempo");
                        continue;
                    }
                }

                if (c.length != columnas || maximo(cOrigen, cDestino, cTiempo, 0, 0) >= columnas) {
                    registrarError(numero, "se esperaban " + columnas + " columnas y hay " + c.length);
                    continue;
                }
                String origen = c[cOrigen].trim();
                String destino = c[cDestino].trim();
                String tiempo = c[cTiempo].trim();
                if (!esEnteroPositivo(tiempo)) {
                    registrarError(numero, "tiempo inválido '" + tiempo + "'");
                    continue;
                }
                if (origen.isEmpty() || destino.isEmpty()) {
                    registrarError(numero, "ID de estación vacío");
                    continue;
                }
                if (origen.equals(destino)) {
                    registrarError(numero, "origen y destino son la misma estación '" + origen + "'");
                    continue;
                }
                try {
                    if (crearFaltantes) {
                        crearSiNoExiste(g, origen);
                        crearSiNoExiste(g, destino);
                    }
                    if (g.ExisteConexion(origen, destino)) {
                        registrarError(numero, "conexión repetida " + origen + " - " + destino);
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

    public void guardarEstaciones(Grafo g, File archivo) throws IOException {
        try (PrintWriter salida = abrirEscritor(archivo)) {
            salida.println(CABECERA_ESTACIONES);
            for (int i = 0; i < g.getCantidadEstaciones(); i++) {
                Estacion e = g.ObtenerEstacionPorIndice(i);
                salida.println(limpiarCampo(e.getId()) + "," + limpiarCampo(e.getNombre()) + ","
                        + limpiarCampo(e.getLinea()) + "," + limpiarCampo(e.getZona()) + ","
                        + e.getCapacidad());
            }
            if (salida.checkError()) {
                throw new IOException("No se pudo escribir el archivo " + archivo.getName());
            }
        }
    }

    public void guardarConexiones(Grafo g, File archivo) throws IOException {
        try (PrintWriter salida = abrirEscritor(archivo)) {
            salida.println(CABECERA_CONEXIONES);
            int n = g.getCantidadEstaciones();
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    int duracion = g.getDuracion(i, j);
                    if (duracion > 0) {
                        salida.println(limpiarCampo(g.ObtenerEstacionPorIndice(i).getId()) + ","
                                + limpiarCampo(g.ObtenerEstacionPorIndice(j).getId()) + "," + duracion);
                    }
                }
            }
            if (salida.checkError()) {
                throw new IOException("No se pudo escribir el archivo " + archivo.getName());
            }
        }
    }

    public String getErrores() {
        return errores.toString();
    }

    public int getLineasValidas() {
        return lineasValidas;
    }

    public int getLineasConError() {
        return lineasConError;
    }

    public int getEstacionesCreadas() {
        return estacionesCreadas;
    }

    private void crearSiNoExiste(Grafo g, String id) {
        if (g.ObtenerEstacion(id) == null) {
            g.AgregarEstacion(new Estacion(id, id, "", "", 0));
            estacionesCreadas++;
        }
    }

    private boolean esEnteroPositivo(String s) {
        return esEnteroNoNegativo(s) && Integer.parseInt(s) > 0;
    }

    private boolean esEnteroNoNegativo(String s) {
        if (s == null || s.isEmpty() || s.length() > 9) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < '0' || s.charAt(i) > '9') {
                return false;
            }
        }
        return true;
    }

    private void registrarError(int numeroLinea, String mensaje) {
        errores.append("[").append(archivoActual).append("] Línea ")
                .append(numeroLinea).append(": ").append(mensaje).append("\n");
        lineasConError++;
    }

    private static BufferedReader abrirLector(File archivo) throws IOException {
        return new BufferedReader(new InputStreamReader(
                new FileInputStream(archivo), StandardCharsets.UTF_8));
    }

    private static PrintWriter abrirEscritor(File archivo) throws IOException {
        return new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(archivo), StandardCharsets.UTF_8)));
    }

    private static String quitarBom(String texto) {
        if (!texto.isEmpty() && texto.charAt(0) == '\uFEFF') {
            return texto.substring(1);
        }
        return texto;
    }

    private static String detectarSeparador(String texto) {
        if (texto.indexOf(',') < 0 && texto.indexOf(';') >= 0) {
            return ";";
        }
        return ",";
    }

    private static String normalizar(String s) {
        String sinTildes = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return sinTildes.toLowerCase().replace("_", "").replace(" ", "");
    }

    private static boolean esCabecera(String[] celdas) {
        String[] claves = {"idestacion", "nombre", "linea", "zona", "capacidad",
            "origen", "destino", "tiempo"};
        int coincidencias = 0;
        for (int i = 0; i < celdas.length; i++) {
            String n = normalizar(celdas[i]);
            boolean coincide = n.equals("id");
            for (int k = 0; k < claves.length && !coincide; k++) {
                coincide = n.contains(claves[k]);
            }
            if (coincide) {
                coincidencias++;
            }
        }
        return coincidencias >= 2;
    }

    private static int buscarColumna(String[] cabecera, int porDefecto, String clave) {
        for (int i = 0; i < cabecera.length; i++) {
            if (normalizar(cabecera[i]).contains(clave)) {
                return i;
            }
        }
        return porDefecto;
    }

    private static int maximo(int a, int b, int c, int d, int e) {
        int m = a;
        if (b > m) {
            m = b;
        }
        if (c > m) {
            m = c;
        }
        if (d > m) {
            m = d;
        }
        if (e > m) {
            m = e;
        }
        return m;
    }

    private static String limpiarCampo(String s) {
        return s.replace(',', ' ').replace('\n', ' ').replace('\r', ' ');
    }
}
