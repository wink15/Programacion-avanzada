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

public class PersonaDAO {

    //SE REALIZA LA CONEXION CON LA BASE DE DATOS
    PreparedStatement ps;
    ResultSet rs;
    Connection con;
    Conexion conectar = new Conexion();

    //METODO PARA TRAER LOS DATOS DESDE LA BD
    public ArrayList<Persona> getPersona() throws SQLException {
        con = conectar.getConnection();
        //SE LLEVA A CABO LA CONSULTA A LA BD
        ps = con.prepareStatement("select * from prog_av.persona WHERE borrado = 0");
        rs = ps.executeQuery();
        //SE CREA UN ARRAY PERSONAS
        ArrayList<Persona> listaPersona = new ArrayList<>();
        try {
            while (rs.next()) {
                Persona persona = new Persona();
                //SE SETEAN LOS DATOS  EN LA INSTANCIA CLIENTE CREADA
                persona.setIdPersona(rs.getInt("id_persona"));
                persona.setNombre(rs.getString("nombre"));
                persona.setApellido(rs.getString("apellido"));
                persona.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
                persona.setTelefono(rs.getString("telefono"));

                //LOS DATOS  SE ALMACENAN EL EL ARRAY CREADO
                listaPersona.add(persona);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ClientesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        //FINALMENTE DEVUELVE LA LISTA DE PERSONAS QUE SE TRAJO DESDE LA BD
        return listaPersona;
    }

    //METODO QUE OBTIENE LLAS PERSONAS DESDE LA BD
    public ArrayList<Persona> listar() {
        ArrayList<Persona> datos = new ArrayList<>();
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement("select * from prog_av.persona WHERE borrado = 0");
            rs = ps.executeQuery();
            while (rs.next()) {
                Persona persona = new Persona();
                persona.setIdPersona(rs.getInt(1));
                persona.setNombre(rs.getString(2));
                persona.setApellido(rs.getString(3));
                persona.setFechaNacimiento(rs.getDate(4));
                persona.setTelefono(rs.getString(5));
                datos.add(persona);
            }
        } catch (Exception e) {
        }
        return datos;

    }
    //METODO PARA ELIMINAR UNA PERSONA DE LA BD

    public int eliminar(int id) {
        int r = 0;
        //SE DEFINE LA CONSULTA
        String sql = "UPDATE prog_av.persona SET borrado = 1 where id_persona =" + id;
        try {
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
    public int agregar(Persona persona) {
        int r = 0;
        // System.out.println("paso");
        String sql = "insert into prog_av.persona(nombre,apellido,fecha_nacimiento,telefono) values (?,?,?,?);";

        try {
            System.out.println("entro al try");
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            System.out.println("ps del try " + ps);

            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getApellido());
            ps.setDate(3, persona.getFechaNacimiento());
            ps.setString(4, persona.getTelefono());

            System.out.println("ps " + ps);
            r = ps.executeUpdate();
            System.out.println("r " + r);
            if (r == 1) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            System.out.println("no entro al try");
        }
        return r;
    }
    
    //METODO PARA CONSULTAR ANTES DE ELIMINAR UNA PERSONA DE LA BD REFERIDO AL CLIENTE
    public int consultaEliminacionCliente(int id) {
        int consultaEliminacionCliente = 0;
        
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement("SELECT COUNT(persona) as estaUtilizado FROM prog_av.clientes WHERE borrado = 0 AND persona =" + id);
            rs = ps.executeQuery();
            while (rs.next()) {
                consultaEliminacionCliente = rs.getInt("estaUtilizado");
            }
        } catch (Exception e) {
        }
        return consultaEliminacionCliente;
    }
    
    //METODO PARA CONSULTAR ANTES DE ELIMINAR UNA PERSONA DE LA BD REFERIDO AL PERSONAL
    public int consultaEliminacionPersonal(int id) {
        int consultaEliminacionPersonal = 0;
        
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement("SELECT COUNT(persona) as estaUtilizado FROM prog_av.personal WHERE borrado = 0 AND persona =" + id);
            rs = ps.executeQuery();
            while (rs.next()) {
                consultaEliminacionPersonal = rs.getInt("estaUtilizado");
            }
        } catch (Exception e) {
        }
        return consultaEliminacionPersonal;
    }

    //METODO PARA CONSULTAR ANTES DE ELIMINAR UNA PERSONA DE LA BD REFERIDO AL NOCLIENTE
    public int consultaEliminacionNoCliente(int id) {
        int consultaEliminacionNoCliente = 0;
        
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement("SELECT COUNT(persona) as estaUtilizado FROM prog_av.nocliente WHERE persona =" + id);
            rs = ps.executeQuery();
            while (rs.next()) {
                consultaEliminacionNoCliente = rs.getInt("estaUtilizado");
            }
        } catch (Exception e) {
        }
        return consultaEliminacionNoCliente;
    }

    public int actualizar(Persona persona) {
        int r = 0;
        //String id = String.valueOf(per.getIdProyecto());

        String sql = "UPDATE prog_av.persona SET nombre = ?, apellido= ?, fecha_nacimiento= ? , telefono=? WHERE id_persona =  " + persona.getIdPersona();
        System.out.println("sql" + sql);
        try {

            con = conectar.getConnection();
            ps = con.prepareStatement(sql);

            System.out.println("Entro ");
            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getApellido());
            ps.setDate(3, persona.getFechaNacimiento());
            ps.setString(4, persona.getTelefono());
            System.out.println("entro 22");
            r = ps.executeUpdate();
            System.out.println("r" + r);

            if (r == 1) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {

        }
        return r;
    }
}
