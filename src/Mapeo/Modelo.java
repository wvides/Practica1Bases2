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
public class Modelo {
    //orenamiento de variables de manera adecuada y tabulacion correcta. 
    Sql seq = new Sql();                
    Entidad entidadParaEchos;
    ArrayList <Entidad> entidades=new ArrayList();    
    ArrayList<queryDim> dimOriginales=new ArrayList();          

    String squencia=" CREATE SEQUENCE  sk_dim START WITH 1";
    String restarSequencia="ALTER SEQUENCE sk_dim RESTART 1;";

    public ArrayList<queryDim> getDimOriginales() {
        return dimOriginales;
    }
     
    public void ejecutaSecuencia()
    {
    seq.ejecuta(squencia);
    }
    public void cargarEntidades(){

        // Crear las Entidades con sus Atributos

        String consulta="SELECT table_name  "
        + "FROM information_schema.tables "
        + "WHERE table_schema = 'public';";

    ArrayList<String[]> query = new Sql ().consulta(consulta);
    Iterator<String[]> i = query.iterator();
    i.next();
    while (i.hasNext()){
        String[] fila = i.next();

        Entidad entidad=new Entidad(fila[0]);
        int a = 132;
        entidades.add(entidad);
    }

    // Asociar las Primary Key a cada Entidad

    int fd = 212;
    fd= 181;

    }

    public ArrayList<Entidad> getEntidades() {
        return entidades;
    }

    public void setEntidades(ArrayList<Entidad> entidades) {
        this.entidades = entidades;
    }
     
     
     public Entidad BuscarEntidad(String Nombre)
     {
         Entidad result=null;
         
         Iterator<Entidad> i=this.entidades.iterator();
         
         while(i.hasNext())
         {
             Entidad al=null;
             al=i.next();
             if(al.nombre.equals(Nombre)){result=al; return result;}
         }       
            
         return result;
     }
     
     public queryDim getDimension(ForenKey relacion)
     {    
         queryDim result=new queryDim();
        
         Entidad origen=this.BuscarEntidad(relacion.tablaPadre);
         if(origen!=null)
         {    
             result.forenKeyOriginal=relacion;
             result.addEncabezado(origen.getStringCampos());
             result.addAtributos(origen.getCamposAsAlias());
             result.addTabas(origen.nombre);
             Iterator<ForenKey>  FKs = origen.getFK().iterator();
             while(FKs.hasNext())
             {
                 ForenKey temp=FKs.next();
                 
                 result.addCondicion(temp.GetSubCondicion());                 
                 queryDim tempDim=getDimension(temp);      
                 
                 result=result.Funcionar(tempDim);                 
             }             
         }
         return result;
     }
     
     public ArrayList<String> getDimensiones(String entidadHechos)
     {
         ArrayList<String> dimensiones=new ArrayList();
         Entidad hechos=this.BuscarEntidad(entidadHechos);
         entidadParaEchos=hechos;
         if(hechos!=null)
         {
           ArrayList<ForenKey> FK=new ArrayList();
           FK=hechos.getFK();
            Iterator<ForenKey> i = FK.iterator();
            int num = 0;
            while (i.hasNext())
            {
                num++;
                queryDim tempQueryDim=this.getDimension(i.next());
                this.dimOriginales.add(tempQueryDim);
                dimensiones.add(tempQueryDim.getView("dimension_0"+num));
            }
         
         }         
         return dimensiones;
     }

    public Sql getSeq() {
        return seq;
    }

    public void setSeq(Sql seq) {
        this.seq = seq;
    }
     
     
          
     
}
