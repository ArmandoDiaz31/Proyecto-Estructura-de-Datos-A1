package urbanroute.interfaz;

import javax.swing.JOptionPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import urbanroute.archivos.GestorArchivo;
import urbanroute.modelo.Estacion;
import TDA.Grafo;
import urbanroute.modelo.AnalisisRed;
import urbanroute.modelo.ResultadoRuta;

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

    public void start() {
        grafo = new Grafo();
        analisis = new AnalisisRed(grafo);
        gestor = new GestorArchivo();
        connectEvents();
        loadOrigin();
        refresh();
    }

    private void connectEvents(){
    //Menú Archivo
    itemUpload.addActionListener(e -> actionUpload());
    itemSave.addActionListener(e -> actionSave());
    itemExit.addActionListener(e -> confirmExit());
    
    //Menú Red
    itemAddStation.addActionListener(e -> actionAddStation());
    itemAddConection.addActionListener(e -> actionAddConnection());
    itemDeleteStation.addActionListener(e -> actionDeleteStation());
    
    //Menú Análisis
    itemSubNets.addActionListener(e -> actionSubnets());
    itemRoute.addActionListener(e -> actionRoute());
    itemHubs.addActionListener(e -> actionHubs());
    
    //Menú Ayuda
    itemHelp.addActionListener(e -> actionHelp());
    
    //Botones del panel derecho
    btRuta.addActionListener (e -> actionRoute());
    btSubredes.addActionListener (e -> actionSubnets());
    btHubs.addActionListener (e -> actionHubs());
    btClear.addActionListener (e -> actionClear());
    
    ventana.addWindowListener(new WindowAdapter() {
    @Override
    public void windowClosing(WindowEvent e) {
        confirmExit();
    }
});
    
    }
    
    private void advise(String mensaje) {
    JOptionPane.showMessageDialog(ventana, mensaje);
    }
    
    private void actionUpload() {
    if (cambiosSinGuardar) {
        int r = JOptionPane.showConfirmDialog(ventana,
                "Hay cambios sin guardar. ¿Desea guardarlos antes de cargar otros archivos?",
                "Cambios sin guardar",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (r == JOptionPane.CANCEL_OPTION || r == JOptionPane.CLOSED_OPTION) {
            return;
        }
           if (r == JOptionPane.YES_OPTION && !actionSave()) {
            return;
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
    
    private boolean actionSave(){
     File est = archivoEstaciones;
        File con = archivoConexiones;
 
        
        if (est == null || con == null) {
            est = chooseSaveFile("Guardar ESTACIONES como...");
            if (est == null) {
                return false;
            }
            con = chooseSaveFile("Guardar CONEXIONES como...");
            if (con == null) {
                return false;
            }
        }
 
        try {
            gestor.guardarEstaciones(grafo, est);
            gestor.guardarConexiones(grafo, con);
        } catch (java.io.IOException | RuntimeException ex) {
            showError("No se pudo guardar", ex.getMessage());
            return false;
    }
          archivoEstaciones = est;
        archivoConexiones = con;
        cambiosSinGuardar = false;
        refresh();
        return true;
    }

    
    private void confirmExit(){
    ventana.dispose();
    System.exit(0);
    }
    
    private void actionAddStation(){
    advise("Agregar estacion(Pendiente)");
    }
    
    private void actionAddConnection(){
    advise("Agregar conexion(Pendiente)");
    }
    
    private void actionDeleteStation(){
    advise("Eliminar estacion(Pendiente)");
    }
    
    private void actionSubnets(){
      Estacion[][] subredes = analisis.DetectarSubredes();
    StringBuilder sb = new StringBuilder("Subredes encontradas: " + subredes.length + "\n");
    for (int s = 0; s < subredes.length; s++) {
        sb.append("Subred ").append(s + 1).append(": ");
        for (Estacion e : subredes[s]) {
            sb.append(e.getId()).append(" ");
        }
        sb.append("\n");
    }
    areaResults.setText(sb.toString());
    grafoPanel1.clearMarcar();
    grafoPanel1.colorSubredes(subredes);
    }
    
    private void actionRoute(){
     Estacion o = (Estacion) cbOrigen.getSelectedItem();
    Estacion d = (Estacion) cbDestino.getSelectedItem();
    if (o == null || d == null) {
        showAdvice("Aviso", "Seleccione una estación de origen y una de destino.");
        return;
    }

    ResultadoRuta r;
    try {
        r = analisis.CalcularRutaMasCorta(o.getId(), d.getId());
    } catch (IllegalArgumentException ex) {
        showError("No se pudo calcular la ruta", ex.getMessage());
        return;
    }

    if (!r.esExisteRuta()) {
        areaResults.setText("No existe ruta entre " + o.getNombre() + " y " + d.getNombre());
        grafoPanel1.clearMarcar();
        return;
    }

    grafoPanel1.highlightRoute(r);

    Estacion[] camino = r.getCamino();
    StringBuilder sb = new StringBuilder();
    sb.append("Ruta más corta: ").append(r.getTiempoTotal()).append(" min\n");
    for (int k = 0; k < camino.length; k++) {
        sb.append(camino[k].getNombre());
        if (k < camino.length - 1) {
            sb.append(" -> ");
        }
    }
    areaResults.setText(sb.toString());
    }
    
    private void actionHubs(){
     Estacion[] ranking = analisis.IdentificarHubs();
    if (ranking.length == 0) {
        showAdvice("Aviso", "No hay estaciones cargadas.");
        return;
    }

    int max = grafo.ObtenerGrado(ranking[0].getId());
    if (max == 0) {
        showAdvice("Aviso", "No hay conexiones, por lo que no hay hubs.");
        return;
    }

    // El ranking viene de mayor a menor grado: los empates quedan al principio
    int cuenta = 0;
    while (cuenta < ranking.length
            && grafo.ObtenerGrado(ranking[cuenta].getId()) == max) {
        cuenta++;
    }
    Estacion[] hubs = new Estacion[cuenta];
    StringBuilder nombres = new StringBuilder();
    for (int i = 0; i < cuenta; i++) {
        hubs[i] = ranking[i];
        nombres.append(ranking[i].getNombre());
        if (i < cuenta - 1) {
            nombres.append(", ");
        }
    }

    areaResults.setText("Hubs de transferencia (grado " + max + "): " + nombres);
    grafoPanel1.highlightHubs(hubs);
    }
    
    private void actionHelp(){
    advise("Ayuda (Pendiente)");
    }
    
    private void actionClear(){
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
        showAdvice("Aviso", "No se encontró el archivo maestro; use Archivo > Cargar");
        return;
    }
    loadFrom(est, con);
}
    private void showError(String titulo, String mensaje) {
    JOptionPane.showMessageDialog(ventana, mensaje, titulo, JOptionPane.ERROR_MESSAGE);
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
private File chooseFile(String titulo) {
    javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
    fc.setDialogTitle(titulo);
    File datos = new File("datos");
    if (datos.isDirectory()) {
        fc.setCurrentDirectory(datos);
    }
    fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Archivos CSV (*.csv)", "csv"));
    if (fc.showOpenDialog(ventana) == javax.swing.JFileChooser.APPROVE_OPTION) {
        return fc.getSelectedFile();
    }
    return null;
}

private File chooseSaveFile(String titulo) {
    javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
    fc.setDialogTitle(titulo);
    File datos = new File("datos");
    if (datos.isDirectory()) {
        fc.setCurrentDirectory(datos);
    }
    fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Archivos CSV (*.csv)", "csv"));
    if (fc.showSaveDialog(ventana) != javax.swing.JFileChooser.APPROVE_OPTION) {
        return null;
    }
    File f = fc.getSelectedFile();
    if (!f.getName().toLowerCase().endsWith(".csv")) {
        f = new File(f.getParentFile(), f.getName() + ".csv");
    }
    if (f.exists()) {
        int r = JOptionPane.showConfirmDialog(ventana,
                "El archivo " + f.getName() + " ya existe. ¿Desea reemplazarlo?",
                "Reemplazar archivo",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (r != JOptionPane.YES_OPTION) {
            return null;
        }
    }
    return f;
}

private void loadFrom(File estaciones, File conexiones) {
    Grafo nuevo = new Grafo();
    gestor.reiniciarResultados();
    try {
        gestor.cargarEstaciones(estaciones, nuevo);
        gestor.cargarConexiones(conexiones, nuevo);
    } catch (java.io.IOException | RuntimeException ex) {
        showError("No se pudo leer el archivo", ex.getMessage());
        return;
    }

    grafo = nuevo;
    analisis = new AnalisisRed(grafo);
    archivoEstaciones = estaciones;
    archivoConexiones = conexiones;
    cambiosSinGuardar = false;
    areaResults.setText("");
    refresh();

    if (gestor.getLineasConError() > 0) {
        showAdvice("Se ignoraron " + gestor.getLineasConError() + " línea(s) inválida(s)",
                gestor.getErrores());
    }
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