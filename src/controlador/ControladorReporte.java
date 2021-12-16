/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import modelo.Clientes;
import modelo.ClientesDAO;
import modelo.Conexion;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import vista.VistaClientes;
import vista.VistaMenu;

/**
 *
 * @author Santiago
 */
public class ControladorReporte implements ActionListener{
     public ControladorReporte () {
    }
    
    //CREAMOS INSTANCIAS DE OTRAS CLASES
 
    VistaMenu vista= new VistaMenu();
 
    
    //CREAMOS UN CONTROLADOR POR CADA VISTA EXISTENTE
    public ControladorReporte (VistaMenu v){
        this.vista = v;
        this.vista.itemReporte.addActionListener(this);
        this.vista.itemGrafico.addActionListener(this);
        
    }
    
    //EJECUCION DE CADA BOTON DENTRO DE CADA PANTALLA
     @Override
    public void actionPerformed(ActionEvent e) {
        //VISTA Cliente
        if (e.getSource() == vista.itemReporte) {
            try {
                //SE LIMPIA LA TABLA QUE CONTIENE LOS TIPOS DE PROYECTO
                generarReporte();
                //SE BUSCAN TODOS LOS TIPOS DE PROYECTO DE LA BD
            } catch (JRException ex) {
                Logger.getLogger(ControladorReporte.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ControladorReporte.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        }
         
        if (e.getSource() == vista.itemGrafico) {
            //SE ELIMINA UN TIPO DE PROYECTO
            generarGrafico();
            //SE BUSCAN LOS NUEVOS TIPOS DE PROYECTO
            
        }
        
    
}
    
     public void generarReporte() throws JRException, SQLException{
        try {
            Connection con;
            Conexion conectar = new Conexion();
            con = conectar.getConnection();

            String reportPath = "src\\reporte\\Reporte.jrxml";
            // System.out.println(reportPath);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("logo", "src\\img\\gizmo.png");
            map.put("contacto1", "src\\img\\contacto1.png");
            map.put("contacto2", "src\\img\\contacto2.png");
            map.put("ubicacion", "src\\img\\ubicacion.png");
            JasperReport jr = JasperCompileManager.compileReport(reportPath);
            JasperPrint jp = JasperFillManager.fillReport(jr, map, con);
            JasperViewer.viewReport(jp);
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
     }
    
     public void generarGrafico(){
         this.generarImg();
         Connection con;
        Conexion conectar = new Conexion();
        con = conectar.getConnection();
         try {
        String reportPath = "C:\\Users\\Santiago\\JaspersoftWorkspace\\Prueba\\GraficoReporte.jrxml";
           // System.out.println(reportPath);
            Map<String,Object>map= new HashMap<String,Object>();
            map.put("logo", "src\\img\\gizmo.png");
            map.put("contacto1", "src\\img\\contacto1.png");
             map.put("contacto2", "src\\img\\contacto2.png");
              map.put("ubicacion", "src\\img\\ubicacion.png");
                map.put("grafico", "src\\img\\GraficoTorta.jpg");
            JasperReport jr = JasperCompileManager.compileReport(reportPath);
            //StandardChartTheme.createLegacyTheme().apply(jr);
           // ChartFactory.setChartTheme(StandardChartTheme.createJFreeTheme());
            JasperPrint jp = JasperFillManager.fillReport(jr, map, con);
            JasperViewer.viewReport(jp);
           
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
     }
     
     public void generarImg(){
           ResultSet rs = null;
        Connection con;
        Conexion conectar = new Conexion();
        con = conectar.getConnection();
        int numNac=0;
         String consultaNacional = " SELECT COUNT(proyecto.ubicacion) as cantidad fROM prog_av.proyecto WHERE proyecto.ubicacion=1";
        String consultaInternacional = " SELECT COUNT(proyecto.ubicacion) as cantidad fROM prog_av.proyecto WHERE proyecto.ubicacion=2";
        
        int numInt = 0;
        try {
        
            PreparedStatement ps = con.prepareStatement(consultaNacional);
             rs = ps.executeQuery();
             while (rs.next()) {  
                
                 numNac=(rs.getInt("cantidad"));
                
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(VistaMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
 
        try {
            
            PreparedStatement  ps = con.prepareStatement(consultaInternacional);
             rs = ps.executeQuery();
             while (rs.next()) {  
                
                 numInt=(rs.getInt("cantidad"));
                
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(VistaMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
         DefaultPieDataset pieDataset = new DefaultPieDataset();
        pieDataset.setValue("Nacional " + numNac , new Integer(numNac));
        pieDataset.setValue("Internacional " +  numInt, new Integer(numInt));
        // pieDataset.setValue("Kubuntu", new Integer(10));
        // pieDataset.setValue("Otros", new Integer(5));
        JFreeChart chart = ChartFactory.createPieChart(
                "Proyectos",
                pieDataset,
                true,
                true,
                false);
        try {
            ChartUtilities.saveChartAsJPEG(new File("src\\img\\GraficoTorta.jpg"), chart, 500,
                    300);
        } catch (Exception e) {
            System.out.println("Error creando grafico.");
        }
     }
}