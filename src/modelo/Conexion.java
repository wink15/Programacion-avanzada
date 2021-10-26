/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Conexion {
    //SE DEFINEN TODOS LOS PARAMETOS PARA LLEVAR A CABO LA CONEXION A LA BD
    String url="jdbc:mysql://localhost:3306/prog_av?useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    String user="root",pass="wink";
    Connection con;
    
    //METODO QUE EFECTUA Y DEVUELVE LA CONEXION CON LA BD
    public Connection getConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con=DriverManager.getConnection(url,user,pass);
        } catch (Exception e) {
        }
        return con;
    }
}

