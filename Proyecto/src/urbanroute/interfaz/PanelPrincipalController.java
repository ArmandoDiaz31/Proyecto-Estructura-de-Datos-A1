package urbanroute.interfaz;

import javax.swing.JOptionPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import urbanroute.archivos.GestorArchivo;
import urbanroute.modelo.Estacion;
import TDA.Grafo;
import urbanroute.modelo.AnalisisRed;

/**
 *
 * @author josea
 */
public class PanelPrincipalController {
    private Grafo grafo;
    private AnalisisRed analisis;
    private GestorArchivo gestor;
    private File archivoEstaciones;
    private File archivoConexiones;
    private boolean cambiosSinGuardar;

    // Ventana y componentes que vienen de PanelPrincipal
    private final javax.swing.JFrame ventana;
    private final GrafoPanel grafoPanel1;
    private final javax.swing.JComboBox<Estacion> cbOrigen;
    private final javax.swing.JComboBox<Estacion> cbDestino;
    private final javax.swing.JTextArea areaResults;
    private final javax.swing.JLabel labelState;
    private final javax.swing.JButton btRuta;
    private final javax.swing.JButton btSubredes;
    private final javax.swing.JButton btHubs;
    private final javax.swing.JButton btClear;
    private final javax.swing.JMenuItem itemUpload;
    private final javax.swing.JMenuItem itemSave;
    private final javax.swing.JMenuItem itemExit;
    private final javax.swing.JMenuItem itemAddStation;
    private final javax.swing.JMenuItem itemAddConection;
    private final javax.swing.JMenuItem itemDeleteStation;
    private final javax.swing.JMenuItem itemSubNets;
    private final javax.swing.JMenuItem itemRoute;
    private final javax.swing.JMenuItem itemHubs;
    private final javax.swing.JMenuItem itemHelp;

    public PanelPrincipalController(javax.swing.JFrame ventana,
            GrafoPanel grafoPanel1,
            javax.swing.JComboBox<Estacion> cbOrigen,
            javax.swing.JComboBox<Estacion> cbDestino,
            javax.swing.JTextArea areaResults,
            javax.swing.JLabel labelState,
            javax.swing.JButton btRuta,
            javax.swing.JButton btSubredes,
            javax.swing.JButton btHubs,
            javax.swing.JButton btClear,
            javax.swing.JMenuItem itemUpload,
            javax.swing.JMenuItem itemSave,
            javax.swing.JMenuItem itemExit,
            javax.swing.JMenuItem itemAddStation,
            javax.swing.JMenuItem itemAddConection,
            javax.swing.JMenuItem itemDeleteStation,
            javax.swing.JMenuItem itemSubNets,
            javax.swing.JMenuItem itemRoute,
            javax.swing.JMenuItem itemHubs,
            javax.swing.JMenuItem itemHelp) {
        this.ventana = ventana;
        this.grafoPanel1 = grafoPanel1;
        this.cbOrigen = cbOrigen;
        this.cbDestino = cbDestino;
        this.areaResults = areaResults;
        this.labelState = labelState;
        this.btRuta = btRuta;
        this.btSubredes = btSubredes;
        this.btHubs = btHubs;
        this.btClear = btClear;
        this.itemUpload = itemUpload;
        this.itemSave = itemSave;
        this.itemExit = itemExit;
        this.itemAddStation = itemAddStation;
        this.itemAddConection = itemAddConection;
        this.itemDeleteStation = itemDeleteStation;
        this.itemSubNets = itemSubNets;
        this.itemRoute = itemRoute;
        this.itemHubs = itemHubs;
        this.itemHelp = itemHelp;
    }

    public void iniciar() {
        grafo = new Grafo();
        analisis = new AnalisisRed(grafo);
        gestor = new GestorArchivo();
        conectarEventos();
        cargarMaestroInicial();
        refresh();
    }

    private void conectarEventos(){
    //Menú Archivo
    itemUpload.addActionListener(e -> accionCargar());
    itemSave.addActionListener(e -> accionGuardar());
    itemExit.addActionListener(e -> confirmSalida());
    
    //Menú Red
    itemAddStation.addActionListener(e -> accionAgregarEstacion());
    itemAddConection.addActionListener(e -> accionAgregarConexion());
    itemDeleteStation.addActionListener(e -> accionEliminarEstacion());
    
    //Menú Análisis
    itemSubNets.addActionListener(e -> accionSubredes());
    itemRoute.addActionListener(e -> accionRutaOptima());
    itemHubs.addActionListener(e -> accionHubs());
    
    //Menú Ayuda
    itemHelp.addActionListener(e -> accionAyuda());
    
    //Botones del panel derecho
    btRuta.addActionListener (e -> accionRutaOptima());
    btSubredes.addActionListener (e -> accionSubredes());
    btHubs.addActionListener (e -> accionHubs());
    btClear.addActionListener (e -> accionLimpiar());
    
    ventana.addWindowListener(new WindowAdapter() {
    @Override
    public void windowClosing(WindowEvent e) {
        confirmSalida();
    }
});
    
    }
    
    private void aviso(String mensaje) {
    JOptionPane.showMessageDialog(ventana, mensaje);
    }
    
    private void accionCargar() {
    if (cambiosSinGuardar) {
        int r = JOptionPane.showConfirmDialog(ventana,
                "Hay cambios sin guardar. ¿Desea guardarlos antes de cargar otros archivos?",
                "Cambios sin guardar",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (r == JOptionPane.CANCEL_OPTION || r == JOptionPane.CLOSED_OPTION) {
            return;
        }
        if (r == JOptionPane.YES_OPTION) {
            accionGuardar();
        }
    }

    File est = chooseFile("Seleccione el archivo de ESTACIONES");
    if (est == null) {
        return;
    }
    File con = chooseFile("Seleccione el archivo de CONEXIONES");
    if (con == null) {
        return;
    }
    loadFrom(est, con);
}
    
    private void accionGuardar(){
    aviso("Guardar cambios(Pendiente)");
    }
    
    private void confirmSalida(){
    ventana.dispose();
    System.exit(0);
    }
    
    private void accionAgregarEstacion(){
    aviso("Agregar estacion(Pendiente)");
    }
    
    private void accionAgregarConexion(){
    aviso("Agregar conexion(Pendiente)");
    }
    
    private void accionEliminarEstacion(){
    aviso("Eliminar estacion(Pendiente)");
    }
    
    private void accionSubredes(){
    areaResults.setText("Subredes (Pendiente)");
    }
    
    private void accionRutaOptima(){
    areaResults.setText("Ruta mas corta (Pendiente)");
    }
    
    private void accionHubs(){
    areaResults.setText("Hubs (Pendiente)");
    }
    
    private void accionAyuda(){
    aviso("Ayuda (Pendiente)");
    }
    
    private void accionLimpiar(){
        areaResults.setText("");
        grafoPanel1.clearMarcar();
    
    }
    private void llenarCombos() {
    cbOrigen.removeAllItems();
    cbDestino.removeAllItems();
    for (int i = 0; i < grafo.getCantidadEstaciones() ; i++) {                    
        Estacion e = grafo.ObtenerEstacionPorIndice(i);
        cbOrigen.addItem( e );
        cbDestino.addItem( e);
    }
}
    
    private void loadOrigin() {
    File est = gestor.getArchivoEstacionesMaestro();
    File con = gestor.getArchivoConexionesMaestro();
    if (!est.exists() || !con.exists()) {
        showAdvice("No se encontró el archivo maestro; use Archivo > Cargar");
        return;
    }
    loadFrom(est, con);
}
    private void showError(String titulo, String mensaje) {
    JOptionPane.showMessageDialog(ventana, mensaje, titulo, JOptionPane.ERROR_MESSAGE);
}

private void showAdvice(String mensaje) {
    JOptionPane.showMessageDialog(ventana, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
}

private void showAdvice(String titulo, String mensaje) {
    Object content = mensaje;
    if (mensaje != null && mensaje.length() > 200) {
        javax.swing.JTextArea texto = new javax.swing.JTextArea(mensaje);
        texto.setEditable(false);
        javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(texto);
        scroll.setPreferredSize(new java.awt.Dimension(520, 260));
        content = scroll;
    }
     JOptionPane.showMessageDialog(ventana, content, titulo, JOptionPane.WARNING_MESSAGE);
}

    private void refresh() {
        grafoPanel1.draw(grafo);            
        llenarCombos();                        
        boolean hayRed = grafo.getCantidadEstaciones() > 0 ;                  
        btRuta.setEnabled(hayRed);
        btSubredes.setEnabled(hayRed);
        btHubs.setEnabled(hayRed);
        labelState.setText(grafo.getCantidadEstaciones() + " estaciones | "
        + grafo.getCantidadConexiones() + " conexiones"
        + (cambiosSinGuardar ? " | cambios sin guardar" : ""));   
    }
    
    private void saveChange() {
    cambiosSinGuardar = true;
    refresh();
    }
    
}