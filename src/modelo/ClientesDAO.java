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

public class ClientesDAO {
    //SE REALIZA LA CONEXION CON LA BASE DE DATOS
    PreparedStatement ps;
    ResultSet rs;
    Connection con;
    Conexion conectar = new Conexion();
    
    //METODO PARA TRAER LOS CLIENTES DESDE LA BD
    public ArrayList<Clientes> getClientes() throws SQLException{
        con=conectar.getConnection();
        //SE LLEVA A CABO LA CONSULTA A LA BD
        ps = con.prepareStatement("select * from prog_av.clientes");
        rs = ps.executeQuery();
        //SE CREA UN ARRAY CLIENTES 
        ArrayList<Clientes> listaClientes = new ArrayList<>();
        try {
            while (rs.next()) {  
                //SE SETEAN LOS DATOS DEL CLIENTE EN LA INSTANCIA CLIENTE CREADA
                Clientes cliente = new Clientes();
                cliente.setIdCliente(rs.getInt("idClientes"));
                cliente.setRazonSocial(rs.getString("razonSocial"));
                //LOS DATOS DEL CLIENTE SE ALMACENAN EL EL ARRAY CREADO
                listaClientes.add(cliente);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClientesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        //FINALMENTE DEVUELVE LA LISTA DE CLIENTES QUE SE TRAJO DESDE LA BD
        return listaClientes;
    }
    
    //METODO QUE OBTIENE LOS PROYECTOS DESDE LA BD
    public ArrayList<Clientes> listar() {
        //SE CREA UN ARRAY DE PROYECTO
        ArrayList<Clientes> datosClientes = new ArrayList<>();
        try {
            con = conectar.getConnection();
            //SE EJECUTA LA CONSULTA A LA BD
            ps = con.prepareStatement("select * from prog_av.clientes");
            rs = ps.executeQuery();
            while (rs.next()) {
                //CADA REGISTRO QUE SE TRAE DE LA CONSULTA SE SETEA EN LA INSTANCIA CREADA
                Clientes c = new Clientes();
                c.setIdCliente(rs.getInt(1));
                c.setRazonSocial(rs.getString(2));
                c.setPersona(rs.getInt(3));
                
                //SE GUARDAN LOS DATOS DEL PROYECTO EN EL ARRAY CREADO
                datosClientes.add(c);
            }
        } catch (Exception e) {
        }
        //FINALMENTE SE DEVUELVE EL ARRAY DE PROYECTOS
        return datosClientes;
    }
    
    //METODO PARA ELIMINAR UN CLIENTE DE LA BD
    public int eliminar(int id){
        int r=0;
        //SE DEFINE LA CONSULTA
        String sql="delete from prog_av.clientes where idClientes ="+id;
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
    public int agregar(Clientes clie) {
        int r=0;
        //SE DEFINE LA CONSULTA
        String sql="insert into prog_av.clientes(razonSocial,persona) values (?,?);";
        try {
            //SE REALIZA LA CONEXION A LA BD
            con = conectar.getConnection();
            //SE EJECUTA LA CONSULTA
            ps = con.prepareStatement(sql);
         
            ps.setString(1,clie.getRazonSocial());
            ps.setInt(2,clie.getPersona());
           
            r=ps.executeUpdate();
           
        } catch (Exception e) {
        }
        //SE RETORNA EL RESULTADO DE LA AGREGACION DEL REGISTRO A LA BD
        return r;
    }
    
    public int actualizar(Clientes clie) {
        int r=0;
        //SE DEFINE LA CONSULTA
        String sql="UPDATE prog_av.clientes SET razonSocial = ?, persona = ? WHERE idClientes =  " + clie.getIdCliente();  
        
        try {
            //SE REALIZA LA CONEXION A LA BD
            con = conectar.getConnection();
            //SE EJECUTA LA CONSULTA
            ps = con.prepareStatement(sql);
            
            ps.setString(1,clie.getRazonSocial());
            ps.setInt(2,clie.getPersona());
            
            //SE GUARDA EL RESULTADO DE LA CONSULTA
            r=ps.executeUpdate();

        } catch (Exception e) {
         
        }
        //FINALMENTE SE RETORNA EL RESULTADO DE LA ACTUALIZACION DEL REGISTRO
        return r;
    }
}   
