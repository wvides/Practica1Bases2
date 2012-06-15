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
public class Estrella {
    
    Modelo a;    
    
    ArrayList<queryDim> dimensionesPosibles;

    

    
    
    ArrayList <Dimension> dimX1=new ArrayList();
     
    String tablaechosOrignial;
    
    
    ArrayList<String> Metricas=new ArrayList();
    
    
    public ArrayList<queryDim> getDimensionesPosibles() {
        return dimensionesPosibles;
    }
    
    public Estrella(Modelo m,String tablaHechos,ArrayList<String> metricas)
    {
        this.a=m;
        
        this.Metricas=metricas;
        this.tablaechosOrignial=tablaHechos;
        a.cargarEntidades();
        a.getDimensiones(tablaHechos);                
        
        this.dimensionesPosibles=a.getDimOriginales();
    }
    
    
     public void setDimX1(String posibleDimension, ArrayList<String> jerarquia,String Nombre)
    {
        Iterator<queryDim> pd=dimensionesPosibles.iterator();
        queryDim temppd=null;
        while(pd.hasNext())
        {
            temppd=pd.next();
            if(temppd.Nombre.equals(posibleDimension)){ break;}else {temppd=null;}            
        }
        
        if(temppd!=null)
        {
            Dimension tempdim=new Dimension(temppd,jerarquia);
            tempdim.CrearDimension(Nombre);
            dimX1.add(tempdim);            
        }   
    }
     
     public void setDimTiempo(String posibleDimension,String campo_fecha)
     {
         Iterator<queryDim> pd=dimensionesPosibles.iterator();
        queryDim temppd=null;
        while(pd.hasNext())
        {
            temppd=pd.next();
            if(temppd.Nombre.equals(posibleDimension)){ break;}   else {temppd=null;}         
        }
        
        if(temppd!=null)
        {
        Dimension tempdim=new Dimension(temppd,null);
        
        tempdim.CrearDimensionTiempo(campo_fecha);
        dimX1.add(tempdim);    
        }
     }
     
     public void generaTablaEchos()
     {      
            
     }
    
    
    
}
