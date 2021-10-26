/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

/**
 *
 * @author Santiago
 */
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import modelo.Persona;
import modelo.PersonaDAO;

import vista.VistaPersona;

/**
 *
 * @author leone
 */
public class ControladorPersona implements ActionListener {
    
    //VECTOR QUE CONTIENE LOS BOTONES PARA LAS CONSULTAS AL USUARIO
    String [] botones = { "Aceptar", "Cancelar"};
   
    //Constructor sin parametros 
    public ControladorPersona () {
         
    }
    
   
    //CREAMOS INSTANCIAS DE OTRAS CLASES
    PersonaDAO daoPersona = new PersonaDAO();
    VistaPersona vista= new VistaPersona();
    DefaultTableModel modelo = new DefaultTableModel();
    Persona per = new Persona();
    
    
    
       
    
      
    //CREAMOS UN CONTROLADOR POR CADA VISTA EXISTENTE
    public ControladorPersona (VistaPersona v){
        this.vista = v;
        
        this.vista.btnAgregar.addActionListener(this);
        this.vista.btnActualizar.addActionListener(this);
        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);
        this.vista.btnModificar.addActionListener(this);
    }
    
    //EJECUCION DE CADA BOTON DENTRO DE CADA PANTALLA
    public void actionPerformed(ActionEvent e) {
        //VISTA Cliente
        if (e.getSource() == vista.btnBuscar) {
            
            //SE LIMPIA LA TABLA QUE CONTIENE LOS TIPOS DE PROYECTO
            limpiarTablaPersona();
            //SE BUSCAN TODOS LOS TIPOS DE PROYECTO DE LA BD
            buscarPersona(vista.tablaPersona);
        }
        
        if (e.getSource() == vista.btnEliminar) {
            //SE ELIMINA UN TIPO DE PROYECTO
            eliminarPersona();
            //SE BUSCAN LOS NUEVOS TIPOS DE PROYECTO
            buscarPersona(vista.tablaPersona);
        }
        
        if (e.getSource() == vista.btnAgregar) {
            //SE VALIDA QUE LOS CAMPOS NO ESTEN VACIOS
             DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy/MM/dd");
             LocalDate ahora = LocalDate.now();
             
             java.util.Date date=vista.fechaNacimiento.getDate();
             LocalDate fechaNac = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
           //  System.out.println(fechaNac);
             Period edad = Period.between(fechaNac, ahora);
             
             
            if (vista.txtNombre.getText().equals("") || vista.txtApellido.getText().equals("") || vista.fechaNacimiento.getDate()==null || vista.txtTelefono.getText().equals("")){
                JOptionPane.showMessageDialog(null,"Debe completar todos los campos");
            }
            
        boolean isNumeric =  vista.txtTelefono.getText().matches("[+-]?\\d*(\\.\\d+)?");
               if (vista.txtTelefono.getText().length()<10 ||vista.txtTelefono.getText().length()>10 || isNumeric==false){
               JOptionPane.showMessageDialog(null,"Numero de telefono invalido"); }
               if (edad.getYears()<18){
               JOptionPane.showMessageDialog(null,"La persona que desea registar es menor de 18"); }
             
            else {
                //SE CONSULTA AL USUARIO SI REALMENTE QUIERE AGREGAR UN TIPO DE PROYECTO
                int variable = JOptionPane.showOptionDialog (null, "¿Deseas agregar una persona?", "Agregacion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]); 

                if (variable == 0) {
                //SE AGREGA UN TIPO DE PROYECTO
                agregarCliente();
                //SE BUSCAN LOS NUEVOS TIPOS DE PROYECTO
                buscarPersona(vista.tablaPersona);
                //SE PREPARA LA VISTA PARA UN NUEVO TIPO DE PROYECTO
                nuevoPersona();
                }else{
                    //EN CASO DE QUE QUIERA CANCELAR LA AGREGACION SE VACIAN LOS CAMPOS 
                    nuevoPersona();
                }
            }
        }
        
        if (e.getSource() == vista.btnModificar) {
            //SE VALIDA QUE SE HAYA SELECCIONADO UN TIPO DE PROYECTO
            int filaPersona = vista.tablaPersona.getSelectedRow();
            if (filaPersona == -1) {
                JOptionPane.showMessageDialog(vista,"Debe seleccionar una persona");
            } else {
                //SE DESHABILITAN LOS BOTONES
                vista.btnAgregar.setEnabled(false);
                vista.btnEliminar.setEnabled(false);
                vista.btnActualizar.setEnabled(true);
                vista.btnModificar.setEnabled(false);
                    
                //EN CASO DE QUE SI, SE PASAN LOS DATOS DE LA TABLA A VARIABLES
                int id = Integer.parseInt((String) vista.tablaPersona.getValueAt(filaPersona, 0).toString());
                String nombre = (String) vista.tablaPersona.getValueAt(filaPersona, 1);
                String apellido = ((String) vista.tablaPersona.getValueAt(filaPersona, 2).toString());
                Date fechaNacimiento = (Date) vista.tablaPersona.getValueAt(filaPersona, 3);
                String telefono = ((String) vista.tablaPersona.getValueAt(filaPersona, 4).toString());
                
                
                //SE SETAN LOS DATOS DE LAS VARABLES A LOS CAMPOS DE LA VISTA
                vista.txtId.setText("" + id);
                vista.txtNombre.setText(nombre);
                vista.txtApellido.setText(apellido);
                vista.fechaNacimiento.setDate(fechaNacimiento);
                vista.txtTelefono.setText(telefono);
            }
        }
        
        //VISTA CLIENTE
        if (e.getSource() == vista.btnActualizar ){
            //SE ACTUALIZA EL TIPO DE PROYECTO
            actualizarPersona();
                    //SE BUSCAN LOS NUEVOS TIPOS DE PROYECTO
                    buscarPersona(vista.tablaPersona);
                
            //SE PREPARA LA VISTA PARA UN NUEVO TIPO DE PROYECTO
            nuevoPersona();
            //SE HABILITAN LOS BOTONES
            vista.btnAgregar.setEnabled(true);
            vista.btnEliminar.setEnabled(true);
            vista.btnActualizar.setEnabled(false);
            vista.btnModificar.setEnabled(true);
        }
    }
    
    // METODOS DE LA VISTA DE CLIENTE
    public void buscarPersona(JTable tabla) {
        // centrarCeldas(tabla);
        //EN EL MODELO CREADO SE GUARDA EL MODELO DE LA TABLA TIPO DE PROYECTO
        modelo = (DefaultTableModel) tabla.getModel();
        tabla.setModel(modelo);
        //SE CREA UN ARRAY CON LOS TIPOS DE PROYECTOS QUE SE TRAEN DESDE LA BD
        ArrayList<Persona> listaPersonas = daoPersona.listar();
        
        //SE CREA UN ARRAY OBJETO QUE ALMACENA LOS DATOS DE CADA TIPO DE PROYECTO
        Object[] objeto = new Object[5];
        for (int i = 0; i < listaPersonas.size(); i++) {
           
            objeto[0] = listaPersonas.get(i).getIdPersona();
            objeto[1] = listaPersonas.get(i).getNombre();
            objeto[2] = listaPersonas.get(i).getApellido();
             objeto[3] = listaPersonas.get(i).getFechaNacimiento();
              objeto[4] = listaPersonas.get(i).getTelefono();
           
           
            
            //SE AGREGAN LOS TIPOS DE PROYECTO EN LAS FILAS DEL MODELO QUE SE LE SETEA A LA TABLA
            modelo.addRow(objeto);
        }
        //SE DEFINE LA CANTIDAD Y EL TAMAÑO DE LAS FILAS DE LA TABLA
        tabla.setRowHeight(35);
        tabla.setRowMargin(10);
    }

    //METODO PARA AGREGAR UN NUEVO TIPO DE PROYECTO
    public void agregarCliente() {
        try {
            //SE PASAN LOS VALORES DE LOS CAMPOS DE LA VISTA CLIENTE A VARIABLES
            
            String nombre = vista.txtNombre.getText();
             String apellido = vista.txtApellido.getText();
        
              java.util.Date date = vista.fechaNacimiento.getDate();
            long aux1 = date.getTime();
            java.sql.Date fechaNacimiento= new java.sql.Date(aux1);
             String telefono = vista.txtTelefono.getText();
            //SE SETEAN ESOS DATOS EN LA INSTANCIA DE TIPOPROYECTO CREADA
            
          per.setNombre(nombre);
           per.setApellido(apellido);
           per.setFechaNacimiento(fechaNacimiento);
           per.setTelefono(telefono);
            //SE TRAE EL RESULTADO DE LA AGREGACION A LA BD
            int r = daoPersona.agregar(per);
            
            if (r == 1) {
                JOptionPane.showMessageDialog(vista, "Persona agregada con exito");
            } else {
                JOptionPane.showMessageDialog(vista, "Error al agregar una persona");
            }
            //POR ULTIMO SE LIMPIA LA TABLA DE TIPO DE PROYECTO
            limpiarTablaPersona();
        } catch (HeadlessException e) {
        }
    }  
    
    //METODO PARA ELIMINAR UN REGISTRO TIPO DE PROYECTO    
    public void eliminarPersona() {
        //SE VALIDA SI FUE SELECCIONADO UN TIPO DE PROYECTO
        int fila = vista.tablaPersona.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Debe seleccionar una persona");
        } else {
            //EN CASO DE QUE SE HAYA SELECCIONADO UN TIPO DE PROYECTO, SE LE CONSULTA SI REALMENTE DESEA ELIMINARLO
            int variable = JOptionPane.showOptionDialog (null, "¿Deseas eliminar una persona?", "Eliminacion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]); 
            if (variable == 0) {
                //SE LE PASA EL ID DEL TIPO DE PROYECTO SELECCIONADO PARA QUE SE UTILICE EN LA CONSULTA A LA BD
                int id = Integer.parseInt((String) vista.tablaPersona.getValueAt(fila, 0).toString());
                daoPersona.eliminar(id);
                //SE LE INFORMA AL USUARIO QUE EL TIPO DE PROYECTO FUE ELIMINADO
                JOptionPane.showMessageDialog(vista, "Persona eliminada con exito");
            }
        }    
        //POR ULTIMO SE LIMPIA LA TABLA TIPO DE PROYECTO
        limpiarTablaPersona();
    }
    
    //METODO PARA ACTUALIZAR UN TIPO DE PROYECTO
    public void actualizarPersona() {
        //VALIDACION DE CAMPOS VACIOS NECESARIOS PARA ACTUALIZAR EL REGISTRO EN LA BD.  
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy/MM/dd");
             LocalDate ahora = LocalDate.now();
             
             java.util.Date date1=vista.fechaNacimiento.getDate();
             LocalDate fechaNac = Instant.ofEpochMilli(date1.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
           //  System.out.println(fechaNac);
             Period edad = Period.between(fechaNac, ahora);
        if (vista.txtId.getText().equals("")) {
            JOptionPane.showMessageDialog(vista, "Primero debe modificar una persona");
        } if (edad.getYears()<18){
               JOptionPane.showMessageDialog(null,"La persona que desea registar es menor de 18"); } else {
           //EN CASO DE QUE TODOS LOS CAMPOS ESTAN COMPLETOS, SE LE CONSULTA AL CLIENTE SI ESTA SEGURO DE MODIFICAR EL PROYECTO
           int variable = JOptionPane.showOptionDialog (null, "¿Deseas modificar una persona?", "Persona", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]); 
           //SI LA RESPUESTA ES QUE SI DESEA MODIFICARLO
           if (variable == 0) {
        
            //EN CASO QUE SE HAYA SELECCIONADO UN TIPO DE PROYECTO, SE GUARDA CADA DATO DE LOS CAMPOS DE LA VISTA EN VARIABLES
            String idAux= vista.txtId.getText();
            int id = Integer.valueOf(idAux);
             String nombre = vista.txtNombre.getText();
              String apellido = vista.txtApellido.getText();
            java.util.Date date = vista.fechaNacimiento.getDate();
                long aux1=date.getTime();
             java.sql.Date fechaNacimiento= new java.sql.Date(aux1);
              String telefono = vista.txtTelefono.getText();
            
            //SE SETEAN ESOS DATOS EN LA INSTANCIA DE TIPOPROYECTO CREADA
            
            per.setIdPersona(id);
            per.setNombre(nombre);
            per.setApellido(apellido);
            per.setFechaNacimiento(fechaNacimiento);
            per.setTelefono(telefono);
            
            
            //SE GUARDA EL RESULTADO DE LA ACTUALIZACION
                int r= daoPersona.actualizar(per);
                if (r == 1) {
                    JOptionPane.showMessageDialog(vista, "Persona modificada con exito");
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al actualizar una persona");
                }
            }    
        }
        //POR ULTIMO SE VACIA LA TABLA DE TIPO DE PROYECTO
        limpiarTablaPersona();
    }
    
    void limpiarTablaPersona() {
        //SE RECORRE TODA LA TABLA Y CADA REGISTRO LEIDO SE REMUEVE DE LA TABLA
        for (int i = 0; i < vista.tablaPersona.getRowCount(); i++) {
            modelo.removeRow(i);
            i = i - 1;
        }
    }  
     public void nuevoPersona() {
        //SE VACIAN TODOS LOS CAMPOS QUE PERMITEN AGREGAR UN NUEVO TIPO DE PROYECTO
        vista.txtId.setText("");
        vista.txtNombre.setText("");
          vista.txtApellido.setText("");
            vista.txtTelefono.setText("");
           vista.fechaNacimiento.setDate(null);
          
        
        
        vista.txtNombre.requestFocus();
    }}
    

