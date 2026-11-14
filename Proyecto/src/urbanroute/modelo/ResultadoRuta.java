/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package urbanroute.modelo;

/**
 * Almacena el resultado del cálculo de una ruta óptima, incluyendo las estaciones
 * del recorrido, el tiempo total en minutos y si existe conexión válida.
 * @author diegoarvelo
 */
public class ResultadoRuta{
    /** Arreglo ordenado con las estaciones que conforman el trayecto de origen a destino. */
    private final Estacion[] Camino;
    /** Duración total acumulada del recorrido en minutos. */
    private final int TiempoTotal;
    /** Indica si se encontró un camino válido entre las dos estaciones consultadas. */
    private final boolean ExisteRuta;
    
    /** Crea el resultado de la ruta guardando el camino recorrido, el tiempo total y su validez. */
    public ResultadoRuta(Estacion[] Camino, int TiempoTotal, boolean ExisteRuta){
            this.Camino = Camino;
            this.TiempoTotal= TiempoTotal;
            this.ExisteRuta = ExisteRuta;
        }
        
    /** Devuelve el arreglo de estaciones que integran el camino calculado. */
    public Estacion[] getCamino(){
            return Camino;
        }
    
    /** Devuelvel tiempo total estimado del trayecto en minutos. */
    public int getTiempoTotal(){
            return TiempoTotal;
        }
    
    /** Indica si existe una ruta posible entre el origen y el destino. */
    public boolean esExisteRuta(){
            return ExisteRuta;
        }
    }
