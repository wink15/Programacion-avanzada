/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersonalDAO {

    //SE REALIZA LA CONEXION CON LA BASE DE DATOS
    PreparedStatement ps;
    ResultSet rs;
    Connection con;
    Conexion conectar = new Conexion();

    //METODO PARA TRAER EL PERSONAL DESDE LA BD
    public ArrayList<Personal> getPersonal() throws SQLException {
        con = conectar.getConnection();
        //SE LLEVA A CABO LA CONSULTA A LA BD
        ps = con.prepareStatement("select * from prog_av.personal WHERE borrado = 0");
        rs = ps.executeQuery();
        //SE CREA UN ARRAY PERSONAL 
        ArrayList<Personal> listaPersonal = new ArrayList<>();
        try {
            while (rs.next()) {
                //SE SETEAN LOS DATOS DEL CLIENTE EN LA INSTANCIA CLIENTE CREADA
                Personal personal = new Personal();
                personal.setIdPersonal(rs.getInt("idPersonal"));
                personal.setCUIT(rs.getLong("cuit"));
                // personal.setPersona(rs.getInt("persona"));

                //LOS DATOS DEL CLIENTE SE ALMACENAN EL EL ARRAY CREADO
                listaPersonal.add(personal);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PersonalDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        //FINALMENTE DEVUELVE LA LISTA DE CLIENTES QUE SE TRAJO DESDE LA BD
        return listaPersonal;
    }

    //METODO QUE OBTIENE EL PERSONAL DESDE LA BD
    public ArrayList<Personal> listar() {
        //SE CREA UN ARRAY DE PERSONAL
        ArrayList<Personal> datosPersonal = new ArrayList<>();
        try {
            con = conectar.getConnection();
            //SE EJECUTA LA CONSULTA A LA BD
            ps = con.prepareStatement("select * from prog_av.personal WHERE borrado = 0");
            rs = ps.executeQuery();
            while (rs.next()) {
                //CADA REGISTRO QUE SE TRAE DE LA CONSULTA SE SETEA EN LA INSTANCIA CREADA
                Personal p = new Personal();
                p.setIdPersonal(rs.getInt(1));
                p.setCUIT(rs.getLong(2));
                p.setPersona(rs.getInt(3));

                //SE GUARDAN LOS DATOS DEL PERSONAL EN EL ARRAY CREADO
                datosPersonal.add(p);
            }
        } catch (Exception e) {
        }
        //FINALMENTE SE DEVUELVE EL ARRAY DE PERSONAL
        return datosPersonal;
    }

    //METODO PARA ELIMINAR UN PERSONAL DE LA BD
    public int eliminar(int id) {
        int r = 0;
        //SE DEFINE LA CONSULTA
        String sql = "UPDATE prog_av.personal SET borrado = 1 where idPersonal =" + id;
        try {
            //SE REALIZA LA CONEXION A LA BD
            con = conectar.getConnection();
            //SE LLEVA A CABO LA CONSULTA
            ps = con.prepareStatement(sql);
            r = ps.executeUpdate();
        } catch (Exception e) {
        }
        //SE DEVUELVE EL RESULTADO DE LA ELIMINACION DEL REGISTRO 
        return r;
    }

    //METODO PARA AGREGAR UN REGISTRO A LA BD
    public int agregar(Personal per) {
        int r = 0;
        //SE DEFINE LA CONSULTA
        String sql = "insert into prog_av.personal(cuit,persona) values (?,?)";
        try {
            //SE REALIZA LA CONEXION A LA BD
            con = conectar.getConnection();
            //SE EJECUTA LA CONSULTA
            ps = con.prepareStatement(sql);

            ps.setLong(1, per.getCUIT());
            ps.setInt(2, per.getPersona());

            r = ps.executeUpdate();

        } catch (Exception e) {
        }
        //SE RETORNA EL RESULTADO DE LA AGREGACION DEL REGISTRO A LA BD
        return r;
    }
    
    //METODO PARA CONSULTAR ANTES DE ELIMINAR UN PERSONAL DE LA BD REFERIDO AL PERSONAL_PERFIL
    public int consultaEliminacionPersonalPerfil(int id) {
        int consultaEliminacionPersonalPerfil = 0;
        
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement("SELECT COUNT(personal) as estaUtilizado FROM prog_av.personal_perfil WHERE borrado = 0 AND personal =" + id);
            rs = ps.executeQuery();
            while (rs.next()) {
                consultaEliminacionPersonalPerfil = rs.getInt("estaUtilizado");
            }
        } catch (Exception e) {
        }
        return consultaEliminacionPersonalPerfil;
    }
    //ETODO PARA CONSULTAR ANTES DE ELIMINAR UN PERSONAL DE LA BD REFERIDO AL PERSONAL_PROYECTO
    public int consultaEliminacionPersonalProyecto(int id) {
        int consultaEliminacionPersonalProyecto = 0;
        
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement("SELECT COUNT(personal) as estaUtilizado FROM prog_av.personal_proyecto WHERE borrado = 0 AND personal =" + id);
            rs = ps.executeQuery();
            while (rs.next()) {
                consultaEliminacionPersonalProyecto = rs.getInt("estaUtilizado");
            }
        } catch (Exception e) {
        }
        return consultaEliminacionPersonalProyecto;
    }
    
    //METODO PARA CONSULTAR ANTES DE AGREGAR UN PERSONAL REFERIDO A LA PERSONA
    public int consultaAgregacion(int id) {
        int consultaAgregacion = 0;
        
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement("SELECT COUNT(persona) as yaExiste FROM prog_av.personal WHERE borrado = 0 AND persona =" + id);
            rs = ps.executeQuery();
            while (rs.next()) {
                consultaAgregacion = rs.getInt("yaExiste");
            }
        } catch (Exception e) {
        }
        return consultaAgregacion;
    }
    //METODO PARA CONSULTAR ANTES DE AGREGAR UN PERSONAL REFERIDO AL CUIT
    public int consultaAgregacionCUIT(long id) {
        int consultaAgregacionCUIT = 0;
        
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement("SELECT COUNT(cuit) as yaExiste FROM prog_av.personal WHERE borrado = 0 AND cuit =" + id);
            rs = ps.executeQuery();
            while (rs.next()) {
                consultaAgregacionCUIT = rs.getInt("yaExiste");
            }
        } catch (Exception e) {
        }
        return consultaAgregacionCUIT;
    }
    
    public int actualizar(Personal pers) {
        int r = 0;
        //SE DEFINE LA CONSULTA
        String sql = "UPDATE prog_av.personal SET cuit = ?, persona = ? WHERE idPersonal =  " + pers.getIdPersonal();

        try {
            //SE REALIZA LA CONEXION A LA BD
            con = conectar.getConnection();
            //SE EJECUTA LA CONSULTA
            ps = con.prepareStatement(sql);

            ps.setLong(1, pers.getCUIT());
            ps.setInt(2, pers.getPersona());

            //SE GUARDA EL RESULTADO DE LA CONSULTA
            r = ps.executeUpdate();

        } catch (Exception e) {

        }
        //FINALMENTE SE RETORNA EL RESULTADO DE LA ACTUALIZACION DEL REGISTRO
        return r;
    }
}
