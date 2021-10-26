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
    PreparedStatement ps;
    ResultSet rs;
    Connection con;
    Conexion conectar = new Conexion();
    
    
    
    public ArrayList<Persona> getPersona() throws SQLException {
        con = conectar.getConnection();
        ps = con.prepareStatement("select * from prog_av.persona");
        rs = ps.executeQuery();
        ArrayList<Persona> listaPersona = new ArrayList<>();
        try {
            while (rs.next()) {
                Persona persona = new Persona();
                
                persona.setIdPersona(rs.getInt("id_persona"));
                persona.setNombre(rs.getString("nombre"));
                persona.setApellido(rs.getString("apellido"));
                persona.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
                persona.setTelefono(rs.getString("telefono"));
                
                
                listaPersona.add(persona);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ClientesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaPersona;
    }

    public ArrayList<Persona> listar() {
        ArrayList<Persona> datos = new ArrayList<>();
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement("select * from prog_av.persona");
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

    public int eliminar(int id) {
        int r = 0;
        String sql = "delete from prog_av.persona where id_persona =" + id;
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            r = ps.executeUpdate();
        } catch (Exception e) {
        }
        return r;
    }

    public int agregar(Persona persona) {
        int r = 0;
        System.out.println("paso");
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
