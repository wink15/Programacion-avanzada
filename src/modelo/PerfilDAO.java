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
import modelo.ClientesDAO;
import modelo.Conexion;

/**
 *
 * @author Santiago
 */
public class PerfilDAO {
     PreparedStatement ps;
    ResultSet rs;
    Connection con;
    Conexion conectar = new Conexion();
    
    
public ArrayList<Perfil> getPerfil() throws SQLException {
        con = conectar.getConnection();
        ps = con.prepareStatement("select * from prog_av.perfil");
        rs = ps.executeQuery();
        ArrayList<Perfil> listaPerfil = new ArrayList<>();
        try {
            while (rs.next()) {
                Perfil per = new Perfil();
                
                per.setId(rs.getInt("idperfil"));
                per.setNombre(rs.getString("nombre"));
                per.setDescripcion(rs.getString("descripcion"));
              
                
                
                listaPerfil.add(per);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ClientesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaPerfil;
    }

    public ArrayList<Perfil> listar() {
        ArrayList<Perfil> datos = new ArrayList<>();
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement("select * from perfil");
            rs = ps.executeQuery();
            while (rs.next()) {
               Perfil per = new Perfil();
                per.setId(rs.getInt(1));
                per.setNombre(rs.getString(2));
                per.setDescripcion(rs.getString(3));
               
                datos.add(per);
            }
        } catch (Exception e) {
        }
        return datos;

    }

    public int eliminar(int id) {
        int r = 0;
        String sql = "delete from perfil where idperfil =" + id;
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            r = ps.executeUpdate();
        } catch (Exception e) {
        }
        return r;
    }

    public int agregar(Perfil perfil) {
        int r = 0;
        System.out.println("paso");
        String sql = "insert into perfil(nombre,descripcion) values (?,?);";
       
        try {
            System.out.println("entro al try");
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            System.out.println("ps del try " + ps);

            ps.setString(1, perfil.getNombre());
            ps.setString(2, perfil.getDescripcion());;
            
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

    public int actualizar(Perfil perfil) {
        int r = 0;
        //String id = String.valueOf(per.getIdProyecto());

        String sql = "UPDATE perfil SET nombre = ?, descripcion= ? WHERE idperfil=  " + perfil.getId();
        System.out.println("sql" + sql);
        try {

            con = conectar.getConnection();
            ps = con.prepareStatement(sql);

            System.out.println("Entro ");
            ps.setString(1, perfil.getNombre());
            ps.setString(2, perfil.getDescripcion());
    
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

