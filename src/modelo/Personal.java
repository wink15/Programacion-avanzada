/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Date;
import javax.swing.JOptionPane;

public class Personal  extends Persona{
    //ATRIBUTOS DEL PERSONAL
    private int idPersonal;
    private int persona;
    private long CUIT;
    private int perfil;

    

    //METODOS SETERS Y GETERS
    public int getIdPersonal() {
        return idPersonal;
    }

    public void setIdPersonal(int idPersonal) {
        this.idPersonal = idPersonal;
    }

    public long getCUIT() {
        return CUIT;
    }

    public int getPersona() {
        return persona;
    }

    public void setPersona(int persona) {
        this.persona = persona;
    }

    public void setCUIT(long CUIT){
        String tamaño = Long.toString(CUIT);
        if(tamaño.length()== 11){
            this.CUIT = CUIT;
        }
    }
    public int getPerfil() {
        return perfil;
    }

    public void setPerfil(int perfil) {
        this.perfil = perfil;
    }
    
    //METODOS CONSTRUCTORES CLIENTES
    public Personal(int idPersonal, long CUIT, int persona) {
        this.idPersonal = idPersonal;
        this.CUIT = CUIT;
        this.persona = persona;
    }
    
    public Personal (int idPersonal, long CUIT){
        this.idPersonal = idPersonal;
        this.CUIT = CUIT;
    }

    public Personal(int idPersonal, long CUIT, String nombre, String apellido, Date fechaNacimiento, String telefono) {
        super(nombre, apellido, fechaNacimiento, telefono);
        this.idPersonal = idPersonal;
        this.CUIT = CUIT;
    }
    
    public Personal (){     
    }
    
    //METODO TOSTRING PARA PODER VISUALIZAR EL PERSONAL
    public String toString () {
        return idPersonal + "-" + CUIT; 
    }
}
