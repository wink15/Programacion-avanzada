/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.Clientes;
import modelo.ClientesDAO;
import modelo.Perfil;
import modelo.PerfilDAO;
import modelo.Persona;
import modelo.PersonaDAO;
import vista.VistaClientes;
import vista.VistaPerfil;
/**
 *
 * @author Santiago
 */
public class ControladorPerfil implements ActionListener {
    
    //VECTOR QUE CONTIENE LOS BOTONES PARA LAS CONSULTAS AL USUARIO
    String [] botones = { "Aceptar", "Cancelar"};
    
    //Constructor sin parametros 
    public ControladorPerfil () {
    }
    
    //CREAMOS INSTANCIAS DE OTRAS CLASES
    PerfilDAO daoPerfil = new PerfilDAO();
    VistaPerfil vista3= new VistaPerfil();
    DefaultTableModel modelo = new DefaultTableModel();
    Perfil perfil = new Perfil();
    
    //CREAMOS UN CONTROLADOR POR CADA VISTA EXISTENTE
    public ControladorPerfil (VistaPerfil v){
        this.vista3 = v;
        this.vista3.btnAgregar.addActionListener(this);
        this.vista3.btnActualizar.addActionListener(this);
        this.vista3.btnBuscar.addActionListener(this);
        this.vista3.btnEliminar.addActionListener(this);
        this.vista3.btnModificar.addActionListener(this);
    }
    
    //EJECUCION DE CADA BOTON DENTRO DE CADA PANTALLA
    public void actionPerformed(ActionEvent e) {
        //VISTA Cliente
        if (e.getSource() == vista3.btnBuscar) {
            //SE LIMPIA LA TABLA QUE CONTIENE LOS TIPOS DE PROYECTO
            limpiarTabla();
            //SE BUSCAN TODOS LOS TIPOS DE PROYECTO DE LA BD
            buscarPerfil(vista3.tablaPerfil);
        }
         
        if (e.getSource() == vista3.btnEliminar) {
            //SE ELIMINA UN TIPO DE PROYECTO
            eliminarPerfil();
            //SE BUSCAN LOS NUEVOS TIPOS DE PROYECTO
            buscarPerfil(vista3.tablaPerfil);
        }
        
        if (e.getSource() == vista3.btnAgregar) {
            //SE VALIDA QUE LOS CAMPOS NO ESTEN VACIOS
            if (vista3.txtNombre.getText().equals("") || vista3.txtDescripcion.getText().equals("")){
                JOptionPane.showMessageDialog(null,"Debe completar todos los campos");
            } else {
                //SE CONSULTA AL USUARIO SI REALMENTE QUIERE AGREGAR UN TIPO DE PROYECTO
                int variable = JOptionPane.showOptionDialog (null, "¿Deseas agregar un cliente?", "Agregacion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]); 

                if (variable == 0) {
                //SE AGREGA UN TIPO DE PROYECTO
                agregarCliente();
                //SE BUSCAN LOS NUEVOS TIPOS DE PROYECTO
                buscarPerfil(vista3.tablaPerfil);
                //SE PREPARA LA VISTA PARA UN NUEVO TIPO DE PROYECTO
                nuevoPerfil();
                }else{
                    //EN CASO DE QUE QUIERA CANCELAR LA AGREGACION SE VACIAN LOS CAMPOS 
                    nuevoPerfil();
                }
            }
        }
        
        if (e.getSource() == vista3.btnModificar) {
            //SE VALIDA QUE SE HAYA SELECCIONADO UN TIPO DE PROYECTO
            int filaPerfil = vista3.tablaPerfil.getSelectedRow();
            if (filaPerfil == -1) {
                JOptionPane.showMessageDialog(vista3, "Debe seleccionar un cliente");
            } else {
                //SE DESHABILITAN LOS BOTONES
                vista3.btnAgregar.setEnabled(false);
                vista3.btnEliminar.setEnabled(false);
                vista3.btnActualizar.setEnabled(true);
                vista3.btnModificar.setEnabled(false);
                    
                //EN CASO DE QUE SI, SE PASAN LOS DATOS DE LA TABLA A VARIABLES
                int id = Integer.parseInt((String) vista3.tablaPerfil.getValueAt(filaPerfil, 0).toString());
                String nombre = (String) vista3.tablaPerfil.getValueAt(filaPerfil, 1);
                String  descripcion= (String) vista3.tablaPerfil.getValueAt(filaPerfil, 2).toString();
                
                
                //SE SETAN LOS DATOS DE LAS VARABLES A LOS CAMPOS DE LA VISTA
                vista3.txtID.setText("" + id);
                vista3.txtNombre.setText(nombre);
                vista3.txtDescripcion.setText(descripcion);
            }
        }
        
        //VISTA CLIENTE
        if (e.getSource() == vista3.btnActualizar) {
            //SE ACTUALIZA EL TIPO DE PROYECTO
            actualizarPerfil();
                    //SE BUSCAN LOS NUEVOS TIPOS DE PROYECTO
                    buscarPerfil(vista3.tablaPerfil);
                
            //SE PREPARA LA VISTA PARA UN NUEVO TIPO DE PROYECTO
            nuevoPerfil();
            //SE HABILITAN LOS BOTONES
            vista3.btnAgregar.setEnabled(true);
            vista3.btnEliminar.setEnabled(true);
            vista3.btnActualizar.setEnabled(false);
            vista3.btnModificar.setEnabled(true);
        }
    }
    
    // METODOS DE LA VISTA DE CLIENTE
    public void buscarPerfil(JTable tabla) {
        // centrarCeldas(tabla);
        //EN EL MODELO CREADO SE GUARDA EL MODELO DE LA TABLA TIPO DE PROYECTO
        modelo = (DefaultTableModel) tabla.getModel();
        tabla.setModel(modelo);
        //SE CREA UN ARRAY CON LOS TIPOS DE PROYECTOS QUE SE TRAEN DESDE LA BD
        ArrayList<Perfil> listaPerfil = daoPerfil.listar();
        //SE CREA UN ARRAY OBJETO QUE ALMACENA LOS DATOS DE CADA TIPO DE PROYECTO
        Object[] objeto = new Object[3];
        for (int i = 0; i < listaPerfil.size(); i++) {
            objeto[0] = listaPerfil.get(i).getId();
            objeto[1] = listaPerfil.get(i).getNombre();
            objeto[2] = listaPerfil.get(i).getDescripcion();
            
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
            
            String nombre = vista3.txtNombre.getText();
            String descripcion = vista3.txtDescripcion.getText();
            //SE SETEAN ESOS DATOS EN LA INSTANCIA DE TIPOPROYECTO CREADA
            
            perfil.setNombre(nombre);
            perfil.setDescripcion(descripcion);
           
            //SE TRAE EL RESULTADO DE LA AGREGACION A LA BD
            int r = daoPerfil.agregar(perfil);
            
            if (r == 1) {
                JOptionPane.showMessageDialog(vista3, "Cliente agregado con exito");
            } else {
                JOptionPane.showMessageDialog(vista3, "Error al agregar un cliente");
            }
            //POR ULTIMO SE LIMPIA LA TABLA DE TIPO DE PROYECTO
            limpiarTabla();
        } catch (HeadlessException e) {
        }
    }  
    
    //METODO PARA ELIMINAR UN REGISTRO TIPO DE PROYECTO    
    public void eliminarPerfil() {
        //SE VALIDA SI FUE SELECCIONADO UN TIPO DE PROYECTO
        int fila = vista3.tablaPerfil.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista3, "Debe seleccionar un perfil");
        } else {
            //EN CASO DE QUE SE HAYA SELECCIONADO UN TIPO DE PROYECTO, SE LE CONSULTA SI REALMENTE DESEA ELIMINARLO
            int variable = JOptionPane.showOptionDialog (null, "¿Deseas eliminar un prefil?", "Eliminacion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]); 
            if (variable == 0) {
                //SE LE PASA EL ID DEL TIPO DE PROYECTO SELECCIONADO PARA QUE SE UTILICE EN LA CONSULTA A LA BD
                int id = Integer.parseInt((String) vista3.tablaPerfil.getValueAt(fila, 0).toString());
                daoPerfil.eliminar(id);
                //SE LE INFORMA AL USUARIO QUE EL TIPO DE PROYECTO FUE ELIMINADO
                JOptionPane.showMessageDialog(vista3, "Perfil eliminado con exito");
            }
        }    
        //POR ULTIMO SE LIMPIA LA TABLA TIPO DE PROYECTO
        limpiarTabla();
    }
    
    //METODO PARA ACTUALIZAR UN TIPO DE PROYECTO
    public void actualizarPerfil() {
        //VALIDACION DE CAMPOS VACIOS NECESARIOS PARA ACTUALIZAR EL REGISTRO EN LA BD.  
        if (vista3.txtID.getText().equals("")) {
            JOptionPane.showMessageDialog(vista3, "Primero debe modificar un id");
        } else {
           //EN CASO DE QUE TODOS LOS CAMPOS ESTAN COMPLETOS, SE LE CONSULTA AL CLIENTE SI ESTA SEGURO DE MODIFICAR EL PROYECTO
           int variable = JOptionPane.showOptionDialog (null, "¿Deseas modificar un cliente?", "Cliente", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]); 
           //SI LA RESPUESTA ES QUE SI DESEA MODIFICARLO
           if (variable == 0) {
        
            //EN CASO QUE SE HAYA SELECCIONADO UN TIPO DE PROYECTO, SE GUARDA CADA DATO DE LOS CAMPOS DE LA VISTA EN VARIABLES
            String idAux= vista3.txtID.getText();
            int id = Integer.valueOf(idAux);
            String nombre = vista3.txtNombre.getText();
            String descripcion = vista3.txtDescripcion.getText();;
            
            //SE SETEAN ESOS DATOS EN LA INSTANCIA DE TIPOPROYECTO CREADA
            
            perfil.setId(id);
            perfil.setNombre(nombre);
            perfil.setDescripcion(descripcion);
            
            
            //SE GUARDA EL RESULTADO DE LA ACTUALIZACION
                int r= daoPerfil.actualizar(perfil);
                if (r == 1) {
                    JOptionPane.showMessageDialog(vista3, "Cliente modificado con exito");
                } else {
                    JOptionPane.showMessageDialog(vista3, "Error al actualizar un cliente");
                }
            }    
        }
        //POR ULTIMO SE VACIA LA TABLA DE TIPO DE PROYECTO
        limpiarTabla();
    }
    
    void limpiarTabla() {
        //SE RECORRE TODA LA TABLA Y CADA REGISTRO LEIDO SE REMUEVE DE LA TABLA
        for (int i = 0; i < vista3.tablaPerfil.getRowCount(); i++) {
            modelo.removeRow(i);
            i = i - 1;
        }
    }  
     public void nuevoPerfil() {
        //SE VACIAN TODOS LOS CAMPOS QUE PERMITEN AGREGAR UN NUEVO TIPO DE PROYECTO
        vista3.txtID.setText("");
        vista3.txtNombre.setText("");
        vista3.txtDescripcion.setText("");
        
        
        vista3.txtDescripcion.requestFocus();
    }
    
    //METODO QUE CARGA EL COMBO PERSONA EN LA VISTA CLIENTE
}