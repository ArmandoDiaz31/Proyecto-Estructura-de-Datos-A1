/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package urbanroute.modelo;

import TDA.Cola;
import TDA.Grafo;
import TDA.ListaEnlazada;
import TDA.Nodo;

/**
 *
 * @author diegoarvelo
 */
public class AnalisisRed {
    private final Grafo grafo = new Grafo();
    
    public ResultadoRuta CalcularRutaMasCorta(String idOrigen, String idDestino){
        Estacion Origen = grafo.ObtenerEstacion(idOrigen);
        Estacion Destino = grafo.ObtenerEstacion(idDestino);
        if (Origen == null || Destino == null){
            throw new IllegalArgumentException("Debe seleccionar estaciones de origen y destino validas.");
        }
        
        if (Origen == Destino){
            return new ResultadoRuta(new Estacion[]{Origen}, 0, true);
        }
        
        int n = grafo.CantidadEstaciones;
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
            
            Nodo <Tramo> NodoAdyac = grafo.Adyacentes[u].Primero();
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
            Camino[Posicion--] = grafo.estaciones[temp];
        }
        
        return new ResultadoRuta(Camino, Dist[idxDest], true);
    }
    
    public Estacion[][] DetectarSubredes(){
        if (grafo.CantidadEstaciones == 0){
            return new Estacion[0][0];
        }
        
        boolean[] Visitado = new boolean[grafo.CantidadEstaciones];
        ListaEnlazada<Estacion[]> ListaSubredes = new ListaEnlazada<>("Subredes");
        Cola<Integer> Cola = new Cola<>();
        
        for (int i = 0; i < grafo.CantidadEstaciones; i++){
            if (!Visitado[i]){
                ListaEnlazada<Estacion> Componente = new ListaEnlazada<>("Componente");
                Visitado[i] = true;
                Cola.Encolar(i);
                
                while (!Cola.EsVacio()){
                    int u = Cola.Desencolar();
                    Componente.InsertarAlFinal(grafo.estaciones[u]);
                    
                    Nodo<Tramo> NodoAdyac = grafo.Adyacentes[u].Primero();
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
        Estacion[] Ranking = grafo.ObtenerTodasLasEstaciones();
        for (int i =1; i < Ranking.length; i ++){
            Estacion Actual = Ranking[i];
            int GradoActual = grafo.Adyacentes[Actual.getIndice()].Tamano();
            int j = i -1;
            
            while (j >= 0){
                int GradoJ = grafo.Adyacentes[Ranking[j].getIndice()].Tamano();
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
}
