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
public class ForenKey {
    
    public ArrayList<String> campos=new ArrayList();
    public ArrayList<String> camposPadre=new ArrayList();
    
    String tablaHija;
    public String campo;
    public String tablaPadre;
    public String campoPadre;
    
    public ForenKey (String  [] fila) {
        campo = fila [1];
        campoPadre = fila [0];
        tablaPadre = fila [2];
    }
   
    public ForenKey(String tablaOrigen, String tablaDestino)
    {
          String consulta=" SELECT  a.column_name Campo_Origen,  b.column_name Campo_Destino "
                                    + " FROM information_schema.constraint_column_usage a, information_schema.key_column_usage b, information_schema.table_constraints c  "
                                    + " WHERE a.constraint_name=b.constraint_name and c.constraint_name=a.constraint_name and c.constraint_type='FOREIGN KEY'"
                                    + " and b.table_name=\'"+tablaDestino+"\' and  a.table_name=\'"+tablaOrigen+"\';";
          
          this.tablaPadre=tablaOrigen;
          this.tablaHija=tablaDestino;
          
           ArrayList<String[]> query = new Sql ().consulta(consulta);
           Iterator<String[]>  FilaCampoFK = query.iterator();         
           FilaCampoFK.next();
           while (FilaCampoFK.hasNext())
           {
                    String fila[]=FilaCampoFK.next();
                   campos.add(fila[1]);
                   camposPadre.add(fila[0]);
           }
    }
    
    public String GetSubCondicion()
    {
        String result="";
        String Igualdad="";
        Iterator<String> campo=this.campos.iterator();
        Iterator<String> campoPadre=this.camposPadre.iterator();
        
        if(campos.size()==camposPadre.size())
        {
            if(campo.hasNext()&&campoPadre.hasNext())
            {
               Igualdad=this.tablaPadre+"."+campoPadre.next()+"="+this.tablaHija+"."+campo.next();
               result=Igualdad;
            }
            while (campo.hasNext()&&campoPadre.hasNext())
            {                    
                    Igualdad=this.tablaPadre+"."+campoPadre.next()+"="+this.tablaHija+"."+campo.next();
                    result=result+" and "+Igualdad;
                    
            }
        }
        
        return result;
    }
}
