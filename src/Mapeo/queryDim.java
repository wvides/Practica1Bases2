/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Mapeo;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author eddytrex
 */
public class queryDim {    
    
    String Nombre;      
    String Encabezado;
    String Tablas;
    String Condicion;
    
    public ArrayList<String> CamposLlave=new ArrayList();   
    public ArrayList<String> Atributos=new ArrayList();
    ArrayList<String> Entidades=new ArrayList();        
    public ForenKey forenKeyOriginal;
    
    String Select="";
    
    public ArrayList<String> getAtributos() {
        return Atributos;
    }   
     

    public ArrayList<String> getEntidades() {
        return Entidades;
    }    
    
    public void addEntidad(String e){ this.Entidades.add(e);}        
    
   public queryDim()
   {
        Encabezado="";
        Tablas="";
        Condicion="";
   } 
   
   public void addAtributos(ArrayList<String> campos)
   {
        this.Atributos=campos;       
   }
   
   public void addEncabezado(String header)
   {
       if(!Encabezado.equals(""))
       {  
       Encabezado=Encabezado+","+header;
       }
       else
       {
           Encabezado=header;
       }
   }
   
   public void addTabas(String Tabla)
   {
        if(!Tablas.equals(""))
        {
          Tablas=Tablas+","+Tabla; 
          this.Entidades.add(Tabla) ;
        }
        else
        {
           this.Entidades.add(Tabla) ;
            Tablas=Tabla;
        }
   }
   
   
   
   public void addCondicion(String subCondicion)
   {
       if(!Condicion.equals(""))
       {
           Condicion=Condicion+" and "+subCondicion;
       }
       else
       {
           Condicion=subCondicion;
       }
   }
   
   public queryDim Funcionar(queryDim a)
   {
       queryDim result=new queryDim();
       
       Iterator<String> camposa=a.getAtributos().iterator();
       Iterator<String>  camposthis=this.getAtributos().iterator();
       
       while(camposthis.hasNext())
       {
           result.Atributos.add(camposthis.next());
       }
       
       while(camposa.hasNext())
       {
            result.Atributos.add(camposa.next());
       }
       
       
       Iterator<String> tablasa=a.getEntidades().iterator();
      while(tablasa.hasNext())
      {
          this.addEntidad(tablasa.next());
      }
       
        if(!a.Condicion.equals(""))
        {result.Condicion=a.Condicion+" and  "+this.Condicion;}
        else
        {
            result.Condicion=this.Condicion;
        }
        
        if(!a.Encabezado.equals(""))
        {
        result.Encabezado=a.Encabezado+" , "+this.Encabezado;
        }
        else
        {
            result.Encabezado=this.Encabezado;
        }
        
        result.forenKeyOriginal=this.forenKeyOriginal;
        result.Tablas=a.Tablas+","+this.Tablas;       
       return result;
      }
   
    public String getView(String nombreDim)
    {
            String result="";
            
            this.Nombre=nombreDim;
            
            if(!this.Encabezado.equals("") && !this.Tablas.equals("")&&!this.Condicion.equals(""))
            {
                   String restartSeq="ALTER SEQUENCE sk_dim RESTART 1;";
                   Sql restart=new Sql();
                   restart.ejecuta(restartSeq);
                   
                 String drop="DROP TABLE "+nombreDim+";";
                  Sql dropt=new Sql();
                 dropt.ejecuta(drop);
                   
                    this.Select="SELECT  nextval(\'sk_dim\') AS PK,"+this.Encabezado+" FROM "+this.Tablas+" WHERE "+this.Condicion+";";                    
                    result="CREATE TABLE "+nombreDim+" AS "+this.Select;
                    Sql createView=new Sql();
                    String prueba=createView.ejecuta(result);
                    int a;
                    a=1+1;
                            
            }
            if(!this.Encabezado.equals("") && !this.Tablas.equals("")&&this.Condicion.equals(""))
            {
                String restartSeq="ALTER SEQUENCE sk_dim RESTART 1;";
                Sql restart=new Sql();
                restart.ejecuta(restartSeq);
                   
                String drop="DROP TABLE "+nombreDim+";";
                Sql dropt=new Sql();
                dropt.ejecuta(drop);
                
                this.Select="SELECT nextval(\'sk_dim\') AS PK,"+this.Encabezado+" FROM "+this.Tablas+";";                    
                result="CREATE TABLE "+nombreDim+" AS "+this.Select;
                Sql createView=new Sql();
                String prueba=createView.ejecuta(result);
                
                int a;
                a=1+1;           
            }
            return result;
    }
    
    public boolean equals(queryDim a)
    {
        if(this.Nombre.equals(a.Nombre)){return true;}
        return false;
    }
    
    
    public void CrearDimensionTiempo(String campo_fecha)
    {
        String pk="";
        
        if(this.Atributos.contains(campo_fecha))            
        {    
        Iterator<String> clave=this.forenKeyOriginal.campos.iterator();
        
        if(clave.hasNext())
        {
            pk="t."+this.forenKeyOriginal.tablaPadre+"_"+clave.next();
        while(clave.hasNext())
        {
            pk=pk+", t."+this.forenKeyOriginal.tablaPadre+"_"+clave.next();
        }
            
        }
        
        String drop="DROP TABLE "+"dim_tiempo"+";";
          Sql dropt=new Sql();
          dropt.ejecuta(drop);
        
        String encabezado=pk+", extract(year from t."+campo_fecha+") AS año ,extract(quarter from t."+ campo_fecha+") AS trimestre ,extract(month from t."+ campo_fecha+") AS mes ,extract(day from t."+campo_fecha+") AS dia";
        String tablas=this.Nombre+" t";        
        String CrearTabla="CREATE TABLE dim_tiempo "+" AS SELECT "+encabezado+" FROM "+tablas+";"; 
        
        Sql a=new Sql();
        a.ejecuta(CrearTabla);
        }  
    }
    
    public void CrearDimensionTiempoTablaHechos(String campo_fecha, Entidad entidadParaHechos)
    {
        
        this.Entidades.add(entidadParaHechos.nombre);
        
        ArrayList<String> c=new ArrayList();
        c.add(campo_fecha);
        ArrayList<String> cp=new ArrayList();
        cp.add(campo_fecha);
        
        ForenKey fk=new ForenKey(c,cp,entidadParaHechos.nombre,entidadParaHechos.nombre);
        
        this.Atributos.add("dia");
        this.Atributos.add("mes");
        this.Atributos.add("trimestre");
        this.Atributos.add("año");
        
        
        
        this.Atributos.add(campo_fecha);
        
        this.CamposLlave.add(campo_fecha);
        
        this.Nombre="dimension_0t";
        
        this.forenKeyOriginal=fk;
        
        
         String select="(SELECT "+ campo_fecha +" FROM "+entidadParaHechos.nombre+" GROUP BY "+campo_fecha+") AS t";
         String jeraquia=campo_fecha+" AS "+entidadParaHechos.nombre+"_"+campo_fecha+", extract( year from t."+campo_fecha+") AS año, extract(quarter from t."+campo_fecha+") AS trimestre, extract(month from t."+campo_fecha+") AS mes, extract(day from t."+campo_fecha+") AS dia";
         String SelectQ="SELECT  "+jeraquia+" FROM "+select+";";
         
         String drop="DROP TABLE "+"dimension_0t"+";";
          Sql dropt=new Sql();
          dropt.ejecuta(drop);
          
         String CrearTabla="CREATE  TABLE dimension_0t "+" AS "+SelectQ+"";
         
         Sql a=new Sql();
        a.ejecuta(CrearTabla);
    }

    public String getNombre() {
        return Nombre;
    }
       
}

