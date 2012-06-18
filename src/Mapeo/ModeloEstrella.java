/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Mapeo;

import java.util.ArrayList;

/**
 *
 * @author eddytrex
 */
public class ModeloEstrella {
    
    public String NombreFactTable;
    ArrayList <Dimension> dimX1=new ArrayList();      
    
    ArrayList <String> campos=new ArrayList(); //Todos los atributos de la tabla de hechos
    ArrayList<ForenKey> FK=new ArrayList();
    
    
    public ModeloEstrella(ArrayList <Dimension> x,ArrayList <String> c,ArrayList<ForenKey>fk,String nf)
    {
        this.dimX1=x;
        this.campos=c;
        this.FK=fk;
        this.NombreFactTable=nf;
    }
    
    
    
}
