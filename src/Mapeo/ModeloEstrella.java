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
    
    ArrayList <Dimension> dimX1=new ArrayList();      
    
    ArrayList <String> campos=new ArrayList();
    ArrayList<ForenKey> FK=new ArrayList();
    
    
    public ModeloEstrella(ArrayList <Dimension> x,ArrayList <String> c,ArrayList<ForenKey>fk)
    {
        this.dimX1=x;
        this.campos=c;
        this.FK=fk;
    }
    
    
    
}
