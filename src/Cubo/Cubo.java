/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cubo;
import Mapeo.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.table.DefaultTableModel;
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
    DefaultTableModel mot;
    String jerextra="";
    /*--------Constructores----------*/
    public Cubo(){ 
        mode=null;
        this.metricas=new ArrayList<String>();
        this.JerarquiasActuales = new ArrayList<String>();
        this.DimensionesActuales=  new ArrayList<String>();
    }
    public Cubo(ModeloEstrella mo){
        mode=mo;
        mot=new DefaultTableModel();
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
        String cons= "select d1."+jer+" from "+dim+" d1 group by d1."+jer+" order by d1."+jer;
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
        this.jerextra=this.JerarquiasActuales.get(1);
        //Jerarquias
        String com="select d1."+this.JerarquiasActuales.get(1)+", d2."+this.JerarquiasActuales.get(0)+
                ", sum(t."+this.metricas.get(0)+")\n";
        
        
        //Dimensiones 
        String fro="from "+mode.NombreFactTable+" t, "+this.DimensionesActuales.get(0)+
                " d2, "+this.DimensionesActuales.get(1)+" d1\n";
        
        ArrayList<String> camps=mode.campos;
        String camp00="";
        String camp11="";
        String camp0="";
        String camp1="";
        
        Iterator ite= camps.iterator();
        String st="";
        while(ite.hasNext()){
            st=(String)ite.next();
            if(st.startsWith(this.DimensionesActuales.get(0))){
                camp00=st;
                camp0=st.replace(this.getDimension(0)+"_","");
                break;
            }
        }
        
        Iterator ite2=camps.iterator();
        while(ite2.hasNext()){
            st=(String)ite2.next();
            if(st.startsWith(this.DimensionesActuales.get(1))){
                camp11=st;
                camp1=st.replace(this.getDimension(1)+"_","");
                break;
            }
        }
        
        String whe="where t."+camp00+"=d2."+camp0+"\n and t."+
                camp11+"=d1."+camp1+"\n";
        String gro="group by d1."+this.getJerarquia(1)+", d2."+this.getJerarquia(0)
               +"\n order by d1."+this.getJerarquia(1)+", d2."+this.getJerarquia(0);
        
        String cmd=com+fro+whe+gro;
        System.out.println(cmd);
        this.hacerTableModel(cmd);
    }
    
    private void hacerTableModel(String comando){
        Sql sq= new Sql();
        ArrayList<String[]> pr=sq.consulta(comando);
        //System.out.println("");
        Object[][] dats= this.convertirDatos(pr);
        ArrayList<String> lis=new ArrayList<String>();
        lis.add(this.jerextra);
        lis.addAll(this.getColumnas());
        this.mot.setDataVector(dats, lis.toArray());
    }
    
    public Object[][] convertirDatos(ArrayList<String[]> dat){
        dat.remove(0);//Remover cabeceras
        String[][] datos= new String[this.fils][this.cols+1];
        HashMap hash= new HashMap();//Mapa de columnas
        for (int i = 0; i < this.cols; i++) {
            hash.put(this.columnas.get(i),i+1);
        }
        
        //String[] temp = (String[]) dat.get(0);
        //int lar = temp.length;//cantidad de columnas
        //int obp = lar-1;//Posicion de metrica
        int cont=1;//Contador de columnas hechas
        int repe=-1;//Contador de elementos repetidos
        int i=0;//indice
        int res=0;
        String aux="";
        Iterator ite = dat.iterator();
        while(ite.hasNext()){
            String[] vec=(String[])ite.next();
            if(!vec[0].equals(aux)){
                res=this.cols-cont+1;
                if(res!=0 && repe>0){
                    for (int j = 0; j < res; j++) {
                        datos[repe][cont+j]="0";
                        
                    }
                }
                aux=vec[0];//anterior
                repe++;
                datos[repe][0]=aux;//Nombre de fila
                cont=1;
            }
            //Comprobar indices
            i=Integer.parseInt(hash.get(vec[1]).toString());
            res=cont-i;
            if(res<0){
                res*=-1;
                for (int j = 0; j < res; j++){
                    datos[repe][cont+j]="0";
                }
            }
            datos[repe][i]=vec[2];//colocando el dato
            cont++;//inc columna hecha
        }
        //Ultima fila y columna
        res=this.cols-cont+1;
        if(res!=0 && repe>0){
            for (int j = 0; j < res; j++) {
                datos[repe][cont+j]="0";

            }
        }
        return datos;
    }
}
