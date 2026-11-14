package TDA;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Representa la red de transporte como un grafo ponderado no dirigido mediante
 * listas de adyacencia y tablas hash para búsquedas rápidas de orden O(1).
 * @author diegoarvelo
 */
import urbanroute.modelo.Estacion;
import urbanroute.modelo.Tramo;

public class Grafo {
    /** Capacidad inicial de los arreglos internos de estaciones y adyacencia. */
    private static final int CAPACIDAD_INICIAL = 64;
    /** Arreglo que almacena las estaciones registradas en la red. */
    public Estacion[] estaciones;
    /** Arreglo de listas enlazadas con los tramos conectados a cada estación. */
    public ListaEnlazada<Tramo>[] Adyacentes;
    /** Cantidad actual de estaciones activas en el grafo. */
    public int CantidadEstaciones;
    /** Cantidad actual de conexiones bidireccionales en la red. */
    public int CantidadConexiones;
    /** Tabla hash para buscar estaciones rápidamente por su código ID. */
    private TablaHash IndicePorID;
    /** Tabla hash para buscar estaciones rápidamente por su nombre. */
    private TablaHash IndicePorNombre;
    
    /** Crea un grafo vacío inicializando los arreglos y las tablas hash. */
    public Grafo(){
        this.estaciones = new Estacion[CAPACIDAD_INICIAL];
        this.Adyacentes = (ListaEnlazada<Tramo>[]) new ListaEnlazada[CAPACIDAD_INICIAL];
        this.CantidadEstaciones = 0;
        this.CantidadConexiones = 0;
        this.IndicePorID = new TablaHash();
        this.IndicePorNombre = new TablaHash();
    }
    
    /** Vacía todas las estaciones y conexiones del grafo reiniciandolo. */
    public void Limpiar(){
        this.estaciones = new Estacion[CAPACIDAD_INICIAL];
        this.Adyacentes = (ListaEnlazada<Tramo>[]) new ListaEnlazada[CAPACIDAD_INICIAL];
        this.CantidadEstaciones = 0;
        this.CantidadConexiones = 0;
        this.IndicePorID = new TablaHash();
        this.IndicePorNombre = new TablaHash();
    }
    
    /**
     * Registra una estación nueva en el grafo, crea su lista de adyacencia
     * y la indexa en las tablas hash por ID y por nombre.
     * @param e
     */
    public void AgregarEstacion(Estacion e){
        if (e == null){
            throw new IllegalArgumentException("La estacion no puede ser nula.");
        }
        String ClaveID = NormalizarClave(e.getId());
        if (IndicePorID.contiene(ClaveID)){
            throw new IllegalArgumentException("Ya existe una estacion con el ID'"+ e.getId() + "'.");
        }
        AsegurarCapacidad();
        
        int idx = CantidadEstaciones;
        e.setIndice(idx);
        estaciones[idx] = e;
        Adyacentes[idx] = new ListaEnlazada<>("Adyacente_"+ e.getId());
        CantidadEstaciones++;
        
        IndicePorID.insertar(ClaveID, e);
        String ClaveNombre = NormalizarClave(e.getNombre());
        if (!ClaveNombre.isEmpty() && !IndicePorNombre.contiene(ClaveNombre)){
            IndicePorNombre.insertar(ClaveNombre, e);
        }
    }
    
    /** Busca y devuelve una estación en tiempo O(1) consultando por su ID o nombre. 
     * @param Clave
     * @return
     */
    public Estacion ObtenerEstacion(String Clave){
        if (Clave == null || Clave.trim().isEmpty()){
            return null;
        }
        String Normal = NormalizarClave(Clave);
        Estacion Encontrada = IndicePorID.buscar(Normal);
        if (Encontrada != null){
            return Encontrada;
        }
        return IndicePorNombre.buscar(Normal);
    }
    
    /** Método auxiliar que delega la búsqueda rápida de una estación por nombre o ID. 
     * @param NombreOid
     * @return
     */
    public Estacion buscarEstacion(String NombreOid){
        return ObtenerEstacion(NombreOid);
    }
    
    /** Devuelve la estación ubicada en la posición del índice interno indicado. 
     * @param Indice
     * @return
     */
    public Estacion ObtenerEstacionPorIndice(int Indice){
        if (Indice < 0 || Indice >= CantidadEstaciones){
            return null;
        }
        return estaciones[Indice];
    }
    
    /** Devuelve la lista de tramos adyacentes de una estación según su índice interno. 
     * @return
     * @param Indice
     */
    public ListaEnlazada<Tramo> ObtenerAdyacentesPorIndice(int Indice){
        if (Indice < 0 || Indice >= CantidadEstaciones) {
            return null;
        }
        return Adyacentes[Indice];
    }
    
    /** Devuelve un arreglo con la copia de todas las estaciones activas en la red. 
     * @return
     */
    public Estacion[] ObtenerTodasLasEstaciones(){
        Estacion[] pCopia = new Estacion[CantidadEstaciones];
        for (int i = 0; i < CantidadEstaciones; i++){
            pCopia[i] = estaciones[i];
        }
        return pCopia;
    }
    
    /** Verifica si existe un tramo directo que conecte las dos estaciones indicadas. 
     * @return 
     * @param idOrigen
     * @param idDestino
     */
    public boolean ExisteConexion(String idOrigen, String idDestino){
        Estacion Origen = ObtenerEstacion(idOrigen);
        Estacion Destino = ObtenerEstacion(idDestino);
        if (Origen == null || Destino == null){
            return false;
        }
        return BuscarTramo(Origen.getIndice(), Destino) != null;
    }
    
    /**
     * Crea una conexión de ida y vuelta entre dos estaciones existentes con el
     * tiempo en minutos indicado.
     * @param idOrigen
     * @param idDestino
     * @param TiempoMinutos
     */
    public void AgregarConexion(String idOrigen, String idDestino, int TiempoMinutos){
        if (TiempoMinutos <= 0){
            throw new IllegalArgumentException("El tiempo de traslado debe ser mayor a 0 minutos.");
        }
        Estacion Origen = ObtenerEstacion(idOrigen);
        Estacion Destino = ObtenerEstacion(idDestino);
        if (Origen == null){
            throw new IllegalArgumentException("No existe la estacion origen: " +idOrigen);
        }
        if (Destino == null){
            throw new IllegalArgumentException("No existe la estacion destino: " +idDestino);
        }
        if (Origen == Destino){
            throw new IllegalArgumentException("No se permiten conexiones de una estacion consigo misma");
        }
        if (BuscarTramo(Origen.getIndice(), Destino) != null){
            throw new IllegalArgumentException("Ya existe una conexion entre " + Origen.getId() + " y " + Destino.getId() + ".");
        }
        
        Adyacentes[Origen.getIndice()].InsertarAlFinal(new Tramo(Destino, TiempoMinutos));
        Adyacentes[Destino.getIndice()].InsertarAlFinal(new Tramo(Origen, TiempoMinutos));
        CantidadConexiones++;
    }
    
    /** Elimina la conexión directa en ambos sentidos entre las dos estaciones indicadas. 
     * @return
     * @param idOrigen
     * @param idDestino
     */
    public boolean EliminarConexion(String idOrigen, String idDestino){
        Estacion Origen = ObtenerEstacion(idOrigen);
        Estacion Destino = ObtenerEstacion(idDestino);
        if (Origen == null || Destino == null){
            return false;
        }
        Tramo tIda = BuscarTramo(Origen.getIndice(), Destino);
        Tramo tVuelta = BuscarTramo(Destino.getIndice(), Origen);
        if (tIda == null || tVuelta == null){
            return false;
        }
        Adyacentes[Origen.getIndice()].Eliminar(tIda);
        Adyacentes[Destino.getIndice()].Eliminar(tVuelta);
        CantidadConexiones--;
        return true;
    }
    
    /**
     * Elimina una estación junto con todos sus tramos conectados, dándola de baja
     * tanto en el grafo como en las tablas hash.
     * @return
     * @param NombreOid
     */
    public Estacion EliminarEstacion(String NombreOid){
        Estacion Objetivo = ObtenerEstacion(NombreOid);
        if(Objetivo == null){
            return null;
        }
        int idx = Objetivo.getIndice();
        
        Nodo<Tramo> pAux = Adyacentes[idx].Primero();
        while (pAux != null){
            Estacion Vecino = pAux.getInfo().getDestino();
            Tramo Inverso = BuscarTramo(Vecino.getIndice(), Objetivo);
            if (Inverso != null){
                Adyacentes[Vecino.getIndice()].Eliminar(Inverso);
                CantidadConexiones--;
            }
            pAux = pAux.getSiguiente();
        }
        
        IndicePorID.eliminar(NormalizarClave(Objetivo.getId()));
        String ClaveNombre = NormalizarClave(Objetivo.getNombre());
        if (!ClaveNombre.isEmpty() && IndicePorNombre.buscar(ClaveNombre) == Objetivo){
            IndicePorNombre.eliminar(ClaveNombre);
            for(int i = 0; i < CantidadEstaciones; i++){
                if (i != idx && NormalizarClave(estaciones[i].getNombre()).equals(ClaveNombre)){
                    IndicePorNombre.insertar(ClaveNombre, estaciones[i]);
                    break;
                }
            }
        }
        for (int i = idx; i < CantidadEstaciones - 1; i ++){
            estaciones[i] = estaciones[i + 1];
            Adyacentes[i] = Adyacentes [i + 1];
            estaciones[i].setIndice(i);
        }
        estaciones[CantidadEstaciones - 1] = null;
        Adyacentes[CantidadEstaciones - 1] = null;
        CantidadEstaciones--;
        Objetivo.setIndice(-1);
        
        return Objetivo;
    }
    
    /** Devuelve la duración en minutos entre dos índices de estación o cero si no están conectadas. 
     * @return
     * @param IndiceOrigen
     * @param IndiceDestino
     */
    public int getDuracion(int IndiceOrigen, int IndiceDestino){
        if (IndiceOrigen < 0 || IndiceOrigen >= CantidadEstaciones || IndiceDestino < 0 || IndiceDestino >= CantidadEstaciones){
            return 0;
        }
        Tramo t = BuscarTramo(IndiceOrigen, estaciones[IndiceDestino]);
        if (t == null){
            return 0;
        }
        else {
            return t.getTiempoMinutos();
        }
    }
    
    /** Devuelve el número total de estaciones cargadas en el grafo. 
     * @return
     */
    public int getCantidadEstaciones(){
        return CantidadEstaciones;
    }
    
    /** Devuelve el número total de conexiones existentes en el grafo. 
     * @return
     */
    public int getCantidadConexiones(){
        return CantidadConexiones;
    }
    
    /** Devuelve la cantidad de conexiones directas (grado) que tiene una estación. 
     *@return
     * @param NombreOid
     */
    public int ObtenerGrado(String NombreOid){
        Estacion e = ObtenerEstacion(NombreOid);
        if (e == null){
            return 0;
        }
        return Adyacentes[e.getIndice()].Tamano();
    }
    
    /** Busca en la lista de adyacencia del origen el tramo que apunta hacia la estación destino. 
     * @return
     * @param IndiceOrigen
     * @param Destino
     */
    private Tramo BuscarTramo(int IndiceOrigen, Estacion Destino){
        Nodo<Tramo> pAux = Adyacentes[IndiceOrigen].Primero();
        while (pAux != null){
            if ( pAux.getInfo().getDestino() == Destino){
                return pAux.getInfo();
            }
            pAux = pAux.getSiguiente();
        }
        return null;
    }
    
    /** Duplica el tamaño de los arreglos internos cuando se alcanza el límite de capacidad. 
     */
    private void AsegurarCapacidad(){
        if (CantidadEstaciones < estaciones.length){
            return;
        }
        int NewCap = estaciones.length * 2;
        Estacion[] NewEst = new Estacion[NewCap];
        ListaEnlazada<Tramo>[] NewAdyac = (ListaEnlazada<Tramo>[]) new ListaEnlazada[NewCap];
        for (int i = 0; i < CantidadEstaciones; i++){
            NewEst[i] = estaciones[i];
            NewAdyac[i] = Adyacentes[i];
        }
        this.estaciones = NewEst;
        this.Adyacentes = NewAdyac;
    }
    
    /** Limpia los espacios laterales y convierte el texto a mayúsculas para usarlo como clave. 
     * @param texto
     * @return
     */
    public static String NormalizarClave(String texto){
        if (texto == null){
            return "";
        }
        else{
            return texto.trim().toUpperCase();
        }
    }
}
