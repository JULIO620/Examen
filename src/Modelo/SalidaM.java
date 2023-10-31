/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.util.Date;

public class SalidaM {
    private int id;
    private int idMateriaPrima;
    private String nombre;
    private Date fechaMovimiento;
    private int cantidad;
 public SalidaM(){
 
 }
    // Constructor
    public SalidaM(int id, int idMateriaPrima, String nombre, Date fechaMovimiento, int cantidad) {
        this.id = id;
        this.idMateriaPrima = idMateriaPrima;
        this.nombre = nombre;
        this.fechaMovimiento = fechaMovimiento;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMateriaPrima() {
        return idMateriaPrima;
    }

    public void setIdMateriaPrima(int idMateriaPrima) {
        this.idMateriaPrima = idMateriaPrima;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}

