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
}
