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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.Personal;
import modelo.PersonalDAO;
import modelo.Persona;
import modelo.PersonaDAO;
import vista.Perfil_Personal;
import vista.VistaPersonal;

public class ControladorPersonal implements ActionListener {

    //VECTOR QUE CONTIENE LOS BOTONES PARA LAS CONSULTAS AL USUARIO
    String[] botones = {"Aceptar", "Cancelar"};

    //Constructor sin parametros 
    public ControladorPersonal() {
    }

    //CREAMOS INSTANCIAS DE OTRAS CLASES
    PersonalDAO daoPersonal = new PersonalDAO();
    VistaPersonal vista3 = new VistaPersonal();
    DefaultTableModel modelo = new DefaultTableModel();
    Personal personal = new Personal();
    Perfil_Personal vistaPerfil = new Perfil_Personal();

    //CREAMOS UN CONTROLADOR POR CADA VISTA EXISTENTE
    public ControladorPersonal(VistaPersonal v) {
        this.vista3 = v;
        this.vista3.btnAgregarPersonal.addActionListener(this);
        this.vista3.btnActualizarPersonal.addActionListener(this);
        this.vista3.btnBuscarPersonal.addActionListener(this);
        this.vista3.btnEliminarPersonal.addActionListener(this);
        this.vista3.btnModificarPersonal.addActionListener(this);
        this.vista3.btnAgregarPerfil.addActionListener(this);
        this.vista3.btnEliminarPersonal.setEnabled(false);
        this.vista3.btnModificarPersonal.setEnabled(false);
    }

    //EJECUCION DE CADA BOTON DENTRO DE CADA PANTALLA
    public void actionPerformed(ActionEvent e) {
        //VISTA Personal
        if (e.getSource() == vista3.btnBuscarPersonal) {
            //SE LIMPIA LA TABLA QUE CONTIENE LOS PERSONALES
            limpiarTablaPersonal();
            //SE BUSCAN TODOS LOS PERSONALES DE LA BD
            buscarPersonal(vista3.tablaPersonal);
            this.vista3.btnEliminarPersonal.setEnabled(true);
            this.vista3.btnModificarPersonal.setEnabled(true);
        }

        if (e.getSource() == vista3.btnEliminarPersonal) {
            //SE ELIMINA UN PERSONAL
            eliminarPersonal();
            //SE BUSCAN LOS NUEVOS PERSONALES
            buscarPersonal(vista3.tablaPersonal);
        }

        if (e.getSource() == vista3.btnAgregarPersonal) {
            //SE VALIDA QUE LOS CAMPOS NO ESTEN VACIOS
            if (vista3.txtCUIT.getText().equals("") || vista3.cboPersona.getSelectedItem().equals("")) {
                JOptionPane.showMessageDialog(null, "Debe completar todos los campos");
            } else {
                //SE CONSULTA AL USUARIO SI REALMENTE QUIERE AGREGAR UN PERSONAL
                int variable = JOptionPane.showOptionDialog(null, "¿Deseas agregar un personal?", "Agregacion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]);

                if (variable == 0) {
                    //SE AGREGA UN PERSONAL
                    agregarPersonal();
                    //SE BUSCAN LOS NUEVOS PERSONALES
                    buscarPersonal(vista3.tablaPersonal);
                    //SE PREPARA LA VISTA PARA UN NUEVO PERSONAL
                    nuevoPersonal();
                } else {
                    //EN CASO DE QUE QUIERA CANCELAR LA AGREGACION SE VACIAN LOS CAMPOS 
                    nuevoPersonal();
                }
            }
        }

        if (e.getSource() == vista3.btnModificarPersonal) {
            //SE VALIDA QUE SE HAYA SELECCIONADO UN PERSONAL
            int filaPersonal = vista3.tablaPersonal.getSelectedRow();
            if (filaPersonal == -1) {
                JOptionPane.showMessageDialog(vista3, "Debe seleccionar un personal");
            } else {
                //SE DESHABILITAN LOS BOTONES
                vista3.btnAgregarPersonal.setEnabled(false);
                vista3.btnEliminarPersonal.setEnabled(false);
                vista3.btnActualizarPersonal.setEnabled(true);
                vista3.btnModificarPersonal.setEnabled(false);
                vista3.btnAgregarPerfil.setEnabled(true);

                //EN CASO DE QUE SI, SE PASAN LOS DATOS DE LA TABLA A VARIABLES
                int id = Integer.parseInt((String) vista3.tablaPersonal.getValueAt(filaPersonal, 0).toString());
                long CUIT = Long.parseLong((String) vista3.tablaPersonal.getValueAt(filaPersonal, 1).toString());
                int persona = Integer.parseInt((String) vista3.tablaPersonal.getValueAt(filaPersonal, 2).toString());

                //SE SETAN LOS DATOS DE LAS VARABLES A LOS CAMPOS DE LA VISTA
                vista3.txtIdPersona.setText("" + id);
                vista3.txtCUIT.setText("" + CUIT);
                vista3.cboPersona.setSelectedItem(persona);
            }
        }

        if (e.getSource() == vista3.btnActualizarPersonal) {
            //SE ACTUALIZA EL PERSONAL
            actualizarPersonal();
            //SE BUSCAN LOS NUEVOS PERSONALES
            buscarPersonal(vista3.tablaPersonal);

            //SE PREPARA LA VISTA PARA UN NUEVO PERSONAL
            nuevoPersonal();
            //SE HABILITAN LOS BOTONES
            vista3.btnAgregarPersonal.setEnabled(true);
            vista3.btnEliminarPersonal.setEnabled(true);
            vista3.btnActualizarPersonal.setEnabled(false);
            vista3.btnModificarPersonal.setEnabled(true);
            vista3.btnAgregarPerfil.setEnabled(false);
        }
        if (e.getSource() == vista3.btnAgregarPerfil) {
            try {
                vistaPerfil.inicializar(vista3.txtIdPersona.getText(), vistaPerfil);
            } catch (SQLException ex) {
                Logger.getLogger(ControladorPersonal.class.getName()).log(Level.SEVERE, null, ex);
            }
            //SE PREPARA LA VISTA PARA UN NUEVO PERSONAL
            vista3.txtIdPersona.setText("");
            vista3.txtCUIT.setText("");
            //SE HABILITAN LOS BOTONES
            vista3.btnAgregarPersonal.setEnabled(true);
            vista3.btnEliminarPersonal.setEnabled(true);
            vista3.btnActualizarPersonal.setEnabled(false);
            vista3.btnModificarPersonal.setEnabled(true);
            vista3.btnAgregarPerfil.setEnabled(false);
        }
    }

    public void buscarPersonal(JTable tabla) {
        // centrarCeldas(tabla);
        //EN EL MODELO CREADO SE GUARDA EL MODELO DE LA TABLA PERSONAL
        modelo = (DefaultTableModel) tabla.getModel();
        tabla.setModel(modelo);
        //SE CREA UN ARRAY CON LOS PERSONALESS QUE SE TRAEN DESDE LA BD
        ArrayList<Personal> listaPersonals = daoPersonal.listar();
        //SE CREA UN ARRAY OBJETO QUE ALMACENA LOS DATOS DE CADA PERSONAL
        Object[] objeto = new Object[3];
        for (int i = 0; i < listaPersonals.size(); i++) {
            objeto[0] = listaPersonals.get(i).getIdPersonal();
            objeto[1] = listaPersonals.get(i).getCUIT();
            objeto[2] = listaPersonals.get(i).getPersona();

            //SE AGREGAN LOS PERSONALES EN LAS FILAS DEL MODELO QUE SE LE SETEA A LA TABLA
            modelo.addRow(objeto);
        }
        //SE DEFINE LA CANTIDAD Y EL TAMAÑO DE LAS FILAS DE LA TABLA
        tabla.setRowHeight(35);
        tabla.setRowMargin(10);
    }

    //METODO PARA AGREGAR UN NUEVO PERSONAL
    public void agregarPersonal() {
        try {
            if (this.esNumerico(vista3.txtCUIT.getText()) == true) {
                //SE PASAN LOS VALORES DE LOS CAMPOS DE LA VISTA personal A VARIABLE
                //Long CUIT = Long.parseLong(vista3.txtCUIT.getText());
                //SE SETEAN ESOS DATOS EN LA INSTANCIA DE PERSONAL CREADA
                Integer persona = (vista3.cboPersona.getItemAt(vista3.cboPersona.getSelectedIndex()).getIdPersona());
                //String tamaño = Long.toString(CUIT);
                String Cuit = vista3.txtCUIT.getText();
                System.out.println(Cuit.length());
                if (daoPersonal.consultaAgregacion(persona) > 0) {
                    JOptionPane.showMessageDialog(vista3, "Esta persona ya es un personal");
                } else if (Cuit.length() == 11 && daoPersonal.consultaAgregacionCUIT(Long.parseLong(Cuit)) == 0) {
                    Long CUIT = Long.parseLong(Cuit);
                    personal.setCUIT(CUIT);
                    personal.setPersona(persona);
                    int r = daoPersonal.agregar(personal);

                    if (r == 1) {
                        JOptionPane.showMessageDialog(vista3, "Personal agregado con exito");
                    } else {
                        JOptionPane.showMessageDialog(vista3, "Error al agregar un personal");
                    }
                    //POR ULTIMO SE LIMPIA LA TABLA DE PERSONAL
                    limpiarTablaPersonal();
                } else if (Cuit.length() != 11) {
                    JOptionPane.showMessageDialog(vista3, "CUIT incorrecto");
                } else if (daoPersonal.consultaAgregacionCUIT(Long.parseLong(Cuit)) > 0) {
                    JOptionPane.showMessageDialog(vista3, "CUIT ya asignado");
                }
            } else {
                JOptionPane.showMessageDialog(vista3, "CUIT incorrecto");
            }
            //SE TRAE EL RESULTADO DE LA AGREGACION A LA BD

        } catch (HeadlessException e) {
        }
    }

    //METODO PARA ELIMINAR UN REGISTRO PERSONAL    
    public void eliminarPersonal() {
        //SE VALIDA SI FUE SELECCIONADO UN PERSONAL
        int fila = vista3.tablaPersonal.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista3, "Debe seleccionar un personal");
        } else {
            //EN CASO DE QUE SE HAYA SELECCIONADO UN PERSONAL, SE LE CONSULTA SI REALMENTE DESEA ELIMINARLO
            int variable = JOptionPane.showOptionDialog(null, "¿Deseas eliminar un personal?", "Eliminacion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]);
            if (variable == 0) {
                //SE GUARDA EL ID DEL PROYECTO SELECCIONADO PARA PASARLO COMO PARAMETRO Y SER USADO EN LA CONSULTA A LA BD
                int id = Integer.parseInt((String) vista3.tablaPersonal.getValueAt(fila, 0).toString());
                if (daoPersonal.consultaEliminacionPersonalPerfil(id) == 0 && daoPersonal.consultaEliminacionPersonalProyecto(id) == 0) {
                    daoPersonal.eliminar(id);
                    JOptionPane.showMessageDialog(vista3, "Personal eliminado con exito");
                } else if (daoPersonal.consultaEliminacionPersonalPerfil(id) > 0) {
                    JOptionPane.showMessageDialog(vista3, "El personal no se puede eliminar debido a que se la ha asignado a uno o más perfiles");
                    JOptionPane.showMessageDialog(vista3, "Desasigne el personal del o los perfiles para poder eliminarlo");
                } else if (daoPersonal.consultaEliminacionPersonalProyecto(id) > 0) {
                    JOptionPane.showMessageDialog(vista3, "El personal no se puede eliminar debido a que se la ha asignado a uno o más proyectos");
                    JOptionPane.showMessageDialog(vista3, "Desasigne el personal del o los proyectos para poder eliminarlo");
                }
            }
        }
        //POR ULTIMO SE LIMPIA LA TABLA PERSONAL
        limpiarTablaPersonal();
    }

    //METODO PARA ACTUALIZAR UN PERSONAL
    public void actualizarPersonal() {
        //VALIDACION DE CAMPOS VACIOS NECESARIOS PARA ACTUALIZAR EL REGISTRO EN LA BD.  
        if (vista3.txtIdPersona.getText().equals("")) {
            JOptionPane.showMessageDialog(vista3, "Primero debe modificar un personal");
        } else {
            //EN CASO DE QUE TODOS LOS CAMPOS ESTAN COMPLETOS, SE LE CONSULTA AL CLIENTE SI ESTA SEGURO DE MODIFICAR EL PERSONAL
            int variable = JOptionPane.showOptionDialog(null, "¿Deseas modificar un personal?", "Personal", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]);
            //SI LA RESPUESTA ES QUE SI DESEA MODIFICARLO
            if (variable == 0) {
                if (this.esNumerico(vista3.txtCUIT.getText())) {
                    //EN CASO QUE SE HAYA SELECCIONADO UN PERSONAL, SE GUARDA CADA DATO DE LOS CAMPOS DE LA VISTA EN VARIABLES
                    String idAux = vista3.txtIdPersona.getText();
                    int id = Integer.valueOf(idAux);
                    //long CUIT = Long.parseLong(vista3.txtCUIT.getText());
                    String Cuit = vista3.txtCUIT.getText();
                    Integer persona = (vista3.cboPersona.getItemAt(vista3.cboPersona.getSelectedIndex()).getIdPersona());
                    if (daoPersonal.consultaAgregacion(persona) > 0) {
                        JOptionPane.showMessageDialog(vista3, "Esta persona ya es un personal");
                    } else if (Cuit.length() == 11 && daoPersonal.consultaAgregacionCUIT(Long.parseLong(Cuit)) == 0) {
                        Long CUIT = Long.parseLong(Cuit);
                        //SE SETEAN ESOS DATOS EN LA INSTANCIA DE PERSONAL CREADA
                        personal.setIdPersonal(id);
                        personal.setCUIT(CUIT);
                        personal.setPersona(persona);

                        //SE GUARDA EL RESULTADO DE LA ACTUALIZACION
                        int r = daoPersonal.actualizar(personal);
                        if (r == 1) {
                            JOptionPane.showMessageDialog(vista3, "Personal modificado con exito");
                        } else {
                            JOptionPane.showMessageDialog(vista3, "Error al actualizar un personal");
                        }
                    } else if (Cuit.length() != 11) {
                        JOptionPane.showMessageDialog(vista3, "CUIT incorrecto");
                    } else if (daoPersonal.consultaAgregacionCUIT(Long.parseLong(Cuit)) > 0) {
                        JOptionPane.showMessageDialog(vista3, "CUIT ya asignado");
                    }
                } else {
                    JOptionPane.showMessageDialog(vista3, "Cuit Incorrecto");
                }
            }
        }
        //POR ULTIMO SE VACIA LA TABLA DE PERSONAL
        limpiarTablaPersonal();
    }

    void limpiarTablaPersonal() {
        //SE RECORRE TODA LA TABLA Y CADA REGISTRO LEIDO SE REMUEVE DE LA TABLA
        for (int i = 0; i < vista3.tablaPersonal.getRowCount(); i++) {
            modelo.removeRow(i);
            i = i - 1;
        }
    }

    public void nuevoPersonal() {
        //SE VACIAN TODOS LOS CAMPOS QUE PERMITEN AGREGAR UN NUEVO PERSONAL
        vista3.txtIdPersona.setText("");
        vista3.txtCUIT.setText("");

        vista3.txtCUIT.requestFocus();
    }

    public void llenarPersonaPersonal() throws SQLException {
        //SE CREA UNA INSTANCIA DE LA CLASE PersonaDAO
        PersonaDAO perDAO = new PersonaDAO();
        //SE CREA UN ARRAY QUE ALMACENA LAS PERSONAS TRAIDAS DESDE LA BD
        ArrayList<Persona> listaPersona = perDAO.getPersona();
        //SE VACIA EL COMBO PERSONA
        vista3.cboPersona.removeAllItems();
        //SE RECORRE LA LISTA TRAIDA DESDE LA BD Y SE AGREGA CADA REGISTRO AL COMBO
        for (int i = 0; i < listaPersona.size(); i++) {
            vista3.cboPersona.addItem(new Persona(listaPersona.get(i).getIdPersona(), listaPersona.get(i).getNombre(), listaPersona.get(i).getApellido(), listaPersona.get(i).getFechaNacimiento(), listaPersona.get(i).getTelefono()));
            // System.out.println(vista3.cboPersona.getItemAt(i));
        }

    }

    public boolean esNumerico(String num) {
        if (num.matches("[+-]?\\d*(\\.\\d+)?")) {
            return true;
        }

        return false;

    }

}
