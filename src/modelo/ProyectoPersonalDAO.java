/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Santiago
 */
public class ProyectoPersonalDAO {

    PreparedStatement ps;
    ResultSet rs;
    Connection con;
    Conexion conectar = new Conexion();

    public ArrayList<ProyectoPersonal> listar() {
        //SE CREA UN ARRAY DE PROYECTOPERSONAL
        ArrayList<ProyectoPersonal> datos = new ArrayList<>();
        try {
            con = conectar.getConnection();
            //SE EJECUTA LA CONSULTA A LA BD
            ps = con.prepareStatement("select * from prog_av.personal_proyecto WHERE borrado = 0");
            rs = ps.executeQuery();
            while (rs.next()) {

                //CADA REGISTRO QUE SE TRAE DE LA CONSULTA SE SETEA EN LA INSTANCIA CREADA
                ProyectoPersonal pp = new ProyectoPersonal();
                pp.setId(rs.getInt(1));

                pp.setIdProyecto(rs.getInt(2));
                pp.setIdPersonal(rs.getInt(3));

                //SE GUARDAN LOS DATOS DEL PROYECTOPERSONAL EN EL ARRAY CREADO
                datos.add(pp);
            }
        } catch (Exception e) {
        }
        //FINALMENTE SE DEVUELVE EL ARRAY DE PROYECTOS
        return datos;
    }

    public int agregar(ProyectoPersonal pp) {
        int r = 0;
        //SE DEFINE LA CONSULTA
        String sql = "insert into prog_av.personal_proyecto(proyecto,personal) values (?,?);";

        try {
            //SE REALIZA LA CONEXION A LA BD
            con = conectar.getConnection();
            //SE EJECUTA LA CONSULTA
            ps = con.prepareStatement(sql);

            ps.setInt(1, pp.getIdProyecto());
            ps.setInt(2, pp.getIdPersonal());
            r = ps.executeUpdate();

        } catch (Exception e) {
        }
        //SE RETORNA EL RESULTADO DE LA AGREGACION DEL REGISTRO A LA BD
        return r;
    }

    public int eliminar(int id) {
        int r = 0;
        //SE DEFINE LA CONSULTA
        String sql = "UPDATE prog_av.personal_proyecto SET borrado = 1 where idpersonal_proyecto= " + id;
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

    public ArrayList<Persona> sugeridos(int idProy) {

        ArrayList<Persona> datos = new ArrayList<>();
        try {
            con = conectar.getConnection();
            //SE EJECUTA LA CONSULTA A LA BD
            //LA CONSULTA CONSISTE EN PRIMER LUGAR BUSCAR TODOS LOS PERFILES PARA ESE PROYECTO, EN SEGUNDO LUGAR  BUSCA EN LA TABLA PERSONAL A AQUELLOS QUE CONTENGAN ALGUNO DE LOS PERFILES QUE POSEE EL PROYECTO, Y POSTERIOR A ELLO BUSCA LAS PERSONAS QUE APARECEN EN DICHO FILTRADO, Y OBTIENE SU NOMBRE, APELLIDO E IDPERSONA
            ps = con.prepareStatement("SELECT nombre, apellido ,id_persona FROM prog_av.persona WHERE borrado = 0 AND id_persona = ANY (SELECT persona FROM prog_av.personal WHERE borrado = 0 AND idPersonal = ANY (SELECT DISTINCT personal FROM prog_av.personal_perfil WHERE borrado = 0 AND personal_perfil.perfil = ANY (SELECT perfil FROM prog_av.proyecto_perfil WHERE borrado = 0 AND proyecto = " + idProy + " )));");
            rs = ps.executeQuery();
            while (rs.next()) {

                //CADA REGISTRO QUE SE TRAE DE LA CONSULTA SE SETEA EN LA INSTANCIA CREADA
                Persona pp = new Persona();
                pp.setNombre(rs.getString(1));

                pp.setApellido(rs.getString(2));
                pp.setIdPersona(rs.getInt(3));

                //SE GUARDAN LOS DATOS DEL PROYECTO EN EL ARRAY CREADO
                datos.add(pp);
            }
        } catch (Exception e) {
        }
        //FINALMENTE SE DEVUELVE EL ARRAY DE PROYECTOS
        return datos;
    }

}
