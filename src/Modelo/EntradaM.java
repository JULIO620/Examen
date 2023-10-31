
package Modelo;

import java.util.Date;

public class EntradaM {
    private int id;
    private int idMateriaPrima;
    private String Nombre;
    private Date fechaMovimiento;
    private int cantidad;
    private double precio;
    private double costo_total;      
    public EntradaM() {
        // Constructor vacío
    }

    public EntradaM(int id, int idMateriaPrima, String Nombre,Date fechaMovimiento, int cantidad, double precio, double costo_total) {
        this.id = id;
        this.idMateriaPrima = idMateriaPrima;
        this.Nombre= Nombre;
        this.fechaMovimiento = fechaMovimiento;
        this.cantidad = cantidad;
        this.precio = precio;
        this.costo_total=costo_total;
    }

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
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public double getCostoTotal() {
        return costo_total;
    }

    // Setter para costo_total
    public void setCostoTotal(double costoTotal) {
        this.costo_total = costoTotal;
    }
}
