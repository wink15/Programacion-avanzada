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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import modelo.Perfil;
import modelo.PerfilDAO;
import modelo.Proyecto;
import modelo.ProyectoDAO;
import modelo.ProyectoPerfil;
import modelo.ProyectoPerfilDAO;
import vista.VistaPerfilProyec;
import vista.VistaProyecto;

/**
 *
 * @author Santiago
 */
public class ControladorAsignarPerfilAProyecto implements ActionListener {

    //VECTOR QUE CONTIENE LOS BOTONES PARA LAS CONSULTAS AL USUARIO
    String[] botones = {"Aceptar", "Cancelar"};
    ProyectoDAO dao = new ProyectoDAO();
    ProyectoPerfil pp = new ProyectoPerfil();
    VistaPerfilProyec vista = new VistaPerfilProyec();
    DefaultTableModel modelo = new DefaultTableModel();
    DefaultTableModel modelo2 = new DefaultTableModel();
    ProyectoPerfilDAO ppDao = new ProyectoPerfilDAO();
    PerfilDAO daope = new PerfilDAO();
    private int idTablaProy;

    //CREAMOS UN CONTROLADOR POR CADA VISTA EXISTENTE EN DONDE SE LES PASA EL ACTION PARA PODER UTILIZARLOS
    public ControladorAsignarPerfilAProyecto(VistaPerfilProyec v) {
        this.vista = v;
        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnAgg.addActionListener(this);
        this.vista.botonAggPerfil.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);
        this.vista.btnAceptar.addActionListener(this);
        this.vista.btnBuscarAPP.addActionListener(this);

    }
//EJECUCION DE CADA BOTON DENTRO DE CADA PANTALLA

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnBuscar) {

            //SE BUSCAN LOS PROYECTO
            buscar(vista.tablaProyecto);
            vista.btnBuscarAPP.setEnabled(false);

        }
        if (e.getSource() == vista.btnAgg) {

            try {
                llenarPerfiles();
            } catch (SQLException ex) {
                Logger.getLogger(ControladorAsignarPerfilAProyecto.class.getName()).log(Level.SEVERE, null, ex);
            }
            //SE BUSCAN LOS PROYECTO
            // buscar(vista.tablaProyecto);

            int fila = vista.tablaProyecto.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(vista, "Debe seleccionar un personal");
            } else {
                //SE HABILITAN LOS BOTONES QUE SE PUEDEN UTILIZAR UNA VEZ SELECCIONADO UN PROYECTO
                vista.comboPerfiles.setEnabled(true);
                vista.botonAggPerfil.setEnabled(true);
                vista.btnEliminar.setEnabled(true);
                vista.btnAceptar.setEnabled(true);
                //SE GUARDA EL ID DEL PROYECTO SELECCIONADO
                this.idTablaProy = Integer.parseInt((String) vista.tablaProyecto.getValueAt(fila, 0).toString());
            }

        }
        if (e.getSource() == vista.botonAggPerfil) {
            //SE GUARDA EL ID DEL PERFIL SELECCIONADO 
            int perfil = (vista.comboPerfiles.getItemAt(vista.comboPerfiles.getSelectedIndex()).getId());
            // SE LLAMA AL METODO COMPROBARPERFILES QUE SE ENCARGA DE VERIFICAR SI EL PERFIL YA FUE ASIGNADO AL PROYECTO
            if (comprobarPerfiles(idTablaProy, perfil) == true) {
                JOptionPane.showMessageDialog(null, "El perfil seleccionado ya fue asiganado a dicho proyecto", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                //EN CASO DE NO ESTAR YA ASIGNADO SE AGREGA Y SE LLENA LA TABAL
                agregar();
                llenarTablaPP(vista.tablaAPP, perfil, idTablaProy);
            }

            //SE LIMPIA LA TABLA
            //limpiarTabla();
        }
        if (e.getSource() == vista.btnAceptar) {
            //EL BOTON ACEPTAR SE ENCARGA DE FINALIZAR TODO POR ENDE VUELVE AL DEFAULT LA PANTALLA
            limpiarTabla();
            limpiarTablaAPP();
            vista.comboPerfiles.setEnabled(false);
            vista.botonAggPerfil.setEnabled(false);
            vista.btnEliminar.setEnabled(false);
            vista.btnAceptar.setEnabled(false);
            vista.btnBuscarAPP.setEnabled(true);
            vista.btnAgg.setEnabled(true);
            vista.btnBuscar.setEnabled(true);

        }
        if (e.getSource() == vista.btnEliminar) {
            eliminar();

        }
        if (e.getSource() == vista.btnBuscarAPP) {

            buscarAPP(vista.tablaAPP);
            vista.btnEliminar.setEnabled(true);
            vista.btnAceptar.setEnabled(true);
            vista.btnBuscar.setEnabled(false);
            vista.btnAgg.setEnabled(false);

        }

    }

    public void eliminar() {
        int fila = vista.tablaAPP.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Debe Seleccionar un perfil asignado");
        } else {
            //EN CASO DE QUE SE HAYA SELECCIONADO UN PERFIL, SE LE CONSULTA SI REALMENTE DESEA ELIMINARLO
            int variable = JOptionPane.showOptionDialog(null, "¿Deseas eliminar un perfil asignado?", "Eliminacion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]);
            if (variable == 0) {
                //SE GUARDA EL ID DEL PERFIL SELECCIONADO PARA PASARLO COMO PARAMETRO Y SER USADO EN LA CONSULTA A LA BD
                int id = Integer.parseInt((String) vista.tablaAPP.getValueAt(fila, 0).toString());
                //SE EJECUTA LA ELIMINACION DEL REGISTRO EN LA BD

                ppDao.eliminar(id);
                modelo2.removeRow(fila);
                //SE LE AVISA AL USUARIO QUE EL REGISTRO FUE ELIMINADO
                JOptionPane.showMessageDialog(vista, "Perfil asignado  eliminado");
            }
        }
    }

    public void agregar() {

        int perfil = (vista.comboPerfiles.getItemAt(vista.comboPerfiles.getSelectedIndex()).getId());
        pp.setIdProyecto(idTablaProy);
        pp.setIdPerfil(perfil);
        int r = ppDao.agregar(pp);

        if (r == 1) {
            JOptionPane.showMessageDialog(vista, "Proyecto agregado con Exito.");
        } else {
            JOptionPane.showMessageDialog(vista, "Error");

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

        //SE CREA UN VECTOR DE 8 OBJETOS Y EN CADA UNO SE GUARDAN LOS DATOS QUE COMPONEN A CADA PROYECTO
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

    public void buscarAPP(JTable tabla) {

        //AL MODELO CREADO SE LE PASA EL MODELA DE LA TABLA
        modelo2 = (DefaultTableModel) tabla.getModel();
        tabla.setModel(modelo2);
        //SE CREA UN VECTOR DE PROYECTOS
        ArrayList<ProyectoPerfil> lista = ppDao.listar();
        ArrayList<Proyecto> listap = dao.listar();
        ArrayList<Perfil> listaper = daope.listar();

        //SE CREA UN VECTOR DE 8 OBJETOS Y EN CADA UNO SE GUARDAN LOS DATOS QUE COMPONEN A CADA PERFIL ASIGNADO AL PROYECTO
        Object[] objeto = new Object[5];
        // SE RECORRE LOS PERFILES ASIGNADOS
        for (int i = 0; i < lista.size(); i++) {
            //SE RECORRE LOS PROYECTOS
            for (int j = 0; j < listap.size(); j++) {
                //SE RECORREN LOS PERFILES
                for (int b = 0; b < listaper.size(); b++) {
                    //SE USA ESTE IF PARA PODER OBTENER LOS NOMBRES DEL PROYECTO Y PERFIL CORRESPONDIENTE
                    // PORQUE SE BUSCA EL PROYECTO Y EL PERFIL QUE SEAN IGUAL A LOS QUE ESTAN EN ALMACENADOS EN LA TABLA INTERMEDIA
                    if (lista.get(i).getIdProyecto() == listap.get(j).getIdProyecto() && lista.get(i).getIdPerfil() == listaper.get(b).getId()) {
                        objeto[0] = lista.get(i).getId();
                        objeto[1] = lista.get(i).getIdProyecto();

                        objeto[2] = listap.get(j).getNombre();
                        objeto[3] = lista.get(i).getIdPerfil();

                        objeto[4] = listaper.get(b).getNombre();

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
        for (int i = 0; i < vista.tablaAPP.getRowCount(); i++) {
            modelo2.removeRow(i);
            i = i - 1;
        }
    }

    public void llenarPerfiles() throws SQLException {

        //SE CREA UNA INSTANCIA DE LA CLASE PERFILDAO
        PerfilDAO daoPerfil = new PerfilDAO();
        //SE GUARDA EN UN VECTOR TODOS LOS PERFILES DESDE LA BD
        ArrayList<Perfil> listaPerfil = daoPerfil.getPerfil();
        //SE VACIA EL CLOMBO PERFIL
        vista.comboPerfiles.removeAllItems();
        //SE RECORRE EL VECTOR DE LOS PERFILES QUE SE TRAJO DESDE LA BD
        for (int i = 0; i < listaPerfil.size(); i++) {
            //CADA PERFIL DE LA LISTA SE AGREGA AL COMBO

            vista.comboPerfiles.addItem(new Perfil(listaPerfil.get(i).getId(), listaPerfil.get(i).getNombre()));

        }
    }

    public boolean comprobarPerfiles(int idProy, int idPer) {
        ArrayList<ProyectoPerfil> lista = ppDao.listar();

        for (int i = 0; i < lista.size(); i++) {
            if (idProy == lista.get(i).getIdProyecto() && idPer == lista.get(i).getIdPerfil()) {
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
        ArrayList<ProyectoPerfil> lista = ppDao.listar();
        ArrayList<Proyecto> listap = dao.listar();
        ArrayList<Perfil> listaper = daope.listar();

        //SE CREA UN VECTOR DE 8 OBJETOS Y EN CADA UNO SE GUARDAN LOS DATOS QUE COMPONEN A CADA PROYECTO
        Object[] objeto = new Object[5];
        //COMO ANDUVO??? NO SE 
        //for para recorer el app
        for (int i = 0; i < lista.size(); i++) {
            //for para recorrer el proyecto
            for (int j = 0; j < listap.size(); j++) {
                //for para recorter los perfiles
                for (int b = 0; b < listaper.size(); b++) {
                    // el if solo se va a cumplir si el id del proyecto y del perfil son iguales tanto a lo obtenido en el listar del app y en los listar de proyecto y perfil
                    if (idProy == lista.get(i).getIdProyecto() && idPer == lista.get(i).getIdPerfil() && idProy == listap.get(j).getIdProyecto() && idPer == listaper.get(b).getId()) {
                        objeto[0] = lista.get(i).getId();
                        System.out.println(" aa " + lista.get(i).getId());
                        objeto[1] = lista.get(i).getIdProyecto();
                        // int aux = lista.get(i).getIdProyecto();
                        objeto[2] = listap.get(j).getNombre();
                        objeto[3] = lista.get(i).getIdPerfil();
                        //int aux2 = lista.get(i).getIdPerfil();
                        objeto[4] = listaper.get(b).getNombre();

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
