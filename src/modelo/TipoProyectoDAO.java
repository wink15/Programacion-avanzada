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

public class TipoProyectoDAO {

    //SE REALIZA LA CONEXION CON LA BD
    PreparedStatement ps;
    ResultSet rs;
    Connection con;
    Conexion conectar = new Conexion();

    //METODO APRA TRAER LOS TIPO DE PROYECTO DESDE LA BD
    public ArrayList<TipoProyecto> getTipoProyecto() throws SQLException {
        con = conectar.getConnection();
        //SE LLEVA A CABO LA CONSULTA
        ps = con.prepareStatement("select * from prog_av.tipo_Proyecto WHERE borrado = 0");
        rs = ps.executeQuery();
        //SE CREA UN ARRAY DE TIPO DE PROYECTO
        ArrayList<TipoProyecto> listaTipoProyecto = new ArrayList<>();
        try {
            while (rs.next()) {
                //POR CADA REGISTRO QUE SE TRAE DESDE LA BD SE SETEA A LA INSTANCIA DE TIPOPROYECTO CREADA
                TipoProyecto tp = new TipoProyecto();
                tp.setIdTipoProyecto(rs.getInt("idTipoProyecto"));
                tp.setNombre(rs.getString("nombre"));
                tp.setDescripcion(rs.getString("descripcion"));
                listaTipoProyecto.add(tp);
            }

        } catch (SQLException ex) {
            Logger.getLogger(TipoProyectoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        //FINALMENTE SE DEVUELVE EL LISTADO DE TIPO DE PROYECTO
        return listaTipoProyecto;
    }

    //METODO PARA MOSTRAR LOS TIPO DE PROYECTO EN LA TABLA 
    public ArrayList<TipoProyecto> listar() {
        ArrayList<TipoProyecto> datos = new ArrayList<>();
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement("select * from prog_av.tipo_proyecto WHERE borrado = 0");
            rs = ps.executeQuery();
            while (rs.next()) {
                TipoProyecto p = new TipoProyecto();
                p.setIdTipoProyecto(rs.getInt(1));
                p.setNombre(rs.getString(2));
                p.setDescripcion(rs.getString(3));

                datos.add(p);
            }
        } catch (Exception e) {
        }
        return datos;

    }

    //METODO PARA ELIMINAR UN TIPO DE PROYECTO DE LA BD
    public int eliminar(int id) {
        int r = 0;
        //SE DEFINE LA CONSULTA
        String sql = "UPDATE prog_av.tipo_proyecto SET borrado = 1 where idTipoProyecto=" + id;
        try {
            //SE REALIZA LA CONEXION A LA BD
            con = conectar.getConnection();
            //SE EJECUTA LA CONSULTA
            ps = con.prepareStatement(sql);
            r = ps.executeUpdate();
        } catch (Exception e) {
        }
        //SE RETORNA EL RESULTADO DE LA ELIMINACION 
        return r;
    }

    //METODO PARA AGREGAR UN TIPO DE PROYECTO A LA BD
    public int agregar(TipoProyecto tipo) {
        int r = 0;
        //SE DEFINE LA CONSULTA
        String sql = "insert into prog_av.tipo_proyecto(nombre,descripcion) values (?,?);";

        try {
            //SE REALIZA LA CONEXION A LA BD
            con = conectar.getConnection();
            //SE EJECUTA LA CONSULTA
            ps = con.prepareStatement(sql);

            ps.setString(1, tipo.getNombre());
            ps.setString(2, tipo.getDescripcion());

            //SE GUARDA EL RESULTADO DE LA AGREGACION 
            r = ps.executeUpdate();

            //if(r==1){
            //    return 1;
            //}
            //else{
            //    return 0;
            //}
        } catch (Exception e) {
        }
        //FINALMENTE SE DEVUELVE EL RESULTADO DE AGREGACION DEL REGISTRO
        return r;
    }

    //METODO PARA ACTUALIZAR UN REGISTRO DE LA BD
    public int actualizar(TipoProyecto tipo) {
        int r = 0;
        //SE DEFINE LA CONSULTA
        String sql = "UPDATE prog_av.tipo_proyecto SET nombre = ?, descripcion= ? WHERE idTipoProyecto =  " + tipo.getIdTipoProyecto();

        try {
            //SE REALIZA LA CONEXION A LA BD
            con = conectar.getConnection();
            //SE EJECUTA LA CONSULTA
            ps = con.prepareStatement(sql);

            ps.setString(1, tipo.getNombre());
            ps.setString(2, tipo.getDescripcion());

            //SE GUARDA EL RESULTADO DE LA ACTUALIZACION DEL REGISTRO
            r = ps.executeUpdate();

        } catch (Exception e) {

        }
        //FINALMENTE SE RETORNA EL RESULTADO DE LA ACTUALIZACION DEL REGISTRO
        return r;
    }
}
