/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author diegoarvelo
 */
public class Grafo {
    private Lista<Estacion> Estaciones;
    
    public Grafo(){
        this.Estaciones = new Lista();
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
