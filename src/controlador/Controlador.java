/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

//import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import modelo.Clientes;
import modelo.Proyecto;
import modelo.ProyectoDAO;
import vista.VistaProyecto;
import modelo.ClientesDAO;
import modelo.Persona;
import modelo.PersonaDAO;
import modelo.TipoProyecto;
import modelo.TipoProyectoDAO;


import vista.VistaClientes;



public class Controlador implements ActionListener {
    //VECTOR QUE CONTIENE LOS BOTONES PARA LAS CONSULTAS AL USUARIO
    String [] botones = { "Aceptar", "Cancelar"};
    
    //Constructor sin parametros 
    public Controlador () {
    }

    //CREAMOS INSTANCIAS DE OTRAS CLASES
    ProyectoDAO dao = new ProyectoDAO();
    Proyecto p = new Proyecto();
    VistaProyecto vista = new VistaProyecto();
    DefaultTableModel modelo = new DefaultTableModel();
    

    //CREAMOS UN CONTROLADOR POR CADA VISTA EXISTENTE
    public Controlador(VistaProyecto v) {
        this.vista = v;
        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnAgregar.addActionListener(this);
        this.vista.btnModificar.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);
        this.vista.btnActualizar.addActionListener(this);
    }
    
    
    //EJECUCION DE CADA BOTON DENTRO DE CADA PANTALLA
    public void actionPerformed(ActionEvent e) {
        //VISTA PROYECTO
        if (e.getSource() == vista.btnBuscar) {
            //SE LIMPIA LA TABLA
            limpiarTabla();
            //SE BUSCAN LOS PROYECTO
            buscar(vista.tabla);
            //SE PREPARA LA VISTA PARA UN NUEVO PROYECTO
            nuevo();
        }
        if (e.getSource() == vista.btnEliminar) {
                //SE ELIMINA UN PROYECTO TANTO DE LA TABLA COMO DE LA BD
                eliminar();
                //SE BUSCA UN PROYECTO LOS PROYECTOS EXISTENTES
                buscar(vista.tabla);
                //SE PREPARA LA VISTA PARA UN NUEVO PROYECTO
                nuevo();
        }    
        if (e.getSource() == vista.btnAgregar) {
            //SE VALIDA QUE ESTEN TODOS LOS CAMPOS COMPLETADOS
            if (vista.txtNombre.getText().equals("") || vista.txtFechaInicio.getDate() == null || vista.txtFechaConfirmacion.getDate() == null || vista.txtFechaFin.getDate() == null || vista.txtObservacion.getText().equals("")){
                JOptionPane.showMessageDialog(null,"Debe completar todos los campos");
            } else {
                //SE VALIDAN LAS FECHA DEL PROYECTO
                Date fechaactual = new Date(System.currentTimeMillis());
                if (vista.txtFechaConfirmacion.getDate().getTime() >= fechaactual.getTime()) {
                    if (vista.txtFechaInicio.getDate().getTime() >= vista.txtFechaConfirmacion.getDate().getTime()){
                        if (vista.txtFechaFin.getDate().getTime() >= vista.txtFechaInicio.getDate().getTime()){
                            int variable = JOptionPane.showOptionDialog (null, "¿Deseas agregar un proyecto?", "Agregacion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]); 
                            //EN CASO DE QUE EL USUARIO DESEA REALMENTE AGREGAR UN PROYECTO
                            if (variable == 0) {
                                //SE AGREGA EL PROYECTO
                                agregar();
                                //SE BUSCAN LOS NUEVOS PROYECTOS DE LA BD
                                buscar(vista.tabla);
                                //SE PREPARA LA VISTA PARA UN NUEVO PROYECTO
                                nuevo();
                            } else {
                                //DE LO CONTRARIO SE PREPARA TAMBIEN LA VISTA PARA UN NUEVO PROYECTO
                                nuevo();
                            }
                        }else {
                             JOptionPane.showMessageDialog(null, "La fecha de fin debe ser mayor a la fecha de inicio.");
                        }   
                    } else{
                        JOptionPane.showMessageDialog(null, "La fecha de inicio debe ser mayor a la fecha de confirmacion.");
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "La fecha de confirmacion debe ser mayor o igual a la actual .");
                }
            }    
        }
        if (e.getSource() == vista.btnModificar) {
            //SE VALIDA QUE SE HAYA SELECCIONADO UN PROYECTO
            int fila = vista.tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(vista, "Debe seleccionar un proyecto");
            } else {
                    //SE DESHABILITAN LOS BOTONES
                    vista.btnAgregar.setEnabled(false);
                    vista.btnEliminar.setEnabled(false);
                    vista.btnActualizar.setEnabled(true);
                    vista.btnModificar.setEnabled(false);
                    
                    //EN CASO QUE SE HAYA SELECCIONADO UN PROYECTO, SE GUARDAN LOS DATOS DE LA TABLA EN VARIABLES
                    int id = Integer.parseInt((String) vista.tabla.getValueAt(fila, 0).toString());
                    String nombre = (String) vista.tabla.getValueAt(fila, 1);
                    Date fechaInicio = (Date) vista.tabla.getValueAt(fila, 2);
                    Date fechaConfirmacion = (Date) vista.tabla.getValueAt(fila, 3);
                    Date fechaFin = (Date) vista.tabla.getValueAt(fila, 4);
                    int cliente = Integer.parseInt((String) vista.tabla.getValueAt(fila, 5).toString());
                    int tipoProyecto = Integer.parseInt((String) vista.tabla.getValueAt(fila, 6).toString());
                    
                    String observacion = (String) vista.tabla.getValueAt(fila, 7);
                    
                    //SE PASAN LOS DATOS DE LAS VARIABLES A LOS CAMPOS
                    vista.txtId.setText("" + id);
                    vista.txtNombre.setText(nombre);
                    vista.txtFechaInicio.setDate(fechaInicio);
                    vista.txtFechaConfirmacion.setDate(fechaConfirmacion);
                    vista.txtFechaFin.setDate(fechaFin);
                    vista.cboCliente.setSelectedItem(cliente);
                    vista.cboTipoProyecto.setSelectedItem(tipoProyecto);
                    vista.txtObservacion.setText(observacion);
            }
        }
        
        if (e.getSource() == vista.btnActualizar) {
                //SE ACTUALIZA UN PROYECTO
                actualizar();
                //SE BUSCAN LOS NUEVOS PROYECTOS DE LA BD
                buscar(vista.tabla);
                //SE PREPARA LA VISTA PARA UN NUEVO PROYECTO
                nuevo();
                //SE VUELVEN A HABILITAR LOS BOTONES
                vista.btnAgregar.setEnabled(true);
                vista.btnEliminar.setEnabled(true);
                vista.btnActualizar.setEnabled(false);
                vista.btnModificar.setEnabled(true);
        }
    }
    
    //METODO ACTUALIZAR 
    public void actualizar() {
       //VALIDACION DE CAMPOS VACIOS NECESARIOS PARA ACTUALIZAR EL REGISTRO EN LA BD.  
       if (vista.txtId.getText().equals("")) {
            JOptionPane.showMessageDialog(vista, "Primero debe modificar un proyecto");
        } else {
           //EN CASO DE QUE TODOS LOS CAMPOS ESTAN COMPLETOS, SE LE CONSULTA AL CLIENTE SI ESTA SEGURO DE MODIFICAR EL PROYECTO
           int variable = JOptionPane.showOptionDialog (null, "¿Deseas modificar un proyecto?", "Proyecto", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]); 
           //SI LA RESPUESTA ES QUE SI DESEA MODIFICARLO
           if (variable == 0) {
                //SE PASAN LOS DATOS DESDE LOS CAMPOS DE LA PANTALLA A VARIABLES
                String idAux= vista.txtId.getText();
                int id = Integer.valueOf(idAux);
                String nombre = vista.txtNombre.getText();
                java.util.Date date = vista.txtFechaInicio.getDate();
                long aux1=date.getTime();
                java.sql.Date fechaInicio= new java.sql.Date(aux1);
                java.util.Date date2= vista.txtFechaConfirmacion.getDate();
                long aux2=date2.getTime();
                java.sql.Date fechaConfirmacion= new java.sql.Date(aux2);
                java.util.Date date3 = vista.txtFechaFin.getDate();
                long aux3=date3.getTime();
                java.sql.Date fechaFin= new java.sql.Date(aux3);
                int tipoProyecto =  vista.cboTipoProyecto.getSelectedIndex();
                Integer cliente = vista.cboCliente.getSelectedIndex();
                String observacion = vista.txtObservacion.getText();

                //SETEAMOS CADA VALOR EN LA INSTANCIA CREADA DE PROYECTO
                p.setIdProyecto(id);
                p.setNombre(nombre);
                p.setFechaInicio(fechaInicio);
                p.setFechaConfirmacion(fechaConfirmacion);
                p.setFechaFin(fechaFin);
                p.setTipoProyecto(tipoProyecto);
                p.setCliente(cliente);
                p.setObservacion(observacion); 
                
                //GUARDAMOS EN r EL RESULTADO DE LA ACTUALIZACION (1 COMPLETADA, 0 FALLIDA)
                int r= dao.actualizar(p);
                if (r == 1) {
                    JOptionPane.showMessageDialog(vista, "Proyecto actualizado con exito");
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al actualizar");
                }
            }else{
               //EN CASO DE NO DESEAR ACTUALIZAR EL PROYECTO SE VACIAN TODOS LOS CAMPOS DE LA PANTALLA.
               nuevo();
            } 
        }
        //POR ULTIMO SE LIMPIA LA TABLA EN LA QUE SE MUESTRAN LOS REGISTROS DE PROYECTO.
        limpiarTabla();
    }
    
    //METODO AGREGAR 
    public void agregar() {
        try {
            //SE PASAN LOS DATOS DESDE LOS CAMPOS DE LA PANTALLA A VARIABLES
            String nombre = vista.txtNombre.getText();
          
            java.util.Date date = vista.txtFechaInicio.getDate();
            long aux1 = date.getTime();
            java.sql.Date fechaInicio= new java.sql.Date(aux1);
            
            java.util.Date date2= vista.txtFechaConfirmacion.getDate();
            long aux2=date2.getTime();
            java.sql.Date fechaConfirmacion= new java.sql.Date(aux2);
            
            java.util.Date date3 = vista.txtFechaFin.getDate();
            long aux3=date3.getTime();
            java.sql.Date fechaFin= new java.sql.Date(aux3);
            
            Integer tipoProyecto = (vista.cboTipoProyecto.getItemAt(vista.cboTipoProyecto.getSelectedIndex()).getIdTipoProyecto());
            Integer cliente = (vista.cboCliente.getItemAt(vista.cboCliente.getSelectedIndex()).getIdCliente());
            String observacion = vista.txtObservacion.getText();
            
            //SETEAMOS CADA VALOR EN LA INSTANCIA CREADA DE PROYECTO
            p.setNombre(nombre);
            p.setFechaInicio(fechaInicio);
            p.setFechaConfirmacion(fechaConfirmacion);
            p.setFechaFin(fechaFin);
            p.setTipoProyecto(tipoProyecto);
            p.setCliente(cliente);
            p.setObservacion(observacion);
            
            //GUARDAMOS EN r EL RESULTADO DE LA AGREGACION (1 COMPLETADA, 0 FALLIDA)
            int r = dao.agregar(p);
            if (r == 1) {
                JOptionPane.showMessageDialog(vista, "Proyecto agregado con Exito.");
            } else {
                JOptionPane.showMessageDialog(vista, "Error");
            }
            //POR ULTIMO SE LIMPIA LA TABLA EN LA QUE SE MUESTRAN LOS REGISTROS DE PROYECTO.
            limpiarTabla();
        } catch (HeadlessException e) {

        }
    }

    //METODO ELIMINAR
    public void eliminar() {
        //VALIDACION DE REGISTRO NO SELECCIONADO DESDE LA TABLA
        int fila = vista.tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Debe Seleccionar un proyecto");
        } else {
            //EN CASO DE QUE SE HAYA SELECCIONADO UN PROYECTO, SE LE CONSULTA SI REALMENTE DESEA ELIMINARLO
            int variable = JOptionPane.showOptionDialog (null, "¿Deseas eliminar un proyecto?", "Eliminacion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]); 
            if (variable == 0) {
                //SE GUARDA EL ID DEL PROYECTO SELECCIONADO PARA PASARLO COMO PARAMETRO Y SER USADO EN LA CONSULTA A LA BD
                int id = Integer.parseInt((String) vista.tabla.getValueAt(fila, 0).toString());
                //SE EJECUTA LA ELIMINACION DEL REGISTRO EN LA BD
                dao.eliminar(id);
                //SE LE AVISA AL USUARIO QUE EL REGISTRO FUE ELIMINADO
                JOptionPane.showMessageDialog(vista, "Proyecto eliminado");
            }
        }
        //POR ULTIMO SE LIMPIA LA TABLA PARA SACAR EL REGISTRO QUE FUE ELIMINADO
        limpiarTabla();
    }

    //PREPARAR LA PANTALLA PARA UN NUEVO PROYECTO
    public void nuevo() {
        //SE VACIAN TODOS LOS CAMPOS QUE PERMITEN AGREGAR UN NUEVO PROYECTO
        vista.txtId.setText("");
        vista.txtNombre.setText("");
        vista.txtFechaInicio.setDate(null);
        vista.txtFechaConfirmacion.setDate(null);
        vista.txtFechaFin.setDate(null);
        vista.txtObservacion.setText("");
        vista.txtNombre.requestFocus();
    }
    
    
    //METODO BUSCAR LOS PROYECTOS EXISTENTES EN LA BD
    public void buscar(JTable tabla) {
        //SE CENTRAN LAS CELDAS DE LA TABLA
        centrarCeldas(tabla);
        //AL MODELO CREADO SE LE PASA EL MODELA DE LA TABLA
        modelo = (DefaultTableModel) tabla.getModel();
        tabla.setModel(modelo);
        //SE CREA UN VECTOR DE PROYECTOS
        ArrayList<Proyecto> lista = dao.listar();
        //SE CREA UN VECTOR DE 8 OBJETOS Y EN CADA UNO SE GUARDAN LOS DATOS QUE COMPONEN A CADA PROYECTO
        Object[] objeto = new Object[8];
        for (int i = 0; i < lista.size(); i++) {
            objeto[0] = lista.get(i).getIdProyecto();
            objeto[1] = lista.get(i).getNombre();
            objeto[2] = lista.get(i).getFechaInicio();
            objeto[3] = lista.get(i).getFechaConfirmacion();
            objeto[4] = lista.get(i).getFechaFin();
            objeto[5] = lista.get(i).getTipoProyecto();
            objeto[6] = lista.get(i).getCliente();
            objeto[7] = lista.get(i).getObservacion();
            //SE AGREGAN LOS DATOS AL MODELO 
            modelo.addRow(objeto);
        }
        
        //SE DEFINE EL NUMERO Y EL TAMAÑO DE CADA FILA
        tabla.setRowHeight(35);
        tabla.setRowMargin(10);
    }

    //METODO DE ESTETICA DE LA TABLA PROYECTO
    void centrarCeldas(JTable tabla) {
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < vista.tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(tcr);
        }
    }
    
    //METODO QUE VACIA LA TABLA PROYECTO
    void limpiarTabla() {
        //RECORRE TODA LA TABLA Y REMUEVE CADA REGISTRO. 
        for (int i = 0; i < vista.tabla.getRowCount(); i++) {
            modelo.removeRow(i);
            i = i - 1;
        }
    }
    
    //METODO PARA LLENAR EL COMBO CLIENTE
    public void llenarClientes() throws SQLException {
        //SE CREA UNA INSTANCIA DE LA CLASE ClientesDAO
        ClientesDAO daoClientes = new ClientesDAO();
        //SE GUARDA EN UN VECTOR TODOS LOS CLIENTES DESDE LA BD
        ArrayList<Clientes> listaClientes = daoClientes.getClientes();
        //SE VACIA EL COMBO CLIENTE
        vista.cboCliente.removeAllItems();
        //SE RECORRE EL VECTOR DE LOS CLIENTES QUE SE TRAJO DESDE LA BD
        for (int i = 0; i < listaClientes.size(); i++) {
           //CADA CLIENTE DE LA LISTA SE AGREGA AL COMBO  
           vista.cboCliente.addItem(new Clientes(listaClientes.get(i).getIdCliente(),listaClientes.get(i).getRazonSocial()));
        }
    }
    
    //METODO PARA LLENAR EL COMBO TIPO PROYECTO
     public void llenarTipoProyecto() throws SQLException {
        //SE CREA UNA INSTANCIA DE LA CLASE TipoProyectoDAO
        TipoProyectoDAO daoTipoProyecto = new TipoProyectoDAO();
        //SE GUARDA EN UN VECTOR TODOS LOS TIPOS DE PROYECTO DESDE LA BD
        ArrayList<TipoProyecto> listaTipoProyecto = daoTipoProyecto.getTipoProyecto();
        //SE VACIA EL CLOMBO TIPO PROYECTO
        vista.cboTipoProyecto.removeAllItems();
        //SE RECORRE EL VECTOR DE LOS TIPOS DE PROYECTOS QUE SE TRAJO DESDE LA BD
        for (int i = 0; i < listaTipoProyecto.size(); i++) {
            //CADA TIPO DE PROYECTO DE LA LISTA SE AGREGA AL COMBO
            vista.cboTipoProyecto.addItem(new TipoProyecto (listaTipoProyecto.get(i).getIdTipoProyecto(),listaTipoProyecto.get(i).getNombre()));
        }
    }
}
