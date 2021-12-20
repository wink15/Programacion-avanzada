/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

public class TipoProyecto {

    //ATRIBUTOS DE LA CLASE TIPO PROYECTO
    private int idTipoProyecto;
    private String nombre;
    private String descripcion;

    //METODOS SETER Y GETERS
    public int getIdTipoProyecto() {
        return idTipoProyecto;
    }

    public void setIdTipoProyecto(int idTipoProyecto) {
        this.idTipoProyecto = idTipoProyecto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    //METODOS CONTRUCTORES 
    public TipoProyecto(int idTipoProyecto, String nombre, String descripcion) {
        this.idTipoProyecto = idTipoProyecto;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public TipoProyecto() {
    }

    public TipoProyecto(int idTipoProyecto, String nombre) {
        this.idTipoProyecto = idTipoProyecto;
        this.nombre = nombre;
    }

    //METODO TOSTRING PARA VISUALIZAR LOS TIPOS DE PROYECTO
    public String toString() {
        return idTipoProyecto + "-" + nombre;
    }
}
