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

public class Grafo {
    private ListaEnlazada<Estacion> Estaciones;
    
    public Grafo(){
        this.Estaciones = new ListaEnlazada("Estaciones");
    }
    
    public Estacion buscarEstacion(String Nombre){
        for (Estacion estacion: Estaciones){
            if (estacion.getNombre().equalsIgnoreCase(Nombre.trim())){
                return estacion;
            }
        }
        return null;
    }
}
