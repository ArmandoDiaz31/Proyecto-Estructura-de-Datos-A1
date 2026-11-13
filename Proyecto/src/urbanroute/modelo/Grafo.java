package urbanroute.modelo;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author diegoarvelo
 */
import TDA.ListaEnlazada;
import TDA.Cola;
import TDA.Nodo;

public class Grafo {
    private static final int CAPACIDAD_INICIAL = 64;
    
    public static class ResultadoRuta{
        private final Estacion[] Camino;
        private final int TiempoTotal;
        private final boolean ExisteRuta;
        
        public ResultadoRuta(Estacion[] Camino, int TiempoTotal, boolean ExisteRuta){
            this.Camino = Camino;
            this.TiempoTotal= TiempoTotal;
            this.ExisteRuta = ExisteRuta;
        }
        
        public Estacion[] getCamino(){
            return Camino;
        }
        
        public int getTiempoTotal(){
            return TiempoTotal;
        }
        
        public boolean esExisteRuta(){
            return ExisteRuta;
        }
    }
    
    private Estacion[] estaciones;
    private ListaEnlazada<Tramo>[] Adyacentes;
    private int CantidadEstaciones;
    private int CantidadConexiones;
    private TablaHash IndicePorID;
    private TablaHash IndicePorNombre;
    
    public Grafo(){
        this.estaciones = new Estacion[CAPACIDAD_INICIAL];
        this.Adyacentes = (ListaEnlazada<Tramo>[]) new ListaEnlazada[CAPACIDAD_INICIAL];
        this.CantidadEstaciones = 0;
        this.CantidadConexiones = 0;
        this.IndicePorID = new TablaHash();
        this.IndicePorNombre = new TablaHash();
    }
    
    public void Limpiar(){
        this.estaciones = new Estacion[CAPACIDAD_INICIAL];
        this.Adyacentes = (ListaEnlazada<Tramo>[]) new ListaEnlazada[CAPACIDAD_INICIAL];
        this.CantidadEstaciones = 0;
        this.CantidadConexiones = 0;
        this.IndicePorID = new TablaHash();
        this.IndicePorNombre = new TablaHash();
    }
    
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
    
    public Estacion buscarEstacion(String NombreOid){
        return ObtenerEstacion(NombreOid);
    }
    
    public Estacion ObtenerEstacionPorIndice(int Indice){
        if (Indice < 0 || Indice >= CantidadEstaciones){
            return null;
        }
        return estaciones[Indice];
    }
    
    public ListaEnlazada<Tramo> ObtenerAdyacentesPorIndice(int Indice){
        if (Indice < 0 || Indice >= CantidadEstaciones) {
            return null;
        }
        return Adyacentes[Indice];
    }
    
    public Estacion[] ObtenerTodasLasEstaciones(){
        Estacion[] pCopia = new Estacion[CantidadEstaciones];
        for (int i = 0; i < CantidadEstaciones; i++){
            pCopia[i] = estaciones[i];
        }
        return pCopia;
    }
    
    public boolean ExisteConexion(String idOrigen, String idDestino){
        Estacion Origen = ObtenerEstacion(idOrigen);
        Estacion Destino = ObtenerEstacion(idDestino);
        if (Origen == null || Destino == null){
            return false;
        }
        return BuscarTramo(Origen.getIndice(), Destino) != null;
    }
    
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
            estaciones[i].setIndice( -1);
        }
        estaciones[CantidadEstaciones - 1] = null;
        Adyacentes[CantidadEstaciones - 1] = null;
        CantidadEstaciones--;
        Objetivo.setIndice(-1);
        
        return Objetivo;
    }
    
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
    
    public int getCantidadEstaciones(){
        return CantidadEstaciones;
    }
    
    public int getCantidadConexiones(){
        return CantidadConexiones;
    }
    
    public int ObtenerGrado(String NombreOid){
        Estacion e = ObtenerEstacion(NombreOid);
        if (e == null){
            return 0;
        }
        return Adyacentes[e.getIndice()].Tamano();
    }
    
    public ResultadoRuta CalcularRutaMasCorta(String idOrigen, String idDestino){
        Estacion Origen = ObtenerEstacion(idOrigen);
        Estacion Destino = ObtenerEstacion(idDestino);
        if (Origen == null || Destino == null){
            throw new IllegalArgumentException("Debe seleccionar estaciones de origen y destino validas.");
        }
        
        if (Origen == Destino){
            return new ResultadoRuta(new Estacion[]{Origen}, 0, true);
        }
        
        int n = CantidadEstaciones;
        int[] Dist = new int[n];
        int[] Previo = new int[n];
        boolean[] Visitado = new boolean[n];
        
        for (int i = 0; i < n; i++){
            Dist[i] = Integer.MAX_VALUE;
            Previo[i] = -1;
            Visitado[i] = false;
        }
        Dist[Origen.getIndice()] = 0;
        
        for (int iter = 0; iter < n; iter++){
            int u = -1;
            int minDist = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++){
                if (!Visitado[i] && Dist[i] < minDist){
                    minDist = Dist[i];
                    u = i;
                }
            }
            if (u == -1 || u == Destino.getIndice()){
                break;
            }
            Visitado[u] = true;
            
            Nodo <Tramo> NodoAdyac = Adyacentes[u].Primero();
            while (NodoAdyac != null){
                Tramo tramo = NodoAdyac.getInfo();
                int v = tramo.getDestino().getIndice();
                int Duracion = tramo.getTiempoMinutos();
                if (!Visitado[v] && Dist[u] != Integer.MAX_VALUE && Dist[u] + Duracion < Dist[v]){
                    Dist[v] = Dist[u] + Duracion;
                    Previo[v] = u;
                }
                NodoAdyac = NodoAdyac.getSiguiente();
            }
        }
        
        int idxDest = Destino.getIndice();
        if (Dist[idxDest] == Integer.MAX_VALUE){
            return new ResultadoRuta(new Estacion[0], -1, false);
        }
        
        int Pasos = 0;
        for (int temp = idxDest; temp != -1; temp = Previo[temp]){
            Pasos++;
        }
        Estacion[] Camino = new Estacion[Pasos];
        int Posicion = Pasos -1;
        for (int temp = idxDest; temp != -1; temp = Previo[temp]){
            Camino[Posicion--] = estaciones[temp];
        }
        
        return new ResultadoRuta(Camino, Dist[idxDest], true);
    }
    
    public Estacion[][] DetectarSubredes(){
        if (CantidadEstaciones == 0){
            return new Estacion[0][0];
        }
        
        boolean[] Visitado = new boolean[CantidadEstaciones];
        ListaEnlazada<Estacion[]> ListaSubredes = new ListaEnlazada<>("Subredes");
        Cola<Integer> Cola = new Cola<>();
        
        for (int i = 0; i < CantidadEstaciones; i++){
            if (!Visitado[i]){
                ListaEnlazada<Estacion> Componente = new ListaEnlazada<>("Componente");
                Visitado[i] = true;
                Cola.Encolar(i);
                
                while (!Cola.EsVacio()){
                    int u = Cola.Desencolar();
                    Componente.InsertarAlFinal(estaciones[u]);
                    
                    Nodo<Tramo> NodoAdyac = Adyacentes[u].Primero();
                    while (NodoAdyac != null){
                        int v = NodoAdyac.getInfo().getDestino().getIndice();
                        if (!Visitado[v]){
                            Visitado[v] = true;
                            Cola.Encolar(v);
                        }
                        NodoAdyac = NodoAdyac.getSiguiente();
                    }
                }
                Estacion[] ArregloComp = new Estacion[Componente.Tamano()];
                Nodo<Estacion> pAux = Componente.Primero();
                int k = 0;
                while (pAux != null){
                    ArregloComp[k++] = pAux.getInfo();
                    pAux = pAux.getSiguiente();
                }
                ListaSubredes.InsertarAlFinal(ArregloComp);
            }
        }
        
        Estacion[][] Resultado = new Estacion[ListaSubredes.Tamano()][];
        Nodo<Estacion[]> pAuxRedes = ListaSubredes.Primero();
        int idxRedes = 0;
        while (pAuxRedes != null){
            Resultado[idxRedes++] = pAuxRedes.getInfo();
            pAuxRedes = pAuxRedes.getSiguiente();
        }
        return Resultado;
    }
    
    public Estacion[] IdentificarHubs() {
        Estacion[] Ranking = ObtenerTodasLasEstaciones();
        for (int i =1; i < Ranking.length; i ++){
            Estacion Actual = Ranking[i];
            int GradoActual = Adyacentes[Actual.getIndice()].Tamano();
            int j = i -1;
            
            while (j >= 0){
                int GradoJ = Adyacentes[Ranking[j].getIndice()].Tamano();
                boolean DebeSubir = false;
                
                if (GradoActual > GradoJ){
                    DebeSubir = true;
                }
                else if (GradoActual == GradoJ){
                    int Orden = Actual.getId().compareToIgnoreCase(Ranking[j].getId());
                    if (Orden < 0){
                        DebeSubir = true;
                    }
                }
                if (DebeSubir == false){
                    break;
                }
                Ranking[j + 1] = Ranking[j];
                j--;
            }
            Ranking[j + 1] = Actual;
        }
        return Ranking;
    }
    
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
    
    public static String NormalizarClave(String texto){
        if (texto == null){
            return "";
        }
        else{
            return texto.trim().toUpperCase();
        }
    }
}
