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
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import modelo.PerfilDAO;
import vista.VistaPersonal;
import vista.Perfil_Personal;
import modelo.Perfil;
import modelo.PerfilPersonal;
import modelo.PerfilPersonalDAO;
import controlador.ControlardorPerfil_Personal;
import javax.swing.JList;
import javax.swing.ListModel;

/**
 *
 * @author leone
 */
public class ControlardorPerfil_Personal implements ActionListener {

    //Constructor sin parametros 
    public ControlardorPerfil_Personal() {
    }

    //SE DEFINEN LAS VARIABLES Y VECTORES 
    String[] botones = {"Aceptar", "Cancelar"};
    int operacion;
    String elementoABorrar;
    int indiceABorrar;
    private ArrayList<String> detallePerfil = new ArrayList<String>();
    private ArrayList<String> perfiles = new ArrayList<String>();
    private ArrayList<String> detalles = new ArrayList<String>();
    private ArrayList<String> detallesPerfil = new ArrayList<String>();
    private ArrayList<Integer> resultado = new ArrayList<Integer>();
    private ArrayList<Integer> resultadoEliminacion = new ArrayList<Integer>();
    private ArrayList<Perfil> listaPerfilPersonal = new ArrayList<Perfil>();

    int idPersonal = 0;
    int idPerfil = 0;

    //CREAMOS UN MODELO PARA LUEGO MOSTRAR EL DETALLE EN PANTALLA
    DefaultListModel modelo1 = new DefaultListModel();

    //CREAMOS INSTANCIAS DE OTRAS CLASES
    PerfilDAO daoPerfil = new PerfilDAO();
    Perfil_Personal vista3 = new Perfil_Personal();
    DefaultTableModel modelo = new DefaultTableModel();
    VistaPersonal vistaPersonal = new VistaPersonal();
    PerfilPersonal perfilPersonal = new PerfilPersonal();
    PerfilPersonalDAO daoPerfilPersonal = new PerfilPersonalDAO();

    //CREAMOS UN CONTROLADOR POR CADA VISTA EXISTENTE
    public ControlardorPerfil_Personal(Perfil_Personal v) {
        this.vista3 = v;
        this.vista3.btnAgregar.addActionListener(this);
        this.vista3.btnCancelar.addActionListener(this);
        this.vista3.btnConfirmar.addActionListener(this);
        this.vista3.btnEliminar.addActionListener(this);
    }

    //METODO QUE CARGA EL COMBO PERSONA EN LA VISTA CLIENTE
    public void llenarPerfiles() throws SQLException {
        //SE CREA UNA INSTANCIA DE LA CLASE PersonaDAO
        //SE CREA UN ARRAY QUE ALMACENA LAS PERSONAS TRAIDAS DESDE LA BD
        ArrayList<Perfil> listaPerfiles = daoPerfil.listar();
        //SE VACIA EL COMBO PERSONA
        vista3.cboPerfiles.removeAllItems();
        //SE RECORRE LA LISTA TRAIDA DESDE LA BD Y SE AGREGA CADA REGISTRO AL COMBO
        for (int i = 0; i < listaPerfiles.size(); i++) {
            vista3.cboPerfiles.addItem(new Perfil(listaPerfiles.get(i).getId(), listaPerfiles.get(i).getNombre()));
        }
    }

    //FUNCIONAMIENTO DE CADA UNO DE LOS BOTONES DE LA PANTALLA 
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista3.btnAgregar) {
            int resp = 0;
            //SE VERIFICA QUE EL LISTADO DE PERFILES NO ESTE VACIO
            if (detallePerfil == null) {
                resp = resp;
            } else {
                //EN CASO DE NO ESTAR VACIO SE VERIFICA SI CONTIENE TODOS LOS PERFILES DISPONIBLES
                for (int i = 0; i < detallePerfil.size(); i++) {
                    if (detallePerfil.get(i).equals(String.valueOf(vista3.cboPerfiles.getSelectedItem()))) {
                        resp = resp + 1;
                        break;
                    } else {
                        resp = resp;
                    }
                }
            }
            //EN CASO DE CONTENER TODOS LOS PERFILES ASIGNADOS, SE LE INFORMA AL USUARIO
            if (resp > 0) {
                JOptionPane.showMessageDialog(null, "El personal ya tiene asignado este perfil");
            } else {
                //DE LO CONTRARIO 
                //SE VACIAN LOS SIGUIENTES ARRAYS 
                perfiles.clear();
                detallesPerfil.clear();
                //SE HABILITAN LOS BOTONES DE CONFIRMACION DE LA ASIGNACION DE PERFILES Y EL DE ELIMINAR UN PERFIL ASIGNADO
                vista3.btnConfirmar.setEnabled(true);
                vista3.btnEliminar.setEnabled(true);
                //SE CREA EL DETALLE A MOSTRAR
                String det = String.valueOf(vista3.cboPerfiles.getSelectedItem());
                //SE ALMACENAN LOS DETALLE EN UN VECTOR PARA LUEGO GUARDARLOS EN LA BD, SOLO CONTIENE LOS A ASIGNAR
                detalles.add(det);
                //SE ALMACENAN EN OTRO PARA LLEVAR LA CUENTA DE LA CANTIDAD DE PERFILES ASIGNADOS, CONTIENE TODOS
                detallePerfil.add(det);
                //SE ALMACENAN EN UN ULTIMO VECTOR PARA MOSTRARLOS EN LA LISTA, CONTIENE LOS A MOSTRAR
                perfiles.add(det);
                //SE DEFINE UNA VARIABLE PARA ALMACENAR CADA DETALLE NUEVO
                String listadoDetalle;
                //RECORREMOS CADA DETALLE CREADO Y SE ASIGNAN AL MODELO 
                for (int j = 0; j < perfiles.size(); j++) {
                    detallesPerfil.add(perfiles.get(j));
                    listadoDetalle = "";
                    listadoDetalle += detallesPerfil.get(j);
                    modelo1.addElement(listadoDetalle);
                }
                //SE SETEA EL MODELO A LA LISTA
                vista3.detallePerfil.setModel(modelo1);
                //SE SETEA EL TIPO DE OPERACION COMO 1 (AGREGACION)
                operacion = 1;
            }
        }
        if (e.getSource() == vista3.btnEliminar) {
            //SE VERIFICA SI SE SELECCIONO UN ELEMENTO DE LA LISTA
            if (vista3.detallePerfil.getSelectedIndex() < 0) {
                //EN CASO DE QUE NO, SE INFORMA
                JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la lista");
            } else {
                //DE LO CONTRARIO 
                //SE DEFINE EL INDICE A BORRAR
                indiceABorrar = vista3.detallePerfil.getSelectedIndex();
                //SE DEFINE EL ELEMENTO A BORRAR
                elementoABorrar = vista3.detallePerfil.getSelectedValue();
                //SE QUITA DEL MODELO EL ELEMENTO CORRESPONDIENTE AL INDICE SELECCIONADO
                modelo1.remove(indiceABorrar);
                //DEL TOTAL DE PERFILES SE QUITA EL CORRESPONDIENTE AL INDICE SELECCIONADO
                detallePerfil.remove(indiceABorrar);
                //SE HABILITA LA CONFIRMACION DE LA ASIGNACION DE PERFILES
                vista3.btnConfirmar.setEnabled(true);
                //SE VACIAN LOS DETALLES A AGREGAR
                detalles.clear();
                //SE SETEA EL TIPO DE OPERACION COMO 0 (ELIMINACION)
                operacion = 0;
            }
        }
        if (e.getSource() == vista3.btnCancelar) {
            //SE VACIA EL TOTAL DE LOS DETALLES CORRESPONDIENTES AL PERSONAL 
            detallePerfil.clear();
            //SE VACIAN LOS PERFILES A MOSTRAR
            perfiles.clear();
            //SE CIERRA LA PANTALLA
            vista3.dispose();
        }
        if (e.getSource() == vista3.btnConfirmar) {
            //SE VERIFICA SI LA CONFIRMACION ES DE UNA AGREGACION O ELIMINACION
            if (operacion == 1) {
                //EN CASO DE UNA AGREGACION
                //SE CONSULTA AL USUARIO SI REALMENTE QUIERE AGREGAR UN PERFIL
                int variable = JOptionPane.showOptionDialog(null, "¿Deseas asignar los perfiles?", "Agregacion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]);
                if (variable == 0) {
                    //SE AGREGAN LOS PERFILES
                    agregarPerfil();
                    //SE DESABILITAN LOS BOTONES                    
                    desabilitarBotones();
                }
            } else {
                //EN CASO DE UNA ELIMINACION
                //SE CONSULTA AL USUARIO SI REALMENTE QUIERE ELIMINAR UN PERFIL
                int variable = JOptionPane.showOptionDialog(null, "¿Deseas eliminar el perfil?", "Eliminación", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]);
                if (variable == 0) {
                    //SE QUITA EL PERFIL
                    quitarPerfil();
                    //SE DESABILITAN LOS BOTONES
                    desabilitarBotones();
                }
            }
        }
    }

    //METODO PARA AGREGAR UN NUEVO PERFIL
    public void agregarPerfil() {
        //SE PASA EL VALOR DEL CAMPO ID DEL PERSONAL A UNA VARIABLE
        this.idPersonal = Integer.valueOf(vista3.txtIdPerfil_Personal.getText());
        //SE RECORRE EL CONJUNTO DE DETALLES A AGREGAR 
        for (int i = 0; i < detalles.size(); i++) {
            //CADA DETALLE SE GUARDA EN UNA VARIABLE
            String recorte = detalles.get(i);
            //SE SEPARA CADA DETALLE 
            String[] ids = recorte.split("-");
            //SE GUARDA EL ID DEL PERFIL A AGREGAR
            idPerfil = Integer.parseInt(ids[0]);
            //SE SETEA EL ID DEL PERSONAL Y DEL PERFIL EN EL OBJETO PERFILPERSONAL
            perfilPersonal.setIdPersonal(idPersonal);
            perfilPersonal.setPerfil(idPerfil);
            //SE TRAE EL RESULTADO DE LA AGREGACION A LA BD
            resultado.add(daoPerfilPersonal.agregar(perfilPersonal));
        }
        //SE VERIFICA SI TODOS LOS PERFILES FUERON ASIGNADOS CORRECTAMENTE, INFORMANDO EN CADA CASO
        int cantResultados = 0;
        for (int i = 0; i < resultado.size(); i++) {
            if (resultado.get(i) == 1) {
                cantResultados = cantResultados + 1;
                if (cantResultados == resultado.size()) {
                    JOptionPane.showMessageDialog(vista3, "Todos los perfiles fueron asignados con exito");
                }
            } else {
                JOptionPane.showMessageDialog(vista3, "Error al asignar un perfil");
            }
        }
    }

    //METODO PARA QUITAR UN PERFIL DE LA BD
    public void quitarPerfil() {
        //SE OBTIENE EL ID DEL PERSONAL A PARTIR DEL CAMPO DE LA PANTALLA
        this.idPersonal = Integer.valueOf(vista3.txtIdPerfil_Personal.getText());
        //SE ALMACENA EL PERFIL A BORRAR
        String idPerfil = elementoABorrar;
        //SE SEPARA EL PERFIL EN PARTES
        String[] partes = idPerfil.split("-");
        //SE GUARDA SOLO EL ID DEL PERFIL
        int id = Integer.parseInt(partes[0]);
        //SE TRAE EL RESULTADO DE LA ELIMINACION A LA BD
        resultadoEliminacion.add(daoPerfilPersonal.eliminar(idPersonal, id));
        //SE VERIFICA SI SE ELIMINO CORRECTAMENTE EL PERFIL DE LA BD
        for (int i = 0; i < resultado.size(); i++) {
            if (resultadoEliminacion.get(i) == 1) {
                JOptionPane.showMessageDialog(vista3, "El perfil fue quitado");
            } else {
                JOptionPane.showMessageDialog(vista3, "No se puedo quitar el perfil");
            }
        }
    }

    //METODO PARA CARGAR LOS PERFILES DESDE LA BD
    public void cargarPerfiles() {
        //SE OBTIENE EL ID DEL PERSONAL DESDE LA PANTALLA
        int id = Integer.valueOf(vista3.txtIdPerfil_Personal.getText());
        //SE BUSCAN LOS PERFILES PARA EL PERSONAL CORRESPONDIENTE
        buscarPerfil(id);
    }

    public void buscarPerfil(int idPersonal) {
        //LIMPIAMOS EL ARRAYS DEL CONJUNTO DE PERFILES
        detallePerfil.clear();
        //TRAEMOS LOS PERFILES DESDE LA BD 
        listaPerfilPersonal = daoPerfilPersonal.listar(idPersonal);
        //RECORREMOS LOS PERFILES Y LOS GUARDAMOS EN EL ARRAY
        for (int i = 0; i < listaPerfilPersonal.size(); i++) {
            detallePerfil.add(listaPerfilPersonal.get(i).getId() + "-" + listaPerfilPersonal.get(i).getNombre());
        }
        //SE CREA UNA VARIABLE PARA ALMACENAR CADA DETALLE A MOSTRAR
        String listadoDetalle;
        //RECORREMOS TODOS LOS DETALLES OBTENIDOS Y LOS ASIGNAMOS AL MODELO
        for (int i = 0; i < detallePerfil.size(); i++) {
            listadoDetalle = "";
            listadoDetalle += detallePerfil.get(i);
            modelo1.addElement(listadoDetalle);
        }
        vista3.detallePerfil.setModel(modelo1);
    }

    //METODO PARA DESABILITAR LOS BOTONES 
    public void desabilitarBotones() {
        //SE DESABILITA LA CONFIRMACION
        vista3.btnConfirmar.setEnabled(false);
        //SE VERIFICA SI EL PERSONAL TIENE ASIGNADO TODOS LOS PERFILES DISPONIBLES
        if (vista3.cboPerfiles.getItemCount() == detallePerfil.size()) {
            //EN CASO DE QUE SI, SE DESABILITA LA AGREGACION
            vista3.btnAgregar.setEnabled(false);
        } else {
            //DE LO CONTRARIO SE HABILITA LA AGREGACION
            vista3.btnAgregar.setEnabled(true);
        }
    }
}
