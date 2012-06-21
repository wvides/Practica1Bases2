/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cubo;
import Mapeo.*;
import java.util.ArrayList;
/**
 *
 * @author Fer
 */
//Cubo que contiene la vista de Modelo Dimensional
public class Cubo {
    ModeloEstrella mode;//Modelo Estrella
    ArrayList<String> JerarquiasVisibles;//Jerarquias visibles en el Cubo
    
    /*--------Constructores----------*/
    public Cubo(){ 
        mode=null;
        this.JerarquiasVisibles = new ArrayList<String>();
    }
    public Cubo(ModeloEstrella mo){
        mode=mo;
        this.JerarquiasVisibles = new ArrayList<String>();
    }
    
    /*--------Metodos----------------*/
    public void setModeloEstrella(ModeloEstrella mo){ this.mode=mo; }
    public ModeloEstrella getModeloEstrella(){ return this.mode; }
    
    
    
    
    
}
