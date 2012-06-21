/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cubo;
import Mapeo.*;
import java.util.ArrayList;
import java.util.Iterator;
/**
 *
 * @author Fer
 */
//Cubo que contiene la vista de Modelo Dimensional
public class Cubo {
    ModeloEstrella mode;//Modelo Estrella
    ArrayList<String> JerarquiasActuales;/*Jerarquias actuales
     * (0) Jerarquia horizontal actual - visible
     * (1) Jerarquia vertical actual - visible
     *  ... (n) JerarquiaN posible actual
     */
    ArrayList<String> DimensionesActuales;/*Dimensiones actuales
     * (0) Dimension horizontal actual - visible
     * (1) Dimension vertical actual - visible
     *  ... (n) DimensionN posible actual
     */
    ArrayList<String> columnas;
    ArrayList<String> filas;
    ArrayList<String> metricas;
    int cols=0;//Cantidad de columnas
    int fils=0;//Cantidad de filas
            
    
    /*--------Constructores----------*/
    public Cubo(){ 
        mode=null;
        this.metricas=new ArrayList<String>();
        this.JerarquiasActuales = new ArrayList<String>();
        this.DimensionesActuales=  new ArrayList<String>();
    }
    public Cubo(ModeloEstrella mo){
        mode=mo;
        this.metricas=mode.metricas;
        this.JerarquiasActuales = new ArrayList<String>();
        this.DimensionesActuales=  new ArrayList<String>();
        this.filas= new ArrayList<String>();
        this.columnas= new ArrayList<String>();
    }
    
    /*--------Metodos----------------*/
    public void setModeloEstrella(ModeloEstrella mo){ this.mode=mo; }
    public ModeloEstrella getModeloEstrella(){ return this.mode; }
    
    //Agrega una dimension con su respectiva jerarquia, no acepta dimensiones repetidas
    public void addDimensionJerarquia(String nom, String Jer){
        int size=this.DimensionesActuales.size();
        if(size==0){
            this.DimensionesActuales.add(nom);
            this.JerarquiasActuales.add(Jer);
        }
        else if(!this.DimensionesActuales.contains(nom)){
                this.DimensionesActuales.add(nom);
                this.JerarquiasActuales.add(Jer);
        }
    }
    
    //public void DiceDimension(String nom){  }
    
    private String getDimension(int i){
        return this.DimensionesActuales.get(i);
    }
    
    private String getJerarquia(int i){
        return this.JerarquiasActuales.get(i);
    }
    
    //Retorna el nombre de las columnas(0), filas(1) segun su jerarquia actual
    public ArrayList<String> getHeader(int i){
        Sql sq= new Sql();
        String dim=this.getDimension(i);
        String jer=this.getJerarquia(i);
        String cons= "select d1."+jer+" from "+dim+" d1 group by d1."+jer;
        //ArrayList<String[]> pr=sq.consulta("select d1.pais_nombre from dim_ubicacion d1 group by d1.pais_nombre");
        ArrayList<String[]> pr=sq.consulta(cons);
        ArrayList<String> lis = new ArrayList<String>();
        if(pr!=null){
            pr.remove(0);
            Iterator ite=pr.iterator();
            while(ite.hasNext()){
                String[] vec = (String[])ite.next();
                lis.add(vec[0]);
            }
            
        }
        //System.out.println("pri");
        return lis;
    }

    //Devuelve encabezado de columnas
    public void hacerHeaderColumnas(){
        this.columnas=this.getHeader(0);
        this.cols=this.columnas.size();
    }
    
    //Devuelve encabezado de filas
    public void hacerHeaderFilas(){
        this.filas=this.getHeader(1);
        this.fils=this.filas.size();
    }
    //Devuelve Encabezados
    public ArrayList<String>  getColumnas(){ return this.columnas;}
    public ArrayList<String>  getFilas(){ return this.filas;}
    
    
    public void hacerJoin(){
        
    }
}
