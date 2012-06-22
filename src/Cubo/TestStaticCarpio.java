/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cubo;

import GUI.Front;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import Mapeo.*;
/**
 *
 * @author Fer
 */
public class TestStaticCarpio {
     public static void main(String args[]) {
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
            int c;
            c=1+1;
            Cubo cubo= new Cubo(mode);
            cubo.addDimensionJerarquia("dim_ubicacion", "pais_nombre");
            cubo.addDimensionJerarquia("dim_producto", "producto_nombre");
            //cubo.getHeader(0);
            cubo.hacerJoin();
            
     }
}
