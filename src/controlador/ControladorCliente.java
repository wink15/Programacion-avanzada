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
import modelo.Persona;
import modelo.PersonaDAO;
import vista.VistaClientes;

/**
 *
 * @author leone
 */
public class ControladorCliente implements ActionListener {

    //VECTOR QUE CONTIENE LOS BOTONES PARA LAS CONSULTAS AL USUARIO
    String[] botones = {"Aceptar", "Cancelar"};

    //Constructor sin parametros 
    public ControladorCliente() {
    }

    //CREAMOS INSTANCIAS DE OTRAS CLASES
    ClientesDAO daoCliente = new ClientesDAO();
    VistaClientes vista3 = new VistaClientes();
    DefaultTableModel modelo = new DefaultTableModel();
    Clientes cliente = new Clientes();

    //CREAMOS UN CONTROLADOR POR CADA VISTA EXISTENTE
    public ControladorCliente(VistaClientes v) {
        this.vista3 = v;
        this.vista3.btnAgregarClientes.addActionListener(this);
        this.vista3.btnActualizarClientes.addActionListener(this);
        this.vista3.btnBuscarClientes.addActionListener(this);
        this.vista3.btnEliminarClientes.addActionListener(this);
        this.vista3.btnModificarClientes.addActionListener(this);
    }

    //EJECUCION DE CADA BOTON DENTRO DE CADA PANTALLA
    public void actionPerformed(ActionEvent e) {
        //VISTA Cliente
        if (e.getSource() == vista3.btnBuscarClientes) {
            //SE LIMPIA LA TABLA QUE CONTIENE LOS clienteS
            limpiarTablaCliente();
            //SE BUSCAN TODOS LOS CLIENTES DE LA BD
            buscarCliente(vista3.tablaClientes);
        }

        if (e.getSource() == vista3.btnEliminarClientes) {
            //SE ELIMINA UN CLIENTE
            eliminarCliente();
            //SE BUSCAN LOS NUEVOS CLIENTES
            buscarCliente(vista3.tablaClientes);
        }

        if (e.getSource() == vista3.btnAgregarClientes) {
            //SE VALIDA QUE LOS CAMPOS NO ESTEN VACIOS
            if (vista3.txtRazonSocial.getText().equals("") || vista3.cboPersona.getSelectedItem().equals("")) {
                JOptionPane.showMessageDialog(null, "Debe completar todos los campos");
            } else {
                //SE CONSULTA AL USUARIO SI REALMENTE QUIERE AGREGAR UN CLIENTE
                int variable = JOptionPane.showOptionDialog(null, "¿Deseas agregar un cliente?", "Agregacion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]);

                if (variable == 0) {
                    //SE AGREGA UN CLIENTE
                    agregarCliente();
                    //SE BUSCAN LOS NUEVOS CLIENTES
                    buscarCliente(vista3.tablaClientes);
                    //SE PREPARA LA VISTA PARA UN NUEVO CLIENTES
                    nuevoCliente();
                } else {
                    //EN CASO DE QUE QUIERA CANCELAR LA AGREGACION SE VACIAN LOS CAMPOS 
                    nuevoCliente();
                }
            }
        }

        if (e.getSource() == vista3.btnModificarClientes) {
            //SE VALIDA QUE SE HAYA SELECCIONADO UN CLIENTE
            int filaCliente = vista3.tablaClientes.getSelectedRow();
            if (filaCliente == -1) {
                JOptionPane.showMessageDialog(vista3, "Debe seleccionar un cliente");
            } else {
                //SE DESHABILITAN LOS BOTONES
                vista3.btnAgregarClientes.setEnabled(false);
                vista3.btnEliminarClientes.setEnabled(false);
                vista3.btnActualizarClientes.setEnabled(true);
                vista3.btnModificarClientes.setEnabled(false);

                //EN CASO DE QUE SI, SE PASAN LOS DATOS DE LA TABLA A VARIABLES
                int id = Integer.parseInt((String) vista3.tablaClientes.getValueAt(filaCliente, 0).toString());
                String razonSocial = (String) vista3.tablaClientes.getValueAt(filaCliente, 1);
                int persona = Integer.parseInt((String) vista3.tablaClientes.getValueAt(filaCliente, 2).toString());

                //SE SETAN LOS DATOS DE LAS VARABLES A LOS CAMPOS DE LA VISTA
                vista3.txtIdPersona.setText("" + id);
                vista3.txtRazonSocial.setText(razonSocial);
                vista3.cboPersona.setSelectedItem(persona);
            }
        }

        //VISTA CLIENTE
        if (e.getSource() == vista3.btnActualizarClientes) {
            //SE ACTUALIZA EL CLIENTE
            actualizarCliente();
            //SE BUSCAN LOS NUEVOS CLIENTES
            buscarCliente(vista3.tablaClientes);

            //SE PREPARA LA VISTA PARA UN NUEVO CLIENTE
            nuevoCliente();
            //SE HABILITAN LOS BOTONES
            vista3.btnAgregarClientes.setEnabled(true);
            vista3.btnEliminarClientes.setEnabled(true);
            vista3.btnActualizarClientes.setEnabled(false);
            vista3.btnModificarClientes.setEnabled(true);
        }
    }

    // METODOS DE LA VISTA DE CLIENTE
    public void buscarCliente(JTable tabla) {
        // centrarCeldas(tabla);
        //EN EL MODELO CREADO SE GUARDA EL MODELO DE LA TABLA CLIENTES
        modelo = (DefaultTableModel) tabla.getModel();
        tabla.setModel(modelo);
        //SE CREA UN ARRAY CON LOS CLIENTES QUE SE TRAEN DESDE LA BD
        ArrayList<Clientes> listaClientes = daoCliente.listar();
        //SE CREA UN ARRAY OBJETO QUE ALMACENA LOS DATOS DE CADA CLIENTE
        Object[] objeto = new Object[3];
        for (int i = 0; i < listaClientes.size(); i++) {
            objeto[0] = listaClientes.get(i).getIdCliente();
            objeto[1] = listaClientes.get(i).getRazonSocial();
            objeto[2] = listaClientes.get(i).getPersona();

            //SE AGREGAN LOS CLIENTES EN LAS FILAS DEL MODELO QUE SE LE SETEA A LA TABLA
            modelo.addRow(objeto);
        }
        //SE DEFINE LA CANTIDAD Y EL TAMAÑO DE LAS FILAS DE LA TABLA
        tabla.setRowHeight(35);
        tabla.setRowMargin(10);
    }

    //METODO PARA AGREGAR UN NUEVO CLIENTE
    public void agregarCliente() {
        try {
            //SE PASAN LOS VALORES DE LOS CAMPOS DE LA VISTA CLIENTE A VARIABLES

            String razonSocial = vista3.txtRazonSocial.getText();
            Integer persona = (vista3.cboPersona.getItemAt(vista3.cboPersona.getSelectedIndex()).getIdPersona());
            //SE SETEAN ESOS DATOS EN LA INSTANCIA DE CLIENTE CREADA

            cliente.setRazonSocial(razonSocial);
            cliente.setPersona(persona);

            //SE TRAE EL RESULTADO DE LA AGREGACION A LA BD
            int r = daoCliente.agregar(cliente);

            if (r == 1) {
                JOptionPane.showMessageDialog(vista3, "Cliente agregado con exito");
            } else {
                JOptionPane.showMessageDialog(vista3, "Error al agregar un cliente");
            }
            //POR ULTIMO SE LIMPIA LA TABLA DE CLIENTES
            limpiarTablaCliente();
        } catch (HeadlessException e) {
        }
    }

    //METODO PARA ELIMINAR UN REGISTRO CLIENTE   
    public void eliminarCliente() {
        //SE VALIDA SI FUE SELECCIONADO UN CLIENTE
        int fila = vista3.tablaClientes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista3, "Debe seleccionar un cliente");
        } else {
            //EN CASO DE QUE SE HAYA SELECCIONADO UN CLIENTE, SE LE CONSULTA SI REALMENTE DESEA ELIMINARLO
            int variable = JOptionPane.showOptionDialog(null, "¿Deseas eliminar un cliente?", "Eliminacion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]);
            if (variable == 0) {
                //SE LE PASA EL ID DEL CLIENTE SELECCIONADO PARA QUE SE UTILICE EN LA CONSULTA A LA BD
                int id = Integer.parseInt((String) vista3.tablaClientes.getValueAt(fila, 0).toString());
                daoCliente.eliminar(id);
                //SE LE INFORMA AL USUARIO QUE EL CLIENTE FUE ELIMINADO
                JOptionPane.showMessageDialog(vista3, "Cliente eliminado con exito");
            }
        }
        //POR ULTIMO SE LIMPIA LA TABLA CLIENTE
        limpiarTablaCliente();
    }

    //METODO PARA ACTUALIZAR UN CLIENTE
    public void actualizarCliente() {
        //VALIDACION DE CAMPOS VACIOS NECESARIOS PARA ACTUALIZAR EL REGISTRO EN LA BD.  
        if (vista3.txtIdPersona.getText().equals("")) {
            JOptionPane.showMessageDialog(vista3, "Primero debe modificar un cliente");
        } else {
            //EN CASO DE QUE TODOS LOS CAMPOS ESTAN COMPLETOS, SE LE CONSULTA AL CLIENTE SI ESTA SEGURO DE MODIFICAR EL PROYECTO
            int variable = JOptionPane.showOptionDialog(null, "¿Deseas modificar un cliente?", "Cliente", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]);
            //SI LA RESPUESTA ES QUE SI DESEA MODIFICARLO
            if (variable == 0) {

                //EN CASO QUE SE HAYA SELECCIONADO UN CLIENTE, SE GUARDA CADA DATO DE LOS CAMPOS DE LA VISTA EN VARIABLES
                String idAux = vista3.txtIdPersona.getText();
                int id = Integer.valueOf(idAux);
                String razonSocial = vista3.txtRazonSocial.getText();
                Integer persona = (vista3.cboPersona.getItemAt(vista3.cboPersona.getSelectedIndex()).getIdPersona());

                //SE SETEAN ESOS DATOS EN LA INSTANCIA DE CLIENTE CREADA
                cliente.setIdCliente(id);
                cliente.setRazonSocial(razonSocial);
                cliente.setPersona(persona);

                //SE GUARDA EL RESULTADO DE LA ACTUALIZACION
                int r = daoCliente.actualizar(cliente);
                if (r == 1) {
                    JOptionPane.showMessageDialog(vista3, "Cliente modificado con exito");
                } else {
                    JOptionPane.showMessageDialog(vista3, "Error al actualizar un cliente");
                }
            }
        }
        //POR ULTIMO SE VACIA LA TABLA DE CLIENTES
        limpiarTablaCliente();
    }

    void limpiarTablaCliente() {
        //SE RECORRE TODA LA TABLA Y CADA REGISTRO LEIDO SE REMUEVE DE LA TABLA
        for (int i = 0; i < vista3.tablaClientes.getRowCount(); i++) {
            modelo.removeRow(i);
            i = i - 1;
        }
    }

    public void nuevoCliente() {
        //SE VACIAN TODOS LOS CAMPOS QUE PERMITEN AGREGAR UN NUEVO CLIENTE
        vista3.txtIdPersona.setText("");
        vista3.txtRazonSocial.setText("");

        vista3.txtRazonSocial.requestFocus();
    }

    //METODO QUE CARGA EL COMBO PERSONA EN LA VISTA CLIENTE
    public void llenarPersonaCliente() throws SQLException {
        //SE CREA UNA INSTANCIA DE LA CLASE PersonaDAO
        PersonaDAO perDAO = new PersonaDAO();
        //SE CREA UN ARRAY QUE ALMACENA LAS PERSONAS TRAIDAS DESDE LA BD
        ArrayList<Persona> listaPersona = perDAO.getPersona();
        //SE VACIA EL COMBO PERSONA
        vista3.cboPersona.removeAllItems();
        //SE RECORRE LA LISTA TRAIDA DESDE LA BD Y SE AGREGA CADA REGISTRO AL COMBO
        for (int i = 0; i < listaPersona.size(); i++) {
            vista3.cboPersona.addItem(new Persona(listaPersona.get(i).getIdPersona(), listaPersona.get(i).getNombre(), listaPersona.get(i).getApellido(), listaPersona.get(i).getFechaNacimiento(), listaPersona.get(i).getTelefono()));
        }
    }
}
