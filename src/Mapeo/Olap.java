
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
public class Olap {
    
    
    ModeloEstrella estrella;
    
    
    Modelo a;    
    
    ArrayList<queryDim> dimensionesPosibles;     
    
    ArrayList <Dimension> dimX1=new ArrayList();
     
    String tablaechosOrignial;
    
    String NombreTablahechos;   
    ArrayList <String> camposTablahechos=new ArrayList();
    ArrayList<ForenKey> listaNuevasFK=new ArrayList();
    
    
    
    ArrayList<String> Camposmetricas=new ArrayList();
    
    
    public ArrayList<queryDim> getDimensionesPosibles() {
        return dimensionesPosibles;
    }
    
    public Olap(Modelo m,String tablaHechos,ArrayList<String> camposmetricas)
    {
        this.a=m;
        
        this.Camposmetricas=camposmetricas;
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
           Dimension tempdim=new    Dimension(temppd);
        //Dimension tempdim=new Dimension(temppd,null);
        
        tempdim.CrearDimensionTiempo(campo_fecha);
        dimX1.add(tempdim);    
        }
     }
     
     public void generaTablaEchos(String ntablaH)
     {      
            this.NombreTablahechos=ntablaH;
            Iterator <Dimension> dimension=this.dimX1.iterator();
            queryDim n=new queryDim();
            
            String sPredicadoC="";
            String sTablasC="";
            String sCamposC="";
            
            String sCampos="";
            String sTablas="";
            String sPredicado="";
            
            ArrayList<String> Campos =new ArrayList();
            ArrayList<String> Tablas=new ArrayList();
            
            ArrayList<String> cp=new ArrayList();
            ArrayList<String> c=new ArrayList();
            
            if(dimension.hasNext())
            {
               Dimension tempX1=dimension.next();
                Tablas.add(tempX1.NombreDim);               
                  
                Iterator <String> camposllave=tempX1.dimOriginal.forenKeyOriginal.camposPadre.iterator();                
                Iterator <String> camposllaveHija=tempX1.dimOriginal.forenKeyOriginal.campos.iterator();                
                                
                String tablaPadreO=tempX1.dimOriginal.forenKeyOriginal.tablaPadre;
                
                 if(camposllave.hasNext())
                 {
                    String tempCampollave=camposllave.next();
                    String tempCampollavehija=camposllaveHija.next();
                    Campos.add(tempX1.NombreDim+"_"+tablaPadreO+"_"+tempCampollave);
                    
                    cp.add(tablaPadreO+"_"+tempCampollave);
                    c.add(tempX1.NombreDim+"_"+tablaPadreO+"_"+tempCampollave);
                            
                    sCampos=tempX1.NombreDim+"."+tablaPadreO+"_"+tempCampollave+" AS "+tempX1.NombreDim+"_"+tablaPadreO+"_"+tempCampollave;
                    sPredicado=tempX1.NombreDim+"."+tablaPadreO+"_"+tempCampollave+"="+this.tablaechosOrignial+"."+tempCampollavehija;
                    
                  while(camposllave.hasNext())
                  {
                    tempCampollave=camposllave.next();
                    tempCampollavehija=camposllaveHija.next();
                    Campos.add(tempX1.NombreDim+"_"+tempCampollave);
                    
                    cp.add(tablaPadreO+"_"+tempCampollave);
                    c.add(tempX1.NombreDim+"_"+tempCampollave);
                    
                    sCampos=tempX1.NombreDim+"."+tablaPadreO+"_"+tempCampollave+" AS "+tempX1.NombreDim+"_"+tablaPadreO+"_"+tempCampollave;;
                    sPredicado=sPredicado+" and "+tempX1.NombreDim+"."+tablaPadreO+"_"+tempCampollave+"="+this.tablaechosOrignial+"."+tempCampollavehija;
                  }
                  
                  this.listaNuevasFK.add(new ForenKey(c,cp,tempX1.NombreDim,NombreTablahechos));
                  cp=new ArrayList();
                  c=new ArrayList();
                }
                sCamposC=sCampos;
                sPredicadoC=sPredicado;    
                                
                while(dimension.hasNext())
                {
                    
                 tempX1=dimension.next();
                Tablas.add(tempX1.NombreDim);               
                  
                camposllave=tempX1.dimOriginal.forenKeyOriginal.camposPadre.iterator();                
                camposllaveHija=tempX1.dimOriginal.forenKeyOriginal.campos.iterator();                
                                
                 tablaPadreO=tempX1.dimOriginal.forenKeyOriginal.tablaPadre;
                
                 if(camposllave.hasNext())
                 {
                    String tempCampollave=camposllave.next();
                    String tempCampollavehija=camposllaveHija.next();
                    Campos.add(tempX1.NombreDim+"_"+tablaPadreO+"_"+tempCampollave);
                    
                    cp.add(tablaPadreO+"_"+tempCampollave);
                    c.add(tempX1.NombreDim+"_"+tablaPadreO+"_"+tempCampollave);
                    
                    sCampos=tempX1.NombreDim+"."+tablaPadreO+"_"+tempCampollave+" AS "+tempX1.NombreDim+"_"+tablaPadreO+"_"+tempCampollave;
                    sPredicado=tempX1.NombreDim+"."+tablaPadreO+"_"+tempCampollave+"="+this.tablaechosOrignial+"."+tempCampollavehija;
                    
                  while(camposllave.hasNext())
                  {
                    tempCampollave=camposllave.next();
                    tempCampollavehija=camposllaveHija.next();
                    Campos.add(tempX1.NombreDim+"_"+tablaPadreO+"_"+tempCampollave);
                    
                    cp.add(tablaPadreO+"_"+tempCampollave);
                    c.add(tempX1.NombreDim+"_"+tablaPadreO+"_"+tempCampollave);
                    
                    sCampos=tempX1.NombreDim+"."+tablaPadreO+"_"+tempCampollave +" AS "+tempX1.NombreDim+"_"+tablaPadreO+"_"+tempCampollave;
                    sPredicado=sPredicado+" and "+tempX1.NombreDim+"."+tablaPadreO+"_"+tempCampollave+"="+this.tablaechosOrignial+"."+tempCampollavehija;
                  }                
                }
                 
                 this.listaNuevasFK.add(new ForenKey(c,cp,tempX1.NombreDim,NombreTablahechos));
                 cp=new ArrayList();
                 c=new ArrayList();
                 
                sCamposC=sCamposC+","+sCampos;
                sPredicadoC=sPredicadoC+" and "+sPredicado;    
                }
            }
            
            
            Tablas.add(this.tablaechosOrignial);
            Iterator<String> T=Tablas.iterator();
            if(T.hasNext())
            {
                sTablasC=T.next();
                while(T.hasNext())
                {
                    sTablasC=sTablasC+","+T.next();
                }
            
            }
            
            Iterator<String> mcamposMetricas=this.Camposmetricas.iterator();
            while(mcamposMetricas.hasNext())
            {
                String tempcMetica=mcamposMetricas.next();
                Campos.add(tempcMetica);                                          
                sCamposC=sCamposC+", "+tempcMetica;
            }
            
            
            
            String STH="CREATE TABLE "+this.NombreTablahechos+" AS SELECT "+sCamposC+" FROM "+sTablasC+" WHERE "+sPredicadoC+";";
            
            Sql th=new Sql();
            th.ejecuta(STH);
            
            
            this.estrella=new ModeloEstrella(this.dimX1,Campos,this.listaNuevasFK,ntablaH);
            int p;
            p=1;
     }
    
    public ModeloEstrella getModeloEstrella(){
        return this.estrella;
    }
    
}
