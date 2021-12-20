/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.sun.awt.AWTUtilities;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JWindow;
import javax.swing.table.DefaultTableModel;
import modelo.APIDAO;
import modelo.Api;
import modelo.Persona;
import vista.VistaApi;

/**
 *
 * @author Santiago
 */
public class ControladorApi implements ActionListener {

    //CREAMOS INSTANCIAS DE OTRAS CLASES
    VistaApi vista = new VistaApi();
    DefaultTableModel modelo = new DefaultTableModel();
    Api testApi = new Api();
    APIDAO dao = new APIDAO();
    //SE UTILIZA UNA BANDERA PARA LUEGO SABER QUE OPCION ES LA QUE SE SELECCIONO
    private int bandera = 0;

    //CREAMOS UN CONTROLADOR POR CADA VISTA EXISTENTE
    public ControladorApi(VistaApi v) {
        this.vista = v;
        this.vista.ComboxApi.addActionListener(this);
        this.vista.btnBuscar.addActionListener(this);

    }

    //EJECUCION DE CADA BOTON DENTRO DE CADA PANTALLA
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.ComboxApi) {
//LLAMA AL METODO QUE SE ENCARGA REALIZAR LA ACCION DEPENDIENDO DE LO SELECCIONADO
            Intento();
        }
        if (e.getSource() == vista.btnBuscar) {

            try {

                //SE BUSCAN LOS VALORES DE LA API
                buscarApi(vista.tablaApi);

            } catch (MalformedURLException ex) {
                Logger.getLogger(ControladorApi.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void Intento() {
        //String opcion = String.valueOf(vista.ComboxApi.getSelectedItem());

        //SE OBTIENE LO SELECCIONADO POR EL USUARIO
        String opcion = vista.ComboxApi.getSelectedItem().toString();
        //SE TIENE ENCUENTA LAS 2 OPCIONES
        String opcionE = "Busqueda por rango";
        String opcionE2 = "Busqueda unica";
        //SI ES LA OPCION POR RANGO ENTRA EN ESTE IF Y SETEA LO REQUERIDO PARA LA BUSQUEDA 
        if (opcion.equals(opcionE)) {
            this.bandera = 1;
            vista.desdeApi.setVisible(true);
            vista.hastaApi.setVisible(true);
            vista.labelApi.setVisible(true);
            vista.labelUnica.setVisible(false);
            vista.unica.setVisible(false);

            // JLabel.setText("Estoy modificando el texto");
            //SI NO  ES LA OPCION POR RANGO ENTRA EN ESTE IF Y SETEA LO REQUERIDO PARA LA BUSQUEDA 
        } else if (opcionE2.equals(opcion)) {
            this.bandera = 2;
            //  vista.desdeApi.setBounds(new Rectangle(25, 15, 250, 21));
            vista.labelUnica.setVisible(true);
            vista.unica.setVisible(true);
            vista.desdeApi.setVisible(false);
            vista.hastaApi.setVisible(false);
            vista.labelApi.setVisible(false);
        }
    }

    public void buscarApi(JTable tabla) throws MalformedURLException {
        ArrayList<Api> listas = new ArrayList<>();
        // System.out.println("a " + this.esNumerico(vista.desdeApi.getText()));

        if (this.bandera == 1) {
            //SI LO QUE SE OBTUVO DE LO INGRESADO POR EL USUARIO ES UN NUMERO ENTONCES ENTRA AL IF
            if (this.esNumerico(vista.desdeApi.getText()) == true && this.esNumerico(vista.hastaApi.getText()) == true) {
                //SE GUARDAN LOS PARAMETROS INGRESADOS
                int desde = Integer.parseInt(vista.desdeApi.getText());
                int hasta = Integer.parseInt(vista.hastaApi.getText());
                System.out.println("entro");
                // SE LLAMA AL METODO ESTAENRANGO() QUE DEVUELVE TRUE EN CASO DE QUE ESTE ENTRE 1-100 EL VALOR INGRESADO
                if (this.estaEnRango(desde) == true) {
                    if (this.estaEnRango(hasta) == true) {
                        // SI LOS 2 ESTAN EN DENTRO DEL RANGO ENTONCES SE LLAMA AL METODO GETAPI  EN DONDE SE LE PASA LA OPCION SELECCIONADA Y LOS VALORES
                        listas = dao.GetApi(2, desde, hasta);
                    } else {
                        JOptionPane.showMessageDialog(null, "El numero ingresado en el parametro hasta esta fuera del rango", "Error", JOptionPane.WARNING_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "El numero ingresado en el parametro desde esta fuera del rango", "Error", JOptionPane.WARNING_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(null, "El numero ingresado no es un numero", "Error", JOptionPane.WARNING_MESSAGE);
            }
        } else if (this.bandera == 2) {
            if (this.esNumerico(vista.unica.getText()) == true) {
                int id = Integer.parseInt(vista.unica.getText());
                if (this.estaEnRango(id) == true) {
                    // EN CASO DE CUMPLIRSE LAS CONDICIONES SE LLAMA AL GETAPI Y EN ESTE CASO SE LE PASA LA OPCION 1 Y EL RANGO HASAT 0 PORQUE ES BUSQUEDA UNICA
                    listas = dao.GetApi(1, id, 0);
                } else {
                    JOptionPane.showMessageDialog(null, "El numero ingresado  esta fuera del rango", "Error", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "El numero ingresado no es un numero", "Error", JOptionPane.WARNING_MESSAGE);
            }

        }

        // centrarCeldas(tabla);
        //EN EL MODELO CREADO SE GUARDA EL MODELO DE LA TABLA API
        modelo = (DefaultTableModel) tabla.getModel();
        tabla.setModel(modelo);
        //SE CREA UN ARRAY CON LOS TIPOS DE PROYECTOS QUE SE TRAEN DESDE LA BD

        //SE CREA UN ARRAY OBJETO QUE ALMACENA LOS DATOS DE CADA API
        Object[] objeto = new Object[4];
        //SE UTILIZA LA LISTA DECLARADA ARRIBA QUE DEPENDIENDO DE LA OPCION VA A CARGAR DETERMINADOS VALORES
        for (int i = 0; i < listas.size(); i++) {

            objeto[0] = listas.get(i).getUserId();
            objeto[1] = listas.get(i).getId();
            objeto[2] = listas.get(i).getTitle();
            objeto[3] = listas.get(i).getBody();

            //SE AGREGAN LOS TIPOS DE PROYECTO EN LAS FILAS DEL MODELO QUE SE LE SETEA A LA TABLA
            modelo.addRow(objeto);
        }
        //SE DEFINE LA CANTIDAD Y EL TAMAÑO DE LAS FILAS DE LA TABLA
        tabla.setRowHeight(35);
        tabla.setRowMargin(10);
    }

    public boolean estaEnRango(int num) {

        if (num > 0 & num <= 100) {
            return true;
        }
        return false;
    }

    public boolean esNumerico(String num) {
        if (num.matches("[+-]?\\d*(\\.\\d+)?")) {
            return true;
        }

        return false;

    }

    public void cargando() {
        ImageIcon icon = new ImageIcon("C:\\Users\\Santiago\\Documents\\NetBeansProjects\\Programacion-avanzada-Branch-de-intento\\src\\img\\prueba.gif");
        JLabel label = new JLabel(icon);
        JWindow myJFrame = new JWindow();
        myJFrame.setLayout(new BorderLayout());
        myJFrame.add(label, BorderLayout.CENTER);
        myJFrame.setLocationRelativeTo(null);
        myJFrame.setOpacity(0.5f);
        myJFrame.setAlwaysOnTop(true);
        label.setOpaque(false);
        AWTUtilities.setWindowOpaque(myJFrame, false);
        myJFrame.pack();
        myJFrame.setVisible(true);

        /*   final String  img="https://tenor.com/bpeK1.gif";
        URL urlGif= new URL(img);
        Icon icongif = new ImageIcon(urlGif);
        JLabel anim= new JLabel(icongif);
        JFrame ventana = new JFrame("Visualizando gif");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.getContentPane().add(anim);
        ventana.setSize(400, 400);
        ventana.setVisible(true);
         */
    }
}
