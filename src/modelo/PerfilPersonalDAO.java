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
import modelo.Perfil;
/**
 *
 * @author leone
 */
public class PerfilPersonalDAO {
    //SE REALIZA LA CONEXION CON LA BASE DE DATOS
    PreparedStatement ps;
    ResultSet rs;
    Connection con;
    Conexion conectar = new Conexion();
    
    //METODO PARA AGREGAR UN REGISTRO A LA BD
    public int agregar(PerfilPersonal perfilPersonal ) {
        int r = 0;
        //SE DEFINE LA CONSULTA
        String sql = "insert into prog_av.personal_perfil (personal,perfil) values (?,?);";
       
        try {
            //SE REALIZA LA CONEXION A LA BD
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            //SE SETEAN LOS DATOS DE LA CONSULTA
            ps.setInt(1, perfilPersonal.getIdPersonal());
            ps.setInt(2, perfilPersonal.getPerfil());;
            //SE EJECUTA LA CONSULTA
            r = ps.executeUpdate();
            
            if (r == 1) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
        }
        //SE RETORNA EL RESULTADO DE LA AGREGACION DEL REGISTRO A LA BD
        return r;
    }
    
    //METODO QUE OBTIENE LOS PERFILES DESDE LA BD
    public ArrayList<Perfil> listar(int idPersonal) {
        //SE CREA UN ARRAY DE PERFIL
        ArrayList<Perfil> datos = new ArrayList<>();
        try {
            
            con = conectar.getConnection();
            //SE EJECUTA LA CONSULTA A LA BD
            ps = con.prepareStatement("select prog_av.perfil.idperfil,prog_av.perfil.nombre from prog_av.perfil inner join prog_av.personal_perfil on idperfil = perfil where prog_av.personal_perfil.personal =" + idPersonal);
            rs = ps.executeQuery();
            while (rs.next()) {
                //CADA REGISTRO QUE SE TRAE DE LA CONSULTA SE SETEA EN LA INSTANCIA CREADA
                Perfil per = new Perfil();
                per.setId(rs.getInt(1)); 
                per.setNombre(rs.getString(2));
                //SE GUARDAN LOS DATOS DEL PERFIL EN EL ARRAY CREADO
                datos.add(per);
            }
        } catch (Exception e) {
        }
        //FINALMENTE SE DEVUELVE EL ARRAY DE PERFILES
        return datos;
    }
    
    //METODO PARA ELIMINAR UN PROYECTO DE LA BD
    public int eliminar (int idPersonal, int idPerfil) {
        int r = 0;
        //SE DEFINE LA CONSULTA
        String sql = "delete from prog_av.personal_perfil where personal ="+ idPersonal + " and perfil ="+ idPerfil ;
       try {
           //SE REALIZA LA CONEXION A LA BD
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            //SE LLEVA A CABO LA CONSULTA
            r = ps.executeUpdate();
            
            if (r == 1) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
        }
        //SE DEVUELVE EL RESULTADO DE LA ELIMINACION DEL REGISTRO 
        return r;
    }
}
