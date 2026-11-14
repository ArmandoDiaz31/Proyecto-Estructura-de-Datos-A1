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
        btRuta.setToolTipText("Calcula el trayecto de menor tiempo entre origen y destino");
        btSubredes.setToolTipText("Muestra los grupos de estaciones conectadas entre sí");
        btHubs.setToolTipText("Resalta las estaciones con más conexiones");
        btClear.setToolTipText("Quita los colores y vacía los resultados");
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
    
    private boolean actionSave() {
    File est = archivoEstaciones;
    File con = archivoConexiones;

    if (est == null || con == null) {
        est = chooseSaveFile("Guardar estaciones como...");
        if (est == null) {
            return false;
        }
        con = chooseSaveFile("Guardar conexiones como...");
        if (con == null) {
            return false;
        }
    } else {
        int r = JOptionPane.showConfirmDialog(ventana,
                "Se sobrescribirán " + est.getName() + " y " + con.getName() + ". Continuar?",
                "Guardar cambios",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (r != JOptionPane.YES_OPTION) {
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

    
    private void confirmExit() {
    if (cambiosSinGuardar) {
        int r = JOptionPane.showConfirmDialog(ventana,
                "Hay cambios sin guardar. ¿Desea guardarlos antes de salir?",
                "Cambios sin guardar",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (r == JOptionPane.CANCEL_OPTION || r == JOptionPane.CLOSED_OPTION) {
            return;
        }
        if (r == JOptionPane.YES_OPTION && !actionSave()) {
            return;
        }
        // Si responde No, sale sin guardar
    }
    ventana.dispose();
    System.exit(0);
}
    
   private void actionAddStation() {
    javax.swing.JTextField txtId = new javax.swing.JTextField();
    javax.swing.JTextField txtNombre = new javax.swing.JTextField();
    javax.swing.JTextField txtLinea = new javax.swing.JTextField();
    javax.swing.JTextField txtZona = new javax.swing.JTextField();
    javax.swing.JTextField txtCap = new javax.swing.JTextField();

    Object[] campos = {
        "ID:", txtId,
        "Nombre:", txtNombre,
        "Línea:", txtLinea,
        "Zona:", txtZona,
        "Capacidad:", txtCap
    };
    int r = JOptionPane.showConfirmDialog(ventana, campos, "Agregar estación",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (r != JOptionPane.OK_OPTION) {
        return;
    }

    String id = txtId.getText().trim();
    String nombre = txtNombre.getText().trim();
    String linea = txtLinea.getText().trim();
    String zona = txtZona.getText().trim();

    if (id.isEmpty()) {
        showError("Dato inválido", "El ID no puede estar vacío.");
        return;
    }
   
    if (id.contains(",") || nombre.contains(",") || linea.contains(",") || zona.contains(",")) {
        showError("Dato inválido", "Los campos no pueden contener comas.");
        return;
    }

    int cap;
    try {
        cap = Integer.parseInt(txtCap.getText().trim());
    } catch (NumberFormatException ex) {
        showError("Dato inválido", "La capacidad debe ser un número entero");
        return;
    }

    try {
        grafo.AgregarEstacion(new Estacion(id, nombre, linea, zona, cap));
    } catch (IllegalArgumentException ex) {
        showError("No se pudo agregar", ex.getMessage());
        return;
    }
    saveChange();
}
    
    private void actionAddConnection() {
    if (grafo.getCantidadEstaciones() < 2) {
        showAdvice("Aviso", "Se necesitan al menos 2 estaciones para crear una conexión.");
        return;
    }

    javax.swing.JComboBox<Estacion> cbO =
            new javax.swing.JComboBox<>(grafo.ObtenerTodasLasEstaciones());
    javax.swing.JComboBox<Estacion> cbD =
            new javax.swing.JComboBox<>(grafo.ObtenerTodasLasEstaciones());
    javax.swing.JTextField txtTiempo = new javax.swing.JTextField();

    Object[] campos = {
        "Origen:", cbO,
        "Destino:", cbD,
        "Tiempo (minutos):", txtTiempo
    };
    int r = JOptionPane.showConfirmDialog(ventana, campos, "Agregar conexión",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (r != JOptionPane.OK_OPTION) {
        return;
    }

    Estacion o = (Estacion) cbO.getSelectedItem();
    Estacion d = (Estacion) cbD.getSelectedItem();

    int tiempo;
    try {
        tiempo = Integer.parseInt(txtTiempo.getText().trim());
    } catch (NumberFormatException ex) {
        showError("Dato inválido", "El tiempo debe ser un número entero");
        return;
    }

    try {
        grafo.AgregarConexion(o.getId(), d.getId(), tiempo);
    } catch (IllegalArgumentException ex) {
        showError("No se pudo agregar", ex.getMessage());
        return;
    }
    saveChange();
}

    
    private void actionDeleteStation() {
    if (grafo.getCantidadEstaciones() == 0) {
        showAdvice("Aviso", "No hay estaciones para eliminar.");
        return;
    }

    javax.swing.JComboBox<Estacion> cb =
            new javax.swing.JComboBox<>(grafo.ObtenerTodasLasEstaciones());
    Object[] campos = { "Estación a eliminar:", cb };
    int r = JOptionPane.showConfirmDialog(ventana, campos, "Eliminar estación",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (r != JOptionPane.OK_OPTION) {
        return;
    }

    Estacion e = (Estacion) cb.getSelectedItem();
    int n = grafo.ObtenerGrado(e.getId());
    int c = JOptionPane.showConfirmDialog(ventana,
            "Se eliminará " + e.getNombre() + " y sus " + n + " conexiones. ¿Continuar?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    if (c != JOptionPane.YES_OPTION) {
        return;
    }

    grafo.EliminarEstacion(e.getId());
    saveChange();
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

    grafoPanel1.resaltRuta(r);

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
    grafoPanel1.resaltHubs(hubs);
    }
    
    private void actionHelp() {
    String texto =
        """
        Cargar archivos:
          Archivo > Cargar Archivos. Elija primero el CSV de estaciones y luego el de conexiones.
        
        Agregar o eliminar (men\u00fa Red):
          Agregar Estaci\u00f3n: ID, nombre, l\u00ednea, zona y capacidad (sin comas).
          Agregar Conexi\u00f3n: elija origen y destino y escriba el tiempo en minutos.
          Eliminar Estaci\u00f3n: tambi\u00e9n elimina sus conexiones.
        
        An\u00e1lisis (men\u00fa An\u00e1lisis o botones del panel):
          Subredes: muestra los grupos de estaciones conectadas entre s\u00ed.
          Ruta m\u00e1s corta: calcula el trayecto de menor tiempo entre origen y destino.
          Hubs de transferencia: resalta las estaciones con m\u00e1s conexiones.
        
        Colores:
          Rojo: ruta m\u00e1s corta.
          Naranja: hubs de transferencia.
          Un color distinto por cada subred.
          Limpiar resultado quita los colores y vac\u00eda el texto.
        
        Guardar:
          Archivo > Guardar Archivo. Sobrescribe los CSV cargados y avisa antes de hacerlo.
          Al salir con cambios pendientes se pregunta si desea guardar.""";
    JOptionPane.showMessageDialog(ventana, texto, "Cómo usar UrbanRoute",
            JOptionPane.INFORMATION_MESSAGE);
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