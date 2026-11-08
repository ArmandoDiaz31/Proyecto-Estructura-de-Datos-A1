package urbanroute.modelo;

public class Estacion {
    private final String id;
    private final String nombre;
    private final String linea;
    private final String zona;
    private final int capacidad;

    private int indice;

    public Estacion(String id, String nombre, String linea, String zona, int capacidad) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de la estación no puede estar vacío.");
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

    private static String limpiar(String texto) {
        return texto == null ? "" : texto.trim();
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getLinea() {
        return linea;
    }

    public String getZona() {
        return zona;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    @Override
    public String toString() {
        return nombre + " (" + id + ")" + (linea.isEmpty() ? "" : " - Línea " + linea);
    }
}
