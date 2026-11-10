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
    private int iN;
    private String name;
    
    public Lista (String s){
        this.name = s;
        this.pFirst = null;
        this.iN = 0;
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
    
    public T Leer(Nodo<T> pValor){
        return pValor.info;
    }
    
    public Nodo<T> Ultimo(){
        return null;
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
        Nodo <T> pNew =new Nodo<> (x);
        if (this.EsVacio()){
            this.pFirst=pNew;
            iN++;
        }
        else{
            if (pValor != this.Ultimo()){
                pNew.pNext=pValor.pNext;
                pValor.pNext=pNew;
                this.iN++;
            }
        }
    }
    public Nodo<T> Final(){
        if (this.EsVacio()){
            return null;
        }
        else{
            Nodo<T> pAux = this.Primero();
            while (pAux.pNext != this.Ultimo()){
                pAux = this.Proximo(pAux);
            }
            return pAux;
        }
    }
}
