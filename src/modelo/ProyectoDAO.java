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


public class ProyectoDAO {
    //SE REALIZA LA CONEXION CON LA BASE DE DATOS
    PreparedStatement ps;
    ResultSet rs;
    Connection con;
    Conexion conectar = new Conexion();
    
    //METODO QUE OBTIENE LOS PROYECTOS DESDE LA BD
    public ArrayList<Proyecto> listar() {
        //SE CREA UN ARRAY DE PROYECTO
        ArrayList<Proyecto> datos = new ArrayList<>();
        try {
            con = conectar.getConnection();
            //SE EJECUTA LA CONSULTA A LA BD
            ps = con.prepareStatement("select * from prog_av.proyecto");
            rs = ps.executeQuery();
            while (rs.next()) {
                //CADA REGISTRO QUE SE TRAE DE LA CONSULTA SE SETEA EN LA INSTANCIA CREADA
                Proyecto p = new Proyecto();
                p.setIdProyecto(rs.getInt(1));
                p.setNombre(rs.getString(2));
                p.setFechaInicio(rs.getDate(3));
                p.setFechaConfirmacion(rs.getDate(4));
                p.setFechaFin(rs.getDate(5));
                p.setTipoProyecto(rs.getInt(6));
                p.setCliente(rs.getInt(7));
                p.setObservacion(rs.getString(8));
                
                //SE GUARDAN LOS DATOS DEL PROYECTO EN EL ARRAY CREADO
                datos.add(p);
            }
        } catch (Exception e) {
        }
        //FINALMENTE SE DEVUELVE EL ARRAY DE PROYECTOS
        return datos;
    }
    
    //METODO PARA ELIMINAR UN PROYECTO DE LA BD
    public int eliminar(int id){
        int r=0;
        //SE DEFINE LA CONSULTA
        String sql="delete from prog_av.proyecto where idproyecto="+id;
        try {
            //SE REALIZA LA CONEXION A LA BD
            con=conectar.getConnection();
            //SE LLEVA A CABO LA CONSULTA
            ps=con.prepareStatement(sql);
            r= ps.executeUpdate();
        } catch (Exception e) {
        }
        //SE DEVUELVE EL RESULTADO DE LA ELIMINACION DEL REGISTRO 
        return r;
    }
    
    //METODO PARA AGREGAR UN REGISTRO A LA BD
    public int agregar(Proyecto per) {
        int r=0;
        //SE DEFINE LA CONSULTA
        String sql="insert into prog_av.proyecto(nombre,fechaInicio,fechaConfirmacion,fechaFin,tipoProyecto,cliente,observacion) values (?,?,?,?,?,?,?);";
        try {
            //SE REALIZA LA CONEXION A LA BD
            con = conectar.getConnection();
            //SE EJECUTA LA CONSULTA
            ps = con.prepareStatement(sql);
         
            ps.setString(1,per.getNombre());
            ps.setDate(2,per.getFechaInicio());
            ps.setDate(3,per.getFechaConfirmacion());
            ps.setDate(4,per.getFechaFin());
            ps.setInt(5,per.getTipoProyecto());
            ps.setInt(6,per.getCliente());
            ps.setString(7,per.getObservacion());
            
            
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
    
    //METODO PARA ACTUALIZA UN PROYECTO DE LA BD
    public int actualizar(Proyecto per) {
        int r=0;
        //SE DEFINE LA CONSULTA
        String sql="UPDATE prog_av.proyecto SET nombre = ?, fechaInicio = ?, fechaConfirmacion = ?, fechaFin = ?, tipoProyecto = ?, cliente = ?, observacion = ? WHERE idproyecto =  " + per.getIdProyecto() ;  
        
        try {
            //SE REALIZA LA CONEXION A LA BD
            con = conectar.getConnection();
            //SE EJECUTA LA CONSULTA
            ps = con.prepareStatement(sql);
            
            ps.setString(1,per.getNombre());
            ps.setDate(2,per.getFechaInicio());
            ps.setDate(3,per.getFechaConfirmacion());
            ps.setDate(4,per.getFechaFin());
            ps.setInt(5,per.getTipoProyecto());
            ps.setInt(6,per.getCliente());
            ps.setString(7,per.getObservacion());
           
            //SE GUARDA EL RESULTADO DE LA CONSULTA
            r=ps.executeUpdate();
            
            //if(r==1){
            //    return 1;
            //}
            //else{
            //    return 0;
            //}
            
        } catch (Exception e) {
         
        }
        //FINALMENTE SE RETORNA EL RESULTADO DE LA ACTUALIZACION DEL REGISTRO
        return r;
    }
}
