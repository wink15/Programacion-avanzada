/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.TipoProyecto;
import vista.VistaTipoProyecto;
import modelo.TipoProyectoDAO;

/**
 *
 * @author leone
 */
public class ControladorTipoProyecto implements ActionListener {

    //VECTOR QUE CONTIENE LOS BOTONES PARA LAS CONSULTAS AL USUARIO
    String[] botones = {"Aceptar", "Cancelar"};

    //Constructor sin parametros 
    public ControladorTipoProyecto() {
    }

    //CREAMOS INSTANCIAS DE OTRAS CLASES
    TipoProyectoDAO daoTipo = new TipoProyectoDAO();
    VistaTipoProyecto vista2 = new VistaTipoProyecto();
    DefaultTableModel modelo = new DefaultTableModel();
    TipoProyecto tipo = new TipoProyecto();
    Controlador proyecto = new Controlador();

    public ControladorTipoProyecto(VistaTipoProyecto v) {
        this.vista2 = v;
        this.vista2.btnBuscarTipo.addActionListener(this);
        this.vista2.btnAgregarTipo.addActionListener(this);
        this.vista2.btnModificarTipo.addActionListener(this);
        this.vista2.btnEliminarTipo.addActionListener(this);
        this.vista2.btnActualizarTipo.addActionListener(this);
        this.vista2.btnEliminarTipo.setEnabled(false);
        this.vista2.btnModificarTipo.setEnabled(false);
    }

    //EJECUCION DE CADA BOTON DENTRO DE CADA PANTALLA
    public void actionPerformed(ActionEvent e) {
        //VISTA TIPO PROYECTO
        if (e.getSource() == vista2.btnBuscarTipo) {
            //SE LIMPIA LA TABLA QUE CONTIENE LOS TIPOS DE PROYECTO
            limpiarTablaTipo();
            //SE BUSCAN TODOS LOS TIPOS DE PROYECTO DE LA BD
            buscarTipo(vista2.jtableTipo);
            this.vista2.btnEliminarTipo.setEnabled(true);
        this.vista2.btnModificarTipo.setEnabled(true);
        }

        if (e.getSource() == vista2.btnEliminarTipo) {
            //SE ELIMINA UN TIPO DE PROYECTO
            eliminarTipo();
            //SE BUSCAN LOS NUEVOS TIPOS DE PROYECTO
            buscarTipo(vista2.jtableTipo);
        }

        if (e.getSource() == vista2.btnAgregarTipo) {
            //SE VALIDA QUE LOS CAMPOS NO ESTEN VACIOS
            if (vista2.txtNombreTipo.getText().equals("") || vista2.txtDescripcionTipo.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Debe completar todos los campos");
            }else if(this.esNumerico(vista2.txtNombreTipo.getText()) == true){
                JOptionPane.showMessageDialog(null, "El nombre no puede incluir numeros");
            } else {
                //SE CONSULTA AL USUARIO SI REALMENTE QUIERE AGREGAR UN TIPO DE PROYECTO
                int variable = JOptionPane.showOptionDialog(null, "¿Deseas agregar un tipo de proyecto?", "Agregacion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]);

                if (variable == 0) {
                    //SE AGREGA UN TIPO DE PROYECTO
                    agregarTipo();
                    //SE BUSCAN LOS NUEVOS TIPOS DE PROYECTO
                    buscarTipo(vista2.jtableTipo);
                    //SE PREPARA LA VISTA PARA UN NUEVO TIPO DE PROYECTO
                    nuevoTipoProyecto();
                    try {
                        proyecto.llenarTipoProyecto();
                    } catch (SQLException ex) {
                        Logger.getLogger(ControladorTipoProyecto.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    //EN CASO DE QUE QUIERA CANCELAR LA AGREGACION SE VACIAN LOS CAMPOS 
                    nuevoTipoProyecto();
                }
            }
        }

        if (e.getSource() == vista2.btnModificarTipo) {
            //SE VALIDA QUE SE HAYA SELECCIONADO UN TIPO DE PROYECTO
            int fila = vista2.jtableTipo.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(vista2, "Debe seleccionar un tipo de proyecto");
            } else {
                //SE DESHABILITAN LOS BOTONES
                vista2.btnAgregarTipo.setEnabled(false);
                vista2.btnEliminarTipo.setEnabled(false);
                vista2.btnActualizarTipo.setEnabled(true);
                vista2.btnModificarTipo.setEnabled(false);

                //EN CASO DE QUE SI, SE PASAN LOS DATOS DE LA TABLA A VARIABLES
                int id = Integer.parseInt((String) vista2.jtableTipo.getValueAt(fila, 0).toString());
                String nombre = (String) vista2.jtableTipo.getValueAt(fila, 1);
                String descripcion = (String) vista2.jtableTipo.getValueAt(fila, 2);

                //SE SETAN LOS DATOS DE LAS VARABLES A LOS CAMPOS DE LA VISTA
                vista2.txtIdTipo.setText("" + id);
                vista2.txtNombreTipo.setText(nombre);
                vista2.txtDescripcionTipo.setText(descripcion);
            }
        }

        if (e.getSource() == vista2.btnActualizarTipo) {
            //SE ACTUALIZA EL TIPO DE PROYECTO
            actualizarTipo();
            //SE BUSCAN LOS NUEVOS TIPOS DE PROYECTO
            buscarTipo(vista2.jtableTipo);
            //SE PREPARA LA VISTA PARA UN NUEVO TIPO DE PROYECTO
            nuevoTipoProyecto();
            //SE HABILITAN LOS BOTONES
            vista2.btnAgregarTipo.setEnabled(true);
            vista2.btnEliminarTipo.setEnabled(true);
            vista2.btnActualizarTipo.setEnabled(false);
            vista2.btnModificarTipo.setEnabled(true);
        }
    }

    //PREPARAR LA PANTALLA PARA UN NUEVO TIPO DE PROYECTO
    public void nuevoTipoProyecto() {
        //SE VACIAN TODOS LOS CAMPOS QUE PERMITEN AGREGAR UN NUEVO TIPO DE PROYECTO
        vista2.txtIdTipo.setText("");
        vista2.txtNombreTipo.setText("");
        vista2.txtDescripcionTipo.setText("");
        vista2.txtNombreTipo.requestFocus();
    }

    //--------------------------------------------------
    // METODOS DE LA VISTA DE TIPO DE PROYECTO
    public void buscarTipo(JTable tabla) {
        // centrarCeldas(tabla);
        //EN EL MODELO CREADO SE GUARDA EL MODELO DE LA TABLA TIPO DE PROYECTO
        modelo = (DefaultTableModel) tabla.getModel();
        tabla.setModel(modelo);
        //SE CREA UN ARRAY CON LOS TIPOS DE PROYECTOS QUE SE TRAEN DESDE LA BD
        ArrayList<TipoProyecto> lista = daoTipo.listar();
        //SE CREA UN ARRAY OBJETO QUE ALMACENA LOS DATOS DE CADA TIPO DE PROYECTO
        Object[] objeto = new Object[3];
        for (int i = 0; i < lista.size(); i++) {
            objeto[0] = lista.get(i).getIdTipoProyecto();
            objeto[1] = lista.get(i).getNombre();
            objeto[2] = lista.get(i).getDescripcion();
            //SE AGREGAN LOS TIPOS DE PROYECTO EN LAS FILAS DEL MODELO QUE SE LE SETEA A LA TABLA
            modelo.addRow(objeto);
        }
        //SE DEFINE LA CANTIDAD Y EL TAMAÑO DE LAS FILAS DE LA TABLA
        tabla.setRowHeight(35);
        tabla.setRowMargin(10);
    }

    //METODO PARA AGREGAR UN NUEVO TIPO DE PROYECTO
    public void agregarTipo() {
        try {
            //SE PASAN LOS VALORES DE LOS CAMPOS DE LA VISTA TIPO DE PROYECTO A VARIABLES
            String nombre = vista2.txtNombreTipo.getText();
            String descripcion = vista2.txtDescripcionTipo.getText();

            //SE SETEAN ESOS DATOS EN LA INSTANCIA DE TIPOPROYECTO CREADA
            tipo.setNombre(nombre);
            tipo.setDescripcion(descripcion);

            //SE TRAE EL RESULTADO DE LA AGREGACION A LA BD
            int r = daoTipo.agregar(tipo);

            if (r == 1) {
                JOptionPane.showMessageDialog(vista2, "Tipo de proyecto agregado con exito");
            } else {
                JOptionPane.showMessageDialog(vista2, "Error al agregar un tipo de proyecto");
            }
            //POR ULTIMO SE LIMPIA LA TABLA DE TIPO DE PROYECTO
            limpiarTablaTipo();
        } catch (HeadlessException e) {
        }
    }

    //METODO PARA ELIMINAR UN REGISTRO TIPO DE PROYECTO    
    public void eliminarTipo() {
        //SE VALIDA SI FUE SELECCIONADO UN TIPO DE PROYECTO
        int fila = vista2.jtableTipo.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista2, "Debe seleccionar un tipo de proyecto");
        } else {
            //EN CASO DE QUE SE HAYA SELECCIONADO UN TIPO DE PROYECTO, SE LE CONSULTA SI REALMENTE DESEA ELIMINARLO
            int variable = JOptionPane.showOptionDialog(null, "¿Deseas eliminar un tipo de proyecto?", "Eliminacion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]);
            if (variable == 0) {
                //SE LE PASA EL ID DEL TIPO DE PROYECTO SELECCIONADO PARA QUE SE UTILICE EN LA CONSULTA A LA BD
                int id = Integer.parseInt((String) vista2.jtableTipo.getValueAt(fila, 0).toString());
                System.out.print(daoTipo.consultaEliminacion(id));
                if(daoTipo.consultaEliminacion(id) == 0){
                    daoTipo.eliminar(id);
                    JOptionPane.showMessageDialog(vista2, "Tipo de proyecto eliminado con exito");
                }else{
                    JOptionPane.showMessageDialog(vista2, "El Tipo de proyecto no se puede eliminar debido a que esta siendo utilizado en uno o más proyectos");
                    JOptionPane.showMessageDialog(vista2, "Desasigne este tipo de proyecto del o los proyectos para poder eliminarlo");
                }
                //SE LE INFORMA AL USUARIO QUE EL TIPO DE PROYECTO FUE ELIMINADO
                
            }
        }
        //POR ULTIMO SE LIMPIA LA TABLA TIPO DE PROYECTO
        limpiarTablaTipo();
    }

    //METODO PARA ACTUALIZAR UN TIPO DE PROYECTO
    public void actualizarTipo() {
        //VALIDACION DE CAMPOS VACIOS NECESARIOS PARA ACTUALIZAR EL REGISTRO EN LA BD.  
        if (vista2.txtIdTipo.getText().equals("")) {
            JOptionPane.showMessageDialog(vista2, "Primero debe modificar un tipo de proyecto");
        } else {
            //EN CASO DE QUE TODOS LOS CAMPOS ESTAN COMPLETOS, SE LE CONSULTA AL CLIENTE SI ESTA SEGURO DE MODIFICAR EL PROYECTO
            int variable = JOptionPane.showOptionDialog(null, "¿Deseas modificar un tipo de proyecto?", "Tipo de proyecto", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]);
            //SI LA RESPUESTA ES QUE SI DESEA MODIFICARLO
            if (variable == 0) {

                //EN CASO QUE SE HAYA SELECCIONADO UN TIPO DE PROYECTO, SE GUARDA CADA DATO DE LOS CAMPOS DE LA VISTA EN VARIABLES
                String idAux = vista2.txtIdTipo.getText();
                int id = Integer.valueOf(idAux);

                String nombre = vista2.txtNombreTipo.getText();
                String descripcion = vista2.txtDescripcionTipo.getText();

                //SE SETEAN LOS DATOS EN LA INSTANCIA DE TIPO DE PROYECTO 
                tipo.setIdTipoProyecto(id);
                tipo.setNombre(nombre);
                tipo.setDescripcion(descripcion);

                //SE GUARDA EL RESULTADO DE LA ACTUALIZACION
                int r = daoTipo.actualizar(tipo);
                if (r == 1) {
                    JOptionPane.showMessageDialog(vista2, "Tipo de proyecto modificado con exito");
                } else {
                    JOptionPane.showMessageDialog(vista2, "Error al actualizar un tipo de proyecto");
                }
            }
        }
        //POR ULTIMO SE VACIA LA TABLA DE TIPO DE PROYECTO
        limpiarTablaTipo();
    }
    public boolean esNumerico(String num) {
        if (num.matches("[+-]?\\d*(\\.\\d+)?")) {
            return true;
        }

        return false;

    }
    

    //METODO PARA LIMPIAR LA TABLA TIPO DE PROYECTO
    void limpiarTablaTipo() {
        //SE RECORRE TODA LA TABLA Y CADA REGISTRO LEIDO SE REMUEVE DE LA TABLA
        for (int i = 0; i < vista2.jtableTipo.getRowCount(); i++) {
            modelo.removeRow(i);
            i = i - 1;
        }
    }

}
