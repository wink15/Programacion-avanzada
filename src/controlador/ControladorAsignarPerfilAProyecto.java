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

    ProyectoDAO dao = new ProyectoDAO();
    ProyectoPerfil pp = new ProyectoPerfil();
    VistaPerfilProyec vista = new VistaPerfilProyec();
    DefaultTableModel modelo = new DefaultTableModel();
    ProyectoPerfilDAO ppDao = new ProyectoPerfilDAO();
    PerfilDAO daope = new PerfilDAO();
    private int idTablaProy;

    public ControladorAsignarPerfilAProyecto(VistaPerfilProyec v) {
        this.vista = v;
        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnAgg.addActionListener(this);
        this.vista.botonAggPerfil.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnBuscar) {

            //SE LIMPIA LA TABLA
            limpiarTabla();
            //SE BUSCAN LOS PROYECTO
            buscar(vista.tablaProyecto);
            //SE PREPARA LA VISTA PARA UN NUEVO PROYECTO
            //nuevo();

        }
        if (e.getSource() == vista.btnAgg) {

            try {
                llenarPerfiles();
            } catch (SQLException ex) {
                Logger.getLogger(ControladorAsignarPerfilAProyecto.class.getName()).log(Level.SEVERE, null, ex);
            }
            //SE BUSCAN LOS PROYECTO
            // buscar(vista.tablaProyecto);
            vista.comboPerfiles.setEnabled(true);
            vista.botonAggPerfil.setEnabled(true);
            vista.btnEliminar.setEnabled(true);
            int fila = vista.tablaProyecto.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(vista, "Debe seleccionar un personal");
            } else {
                this.idTablaProy = Integer.parseInt((String) vista.tablaProyecto.getValueAt(fila, 0).toString());
            }

            //SE PREPARA LA VISTA PARA UN NUEVO PROYECTO
            //nuevo();
        }
        if (e.getSource() == vista.botonAggPerfil) {
            int perfil = (vista.comboPerfiles.getItemAt(vista.comboPerfiles.getSelectedIndex()).getId());
            if (comprobarPerfiles(idTablaProy, perfil) == true) {
                JOptionPane.showMessageDialog(null, "El perfil seleccionado ya fue asiganado a dicho proyecto", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                agregar();
                llenarTablaPP(vista.tablaAPP, perfil, idTablaProy);
            }

            //SE LIMPIA LA TABLA
            // limpiarTabla();
        }
        // int id = Integer.parseInt((String) vista.tabla.getValueAt(fila, 0).toString());
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

    public void llenarPerfiles() throws SQLException {

        //SE CREA UNA INSTANCIA DE LA CLASE TipoProyectoDAO
        PerfilDAO daoPerfil = new PerfilDAO();
        //SE GUARDA EN UN VECTOR TODOS LOS TIPOS DE PROYECTO DESDE LA BD
        ArrayList<Perfil> listaPerfil = daoPerfil.getPerfil();
        //SE VACIA EL CLOMBO TIPO PROYECTO
        vista.comboPerfiles.removeAllItems();
        //SE RECORRE EL VECTOR DE LOS TIPOS DE PROYECTOS QUE SE TRAJO DESDE LA BD
        for (int i = 0; i < listaPerfil.size(); i++) {
            //CADA TIPO DE PROYECTO DE LA LISTA SE AGREGA AL COMBO

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
        modelo = (DefaultTableModel) tabla.getModel();
        tabla.setModel(modelo);
        //SE CREA UN VECTOR DE PROYECTOS
        ArrayList<ProyectoPerfil> lista = ppDao.listar();
        ArrayList<Proyecto> listap = dao.listar();
        ArrayList<Perfil> listaper = daope.listar();

        //SE CREA UN VECTOR DE 8 OBJETOS Y EN CADA UNO SE GUARDAN LOS DATOS QUE COMPONEN A CADA PROYECTO
        Object[] objeto = new Object[5];
        
        for (int i = 0; i < lista.size(); i++) {
            for(int j = 0; j< listap.size(); j++){
                for (int b=0; b<listaper.size();b++){
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
                modelo.addRow(objeto);
            }
        }}}

        //SE DEFINE EL NUMERO Y EL TAMAÑO DE CADA FILA
        tabla.setRowHeight(35);
        tabla.setRowMargin(10);

    }
}
