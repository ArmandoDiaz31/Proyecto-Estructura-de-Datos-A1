/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TDA;

/**
 * Representa un nodo genérico que almacena un dato y el enlace al siguiente
 * elemento dentro de una estructura dinámica.
 * @author diegoarvelo
 */
public class Nodo <T> {
    /** Dato almacenado dentro del nodo. */
    protected T info;
    /** Referencia al siguiente nodo de la estructura. */
    protected Nodo <T> pNext;
    
    /** Crea un nodo nuevo guardando el elemento recibido y sin nodo siguiente. 
     * @param elem
     */
    public Nodo (T elem) {
        this.info = elem;
        this.pNext = null;
    }
    
    /**
     * Devuelve el dato almacenado en el nodo.
     * @return
     */
    public T getInfo(){
        return info;
    }
    
    /** Actualiza el dato almacenado dentro del nodo. 
     * @param info
     */
    public void setInfo(T info){
        this.info = info;
    }
    
    /** Devuelve el nodo siguiente enlazado al actual. 
     * @return
     */
    public Nodo <T> getSiguiente(){
        return pNext;
    }
    
    /** Asigna cuál será el nodo siguiente al actual. 
     * @param siguiente
     */
    public void setSiguiente(Nodo <T> siguiente){
        this.pNext = siguiente;
    }
}
