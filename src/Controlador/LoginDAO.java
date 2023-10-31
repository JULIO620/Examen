package Controlador;

import Modelo.login;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import org.mindrot.jbcrypt.BCrypt;

public class LoginDAO {
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    Conexion cn = new Conexion();
    
    public login log(String correo, String pass) {
        login l = new login();
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND estado = 'activo'";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, correo);
            rs = ps.executeQuery();
            if (rs.next()) {
                String hashedPasswordFromDB = rs.getString("pass");

                if (BCrypt.checkpw(pass, hashedPasswordFromDB)) {
                    l.setId(rs.getInt("id"));
                    l.setNombre(rs.getString("nombre"));
                    l.setCorreo(rs.getString("correo"));
                    l.setPass(hashedPasswordFromDB); // Opcional: Devuelve la contraseña encriptada
                    l.setRol(rs.getString("rol"));
                    l.setEstado(rs.getString("estado"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return l;
    }

    public boolean Registrar(login lg) {
        String sql = "INSERT INTO usuarios (nombre, correo, pass, rol, estado) VALUES (?,?,?,?,?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, lg.getNombre());
            ps.setString(2, lg.getCorreo());

            // Encripta la contraseña antes de almacenarla
            String hashedPassword = BCrypt.hashpw(lg.getPass(), BCrypt.gensalt());
            ps.setString(3, hashedPassword);

            ps.setString(4, lg.getRol());
            ps.setString(5, lg.getEstado());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public List<login> ListarUsuarios() {
        List<login> Lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                login lg = new login();
                lg.setId(rs.getInt("id"));
                lg.setNombre(rs.getString("nombre"));
                lg.setCorreo(rs.getString("correo"));
                lg.setPass(rs.getString("pass")); // Recupera la contraseña encriptada
                lg.setRol(rs.getString("rol"));
                lg.setEstado(rs.getString("estado"));
                Lista.add(lg);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return Lista;
    }

    public boolean ModificarUsuario(login lg) {
        String sql = "UPDATE usuarios SET nombre=?, correo=?, pass=?, rol=?, estado=? WHERE id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, lg.getNombre());
            ps.setString(2, lg.getCorreo());

            // Encripta la nueva contraseña antes de actualizarla
            String hashedPassword = BCrypt.hashpw(lg.getPass(), BCrypt.gensalt());
            ps.setString(3, hashedPassword);

            ps.setString(4, lg.getRol());
            ps.setString(5, lg.getEstado());
            ps.setInt(6, lg.getId());
            ps.execute();
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

    public login BuscarUsuario(int id) {
        login l = new login();
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                l.setId(rs.getInt("id"));
                l.setCorreo(rs.getString("correo"));
                
                // No se almacena la contraseña en este campo para no revelarla
                // l.setPass(rs.getString("pass"));
                
                l.setRol(rs.getString("rol"));
                l.setEstado(rs.getString("estado"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return l;
    }
    public boolean validarCorreo(String correo) {
    // Utiliza una expresión regular para verificar el formato del correo.
    String expresionRegular = "^[A-Za-z0-9+_.-]+@(.+)$";
    Pattern pat = Pattern.compile(expresionRegular);
    Matcher mat = pat.matcher(correo);

    return mat.matches();
}
     private boolean validarContrasena(String contrasena) {
    // La expresión regular verifica que la contraseña contenga al menos 8 caracteres, una letra mayúscula, una letra minúscula, un número y un carácter especial.
    String expresionRegular = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
    Pattern pat = Pattern.compile(expresionRegular);
    Matcher mat = pat.matcher(contrasena);

    return mat.find();
}
    public boolean validarCampos(String correo, String nombre, String contrasena) {
    if ("".equals(correo) || "".equals(nombre) || "".equals(contrasena)) {
        JOptionPane.showMessageDialog(null, "Todos los campos son requeridos.");
        return false; // La validación no pasó
    }

    // Validación de correo
    if (!validarCorreo(correo)) {
        JOptionPane.showMessageDialog(null, "El correo no tiene el formato correcto (usuario@dominio.com).");
        return false; // La validación no pasó
    }

    // Validación de contraseña
    if (!validarContrasena(contrasena)) {
        JOptionPane.showMessageDialog(null, "La contraseña no cumple con los requisitos. Debe contener al menos 8 caracteres, una letra mayúscula, una letra minúscula, un número y un carácter especial.");
        return false; // La validación no pasó
    }

    return true; // Todas las validaciones pasaron
}

}
