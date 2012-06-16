/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Mapeo.Dimension;
import Mapeo.Entidad;
import Mapeo.Olap;
import Mapeo.Modelo;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

/**
 *
 * @author omar
 */
public class Front extends javax.swing.JFrame {

    DefaultListModel dm = new DefaultListModel();
    /**
     * Creates new form Front
     */
    public Front() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Test");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 911, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 510, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Transaccional", jPanel1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 911, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 510, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Analitica", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addComponent(jTabbedPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
            Modelo a=new Modelo();
            a.cargarEntidades();
           // Entidad prueba=a.BuscarEntidad("agencia");                        
            //ArrayList<String> dimensiones=a.getDimensiones("detallefactura");           
            ArrayList<Entidad> x = a.getEntidades();            
            
          
           
           int B=10;
           B=10+1;            
            Iterator m = x.iterator();
            int xpos = 0;
            int ypos = 0;
            int wrap = 0;
            while(m.hasNext())
            {
                Entidad w = (Entidad) m.next();                
                JInternalFrame f = new JInternalFrame(w.getNombre(), true);
                JPanel jp = new JPanel();
                jp.setBounds(0, 0, 200, 150);
                ArrayList<String[]> att = w.getAtributos();
                System.out.println(att.size());
                Iterator u = att.iterator();
                while(u.hasNext())
                {
                    String[] t = (String[]) u.next();
                    JCheckBox chk = new JCheckBox(t[1] + " " + t[0]);
                    System.out.println(t[0]);
                    jp.add(chk);
                    
                }
                f.add(jp);
                if(wrap == 7)
                {
                    ypos++;
                    xpos = 0;
                    wrap = 0;
                }
                f.setBounds(xpos * 210, ypos*155, 200, 150);
                f.setVisible(true);                
                this.jPanel1.add(f);
                xpos++;
                wrap++;
            }      
            
           
            
            ArrayList<String> metricas=new ArrayList();
            
            metricas.add("cantidad");
            // un modelo una tabla de hechos, y metricas (campos de la tabla hechos)
            //entre los atributos de Olap tiene un ArrayList de dimenciones (dimensionesPosibles de tipo queryDim ) sin jerarquia para que el usuario elija 
            Olap es=new Olap(a,"detallefactura",metricas);
            
            ArrayList<String> jeraquia=new ArrayList();
            
            jeraquia.add("pais_nombre");
            jeraquia.add("departamento_nombre");
            jeraquia.add("municipio_nombre");
            jeraquia.add("agencia_nombre");           
            
            es.setDimX1("dimencion_02", jeraquia, "ubicacion");
            
           //Dimension dim=new Dimension(a.getDimOriginales().get(1),jeraquia);         
           
           //a.getDimOriginales().get(1).CrearDimensionTiempo("factura_fecha");
                    
           //dim.CrearDimension("Ubicacion");
                     
           jeraquia=new ArrayList();
           jeraquia.add("producto_nombre");
           
           es.setDimX1("dimencion_01", jeraquia, "producto");
           
           es.setDimTiempo("dimencion_02", "factura_fecha");
           
           
           // ya introducidas las dimenciones con jerarquias se coloca el nombre de la tabla hechos.  este metodo crea un Atributo de Olap(estrella) 
           es.generaTablaEchos("prueba");
           //estrella tiene los campos  de la tabla hechos, las llaves foraneas  (hacia las dimenciones) y las dimenciones con jeraquias.
           
            int c;
            c=1+1;
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Front.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Front.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Front.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Front.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Front().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
