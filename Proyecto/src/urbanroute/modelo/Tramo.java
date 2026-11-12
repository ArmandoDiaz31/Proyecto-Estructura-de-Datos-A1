package urbanroute.modelo;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author diegoarvelo
 */
public class Tramo {
    private final Estacion Destino;
    private int TiempoMinutos;
    
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
    
    public Estacion getDestino(){
        return Destino;
    }
    
    public int getTiempoMinutos(){
        return TiempoMinutos;
    }
    
    public void setTiempoMinutos(int TiempoMinutos){
        if (TiempoMinutos <= 0){
            throw new IllegalArgumentException("El tiempo de traslado debe ser mayor a 0 minutos.");
        }
        this.TiempoMinutos = TiempoMinutos;
    }
}
