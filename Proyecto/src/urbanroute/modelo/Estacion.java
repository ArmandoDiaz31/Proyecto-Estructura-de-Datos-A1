/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package urbanroute.modelo;

/**
 *
 * @author Armando Diaz
 */

/**
 * Representa una estación de la red de transporte UrbanRoute.
 * <p>
 * Es el objeto que se guarda en la {@link TDA.TablaHash} y que el {@link TDA.Grafo}
 * organiza por posición (índice). No conoce ni la interfaz ni los archivos.
 * </p>
 */
public class Estacion {

    private final String id;
    private final String nombre;
    private final String linea;
    private final String zona;
    private final int capacidad;

    /** Posición de la estación dentro del grafo (-1 si aún no está en uno). */
    private int indice;

    /**
     * Crea una estación validando sus datos.
     *
     * @param id        identificador único (ej. "PRO", "CAP"); no puede estar vacío
     * @param nombre    nombre de la estación; si viene vacío se usa el ID
     * @param linea     línea a la que pertenece (puede estar vacía)
     * @param zona      zona de la ciudad (puede estar vacía)
     * @param capacidad capacidad de pasajeros por hora (no negativa)
     * @throws IllegalArgumentException si el ID es nulo o vacío, o si la capacidad es negativa
     */
    public Estacion(String id, String nombre, String linea, String zona, int capacidad) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de la estacion no puede estar vacio.");
        }
        if (capacidad < 0) {
            throw new IllegalArgumentException("La capacidad no puede ser negativa: " + capacidad);
        }
        this.id = id.trim();
        this.nombre = limpiar(nombre).isEmpty() ? this.id : limpiar(nombre);
        this.linea = limpiar(linea);
        this.zona = limpiar(zona);
        this.capacidad = capacidad;
        this.indice = -1;
    }

    /**
     * Quita espacios al inicio y al final; devuelve "" si el texto es nulo.
     *
     * @param texto texto a limpiar
     * @return texto limpio, nunca nulo
     */
    private static String limpiar(String texto) {
        return texto == null ? "" : texto.trim();
    }

    /**
     * Devuelve el identificador único de la estación.
     *
     * @return el ID
     */
    public String getId() {
        return id;
    }

    /**
     * Devuelve el nombre de la estación.
     *
     * @return el nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve la línea a la que pertenece la estación.
     *
     * @return la línea
     */
    public String getLinea() {
        return linea;
    }

    /**
     * Devuelve la zona donde está la estación.
     *
     * @return la zona
     */
    public String getZona() {
        return zona;
    }

    /**
     * Devuelve la capacidad de pasajeros por hora.
     *
     * @return la capacidad
     */
    public int getCapacidad() {
        return capacidad;
    }

    /**
     * Devuelve la posición de la estación dentro del grafo.
     *
     * @return el índice, o -1 si no está asignado
     */
    public int getIndice() {
        return indice;
    }

    /**
     * Asigna la posición de la estación dentro del grafo.
     * Lo usa {@link TDA.Grafo} al agregar o al compactar tras eliminar una estación.
     *
     * @param indice nueva posición
     */
    public void setIndice(int indice) {
        this.indice = indice;
    }

    /**
     * Describe la estación en texto, útil para listas, mensajes y etiquetas.
     *
     * @return texto con ID, nombre y línea
     */
    @Override
    public String toString() {
        return nombre + " (" + id + ")" + (linea.isEmpty() ? "" : " - Linea " + linea);
    }
}