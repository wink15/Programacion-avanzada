/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import controlador.ControladorApi;

/**
 *
 * @author Santiago
 */
public class VistaApi extends javax.swing.JFrame {

    /**
     * Creates new form VistaApi
     */
    public VistaApi() {
        initComponents();
         labelApi.setVisible(false);
            desdeApi.setVisible(false);
         hastaApi.setVisible(false);
        labelUnica.setVisible(false);
         unica.setVisible(false);
        
    }
      public void inicializar () {
            //SE CREA UNA INSTACINA DE LA VISTA
            VistaApi va = new VistaApi();
            //SE CREA UNA INSTANCIA DEL CONTROLADOR
               
            ControladorApi con = new ControladorApi(va);
            //SE HACE VISIBLE LA VISTA
            va.setVisible(true);
         
            //SE DEFINE LA UBICACION DE LA VISTA
            va.setLocationRelativeTo(null);
            //SE LLENA EL COMBO PERSONA
            //con.llenarPersonaCliente();
            
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        labelApi = new javax.swing.JLabel();
        desdeApi = new javax.swing.JTextField();
        hastaApi = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaApi = new javax.swing.JTable();
        ComboxApi = new javax.swing.JComboBox<>();
        btnBuscar = new javax.swing.JButton();
        unica = new javax.swing.JTextField();
        labelUnica = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        labelApi.setText("Rango ");

        desdeApi.setText("Desde");
        desdeApi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                desdeApiMouseClicked(evt);
            }
        });

        hastaApi.setText("Hasta");
        hastaApi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                hastaApiMouseClicked(evt);
            }
        });
        hastaApi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hastaApiActionPerformed(evt);
            }
        });

        tablaApi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID User", "ID", "Titulo", "Body"
            }
        ));
        jScrollPane1.setViewportView(tablaApi);

        ComboxApi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione", "Busqueda unica", "Busqueda por rango" }));
        ComboxApi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ComboxApiMouseClicked(evt);
            }
        });
        ComboxApi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboxApiActionPerformed(evt);
            }
        });

        btnBuscar.setText("Buscar");

        labelUnica.setText("ID");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(ComboxApi, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(labelApi, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labelUnica))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(desdeApi)
                                    .addGap(18, 18, 18)
                                    .addComponent(hastaApi, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(unica, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE)))))
                    .addComponent(btnBuscar))
                .addContainerGap(484, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 644, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(ComboxApi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelApi)
                    .addComponent(desdeApi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hastaApi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(unica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelUnica))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBuscar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(62, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void hastaApiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hastaApiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hastaApiActionPerformed

    private void ComboxApiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboxApiActionPerformed
        // TODO add your handling code here:
        
     
        
    }//GEN-LAST:event_ComboxApiActionPerformed

    private void ComboxApiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ComboxApiMouseClicked
        // TODO add your handling code here:
      
    }//GEN-LAST:event_ComboxApiMouseClicked

    private void desdeApiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_desdeApiMouseClicked
        // TODO add your handling code here:
        desdeApi.setText("");
        
    }//GEN-LAST:event_desdeApiMouseClicked

    private void hastaApiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hastaApiMouseClicked
        // TODO add your handling code here:
        hastaApi.setText("");
    }//GEN-LAST:event_hastaApiMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VistaApi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VistaApi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VistaApi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VistaApi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VistaApi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JComboBox<String> ComboxApi;
    public javax.swing.JButton btnBuscar;
    public javax.swing.JTextField desdeApi;
    public javax.swing.JTextField hastaApi;
    public javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JLabel labelApi;
    public javax.swing.JLabel labelUnica;
    public javax.swing.JTable tablaApi;
    public javax.swing.JTextField unica;
    // End of variables declaration//GEN-END:variables
}
