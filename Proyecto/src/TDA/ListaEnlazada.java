/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TDA;

/**
 *
 * @author diegoarvelo
 */
public class ListaEnlazada <T> {
    private Nodo <T> pFirst;
    private Nodo <T> pLast;
    private int iN;
    private String name;
    
    public ListaEnlazada(){
        this("");
    }
    
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
    
    public String getName(){
        return name;
    }
    
    public int Tamano(){
        return this.iN;
    }
    
    public boolean EsVacio(){
        return this.pFirst == null;
    }
    
    public Nodo<T> Primero(){
        return this.pFirst;
    }
    
    public Nodo<T> Ultimo(){
        return null;
    }
    
    public Nodo <T> Final(){
        return this.pLast;
    }
    
    public T Leer(Nodo<T> pValor){
        if (pValor == null){
            return null;
        }
        else{
            return pValor.info;
        }
    }
    
    public Nodo<T> Proximo(Nodo<T> pValor){
        if (pValor != Ultimo()){
            return pValor.pNext;
        }
        else{
            return this.Ultimo();
        }
    }
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
   
   public void Limpiar(){
       this.pFirst = null;
       this.pLast = null;
       this.iN = 0;
   }
}
