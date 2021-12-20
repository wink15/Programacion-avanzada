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
public class ProyectoPerfil {

    private int id;
    private int idProyecto;
    private int idPerfil;
//GETTERS AND SETTERS

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

    public int getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }
//CONSTRUCTORES

    public ProyectoPerfil() {
    }

    public ProyectoPerfil(int idProyecto, int idPerfil) {
        this.idProyecto = idProyecto;
        this.idPerfil = idPerfil;
    }

}
