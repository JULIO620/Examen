package Controlador;

import Modelo.EntradaM;
import Modelo.SalidaM;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EntradaMDao {
    Connection con;
    Conexion cn = new Conexion();
    PreparedStatement ps;
    ResultSet rs;
    int r;

   public boolean registrarEntradaMateriaPrima(int idMateriaPrima, String Nombre, Date fechaMovimiento, int cantidad, double precio) {
    con = cn.getConnection();
    String sql = "INSERT INTO entrada_materia_prima (idMateriaPrima, nombre_materia, fechaMovimiento, cantidad, precio, costo_total) VALUES (?, ?, ?, ?, ?, ?)";
    try {
        double costoTotal = cantidad * precio; // Calcular el costo total

        ps = con.prepareStatement(sql);
        ps.setInt(1, idMateriaPrima);
        ps.setString(2, Nombre);
        ps.setDate(3, new java.sql.Date(fechaMovimiento.getTime()));
        ps.setInt(4, cantidad);
        ps.setDouble(5, precio);
        ps.setDouble(6, costoTotal); // Agregar el costo total a la consulta

        ps.executeUpdate();
        return true;
    } catch (SQLException e) {
        System.err.println(e);
        return false;
    }
}

// Método para registrar la entrada de materia prima en la tabla entrada_materia_prima
public boolean registrarSalidaMateriaPrima(int idMateriaPrima, String nombre, java.util.Date fecha, int cantidad) {
    con = cn.getConnection();
    
    try {
        // Consulta para obtener la cantidad actual de la materia prima
        String consultaCantidad = "SELECT cantidad FROM materia_prima WHERE id = ?";
        ps = con.prepareStatement(consultaCantidad);
        ps.setInt(1, idMateriaPrima);
        rs = ps.executeQuery();
        
        if (rs.next()) {
            int cantidadActual = rs.getInt("cantidad");
            
            // Verificar si hay suficiente cantidad para la salida
            if (cantidadActual >= cantidad) {
                // Consulta para actualizar la cantidad en la tabla materia_prima
                String consultaActualizar = "UPDATE materia_prima SET cantidad = ? WHERE id = ?";
                ps = con.prepareStatement(consultaActualizar);
                ps.setInt(1, cantidadActual - cantidad);
                ps.setInt(2, idMateriaPrima);
                ps.executeUpdate();
                
                // Ahora registra la salida en la tabla entrada_materia_prima
                String sql = "INSERT INTO salida_materia_prima (idMateriaPrima, nombre_materia, fechaMovimiento, cantidad) VALUES (?, ?, ?, ?)";
                ps = con.prepareStatement(sql);
                ps.setInt(1, idMateriaPrima);
                ps.setString(2, nombre);
                ps.setDate(3, new java.sql.Date(fecha.getTime()));
                ps.setInt(4, cantidad);
                ps.executeUpdate();
                
                return true;
            } else {
                System.err.println("No hay suficiente cantidad de materia prima para la salida.");
                return false;
            }
        } else {
            System.err.println("ID de materia prima no encontrado.");
            return false;
        }
    } catch (SQLException e) {
        System.err.println(e);
        return false;
    }
}

    public List<EntradaM> listarEntradasMateriaPrima() {
        List<EntradaM> listaEntradas = new ArrayList<>();
        con = cn.getConnection();
        String sql = "SELECT * FROM entrada_materia_prima";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                EntradaM entrada = new EntradaM();
                entrada.setId(rs.getInt("id"));
                entrada.setIdMateriaPrima(rs.getInt("idMateriaPrima"));
                entrada.setNombre(rs.getString("nombre_materia"));
                entrada.setFechaMovimiento(rs.getDate("fechaMovimiento"));
                entrada.setCantidad(rs.getInt("cantidad"));
                entrada.setPrecio(rs.getDouble("precio"));
                entrada.setPrecio(rs.getDouble("costo_total"));
                listaEntradas.add(entrada);
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
        return listaEntradas;
    }
    public List<SalidaM> listarSalidasMateriaPrima() {
    List<SalidaM> listaSalidas = new ArrayList<>();
    con = cn.getConnection();
    String sql = "SELECT * FROM salida_materia_prima"; // Cambio de "entrada_materia_prima" a "salida_materia_prima"
    try {
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            SalidaM salida = new SalidaM(); // Asegúrate de tener una clase "SalidaM" definida
            salida.setId(rs.getInt("id"));
            salida.setIdMateriaPrima(rs.getInt("idMateriaPrima"));
            salida.setNombre(rs.getString("nombre_materia"));
            salida.setFechaMovimiento(rs.getDate("fechaMovimiento"));
            salida.setCantidad(rs.getInt("cantidad"));
            listaSalidas.add(salida);
        }
    } catch (SQLException e) {
        System.err.println(e);
    }
    return listaSalidas;
}

public boolean IncrementarStockMateriaPrima(int idMateriaPrima, int cantidadAAgregar) {
   try {
        con = cn.getConnection();
        String sql = "UPDATE materia_prima SET cantidad = cantidad + ? WHERE id = ?";
        ps = con.prepareStatement(sql);
        ps.setInt(1, cantidadAAgregar);
        ps.setInt(2, idMateriaPrima);
        int filasAfectadas = ps.executeUpdate();
        return filasAfectadas > 0;
    } catch (SQLException e) {
        System.err.println(e);
        return false;
    } finally {
        // Cierra las conexiones y recursos
    }
}

    // Puedes agregar otros métodos, como actualizar entradas o calcular totales, según tus necesidades.
}
