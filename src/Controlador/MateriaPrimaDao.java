
package Controlador;

import Modelo.MateriaPrima;
import Modelo.Proveedor;
import Vista.Sistema;
import java.awt.Component;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.apache.poi.sl.draw.ImageRenderer;

public class MateriaPrimaDao {
    Connection con;
    Conexion cn = new Conexion();
    PreparedStatement ps;
    ResultSet rs;
    
   public boolean RegistrarMateriaPrima(MateriaPrima mat) {
    String sql = "INSERT INTO materia_prima(codigo, nombre, proveedor, cantidad, precio, img) VALUES (?,?,?,?,?,?)";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setString(1, mat.getCodigo());
        ps.setString(2, mat.getNombre());
        ps.setInt(3, mat.getProveedor());
        ps.setInt(4, mat.getCantidad());
        ps.setDouble(5, mat.getPrecio());

        // Para guardar la imagen en la base de datos
        // El campo "imagen" debe ser de tipo BLOB o similar en la base de datos
        ps.setBytes(6, mat.getImageData());

        ps.execute();
        return true;
    } catch (SQLException e) {
        System.out.println(e.toString());
        return false;
    }
}

    public List<MateriaPrima> ListarMateria() {
    List<MateriaPrima> ListaMat = new ArrayList();
    String sql = "SELECT pr.id AS id_proveedor, pr.nombre AS nombre_proveedor, p.*, p.img AS imagen_materia_prima " +
                 "FROM proveedor pr INNER JOIN materia_prima p ON pr.id = p.proveedor ORDER BY p.id DESC";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {               
            MateriaPrima mat = new MateriaPrima();
            mat.setId(rs.getInt("id"));
            mat.setCodigo(rs.getString("codigo"));
            mat.setNombre(rs.getString("nombre"));
            mat.setProveedor(rs.getInt("id_proveedor"));
            mat.setProveedorPro(rs.getString("nombre_proveedor"));
            mat.setCantidad(rs.getInt("cantidad"));
            mat.setPrecio(rs.getDouble("precio"));

            // Recuperar los datos de la imagen como un array de bytes
            byte[] imageData = rs.getBytes("imagen_materia_prima");
            mat.setImageData(imageData);

            ListaMat.add(mat);
        }
    } catch (SQLException e) {
        System.out.println(e.toString());
    }
    return ListaMat;
}

    
  public boolean EliminarMateriaPrima(int id) {
    String selectImageSql = "SELECT img FROM materia_prima WHERE id = ?";
    String deleteMatSql = "DELETE FROM materia_prima WHERE id = ?";
    try {
        con = cn.getConnection();
        con.setAutoCommit(false);  // Desactivar la confirmación automática

        // Obtener los datos de la imagen antes de eliminar la materia prima
        ps = con.prepareStatement(selectImageSql);
        ps.setInt(1, id);
        rs = ps.executeQuery();
        if (rs.next()) {
            // Recuperar los datos de la imagen
            Blob blob = rs.getBlob("img");

            // Eliminar la materia prima
            ps = con.prepareStatement(deleteMatSql);
            ps.setInt(1, id);
            ps.execute();

            // Confirmar la transacción
            con.commit();

            // Cerrar el blob
            if (blob != null) {
                blob.free();
            }

            return true;
        } else {
            // No se encontró la materia prima
            con.rollback();
            return false;
        }
    } catch (SQLException e) {
        System.out.println(e.toString());
        try {
            con.rollback();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        return false;
    } finally {
        try {
            con.setAutoCommit(true);  // Restaurar la confirmación automática
            con.close();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }
}

    
public boolean ModificarMateriaPrima(MateriaPrima mat) {
    String selectImageSql = "SELECT img FROM materia_prima WHERE id = ?";
    String updateMatSql = "UPDATE materia_prima SET codigo=?, nombre=?, proveedor=?, cantidad=?, precio=?, img=? WHERE id=?";
    try {
        con = cn.getConnection();
        con.setAutoCommit(false);  // Desactivar la confirmación automática

        // Obtener los datos de la imagen antes de actualizar la materia prima
        ps = con.prepareStatement(selectImageSql);
        ps.setInt(1, mat.getId());
        rs = ps.executeQuery();
        Blob blob = null;
        if (rs.next()) {
            // Recuperar los datos de la imagen
            blob = rs.getBlob("img");
        }

        // Actualizar la materia prima
        ps = con.prepareStatement(updateMatSql);
        ps.setString(1, mat.getCodigo());
        ps.setString(2, mat.getNombre());
        ps.setInt(3, mat.getProveedor());
        ps.setInt(4, mat.getCantidad());
        ps.setDouble(5, mat.getPrecio());

        // Actualizar la imagen si se proporciona una nueva
        if (mat.getImageData() != null) {
            ps.setBytes(6, mat.getImageData());
        } else {
            // Si no se proporciona una nueva imagen, mantener la imagen existente
            if (blob != null) {
                byte[] existingImageData = blob.getBytes(1, (int) blob.length());
                ps.setBytes(6, existingImageData);
            } else {
                // Si no hay una imagen existente, establecer el campo imagen a NULL
                ps.setNull(6, Types.BLOB);
            }
        }

        ps.setInt(7, mat.getId());
        ps.execute();

        // Confirmar la transacción
        con.commit();

        // Cerrar el blob
        if (blob != null) {
            blob.free();
        }

        return true;
    } catch (SQLException e) {
        System.out.println(e.toString());
        try {
            con.rollback();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        return false;
    } finally {
        try {
            con.setAutoCommit(true);  // Restaurar la confirmación automática
            con.close();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }
}

   public MateriaPrima BuscarMateriaPrima(String cod) {
    MateriaPrima materia = new MateriaPrima();
    String sql = "SELECT * FROM materia_prima  WHERE codigo = ?";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setString(1, cod);
        rs = ps.executeQuery();
        if (rs.next()) {
            materia.setId(rs.getInt("id"));
            materia.setCodigo(cod);  // Establecer el código
            materia.setNombre(rs.getString("nombre"));
            materia.setPrecio(rs.getDouble("precio"));
            materia.setCantidad(rs.getInt("cantidad"));

            // Recuperar los datos de la imagen
            Blob blob = rs.getBlob("img");
            if (blob != null) {
                byte[] imageData = blob.getBytes(1, (int) blob.length());
                materia.setImageData(imageData);
            }
        }
    } catch (SQLException e) {
        System.out.println(e.toString());
    }
    return materia;
}

   public MateriaPrima BuscarId(int id) {
    MateriaPrima mat = new MateriaPrima();
    String sql = "SELECT pr.id AS id_proveedor, pr.nombre AS nombre_proveedor, p.*, p.img as imagen_materia FROM proveedor pr INNER JOIN materia_prima p ON p.proveedor = pr.id WHERE p.id = ?";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        rs = ps.executeQuery();
        if (rs.next()) {
            mat.setId(id);
            mat.setCodigo(rs.getString("codigo"));
            mat.setNombre(rs.getString("nombre"));
            mat.setProveedor(rs.getInt("proveedor"));
            mat.setProveedorPro(rs.getString("nombre_proveedor"));
            mat.setCantidad(rs.getInt("cantidad"));
            mat.setPrecio(rs.getDouble("precio"));

            // Recuperar los datos de la imagen
            Blob blob = rs.getBlob("imagen_materia");
            if (blob != null) {
                byte[] imageData = blob.getBytes(1, (int) blob.length());
                mat.setImageData(imageData);
            }
        }
    } catch (SQLException e) {
        System.out.println(e.toString());
    }
    return mat;
}

    public Proveedor BuscarProveedor(String nombre){
        Proveedor pr = new Proveedor();
        String sql = "SELECT * FROM proveedor WHERE nombre = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            rs = ps.executeQuery();
            if (rs.next()) {
                pr.setId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return pr;
    }

    public List<MateriaPrima> verificarMateriaPrimaAgotada() {
    List<MateriaPrima> materiaPrimaAgotada = new ArrayList<>();
    String sql = "SELECT id, codigo, nombre, cantidad FROM materia_prima WHERE cantidad = 0";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            MateriaPrima mat = new MateriaPrima();
            mat.setId(rs.getInt("id"));
            mat.setCodigo(rs.getString("codigo"));
            mat.setNombre(rs.getString("nombre"));
            mat.setCantidad(rs.getInt("cantidad"));
            materiaPrimaAgotada.add(mat);
        }
    } catch (SQLException e) {
        System.out.println(e.toString());
    }
    return materiaPrimaAgotada;
}
public List<MateriaPrima> obtenerMateriaPrimaAgotada(int stockMinimo) {
    List<MateriaPrima> materiaPrimaAgotada = new ArrayList<>();

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        con = cn.getConnection();
       String sql = "SELECT pr.id AS id_proveedor, pr.nombre AS nombre_proveedor, mp.*, mp.img AS imagen_materia_prima " +
             "FROM proveedor pr INNER JOIN materia_prima mp ON pr.id = mp.proveedor WHERE mp.cantidad <= ? " +
             "ORDER BY mp.id DESC";

        ps = con.prepareStatement(sql);
        ps.setInt(1, stockMinimo); // Aquí puedes ajustar el stock mínimo según tus necesidades

        rs = ps.executeQuery();

        while (rs.next()) {
            MateriaPrima mat = new MateriaPrima();
            mat.setId(rs.getInt("id"));
            mat.setCodigo(rs.getString("codigo"));
            mat.setNombre(rs.getString("nombre"));
            mat.setProveedor(rs.getInt("id_proveedor"));
            mat.setProveedorPro(rs.getString("nombre_proveedor"));
            mat.setCantidad(rs.getInt("cantidad"));
            mat.setPrecio(rs.getDouble("precio"));
              // Recuperar los datos de la imagen como un array de bytes
            byte[] imageData = rs.getBytes("img");
            mat.setImageData(imageData);

            // Puedes manejar la imagen según el formato de la base de datos
            // Esto dependerá de cómo almacenas las imágenes en tu base de datos
            // Por ejemplo, si usas un BLOB, puedes obtener la imagen de esta manera
            // byte[] imageData = rs.getBytes("img");
            // mat.setImageData(imageData);
            
            materiaPrimaAgotada.add(mat);
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

    return materiaPrimaAgotada;
}    
}
