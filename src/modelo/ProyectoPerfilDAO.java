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
public class ProyectoPerfilDAO {
    
     PreparedStatement ps;
    ResultSet rs;
    Connection con;
    Conexion conectar = new Conexion();
    
     public ArrayList<ProyectoPerfil> listar() {
        //SE CREA UN ARRAY DE PROYECTO
        ArrayList<ProyectoPerfil> datos = new ArrayList<>();
        try {
            con = conectar.getConnection();
            //SE EJECUTA LA CONSULTA A LA BD
            ps = con.prepareStatement("select * from prog_av.proyecto_perfil");
            rs = ps.executeQuery();
            while (rs.next()) {
               
                //CADA REGISTRO QUE SE TRAE DE LA CONSULTA SE SETEA EN LA INSTANCIA CREADA
                ProyectoPerfil pp = new ProyectoPerfil();
                pp.setId(rs.getInt(1));
  
                pp.setIdProyecto(rs.getInt(2));
                pp.setIdPerfil(rs.getInt(3));
               
                 
                //SE GUARDAN LOS DATOS DEL PROYECTO EN EL ARRAY CREADO
                datos.add(pp);
            }
        } catch (Exception e) {
        }
        //FINALMENTE SE DEVUELVE EL ARRAY DE PROYECTOS
        return datos;
    }
    
    
    
    
     public int agregar(ProyectoPerfil pp) {
        int r=0;
        //SE DEFINE LA CONSULTA
        String sql="insert into prog_av.proyecto_perfil(proyecto,perfil) values (?,?);";
        
        try {
            //SE REALIZA LA CONEXION A LA BD
            con = conectar.getConnection();
            //SE EJECUTA LA CONSULTA
            ps = con.prepareStatement(sql);
        
        
            ps.setInt(1,pp.getIdProyecto());
            ps.setInt(2,pp.getIdPerfil());
            r=ps.executeUpdate();
            
            //SE RETORNA EL RESULTADO DE LA AGREGACION DEL REGISTRO A LA BD
            //if(r==1){
            //    return 1;
            //}
            //else{
            //    return 0;
            //}
        } catch (Exception e) {
        }
        //SE RETORNA EL RESULTADO DE LA AGREGACION DEL REGISTRO A LA BD
        return r;
    }
    
    
}
