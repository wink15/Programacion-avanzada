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
public class ProyectoPersonal {
    private int id;
    private int idProyecto;
    private int idPersonal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public int getIdPersonal() {
        return idPersonal;
    }

    public void setIdPersonal(int idPersonal) {
        this.idPersonal = idPersonal;
    }

    public ProyectoPersonal(int idProyecto, int idPersonal) {
        this.idProyecto = idProyecto;
        this.idPersonal = idPersonal;
    }

    public ProyectoPersonal() {
    }
    
          
    
}
