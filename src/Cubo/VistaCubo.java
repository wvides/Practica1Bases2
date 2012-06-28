/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cubo;

import GUI.Front;
import Mapeo.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;

/**
 *
 * @author Fer
 */
public class VistaCubo extends javax.swing.JFrame {

    /**
     * Creates new form VistaCubo
     */
    Cubo cubo=null;
    String jerh="";
    String jerv="";
    String dimh="";
    String dimv="";
    DefaultListModel model = new DefaultListModel();
    public VistaCubo() {
        initComponents();
        this.limpiarJList();
        Front.URL = "jdbc:postgresql://127.0.0.1:5432/prueba1";
        Front.user = "postgres";
        Front.password = "postgres";

        System.out.println("My new String: " + Front.URL);
        
          Modelo a=new Modelo();
            a.cargarEntidades();                                    
            a.ejecutaSecuencia();
           // Entidad prueba=a.BuscarEntidad("agencia");                        
            //ArrayList<String> dimensiones=a.getDimensiones("detallefactura");           
            ArrayList<Entidad> x = a.getEntidades();            
            
          
           
          
            
           
            
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
            
            es.setDimX1("dimension_02", jeraquia, "ubicacion");
            
          
            
            //Dimension dim=new Dimension(a.getDimOriginales().get(1),jeraquia);         
           
           //a.getDimOriginales().get(1).CrearDimensionTiempo("factura_fecha");
                    
           //dim.CrearDimension("Ubicacion");
                     
           jeraquia=new ArrayList();
           jeraquia.add("producto_nombre");
           
           es.setDimX1("dimension_01", jeraquia, "producto");
           
           es.setDimTiempo("dimension_02", "factura_fecha");
           
           
           // ya introducidas las dimenciones con jerarquias se coloca el nombre de la tabla hechos.  este metodo crea un Atributo de Olap(estrella) 
           es.generaTablaEchos("prueba");
           //TODO: 
           //estrella tiene los campos  de la tabla hechos, las llaves foraneas  (hacia las dimenciones) y las dimenciones con jeraquias.
           
           
            
            ModeloEstrella mode= es.getModeloEstrella();
            mode.setMetricas(metricas);
            
            cubo= new Cubo(mode);
            this.llenarItemsDims();
            cubo.cambiarDimensionJerarquia("dim_ubicacion", "pais_nombre",0);
            cubo.cambiarDimensionJerarquia("dim_producto", "producto_nombre",1);
            //cubo.getHeader(0);
            cubo.rehacer(); 
            this.tabla.setModel(cubo.mot);
        
    }

    public VistaCubo(Cubo cub) {
        cubo=cub;
        initComponents();
        this.limpiarJList();
        this.llenarItemsDims();
        
    }
    
    private void llenarItemsDims(){
        this.dch.removeAllItems();
        this.dcv.removeAllItems();
        this.jch.removeAllItems();
        this.jcv.removeAllItems();
        this.dsli.removeAllItems();
        ArrayList<Dimension> dims =this.cubo.mode.dimX1;
        Iterator ite=dims.iterator();
        int cont=0;
        while(ite.hasNext()){
            Dimension dim=(Dimension)ite.next();
            String nom=dim.NombreDim;
            this.dch.addItem(nom);
            this.dcv.addItem(nom);
            this.dsli.addItem(nom);
            this.cubo.addDimensionJerarquia(nom, "");
            cont++;
        }
        if(cont>0){
            dch.setSelectedIndex(0);
            dcv.setSelectedIndex(0);
            dsli.setSelectedIndex(0);
        }
        
    }
    
    private void llenarCampos(){
        try{
        if(this.dsli!= null && this.jsli!= null && this.csli!=null){
            String dim=this.dsli.getSelectedItem().toString();
            String jer=this.jsli.getSelectedItem().toString();
            Sql sq= new Sql();
            String comando="select distinct "+jer+" from "+dim;
            ArrayList<String[]> pr=sq.consulta(comando);
            pr.remove(0);
            System.out.println("");
            Iterator ite= pr.iterator();
            this.csli.removeAllItems();
            while(ite.hasNext()){
                String[] cam=(String[])ite.next();
               this.csli.addItem(cam[0]);
            }
            
        }
        }catch(Exception e){
            System.out.println("seleccionar campos");
            JOptionPane.showMessageDialog(this, "Seleccione los campos otra vez.");
        }
    }
    private void llenarJerarquia(int nu){
        JComboBox com=this.dch;
        JComboBox hij=this.jch;
        if(com!=null && hij !=null){
            if(nu==1){
                com=this.dcv;
                hij=this.jcv;
            } else if(nu==2){
                com=this.dsli;
                hij=this.jsli;
            }
            if(com.getSelectedItem()!=null){
                String di=com.getSelectedItem().toString();
                Dimension dim=null;
                Iterator ite=this.cubo.mode.dimX1.iterator();
                while(ite.hasNext()){
                    dim=(Dimension)ite.next();
                    if(dim.NombreDim.equals(di)){
                        hij.removeAllItems();
                        Iterator ite2=dim.Jerarquia.iterator();
                        while(ite2.hasNext()){
                            String nom=ite2.next().toString();
                            hij.addItem(nom);
                        }
                    }
                }
            }

        }
    }
    //Cambia los nombres de los LabesNombres y cambia los JCombox de dimensiones
    public void setNombres(){
        this.dimh=this.cubo.DimensionesActuales.get(0);
        this.dimv=this.cubo.DimensionesActuales.get(1);
        this.jerh=this.cubo.JerarquiasActuales.get(0);
        this.jerv=this.cubo.JerarquiasActuales.get(1);
        this.ndh.setText("Nombres: "+dimh+", "+jerh);
        this.ndv.setText("Nombres: "+dimv+", "+jerv);
        //this.dch.setSelectedItem(dimh);
        //this.dcv.setSelectedItem(dimv);
    }
    
    private void limpiarJList(){
        DefaultListModel  model =(DefaultListModel)this.slics.getModel();
        model.removeAllElements();
    }
    private void addItemJL(String st){
        DefaultListModel  model =(DefaultListModel)this.slics.getModel();
        model.addElement(st);
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
        jLabel1 = new javax.swing.JLabel();
        dch = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jch = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        dcv = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jcv = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        radH = new javax.swing.JRadioButton();
        radV = new javax.swing.JRadioButton();
        drillup = new javax.swing.JButton();
        drilldonw = new javax.swing.JButton();
        diceb = new javax.swing.JButton();
        slice = new javax.swing.JButton();
        ndh = new javax.swing.JLabel();
        ndv = new javax.swing.JLabel();
        selHor = new javax.swing.JButton();
        JGenerarCubo = new javax.swing.JButton();
        selVer = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jDesSlice = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        dsli = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jsli = new javax.swing.JComboBox();
        csli = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        addSlice = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        slics = new javax.swing.JList();
        obCampos = new javax.swing.JButton();
        jDesSliceAll = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Dimension Horizontal:");

        dch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        dch.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dchItemStateChanged(evt);
            }
        });

        jLabel2.setText("Jerarquia Dimension H:");

        jch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText("Dimension Vertical:");

        dcv.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        dcv.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dcvItemStateChanged(evt);
            }
        });

        jLabel4.setText("Jerarquia Dimension V:");

        jcv.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Operaciones");

        radH.setSelected(true);
        radH.setText("Dimension Horizontal");
        radH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radHActionPerformed(evt);
            }
        });

        radV.setText("Dimension Vertical");
        radV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radVActionPerformed(evt);
            }
        });

        drillup.setText("Drill up");
        drillup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                drillupActionPerformed(evt);
            }
        });

        drilldonw.setText("Drill down");
        drilldonw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                drilldonwActionPerformed(evt);
            }
        });

        diceb.setText("Dice");
        diceb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dicebActionPerformed(evt);
            }
        });

        slice.setText("Slice");
        slice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sliceActionPerformed(evt);
            }
        });

        ndh.setText("Nombres:");

        ndv.setText("Nombres:");

        selHor.setText("seleccionar");
        selHor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selHorActionPerformed(evt);
            }
        });

        JGenerarCubo.setText("Generar Vista");
        JGenerarCubo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JGenerarCuboActionPerformed(evt);
            }
        });

        selVer.setText("seleccionar");
        selVer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selVerActionPerformed(evt);
            }
        });

        jDesSlice.setText("Remover Slice");
        jDesSlice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDesSliceActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Atributos de Slice");

        dsli.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        dsli.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dsliItemStateChanged(evt);
            }
        });

        jLabel7.setText("Dimension:");

        jLabel8.setText("Jerarquia:");

        jsli.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        csli.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel9.setText("Campo:");

        addSlice.setText("Agregar a Slice");
        addSlice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSliceActionPerformed(evt);
            }
        });

        jLabel10.setText("Slices:");

        slics.setModel(model);
        jScrollPane2.setViewportView(slics);

        obCampos.setText("Obtener Campos");
        obCampos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                obCamposActionPerformed(evt);
            }
        });

        jDesSliceAll.setText("Remover Todas las Slices");
        jDesSliceAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDesSliceAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(41, 41, 41)
                                        .addComponent(selHor))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(29, 29, 29)
                                        .addComponent(dcv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                        .addComponent(radV)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(ndv)
                                                        .addGap(4, 4, 4))
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addComponent(drillup)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(drilldonw)
                                                        .addGap(18, 18, 18)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addComponent(radH)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(ndh)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(diceb))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel6)
                                                .addGap(67, 67, 67))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel7)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(dsli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel8)
                                                .addGap(18, 18, 18)
                                                .addComponent(jsli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(slice)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(74, 74, 74)
                                                .addComponent(obCampos)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel9))))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addGap(18, 18, 18)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jDesSlice)
                                            .addComponent(jDesSliceAll)))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jcv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addGap(34, 34, 34)
                                        .addComponent(selVer)
                                        .addGap(223, 223, 223)
                                        .addComponent(JGenerarCubo)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(csli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(addSlice)
                        .addGap(249, 249, 249))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(dch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(dch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(selHor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(dcv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jcv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(selVer)
                            .addComponent(JGenerarCubo))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(radH)
                            .addComponent(ndh))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(radV)
                            .addComponent(ndv))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(drillup)
                            .addComponent(drilldonw)
                            .addComponent(diceb)
                            .addComponent(slice))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(dsli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jsli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(obCampos)
                            .addComponent(jLabel9)
                            .addComponent(csli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addSlice))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jDesSlice)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDesSliceAll)
                                .addContainerGap())
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 44, Short.MAX_VALUE))))))
        );

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tabla);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JGenerarCuboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JGenerarCuboActionPerformed
        // TODO add your handling code here:
        int nu=0;
        if(cubo.DimensionesActuales.size()==0){
            cubo.addDimensionJerarquia(dimh, jerh);
            cubo.addDimensionJerarquia(dimv, jerv);
        } else {
        this.cubo.cambiarDimensionJerarquia(dimh, jerh, 0);
        this.cubo.cambiarDimensionJerarquia(dimv, jerv, 1);
        }
        this.cubo.rehacer();
        
        this.tabla.setModel(this.cubo.mot);//this.tabla.setModel(cubo.mot);
        //this.jScrollPane1.remove(tabla);
        //this.tabla=tab;
        //this.jScrollPane1.add(tabla);
        
        
    }//GEN-LAST:event_JGenerarCuboActionPerformed

    private void radVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radVActionPerformed
            // TODO add your handling code here:
        if(this.radV.isSelected()){
            this.radH.setSelected(false);
        }
    }//GEN-LAST:event_radVActionPerformed

    private void radHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radHActionPerformed
        // TODO add your handling code here:
        if(this.radH.isSelected()){
            this.radV.setSelected(false);
        }
    }//GEN-LAST:event_radHActionPerformed

    private void dchItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_dchItemStateChanged
        // TODO add your handling code here:
        this.llenarJerarquia(0);
    }//GEN-LAST:event_dchItemStateChanged

    private void dcvItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_dcvItemStateChanged
        // TODO add your handling code here:
        this.llenarJerarquia(1);
    }//GEN-LAST:event_dcvItemStateChanged

    private void selHorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selHorActionPerformed
        // TODO add your handling code here:
        this.dimh=this.dch.getSelectedItem().toString();
        this.jerh=this.jch.getSelectedItem().toString();
        if(this.radH.isSelected())
            this.cubo.guardarAnterior(dimh, jerh);
        this.ndh.setText("Nombres: "+dimh+", "+jerh);
        
    }//GEN-LAST:event_selHorActionPerformed

    private void selVerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selVerActionPerformed
        // TODO add your handling code here:
        this.dimv=this.dcv.getSelectedItem().toString();
        this.jerv=this.jcv.getSelectedItem().toString();
        if(this.radV.isSelected())
            this.cubo.guardarAnterior(dimv, jerv);
        this.ndv.setText("Nombres: "+dimv+", "+jerv);
    }//GEN-LAST:event_selVerActionPerformed

    private void drillupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_drillupActionPerformed
        // TODO add your handling code here:
        int nu=0;
        if(this.radV.isSelected()){
            nu=1;
        }
        this.cubo.drill(nu,false);
        this.cubo.rehacer();
        this.setNombres();//Cambia los nombres de los LabesNombres
        this.tabla.setModel(this.cubo.mot);
    }//GEN-LAST:event_drillupActionPerformed

    private void dicebActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dicebActionPerformed
        // TODO add your handling code here:
        //this.cubo.cambiarDimensionJerarquia(dimh, jerh, 0);
        //this.cubo.cambiarDimensionJerarquia(dimv, jerv, 1);
        //this.cubo.rehacer();
        int nu=0;
        if(this.radV.isSelected())
            nu=1;
        this.cubo.dice(nu);
        this.setNombres();//Cambia los nombres de los LabesNombres
        this.tabla.setModel(this.cubo.mot);
    }//GEN-LAST:event_dicebActionPerformed

    private void drilldonwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_drilldonwActionPerformed
        // TODO add your handling code here:
        int nu=0;
        if(this.radV.isSelected()){
            nu=1;
        }
        this.cubo.drill(nu, true);
        this.cubo.rehacer();
        this.setNombres();//Cambia los nombres de los LabesNombres
        this.tabla.setModel(this.cubo.mot);
    }//GEN-LAST:event_drilldonwActionPerformed

    private void sliceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sliceActionPerformed
        // TODO add your handling code here:
        this.cubo.makeSlice();
        this.cubo.rehacer();
        this.setNombres();
        this.tabla.setModel(this.cubo.mot);
    }//GEN-LAST:event_sliceActionPerformed

    private void jDesSliceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDesSliceActionPerformed
        // TODO add your handling code here:
        int index=this.slics.getSelectedIndex();
        this.model.remove(index);
        this.cubo.removeSlice(index);
        this.cubo.makeSlice();
        this.cubo.rehacer();
        this.setNombres();
        this.tabla.setModel(this.cubo.mot);
    }//GEN-LAST:event_jDesSliceActionPerformed

    private void addSliceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSliceActionPerformed
        // TODO add your handling code here:
        String camp=this.dsli.getSelectedItem().toString()+"|"+
                    this.jsli.getSelectedItem().toString()+"|"+
                    this.csli.getSelectedItem().toString();
        boolean val = this.cubo.addSlice(camp);
        if(val)
            this.addItemJL(camp);
    }//GEN-LAST:event_addSliceActionPerformed

    private void dsliItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_dsliItemStateChanged
        // TODO add your handling code here:
        this.llenarJerarquia(2);
    }//GEN-LAST:event_dsliItemStateChanged

    private void obCamposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_obCamposActionPerformed
        // TODO add your handling code here:
        
        this.llenarCampos();
    }//GEN-LAST:event_obCamposActionPerformed

    private void jDesSliceAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDesSliceAllActionPerformed
        // TODO add your handling code here:
        this.cubo.removeSlices();
        this.model.removeAllElements();
    }//GEN-LAST:event_jDesSliceAllActionPerformed

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
            java.util.logging.Logger.getLogger(VistaCubo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VistaCubo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VistaCubo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VistaCubo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new VistaCubo().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JGenerarCubo;
    private javax.swing.JButton addSlice;
    private javax.swing.JComboBox csli;
    private javax.swing.JComboBox dch;
    private javax.swing.JComboBox dcv;
    private javax.swing.JButton diceb;
    private javax.swing.JButton drilldonw;
    private javax.swing.JButton drillup;
    private javax.swing.JComboBox dsli;
    private javax.swing.JButton jDesSlice;
    private javax.swing.JButton jDesSliceAll;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JComboBox jch;
    private javax.swing.JComboBox jcv;
    private javax.swing.JComboBox jsli;
    private javax.swing.JLabel ndh;
    private javax.swing.JLabel ndv;
    private javax.swing.JButton obCampos;
    private javax.swing.JRadioButton radH;
    private javax.swing.JRadioButton radV;
    private javax.swing.JButton selHor;
    private javax.swing.JButton selVer;
    private javax.swing.JButton slice;
    private javax.swing.JList slics;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
}
