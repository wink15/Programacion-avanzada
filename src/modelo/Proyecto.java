/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Date;


public class Proyecto {
    //ATRIBUTOS DE LA CLASE PROYECTO
    private int idProyecto;
    private String nombre;
    private Date fechaInicio;
    private Date fechaConfirmacion;
    private Date fechaFin;
    private String observacion;
    private int tipoProyecto;
    private int cliente;
    private double monto;
    private int ubicacion;
   
    public int getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion( int ubicacion) {
       
        this.ubicacion = ubicacion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    //METODOS CONSTRUCTORES DE LA CLASE PROYECTO
    public Proyecto () { 
    }
    
    public Proyecto(int idProyecto, String nombre, Date fechaInicio, Date  fechaConfirmacion, Date  fechaFin, String observacion, int tipoProyecto, int cliente,double monto, int ubicacion) {
        this.idProyecto = idProyecto;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaConfirmacion = fechaConfirmacion;
        this.fechaFin = fechaFin;
        this.observacion = observacion;
        this.tipoProyecto = tipoProyecto;
        this.cliente = cliente;
        this.monto= monto;
        this.ubicacion= ubicacion;
    }
    
    //METODOS SETERS Y GETERS
    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date  getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date  fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date  getFechaConfirmacion() {
        return fechaConfirmacion;
    }

    public void setFechaConfirmacion(Date  fechaConfirmacion) {
        this.fechaConfirmacion = fechaConfirmacion;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date  fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public int getTipoProyecto() {
        return tipoProyecto;
    }

    public void setTipoProyecto(int tipoProyecto) {
        this.tipoProyecto = tipoProyecto;
    }

    public int getCliente() {
        return cliente;
    }

    public void setCliente(int cliente) {
        this.cliente = cliente;
    }
}
