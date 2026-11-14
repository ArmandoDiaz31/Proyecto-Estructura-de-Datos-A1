/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TDA;

/**
 * Lista simplemente enlazada genérica desarrollada desde cero para almacenar
 * y recorrer colecciones dinámicas de elementos.
 * @author diegoarvelo
 */
public class ListaEnlazada <T> {
    /** Referencia al primer nodo de la lista. */
    private Nodo <T> pFirst;
    /** Referencia al último nodo de la lista para inserciones rápidas al final. */
    private Nodo <T> pLast;
    /** Cantidad actual de elementos guardados en la lista. */
    private int iN;
    /** Nombre descriptivo asignado a la lista. */
    private String name;
    
    /** Crea una lista enlazada vacía sin nombre. */
    public ListaEnlazada(){
        this("");
    }
    
    /** Crea una lista enlazada vacía asignándole el nombre recibido. 
     * @param s
     */
    public ListaEnlazada (String s){
        if (s == null){
            this.name = "";
        }
        else{
            this.name = s;
        }
        this.pFirst = null;
        this.pLast = null;
        this.iN = 0;
    }
    
    /** Devuelve el nombre descriptivo de la lista. 
     * @return
     */
    public String getName(){
        return name;
    }
    
    /** Devuelve la cantidad total de elementos en la lista. 
     * @return
     */
    public int Tamano(){
        return this.iN;
    }
    
    /** Indica si la lista no tiene elementos. 
     * @return
     */
    public boolean EsVacio(){
        return this.pFirst == null;
    }
    
    /** Devuelve el primer nodo de la lista. 
     * @return
     */
    public Nodo<T> Primero(){
        return this.pFirst;
    }
    
    /** Devuelve nulo para representar el final de un recorrido. 
     * @return
     */
    public Nodo<T> Ultimo(){
        return null;
    }
    
    /** Devuelve el último nodo físico almacenado en la lista. 
     * @return
     */
    public Nodo <T> Final(){
        return this.pLast;
    }
    
    /** Devuelve el dato guardado dentro del nodo indicado si no es nulo. 
     * @return
     * @param pValor
     */
    public T Leer(Nodo<T> pValor){
        if (pValor == null){
            return null;
        }
        else{
            return pValor.info;
        }
    }
    
    /** Devuelve el nodo siguiente al nodo recibido por parámetro. 
     * @param pValor
     * @return
     */
    public Nodo<T> Proximo(Nodo<T> pValor){
        if (pValor != Ultimo()){
            return pValor.pNext;
        }
        else{
            return this.Ultimo();
        }
    }
    
    /** Inserta un elemento nuevo justo después del nodo de referencia indicado. 
     * @return
     * @param pValor
     */
    public void Insertar(T x, Nodo<T> pValor){
        if (pValor == Ultimo() || this.EsVacio()){
            InsertarAlFinal(x);
            return;
        }
        Nodo <T> pNew = new Nodo<>(x);
        pNew.pNext = pValor.pNext;
        pValor.pNext = pNew;
        if (pValor == this.pLast){
            this.pLast = pNew;
        }
        this.iN++;
    }
    
    /** Agrega un elemento nuevo directamente al final de la lista. 
     * @param x
     */
    public void InsertarAlFinal(T x){
       Nodo <T> pNew = new Nodo<>(x);
       if (this.pFirst == null){
           this.pFirst = pNew;
           this.pLast = pNew;
       }
       else{
           this.pLast.pNext = pNew;
           this.pLast = pNew;
       }
       this.iN++;
   }
   
    /** Busca y elimina la primera aparición del dato indicado dentro de la lista
     * y retorna si se logra eliminar el dato o no.
     * @param x
     * @return
     */
    public boolean Eliminar(T x){
       Nodo <T> pAnt = null;
       Nodo <T> pAux = this.pFirst;
       
       while (pAux != null){
           boolean coincide = false;
           if (pAux.info == x){
               coincide = true;
           }
           else if (x != null){
               if (x.equals(pAux.info)){
                   coincide = true;
               }
           }
           
           if (coincide){
               if (pAnt == null){
                   this.pFirst = pAux.pNext;
               }
               else {
                   pAnt.pNext = pAux.pNext;
               }
               if (pAux == this.pLast){
                   this.pLast = pAnt;
               }
               this.iN--;
               return true;
           }
           pAnt = pAux;
           pAux = pAux.pNext;
       }
       return false;
   }
   
    /** Vacía la lista por completo reiniciando sus punteros y su contador. */
    public void Limpiar(){
       this.pFirst = null;
       this.pLast = null;
       this.iN = 0;
   }
}
