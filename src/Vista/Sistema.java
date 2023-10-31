/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Modelo.Combo;
import Modelo.Eventos;
import Modelo.ImageRenderer;
import Controlador.LoginDAO;
import Modelo.MateriaPrima;
import Controlador.MateriaPrimaDao;
import Modelo.Producto;
import Controlador.ProductoDao;
import Modelo.Proveedor;
import Controlador.ProveedorDao;
import Modelo.EntradaM;
import Controlador.EntradaMDao;
import Controlador.EntradaPro;
import Modelo.login;
import Reportes.Reportesproveedores;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author USUARIO
 */
public final class Sistema extends javax.swing.JFrame {
    private byte[] imagenSeleccionada = null;
    private File selectedFile;

    Date fechaVenta = new Date();
    String fechaActual = new SimpleDateFormat("dd/MM/yyyy").format(fechaVenta);
    Proveedor pr = new Proveedor();
    ProveedorDao PrDao = new ProveedorDao();
    MateriaPrima mat = new MateriaPrima();
    MateriaPrimaDao matDao = new MateriaPrimaDao();
    Producto pro= new Producto ();
    ProductoDao proDao = new ProductoDao ();
    EntradaM EM = new EntradaM();
    EntradaMDao EMdao = new EntradaMDao();
    EntradaPro EPdao = new EntradaPro();
    Eventos event = new Eventos();
    login lg = new login();
    LoginDAO login = new LoginDAO();
    DefaultTableModel modelo = new DefaultTableModel();
    DefaultTableModel tmp = new DefaultTableModel();
    int item;
    double Totalpagar = 0.00;
    

   public Sistema() {
        initComponents();
    }
    public Sistema (login priv){
        initComponents();
        this.setLocationRelativeTo(null);
        mostrarMateriasPrimasAgotadasEnTabla();
        mostrarProductosAgotadosEnTabla();
        txtIdMateria1.setVisible(false);
        txtIdMateria.setVisible(false);
        txtIdProveedor.setVisible(false);
        if (priv.getRol().equals("Asistente")) {
            btnMateriaPrima.setEnabled(false);
            btnProveedor.setEnabled(false);
            jButton1.setEnabled(false);
            LabelVendedor.setText(priv.getNombre());
        }else{
            LabelVendedor.setText(priv.getNombre());
        }
    }
    
   public void ListarEntraM() {
        List<MateriaPrima> ListarMat = matDao.ListarMateria();
    modelo = (DefaultTableModel) TablaMateria1.getModel();
    Object[] ob = new Object[7]; // Asumiendo que tienes 7 columnas, incluyendo la columna de imagen

    for (int i = 0; i < ListarMat.size(); i++) {
        ob[0] = ListarMat.get(i).getId();
        ob[1] = ListarMat.get(i).getCodigo();
        ob[2] = ListarMat.get(i).getNombre();
        ob[3] = ListarMat.get(i).getProveedorPro();
        ob[4] = ListarMat.get(i).getCantidad();
        ob[5] = ListarMat.get(i).getPrecio();

        // La columna 6 contendrá la imagen
        byte[] imageData = ListarMat.get(i).getImageData();

        if (imageData != null) {
            ob[6] = getImageIconFromBytes(imageData); // Convierte los datos de imagen en un ImageIcon
        } else {
            ob[6] = null; // Si los datos de imagen son nulos, establece la columna de imagen en null
        }

        modelo.addRow(ob);
    }

    TablaMateria1.setModel(modelo);

    // Ajustar el alto de las filas
    int rowHeight = 80; // Cambia este valor según tu preferencia
    TablaMateria1.setRowHeight(rowHeight);

    // Ajustar el ancho de las columnas, incluida la columna de la imagen (columna 6)
    int columnWidth = 100; // Cambia este valor según tu preferencia
    for (int columnIndex = 0; columnIndex < TablaMateria1.getColumnCount(); columnIndex++) {
        TableColumn column = TablaMateria1.getColumnModel().getColumn(columnIndex);
        column.setPreferredWidth(columnWidth);
    }

    // Si deseas que la última columna (imagen) se ajuste al ancho de la tabla, puedes usar esto:
    int lastColumnWidth = TablaMateria1.getWidth() - (TablaMateria1.getColumnModel().getColumnCount() - 1) * columnWidth;
    TablaMateria1.getColumnModel().getColumn(TablaMateria1.getColumnModel().getColumnCount() - 1).setPreferredWidth(lastColumnWidth);

    // Ajustar el tamaño de la imagen en la tabla
    int columnIndex = 6; // Supongamos que la columna de imagen es la 6 (cuenta desde 0)
    TableColumn column = TablaMateria1.getColumnModel().getColumn(columnIndex);
    column.setCellRenderer(new ImageRenderer());

    JTableHeader header = TablaMateria1.getTableHeader();
    header.setOpaque(false);
    header.setBackground(new Color(0, 110, 255));
    header.setForeground(Color.white);
}
   
   
   
   
   
    public void ListarEntraPro() {
        List<Producto> ListarPro = proDao.ListarProductos();
    modelo = (DefaultTableModel) TableEySPro.getModel();
    Object[] ob = new Object[9]; // Asumiendo que tienes 9 columnas, incluyendo la columna de imagen

    for (int i = 0; i < ListarPro.size(); i++) {
        ob[0] = ListarPro.get(i).getId();
        ob[1] = ListarPro.get(i).getCodigo();
        ob[2] = ListarPro.get(i).getNombre();
        ob[3] = ListarPro.get(i).getTipo();
        ob[4] = ListarPro.get(i).getColor();
        ob[5] = ListarPro.get(i).getTamaño();
        ob[6] = ListarPro.get(i).getStock();
        ob[7] = ListarPro.get(i).getPrecio();

        // La columna 6 contendrá la imagen
        byte[] imageData = ListarPro.get(i).getImageData();

        if (imageData != null) {
            ob[8] = getImageIconFromBytes(imageData); // Convierte los datos de imagen en un ImageIcon
        } else {
            ob[8] = null; // Si los datos de imagen son nulos, establece la columna de imagen en null
        }

        modelo.addRow(ob);
    }

    TableEySPro.setModel(modelo);

    // Ajustar el alto de las filas
    int rowHeight = 80; // Cambia este valor según tu preferencia
    TableEySPro.setRowHeight(rowHeight);

    // Ajustar el ancho de las columnas, incluida la columna de la imagen (columna 6)
    int columnWidth = 66; // Cambia este valor según tu preferencia
    for (int columnIndex = 0; columnIndex < TableEySPro.getColumnCount(); columnIndex++) {
        TableColumn column = TableEySPro.getColumnModel().getColumn(columnIndex);
        column.setPreferredWidth(columnWidth);
    }

    // Si deseas que la última columna (imagen) se ajuste al ancho de la tabla, puedes usar esto:
    int lastColumnWidth = TableEySPro.getWidth() - (TableEySPro.getColumnModel().getColumnCount() - 1) * columnWidth;
    TableEySPro.getColumnModel().getColumn(TableEySPro.getColumnModel().getColumnCount() - 1).setPreferredWidth(lastColumnWidth);

    // Ajustar el tamaño de la imagen en la tabla
    int columnIndex = 8; // Supongamos que la columna de imagen es la 6 (cuenta desde 0)
    TableColumn column = TableEySPro.getColumnModel().getColumn(columnIndex);
    column.setCellRenderer(new ImageRenderer());

    JTableHeader header = TableEySPro.getTableHeader();
    header.setOpaque(false);
    header.setBackground(new Color(0, 110, 255));
    header.setForeground(Color.white);
}
   
   
   
   
   
   
public void mostrarMateriasPrimasAgotadasEnTabla() {
    DefaultTableModel modeloTabla = (DefaultTableModel) tablamateriaagotada.getModel();
    int stockMinimo = 10; // Ajusta el stock mínimo según tus necesidades
    List<MateriaPrima> materiasAgotadas = matDao.obtenerMateriaPrimaAgotada(stockMinimo);

    // Limpia el modelo de la tabla
    modeloTabla.setRowCount(0);

    // Aplica el renderizador personalizado para la columna de imágenes
    tablamateriaagotada.getColumnModel().getColumn(3).setCellRenderer(new ImageRenderer());

    // Llena la tabla con los datos de las materias primas agotadas
    for (MateriaPrima mat : materiasAgotadas) {
        ImageIcon imagenIcon = new ImageIcon(mat.getImageData());

        Object[] rowData = {
            mat.getCodigo(),
            mat.getNombre(),
            mat.getProveedorPro(),
            imagenIcon  // Agrega el icono de la imagen a la fila
        };
        modeloTabla.addRow(rowData);
        
         // Ajustar el alto de las filas
    int rowHeight = 80; // Cambia este valor según tu preferencia
    tablamateriaagotada.setRowHeight(rowHeight);

    // Ajustar el ancho de las columnas, incluida la columna de la imagen (columna 6)
    int columnWidth = 100; // Cambia este valor según tu preferencia
    for (int columnIndex = 0; columnIndex < tablamateriaagotada.getColumnCount(); columnIndex++) {
        TableColumn column = tablamateriaagotada.getColumnModel().getColumn(columnIndex);
        column.setPreferredWidth(columnWidth);
    }

    // Si deseas que la última columna (imagen) se ajuste al ancho de la tabla, puedes usar esto:
    int lastColumnWidth = tablamateriaagotada.getWidth() - (tablamateriaagotada.getColumnModel().getColumnCount() - 1) * columnWidth;
    tablamateriaagotada.getColumnModel().getColumn(tablamateriaagotada.getColumnModel().getColumnCount() - 1).setPreferredWidth(lastColumnWidth);

    // Ajustar el tamaño de la imagen en la tabla
    int columnIndex = 3; // Supongamos que la columna de imagen es la 6 (cuenta desde 0)
    TableColumn column = tablamateriaagotada.getColumnModel().getColumn(columnIndex);
    column.setCellRenderer(new ImageRenderer());

    JTableHeader header = tablamateriaagotada.getTableHeader();
    header.setOpaque(false);
    header.setBackground(new Color(0, 110, 255));
    header.setForeground(Color.white);
    }
}


private void mostrarProductosAgotadosEnTabla() {
    DefaultTableModel modeloTabla = (DefaultTableModel) tablaproductoagotada.getModel();
    int stockMinimo = 3; // Ajusta el stock mínimo según tus necesidades
    List<Producto> productosAgotados = proDao.obtenerProductosAgotados(stockMinimo);

    // Limpia el modelo de la tabla
    modeloTabla.setRowCount(0);

    // Aplica el renderizador personalizado para la columna de imágenes
    tablaproductoagotada.getColumnModel().getColumn(3).setCellRenderer(new ImageRenderer());

    // Ajustar el alto de las filas
    int rowHeight = 80; // Cambia este valor según tu preferencia
    tablaproductoagotada.setRowHeight(rowHeight);

    // Ajustar el ancho de las columnas
    int columnWidth = 100; // Cambia este valor según tu preferencia
    for (int columnIndex = 0; columnIndex < tablaproductoagotada.getColumnCount(); columnIndex++) {
        TableColumn column = tablaproductoagotada.getColumnModel().getColumn(columnIndex);
        column.setPreferredWidth(columnWidth);
    }

    // Si deseas que la última columna (imagen) se ajuste al ancho de la tabla, puedes usar esto:
    int lastColumnWidth = tablaproductoagotada.getWidth() - (tablaproductoagotada.getColumnModel().getColumnCount() - 1) * columnWidth;
    tablaproductoagotada.getColumnModel().getColumn(tablaproductoagotada.getColumnModel().getColumnCount() - 1).setPreferredWidth(lastColumnWidth);

    // Llena la tabla con los datos de los productos agotados
    for (Producto producto : productosAgotados) {
        ImageIcon imagenIcon = new ImageIcon(producto.getImageData());

        Object[] rowData = {
            producto.getCodigo(),
            producto.getNombre(),
            producto.getStock(),
            imagenIcon  // Agrega el icono de la imagen a la fila
        };
        modeloTabla.addRow(rowData);
    }

    JTableHeader header = tablaproductoagotada.getTableHeader();
    header.setOpaque(false);
    header.setBackground(new Color(0, 110, 255));
    header.setForeground(Color.white);
}



    public void ListarProveedor() {
        List<Proveedor> ListarPr = PrDao.ListarProveedor();
        modelo = (DefaultTableModel) TableProveedor.getModel();
        Object[] ob = new Object[5];
        for (int i = 0; i < ListarPr.size(); i++) {
            ob[0] = ListarPr.get(i).getId();
            ob[1] = ListarPr.get(i).getRuc();
            ob[2] = ListarPr.get(i).getNombre();
            ob[3] = ListarPr.get(i).getTelefono();
            ob[4] = ListarPr.get(i).getDireccion();
            modelo.addRow(ob);
        }
        TableProveedor.setModel(modelo);
        JTableHeader header = TableProveedor.getTableHeader();
        header.setOpaque(false);
        header.setBackground(new Color(0, 110, 255));
        header.setForeground(Color.white);
    }
   public void ListarUsuarios() {
    List<login> Listar = login.ListarUsuarios();
    modelo = (DefaultTableModel) TableUsuarios.getModel();
    Object[] ob = new Object[6];
    
    for (int i = 0; i < Listar.size(); i++) {
        ob[0] = Listar.get(i).getId();
        ob[1] = Listar.get(i).getNombre();
        ob[2] = Listar.get(i).getCorreo();
        ob[3] = "Contraseña encriptada"; // En lugar de mostrar la contraseña real
        ob[4] = Listar.get(i).getRol();
        ob[5] = Listar.get(i).getEstado();
        modelo.addRow(ob);
    }
    
    TableUsuarios.setModel(modelo);
    
    JTableHeader header = TableUsuarios.getTableHeader();
    header.setOpaque(false);
    header.setBackground(new Color(0, 110, 255));
    header.setForeground(Color.white);
}

   
   public ImageIcon getImageIconFromBytes(byte[] imageData) {
    if (imageData != null && imageData.length > 0) {
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageData));
            return new ImageIcon(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    return null;
}

public void ListarMateriaPrima() {
    List<MateriaPrima> ListarMat = matDao.ListarMateria();
    modelo = (DefaultTableModel) TablaMateria.getModel();
    Object[] ob = new Object[7]; // Asumiendo que tienes 7 columnas, incluyendo la columna de imagen

    for (int i = 0; i < ListarMat.size(); i++) {
        ob[0] = ListarMat.get(i).getId();
        ob[1] = ListarMat.get(i).getCodigo();
        ob[2] = ListarMat.get(i).getNombre();
        ob[3] = ListarMat.get(i).getProveedorPro();
        ob[4] = ListarMat.get(i).getCantidad();
        ob[5] = ListarMat.get(i).getPrecio();

        // La columna 6 contendrá la imagen
        byte[] imageData = ListarMat.get(i).getImageData();

        if (imageData != null) {
            ob[6] = getImageIconFromBytes(imageData); // Convierte los datos de imagen en un ImageIcon
        } else {
            ob[6] = null; // Si los datos de imagen son nulos, establece la columna de imagen en null
        }

        modelo.addRow(ob);
    }

    TablaMateria.setModel(modelo);

    // Ajustar el alto de las filas
    int rowHeight = 80; // Cambia este valor según tu preferencia
    TablaMateria.setRowHeight(rowHeight);

    // Ajustar el ancho de las columnas, incluida la columna de la imagen (columna 6)
    int columnWidth = 100; // Cambia este valor según tu preferencia
    for (int columnIndex = 0; columnIndex < TablaMateria.getColumnCount(); columnIndex++) {
        TableColumn column = TablaMateria.getColumnModel().getColumn(columnIndex);
        column.setPreferredWidth(columnWidth);
    }

    // Si deseas que la última columna (imagen) se ajuste al ancho de la tabla, puedes usar esto:
    int lastColumnWidth = TablaMateria.getWidth() - (TablaMateria.getColumnModel().getColumnCount() - 1) * columnWidth;
    TablaMateria.getColumnModel().getColumn(TablaMateria.getColumnModel().getColumnCount() - 1).setPreferredWidth(lastColumnWidth);

    // Ajustar el tamaño de la imagen en la tabla
    int columnIndex = 6; // Supongamos que la columna de imagen es la 6 (cuenta desde 0)
    TableColumn column = TablaMateria.getColumnModel().getColumn(columnIndex);
    column.setCellRenderer(new ImageRenderer());

    JTableHeader header = TablaMateria.getTableHeader();
    header.setOpaque(false);
    header.setBackground(new Color(0, 110, 255));
    header.setForeground(Color.white);
}

private void buscarPorNombre(String nombre) {
    DefaultTableModel modeloFiltrado = new DefaultTableModel();
    modeloFiltrado.setColumnIdentifiers(new Object[]{"ID", "Código", "Nombre", "Proveedor", "Cantidad", "Precio", "Imagen"});

    for (int i = 0; i < modelo.getRowCount(); i++) {
        String nombreEnFila = modelo.getValueAt(i, 2).toString(); // Suponiendo que el nombre está en la tercera columna (índice 2)
        if (nombreEnFila.toLowerCase().contains(nombre.toLowerCase())) {
            Object[] fila = new Object[7];
            for (int j = 0; j < 7; j++) {
                fila[j] = modelo.getValueAt(i, j);
            }
            modeloFiltrado.addRow(fila);
        }
    }

    TablaMateria.setModel(modeloFiltrado);

    // Aplicar el renderizador personalizado para la columna de imagen (columna 6)
    int columnIndex = 6;
    TableColumn column = TablaMateria.getColumnModel().getColumn(columnIndex);
    column.setCellRenderer(new ImageRenderer());
}

private void buscarPorNombreMaSa(String nombre) {
    DefaultTableModel modeloFiltrado = new DefaultTableModel();
    modeloFiltrado.setColumnIdentifiers(new Object[]{"ID", "Código", "Nombre", "Proveedor", "Cantidad", "Precio", "Imagen"});

    for (int i = 0; i < modelo.getRowCount(); i++) {
        String nombreEnFila = modelo.getValueAt(i, 2).toString(); // Suponiendo que el nombre está en la tercera columna (índice 2)
        if (nombreEnFila.toLowerCase().contains(nombre.toLowerCase())) {
            Object[] fila = new Object[7];
            for (int j = 0; j < 7; j++) {
                fila[j] = modelo.getValueAt(i, j);
            }
            modeloFiltrado.addRow(fila);
        }
    }

    TablaMateria1.setModel(modeloFiltrado);

    // Aplicar el renderizador personalizado para la columna de imagen (columna 6)
    int columnIndex = 6;
    TableColumn column = TablaMateria1.getColumnModel().getColumn(columnIndex);
    column.setCellRenderer(new ImageRenderer());
}

public void ListarProductos() {
    List<Producto> ListarPro = proDao.ListarProductos();
    modelo = (DefaultTableModel) TablaProductos.getModel();

    // Ajusta el modelo de la tabla para tener 9 columnas, incluyendo la columna de imagen
    modelo.setColumnCount(9); // Cambiado de 8 a 9
    modelo.setColumnIdentifiers(new String[]{"ID", "Código", "Nombre", "Stock", "Precio", "Tipo", "Color", "Tamaño", "Imagen"});

    for (int i = 0; i < ListarPro.size(); i++) {
        Producto producto = ListarPro.get(i);

        Object[] ob = new Object[9]; // Cambiado de 8 a 9
        ob[0] = producto.getId();
        ob[1] = producto.getCodigo();
        ob[2] = producto.getNombre();
        ob[3] = producto.getStock();
        ob[4] = producto.getPrecio()+ " Bs";
        ob[5] = producto.getTipo();
        ob[6] = producto.getColor();
        ob[7] = producto.getTamaño();

        // La columna 8 contendrá la imagen (cambio de 7 a 8)
        byte[] imageData = producto.getImageData();

        if (imageData != null) {
            ob[8] = getImageIconFromBytes(imageData); // Convierte los datos de imagen en un ImageIcon
        } else {
            ob[8] = null; // Si los datos de imagen son nulos, establece la columna de imagen en null
        }

        modelo.addRow(ob);
    }

    TablaProductos.setModel(modelo);

    // Ajustar el alto de las filas
    int rowHeight = 80; // Cambia este valor según tu preferencia
    TablaProductos.setRowHeight(rowHeight);

    // Ajustar el ancho de las columnas, incluida la columna de la imagen (columna 8)
    int columnWidth = 70; // Cambia este valor según tu preferencia
    for (int columnIndex = 0; columnIndex < TablaProductos.getColumnCount(); columnIndex++) {
        TableColumn column = TablaProductos.getColumnModel().getColumn(columnIndex);
        column.setPreferredWidth(columnWidth);
    }

    // Si deseas que la última columna (imagen) se ajuste al ancho de la tabla, puedes usar esto:
    int lastColumnWidth = TablaProductos.getWidth() - (TablaProductos.getColumnModel().getColumnCount() - 1) * columnWidth;
    TablaProductos.getColumnModel().getColumn(TablaProductos.getColumnModel().getColumnCount() - 1).setPreferredWidth(lastColumnWidth);

    // Ajustar el tamaño de la imagen en la tabla
    int columnIndex = 8; // Supongamos que la columna de imagen es la 8 (cuenta desde 0)
    TableColumn column = TablaProductos.getColumnModel().getColumn(columnIndex);
    column.setCellRenderer(new ImageRenderer());

    JTableHeader header = TablaProductos.getTableHeader();
    header.setOpaque(false);
    header.setBackground(new Color(0, 110, 255));
    header.setForeground(Color.white);
}


    

    public void LimpiarTable() {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.removeRow(i);
            i = i - 1;
        }
    }
  public void entrarProducto(int idProducto, int cantidad) {
        Producto producto = proDao.BuscarId(idProducto);

        // Luego de actualizar el stock, verifica el nivel de stock.
        if (proDao.entrarProducto(idProducto, cantidad)) {
            producto.verificarNivelDeStock();
        }
    }

    public void salirProducto(int idProducto, int cantidad) {
        Producto producto = proDao.BuscarId(idProducto);

        // Luego de actualizar el stock, verifica el nivel de stock.
        if (proDao.salirProducto(idProducto, cantidad)) {
            producto.verificarNivelDeStock();
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

        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnNuevaVenta = new javax.swing.JButton();
        btnClientes = new javax.swing.JButton();
        btnProveedor = new javax.swing.JButton();
        btnMateriaPrima = new javax.swing.JButton();
        btnVentas = new javax.swing.JButton();
        LabelVendedor = new javax.swing.JLabel();
        tipo = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        btnProducto = new javax.swing.JButton();
        btnreporte = new javax.swing.JButton();
        btnCerrarSesion = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablamateriaagotada = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaproductoagotada = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        TXTnomENma = new javax.swing.JTextField();
        btnGuardarSaMa = new javax.swing.JButton();
        txtENSAMA = new javax.swing.JLabel();
        txtIdMateria1 = new javax.swing.JTextField();
        txtstockEnMA = new javax.swing.JLabel();
        btnSalidaMa = new javax.swing.JButton();
        btnEntradaMa = new javax.swing.JButton();
        txtcantidadQUITARMa = new javax.swing.JTextField();
        btnGuardarSaMa1 = new javax.swing.JButton();
        textprecioEySM = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        TablaMateria1 = new javax.swing.JTable();
        btnbuscadorMAt1 = new javax.swing.JButton();
        txtBusqueda1 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableProveedor = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txtRucProveedor = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtNombreproveedor = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtTelefonoProveedor = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtDireccionProveedor = new javax.swing.JTextField();
        btnguardarProveedor = new javax.swing.JButton();
        btnEditarProveedor = new javax.swing.JButton();
        btnNuevoProveedor = new javax.swing.JButton();
        btnEliminarProveedor = new javax.swing.JButton();
        txtIdProveedor = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        TablaMateria = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        txtCodigoMat = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtDesMat = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtCantMat = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtPrecioMat = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        cbxProveedorMat = new javax.swing.JComboBox<>();
        btnGuardarMat = new javax.swing.JButton();
        btnEditarMat = new javax.swing.JButton();
        btnEliminarMat = new javax.swing.JButton();
        btnNuevoMat = new javax.swing.JButton();
        btnsubirImagen = new javax.swing.JButton();
        jLabel43 = new javax.swing.JLabel();
        txtIdMateria = new javax.swing.JTextField();
        txtimg = new javax.swing.JLabel();
        txtBusqueda = new javax.swing.JTextField();
        btnbuscadorMAt = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        TableEySPro = new javax.swing.JTable();
        idEPro = new javax.swing.JPanel();
        txtISPro1 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        TXTnomENma2 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        textprecioEySM2 = new javax.swing.JTextField();
        btnSalidaPro = new javax.swing.JButton();
        txtcantidadQUITARMa2 = new javax.swing.JTextField();
        btnEntradaPro = new javax.swing.JButton();
        btnGuardarSaMa4 = new javax.swing.JButton();
        btnGuardarSaMa5 = new javax.swing.JButton();
        txtstockEnMA1 = new javax.swing.JLabel();
        txtColor1 = new javax.swing.JTextField();
        txtTamaño1 = new javax.swing.JTextField();
        txtTipo1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtIdMateria2 = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        txtPass = new javax.swing.JPasswordField();
        btnIniciar = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();
        txtNombreu = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        cbxRol = new javax.swing.JComboBox<>();
        btnEditarU = new javax.swing.JButton();
        cbxEstado = new javax.swing.JComboBox<>();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        TableUsuarios = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        TablaProductos = new javax.swing.JTable();
        jPanel16 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        txtCodigoPro = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        txtDesPro = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        txtStockPro = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        txtPrecioPro = new javax.swing.JTextField();
        btnGuardarPro = new javax.swing.JButton();
        btnEditarPro = new javax.swing.JButton();
        btnEliminarPro = new javax.swing.JButton();
        btnNuevoPro = new javax.swing.JButton();
        txtIdproducto = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        btnsubirImagen1 = new javax.swing.JButton();
        txtTamañoPro = new javax.swing.JTextField();
        txtTipoPro = new javax.swing.JTextField();
        txtColorPro = new javax.swing.JTextField();
        txtimgPro = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        btnReportes = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/nealy.png"))); // NOI18N
        jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1220, 130));

        jPanel1.setBackground(new java.awt.Color(141, 102, 233));

        btnNuevaVenta.setBackground(new java.awt.Color(123, 203, 238));
        btnNuevaVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/compras (2).png"))); // NOI18N
        btnNuevaVenta.setText("Inicio");
        btnNuevaVenta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnNuevaVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevaVenta.setFocusable(false);
        btnNuevaVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevaVentaActionPerformed(evt);
            }
        });

        btnClientes.setBackground(new java.awt.Color(123, 203, 238));
        btnClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/salida.png"))); // NOI18N
        btnClientes.setText("Entrada y Salida Materia");
        btnClientes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnClientes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClientes.setFocusable(false);
        btnClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClientesActionPerformed(evt);
            }
        });

        btnProveedor.setBackground(new java.awt.Color(123, 203, 238));
        btnProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/proveedor.png"))); // NOI18N
        btnProveedor.setText("Proveedor");
        btnProveedor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProveedor.setFocusable(false);
        btnProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProveedorActionPerformed(evt);
            }
        });

        btnMateriaPrima.setBackground(new java.awt.Color(123, 203, 238));
        btnMateriaPrima.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/materias-primas.png"))); // NOI18N
        btnMateriaPrima.setText("Materia Prima");
        btnMateriaPrima.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnMateriaPrima.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMateriaPrima.setFocusable(false);
        btnMateriaPrima.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMateriaPrimaMouseClicked(evt);
            }
        });
        btnMateriaPrima.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMateriaPrimaActionPerformed(evt);
            }
        });

        btnVentas.setBackground(new java.awt.Color(123, 203, 238));
        btnVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/salida (1).png"))); // NOI18N
        btnVentas.setText("Entrada y Salida Producto");
        btnVentas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnVentas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVentas.setFocusable(false);
        btnVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentasActionPerformed(evt);
            }
        });

        LabelVendedor.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        LabelVendedor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabelVendedor.setText("administrador");

        tipo.setForeground(new java.awt.Color(255, 255, 255));

        jButton1.setBackground(new java.awt.Color(123, 203, 238));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/metrico.png"))); // NOI18N
        jButton1.setText("Usuarios");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnProducto.setBackground(new java.awt.Color(123, 203, 238));
        btnProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/producto23.png"))); // NOI18N
        btnProducto.setText("Productos");
        btnProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProducto.setFocusable(false);
        btnProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProductoMouseClicked(evt);
            }
        });
        btnProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductoActionPerformed(evt);
            }
        });

        btnreporte.setBackground(new java.awt.Color(123, 203, 238));
        btnreporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/portapapeles.png"))); // NOI18N
        btnreporte.setText("Reportes");
        btnreporte.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnreporte.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnreporte.setFocusable(false);
        btnreporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnreporteActionPerformed(evt);
            }
        });

        btnCerrarSesion.setBackground(new java.awt.Color(123, 203, 238));
        btnCerrarSesion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/cerrar-sesion.png"))); // NOI18N
        btnCerrarSesion.setText("Salir");
        btnCerrarSesion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnCerrarSesion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCerrarSesion.setFocusable(false);
        btnCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(tipo)
                .addGap(153, 153, 153))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(btnCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
            .addComponent(LabelVendedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNuevaVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnClientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnMateriaPrima, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnreporte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(198, 198, 198)
                        .addComponent(tipo))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(133, 133, 133)
                        .addComponent(LabelVendedor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnNuevaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMateriaPrima, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnreporte, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(btnCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -3, 220, 620));

        jTabbedPane1.setBackground(new java.awt.Color(51, 0, 51));
        jTabbedPane1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTabbedPane1.setForeground(new java.awt.Color(153, 204, 255));

        jPanel2.setBackground(new java.awt.Color(102, 255, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablamateriaagotada.setBackground(new java.awt.Color(255, 204, 255));
        tablamateriaagotada.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Codigo", "Nombre", "Proveedor", "Imagen"
            }
        ));
        tablamateriaagotada.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setViewportView(tablamateriaagotada);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, -1, 180));

        jLabel1.setFont(new java.awt.Font("Yu Gothic", 1, 14)); // NOI18N
        jLabel1.setText("Productos Con Stock Minimo");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 260, 220, 30));

        tablaproductoagotada.setBackground(new java.awt.Color(255, 204, 255));
        tablaproductoagotada.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "CODIGO", "NOMBRE", "STOCK", "IMAGEN"
            }
        ));
        jScrollPane2.setViewportView(tablaproductoagotada);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 300, -1, 180));

        jLabel3.setFont(new java.awt.Font("Yu Gothic", 1, 14)); // NOI18N
        jLabel3.setText("Materia Prima Con Cantidad Minima");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 260, 260, 30));

        jTabbedPane1.addTab("1", jPanel2);

        jPanel3.setBackground(new java.awt.Color(102, 255, 204));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBackground(new java.awt.Color(255, 204, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Entradas y Salidas de Materia Prima"));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("Nombre:");

        btnGuardarSaMa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/salvado1.png"))); // NOI18N
        btnGuardarSaMa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarSaMa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarSaMaActionPerformed(evt);
            }
        });

        txtENSAMA.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "Imagen"));

        btnSalidaMa.setText("-");
        btnSalidaMa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalidaMaActionPerformed(evt);
            }
        });

        btnEntradaMa.setText("+");
        btnEntradaMa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntradaMaActionPerformed(evt);
            }
        });

        txtcantidadQUITARMa.setText("0");

        btnGuardarSaMa1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/salvado1.png"))); // NOI18N
        btnGuardarSaMa1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarSaMa1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarSaMa1ActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setText("Precio:");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(txtENSAMA, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIdMateria1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textprecioEySM, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TXTnomENma, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(21, Short.MAX_VALUE))
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnGuardarSaMa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGuardarSaMa1))
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                            .addGap(46, 46, 46)
                            .addComponent(txtstockEnMA, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel9Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(btnSalidaMa)
                            .addGap(18, 18, 18)
                            .addComponent(txtcantidadQUITARMa, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(30, 30, 30)
                            .addComponent(btnEntradaMa))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(txtENSAMA, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtIdMateria1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TXTnomENma, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(textprecioEySM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(txtstockEnMA, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSalidaMa)
                    .addComponent(txtcantidadQUITARMa, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEntradaMa))
                .addGap(11, 11, 11)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(btnGuardarSaMa1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(93, 93, 93))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(btnGuardarSaMa)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jPanel3.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 260, 430));

        TablaMateria1.setBackground(new java.awt.Color(255, 204, 255));
        TablaMateria1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "CODIGO", "DESCRIPCIÓN", "PROVEEDOR", "STOCK", "PRECIO", "img"
            }
        ));
        TablaMateria1.setGridColor(new java.awt.Color(0, 51, 255));
        TablaMateria1.setSelectionBackground(new java.awt.Color(102, 255, 51));
        TablaMateria1.setSelectionForeground(new java.awt.Color(0, 0, 0));
        TablaMateria1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaMateria1MouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(TablaMateria1);
        if (TablaMateria1.getColumnModel().getColumnCount() > 0) {
            TablaMateria1.getColumnModel().getColumn(0).setPreferredWidth(20);
            TablaMateria1.getColumnModel().getColumn(1).setPreferredWidth(50);
            TablaMateria1.getColumnModel().getColumn(2).setPreferredWidth(100);
            TablaMateria1.getColumnModel().getColumn(3).setPreferredWidth(60);
            TablaMateria1.getColumnModel().getColumn(4).setPreferredWidth(40);
        }

        jPanel3.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 70, 720, 370));

        btnbuscadorMAt1.setBackground(new java.awt.Color(51, 153, 255));
        btnbuscadorMAt1.setText("Buscar Materia Prima");
        btnbuscadorMAt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscadorMAt1ActionPerformed(evt);
            }
        });
        jPanel3.add(btnbuscadorMAt1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 30, 170, -1));

        txtBusqueda1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.add(txtBusqueda1, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 30, 230, 30));

        jTabbedPane1.addTab("2", jPanel3);

        jPanel4.setBackground(new java.awt.Color(102, 255, 204));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TableProveedor.setBackground(new java.awt.Color(255, 204, 255));
        TableProveedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "RUC", "NOMBRE", "TELÉFONO", "DIRECCIÓN"
            }
        ));
        TableProveedor.setGridColor(new java.awt.Color(0, 51, 255));
        TableProveedor.setSelectionBackground(new java.awt.Color(102, 255, 51));
        TableProveedor.setSelectionForeground(new java.awt.Color(0, 0, 0));
        TableProveedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableProveedorMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(TableProveedor);
        if (TableProveedor.getColumnModel().getColumnCount() > 0) {
            TableProveedor.getColumnModel().getColumn(0).setPreferredWidth(20);
            TableProveedor.getColumnModel().getColumn(1).setPreferredWidth(40);
            TableProveedor.getColumnModel().getColumn(2).setPreferredWidth(100);
            TableProveedor.getColumnModel().getColumn(3).setPreferredWidth(50);
            TableProveedor.getColumnModel().getColumn(4).setPreferredWidth(80);
        }

        jPanel4.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(285, 57, 720, 390));

        jPanel10.setBackground(new java.awt.Color(255, 204, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Nuevo Proveedor"));

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel17.setText("Ruc:");

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel18.setText("Nombre:");

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel19.setText("Teléfono:");

        jLabel20.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel20.setText("Dirección:");

        btnguardarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/salvado1.png"))); // NOI18N
        btnguardarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnguardarProveedorActionPerformed(evt);
            }
        });

        btnEditarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/expediente1.png"))); // NOI18N
        btnEditarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarProveedorActionPerformed(evt);
            }
        });

        btnNuevoProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pestana1.png"))); // NOI18N
        btnNuevoProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoProveedorActionPerformed(evt);
            }
        });

        btnEliminarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/eliminar1.png"))); // NOI18N
        btnEliminarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProveedorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel18)
                                .addComponent(jLabel17))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtNombreproveedor, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                                .addComponent(txtRucProveedor)))
                        .addGroup(jPanel10Layout.createSequentialGroup()
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel19)
                                .addComponent(jLabel20))
                            .addGap(24, 24, 24)
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtTelefonoProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                                .addComponent(txtDireccionProveedor))))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(btnguardarProveedor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                        .addComponent(txtIdProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEditarProveedor)
                        .addGap(24, 24, 24)))
                .addGap(18, 18, 18))
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(btnEliminarProveedor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnNuevoProveedor)
                .addGap(42, 42, 42))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtRucProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtNombreproveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtTelefonoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel20))
                    .addComponent(txtDireccionProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtIdProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnguardarProveedor)
                    .addComponent(btnEditarProveedor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnEliminarProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNuevoProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(127, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 260, 430));

        jTabbedPane1.addTab("3", jPanel4);

        jPanel5.setBackground(new java.awt.Color(102, 255, 204));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TablaMateria.setBackground(new java.awt.Color(255, 204, 255));
        TablaMateria.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "CODIGO", "DESCRIPCIÓN", "PROVEEDOR", "STOCK", "PRECIO", "img"
            }
        ));
        TablaMateria.setGridColor(new java.awt.Color(0, 51, 255));
        TablaMateria.setSelectionBackground(new java.awt.Color(102, 255, 51));
        TablaMateria.setSelectionForeground(new java.awt.Color(0, 0, 0));
        TablaMateria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaMateriaMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(TablaMateria);
        if (TablaMateria.getColumnModel().getColumnCount() > 0) {
            TablaMateria.getColumnModel().getColumn(0).setPreferredWidth(20);
            TablaMateria.getColumnModel().getColumn(1).setPreferredWidth(50);
            TablaMateria.getColumnModel().getColumn(2).setPreferredWidth(100);
            TablaMateria.getColumnModel().getColumn(3).setPreferredWidth(60);
            TablaMateria.getColumnModel().getColumn(4).setPreferredWidth(40);
            TablaMateria.getColumnModel().getColumn(5).setPreferredWidth(50);
        }

        jPanel5.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 120, 730, 330));

        jPanel11.setBackground(new java.awt.Color(255, 204, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Nueva Materia Prima"));

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel22.setText("Código:");

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel23.setText("Descripción:");

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel24.setText("Cantidad:");

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel25.setText("Precio:");

        txtPrecioMat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioMatKeyTyped(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel26.setText("Proveedor:");

        cbxProveedorMat.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxProveedorMatItemStateChanged(evt);
            }
        });
        cbxProveedorMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxProveedorMatActionPerformed(evt);
            }
        });

        btnGuardarMat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/salvado1.png"))); // NOI18N
        btnGuardarMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarMatActionPerformed(evt);
            }
        });

        btnEditarMat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/expediente1.png"))); // NOI18N
        btnEditarMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarMatActionPerformed(evt);
            }
        });

        btnEliminarMat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/eliminar1.png"))); // NOI18N
        btnEliminarMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarMatActionPerformed(evt);
            }
        });

        btnNuevoMat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pestana1.png"))); // NOI18N
        btnNuevoMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoMatActionPerformed(evt);
            }
        });

        btnsubirImagen.setText("Agregar Imagen");
        btnsubirImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsubirImagenActionPerformed(evt);
            }
        });

        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel43.setText("Imagen");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(jLabel22))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCodigoMat, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDesMat, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addGap(29, 29, 29)
                        .addComponent(txtCantMat, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addGap(47, 47, 47)
                        .addComponent(txtPrecioMat, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addGap(21, 21, 21)
                        .addComponent(cbxProveedorMat, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(145, 145, 145))
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(btnEliminarMat)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnNuevoMat))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(btnGuardarMat)
                                .addGap(48, 48, 48)
                                .addComponent(btnEditarMat))))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel43)
                        .addGap(51, 51, 51)
                        .addComponent(btnsubirImagen)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCodigoMat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnsubirImagen)
                    .addComponent(jLabel43))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel23))
                    .addComponent(txtDesMat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel24))
                    .addComponent(txtCantMat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel25))
                    .addComponent(txtPrecioMat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel26))
                    .addComponent(cbxProveedorMat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(btnGuardarMat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEliminarMat))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(btnEditarMat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnNuevoMat)))
                .addContainerGap())
        );

        jPanel5.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 250, 430));

        txtIdMateria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdMateriaActionPerformed(evt);
            }
        });
        jPanel5.add(txtIdMateria, new org.netbeans.lib.awtextra.AbsoluteConstraints(223, 25, -1, -1));

        txtimg.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "Imagen"));
        jPanel5.add(txtimg, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, 150, 110));

        txtBusqueda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.add(txtBusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 40, 230, 30));

        btnbuscadorMAt.setBackground(new java.awt.Color(51, 153, 255));
        btnbuscadorMAt.setText("Buscar Materia Prima");
        btnbuscadorMAt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscadorMAtActionPerformed(evt);
            }
        });
        jPanel5.add(btnbuscadorMAt, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 40, 170, -1));

        jTabbedPane1.addTab("4", jPanel5);

        jPanel6.setBackground(new java.awt.Color(102, 255, 204));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TableEySPro.setBackground(new java.awt.Color(255, 204, 255));
        TableEySPro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "CODIGO", "NOMBRE", "TIPO", "COLOR", "TAMAÑO", "STOCK", "PRECIO", "IMAGEN"
            }
        ));
        TableEySPro.setGridColor(new java.awt.Color(0, 51, 255));
        TableEySPro.setSelectionBackground(new java.awt.Color(102, 255, 51));
        TableEySPro.setSelectionForeground(new java.awt.Color(0, 0, 0));
        TableEySPro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableEySProMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(TableEySPro);
        if (TableEySPro.getColumnModel().getColumnCount() > 0) {
            TableEySPro.getColumnModel().getColumn(0).setPreferredWidth(20);
            TableEySPro.getColumnModel().getColumn(1).setPreferredWidth(60);
            TableEySPro.getColumnModel().getColumn(2).setPreferredWidth(60);
            TableEySPro.getColumnModel().getColumn(3).setPreferredWidth(60);
        }

        jPanel6.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 80, 620, 400));

        idEPro.setBackground(new java.awt.Color(255, 204, 255));
        idEPro.setBorder(javax.swing.BorderFactory.createTitledBorder("Entradas Y Salidas de Productos"));

        txtISPro1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "Imagen"));

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel21.setText("Nombre:");

        TXTnomENma2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TXTnomENma2ActionPerformed(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel27.setText("Precio:");

        btnSalidaPro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/menos.png"))); // NOI18N
        btnSalidaPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalidaProActionPerformed(evt);
            }
        });

        txtcantidadQUITARMa2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtcantidadQUITARMa2.setText("0");

        btnEntradaPro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/signo-de-mas.png"))); // NOI18N
        btnEntradaPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntradaProActionPerformed(evt);
            }
        });

        btnGuardarSaMa4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/salvado1.png"))); // NOI18N
        btnGuardarSaMa4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarSaMa4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarSaMa4ActionPerformed(evt);
            }
        });

        btnGuardarSaMa5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/salvado1.png"))); // NOI18N
        btnGuardarSaMa5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarSaMa5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarSaMa5ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Color:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Tamaño:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Tipo:");

        javax.swing.GroupLayout idEProLayout = new javax.swing.GroupLayout(idEPro);
        idEPro.setLayout(idEProLayout);
        idEProLayout.setHorizontalGroup(
            idEProLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(idEProLayout.createSequentialGroup()
                .addGroup(idEProLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(idEProLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(idEProLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(jLabel27))
                        .addGap(19, 19, 19)
                        .addGroup(idEProLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(idEProLayout.createSequentialGroup()
                                .addComponent(textprecioEySM2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtColor1, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                            .addComponent(TXTnomENma2, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(idEProLayout.createSequentialGroup()
                        .addGroup(idEProLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(idEProLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(idEProLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(idEProLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(idEProLayout.createSequentialGroup()
                                            .addComponent(jLabel6)
                                            .addGap(32, 32, 32)
                                            .addComponent(txtTipo1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(23, 23, 23))
                                        .addComponent(txtstockEnMA1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(idEProLayout.createSequentialGroup()
                                            .addGroup(idEProLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(btnGuardarSaMa5)
                                                .addComponent(btnSalidaPro, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(18, 18, 18)
                                            .addComponent(txtcantidadQUITARMa2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(btnEntradaPro, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(idEProLayout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtTamaño1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(idEProLayout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(txtISPro1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(idEProLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGuardarSaMa4)
                .addGap(36, 36, 36))
        );
        idEProLayout.setVerticalGroup(
            idEProLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(idEProLayout.createSequentialGroup()
                .addComponent(txtISPro1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(idEProLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(TXTnomENma2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(idEProLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addGroup(idEProLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(textprecioEySM2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(txtColor1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(idEProLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtTamaño1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(idEProLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtTipo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtstockEnMA1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(idEProLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(idEProLayout.createSequentialGroup()
                        .addGroup(idEProLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEntradaPro, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtcantidadQUITARMa2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                        .addComponent(btnGuardarSaMa4))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, idEProLayout.createSequentialGroup()
                        .addComponent(btnSalidaPro, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGuardarSaMa5))))
        );

        jPanel6.add(idEPro, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 310, 460));
        jPanel6.add(txtIdMateria2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 0, 16, 0));

        jTabbedPane1.addTab("5", jPanel6);

        jPanel12.setBackground(new java.awt.Color(102, 255, 204));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel13.setBackground(new java.awt.Color(255, 204, 255));

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/privado.png"))); // NOI18N

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(0, 0, 255));
        jLabel34.setText("Correo Electrónico");

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(0, 0, 255));
        jLabel35.setText("Password");

        txtCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCorreoActionPerformed(evt);
            }
        });

        btnIniciar.setBackground(new java.awt.Color(0, 0, 204));
        btnIniciar.setForeground(new java.awt.Color(255, 255, 255));
        btnIniciar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/cuenta.png"))); // NOI18N
        btnIniciar.setText("Registrar");
        btnIniciar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarActionPerformed(evt);
            }
        });

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(0, 0, 255));
        jLabel36.setText("Nombre:");

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(0, 0, 255));
        jLabel37.setText("Rol:");

        cbxRol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Administrador", "Asistente" }));

        btnEditarU.setBackground(new java.awt.Color(0, 0, 204));
        btnEditarU.setForeground(new java.awt.Color(255, 255, 255));
        btnEditarU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/cuenta.png"))); // NOI18N
        btnEditarU.setText("Editar");
        btnEditarU.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditarU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarUActionPerformed(evt);
            }
        });

        cbxEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "inhactivo" }));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCorreo, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                            .addComponent(txtPass)
                            .addComponent(txtNombreu)
                            .addComponent(cbxRol, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbxEstado, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel36)
                            .addComponent(jLabel35)
                            .addComponent(jLabel34)
                            .addComponent(jLabel37)))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnIniciar)
                        .addGap(18, 18, 18)
                        .addComponent(btnEditarU)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel33)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addComponent(jLabel34)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel36)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNombreu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel37)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxRol, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(cbxEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditarU, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel12.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 280, 410));

        TableUsuarios.setBackground(new java.awt.Color(255, 204, 255));
        TableUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nombre", "Correo", "Pass", "Rol", "Estado"
            }
        ));
        TableUsuarios.setGridColor(new java.awt.Color(0, 51, 255));
        TableUsuarios.setRowHeight(20);
        TableUsuarios.setSelectionBackground(new java.awt.Color(102, 255, 51));
        TableUsuarios.setSelectionForeground(new java.awt.Color(0, 0, 0));
        TableUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableUsuariosMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(TableUsuarios);

        jPanel12.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 50, 610, 400));

        jTabbedPane1.addTab("7", jPanel12);

        jPanel15.setBackground(new java.awt.Color(102, 255, 204));
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TablaProductos.setBackground(new java.awt.Color(255, 204, 255));
        TablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "CODIGO", "DESCRIPCIÓN", "STOCK", "PRECIO", "TIPO", "COLOR", "TAMAÑO", "IMAGEN"
            }
        ));
        TablaProductos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        TablaProductos.setGridColor(new java.awt.Color(0, 51, 255));
        TablaProductos.setSelectionBackground(new java.awt.Color(102, 255, 51));
        TablaProductos.setSelectionForeground(new java.awt.Color(0, 0, 0));
        TablaProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaProductosMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(TablaProductos);
        if (TablaProductos.getColumnModel().getColumnCount() > 0) {
            TablaProductos.getColumnModel().getColumn(0).setPreferredWidth(20);
            TablaProductos.getColumnModel().getColumn(1).setPreferredWidth(50);
            TablaProductos.getColumnModel().getColumn(2).setPreferredWidth(100);
            TablaProductos.getColumnModel().getColumn(3).setPreferredWidth(40);
            TablaProductos.getColumnModel().getColumn(4).setPreferredWidth(50);
        }

        jPanel15.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 110, 720, 370));

        jPanel16.setBackground(new java.awt.Color(255, 204, 255));
        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nuevo Producto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel39.setText("Código:");

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel40.setText("Descripción:");

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel41.setText("Cantidad:");

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel42.setText("Precio:");

        btnGuardarPro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/salvado1.png"))); // NOI18N
        btnGuardarPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarProActionPerformed(evt);
            }
        });

        btnEditarPro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/expediente1.png"))); // NOI18N
        btnEditarPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarProActionPerformed(evt);
            }
        });

        btnEliminarPro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/eliminar1.png"))); // NOI18N
        btnEliminarPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProActionPerformed(evt);
            }
        });

        btnNuevoPro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pestana1.png"))); // NOI18N
        btnNuevoPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoProActionPerformed(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel44.setText("Tipo:");

        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel45.setText("Color:");

        jLabel46.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel46.setText("Tamaño:");

        jLabel47.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel47.setText("Imagen:");

        btnsubirImagen1.setText("Agregar Imagen");
        btnsubirImagen1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsubirImagen1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel39)
                                .addGap(38, 38, 38)
                                .addComponent(txtCodigoPro, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel40)
                                .addGap(12, 12, 12)
                                .addComponent(txtDesPro))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel16Layout.createSequentialGroup()
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel42)
                                    .addComponent(jLabel44)
                                    .addComponent(jLabel45)
                                    .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnsubirImagen1))
                                    .addGroup(jPanel16Layout.createSequentialGroup()
                                        .addGap(29, 29, 29)
                                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtColorPro, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtTipoPro, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtStockPro)
                                            .addComponent(txtPrecioPro)
                                            .addComponent(txtTamañoPro)))))))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnGuardarPro)
                            .addComponent(btnEliminarPro))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                                .addComponent(txtIdproducto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEditarPro))
                            .addComponent(btnNuevoPro, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(55, 55, 55))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel39))
                    .addComponent(txtCodigoPro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel40))
                    .addComponent(txtDesPro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel41))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtStockPro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(txtPrecioPro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel42)))
                .addGap(14, 14, 14)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(txtTipoPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(txtColorPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel46)
                    .addComponent(txtTamañoPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47)
                    .addComponent(btnsubirImagen1))
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(txtIdproducto, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEditarPro)
                            .addComponent(btnGuardarPro))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnEliminarPro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNuevoPro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jPanel15.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 250, 450));

        txtimgPro.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "Imagen"));
        jPanel15.add(txtimgPro, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 0, 150, 110));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("8", jPanel14);

        jPanel17.setBackground(new java.awt.Color(102, 255, 204));

        btnReportes.setBackground(new java.awt.Color(255, 204, 204));
        btnReportes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/reportebtn.png"))); // NOI18N
        btnReportes.setText("reporte proveedor");
        btnReportes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(111, 111, 111)
                .addComponent(btnReportes, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(695, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addComponent(btnReportes, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(362, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("9", jPanel17);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 95, 1000, 520));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClientesActionPerformed
        // TODO add your handling code here:
        LimpiarTable();
        ListarEntraM();
        LimpiarSaEnMateriaPrima();
        txtcantidadQUITARMa.setText("0");
        jTabbedPane1.setSelectedIndex(1);
        txtENSAMA.setIcon(null);
    }//GEN-LAST:event_btnClientesActionPerformed

    private void btnProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProveedorActionPerformed
        // TODO add your handling code here:
        LimpiarTable();
        ListarProveedor();
        jTabbedPane1.setSelectedIndex(2);
        btnEditarProveedor.setEnabled(false);
        btnEliminarProveedor.setEnabled(false);
        btnGuardarPro.setEnabled(true);
        LimpiarProveedor();
    }//GEN-LAST:event_btnProveedorActionPerformed

    private void btnMateriaPrimaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMateriaPrimaActionPerformed
        // TODO add your handling code here:
        LimpiarTable();
        ListarMateriaPrima();
        jTabbedPane1.setSelectedIndex(3);
        btnEditarMat.setEnabled(false);
        btnEliminarMat.setEnabled(false);
        btnGuardarMat.setEnabled(true);
        LimpiarMateriaPrima();
    }//GEN-LAST:event_btnMateriaPrimaActionPerformed

    private void btnNuevaVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevaVentaActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(0);
        mostrarMateriasPrimasAgotadasEnTabla();
        mostrarProductosAgotadosEnTabla();

    }//GEN-LAST:event_btnNuevaVentaActionPerformed

    private void btnVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentasActionPerformed
        // TODO add your handling code here:
        LimpiarTable();
        ListarEntraPro();
        LimpiarSaEnMateriaPrima();
        txtcantidadQUITARMa.setText("0");
        txtENSAMA.setIcon(null);
        jTabbedPane1.setSelectedIndex(4);
        
    }//GEN-LAST:event_btnVentasActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(5);
        LimpiarTable();
        ListarUsuarios();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnMateriaPrimaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMateriaPrimaMouseClicked
        // TODO add your handling code here:
        cbxProveedorMat.removeAllItems();
        llenarProveedor();
        
    }//GEN-LAST:event_btnMateriaPrimaMouseClicked

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarActionPerformed
        String correo = txtCorreo.getText();
    String pass = String.valueOf(txtPass.getPassword());
    String nom = txtNombreu.getText();
    String rol = cbxRol.getSelectedItem().toString();
    String estado = cbxEstado.getSelectedItem().toString();

    if (login.validarCampos(correo, nom, pass)) {
        lg.setNombre(nom);
        lg.setCorreo(correo);
        lg.setPass(pass);
        lg.setRol(rol);
        lg.setEstado(estado);

        // Luego, realiza la inserción en la base de datos
        if (login.Registrar(lg)) {
            JOptionPane.showMessageDialog(null, "Usuario Registrado");
            LimpiarTable();
            ListarUsuarios();
            nuevoUsuario();
        } else {
            JOptionPane.showMessageDialog(null, "Error al registrar usuario");
        }
    }
    }//GEN-LAST:event_btnIniciarActionPerformed
    private void txtCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCorreoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoActionPerformed

    private void TableEySProMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableEySProMouseClicked
      btnGuardarMat.setEnabled(false);
int fila = TableEySPro.rowAtPoint(evt.getPoint());
txtIdMateria2.setText(TableEySPro.getValueAt(fila, 0).toString());
pro = proDao.BuscarId(Integer.parseInt(txtIdMateria2.getText()));
TXTnomENma2.setText(pro.getNombre());
TXTnomENma2.setEditable(false);
txtColor1.setText(pro.getColor());
txtColor1.setEditable(false);
txtTamaño1.setText(pro.getTamaño());
txtTamaño1.setEditable(false);
txtTipo1.setText(pro.getTipo());
txtTipo1.setEditable(false);
textprecioEySM2.setText(Double.toString(pro.getPrecio()));
textprecioEySM2.setEditable(false);
txtstockEnMA1.setText("El stock es: " + pro.getStock());
txtcantidadQUITARMa2.setText("0");
// Para mostrar la imagen en el JLabel txtimg
byte[] imageData = pro.getImageData();
if (imageData != null) {
    try {
        BufferedImage imagenBuffered = ImageIO.read(new ByteArrayInputStream(imageData));
        
        // Escalar la imagen al tamaño del JLabel
        int anchoLabel = txtISPro1.getWidth();
        int altoLabel = txtISPro1.getHeight();
        Image imagenEscalada = imagenBuffered.getScaledInstance(anchoLabel, altoLabel, Image.SCALE_SMOOTH);
        
        // Crear un ImageIcon con la imagen escalada
        ImageIcon icono = new ImageIcon(imagenEscalada);
        txtISPro1.setIcon(icono);
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}
    }//GEN-LAST:event_TableEySProMouseClicked

    private void btnNuevoMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoMatActionPerformed
        // TODO add your handling code here:
        LimpiarMateriaPrima();
    }//GEN-LAST:event_btnNuevoMatActionPerformed

    private void btnEliminarMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarMatActionPerformed
        // TODO add your handling code here:
        if (!"".equals(txtIdMateria.getText())) {
            int pregunta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar");
            if (pregunta == 0) {
                int id = Integer.parseInt(txtIdMateria.getText());
                matDao.EliminarMateriaPrima(id);
                LimpiarTable();
                LimpiarProductos();
                ListarProductos();
                btnEditarMat.setEnabled(false);
                btnEliminarMat.setEnabled(false);
                btnGuardarMat.setEnabled(true);
            }
        }else{
            JOptionPane.showMessageDialog(null, "Selecciona una fila");
        }
    }//GEN-LAST:event_btnEliminarMatActionPerformed

    private void btnEditarMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarMatActionPerformed
if ("".equals(txtIdMateria.getText())) {
        JOptionPane.showMessageDialog(null, "Seleccione una fila");
    } else {
        if (!"".equals(txtCodigoMat.getText()) || !"".equals(txtDesMat.getText()) || !"".equals(txtCantMat.getText()) || !"".equals(txtPrecioMat.getText())) {
            mat.setCodigo(txtCodigoMat.getText());
            mat.setNombre(txtDesMat.getText());
            Combo itemP = (Combo) cbxProveedorMat.getSelectedItem();
            mat.setProveedor(itemP.getId());
            mat.setCantidad(Integer.parseInt(txtCantMat.getText()));
            mat.setPrecio(Double.parseDouble(txtPrecioMat.getText()));
            mat.setId(Integer.parseInt(txtIdMateria.getText()));
            
            // Verifica si se ha seleccionado una nueva imagen
            if (imagenSeleccionada != null) {
                mat.setImageData(imagenSeleccionada);
            }

            matDao.ModificarMateriaPrima(mat);
            JOptionPane.showMessageDialog(null, "Materia Prima Modificada");
            LimpiarTable();
            ListarMateriaPrima();
            LimpiarMateriaPrima();
            cbxProveedorMat.removeAllItems();
            llenarProveedor();
            btnEditarMat.setEnabled(false);
            btnEliminarMat.setEnabled(false);
            btnGuardarMat.setEnabled(true);
        }
    }
    }//GEN-LAST:event_btnEditarMatActionPerformed

    private void btnGuardarMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarMatActionPerformed
  if (!txtCodigoMat.getText().isEmpty() && !txtDesMat.getText().isEmpty() && cbxProveedorMat.getSelectedItem() != null
        && !txtCantMat.getText().isEmpty() && !txtPrecioMat.getText().isEmpty() && imagenSeleccionada != null) {
    MateriaPrima mat = new MateriaPrima();
    mat.setCodigo(txtCodigoMat.getText());
    mat.setNombre(txtDesMat.getText());

    // Obtener el proveedor seleccionado del JComboBox
    Combo itemP = (Combo) cbxProveedorMat.getSelectedItem();
    mat.setProveedor(itemP.getId());

    mat.setCantidad(Integer.parseInt(txtCantMat.getText()));
    mat.setPrecio(Double.parseDouble(txtPrecioMat.getText()));

    // Asignar la imagen seleccionada
    mat.setImageData(imagenSeleccionada);

    if (matDao.RegistrarMateriaPrima(mat)) {
        JOptionPane.showMessageDialog(null, "Producto Registrado");
        LimpiarTable();
        ListarMateriaPrima();
        LimpiarMateriaPrima();
        cbxProveedorMat.removeAllItems();
        llenarProveedor();
        btnEditarMat.setEnabled(false);
        btnEliminarMat.setEnabled(false);
        btnGuardarMat.setEnabled(true);
    } else {
        JOptionPane.showMessageDialog(null, "Error al registrar el producto");
    }
} else {
    JOptionPane.showMessageDialog(null, "Los campos están vacíos");
}

    }//GEN-LAST:event_btnGuardarMatActionPerformed

    private void cbxProveedorMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxProveedorMatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxProveedorMatActionPerformed

    private void cbxProveedorMatItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxProveedorMatItemStateChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_cbxProveedorMatItemStateChanged

    private void txtPrecioMatKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioMatKeyTyped
        // TODO add your handling code here:
        event.numberDecimalKeyPress(evt, txtPrecioMat);
    }//GEN-LAST:event_txtPrecioMatKeyTyped

    private void TablaMateriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaMateriaMouseClicked
btnEditarMat.setEnabled(true);
btnEliminarMat.setEnabled(true);
btnGuardarMat.setEnabled(false);
int fila = TablaMateria.rowAtPoint(evt.getPoint());
txtIdMateria.setText(TablaMateria.getValueAt(fila, 0).toString());
mat = matDao.BuscarId(Integer.parseInt(txtIdMateria.getText()));
txtCodigoMat.setText(mat.getCodigo());
txtDesMat.setText(mat.getNombre());
txtCantMat.setText("" + mat.getCantidad());
txtPrecioMat.setText("" + mat.getPrecio());
cbxProveedorMat.setSelectedItem(new Combo(mat.getProveedor(), mat.getProveedorPro()));

// Para mostrar la imagen en el JLabel txtimg
byte[] imageData = mat.getImageData();
if (imageData != null) {
    try {
        BufferedImage imagenBuffered = ImageIO.read(new ByteArrayInputStream(imageData));
        
        // Escalar la imagen al tamaño del JLabel
        int anchoLabel = txtimg.getWidth();
        int altoLabel = txtimg.getHeight();
        Image imagenEscalada = imagenBuffered.getScaledInstance(anchoLabel, altoLabel, Image.SCALE_SMOOTH);
        
        // Crear un ImageIcon con la imagen escalada
        ImageIcon icono = new ImageIcon(imagenEscalada);
        txtimg.setIcon(icono);
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}
    }//GEN-LAST:event_TablaMateriaMouseClicked

    private void btnEliminarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProveedorActionPerformed
        // TODO add your handling code here:
        if (!"".equals(txtIdProveedor.getText())) {
            int pregunta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar");
            if (pregunta == 0) {
                int id = Integer.parseInt(txtIdProveedor.getText());
                PrDao.EliminarProveedor(id);
                LimpiarTable();
                ListarProveedor();
                LimpiarProveedor();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione una fila");
        }
    }//GEN-LAST:event_btnEliminarProveedorActionPerformed

    private void btnNuevoProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoProveedorActionPerformed
        // TODO add your handling code here:
        LimpiarProveedor();
        btnEditarProveedor.setEnabled(false);
        btnEliminarProveedor.setEnabled(false);
        btnguardarProveedor.setEnabled(true);
    }//GEN-LAST:event_btnNuevoProveedorActionPerformed

    private void btnEditarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarProveedorActionPerformed
        // TODO add your handling code here:
        if ("".equals(txtIdProveedor.getText())) {
            JOptionPane.showMessageDialog(null, "Seleecione una fila");
        } else {
            if (!"".equals(txtRucProveedor.getText()) || !"".equals(txtNombreproveedor.getText()) || !"".equals(txtTelefonoProveedor.getText()) || !"".equals(txtDireccionProveedor.getText())) {
                pr.setRuc(txtRucProveedor.getText());
                pr.setNombre(txtNombreproveedor.getText());
                pr.setTelefono(txtTelefonoProveedor.getText());
                pr.setDireccion(txtDireccionProveedor.getText());
                pr.setId(Integer.parseInt(txtIdProveedor.getText()));
                PrDao.ModificarProveedor(pr);
                JOptionPane.showMessageDialog(null, "Proveedor Modificado");
                LimpiarTable();
                ListarProveedor();
                LimpiarProveedor();
                btnEditarProveedor.setEnabled(false);
                btnEliminarProveedor.setEnabled(false);
                btnguardarProveedor.setEnabled(true);
            }
        }
    }//GEN-LAST:event_btnEditarProveedorActionPerformed

    private void btnguardarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardarProveedorActionPerformed
        // TODO add your handling code here:
        if (!"".equals(txtRucProveedor.getText()) || !"".equals(txtNombreproveedor.getText()) || !"".equals(txtTelefonoProveedor.getText()) || !"".equals(txtDireccionProveedor.getText())) {
            pr.setRuc(txtRucProveedor.getText());
            pr.setNombre(txtNombreproveedor.getText());
            pr.setTelefono(txtTelefonoProveedor.getText());
            pr.setDireccion(txtDireccionProveedor.getText());
            PrDao.RegistrarProveedor(pr);
            JOptionPane.showMessageDialog(null, "Proveedor Registrado");
            LimpiarTable();
            ListarProveedor();
            LimpiarProveedor();
            btnEditarProveedor.setEnabled(false);
            btnEliminarProveedor.setEnabled(false);
            btnguardarProveedor.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(null, "Los campos esta vacios");
        }
    }//GEN-LAST:event_btnguardarProveedorActionPerformed

    private void TableProveedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableProveedorMouseClicked
        // TODO add your handling code here:
        btnEditarProveedor.setEnabled(true);
        btnEliminarProveedor.setEnabled(true);
        btnguardarProveedor.setEnabled(false);
        int fila = TableProveedor.rowAtPoint(evt.getPoint());
        txtIdProveedor.setText(TableProveedor.getValueAt(fila, 0).toString());
        txtRucProveedor.setText(TableProveedor.getValueAt(fila, 1).toString());
        txtNombreproveedor.setText(TableProveedor.getValueAt(fila, 2).toString());
        txtTelefonoProveedor.setText(TableProveedor.getValueAt(fila, 3).toString());
        txtDireccionProveedor.setText(TableProveedor.getValueAt(fila, 4).toString());
    }//GEN-LAST:event_TableProveedorMouseClicked

    private void btnGuardarSaMaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarSaMaActionPerformed
  String cantidadTexto = txtcantidadQUITARMa.getText();
    String idMateriaPrimaTexto = txtIdMateria1.getText();
    String nombreTexto = TXTnomENma.getText();
    String precioTexto = textprecioEySM.getText();

    if (cantidadTexto.isEmpty() || idMateriaPrimaTexto.isEmpty() || nombreTexto.isEmpty() || precioTexto.isEmpty()) {
        // Mostrar un mensaje de error si los campos están vacíos
        JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
    } else {
        try {
            int cantidadAAgregar = Integer.parseInt(cantidadTexto);
            int idMateriaPrima = Integer.parseInt(idMateriaPrimaTexto);
            double precio = Double.parseDouble(precioTexto);

            // Registra la entrada de materia prima
            if (EMdao.registrarEntradaMateriaPrima(idMateriaPrima, nombreTexto, new Date(), cantidadAAgregar, precio)) {
                // Incrementa el stock de materia prima
                if (EMdao.IncrementarStockMateriaPrima(idMateriaPrima, cantidadAAgregar)) {
                    JOptionPane.showMessageDialog(null, "Entrada de materia prima registrada y stock incrementado en " + cantidadAAgregar);
                } else {
                    JOptionPane.showMessageDialog(null, "Error al incrementar el stock de materia prima");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error al registrar la entrada de materia prima");
            }

            LimpiarTable();
            ListarEntraM();
            LimpiarSaEnMateriaPrima();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al convertir la cantidad, el ID o el precio a números.");
        }
    
    }//GEN-LAST:event_btnGuardarSaMaActionPerformed

    private void btnNuevoProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoProActionPerformed
        LimpiarProductos();
    }//GEN-LAST:event_btnNuevoProActionPerformed

    private void btnProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductoMouseClicked

        llenarProveedor();
    }//GEN-LAST:event_btnProductoMouseClicked

    private void btnProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductoActionPerformed
           // TODO add your handling code here:
        LimpiarTable();
        ListarProductos();
        jTabbedPane1.setSelectedIndex(6);
        btnEditarPro.setEnabled(false);
        btnEliminarPro.setEnabled(false);
        LimpiarProductos();
    }//GEN-LAST:event_btnProductoActionPerformed

    private void txtIdMateriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdMateriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdMateriaActionPerformed

    private void btnReportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportesActionPerformed
        Reportesproveedores.reporte();
    }//GEN-LAST:event_btnReportesActionPerformed

    private void btnreporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnreporteActionPerformed
         jTabbedPane1.setSelectedIndex(7);
    }//GEN-LAST:event_btnreporteActionPerformed

    private void TableUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableUsuariosMouseClicked
    btnEditarPro.setEnabled(true);
    btnGuardarPro.setEnabled(false);
    int fila = TableUsuarios.rowAtPoint(evt.getPoint());
    jTextField1.setText(TableUsuarios.getValueAt(fila, 0).toString());
    lg = login.BuscarUsuario(Integer.parseInt(jTextField1.getText()));
    txtNombreu.setText(TableUsuarios.getValueAt(fila, 1).toString());
    txtCorreo.setText(TableUsuarios.getValueAt(fila, 2).toString());
    cbxRol.setSelectedItem(TableUsuarios.getValueAt(fila, 4).toString());
    cbxEstado.setSelectedItem(TableUsuarios.getValueAt(fila, 5).toString());
    // No recuperar la contraseña
    txtPass.setText(""); // Deja el campo de contraseña vacío
    }//GEN-LAST:event_TableUsuariosMouseClicked

    private void btnEditarUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarUActionPerformed
   if ("".equals(jTextField1.getText())) {
        JOptionPane.showMessageDialog(null, "Seleccione una fila.");
    } else {
        String correo = txtCorreo.getText();
        String nombre = txtNombreu.getText();
        String contrasena = txtPass.getText();

        if (login.validarCampos(correo, nombre, contrasena)) {
            lg.setCorreo(correo);
            lg.setNombre(nombre);

            // Encripta la nueva contraseña antes de actualizarla
            String hashedPassword = BCrypt.hashpw(contrasena, BCrypt.gensalt());
            lg.setPass(hashedPassword);

            lg.setRol(cbxRol.getSelectedItem().toString());
            lg.setEstado(cbxEstado.getSelectedItem().toString());
            lg.setId(Integer.parseInt(jTextField1.getText()));
            login.ModificarUsuario(lg);
            JOptionPane.showMessageDialog(null, "Usuario Modificado");
            LimpiarTable();
            LimpiarUsuario();
            ListarUsuarios();
        }
    }
    }//GEN-LAST:event_btnEditarUActionPerformed

    private void btnsubirImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsubirImagenActionPerformed
 JFileChooser fileChooser = new JFileChooser();
fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg"));

int resultado = fileChooser.showOpenDialog(this);
if (resultado == JFileChooser.APPROVE_OPTION) {
    File archivoImagen = fileChooser.getSelectedFile();
    try {
        BufferedImage imagenBuffered = ImageIO.read(archivoImagen);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(imagenBuffered, "jpg", byteArrayOutputStream);
        imagenSeleccionada = byteArrayOutputStream.toByteArray();

        // Escalar la imagen al tamaño del JLabel
        int anchoLabel = txtimg.getWidth();
        int altoLabel = txtimg.getHeight();
        Image imagenEscalada = imagenBuffered.getScaledInstance(anchoLabel, altoLabel, Image.SCALE_SMOOTH);
        
        // Crear un ImageIcon con la imagen escalada
        ImageIcon icono = new ImageIcon(imagenEscalada);
        txtimg.setIcon(icono);
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}
    }//GEN-LAST:event_btnsubirImagenActionPerformed

    private void btnbuscadorMAtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscadorMAtActionPerformed

        String nombreABuscar = txtBusqueda.getText();
        buscarPorNombre(nombreABuscar);
    }//GEN-LAST:event_btnbuscadorMAtActionPerformed

    private void TablaMateria1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaMateria1MouseClicked
btnGuardarMat.setEnabled(false);
int fila = TablaMateria1.rowAtPoint(evt.getPoint());
txtIdMateria1.setText(TablaMateria1.getValueAt(fila, 0).toString());
mat = matDao.BuscarId(Integer.parseInt(txtIdMateria1.getText()));
TXTnomENma.setText(mat.getNombre());
TXTnomENma.setEditable(false);
textprecioEySM.setText(Double.toString(mat.getPrecio()));
TXTnomENma.setEditable(false);
txtstockEnMA.setText("El stock es: " + mat.getCantidad());
txtcantidadQUITARMa.setText("0");
// Para mostrar la imagen en el JLabel txtimg
byte[] imageData = mat.getImageData();
if (imageData != null) {
    try {
        BufferedImage imagenBuffered = ImageIO.read(new ByteArrayInputStream(imageData));
        
        // Escalar la imagen al tamaño del JLabel
        int anchoLabel = txtENSAMA.getWidth();
        int altoLabel = txtENSAMA.getHeight();
        Image imagenEscalada = imagenBuffered.getScaledInstance(anchoLabel, altoLabel, Image.SCALE_SMOOTH);
        
        // Crear un ImageIcon con la imagen escalada
        ImageIcon icono = new ImageIcon(imagenEscalada);
        txtENSAMA.setIcon(icono);
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}
    }//GEN-LAST:event_TablaMateria1MouseClicked

    private void btnGuardarSaMa1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarSaMa1ActionPerformed
  String cantidadTexto = txtcantidadQUITARMa.getText();
    String idMateriaPrimaTexto = txtIdMateria1.getText();
    String nombreTexto = TXTnomENma.getText();
    String precioTexto = textprecioEySM.getText();

    if (cantidadTexto.isEmpty() || idMateriaPrimaTexto.isEmpty() || nombreTexto.isEmpty() || precioTexto.isEmpty()) {
        // Mostrar un mensaje de error si los campos están vacíos
        JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
    } else {
        try {
            int cantidadAAgregar = Integer.parseInt(cantidadTexto);
            int idMateriaPrima = Integer.parseInt(idMateriaPrimaTexto);
            double precio = Double.parseDouble(precioTexto);

            // Registra la entrada de materia prima
            if (EMdao.registrarEntradaMateriaPrima(idMateriaPrima, nombreTexto, new Date(), cantidadAAgregar, precio)) {
                // Incrementa el stock de materia prima
                if (EMdao.IncrementarStockMateriaPrima(idMateriaPrima, cantidadAAgregar)) {
                    JOptionPane.showMessageDialog(null, "Entrada de materia prima registrada y stock incrementado en " + cantidadAAgregar);
                } else {
                    JOptionPane.showMessageDialog(null, "Error al incrementar el stock de materia prima");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error al registrar la entrada de materia prima");
            }

            LimpiarTable();
            ListarEntraM();
            LimpiarSaEnMateriaPrima();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al convertir la cantidad, el ID o el precio a números.");
        }
    }
    }//GEN-LAST:event_btnGuardarSaMa1ActionPerformed

    private void btnbuscadorMAt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscadorMAt1ActionPerformed
        String nombreABuscar = txtBusqueda1.getText();
        buscarPorNombreMaSa(nombreABuscar);
        txtcantidadQUITARMa.setText("0");
    }//GEN-LAST:event_btnbuscadorMAt1ActionPerformed

    private void btnEntradaMaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntradaMaActionPerformed
      // Obtén el valor actual del campo de texto
    String valorActual = txtcantidadQUITARMa.getText();

    try {
        // Convierte el valor actual a un entero
        int valorEntero = Integer.parseInt(valorActual);

        // Incrementa el valor en 1
        valorEntero++;

        // Actualiza el campo de texto con el nuevo valor
        txtcantidadQUITARMa.setText(String.valueOf(valorEntero));
    } catch (NumberFormatException e) {
        // Maneja la excepción si el valor actual no es un número válido
        System.err.println("El valor actual no es un número válido.");
    }
    }//GEN-LAST:event_btnEntradaMaActionPerformed

    private void btnSalidaMaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalidaMaActionPerformed
        // Obtén el valor actual del campo de texto
    String valorActual = txtcantidadQUITARMa.getText();

    try {
        // Convierte el valor actual a un entero
        int valorEntero = Integer.parseInt(valorActual);

        // Asegúrate de que el valor no sea menor que 0
        if (valorEntero > 0) {
            // Disminuye el valor en 1
            valorEntero--;

            // Actualiza el campo de texto con el nuevo valor
            txtcantidadQUITARMa.setText(String.valueOf(valorEntero));
        }
    } catch (NumberFormatException e) {
        // Maneja la excepción si el valor actual no es un número válido
        System.err.println("El valor actual no es un número válido.");
    }
    }//GEN-LAST:event_btnSalidaMaActionPerformed

    private void btnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesionActionPerformed
    Login ventanaInicioSesion = new Login();  
    ventanaInicioSesion.setVisible(true);
    dispose();
    }//GEN-LAST:event_btnCerrarSesionActionPerformed

    private void btnsubirImagen1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsubirImagen1ActionPerformed
   JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg"));

    int resultado = fileChooser.showOpenDialog(this);
    if (resultado == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();

        try {
            BufferedImage imagenBuffered = ImageIO.read(selectedFile);
            // Convierte la imagen en un arreglo de bytes
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(imagenBuffered, "jpg", byteArrayOutputStream);
            imagenSeleccionada = byteArrayOutputStream.toByteArray(); // Asignar la imagen seleccionada

            // Escalar y mostrar la imagen en el JLabel
            int anchoLabel = txtimgPro.getWidth();
            int altoLabel = txtimgPro.getHeight();
            Image imagenEscalada = imagenBuffered.getScaledInstance(anchoLabel, altoLabel, Image.SCALE_SMOOTH);
            ImageIcon icono = new ImageIcon(imagenEscalada);
            txtimgPro.setIcon(icono);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    }//GEN-LAST:event_btnsubirImagen1ActionPerformed

    private void TXTnomENma2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TXTnomENma2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TXTnomENma2ActionPerformed

    private void btnSalidaProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalidaProActionPerformed
       String valorActual = txtcantidadQUITARMa2.getText();

    try {
        // Convierte el valor actual a un entero
        int valorEntero = Integer.parseInt(valorActual);

        // Asegúrate de que el valor no sea menor que 0
        if (valorEntero > 0) {
            // Disminuye el valor en 1
            valorEntero--;

            // Actualiza el campo de texto con el nuevo valor
            txtcantidadQUITARMa2.setText(String.valueOf(valorEntero));
        }
    } catch (NumberFormatException e) {
        // Maneja la excepción si el valor actual no es un número válido
        System.err.println("El valor actual no es un número válido.");
    }
    }//GEN-LAST:event_btnSalidaProActionPerformed

    private void btnEntradaProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntradaProActionPerformed
        // Obtén el valor actual del campo de texto
    String valorActual = txtcantidadQUITARMa2.getText();

    try {
        // Convierte el valor actual a un entero
        int valorEntero = Integer.parseInt(valorActual);

        // Incrementa el valor en 1
        valorEntero++;

        // Actualiza el campo de texto con el nuevo valor
        txtcantidadQUITARMa2.setText(String.valueOf(valorEntero));
    } catch (NumberFormatException e) {
        // Maneja la excepción si el valor actual no es un número válido
        System.err.println("El valor actual no es un número válido.");
    }
    }//GEN-LAST:event_btnEntradaProActionPerformed

    private void btnGuardarSaMa4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarSaMa4ActionPerformed
String cantidadTexto = txtcantidadQUITARMa2.getText();
String idProductoTexto = txtIdMateria2.getText();
String nombreTexto = TXTnomENma2.getText();
String precioTexto = textprecioEySM2.getText();
String tipo = txtTipo1.getText();
String color = txtColor1.getText();
String tamaño = txtTamaño1.getText();

if (cantidadTexto.isEmpty() || idProductoTexto.isEmpty() || nombreTexto.isEmpty() || precioTexto.isEmpty() || tipo.isEmpty() || color.isEmpty() || tamaño.isEmpty()) {
    JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
} else {
    try {
        int cantidadAAgregar = Integer.parseInt(cantidadTexto);
        int idProducto = Integer.parseInt(idProductoTexto);
        double precio = Double.parseDouble(precioTexto);

        if (EPdao.registrarEntradaPro(idProducto, nombreTexto, new Date(), cantidadAAgregar, precio, idProducto, nombreTexto, tipo, color, tamaño)) {
            if (EPdao.IncrementarStockProducto(idProducto, cantidadAAgregar)) {
                JOptionPane.showMessageDialog(null, "Entrada de producto registrada y stock incrementado en " + cantidadAAgregar);
            } else {
                JOptionPane.showMessageDialog(null, "Error al incrementar el stock de producto");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error al registrar la entrada de producto");
        }

        LimpiarTable();
        ListarEntraPro();
LimpiarSaEnPro();
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Error al convertir la cantidad, el ID o el precio a números.");
    }
}
    }//GEN-LAST:event_btnGuardarSaMa4ActionPerformed

    private void btnGuardarSaMa5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarSaMa5ActionPerformed
int cantidadARestar = Integer.parseInt(txtcantidadQUITARMa2.getText());
String idProductoTexto = txtIdMateria2.getText();
int id_producto = 0;
try {
    id_producto = Integer.parseInt(idProductoTexto);
} catch (NumberFormatException e) {
    JOptionPane.showMessageDialog(null, "Error al convertir el ID de producto a entero.");
}

String nombre_Producto = TXTnomENma2.getText();
java.util.Date fechaActual = new java.util.Date();

// Obtener el precio, tipo, color y tamaño desde los campos de texto
double precio = Double.parseDouble(textprecioEySM2.getText());
String tipo = txtTipo1.getText();
String color = txtColor1.getText();
String tamaño = txtTamaño1.getText();

int cantidadProducto = 100; // Obtenlo desde tu fuente de datos
cantidadProducto -= cantidadARestar;

if (EPdao.registrarSalidaPro(id_producto, nombre_Producto, fechaActual, tipo, color, tamaño, cantidadARestar, precio, cantidadARestar * precio)) {
    JOptionPane.showMessageDialog(null, "Salida de producto registrada con éxito.");
} else {
    JOptionPane.showMessageDialog(null, "Error al registrar la salida de producto.");
}

LimpiarTable();
ListarEntraPro();
LimpiarSaEnPro();

    }//GEN-LAST:event_btnGuardarSaMa5ActionPerformed

    private void TablaProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaProductosMouseClicked
    btnEditarPro.setEnabled(true);
btnEliminarPro.setEnabled(true);
btnGuardarPro.setEnabled(false);

int fila = TablaProductos.rowAtPoint(evt.getPoint());
txtIdproducto.setText(TablaProductos.getValueAt(fila, 0).toString());
pro = proDao.BuscarId(Integer.parseInt(txtIdproducto.getText()));
txtCodigoPro.setText(pro.getCodigo());
txtDesPro.setText(pro.getNombre());
txtStockPro.setText("" + pro.getStock());
txtPrecioPro.setText("" + pro.getPrecio());

// En lugar de usar comboBoxTipo y comboBoxColor, usa los campos de texto correspondientes
txtTipoPro.setText(pro.getTipo());
txtColorPro.setText(pro.getColor());

txtTamañoPro.setText("" + pro.getTamaño());

// Para mostrar la imagen en un JLabel
byte[] imageData = pro.getImageData();
if (imageData != null) {
    try {
        BufferedImage imagenBuffered = ImageIO.read(new ByteArrayInputStream(imageData));

        // Escalar la imagen al tamaño del JLabel
        int anchoLabel = txtimgPro.getWidth();
        int altoLabel = txtimgPro.getHeight();
        Image imagenEscalada = imagenBuffered.getScaledInstance(anchoLabel, altoLabel, Image.SCALE_SMOOTH);

        // Crear un ImageIcon con la imagen escalada
        ImageIcon icono = new ImageIcon(imagenEscalada);
        txtimgPro.setIcon(icono);
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}

    }//GEN-LAST:event_TablaProductosMouseClicked

    private void btnGuardarProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarProActionPerformed
         if (!txtCodigoPro.getText().isEmpty() && !txtDesPro.getText().isEmpty()
        && !txtTipoPro.getText().isEmpty() && !txtColorPro.getText().isEmpty()
        && !txtStockPro.getText().isEmpty() && !txtPrecioPro.getText().isEmpty()
        && imagenSeleccionada != null) {

    Producto pro = new Producto();
    pro.setCodigo(txtCodigoPro.getText());
    pro.setNombre(txtDesPro.getText());

    String tipoIngresado = txtTipoPro.getText();
    String colorIngresado = txtColorPro.getText();

    pro.setTipo(tipoIngresado);
    pro.setColor(colorIngresado);
    pro.setTamaño(txtTamañoPro.getText());
    pro.setStock(Integer.parseInt(txtStockPro.getText()));
    pro.setPrecio(Double.parseDouble(txtPrecioPro.getText()));
    pro.setImageData(imagenSeleccionada);

    if (proDao.RegistrarProducto(pro)) {
        JOptionPane.showMessageDialog(null, "Producto Registrado");
        LimpiarTable();
        ListarProductos();
        LimpiarProductos();
        btnEditarPro.setEnabled(false);
        btnEliminarPro.setEnabled(false);
        btnGuardarPro.setEnabled(true);
    } else {
        JOptionPane.showMessageDialog(null, "Error al registrar el producto");
    }
} else {
    JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos obligatorios");
}

    }//GEN-LAST:event_btnGuardarProActionPerformed

    private void btnEditarProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarProActionPerformed
        if (!"".equals(txtIdproducto.getText())) {
    if (!"".equals(txtCodigoPro.getText()) || !"".equals(txtDesPro.getText()) || !"".equals(txtStockPro.getText()) || !"".equals(txtPrecioPro.getText())) {
        int idProducto = Integer.parseInt(txtIdproducto.getText());

        // Obtén los datos actuales del producto desde la base de datos
        Producto productoExistente = proDao.BuscarId(idProducto);

        // Actualiza los campos del producto con la nueva información
        productoExistente.setCodigo(txtCodigoPro.getText());
        productoExistente.setNombre(txtDesPro.getText());
        productoExistente.setStock(Integer.parseInt(txtStockPro.getText()));
        productoExistente.setPrecio(Double.parseDouble(txtPrecioPro.getText()));

        // Verifica si se ha seleccionado una nueva imagen válida y establece productoExistente.setImageData(imagen) apropiadamente.
        if (imagenSeleccionada != null) {
            productoExistente.setImageData(imagenSeleccionada);
        }

        String tipoIngresado = txtTipoPro.getText();
        String colorIngresado = txtColorPro.getText();

        productoExistente.setTipo(tipoIngresado);
        productoExistente.setColor(colorIngresado);
        productoExistente.setTamaño(txtTamañoPro.getText());

        // Llama al método para modificar el producto en la base de datos
        if (proDao.ModificarProductos(productoExistente)) {
            JOptionPane.showMessageDialog(null, "Producto Modificado");
            LimpiarTable();
            ListarProductos();
            LimpiarProductos();
            btnEditarPro.setEnabled(false);
            btnEliminarPro.setEnabled(false);
            btnGuardarPro.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(null, "Error al modificar el producto");
        }
    }
} else {
    JOptionPane.showMessageDialog(null, "Seleccione una fila");
}
    }//GEN-LAST:event_btnEditarProActionPerformed

    private void btnEliminarProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProActionPerformed
         if (!"".equals(txtIdproducto.getText())) {
            int pregunta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar");
            if (pregunta == 0) {
                int id = Integer.parseInt(txtIdproducto.getText());
                proDao.EliminarProductos(id);
                LimpiarTable();
                LimpiarProductos();
                ListarProductos();
                btnEditarPro.setEnabled(false);
                btnEliminarPro.setEnabled(false);
                btnGuardarPro.setEnabled(true);
            }
        }else{
            JOptionPane.showMessageDialog(null, "Selecciona una fila");
        }
    }//GEN-LAST:event_btnEliminarProActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Sistema().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelVendedor;
    private javax.swing.JTextField TXTnomENma;
    private javax.swing.JTextField TXTnomENma2;
    private javax.swing.JTable TablaMateria;
    private javax.swing.JTable TablaMateria1;
    private javax.swing.JTable TablaProductos;
    private javax.swing.JTable TableEySPro;
    private javax.swing.JTable TableProveedor;
    private javax.swing.JTable TableUsuarios;
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnClientes;
    private javax.swing.JButton btnEditarMat;
    private javax.swing.JButton btnEditarPro;
    private javax.swing.JButton btnEditarProveedor;
    private javax.swing.JButton btnEditarU;
    private javax.swing.JButton btnEliminarMat;
    private javax.swing.JButton btnEliminarPro;
    private javax.swing.JButton btnEliminarProveedor;
    private javax.swing.JButton btnEntradaMa;
    private javax.swing.JButton btnEntradaPro;
    private javax.swing.JButton btnGuardarMat;
    private javax.swing.JButton btnGuardarPro;
    private javax.swing.JButton btnGuardarSaMa;
    private javax.swing.JButton btnGuardarSaMa1;
    private javax.swing.JButton btnGuardarSaMa4;
    private javax.swing.JButton btnGuardarSaMa5;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JButton btnMateriaPrima;
    private javax.swing.JButton btnNuevaVenta;
    private javax.swing.JButton btnNuevoMat;
    private javax.swing.JButton btnNuevoPro;
    private javax.swing.JButton btnNuevoProveedor;
    private javax.swing.JButton btnProducto;
    private javax.swing.JButton btnProveedor;
    private javax.swing.JButton btnReportes;
    private javax.swing.JButton btnSalidaMa;
    private javax.swing.JButton btnSalidaPro;
    private javax.swing.JButton btnVentas;
    private javax.swing.JButton btnbuscadorMAt;
    private javax.swing.JButton btnbuscadorMAt1;
    private javax.swing.JButton btnguardarProveedor;
    private javax.swing.JButton btnreporte;
    private javax.swing.JButton btnsubirImagen;
    private javax.swing.JButton btnsubirImagen1;
    private javax.swing.JComboBox<String> cbxEstado;
    private javax.swing.JComboBox<Object> cbxProveedorMat;
    private javax.swing.JComboBox<String> cbxRol;
    private javax.swing.JPanel idEPro;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTable tablamateriaagotada;
    private javax.swing.JTable tablaproductoagotada;
    private javax.swing.JTextField textprecioEySM;
    private javax.swing.JTextField textprecioEySM2;
    private javax.swing.JLabel tipo;
    private javax.swing.JTextField txtBusqueda;
    private javax.swing.JTextField txtBusqueda1;
    private javax.swing.JTextField txtCantMat;
    private javax.swing.JTextField txtCodigoMat;
    private javax.swing.JTextField txtCodigoPro;
    private javax.swing.JTextField txtColor1;
    private javax.swing.JTextField txtColorPro;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDesMat;
    private javax.swing.JTextField txtDesPro;
    private javax.swing.JTextField txtDireccionProveedor;
    private javax.swing.JLabel txtENSAMA;
    private javax.swing.JLabel txtISPro1;
    private javax.swing.JTextField txtIdMateria;
    private javax.swing.JTextField txtIdMateria1;
    private javax.swing.JTextField txtIdMateria2;
    private javax.swing.JTextField txtIdProveedor;
    private javax.swing.JTextField txtIdproducto;
    private javax.swing.JTextField txtNombreproveedor;
    private javax.swing.JTextField txtNombreu;
    private javax.swing.JPasswordField txtPass;
    private javax.swing.JTextField txtPrecioMat;
    private javax.swing.JTextField txtPrecioPro;
    private javax.swing.JTextField txtRucProveedor;
    private javax.swing.JTextField txtStockPro;
    private javax.swing.JTextField txtTamaño1;
    private javax.swing.JTextField txtTamañoPro;
    private javax.swing.JTextField txtTelefonoProveedor;
    private javax.swing.JTextField txtTipo1;
    private javax.swing.JTextField txtTipoPro;
    private javax.swing.JTextField txtcantidadQUITARMa;
    private javax.swing.JTextField txtcantidadQUITARMa2;
    private javax.swing.JLabel txtimg;
    private javax.swing.JLabel txtimgPro;
    private javax.swing.JLabel txtstockEnMA;
    private javax.swing.JLabel txtstockEnMA1;
    // End of variables declaration//GEN-END:variables
    private void LimpiarCliente() {
        txtIdMateria1.setText("");
        TXTnomENma.setText("");
    }

    private void LimpiarProveedor() {
        txtIdProveedor.setText("");
        txtRucProveedor.setText("");
        txtNombreproveedor.setText("");
        txtTelefonoProveedor.setText("");
        txtDireccionProveedor.setText("");
    }
private void LimpiarSaEnMateriaPrima() {
        txtIdMateria1.setText("");
        txtENSAMA.setIcon(null);
        TXTnomENma.setText("");
        txtstockEnMA.setText("");
        txtcantidadQUITARMa.setText("0");
        textprecioEySM.setText("");
    }
private void LimpiarSaEnPro() {
        txtIdMateria1.setText("");
        txtENSAMA.setIcon(null);
        TXTnomENma.setText("");
        txtstockEnMA.setText("");
        txtcantidadQUITARMa.setText("0");
        textprecioEySM.setText("");
    }
private void LimpiarUsuario() {
        jTextField1.setText("");
        txtCorreo.setText("");
        cbxRol.setSelectedItem(null);
        cbxEstado.setSelectedItem(null);
        txtNombreu.setText("");
        txtPass.setText("");
    }
    private void LimpiarProductos() {
        txtCodigoPro.setText("");
        txtDesPro.setText("");
        txtStockPro.setText("");
        txtPrecioPro.setText("");
        txtColorPro.setText("");
        txtTipoPro.setText("");
        txtTamañoPro.setText("");
        txtimgPro.setIcon(null);
    }

   

    private void nuevoUsuario(){
        txtNombreu.setText("");
        txtCorreo.setText("");
        txtPass.setText("");
        cbxRol.setSelectedItem(null);
        cbxEstado.setSelectedItem(null);
    }
    private void llenarProveedor(){
        List<Proveedor> lista = PrDao.ListarProveedor();
        for (int i = 0; i < lista.size(); i++) {
            int id = lista.get(i).getId();
            String nombre = lista.get(i).getNombre();
            cbxProveedorMat.addItem(new Combo(id, nombre));
        }
    }
    private void LimpiarMateriaPrima() {
        txtCodigoMat.setText("");
        cbxProveedorMat.setSelectedItem(null);
        txtDesMat.setText("");
        txtCantMat.setText("");
        txtPrecioMat.setText("");
    }
}
