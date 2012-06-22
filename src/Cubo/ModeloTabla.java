/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cubo;

/**
 *
 * @author Fer
 */

import java.util.*; 
import javax.swing.table.*; 
import javax.swing.event.TableModelListener; 
import javax.swing.event.TableModelEvent; 
import java.util.Vector;



public class ModeloTabla extends AbstractTableModel { 
    Object[] nombreColumnas={}; 
    Vector filas = new Vector(); 
    
    /** Creates a new instance of ModeloTabla */ 
    public ModeloTabla() { 
    } 
    
    public int getColumnCount() { 
        return nombreColumnas.length; 
    } 
    
    public int getRowCount() { 
        return filas.size(); 
    } 
    
    /*public Object getValueAt(int row, int column) { 
        Vector tmp = (Vector)filas.elementAt(row); 
        return tmp.elementAt(column); 
    } 
    * 
    */
    
    public void setDataVector(Vector data, Object[] columnNames)   {
     if (data == null)
        this.filas = new Vector();
      else
        filas = data;
     this.nombreColumnas = columnNames;
      this.fireTableStructureChanged();
      this.fireTableDataChanged();
   }


    public void setDataVector(Object[][] data, Object[] columnNames) 
    {
      setDataVector(convertToVector(data), 
                   columnNames);
      this.fireTableStructureChanged();
      this.fireTableDataChanged();
    }

    protected static Vector convertToVector(Object[] data) 
   {
     if (data == null)
      return null;
     Vector vector = new Vector(data.length);
     for (int i = 0; i < data.length; i++) 
       vector.add(data[i]);
     return vector;          
  }

     protected static Vector convertToVector(Object[][] data) 
  {
     if (data == null)
       return null;
     Vector vector = new Vector(data.length);
     for (int i = 0; i < data.length; i++)
       vector.add(convertToVector(data[i]));
     return vector;
   }

    public Object getValueAt(int row, int column) { 
        Vector tmp = (Vector)filas.elementAt(row); 
        
        return tmp.elementAt(column); 
    } 
    
    public boolean isCellEditable(int row, int column){ 
        return false; 
    } 
    
    public String getColumnName(int column) 
    {   if(nombreColumnas[column]!=null) 
        { return nombreColumnas[column].toString(); 
        } 
        else 
        {   return ""; 
        } 
    } 
    
    public void setColumnas(Vector columnas) 
    {   try 
        {   nombreColumnas = new String[columnas.size()]; 
            for(int i=0;i<columnas.size();i++) 
            {   nombreColumnas[i]=columnas.elementAt(i).toString(); 
            } 
            
            fireTableStructureChanged(); 
        } 
        catch(Exception ex) { 
            System.out.println("Exception setColumnas"+ex.getMessage 
()); 
            
        } 
    } 
    
    public void setDatos(Vector datos) 
    {   try 
        {   filas = datos; 
           //actualizar(); 
            fireTableStructureChanged(); 
        } 
        catch(Exception ex) { 
            System.out.println("Exception setDatos"+ex.getMessage()); 
            
        } 
    } 
    
    
    
    
    public void nuevaTabla(Vector columnas,Vector datos) { 
        
        try { 
            
           setColumnas(columnas); 
           setDatos(datos); 
            fireTableStructureChanged(); 
        } 
        catch (Exception ex) { 
            System.err.println(ex); 
        } 
    } 
    
    
} 