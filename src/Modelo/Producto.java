
package Modelo;

import javax.swing.JOptionPane;


public class Producto {
 private int id;
    private String codigo;
    private String nombre;
    private String tipo;
    private int stock;
    private double precio;
    private String tamaño;
    private String color;  // Nuevo campo para el color del producto
    private int stockMinimo;  // Umbral mínimo
    private int stockMaximo;  // Umbral máximo
    private byte[] imageData; // Nuevo campo para almacenar datos de la imagen
    
    public Producto() {
    }

    public Producto(int id, String codigo, String nombre, String tipo, int stock, double precio, String tamaño, String color, int stockMinimo, int stockMaximo, byte[] imageData) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.stock = stock;
        this.precio = precio;
        this.tamaño = tamaño;
        this.color = color;
        this.stockMinimo = stockMinimo;
        this.stockMaximo = stockMaximo;
        this.imageData = imageData;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
      public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTamaño() {
        return tamaño;
    }

    public void setTamaño(String tamaño) {
        this.tamaño = tamaño;
    }
// Método para ingresar productos al stock
    public void entrarProducto(int cantidad) {
        if (cantidad > 0) {
            this.stock += cantidad;
        }
    }
// Método para retirar productos del stock
    public void salirProducto(int cantidad) {
        if (cantidad > 0 && cantidad <= this.stock) {
            this.stock -= cantidad;

            // Verificar si está en el nivel mínimo y cambiar la variable enStockMinimo
            if (stock <= stockMinimo) {
                boolean enStockMinimo = true;
            } else {
                boolean enStockMinimo = false;
            }
        }
    }

    // Método para verificar el nivel de stock y mostrar alertas si es necesario
    public void verificarNivelDeStock() {
        boolean enStockMinimo = false;
        if (enStockMinimo) {
            generarAlerta("ALERTA: Stock mínimo alcanzado para el producto: " + nombre);
        } else if (stock >= stockMaximo) {
            generarAlerta("ALERTA: Stock máximo alcanzado para el producto: " + nombre);
        }
    }

    // Método para generar alertas (puedes personalizar cómo se generan las alertas)
    private void generarAlerta(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje);
    }
   
}
