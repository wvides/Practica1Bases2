/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cubo;
import Mapeo.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    ArrayList<String> slices;
    ArrayList<String> columnas;
    ArrayList<String> filas;
    ArrayList<String> metricas;
    int cols=0;//Cantidad de columnas
    int fils=0;//Cantidad de filas
    ModeloTabla mot;
    String jerextra="";
    String pjer="";//Jerarquia pasada
    String pdim="";//Dimension pasada
    //Datos de conexion
    String uri="";
    String usuario="";
    String pass="";
    String fromx="";
    String wherex="";
    String whereh="";
    String wherev="";
    /*--------Constructores----------*/
    public Cubo(){ 
        mode=null;
        this.metricas=new ArrayList<String>();
        this.JerarquiasActuales = new ArrayList<String>();
        this.DimensionesActuales= new ArrayList<String>();
        this.slices= new ArrayList<String>();
    }
    
    public Cubo(ModeloEstrella mo){
        mode=mo;
        mot=new ModeloTabla();
        this.metricas=mode.metricas;
        this.JerarquiasActuales = new ArrayList<String>();
        this.DimensionesActuales=  new ArrayList<String>();
        this.slices= new ArrayList<String>();
        this.filas= new ArrayList<String>();
        this.columnas= new ArrayList<String>();
    }
    
    /*--------Metodos----------------*/
    public void setModeloEstrella(ModeloEstrella mo){ this.mode=mo; }
    public ModeloEstrella getModeloEstrella(){ return this.mode; }
    public void setMetricas(ArrayList<String> lis){ this.metricas=lis; }
    public boolean addSlice(String sl){
        if(!this.slices.contains(sl)){
            this.slices.add(sl);   
            return true;
        }
        return false; 
    }
    public void removeSlice(int index){this.slices.remove(index); this.makeSlice();}
    public void removeSlices(){this.slices.clear(); this.fromx=""; this.wherex="";
                                this.whereh=""; this.wherev="";}
    
    
    //Agrega una dimension con su respectiva jerarquia, no acepta dimensiones repetidas
    public void addDimensionJerarquia(String nom, String Jer){
        int size=this.DimensionesActuales.size();
        if(size==0){
            this.DimensionesActuales.add(nom);
            this.JerarquiasActuales.add(Jer);
        } else if(!this.DimensionesActuales.contains(nom)){
                this.DimensionesActuales.add(nom);
                this.JerarquiasActuales.add(Jer);
        }
    }
    //intercambia la dimension y su respectiva jerarquia con otra dimension y
    //jerarquia que este en otro indice
    public void cambiarDimensionJerarquia(String nom, String Jer, int index){
        
        if(this.DimensionesActuales.size()>0){
            int indorig=this.DimensionesActuales.indexOf(nom);//indice Origen
            String dimc=this.DimensionesActuales.get(index);//dim sustiyente
            String jerc=this.JerarquiasActuales.get(index);//jer sustityente
            if(index==indorig){
                //Solo se cambia la jerarquia
                this.JerarquiasActuales.remove(index);
                this.JerarquiasActuales.add(index, Jer);
            } else{
                if(indorig>1){
                    this.pdim=dimc;
                    this.pjer=jerc;
                }
                //if(this.DimensionesActuales.size()<indorig){
                //Sustituir la primera dimension y jerarquia
                this.DimensionesActuales.remove(index);
                this.JerarquiasActuales.remove(index);
                this.DimensionesActuales.add(index, nom);
                this.JerarquiasActuales.add(index, Jer);
                //Pasar la dimension y jerarquia removidas a su otra posicion
                this.DimensionesActuales.remove(indorig);
                this.JerarquiasActuales.remove(indorig);
                this.DimensionesActuales.add(indorig, dimc);
                this.JerarquiasActuales.add(indorig, jerc);
                //}
            }
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
                String cons= "select "+jer+" from "+dim;
            if(i==0)
                cons+=this.whereh;
            else
                cons+=this.wherev;              
            cons+="\ngroup by "+jer+" order by "+jer;
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
        String com="select d1."+this.JerarquiasActuales.get(1)+", d0."+this.JerarquiasActuales.get(0)+
                ", sum(t."+this.metricas.get(0)+")\n";
        
        
        //Dimensiones 
        String fro="from "+mode.NombreFactTable+" t, "+this.DimensionesActuales.get(0)+
                " d0, "+this.DimensionesActuales.get(1)+" d1\n";
                fro+=this.fromx+"\n";
        
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
        
        String whe="where t."+camp00+"=d0."+camp0+"\n and t."+
                camp11+"=d1."+camp1+"\n";
                whe+=this.wherex+"\n";
        String gro="group by d1."+this.getJerarquia(1)+", d0."+this.getJerarquia(0)
               +"\n order by d1."+this.getJerarquia(1)+", d0."+this.getJerarquia(0);
        
        String cmd=com+fro+whe+gro;
        System.out.println(cmd);
        this.hacerTableModel(cmd);
    }
    
    private void hacerTableModel(String comando){
        Sql sq= new Sql();
        ArrayList<String[]> pr=sq.consulta(comando);
        Object[][] dats= this.convertirDatos(pr);
        ArrayList<String> lis=new ArrayList<String>();
        lis.add(this.jerextra);
        lis.addAll(this.getColumnas());
        mot=new ModeloTabla();
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
    //Datos para Conexio
    public void setDatosUsuario(String ur, String use, String pas){
        this.uri=ur;
        this.usuario=use;
        this.pass=pas;
    }
    
    public void rehacer(){
        this.hacerHeaderColumnas();
        this.hacerHeaderFilas();
        this.hacerJoin();
    }
    //Nu: indice de la dimension, tipo: false - Drill UP y True Drill down
    public void drill(int nu, boolean tipo){
        ArrayList<Dimension> dims=this.mode.dimX1;
        String ds= this.DimensionesActuales.get(nu);
        Iterator ite = dims.iterator();
        Dimension dim=null;
        while(ite.hasNext()){
            dim = (Dimension)ite.next();
            if(dim.NombreDim.equals(ds)){
                ArrayList<String> jers=dim.Jerarquia;
                int tam=dim.Jerarquia.size();
                int acu=jers.indexOf(this.JerarquiasActuales.get(nu));
                if(tipo){
                    if(acu<tam-1)
                        acu++;
                } else {
                    if(acu>0)
                        acu--;
                }
                if(acu!=tam){
                    this.JerarquiasActuales.remove(nu);
                    String jera=jers.get(acu);
                    this.JerarquiasActuales.add(nu,jera);
                }
            }
        }
    }
    
    public void guardarAnterior(String nom, String jer){
        if(this.pdim.equals("")){
            pdim=nom;
            pjer=jer;
        } else {
            Iterator ite=this.DimensionesActuales.iterator();
            String dim="";
            int cont=0;
            boolean flag=true;
            while(ite.hasNext()){
                dim=ite.next().toString();
                if(dim.equals(nom))
                    flag=false;
                else if(cont==2)
                    break;
                cont++;
            }
            if(flag){
                pdim=nom;
                pjer=jer;
            }
        }
    }
    
    //Meto que realiza Dice
    public void dice(int nu){
        this.cambiarDimensionJerarquia(pdim, pjer, nu);
        this.rehacer();
    }
    
    //Metodo que realiza el slice
    public void makeSlice(){
        if(slices.size()>0){
            ArrayList<String> vDims=new ArrayList<String>();
            ArrayList<String> vJers=new ArrayList<String>();
            ArrayList<String> aDims=new ArrayList<String>();
            ArrayList<String> Noms=new ArrayList<String>();
            wherex="";
            wherev="";
            whereh="";
            fromx="";
            vDims.add(this.DimensionesActuales.get(0));
            vDims.add(this.DimensionesActuales.get(1));
            vJers.add(this.JerarquiasActuales.get(0));
            vJers.add(this.JerarquiasActuales.get(1));
            //vista.add(this.di);
            Iterator ite=slices.iterator();
            String st="";
            String[] dats;
            Pattern patron = Pattern.compile("^(\\w|\\s)+$");
            Matcher matc = null;
            boolean pal=false;
            String temp="";
            
            while(ite.hasNext()){
                st=ite.next().toString();
                dats=st.split("\\|");
                int index=0;
                boolean flag=false;
                if(dats!=null){
                    flag=vDims.contains(dats[0]);
                    //Agrega el from
                    if(!flag && !aDims.contains(dats[0]))
                        aDims.add(dats[0]); 
                    if(flag){
                        index=vDims.indexOf(dats[0]);
                    } else {
                        index=aDims.indexOf(dats[0])+2;
                    }
                    matc=patron.matcher(dats[2]);
                    pal = matc.matches();
                    if(pal)
                        dats[2]="'"+dats[2]+"'";
                    //agrega el where
                    Noms.add("d"+index+"."+dats[1]+"="+dats[2]+"\n");
             //wherex+=" and d"+index+"."+dats[1]+"="+dats[2]+"\n";
                    //agregar el where de las filas y columnas
                    if(index<2){
                        if(index==0){
                            if(whereh.equals("")){
                                whereh=" where "+dats[1]+"="+dats[2]+" ";
                            } else {
                                whereh+=" or "+dats[1]+"="+dats[2]+" ";
                            }
                        } else {
                            if(wherev.equals("")){
                                wherev=" where "+dats[1]+"="+dats[2]+" ";
                            } else {
                                wherev+=" or "+dats[1]+"="+dats[2]+" ";
                            }
                        }
                    }
                }
            }
            //Haciendo el where
            int lar=aDims.size()+2;
            for (int i = 0; i < lar; i++) {
                String fin="d"+i;
                String val="";
                String dia="";
                Iterator it=Noms.iterator();
                int cont=0;
                while(it.hasNext()){
                    dia=it.next().toString();
                    if(dia.startsWith(fin)){
                        if(cont==0){
                            val+="("+dia;
                        } else{
                            val+=" or "+dia;
                        }
                        cont++;
                    }
                }
                if(val.length()>0) {
                    this.wherex+=" and "+val+")";
                }
            }
            
            //Haciendo el from
            Iterator ite2=aDims.iterator();
            int cont=2;
            while(ite2.hasNext()){
                st=ite2.next().toString();
                fromx+=", "+st+" d"+cont;
                cont++;
            }
        }
    }
}
