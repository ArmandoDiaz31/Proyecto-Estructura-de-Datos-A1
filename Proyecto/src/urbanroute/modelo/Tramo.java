package urbanroute.modelo;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Representa una conexión directa hacia una estación vecina junto con el
 * tiempo en minutos que toma recorrerla.
 * @author diegoarvelo
 */
public class Tramo {
    /** Estación de destino a la que llega este tramo. */
    private final Estacion Destino;
    /** Duración del recorrido a través de este tramo en minutos. */
    private int TiempoMinutos;
    
    /**
     * Crea un tramo validando que la estación destino exista y que el tiempo
     * de traslado sea mayor a cero minutos.
     * @param Destino
     * @param TiempoMinutos
     */
    public Tramo(Estacion Destino, int TiempoMinutos){
        if (Destino == null){
            throw new IllegalArgumentException("La estacion destino no puede ser nula.");
        }
        if (TiempoMinutos <= 0){
            throw new IllegalArgumentException("El tiempo de traslado debe ser mayor a 0 minutos.");
        }
        this.Destino = Destino;
        this.TiempoMinutos = TiempoMinutos;
    }
    
    /** Devuelve la estación destino conectada por este tramo.
     * @return
     * @param getDestino
     */
    public Estacion getDestino(){
        return Destino;
    }
    
    /** Devuelve el tiempo de traslado en minutos de este tramo.
     * @return
     */
    public int getTiempoMinutos(){
        return TiempoMinutos;
    }
    
    /** Actualiza el tiempo de traslado verificando que sea mayor a cero minutos. 
     * @param TiempoMinutos
     */
    public void setTiempoMinutos(int TiempoMinutos){
        if (TiempoMinutos <= 0){
            throw new IllegalArgumentException("El tiempo de traslado debe ser mayor a 0 minutos.");
        }
        this.TiempoMinutos = TiempoMinutos;
    }
}
