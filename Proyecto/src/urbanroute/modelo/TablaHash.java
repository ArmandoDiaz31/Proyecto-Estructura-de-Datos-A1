package urbanroute.modelo;

public class TablaHash {
    private static final int CAPACIDAD_INICIAL = 31;
    private static final double FACTOR_CARGA_MAXIMO = 0.75;

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

    public TablaHash() {
        tabla = new NodoHash[CAPACIDAD_INICIAL];
        cantidad = 0;
    }

    public void insertar(String id, Estacion e) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID no puede estar vacío.");
        }
        if (e == null) {
            throw new IllegalArgumentException("La estación no puede ser nula.");
        }
        String clave = id.trim();
        if (contiene(clave)) {
            throw new IllegalArgumentException("Ya existe una estación con el ID '" + clave + "'.");
        }
        if ((cantidad + 1) > tabla.length * FACTOR_CARGA_MAXIMO) {
            redimensionar();
        }
        int pos = funcionHash(clave);
        tabla[pos] = new NodoHash(clave, e, tabla[pos]);
        cantidad++;
    }

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

    public boolean contiene(String id) {
        return buscar(id) != null;
    }

    public int tamano() {
        return cantidad;
    }

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

    private int funcionHash(String id) {
        int h = 0;
        for (int i = 0; i < id.length(); i++) {
            h = (h * 31 + id.charAt(i)) % tabla.length;
        }
        return h;
    }

    private void redimensionar() {
        NodoHash[] vieja = tabla;
        tabla = new NodoHash[siguientePrimo(vieja.length * 2 + 1)];
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

    private static int siguientePrimo(int n) {
        int candidato = n;
        while (!esPrimo(candidato)) {
            candidato++;
        }
        return candidato;
    }

    private static boolean esPrimo(int n) {
        if (n < 2) {
            return false;
        }
        for (int d = 2; d * d <= n; d++) {
            if (n % d == 0) {
                return false;
            }
        }
        return true;
    }
}
