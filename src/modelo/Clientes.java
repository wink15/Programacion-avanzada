/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Date;

public class Clientes  extends Persona{
    //ATRIBUTOS DEL CLIENTE
    private int idCliente;
    private String razonSocial;
    private int persona;

    //METODOS SETERS Y GETERS
    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public int getPersona() {
        return persona;
    }

    public void setPersona(int persona) {
        this.persona = persona;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }
    
    //METODOS CONSTRUCTORES CLIENTES
    public Clientes(int idCliente, String razonSocial, int persona) {
        this.idCliente = idCliente;
        this.razonSocial = razonSocial;
        this.persona = persona;
    }
    
    public Clientes (int idCliente, String razonSocial){
        this.idCliente = idCliente;
        this.razonSocial = razonSocial;
    }

    public Clientes(int idCliente, String razonSocial, String nombre, String apellido, Date fechaNacimiento, String telefono) {
        super(nombre, apellido, fechaNacimiento, telefono);
        this.idCliente = idCliente;
        this.razonSocial = razonSocial;
    }
    
    public Clientes (){     
    }
    
    //METODO TOSTRING PARA PODER VISUALIZAR EL CLIENTE
    public String toString () {
        return idCliente + "-" + razonSocial; 
    }
}
