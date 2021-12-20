/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author leone
 */
public class PerfilPersonal {

    private int idPersonal;
    private int perfil;
//CONTRSTUCTOR

    public PerfilPersonal() {
    }
//GETTERS AND SETTERS

    public int getIdPersonal() {
        return idPersonal;
    }

    public void setIdPersonal(int idPersonal) {
        this.idPersonal = idPersonal;
    }

    public int getPerfil() {
        return perfil;
    }

    //CONSTRUCTOR PARA MOSTRAR EN LISTA
    public PerfilPersonal(int perfil) {
        this.perfil = perfil;
    }

    public void setPerfil(int perfil) {
        this.perfil = perfil;
    }

}
