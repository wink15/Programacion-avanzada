/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Santiago
 */
public class Perfil {

    private int id;
    private String nombre;
    private String descripcion;

    //GETTES AND SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    //CONTRUCTORES
    public Perfil() {
    }

    public Perfil(int id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Perfil(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;

    }

    //METODO TO STRING PARA MOSTRARLOS
    @Override
    public String toString() {
        return id + "-" + nombre;
    }

}
