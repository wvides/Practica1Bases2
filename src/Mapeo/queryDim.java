/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Mapeo;

/**
 *
 * @author eddytrex
 */
public class queryDim {
    String Encabezado;
    String Tablas;
    String Condicion;
    
   public queryDim()
   {
        Encabezado="";
        Tablas="";
        Condicion="";
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
        }
        else
        {
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
        
        
        result.Tablas=a.Tablas+","+this.Tablas;       
       return result;
      }
   
    public String getView(String nombreDim)
    {
            String result="";
            String Select="";
            
            if(!this.Encabezado.equals("") && !this.Tablas.equals("")&&!this.Condicion.equals(""))
            {
                
                    Select="SELECT  "+this.Encabezado+" FROM "+this.Tablas+" WHERE "+this.Condicion+";";                    
                    result="CREATE VIEW "+nombreDim+" AS "+Select;
                    Sql createView=new Sql();
                    String prueba=createView.ejecuta(result);
                    int a;
                    a=1+1;
                            
            }
            if(!this.Encabezado.equals("") && !this.Tablas.equals("")&&this.Condicion.equals(""))
            {
                Select="SELECT  "+this.Encabezado+" FROM "+this.Tablas+";";                    
                result="CREATE VIEW "+nombreDim+" AS "+Select;
                Sql createView=new Sql();
                String prueba=createView.ejecuta(result);
                
                int a;
                a=1+1;           
            }
            return result;
    }
   
}
