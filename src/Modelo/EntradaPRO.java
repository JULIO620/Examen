/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.util.Date;

/**
 *
 * @author user
 */
public class EntradaPRO {
     private int id;
     private int idProducto;
    private String nombre;
    private String tipo;
    private String color;
    private String tamaño;
    private Date fecha;
    private int cantidad;
    private double precio;


    public EntradaPRO(int id,int idProducto, String nombre,String tipo, String color, String tamaño, Date fecha, int cantidad, double precio) {
        this.id = id;
        this.idProducto=idProducto;
        this.nombre = nombre;
        this.tipo = tipo;
        this.color = color;
        this.tamaño = tamaño;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public EntradaPRO() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // Getters and setters para todos los campos
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTamaño() {
        return tamaño;
    }

    public void setTamaño(String tamaño) {
        this.tamaño = tamaño;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
