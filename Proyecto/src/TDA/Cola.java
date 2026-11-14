/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TDA;

/**
 * Estructura de datos tipo cola implementada mediante nodos enlazados
 * para procesar elementos en orden de llegada.
 * @author diegoarvelo
 */
public class Cola <T> {
    /** Primer nodo de la cola, de donde se extraen los elementos. */
    private Nodo <T> Cabeza;
    /** Último nodo de la cola, por donde se insertan nuevos elementos. */
    private Nodo <T> Cola;
    /** Cantidad actual de elementos encolados. */
    private int iN;
    
    /** Crea una cola vacía inicializando sus punteros y tamaño en cero. */
    public Cola(){
        this.Cabeza = null;
        this.Cola = null;
        this.iN = 0;
    }
    
    /** Inserta un elemento nuevo al final de la cola. 
     * @param x
     */
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
    
    /** Extrae y devuelve el elemento ubicado al frente de la cola. 
     * @return
     */
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
    
    /** Indica si la cola no contiene ningún elemento. 
     * @return
     */
    public boolean EsVacio(){
        return Cabeza == null;
    }
    
    /** Devuelve la cantidad de elementos almacenados en la cola. 
     * @return
     */
    public int Tamano(){
        return iN;
    }
}
