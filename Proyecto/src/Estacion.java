/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author diegoarvelo
 */
public class Estacion {
    private String Nombre;
    private boolean Operativa;
    private String Linea;
    private Lista Vecinos; //Necesario que se cree clase lista donde se almacenará el recorrido entre anteriores y siguientes a esta estacion.
    
    public Estacion(String Nombre, String Linea){
        this.Nombre = Nombre;
        this.Linea = Linea;
        this.Operativa = true;
        this.Vecinos = new Lista();
    }
    
    public void AgregarVecino(Estacion Vecino, int TiempoMinutos){
        
    }
    
    public String getNombre(){
        return Nombre;
    }
    
    public boolean esOperativa(){
        return Operativa;
    }
    
    public boolean definirOperativa(boolean Operativa){
        this.Operativa = Operativa;
    }
    
    public String getLinea(){
        return Linea;
    }
    
    public Lista getLista(){
        return Vecinos;
    }
}
