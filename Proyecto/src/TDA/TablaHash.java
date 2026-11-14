/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TDA;

/**
 *
 * @author Armando Diaz
 */

import urbanroute.modelo.Estacion;

/**
 * Tabla hash propia que asocia el ID de una estación con su {@link Estacion}.
 * <p>
 * Usa encadenamiento separado: cada casilla del arreglo guarda una cadena de
 * nodos. Empieza con capacidad prima (31) y se redimensiona cuando el factor
 * de carga supera 0.75. No usa ninguna clase del Collections Framework.
 * </p>
 */
public class TablaHash {

    private static final int CAPACIDAD_INICIAL = 31;
    private static final double FACTOR_CARGA_MAXIMO = 0.75;

    /** Nodo de una cadena de colisiones. */
    private static class NodoHash {
        String clave;
        Estacion valor;
        NodoHash siguiente;

        NodoHash(String clave, Estacion valor, NodoHash siguiente) {
            this.clave = clave;
            this.valor = valor;
            this.siguiente = siguiente;
        }
    }

    private NodoHash[] tabla;
    private int cantidad;

    /**
     * Crea una tabla hash vacía con capacidad inicial prima.
     */
    public TablaHash() {
        tabla = new NodoHash[CAPACIDAD_INICIAL];
        cantidad = 0;
    }

    /**
     * Inserta una estación asociada a su ID.
     *
     * @param id ID de la estación (clave)
     * @param e  estación a guardar
     * @throws IllegalArgumentException si el ID es nulo o vacío, la estación es nula
     *                                  o el ID ya existe
     */
    public void insertar(String id, Estacion e) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID no puede estar vacio.");
        }
        if (e == null) {
            throw new IllegalArgumentException("La estacion no puede ser nula.");
        }
        String clave = id.trim();
        if (contiene(clave)) {
            throw new IllegalArgumentException("Ya existe una estacion con el ID '" + clave + "'.");
        }
        if ((cantidad + 1) > tabla.length * FACTOR_CARGA_MAXIMO) {
            redimensionar();
        }
        int pos = funcionHash(clave);
        tabla[pos] = new NodoHash(clave, e, tabla[pos]);
        cantidad++;
    }

    /**
     * Busca una estación por su ID.
     *
     * @param id ID a buscar
     * @return la estación, o null si no existe
     */
    public Estacion buscar(String id) {
        if (id == null) {
            return null;
        }
        String clave = id.trim();
        NodoHash actual = tabla[funcionHash(clave)];
        while (actual != null) {
            if (actual.clave.equals(clave)) {
                return actual.valor;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    /**
     * Elimina una estación por su ID.
     *
     * @param id ID de la estación a eliminar
     * @return la estación eliminada, o null si no existía
     */
    public Estacion eliminar(String id) {
        if (id == null) {
            return null;
        }
        String clave = id.trim();
        int pos = funcionHash(clave);
        NodoHash anterior = null;
        NodoHash actual = tabla[pos];
        while (actual != null) {
            if (actual.clave.equals(clave)) {
                if (anterior == null) {
                    tabla[pos] = actual.siguiente;
                } else {
                    anterior.siguiente = actual.siguiente;
                }
                cantidad--;
                return actual.valor;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
        return null;
    }

    /**
     * Indica si existe una estación con ese ID.
     *
     * @param id ID a consultar
     * @return true si existe
     */
    public boolean contiene(String id) {
        return buscar(id) != null;
    }

    /**
     * Devuelve cuántas estaciones hay guardadas.
     *
     * @return cantidad de estaciones
     */
    public int tamano() {
        return cantidad;
    }

    /**
     * Devuelve todas las estaciones en un arreglo (sin orden particular).
     * Sirve para recorrerlas o guardarlas en archivo.
     *
     * @return arreglo con todas las estaciones; vacío si no hay ninguna
     */
    public Estacion[] obtenerTodas() {
        Estacion[] resultado = new Estacion[cantidad];
        int k = 0;
        for (int i = 0; i < tabla.length; i++) {
            NodoHash actual = tabla[i];
            while (actual != null) {
                resultado[k] = actual.valor;
                k++;
                actual = actual.siguiente;
            }
        }
        return resultado;
    }

    /**
     * Calcula la casilla de una clave recorriendo sus caracteres.
     * El módulo se aplica en cada paso para evitar desbordamientos y negativos.
     *
     * @param id clave
     * @return posición entre 0 y capacidad - 1
     */
    private int funcionHash(String id) {
        int h = 0;
        for (int i = 0; i < id.length(); i++) {
            h = (h * 31 + id.charAt(i)) % tabla.length;
        }
        return h;
    }

    /**
     * Crea una tabla del doble de tamaño más uno y reubica todos los nodos.
     */
    private void redimensionar() {
        NodoHash[] vieja = tabla;
        tabla = new NodoHash[vieja.length * 2 + 1];
        for (int i = 0; i < vieja.length; i++) {
            NodoHash actual = vieja[i];
            while (actual != null) {
                NodoHash proximo = actual.siguiente;
                int pos = funcionHash(actual.clave);
                actual.siguiente = tabla[pos];
                tabla[pos] = actual;
                actual = proximo;
            }
        }
    }
}
