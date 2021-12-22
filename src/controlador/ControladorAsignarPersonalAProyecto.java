/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import modelo.Perfil;
import modelo.Persona;
import modelo.PersonaDAO;
import modelo.Personal;
import modelo.PersonalDAO;
import modelo.Proyecto;
import modelo.ProyectoDAO;
import modelo.ProyectoPersonal;
import modelo.ProyectoPersonalDAO;
import vista.VistaPersonalProyecto;

/**
 *
 * @author Santiago
 */
public class ControladorAsignarPersonalAProyecto implements ActionListener {

    //VECTOR QUE CONTIENE LOS BOTONES PARA LAS CONSULTAS AL USUARIO
    String[] botones = {"Aceptar", "Cancelar"};
    ProyectoDAO dao = new ProyectoDAO();
    ProyectoPersonal pp = new ProyectoPersonal();
    VistaPersonalProyecto vista = new VistaPersonalProyecto();
    DefaultTableModel modelo = new DefaultTableModel();
    DefaultTableModel modelo2 = new DefaultTableModel();
    ProyectoPersonalDAO ppDao = new ProyectoPersonalDAO();
    PersonalDAO daope = new PersonalDAO();
    PersonalDAO daoPersonal = new PersonalDAO();
    private ArrayList<Personal> listaPersonal = daoPersonal.listar();
    private int idTablaProy;
    private int bandera = 0;
    private int personal;
    PersonaDAO per = new PersonaDAO();
    //CREAMOS UN CONTROLADOR POR CADA VISTA EXISTENTE EN DONDE SE LES PASA EL ACTION PARA PODER UTILIZARLOS

    public ControladorAsignarPersonalAProyecto(VistaPersonalProyecto v) {
        this.vista = v;
        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnAgg.addActionListener(this);
        this.vista.btnAgg.setEnabled(false);
        this.vista.botonAggPerfil.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);
        this.vista.btnAceptar.addActionListener(this);
        this.vista.btnBuscarAPP.addActionListener(this);
        this.vista.comboOpcion.addActionListener(this);

    }
    //EJECUCION DE CADA BOTON DENTRO DE CADA PANTALLA

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnBuscar) {
            limpiarTabla();
            //SE BUSCAN LOS PROYECTO
            buscar(vista.tablaProyecto);
            vista.btnBuscarAPP.setEnabled(false);
            vista.btnAceptar.setEnabled(true);
            this.vista.btnAgg.setEnabled(true);

        }
        if (e.getSource() == vista.btnAgg) {

            //SE OBTIENE LA FILA SELECCIONADA
            int fila = vista.tablaProyecto.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(vista, "Debe seleccionar un personal");
            } else {
                //SE HABILITAN TODOS LOS BOTENES QUE SE PUEDEN USAR UNA VEZ SELEECIONADO UN PROYECTO
                vista.comboPersonal.setEnabled(true);
                vista.botonAggPerfil.setEnabled(true);
                vista.btnEliminar.setEnabled(true);
                vista.btnAceptar.setEnabled(true);
                vista.comboOpcion.setEnabled(true);
                try {
                    //SE LLENA EL COMBO
                    llenarPersonales();
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorAsignarPerfilAProyecto.class.getName()).log(Level.SEVERE, null, ex);
                }
                // SE GUARDA EL ID DEL PROYECTO
                this.idTablaProy = Integer.parseInt((String) vista.tablaProyecto.getValueAt(fila, 0).toString());
            }
        }
        if (e.getSource() == vista.comboOpcion) {

            try {
                // METODO QUE SE ENCARGA DE SABER QUE OPCION SE SELEECIONO DEL COMBO

                sugeridos();
            } catch (SQLException ex) {
                Logger.getLogger(ControladorAsignarPersonalAProyecto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (e.getSource() == vista.botonAggPerfil) {
            //int perfil = (vista.comboPerfiles.getItemAt(vista.comboPerfiles.getSelectedIndex()).getId());
            // METODO QUE SE USA PARA SABER SI UN PERSONAL YA FUE ASIGNADO
            if (comprobarPerfiles(idTablaProy, this.personal) == true) {
                JOptionPane.showMessageDialog(null, "El personal seleccionado ya fue asiganado a dicho proyecto", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                agregar();
                System.out.println(this.personal);
                llenarTablaPP(vista.tablaAPerP, this.personal, idTablaProy);
            }

            //SE LIMPIA LA TABLA
            //limpiarTabla();
        }
        if (e.getSource() == vista.btnAceptar) {
            // EL BOTON ACEPTAR SEUSA PARA FINALIZAR LA OPERACION POR ENDE VUELVE A DEFAULT LA PANTALLA
            limpiarTabla();
            limpiarTablaAPP();
            vista.comboPersonal.setEnabled(false);
            vista.botonAggPerfil.setEnabled(false);
            vista.btnEliminar.setEnabled(false);
            vista.btnAceptar.setEnabled(false);
            vista.btnBuscarAPP.setEnabled(true);
            vista.btnAgg.setEnabled(false);
            vista.btnBuscar.setEnabled(true);
            vista.comboOpcion.setEnabled(false);

        }
        if (e.getSource() == vista.btnEliminar) {
            eliminar();

        }
        if (e.getSource() == vista.btnBuscarAPP) {
            limpiarTablaAPP();
            try {
                buscarAPP(vista.tablaAPerP);
            } catch (SQLException ex) {
                Logger.getLogger(ControladorAsignarPersonalAProyecto.class.getName()).log(Level.SEVERE, null, ex);
            }
            vista.btnEliminar.setEnabled(true);
            vista.btnAceptar.setEnabled(true);
            vista.btnBuscar.setEnabled(false);
            vista.btnAgg.setEnabled(false);

        }
    }

    public void eliminar() {
        int fila = vista.tablaAPerP.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Debe Seleccionar un personal asignado");
        } else {
            //EN CASO DE QUE SE HAYA SELECCIONADO UN PERSONAL, SE LE CONSULTA SI REALMENTE DESEA ELIMINARLO
            int variable = JOptionPane.showOptionDialog(null, "¿Deseas eliminar un personal asignado?", "Eliminacion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]);
            if (variable == 0) {
                //SE GUARDA EL ID DEL PERSONAL ASIGNADO SELECCIONADO PARA PASARLO COMO PARAMETRO Y SER USADO EN LA CONSULTA A LA BD
                int id = Integer.parseInt((String) vista.tablaAPerP.getValueAt(fila, 0).toString());
                //SE EJECUTA LA ELIMINACION DEL REGISTRO EN LA BD
                ppDao.eliminar(id);
                modelo2.removeRow(fila);
                //SE LE AVISA AL USUARIO QUE EL REGISTRO FUE ELIMINADO
                JOptionPane.showMessageDialog(vista, "Personl asignado  eliminado");
            }
        }
    }

    public void agregar() {

        this.personal = (vista.comboPersonal.getItemAt(vista.comboPersonal.getSelectedIndex()).getIdPersonal());
        pp.setIdProyecto(idTablaProy);
        pp.setIdPersonal(personal);
        int r = ppDao.agregar(pp);

        if (r == 1) {
            JOptionPane.showMessageDialog(vista, "Personal asignado a proyecto con exito.");
        } else {
            JOptionPane.showMessageDialog(vista, "Error");

            //POR ULTIMO SE LIMPIA LA TABLA EN LA QUE SE MUESTRAN LOS REGISTROS DE PROYECTO.
        } //limpiarTabla();

    }

    public void buscar(JTable tabla) {

        //SE CENTRAN LAS CELDAS DE LA TABLA
        centrarCeldas(tabla);
        //AL MODELO CREADO SE LE PASA EL MODELA DE LA TABLA
        modelo = (DefaultTableModel) tabla.getModel();
        tabla.setModel(modelo);
        //SE CREA UN VECTOR DE PROYECTOS
        ArrayList<Proyecto> lista = dao.listar();

        //SE CREA UN VECTOR DE 2 OBJETOS Y EN CADA UNO SE GUARDAN LOS DATOS QUE COMPONEN A CADA PROYECTO
        Object[] objeto = new Object[2];

        for (int i = 0; i < lista.size(); i++) {

            objeto[0] = lista.get(i).getIdProyecto();
            objeto[1] = lista.get(i).getNombre();

            //SE AGREGAN LOS DATOS AL MODELO 
            modelo.addRow(objeto);
        }

        //SE DEFINE EL NUMERO Y EL TAMAÑO DE CADA FILA
        tabla.setRowHeight(35);
        tabla.setRowMargin(10);
    }

    public void buscarAPP(JTable tabla) throws SQLException {

        //AL MODELO CREADO SE LE PASA EL MODELA DE LA TABLA
        modelo2 = (DefaultTableModel) tabla.getModel();
        tabla.setModel(modelo2);
        //SE CREA UN VECTOR DE PROYECTOTS, PROYECTOS PERSONAL, PERSONA, Y PERSONAL
        ArrayList<ProyectoPersonal> lista = ppDao.listar();
        ArrayList<Proyecto> listap = dao.listar();
        ArrayList<Personal> listaper = daope.listar();
        ArrayList<Persona> listaPersona = per.getPersona();

        //SE CREA UN VECTOR DE 6 OBJETOS Y EN CADA UNO SE GUARDAN LOS DATOS QUE COMPONEN A CADA VALOR DE LA TABALA
        Object[] objeto = new Object[6];
        //SE RECORRE EL LISTADO DE LA TABLA INTERMEDIA
        for (int i = 0; i < lista.size(); i++) {
            //SE RECORRE EL ISTADO DE PROYECTOS
            for (int j = 0; j < listap.size(); j++) {
                //SE RECORRE EL LISTADO DE PERSONAL
                for (int b = 0; b < listaper.size(); b++) {
                    //SE RECORRE EL LISTADO DE PERSONAS
                    for (int k = 0; k < listaPersona.size(); k++) {
                        // SE USA ESTE IF PARA PODER RELACIONAR LOS DATOS QUE ESTAN EN LA TABLA INTERMEDIA CON LOS DATOS DE LAS TABLAS DE LA BD CORRESPONDIESTES QUE LUEGO SE USAN PARA EMPAREJARLOS Y PODER OBETENER EL ID,NOMBRE,ETC
                        if (lista.get(i).getIdProyecto() == listap.get(j).getIdProyecto() && lista.get(i).getIdPersonal() == listaper.get(b).getIdPersonal() && listaper.get(b).getPersona() == listaPersona.get(k).getIdPersona()) {
                            objeto[0] = lista.get(i).getId();
                            objeto[1] = lista.get(i).getIdProyecto();

                            objeto[2] = listap.get(j).getNombre();
                            objeto[3] = listaper.get(b).getCUIT();

                            objeto[4] = listaPersona.get(k).getNombre();
                            objeto[5] = listaPersona.get(k).getApellido();

                            //SE AGREGAN LOS DATOS AL MODELO 
                            modelo2.addRow(objeto);
                        }
                    }
                }
            }
        }

        //SE DEFINE EL NUMERO Y EL TAMAÑO DE CADA FILA
        tabla.setRowHeight(35);
        tabla.setRowMargin(10);
    }

    void centrarCeldas(JTable tabla) {
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < vista.tablaProyecto.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(tcr);
        }
    }

    void limpiarTabla() {
        //RECORRE TODA LA TABLA Y REMUEVE CADA REGISTRO. 
        for (int i = 0; i < vista.tablaProyecto.getRowCount(); i++) {
            modelo.removeRow(i);
            i = i - 1;
        }
    }

    void limpiarTablaAPP() {
        //RECORRE TODA LA TABLA Y REMUEVE CADA REGISTRO. 
        for (int i = 0; i < vista.tablaAPerP.getRowCount(); i++) {
            modelo2.removeRow(i);
            i = i - 1;
        }
    }

    public void llenarPersonales() throws SQLException {

        ArrayList<Persona> listaPersona = per.getPersona();
        vista.comboPersonal.removeAllItems();
        //SE RECORRE EL VECTOR DE LAS PERSONALES QUE SE TRAJO DESDE LA BD
        for (int i = 0; i < listaPersonal.size(); i++) {

            for (int j = 0; j < listaPersona.size(); j++) {
                //SE USA  ESTE IF PARA PODER EMPAREJAS EL PERSONAL CON LA PERSONA Y OBETERN SUS DATOS
                if (listaPersonal.get(i).getPersona() == listaPersona.get(j).getIdPersona()) {

                    vista.comboPersonal.addItem(new Personal(listaPersonal.get(i).getIdPersonal(), listaPersonal.get(i).getCUIT(), listaPersona.get(j).getNombre(), listaPersona.get(j).getApellido(), listaPersona.get(j).getFechaNacimiento(), listaPersona.get(j).getTelefono()));

                }
            }
        }
    }

    public void sugeridos() throws SQLException {
        String opcion = vista.comboOpcion.getSelectedItem().toString();
        String opcionE = "Todos";
        String opcionE2 = "Sugeridos";
        //   ArrayList<Personal> listaPersonal = daoPersonal.listar();
        if (opcion.equals(opcionE)) {
            //SI LA OPCION ES TODOS ENTONCES USA EL LLENAR PERSONALES 
            llenarPersonales();

            // JLabel.setText("Estoy modificando el texto");
        } else if (opcionE2.equals(opcion)) {

            // SI LA OPCION ES SUGERIDOS SE LLAMA A LA CONSULTA HECHA EN EL DAO QUE TRAE LOS PERFILES SUGERIDOS PARA EL PROYECTO
            vista.comboPersonal.removeAllItems();
            ArrayList<Persona> listaPersona = ppDao.sugeridos(idTablaProy);
            for (int i = 0; i < listaPersonal.size(); i++) {
                for (int j = 0; j < listaPersona.size(); j++) {
                    if (listaPersona.get(j).getIdPersona() == listaPersonal.get(i).getPersona()) {

                        vista.comboPersonal.addItem(new Personal(listaPersonal.get(i).getIdPersonal(), listaPersonal.get(i).getCUIT(), listaPersona.get(j).getNombre(), listaPersona.get(j).getApellido(), listaPersona.get(j).getFechaNacimiento(), listaPersona.get(j).getTelefono()));
                    }
                }
            }
        }

    }

    public boolean comprobarPerfiles(int idProy, int idPer) {
        ArrayList<ProyectoPersonal> lista = ppDao.listar();

        for (int i = 0; i < lista.size(); i++) {
            if (idProy == lista.get(i).getIdProyecto() && idPer == lista.get(i).getIdPersonal()) {
                return true;

            }
        }
        return false;

    }

    public void llenarTablaPP(JTable tabla, int idPer, int idProy) {
        //SE CENTRAN LAS CELDAS DE LA TABLA
        centrarCeldas(tabla);
        //AL MODELO CREADO SE LE PASA EL MODELA DE LA TABLA
        modelo2 = (DefaultTableModel) tabla.getModel();
        tabla.setModel(modelo2);
        //SE CREA UN VECTOR DE PROYECTOS
        ArrayList<ProyectoPersonal> lista = ppDao.listar();
        ArrayList<Proyecto> listap = dao.listar();
        ArrayList<Personal> listaper = daope.listar();

        //SE CREA UN VECTOR DE 8 OBJETOS Y EN CADA UNO SE GUARDAN LOS DATOS QUE COMPONEN A CADA PROYECTO
        Object[] objeto = new Object[6];
        //COMO ANDUVO??? NO SE 
        //for para recorer el app
        for (int i = 0; i < lista.size(); i++) {
            //for para recorrer el proyecto
            for (int j = 0; j < listap.size(); j++) {
                //for para recorter los perfiles
                for (int b = 0; b < listaper.size(); b++) {
                    // el if solo se va a cumplir si el id del proyecto y del personal  son iguales tanto a lo obtenido en el listar del app y en los listar de proyecto y personal
                    if (idProy == lista.get(i).getIdProyecto() && idPer == lista.get(i).getIdPersonal() && idProy == listap.get(j).getIdProyecto() && idPer == listaper.get(b).getIdPersonal()) {
                        objeto[0] = lista.get(i).getId();
                        System.out.println(" aa " + lista.get(i).getId());
                        objeto[1] = lista.get(i).getIdProyecto();

                        objeto[2] = listap.get(j).getNombre();
                        //SPLITEA LO QUE SE SELECCIONA EN EL COMBO 
                        String[] opcion = vista.comboPersonal.getSelectedItem().toString().split("-");
                        objeto[3] = opcion[0];
                        //int aux2 = lista.get(i).getIdPerfil();
                        objeto[4] = opcion[1];
                        objeto[5] = opcion[2];

                        //SE AGREGAN LOS DATOS AL MODELO 
                        modelo2.addRow(objeto);
                    }
                }
            }
        }

        //SE DEFINE EL NUMERO Y EL TAMAÑO DE CADA FILA
        tabla.setRowHeight(35);
        tabla.setRowMargin(10);

    }

}
