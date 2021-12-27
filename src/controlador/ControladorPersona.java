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
    String[] botones = {"Aceptar", "Cancelar"};

    //Constructor sin parametros 
    public ControladorPersona() {

    }

    //CREAMOS INSTANCIAS DE OTRAS CLASES
    PersonaDAO daoPersona = new PersonaDAO();
    VistaPersona vista = new VistaPersona();
    DefaultTableModel modelo = new DefaultTableModel();
    Persona per = new Persona();

    //CREAMOS UN CONTROLADOR POR CADA VISTA EXISTENTE
    public ControladorPersona(VistaPersona v) {
        this.vista = v;

        this.vista.btnAgregar.addActionListener(this);
        this.vista.btnActualizar.addActionListener(this);
        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);
        this.vista.btnModificar.addActionListener(this);
        this.vista.btnActualizar.setVisible(false);
        this.vista.btnEliminar.setEnabled(false);
        this.vista.btnModificar.setEnabled(false);
        
    }

    //EJECUCION DE CADA BOTON DENTRO DE CADA PANTALLA
    public void actionPerformed(ActionEvent e) {
        //VISTA Cliente
        if (e.getSource() == vista.btnBuscar) {

            //SE LIMPIA LA TABLA QUE CONTIENE LAS PERSONAS
            limpiarTablaPersona();
            //SE BUSCAN TODOS LAS PERSONAS DE LA BD
            buscarPersona(vista.tablaPersona);
            this.vista.btnEliminar.setEnabled(true);
            this.vista.btnModificar.setEnabled(true);
        }

        if (e.getSource() == vista.btnEliminar) {
            //SE ELIMINA UNA PERSONA
            eliminarPersona();
            //SE BUSCAN LAS NUEVOS PERSONAS
            buscarPersona(vista.tablaPersona);
        }

        if (e.getSource() == vista.btnAgregar) {
            //SE VALIDA QUE LOS CAMPOS NO ESTEN VACIOS
           
            boolean isNumeric = vista.txtTelefono.getText().matches("[+-]?\\d*(\\.\\d+)?");
            Period edad = Period.ofYears(0);
            if(vista.fechaNacimiento.getDate() != null){
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                LocalDate ahora = LocalDate.now();
                java.util.Date date = vista.fechaNacimiento.getDate();
                LocalDate fechaNac = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                edad = Period.between(fechaNac, ahora);
            }
            if (vista.txtNombre.getText().equals("") || vista.txtApellido.getText().equals("") || vista.txtTelefono.getText().equals("") || vista.fechaNacimiento.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Debe completar todos los campos");
            }else if(this.esSoloLetras(vista.txtApellido.getText()) == false || this.esSoloLetras(vista.txtNombre.getText()) == false || this.esSoloLetras(vista.txtTelefono.getText()) == true){
                JOptionPane.showMessageDialog(null, "Verifique los tipo de datos ingresados en los campos");
            }else if (vista.txtTelefono.getText().length() < 10 || vista.txtTelefono.getText().length() > 10 || isNumeric == false) {
                JOptionPane.showMessageDialog(null, "Numero de telefono invalido");
            }else if (edad.getYears() < 18) {
                JOptionPane.showMessageDialog(null, "La persona que desea registar es menor de 18");
            }else {
                //SE CONSULTA AL USUARIO SI REALMENTE QUIERE AGREGAR UNA PERSONA
                int variable = JOptionPane.showOptionDialog(null, "¿Deseas agregar una persona?", "Agregacion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]);

                if (variable == 0) {
                    //SE AGREGA UNA PERSONA
                    agregarCliente();
                    //SE BUSCAN LAS NUEVAS PERSONAS
                    buscarPersona(vista.tablaPersona);
                    //SE PREPARA LA VISTA PARA UNA NUEVA PERSONA
                    nuevoPersona();
                } else {
                    //EN CASO DE QUE QUIERA CANCELAR LA AGREGACION SE VACIAN LOS CAMPOS 
                    nuevoPersona();
                }
            }
        }

        if (e.getSource() == vista.btnModificar) {
            //SE VALIDA QUE SE HAYA SELECCIONADO UNA PERSONA
            int filaPersona = vista.tablaPersona.getSelectedRow();
            if (filaPersona == -1) {
                JOptionPane.showMessageDialog(vista, "Debe seleccionar una persona");
            } else {
                //SE DESHABILITAN LOS BOTONES
                vista.btnAgregar.setEnabled(false);
                vista.btnEliminar.setEnabled(false);
                vista.btnActualizar.setEnabled(true);
                vista.btnModificar.setEnabled(false);
                this.vista.btnActualizar.setVisible(true);

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

        if (e.getSource() == vista.btnActualizar) {
            //SE ACTUALIZA LA PERSONA
            actualizarPersona();
            //SE BUSCAN LAS NUEVAS PERSONAS
            buscarPersona(vista.tablaPersona);

            //SE PREPARA LA VISTA PARA UNA NUEVA PERSONA
            nuevoPersona();
            //SE HABILITAN LOS BOTONES
            vista.btnAgregar.setEnabled(true);
            vista.btnEliminar.setEnabled(true);
            vista.btnActualizar.setEnabled(false);
            vista.btnModificar.setEnabled(true);
        }
    }

    public void buscarPersona(JTable tabla) {
        // centrarCeldas(tabla);
        //EN EL MODELO CREADO SE GUARDA EL MODELO DE LA TABLA PERSONAS
        modelo = (DefaultTableModel) tabla.getModel();
        tabla.setModel(modelo);
        //SE CREA UN ARRAY CON LAS PERSONAS QUE SE TRAEN DESDE LA BD
        ArrayList<Persona> listaPersonas = daoPersona.listar();

        //SE CREA UN ARRAY OBJETO QUE ALMACENA LOS DATOS DE CADA PERSONA
        Object[] objeto = new Object[5];
        for (int i = 0; i < listaPersonas.size(); i++) {

            objeto[0] = listaPersonas.get(i).getIdPersona();
            objeto[1] = listaPersonas.get(i).getNombre();
            objeto[2] = listaPersonas.get(i).getApellido();
            objeto[3] = listaPersonas.get(i).getFechaNacimiento();
            objeto[4] = listaPersonas.get(i).getTelefono();

            //SE AGREGAN LAS PERSONAS EN LAS FILAS DEL MODELO QUE SE LE SETEA A LA TABLA
            modelo.addRow(objeto);
        }
        //SE DEFINE LA CANTIDAD Y EL TAMAÑO DE LAS FILAS DE LA TABLA
        tabla.setRowHeight(35);
        tabla.setRowMargin(10);
    }

    public void agregarCliente() {
        if (vista.txtNombre.getText().equals("") || vista.txtApellido.getText().equals("") || vista.fechaNacimiento.getDate() == null || vista.txtTelefono.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Debe completar todos los campos");
        } else {
            try {
                //SE PASAN LOS VALORES DE LOS CAMPOS DE LA VISTA ERSONA A VARIABLES

                String nombre = vista.txtNombre.getText();
                String apellido = vista.txtApellido.getText();

                java.util.Date date = vista.fechaNacimiento.getDate();
                long aux1 = date.getTime();
                java.sql.Date fechaNacimiento = new java.sql.Date(aux1);
                String telefono = vista.txtTelefono.getText();
                //SE SETEAN ESOS DATOS EN LA INSTANCIA DE PERSONA CREADA

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
                //POR ULTIMO SE LIMPIA LA TABLA DE LA PERSONA
                limpiarTablaPersona();
            } catch (HeadlessException e) {
            }
        }
    }

    public void eliminarPersona() {
        //SE VALIDA SI FUE SELECCIONADO UNA PERSONA
        int fila = vista.tablaPersona.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Debe seleccionar una persona");
        } else {
            //EN CASO DE QUE SE HAYA SELECCIONADO UNA PERSONA, SE LE CONSULTA SI REALMENTE DESEA ELIMINARLO
            int variable = JOptionPane.showOptionDialog(null, "¿Deseas eliminar una persona?", "Eliminacion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]);
            if (variable == 0) {
                //SE GUARDA EL ID DEL PROYECTO SELECCIONADO PARA PASARLO COMO PARAMETRO Y SER USADO EN LA CONSULTA A LA BD
                int id = Integer.parseInt((String) vista.tablaPersona.getValueAt(fila, 0).toString());
                if(daoPersona.consultaEliminacionCliente(id) == 0 && daoPersona.consultaEliminacionPersonal(id) == 0 && daoPersona.consultaEliminacionNoCliente(id) == 0){
                    daoPersona.eliminar(id);
                    JOptionPane.showMessageDialog(vista, "Persona eliminada con exito");
                }else if(daoPersona.consultaEliminacionCliente(id) > 0){
                    JOptionPane.showMessageDialog(vista, "La persona no se puede eliminar debido a que se la ha asignado a uno o más clientes");
                    JOptionPane.showMessageDialog(vista, "Desasigne la persona del o los clientes para poder eliminarlo");
                }else if(daoPersona.consultaEliminacionPersonal(id) > 0){
                    JOptionPane.showMessageDialog(vista, "La persona no se puede eliminar debido a que se la ha asignado a uno o más personales");
                    JOptionPane.showMessageDialog(vista, "Desasigne la persona del o los personales para poder eliminarlo");
                }else if(daoPersona.consultaEliminacionNoCliente(id) > 0){
                    JOptionPane.showMessageDialog(vista, "La persona no se puede eliminar debido a que se la ha asignado a uno o más no clientes");
                    JOptionPane.showMessageDialog(vista, "Desasigne la persona del o los no clientes para poder eliminarlo");
                }
            }
        }
        //POR ULTIMO SE LIMPIA LA TABLA PERSONAS
        limpiarTablaPersona();
    }

    public void actualizarPersona() {
        //VALIDACION DE CAMPOS VACIOS NECESARIOS PARA ACTUALIZAR EL REGISTRO EN LA BD.  
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate ahora = LocalDate.now();

        java.util.Date date1 = vista.fechaNacimiento.getDate();
        LocalDate fechaNac = Instant.ofEpochMilli(date1.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        //  System.out.println(fechaNac);
        Period edad = Period.between(fechaNac, ahora);
        if (vista.txtId.getText().equals("")) {
            JOptionPane.showMessageDialog(vista, "Primero debe modificar una persona");
        }
        if (edad.getYears() < 18) {
            JOptionPane.showMessageDialog(null, "La persona que desea registar es menor de 18");
        } else if (vista.txtNombre.getText() == "" || vista.txtApellido.getText() == "" || vista.txtTelefono.getText() == "") {
            JOptionPane.showMessageDialog(null, "Debe rellenar los campos vacios");
        } else if (this.esSoloLetras(vista.txtNombre.getText()) == false || this.esSoloLetras(vista.txtApellido.getText()) == false || this.esSoloLetras(vista.txtTelefono.getText()) == true) {
            JOptionPane.showMessageDialog(null, "Debe verificar el tipo de dato de los campos que ha llenado");
        } else {
            //EN CASO DE QUE TODOS LOS CAMPOS ESTAN COMPLETOS, SE LE CONSULTA AL CLIENTE SI ESTA SEGURO DE MODIFICAR LA PERSONA
            int variable = JOptionPane.showOptionDialog(null, "¿Deseas modificar una persona?", "Persona", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]);
            //SI LA RESPUESTA ES QUE SI DESEA MODIFICARLO
            if (variable == 0) {

                //EN CASO QUE SE HAYA SELECCIONADO UNA PERSONA, SE GUARDA CADA DATO DE LOS CAMPOS DE LA VISTA EN VARIABLES
                String idAux = vista.txtId.getText();
                int id = Integer.valueOf(idAux);
                String nombre = vista.txtNombre.getText();
                String apellido = vista.txtApellido.getText();
                java.util.Date date = vista.fechaNacimiento.getDate();
                long aux1 = date.getTime();
                java.sql.Date fechaNacimiento = new java.sql.Date(aux1);
                String telefono = vista.txtTelefono.getText();

                //SE SETEAN ESOS DATOS EN LA INSTANCIA DE PERSONA CREADA
                per.setIdPersona(id);
                per.setNombre(nombre);
                per.setApellido(apellido);
                per.setFechaNacimiento(fechaNacimiento);
                per.setTelefono(telefono);

                //SE GUARDA EL RESULTADO DE LA ACTUALIZACION
                int r = daoPersona.actualizar(per);
                if (r == 1) {
                    JOptionPane.showMessageDialog(vista, "Persona modificada con exito");
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al actualizar una persona");
                }
            }
        }
        //POR ULTIMO SE VACIA LA TABLA DE PERSONAS
        limpiarTablaPersona();
    }

    void limpiarTablaPersona() {
        //SE RECORRE TODA LA TABLA Y CADA REGISTRO LEIDO SE REMUEVE DE LA TABLA
        for (int i = 0; i < vista.tablaPersona.getRowCount(); i++) {
            modelo.removeRow(i);
            i = i - 1;
        }
    }

    public boolean esSoloLetras(String cadena) {
        //Recorremos cada caracter de la cadena y comprobamos si son letras.
        //Para comprobarlo, lo pasamos a mayuscula y consultamos su numero ASCII.
        //Si está fuera del rango 65 - 90, es que NO son letras.
        //Para ser más exactos al tratarse del idioma español, tambien comprobamos
        //el valor 165 equivalente a la Ñ

        for (int i = 0; i < cadena.length(); i++) {
            char caracter = cadena.toUpperCase().charAt(i);
            int valorASCII = (int) caracter;
            if (valorASCII != 165 && (valorASCII < 65 || valorASCII > 90)) {
                return false; //Se ha encontrado un caracter que no es letra
            }
        }

        //Terminado el bucle sin que se hay retornado false, es que todos los caracteres son letras
        return true;
    }

    public void nuevoPersona() {
        //SE VACIAN TODOS LOS CAMPOS QUE PERMITEN AGREGAR UNA NUEVA PERSONA
        vista.txtId.setText("");
        vista.txtNombre.setText("");
        vista.txtApellido.setText("");
        vista.txtTelefono.setText("");
        vista.fechaNacimiento.setDate(null);

        vista.txtNombre.requestFocus();
    }
}
