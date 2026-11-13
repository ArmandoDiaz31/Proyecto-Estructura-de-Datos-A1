/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TDA;

/**
 *
 * @author diegoarvelo
 */
public class Cola <T> {
    private Nodo <T> Cabeza;
    private Nodo <T> Cola;
    private int iN;
    
    public Cola(){
        this.Cabeza = null;
        this.Cola = null;
        this.iN = 0;
    }
    
    public void Encolar(T x){
        Nodo <T> pNew = new Nodo<>(x);
        if (EsVacio()){
            Cabeza = pNew;
            Cola = pNew;
        }
        else {
            Cola.pNext  = pNew;
            Cola = pNew;
        }
        iN++;
    }
    
    public T Desencolar(){
        if (EsVacio()){
            return null;
        }
        T x = Cabeza.info;
        Cabeza = Cabeza.pNext;
        if (Cabeza == null){
            Cola = null;
        }
        iN--;
        return x;
    }
    
    public boolean EsVacio(){
        return Cabeza == null;
    }
    
    public int Tamano(){
        return iN;
    }
}
