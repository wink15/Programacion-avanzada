/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JTable;
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
  
    VistaApi vista= new VistaApi();
    DefaultTableModel modelo = new DefaultTableModel();
    Api testApi = new Api();
    APIDAO dao = new APIDAO();
    
    private int bandera = 0;
    
       
    
      
    //CREAMOS UN CONTROLADOR POR CADA VISTA EXISTENTE
    public ControladorApi (VistaApi v){
        this.vista = v;
        this.vista.ComboxApi.addActionListener(this);
        this.vista.btnBuscar.addActionListener(this);
       
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.ComboxApi) {
          
            Intento();
        }
         if (e.getSource() == vista.btnBuscar) {
            //SE ELIMINA UN TIPO DE PROYECTO
            
            //SE BUSCAN LOS NUEVOS TIPOS DE PROYECTO
            buscarApi(vista.tablaApi);
        }
        
    }
    public void Intento (){
          //String opcion = String.valueOf(vista.ComboxApi.getSelectedItem());
           String opcion = vista.ComboxApi.getSelectedItem().toString();
         
            String opcionE = "Busqueda por rango";
            String opcionE2= "Busqueda unica";
            if(opcion.equals(opcionE)){
              this.bandera=1;
                 vista.desdeApi.setVisible(true);
                  vista.hastaApi.setVisible(true);
                 vista.labelApi.setVisible(true);
                  vista.labelUnica.setVisible(false);
              vista.unica.setVisible(false);
                        
               // JLabel.setText("Estoy modificando el texto");
            } else if (opcionE2.equals(opcion)){
                this.bandera=2;
                       //  vista.desdeApi.setBounds(new Rectangle(25, 15, 250, 21));
              vista.labelUnica.setVisible(true);
              vista.unica.setVisible(true);
               vista.desdeApi.setVisible(false);
                  vista.hastaApi.setVisible(false);
                 vista.labelApi.setVisible(false);
            }
    }
    public void buscarApi(JTable tabla){
         ArrayList<Api> listas= new ArrayList<>();
        if (this.bandera ==1){
           String  desde= vista.desdeApi.getText();
           String hasta = vista.hastaApi.getText();
           listas=dao.GetApi(2, desde, hasta);
            
        } else if(this.bandera==2){
            String id = vista.unica.getText();
          listas= dao.GetApi(1,id,"");
        
        }
        
         // centrarCeldas(tabla);
        //EN EL MODELO CREADO SE GUARDA EL MODELO DE LA TABLA TIPO DE PROYECTO
        modelo = (DefaultTableModel) tabla.getModel();
        tabla.setModel(modelo);
        //SE CREA UN ARRAY CON LOS TIPOS DE PROYECTOS QUE SE TRAEN DESDE LA BD
       
        
        //SE CREA UN ARRAY OBJETO QUE ALMACENA LOS DATOS DE CADA TIPO DE PROYECTO
        Object[] objeto = new Object[4];
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
}
