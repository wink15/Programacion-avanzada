/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
            ps = con.prepareStatement("select * from prog_av.proyecto WHERE borrado = 0");
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
                p.setMonto(rs.getDouble(9));

                p.setUbicacion(rs.getInt(10));

                //SE GUARDAN LOS DATOS DEL PROYECTO EN EL ARRAY CREADO
                datos.add(p);
            }
        } catch (Exception e) {
        }
        //FINALMENTE SE DEVUELVE EL ARRAY DE PROYECTOS
        return datos;
    }

    public ArrayList<Proyecto> filtroBusqueda(String parametro, int opc) {
        //SE CREA UN ARRAY DE PROYECTO
        ArrayList<Proyecto> datos = new ArrayList<>();
        try {
            con = conectar.getConnection();
            //SE EJECUTA LA CONSULTA A LA BD
            if (opc == 1) {
                ps = con.prepareStatement("select * from prog_av.proyecto WHERE borrado = 0");
            } else if (opc == 2) {
                System.out.println("entro");
                ps = con.prepareStatement("select * from prog_av.proyecto WHERE borrado = 0 AND proyecto.idproyecto LIKE '%" +parametro+"%'");
            } else {
                ps = con.prepareStatement("select * from prog_av.proyecto WHERE borrado = 0 AND upper(nombre) LIKE '%" + parametro.toUpperCase() + "%'");
            }

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
                p.setMonto(rs.getDouble(9));

                p.setUbicacion(rs.getInt(10));

                //SE GUARDAN LOS DATOS DEL PROYECTO EN EL ARRAY CREADO
                datos.add(p);
            }
        } catch (Exception e) {
        }
        //FINALMENTE SE DEVUELVE EL ARRAY DE PROYECTOS
        return datos;
    }

    public ArrayList<Proyecto> filtroBusquedaFechas(java.sql.Date parametro, java.sql.Date parametro2,int opc, int opc2) {
        //SE CREA UN ARRAY DE PROYECTO
        /* DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String strDate = dateFormat.format(parametro);
        java.sql.Date fechaInicio = new java.sql.Date(parametro);*/

        // System.out.println(strDate);
        ArrayList<Proyecto> datos = new ArrayList<>();
        try {
            con = conectar.getConnection();
            //SE EJECUTA LA CONSULTA A LA BD
            if(opc2==0){
            if (opc == 1) {
                ps = con.prepareStatement("select * from prog_av.proyecto WHERE proyecto.borrado = 0 AND proyecto.fechaInicio ='" + parametro + "'");
            } else if (opc == 2) {
                ps = con.prepareStatement("select * from prog_av.proyecto WHERE proyecto.borrado = 0 AND proyecto.fechaConfirmacion ='" + parametro + "'");
            } else {
                ps = con.prepareStatement("select * from prog_av.proyecto WHERE proyecto.borrado = 0 AND proyecto.fechaFin ='" + parametro + "'");
            }}else{
                 if (opc == 1) {
                ps = con.prepareStatement("select * from prog_av.proyecto WHERE proyecto.borrado = 0 AND proyecto.fechaInicio BETWEEN '" + parametro +"'"+ "AND '"+ parametro2+"'");
            } else if (opc == 2) {
                ps = con.prepareStatement("select * from prog_av.proyecto WHERE proyecto.borrado = 0 AND proyecto.fechaConfirmacionBETWEEN '" + parametro +"'"+ "AND '"+ parametro2+"'");
            } else {
                ps = con.prepareStatement("select * from prog_av.proyecto WHERE proyecto.borrado = 0 AND proyecto.fechaFin BETWEEN '" + parametro +"'"+ "AND '"+ parametro2+"'");
            }
            }

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
                p.setMonto(rs.getDouble(9));

                p.setUbicacion(rs.getInt(10));

                //SE GUARDAN LOS DATOS DEL PROYECTO EN EL ARRAY CREADO
                datos.add(p);
            }
        } catch (Exception e) {
        }
        //FINALMENTE SE DEVUELVE EL ARRAY DE PROYECTOS
        return datos;
    }

    //METODO PARA ELIMINAR UN PROYECTO DE LA BD
    public int eliminar(int id) {
        int r = 0;
        //SE DEFINE LA CONSULTA
        String sql = "UPDATE prog_av.proyecto SET borrado = 1 where idproyecto=" + id;
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
    public int agregar(Proyecto per) {
        int r = 0;
        //SE DEFINE LA CONSULTA
        String sql = "insert into prog_av.proyecto(nombre,fechaInicio,fechaConfirmacion,fechaFin,tipoProyecto,cliente,observacion,monto,ubicacion) values (?,?,?,?,?,?,?,?,?);";
        try {
            //SE REALIZA LA CONEXION A LA BD
            con = conectar.getConnection();
            //SE EJECUTA LA CONSULTA
            ps = con.prepareStatement(sql);

            ps.setString(1, per.getNombre());
            ps.setDate(2, per.getFechaInicio());
            ps.setDate(3, per.getFechaConfirmacion());
            ps.setDate(4, per.getFechaFin());
            ps.setInt(5, per.getTipoProyecto());
            ps.setInt(6, per.getCliente());
            ps.setString(7, per.getObservacion());
            ps.setDouble(8, per.getMonto());
            ps.setInt(9, per.getUbicacion());

            r = ps.executeUpdate();

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
        int r = 0;
        //SE DEFINE LA CONSULTA
        String sql = "UPDATE prog_av.proyecto SET nombre = ?, fechaInicio = ?, fechaConfirmacion = ?, fechaFin = ?, tipoProyecto = ?, cliente = ?, observacion = ?, monto= ? , ubicacion= ? WHERE idproyecto =  " + per.getIdProyecto();

        try {
            //SE REALIZA LA CONEXION A LA BD
            con = conectar.getConnection();
            //SE EJECUTA LA CONSULTA
            ps = con.prepareStatement(sql);

            ps.setString(1, per.getNombre());
            ps.setDate(2, per.getFechaInicio());
            ps.setDate(3, per.getFechaConfirmacion());
            ps.setDate(4, per.getFechaFin());
            ps.setInt(5, per.getTipoProyecto());
            ps.setInt(6, per.getCliente());
            ps.setString(7, per.getObservacion());
            ps.setDouble(8, per.getMonto());
            ps.setInt(9, per.getUbicacion());

            //SE GUARDA EL RESULTADO DE LA CONSULTA
            r = ps.executeUpdate();

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
