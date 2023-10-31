/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.EntradaM;
import Modelo.EntradaPRO;
import Modelo.SalidaM;
import Modelo.SalidaPro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author user
 */
public class EntradaPro {
      Connection con;
    Conexion cn = new Conexion();
    PreparedStatement ps;
    ResultSet rs;
    int r;
public boolean registrarEntradaPro(int idproducto, String Nombre, Date fechaMovimiento, int cantidad, double precio, int id_producto, String nombre_producto, String tipo, String color, String tamaño) {
    con = cn.getConnection();
    String sql = "INSERT INTO entrada_producto (id_producto, nombre_producto, fechaMovimiento, cantidad, precio, tipo, color, tamaño) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    try {
        ps = con.prepareStatement(sql);
        ps.setInt(1, id_producto);
        ps.setString(2, nombre_producto);
        ps.setDate(3, new java.sql.Date(fechaMovimiento.getTime()));
        ps.setInt(4, cantidad);
        ps.setDouble(5, precio);
        ps.setString(6, tipo);
        ps.setString(7, color);
        ps.setString(8, tamaño);

        ps.executeUpdate();
        return true;
    } catch (SQLException e) {
        System.err.println(e);
        return false;
    }
}

// Método para registrar la salida de producto en la tabla salida_producto
public boolean registrarSalidaPro(int id_producto, String nombre_producto, java.util.Date fechaMovimiento, String tipo, String color, String tamaño, int cantidad, double precio, double precioTotal) {
    con = cn.getConnection();

    try {
        // Consulta para obtener la cantidad actual de la materia prima
        String consultaCantidad = "SELECT stock FROM productos WHERE id = ?";
        ps = con.prepareStatement(consultaCantidad);
        ps.setInt(1, id_producto);
        rs = ps.executeQuery();

        if (rs.next()) {
            int cantidadActual = rs.getInt("stock");

            // Verificar si hay suficiente cantidad para la salida
            if (cantidadActual >= cantidad) {
                // Consulta para actualizar la cantidad en la tabla productos
                String consultaActualizar = "UPDATE productos SET stock = ? WHERE id = ?";
                ps = con.prepareStatement(consultaActualizar);
                ps.setInt(1, cantidadActual - cantidad);
                ps.setInt(2, id_producto);
                ps.executeUpdate();

                // Ahora registra la salida en la tabla salida_producto
                String sql = "INSERT INTO salida_producto (id_producto, nombre_producto, fechaMovimiento, tipo, color, tamaño, cantidad, precio, precioTotal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                ps = con.prepareStatement(sql);
                ps.setInt(1, id_producto);
                ps.setString(2, nombre_producto);
                ps.setDate(3, new java.sql.Date(fechaMovimiento.getTime()));
                ps.setString(4, tipo);
                ps.setString(5, color);
                ps.setString(6, tamaño);
                ps.setInt(7, cantidad);
                ps.setDouble(8, precio);
                ps.setDouble(9, precio * cantidad); // Calcula el precioTotal
                ps.executeUpdate();

                return true;
            } else {
                System.err.println("No hay suficiente cantidad de producto para la salida.");
                return false;
            }
        } else {
            System.err.println("ID de producto no encontrado.");
            return false;
        }
    } catch (SQLException e) {
        System.err.println(e);
        return false;
    }
}



  public List<EntradaPRO> listarEntradasProducto() {
    List<EntradaPRO> listaEntradas = new ArrayList<>();
    con = cn.getConnection();
    String sql = "SELECT id_producto, nombre_producto, fechaMovimiento, cantidad, precio, costo_total FROM entrada_producto"; // Selecciona los campos deseados
    try {
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            EntradaPRO entrada = new EntradaPRO();
            entrada.setId(rs.getInt("id_producto"));
            entrada.setNombre(rs.getString("nombre_producto"));
            entrada.setTipo(rs.getString("tipo"));
            entrada.setFecha(rs.getDate("fechaMovimiento"));
            entrada.setCantidad(rs.getInt("cantidad"));
            entrada.setPrecio(rs.getDouble("precio"));
            listaEntradas.add(entrada);
        }
    } catch (SQLException e) {
        System.err.println(e);
    }
    return listaEntradas;
}

   public List<SalidaPro> listarSalidasPro() {
    List<SalidaPro> listaSalidas = new ArrayList<>();
    con = cn.getConnection();
    String sql = "SELECT id, id_producto, nombre_producto, fechaMovimiento, tipo, color, tamaño, cantidad, precio, precioTotal FROM salida_producto"; // Cambio de "entrada_materia_prima" a "salida_producto" y selecciona los campos deseados
    try {
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            SalidaPro salida = new SalidaPro(); // Asegúrate de tener una clase "SalidaM" definida
            salida.setId(rs.getInt("id"));
            salida.setIdProducto(rs.getInt("id_producto"));
            salida.setNombre(rs.getString("nombre_producto"));
            salida.setFecha(rs.getDate("fechaMovimiento"));
            salida.setTipo(rs.getString("tipo"));
            salida.setColor(rs.getString("color"));
            salida.setTamaño(rs.getString("tamaño"));
            salida.setCantidad(rs.getInt("cantidad"));
            salida.setPrecio(rs.getDouble("precio"));
            salida.setPrecioTotal(rs.getDouble("precioTotal"));
            listaSalidas.add(salida);
        }
    } catch (SQLException e) {
        System.err.println(e);
    }
    return listaSalidas;
}


public boolean IncrementarStockProducto(int idProducto, int cantidadAAgregar) {
   try {
        con = cn.getConnection();
        String sql = "UPDATE productos SET stock = stock + ? WHERE id = ?";
        ps = con.prepareStatement(sql);
        ps.setInt(1, cantidadAAgregar);
        ps.setInt(2, idProducto);
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

