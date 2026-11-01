/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author diegoarvelo
 */
public class Tramo {
    private Estacion Destino;
    private int TiempoMinutos;
    
    public Tramo(Estacion Destino, int TiempoMinutos){
        this.Destino = Destino;
        this.TiempoMinutos = TiempoMinutos;
    }
    
    public Estacion getDestino(){
        return Destino;
    }
    
    public int getTiempoMinutos(){
        return TiempoMinutos;
    }
}
