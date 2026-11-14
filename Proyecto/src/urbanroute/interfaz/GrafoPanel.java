package urbanroute.interfaz;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */



import urbanroute.modelo.Estacion;
import TDA.Grafo;
import urbanroute.modelo.ResultadoRuta;



/**
 * Panel que dibuja la red con GraphStream. Permite colorear las subredes y
 * resaltar la ruta más corta y los hubs de transferencia.
 *
 * @author José Palomo
 */
public class GrafoPanel extends javax.swing.JPanel {
    /** Grafo de GraphStream con los nodos y aristas que se dibujan. */
   private org.graphstream.graph.Graph visual;
    /** Componente de GraphStream donde se muestra el grafo. */
   private org.graphstream.ui.swing_viewer.ViewPanel vista;
 
    /**
     * Asigna el layout y, si es BorderLayout, vuelve a agregar la vista en el centro
     * para que se dibuje.
     *
     * @param mgr nuevo administrador de diseño
     */
    @Override
    public void setLayout(java.awt.LayoutManager mgr) {
        super.setLayout(mgr);
        if (vista != null && mgr instanceof java.awt.BorderLayout) {
            add(vista, java.awt.BorderLayout.CENTER);
        }
    }
        
    
   /** Crea el panel, configura GraphStream con el estilo de la red y agrega la vista. */ 
public GrafoPanel() {
    initComponents();
 
    System.setProperty("org.graphstream.ui", "swing");
    setLayout(new java.awt.BorderLayout());
 
    visual = new org.graphstream.graph.implementations.SingleGraph("UrbanRoute");
    
   visual.setAttribute("ui.stylesheet", style);
 
    org.graphstream.ui.swing_viewer.SwingViewer visor =
        new org.graphstream.ui.swing_viewer.SwingViewer(visual,
            org.graphstream.ui.view.Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
    visor.enableAutoLayout();
 
    vista = (org.graphstream.ui.swing_viewer.ViewPanel) visor.addDefaultView(false);
    add(vista, java.awt.BorderLayout.CENTER);
    

}
  /** Cantidad de colores de subred definidos en la hoja de estilos (sub0 a sub5). */
private static final String[] colors = {"#E74C3C", "#3498DB", "#2ECC71", "#F39C12", "#9B59B6", "#1ABC9C"};

/**
     * Colorea cada subred asignando a sus nodos la clase sub0, sub1, etc.
     * Los colores se repiten si hay más de seis subredes.
     *
     * @param subredes estaciones agrupadas por subred
     */
public void colorSubredes(Estacion[][] subredes) {
    for (int i = 0; i < subredes.length; i++) {
        for (Estacion e : subredes[i]) {
            org.graphstream.graph.Node n = visual.getNode(e.getId());
            if (n != null) {
                n.setAttribute("ui.class", "sub" + (i % colors.length));
            }
        }
    }
}

    /**
     * Asigna una clase de estilo a un nodo, si existe.
     *
     * @param id identificador de la estación
     * @param clase nombre de la clase de estilo (por ejemplo ruta o hub)
     */
public void marcarNodo(String id, String clase) {
    if (visual.getNode(id) != null) {
        visual.getNode(id).setAttribute("ui.class", clase);
    }
}

    /**
     * Asigna una clase de estilo a una arista, si existe.
     *
     * @param id identificador de la arista (menor-mayor, por índices)
     * @param clase nombre de la clase de estilo
     */
public void marcarArista(String id, String clase) {
    if (visual.getEdge(id) != null) {
        visual.getEdge(id).setAttribute("ui.class", clase);
    }
}
/** Hoja de estilos del grafo: nodos, aristas y las clases ruta, hub y sub0 a sub5. */
private static final String style =
      "node { fill-color: #2E86C1; size: 22px; text-size: 14; text-alignment: above; text-color: #1B2631; }"
    + "edge { fill-color: #7F8C8D; size: 2px; }"
    + "node.ruta { fill-color: #E74C3C; size: 26px; }"
    + "edge.ruta { fill-color: #E74C3C; size: 4px; }"
    + "node.hub { fill-color: #F39C12; size: 28px; }"    
    + "node.sub0 { fill-color: #9B59B6; }"
    + "node.sub1 { fill-color: #2ECC71; }"
    + "node.sub2 { fill-color: #1ABC9C; }"
    + "node.sub3 { fill-color: #3498DB; }"
    + "node.sub4 { fill-color: #E84393; }"
    + "node.sub5 { fill-color: #A0522D; }";

 /** Quita las clases de estilo de todos los nodos y aristas. */
public void clearMarcar() {
    visual.nodes().forEach(n -> n.removeAttribute("ui.class"));
    visual.edges().forEach(e -> e.removeAttribute("ui.class"));
}

 /**
     * Resalta las estaciones hub.
     *
     * @param hubs estaciones con más conexiones
     */
public void resaltHubs(Estacion[] hubs) {
    clearMarcar();
    for (Estacion h : hubs) {
        marcarNodo(h.getId(), "hub");
    }
}

 /**
     * Resalta las estaciones y los tramos de una ruta.
     *
     * @param r resultado del cálculo de la ruta
     */
public void resaltRuta(ResultadoRuta r) {
    clearMarcar();
    Estacion[] camino = r.getCamino();
    for (int k = 0; k < camino.length; k++) {
        marcarNodo(camino[k].getId(), "ruta");
        if (k < camino.length - 1) {
            int a = camino[k].getIndice();
            int b = camino[k + 1].getIndice();
            marcarArista(Math.min(a, b) + "-" + Math.max(a, b), "ruta");
        }
    }
}

/**
     * Redibuja la red completa: una estación por nodo y una arista por conexión,
     * con el tiempo como etiqueta.
     *
     * @param g red a dibujar
     */
public void draw(Grafo g) {
    visual.clear();
    visual.setAttribute("ui.stylesheet", style);
    int n = g.getCantidadEstaciones();
 
    for (int i = 0; i < n; i++) {
        Estacion e = g.ObtenerEstacionPorIndice(i);
        org.graphstream.graph.Node nodo = visual.addNode( e.getId() );       
        nodo.setAttribute("ui.label", e.getId());                            
    }

    for (int i = 0; i < n; i++) {
        for (int j = i + 1; j < n; j++) {
            int weight = g.getDuracion(i, j);
            if (weight > 0) {
                org.graphstream.graph.Edge arista = visual.addEdge( i + "-" + j, g.ObtenerEstacionPorIndice(i).getId(), g.ObtenerEstacionPorIndice(j).getId() );
                arista.setAttribute("ui.label", weight);
            }
        }
    }
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 618, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
