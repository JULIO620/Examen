
package Controlador;

import Modelo.Producto;
import Modelo.Proveedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDao {
    Connection con;
    Conexion cn = new Conexion();
    PreparedStatement ps;
    ResultSet rs;
    
  public boolean RegistrarProducto(Producto pro) {
    String sql = "INSERT INTO productos (codigo, nombre, tipo, color, tamaño, stock, precio, img) VALUES (?,?,?,?,?,?,?,?)";
    try {
        con = cn.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, pro.getCodigo());
        ps.setString(2, pro.getNombre());
        ps.setString(3, pro.getTipo());
        ps.setString(4, pro.getColor());
        ps.setString(5, pro.getTamaño());
        ps.setInt(6, pro.getStock());
        ps.setDouble(7, pro.getPrecio());
        ps.setBytes(8, pro.getImageData());

        int rowsInserted = ps.executeUpdate();
        return rowsInserted > 0;
    } catch (SQLException e) {
        System.err.println("Error al registrar el producto: " + e.getMessage());
        return false;
    }
}


    
    
   public List<Producto> ListarProductos(){
   List<Producto> Listapro = new ArrayList();
   String sql = "SELECT * FROM productos";
   try {
       con = cn.getConnection();
       ps = con.prepareStatement(sql);
       rs = ps.executeQuery();
       while (rs.next()) {               
           Producto pro = new Producto();
           pro.setId(rs.getInt("id"));
           pro.setCodigo(rs.getString("codigo"));
           pro.setNombre(rs.getString("nombre"));
           pro.setTipo(rs.getString("tipo")); // Nuevo campo
           pro.setColor(rs.getString("color")); // Nuevo campo
           pro.setTamaño(rs.getString("tamaño")); // Nuevo campo
           pro.setStock(rs.getInt("stock"));
           pro.setPrecio(rs.getDouble("precio"));
           pro.setImageData(rs.getBytes("img")); // Supongo que imagen es un array de bytes en tu Producto
           Listapro.add(pro);
       }
   } catch (SQLException e) {
       System.out.println(e.toString());
   }
   return Listapro;
}

    
    public boolean EliminarProductos(int id){
    String sql = "DELETE FROM productos WHERE id = ?";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ps.execute();
        return true;
    } catch (SQLException e) {
        System.out.println(e.toString());
        return false;
    } finally {
        try {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }
}

    
    public boolean ModificarProductos(Producto pro) {
    String sql = "UPDATE productos SET codigo=?, nombre=?, stock=?, precio=?, tipo=?, color=?, tamaño=?, img=? WHERE id=?";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setString(1, pro.getCodigo());
        ps.setString(2, pro.getNombre());
        ps.setInt(3, pro.getStock());
        ps.setDouble(4, pro.getPrecio());
        ps.setString(5, pro.getTipo());
        ps.setString(6, pro.getColor());
        ps.setString(7, pro.getTamaño());
        ps.setBytes(8, pro.getImageData());  // asumiendo que la imagen se almacena como bytes en la base de datos
        ps.setInt(9, pro.getId());
        ps.execute();
        return true;
    } catch (SQLException e) {
        System.out.println(e.toString());
        return false;
    } finally {
        try {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }
}

    
   public Producto BuscarPro(String cod){
    Producto producto = new Producto();
    String sql = "SELECT * FROM productos WHERE codigo = ?";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setString(1, cod);
        rs = ps.executeQuery();
        if (rs.next()) {
            producto.setId(rs.getInt("id"));
            producto.setCodigo(rs.getString("codigo"));
            producto.setNombre(rs.getString("nombre"));
            producto.setStock(rs.getInt("stock"));
            producto.setPrecio(rs.getDouble("precio"));
            producto.setTipo(rs.getString("tipo")); // Agregar tipo
            producto.setColor(rs.getString("color")); // Agregar color
            producto.setTamaño(rs.getString("tamaño")); // Agregar tamaño
            producto.setImageData(rs.getBytes("img")); // Agregar imagen (asumiendo que se almacena como bytes)
        }
    } catch (SQLException e) {
        System.out.println(e.toString());
    }
    return producto;
}

   public Producto BuscarId(int id){
    Producto pro = new Producto();
    String sql = "SELECT * FROM productos WHERE id = ?";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        rs = ps.executeQuery();
        if (rs.next()) {
            pro.setId(rs.getInt("id"));
            pro.setCodigo(rs.getString("codigo"));
            pro.setNombre(rs.getString("nombre"));
            pro.setStock(rs.getInt("stock"));
            pro.setPrecio(rs.getDouble("precio"));
            pro.setTipo(rs.getString("tipo")); // Agregar tipo
            pro.setColor(rs.getString("color")); // Agregar color
            pro.setTamaño(rs.getString("tamaño")); // Agregar tamaño
            pro.setImageData(rs.getBytes("img")); // Agregar imagen (asumiendo que se almacena como bytes)
        }
    } catch (SQLException e) {
        System.out.println(e.toString());
    }
    return pro;
}

   // Método para ingresar productos al stock
public boolean entrarProducto(int id, int cantidad) {
    String sql = "UPDATE productos SET stock = stock + ? WHERE id = ?";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setInt(1, cantidad);
        ps.setInt(2, id);
        ps.executeUpdate();

        // Luego de actualizar el stock, verifica el nivel de stock.
        Producto producto = BuscarId(id);
        producto.verificarNivelDeStock();

        return true;
    } catch (SQLException e) {
        System.out.println(e.toString());
        return false;
    } finally {
        try {
            con.close();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }
}

   
// Método para retirar productos del stock
public boolean salirProducto(int id, int cantidad) {
    String sql = "UPDATE productos SET stock = stock - ? WHERE id = ?";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setInt(1, cantidad);
        ps.setInt(2, id);
        ps.executeUpdate();

        // Luego de actualizar el stock, verifica el nivel de stock.
        Producto producto = BuscarId(id);
        producto.verificarNivelDeStock();

        return true;
    } catch (SQLException e) {
        System.out.println(e.toString());
        return false;
    } finally {
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }
}
   public List<Producto> verificarProductosAgotados() {
    List<Producto> productosAgotados = new ArrayList<>();
    String sql = "SELECT id, codigo, nombre, stock, tipo, color, tamaño, img FROM productos WHERE stock = 0";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            Producto producto = new Producto();
            producto.setId(rs.getInt("id"));
            producto.setCodigo(rs.getString("codigo"));
            producto.setNombre(rs.getString("nombre"));
            producto.setStock(rs.getInt("stock"));
            producto.setTipo(rs.getString("tipo")); // Agregar tipo
            producto.setColor(rs.getString("color")); // Agregar color
            producto.setTamaño(rs.getString("tamaño")); // Agregar tamaño
            producto.setImageData(rs.getBytes("img")); // Agregar imagen (asumiendo que se almacena como bytes)
            productosAgotados.add(producto);
        }
    } catch (SQLException e) {
        System.out.println(e.toString());
    }
    return productosAgotados;
}

public List<Producto> obtenerProductosAgotados(int stockMinimo) {
    List<Producto> productosAgotados = new ArrayList<>();

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        con = cn.getConnection();
        if (con == null) {
            System.out.println("Error: No se pudo establecer la conexión a la base de datos.");
            return productosAgotados;
        }

        String sql = "SELECT id, codigo, nombre, stock, precio,tipo,tamaño,color, img AS imagen_producto " +
                     "FROM productos WHERE stock <= ? " +
                     "ORDER BY id DESC";

        ps = con.prepareStatement(sql);
        ps.setInt(1, stockMinimo);

        rs = ps.executeQuery();

        while (rs.next()) {
            Producto producto = new Producto();
            producto.setId(rs.getInt("id"));
            producto.setCodigo(rs.getString("codigo"));
            producto.setNombre(rs.getString("nombre"));
            producto.setStock(rs.getInt("stock"));
            producto.setTipo(rs.getString("tipo"));
            producto.setTamaño(rs.getString("tamaño"));
            producto.setColor(rs.getString("color"));
            // Recuperar los datos de la imagen como un array de bytes
            byte[] imageData = rs.getBytes("imagen_producto");
            producto.setImageData(imageData);

            productosAgotados.add(producto);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        // Cerrar recursos (ResultSet, PreparedStatement y Connection) en un bloque finally
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return productosAgotados;
}

}
