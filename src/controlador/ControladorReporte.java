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
public class ControladorReporte implements ActionListener {

    public ControladorReporte() {
    }

    //CREAMOS INSTANCIAS DE OTRAS CLASES
    VistaMenu vista = new VistaMenu();

    //CREAMOS UN CONTROLADOR POR CADA VISTA EXISTENTE
    public ControladorReporte(VistaMenu v) {
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
                //SE LLAMA AL METODO QUE GENERA LOS REPORTES
                generarReporte();

            } catch (JRException ex) {
                Logger.getLogger(ControladorReporte.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ControladorReporte.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        if (e.getSource() == vista.itemGrafico) {
            //SE LLAMA AL METODO PARA GENERAR EL GRAFICO
            generarGrafico();

        }

    }

    public void generarReporte() throws JRException, SQLException {
        try {
            Connection con;
            Conexion conectar = new Conexion();
            con = conectar.getConnection();
            //SE BUSCA EL PATH EN DONDE SE ENCUENTRA EL ARCHIVO DEL REPORTE HECHO EN EL JASPERSOFT
            String reportPath = "src\\reporte\\Reporte.jrxml";
            // System.out.println(reportPath);
            //SE UTILIZA EL MAP PARA PODER INGRESAR LOS PARAMETROS PUESTOS EN EL REPORTE, EN ESTE CASO, IMAGENES
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("logo", "src\\img\\gizmo.png");
            map.put("contacto1", "src\\img\\contacto1.png");
            map.put("contacto2", "src\\img\\contacto2.png");
            map.put("ubicacion", "src\\img\\ubicacion.png");
            //COMPILA EL REPORTE
            JasperReport jr = JasperCompileManager.compileReport(reportPath);
            //LO IMPRIME
            JasperPrint jp = JasperFillManager.fillReport(jr, map, con);
            //LO MUESTRA
            JasperViewer.viewReport(jp, false);
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void generarGrafico() {
        //SE LLAMA AL METODO QUE GENERA EL GRAFICO PARA LUEGO COLOCARLO EN EL JASPERSOFT
        this.generarImg();
        Connection con;
        Conexion conectar = new Conexion();
        con = conectar.getConnection();
        try {
            //SE BUSCA EL PATH EN DONDE SE ENCUENTRA EL ARCHIVO DEL REPORTE HECHO EN EL JASPERSOFT
            String reportPath = "src\\reporte\\GraficoReporte.jrxml";
            // System.out.println(reportPath);
            //SE UTILIZA EL MAP PARA PODER INGRESAR LOS PARAMETROS PUESTOS EN EL REPORTE, EN ESTE CASO, IMAGENES
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("logo", "src\\img\\gizmo.png");
            map.put("contacto1", "src\\img\\contacto1.png");
            map.put("contacto2", "src\\img\\contacto2.png");
            map.put("ubicacion", "src\\img\\ubicacion.png");
            map.put("grafico", "src\\img\\GraficoTorta.jpg");
            //COMPILA EL REPORTE
            JasperReport jr = JasperCompileManager.compileReport(reportPath);
            //StandardChartTheme.createLegacyTheme().apply(jr);
            // ChartFactory.setChartTheme(StandardChartTheme.createJFreeTheme());
            //LO IMPRIME
            JasperPrint jp = JasperFillManager.fillReport(jr, map, con);
            //LO MUESTRA
            JasperViewer.viewReport(jp, false);

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void generarImg() {

        ResultSet rs = null;
        Connection con;
        Conexion conectar = new Conexion();
        con = conectar.getConnection();
        // SE SETEAN LOS NUMEROS QUE SE OBTIENENS
        int numNac = 0;
        int numInt = 0;
        // CONSULTA QUE TRAE LA CANTIDAD DE PROYECTOS NACIONALES
        String consultaNacional = " SELECT COUNT(proyecto.ubicacion) as cantidad fROM prog_av.proyecto WHERE proyecto.ubicacion=1 and proyecto.borrado=0";
        // CONSULTA QUE TRAE LA CANTIDAD DE PROYECTOS INTERNACIONALES
        String consultaInternacional = " SELECT COUNT(proyecto.ubicacion) as cantidad fROM prog_av.proyecto WHERE proyecto.ubicacion=2 and proyecto.borrado=0";

        try {

            PreparedStatement ps = con.prepareStatement(consultaNacional);
            rs = ps.executeQuery();
            while (rs.next()) {
                //OBTIENE LA CANTIDAD Y LA GUARDA
                numNac = (rs.getInt("cantidad"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(VistaMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            PreparedStatement ps = con.prepareStatement(consultaInternacional);
            rs = ps.executeQuery();
            while (rs.next()) {
                //OBTIENE LA CANTIDAD Y LA GUARDA
                numInt = (rs.getInt("cantidad"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(VistaMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        //SE LLAMA AL GRAFICO EN TORTA
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        //SE PASAN LOS VALORES DEL GRAFICO
        pieDataset.setValue("Nacional " + numNac, new Integer(numNac));
        pieDataset.setValue("Internacional " + numInt, new Integer(numInt));

        JFreeChart chart = ChartFactory.createPieChart(
                "Proyectos",
                pieDataset,
                true,
                true,
                false);
        try {
            //SE GUARDA LA IMAGEN EN EL SRC PARA LUEGO SER UTILIZADA
            ChartUtilities.saveChartAsJPEG(new File("src\\img\\GraficoTorta.jpg"), chart, 500,
                    300);
        } catch (Exception e) {
            System.out.println("Error creando grafico.");
        }
    }
}
