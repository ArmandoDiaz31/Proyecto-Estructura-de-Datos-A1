/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TDA;

/**
 *
 * @author diegoarvelo
 */
public class Nodo <T> {
    protected T info;
    protected Nodo <T> pNext;
    
    public Nodo (T elem) {
        this.info = elem;
        this.pNext = null;
    }
    
    public T getInfo(){
        return info;
    }
    
    public void setInfo(T info){
        this.info = info;
    }
    
    public Nodo <T> getSiguiente(){
        return pNext;
    }
    
    public void setSiguiente(Nodo <T> siguiente){
        this.pNext = siguiente;
    }
}
