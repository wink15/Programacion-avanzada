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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
import modelo.Perfil;
import modelo.PerfilDAO;
import modelo.Persona;
import modelo.PersonaDAO;
import modelo.TipoProyecto;
import modelo.TipoProyectoDAO;

import vista.VistaClientes;

public class Controlador implements ActionListener {

    //VECTOR QUE CONTIENE LOS BOTONES PARA LAS CONSULTAS AL USUARIO
    String[] botones = {"Aceptar", "Cancelar"};

    //Constructor sin parametros 
    public Controlador() {
    }

    //CREAMOS INSTANCIAS DE OTRAS CLASES
    ProyectoDAO dao = new ProyectoDAO();
    Proyecto p = new Proyecto();
    VistaProyecto vista = new VistaProyecto();
    DefaultTableModel modelo = new DefaultTableModel();
    private String opcion;

    //CREAMOS UN CONTROLADOR POR CADA VISTA EXISTENTE EN DONDE SE LES PASA EL ACTION PARA PODER UTILIZARLOS
    public Controlador(VistaProyecto v) {
        this.vista = v;
        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnAgregar.addActionListener(this);
        this.vista.btnModificar.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);
        this.vista.btnActualizar.addActionListener(this);
        this.vista.btnEliminar.setEnabled(false);
        this.vista.btnModificar.setEnabled(false);
        this.vista.comboBusqueda.addActionListener(this);
        this.vista.comboFecha.addActionListener(this);
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
            this.vista.btnEliminar.setEnabled(true);
            this.vista.btnModificar.setEnabled(true);
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
            if (vista.txtNombre.getText().equals("") || vista.txtFechaInicio.getDate() == null || vista.txtFechaConfirmacion.getDate() == null || vista.txtFechaFin.getDate() == null || vista.txtObservacion.getText().equals("") || vista.txtMonto.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Debe completar todos los campos");
            } else if (this.esNumerico(vista.txtMonto.getText()) == false) {
                JOptionPane.showMessageDialog(null, "El monto no debe contener letras");
            } else {
                //SE VALIDAN LAS FECHA DEL PROYECTO
                LocalDate fechaActualLD = LocalDate.now();
                java.util.Date fechaActual = Date.valueOf(fechaActualLD);

                if (vista.txtFechaConfirmacion.getDate().equals(fechaActual) || vista.txtFechaConfirmacion.getDate().after(fechaActual)) {
                    if (vista.txtFechaInicio.getDate().after(vista.txtFechaConfirmacion.getDate())) {
                        if (vista.txtFechaFin.getDate().after(vista.txtFechaInicio.getDate())) {
                            int variable = JOptionPane.showOptionDialog(null, "¿Deseas agregar un proyecto?", "Agregacion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]);
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
                        } else {
                            JOptionPane.showMessageDialog(null, "La fecha de fin debe ser mayor a la fecha de inicio.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "La fecha de inicio debe ser mayor a la fecha de confirmacion.");
                    }
                } else {
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
                vista.txtFechaConfirmacion.setDate(fechaInicio);
                vista.txtFechaInicio.setDate(fechaConfirmacion);
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
        if (e.getSource() == vista.comboBusqueda) {
            this.opcion = vista.comboBusqueda.getSelectedItem().toString();
            if (opcion.equals("FECHA CONFIRMACION") || opcion.equals("FECHA INICIO") || opcion.equals("FECHA FIN")) {
                vista.fechaBusqueda.setVisible(true);
                vista.txtParametro.setVisible(false);
                vista.comboFecha.setVisible(true);

            } else {
                vista.fechaBusqueda.setVisible(false);
                vista.txtParametro.setVisible(true);
                vista.comboFecha.setVisible(false);
                vista.fechaHasta.setVisible(false);
            }
        }
        if (e.getSource() == vista.comboFecha) {
            String seleccionado = vista.comboFecha.getSelectedItem().toString();
            if (seleccionado.equals("ENTRE FECHAS")) {
                vista.fechaHasta.setVisible(true);
            } else {
                vista.fechaHasta.setVisible(false);
            }
        }
    }

    //METODO ACTUALIZAR 
    public void actualizar() {
        int ubi = 0;
        //VALIDACION DE CAMPOS VACIOS NECESARIOS PARA ACTUALIZAR EL REGISTRO EN LA BD.  
        if (vista.txtId.getText().equals("")) {
            JOptionPane.showMessageDialog(vista, "Primero debe modificar un proyecto");
        } else {
            //EN CASO DE QUE TODOS LOS CAMPOS ESTAN COMPLETOS, SE LE CONSULTA AL CLIENTE SI ESTA SEGURO DE MODIFICAR EL PROYECTO
            int variable = JOptionPane.showOptionDialog(null, "¿Deseas modificar un proyecto?", "Proyecto", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]);
            //SI LA RESPUESTA ES QUE SI DESEA MODIFICARLO
            if (variable == 0) {
                //SE PASAN LOS DATOS DESDE LOS CAMPOS DE LA PANTALLA A VARIABLES
                //COMENTAR!!!!!!!
                String idAux = vista.txtId.getText();
                int id = Integer.valueOf(idAux);
                String nombre = vista.txtNombre.getText();
                java.util.Date date = vista.txtFechaConfirmacion.getDate();
                long aux1 = date.getTime();
                java.sql.Date fechaInicio = new java.sql.Date(aux1);
                java.util.Date date2 = vista.txtFechaInicio.getDate();
                long aux2 = date2.getTime();
                java.sql.Date fechaConfirmacion = new java.sql.Date(aux2);
                java.util.Date date3 = vista.txtFechaFin.getDate();
                long aux3 = date3.getTime();
                java.sql.Date fechaFin = new java.sql.Date(aux3);
                int tipoProyecto = vista.cboTipoProyecto.getSelectedIndex();
                Integer cliente = vista.cboCliente.getSelectedIndex();
                String observacion = vista.txtObservacion.getText();
                String ubicacion = vista.comboProyecto.getSelectedItem().toString();
                double monto = Integer.parseInt(vista.txtMonto.getText());
                if (ubicacion.equals("Nacional")) {
                    ubi = 1;
                } else if (ubicacion.equals("Internacional")) {
                    ubi = 2;
                }

                //SETEAMOS CADA VALOR EN LA INSTANCIA CREADA DE PROYECTO
                p.setIdProyecto(id);
                p.setNombre(nombre);
                p.setFechaInicio(fechaInicio);
                p.setFechaConfirmacion(fechaConfirmacion);
                p.setFechaFin(fechaFin);
                p.setTipoProyecto(tipoProyecto);
                p.setCliente(cliente);
                p.setObservacion(observacion);
                p.setMonto(monto);
                p.setUbicacion(ubi);

                //GUARDAMOS EN r EL RESULTADO DE LA ACTUALIZACION (1 COMPLETADA, 0 FALLIDA)
                int r = dao.actualizar(p);
                if (r == 1) {
                    JOptionPane.showMessageDialog(vista, "Proyecto actualizado con exito");
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al actualizar");
                }
            } else {
                //EN CASO DE NO DESEAR ACTUALIZAR EL PROYECTO SE VACIAN TODOS LOS CAMPOS DE LA PANTALLA.
                nuevo();
            }
        }
        //POR ULTIMO SE LIMPIA LA TABLA EN LA QUE SE MUESTRAN LOS REGISTROS DE PROYECTO.
        limpiarTabla();
    }

    //METODO AGREGAR 
    public void agregar() {
        int ubi = 0;
        try {
            //SE PASAN LOS DATOS DESDE LOS CAMPOS DE LA PANTALLA A VARIABLES
            String nombre = vista.txtNombre.getText();
            //SE OBTIENE LO QUE la fecha Y SE LA PASA A DATE DE JAVA UTIL
            java.util.Date date = vista.txtFechaConfirmacion.getDate();
            //ESTE METODO LO QUE HACE ES PASAR A FECHA EN MES NUMERO  FECHA Y HORA
            long aux1 = date.getTime();
            // LUEGO SE TRANSFORMA A DATE DE SQL PARA PODER ALMACENARLA EN LA BD EL PROCESO SE REPITE PARA LAS 3 FECHAS 
            java.sql.Date fechaInicio = new java.sql.Date(aux1);

            java.util.Date date2 = vista.txtFechaInicio.getDate();
            long aux2 = date2.getTime();
            java.sql.Date fechaConfirmacion = new java.sql.Date(aux2);

            java.util.Date date3 = vista.txtFechaFin.getDate();
            long aux3 = date3.getTime();
            java.sql.Date fechaFin = new java.sql.Date(aux3);

            Integer tipoProyecto = (vista.cboTipoProyecto.getItemAt(vista.cboTipoProyecto.getSelectedIndex()).getIdTipoProyecto());
            Integer cliente = (vista.cboCliente.getItemAt(vista.cboCliente.getSelectedIndex()).getIdCliente());
            String observacion = vista.txtObservacion.getText();
            String ubicacion = vista.comboProyecto.getSelectedItem().toString();
            double monto = Integer.parseInt(vista.txtMonto.getText());
            if (ubicacion.equals("Nacional")) {
                ubi = 1;
            } else if (ubicacion.equals("Internacional")) {
                ubi = 2;
            }
            //SETEAMOS CADA VALOR EN LA INSTANCIA CREADA DE PROYECTO
            p.setNombre(nombre);
            p.setFechaInicio(fechaInicio);
            p.setFechaConfirmacion(fechaConfirmacion);
            p.setFechaFin(fechaFin);
            p.setTipoProyecto(tipoProyecto);
            p.setCliente(cliente);
            p.setObservacion(observacion);
            p.setMonto(monto);
            p.setUbicacion(ubi);

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
            int variable = JOptionPane.showOptionDialog(null, "¿Deseas eliminar un proyecto?", "Eliminacion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]);
            if (variable == 0) {
                //SE GUARDA EL ID DEL PROYECTO SELECCIONADO PARA PASARLO COMO PARAMETRO Y SER USADO EN LA CONSULTA A LA BD
                int id = Integer.parseInt((String) vista.tabla.getValueAt(fila, 0).toString());
                if(dao.consultaEliminacionPerfilProyecto(id) == 0 && dao.consultaEliminacionPersonalProyecto(id) == 0){
                    dao.eliminar(id);
                    JOptionPane.showMessageDialog(vista, "Proyecto eliminado con exito");
                }else if(dao.consultaEliminacionPerfilProyecto(id) > 0){
                    JOptionPane.showMessageDialog(vista, "El Proyecto no se puede eliminar debido a que se le ha asignado uno o más perfiles");
                    JOptionPane.showMessageDialog(vista, "Desasigne los perfiles de dicho proyecto para poder eliminarlo");
                }else if(dao.consultaEliminacionPersonalProyecto(id) > 0){
                    JOptionPane.showMessageDialog(vista, "El Proyecto no se puede eliminar debido a que se le ha asignado uno o más personales");
                    JOptionPane.showMessageDialog(vista, "Desasigne los personales de dicho proyecto para poder eliminarlo");
                }
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
        vista.txtMonto.setText("");
        vista.txtFechaConfirmacion.setDate(null);
        vista.txtFechaInicio.setDate(null);
        vista.txtFechaFin.setDate(null);
        vista.txtObservacion.setText("");
        vista.txtNombre.requestFocus();
    }

    //METODO BUSCAR LOS PROYECTOS EXISTENTES EN LA BD
    public void buscar(JTable tabla) {

        //SE CENTRAN LAS CELDAS DE LA TABLA
        ArrayList<Proyecto> lista = new ArrayList<>();
        centrarCeldas(tabla);
        this.opcion = vista.comboBusqueda.getSelectedItem().toString();
        String selec = vista.comboFecha.getSelectedItem().toString();
        //AL MODELO CREADO SE LE PASA EL MODELA DE LA TABLA
        modelo = (DefaultTableModel) tabla.getModel();
        tabla.setModel(modelo);
        //SE COMPARA CON LAS OPCIONES QUE HAY EN EL COMBOX
        if (opcion.equals("FECHA INICIO")) {
            //SE TRAE LA FECHA QUE SE SELECCIONO EN EL JCALENDAR

            java.util.Date parametro = vista.fechaBusqueda.getDate();
            if (parametro != null) {
                //SE TRANSFORMA A LONG PARA LUEGO PODER PASARLA A DATE
                long aux1 = parametro.getTime();
                java.sql.Date fecha = new java.sql.Date(aux1);
                System.out.println(parametro);

                if (selec == "POR FECHA") {

                    //SI SE SELECCIONA POR FECHA UNICA ENTONCES SE PASA UNA FECHA Y EL NUMERO 1 QUE ES LA OPCION DE BUSQUEDA POR FECHA DE INICIO
                    if (parametro != null) {
                        lista = dao.filtroBusquedaFechas(fecha, null, 1, 0);
                    }
                } else {
                    //SI NO SE HACE  LO MISMO PARA LA FECHA HASTA DONDE SE QUIERA BUSCAR Y LUEGO SE LE PASA OTRA OPCION 1 QUE INDICA QUE ES ENTRE FECHAS
                    java.util.Date hasta = vista.fechaHasta.getDate();
                    if (hasta != null) {
                        long aux2 = hasta.getTime();
                        java.sql.Date fechaHasta = new java.sql.Date(aux2);
                        lista = dao.filtroBusquedaFechas(fecha, fechaHasta, 1, 1);
                    } else {
                        JOptionPane.showMessageDialog(null, "Debe ingresar una fecha ", "Error", JOptionPane.WARNING_MESSAGE);
                    }
                }

            } else {
                JOptionPane.showMessageDialog(null, "Debe ingresar una fecha ", "Error", JOptionPane.WARNING_MESSAGE);
            }
        } else if (opcion.equals("FECHA CONFIRMACION")) {
            java.util.Date parametro = vista.fechaBusqueda.getDate();
            if (parametro != null) {
                long aux1 = parametro.getTime();
                java.sql.Date fecha = new java.sql.Date(aux1);
                if (selec == "POR FECHA") {
                    //SI SE SELECCIONA POR FECHA UNICA ENTONCES SE PASA UNA FECHA Y EL NUMERO 2 QUE ES LA OPCION DE BUSQUEDA POR FECHA DE CONFIRMACION
                    lista = dao.filtroBusquedaFechas(fecha, null, 2, 0);
                } else {
                    //SI NO SE HACE  LO MISMO PARA LA FECHA HASTA DONDE SE QUIERA BUSCAR Y LUEGO SE LE PASA OTRA OPCION 1 QUE INDICA QUE ES ENTRE FECHAS
                    java.util.Date hasta = vista.fechaHasta.getDate();
                    if (hasta != null) {
                        long aux2 = hasta.getTime();
                        java.sql.Date fechaHasta = new java.sql.Date(aux2);
                        lista = dao.filtroBusquedaFechas(fecha, fechaHasta, 2, 1);
                    } else {
                        JOptionPane.showMessageDialog(null, "Debe ingresar una fecha ", "Error", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Debe ingresar una fecha ", "Error", JOptionPane.WARNING_MESSAGE);
            }
        } else if (opcion.equals("FECHA FIN")) {
            java.util.Date parametro = vista.fechaBusqueda.getDate();
            if (parametro != null) {
                long aux1 = parametro.getTime();

                java.sql.Date fecha = new java.sql.Date(aux1);
                if (selec == "POR FECHA") {
                    //SI SE SELECCIONA POR FECHA UNICA ENTONCES SE PASA UNA FECHA Y EL NUMERO 2 QUE ES LA OPCION DE BUSQUEDA POR FECHA FIN
                    lista = dao.filtroBusquedaFechas(fecha, null, 3, 0);
                } else {
                    //SI NO SE HACE  LO MISMO PARA LA FECHA HASTA DONDE SE QUIERA BUSCAR Y LUEGO SE LE PASA OTRA OPCION 1 QUE INDICA QUE ES ENTRE FECHAS
                    java.util.Date hasta = vista.fechaHasta.getDate();
                    if (hasta != null) {
                        long aux2 = hasta.getTime();
                        java.sql.Date fechaHasta = new java.sql.Date(aux2);
                        lista = dao.filtroBusquedaFechas(fecha, fechaHasta, 3, 1);
                    } else {
                        JOptionPane.showMessageDialog(null, "Debe ingresar una fecha ", "Error", JOptionPane.WARNING_MESSAGE);
                    }
                }
                //EN CASO QUE LA BUSQUEDA NO ESTE RELACIONADA CON LAS FECHAS ENTONCES SE USA OTRO METODO DEL DAO QUE TIENE EL PARAMETRO Y UNA OPCION SEGUN LO QUE SE ELIJA
            } else {
                JOptionPane.showMessageDialog(null, "Debe ingresar una fecha ", "Error", JOptionPane.WARNING_MESSAGE);
            }
        } else if (opcion.equals("ID")) {
            
            String parametr = vista.txtParametro.getText();
            if(parametr.isEmpty()==false){
            lista = dao.filtroBusqueda(parametr, 2);} else{ JOptionPane.showMessageDialog(null, "Debe ingresar un valor", "Error", JOptionPane.WARNING_MESSAGE);}

        } else if (opcion.equals("NOMBRE")) {
            String parametr = vista.txtParametro.getText();
            if(parametr.isEmpty()==false){
            lista = dao.filtroBusqueda(parametr, 3);}else{ JOptionPane.showMessageDialog(null, "Debe ingresar un valor", "Error", JOptionPane.WARNING_MESSAGE);}
        } else {
            lista = dao.filtroBusqueda(null, 1);
        }

        //SE CREA UN VECTOR DE 8 OBJETOS Y EN CADA UNO SE GUARDAN LOS DATOS QUE COMPONEN A CADA PROYECTO
        Object[] objeto = new Object[10];
        // System.out.println(lista.size());
        for (int i = 0; i < lista.size(); i++) {

            objeto[0] = lista.get(i).getIdProyecto();
            objeto[1] = lista.get(i).getNombre();
            objeto[2] = lista.get(i).getFechaInicio();
            objeto[3] = lista.get(i).getFechaConfirmacion();
            objeto[4] = lista.get(i).getFechaFin();
            objeto[5] = lista.get(i).getTipoProyecto();
            objeto[6] = lista.get(i).getCliente();
            objeto[7] = lista.get(i).getObservacion();
            objeto[8] = lista.get(i).getMonto();

            objeto[9] = lista.get(i).getUbicacion();
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

    public boolean esNumerico(String num) {
        if (num.matches("[+-]?\\d*(\\.\\d+)?")) {
            return true;
        }

        return false;

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
            vista.cboCliente.addItem(new Clientes(listaClientes.get(i).getIdCliente(), listaClientes.get(i).getRazonSocial()));
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
            vista.cboTipoProyecto.addItem(new TipoProyecto(listaTipoProyecto.get(i).getIdTipoProyecto(), listaTipoProyecto.get(i).getNombre()));
        }

    }
}
