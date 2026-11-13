/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package urbanroute.modelo;

/**
 *
 * @author diegoarvelo
 */
public class ResultadoRuta{
    private final Estacion[] Camino;
    private final int TiempoTotal;
    private final boolean ExisteRuta;
    
    public ResultadoRuta(Estacion[] Camino, int TiempoTotal, boolean ExisteRuta){
            this.Camino = Camino;
            this.TiempoTotal= TiempoTotal;
            this.ExisteRuta = ExisteRuta;
        }
        
    public Estacion[] getCamino(){
            return Camino;
        }
        
    public int getTiempoTotal(){
            return TiempoTotal;
        }
        
    public boolean esExisteRuta(){
            return ExisteRuta;
        }
    }
