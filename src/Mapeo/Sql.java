/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Mapeo;

import java.sql.*;
import java.util.ArrayList;
/**
    Document   : index
    Created on : 7/07/2011, 09:33:52 PM
    Author     : stuardo
 */
public class Sql {

// Ejecuta Insert, Delete y Update. Retorna null si todo bien, caso contrario, el mensaje de error
     public String ejecuta(String sql) {
        String mensaje= null;
        try {
            Conexion 	db = new Conexion();//Llamamos a nuestra Clase Conexion
            Connection	cn = db.getConnection();

            if (cn == null) {
            	mensaje = "No hay conexión a la base de datos...!";
            } else {
            	Statement st = cn.createStatement();//Obtiene procedimiento o consulta
            	st.execute(sql);//Ejecuta Consulta
            	st.close();
            	cn.close();
            }
        } catch(SQLException e)	{
            mensaje= e.getMessage();

        } catch(Exception e) {
            mensaje= e.getMessage();
        }

        return mensaje;
    }

    // Ejecuta Select simple
     public ArrayList <String []> consulta(String sql) {
        ArrayList<String []> regs = new ArrayList<String []>();

        try	{
            Conexion 		db = new Conexion();//Llamamos a nuestra Clase Conexion
            Connection		cn = db.getConnection();

            if (cn == null) {
            	regs = null;
            } else {
	            Statement  		st = cn.createStatement();
	            ResultSet		rs = st.executeQuery(sql);
	            ResultSetMetaData	rm = rs.getMetaData();
	            int 		numCols = rm.getColumnCount();

	            // Toma los títulos de las columnas
	            String[] titCols= new String[numCols];
	            for(int i=0; i<numCols; ++i)//recorre las filas de la Tabla
	                titCols[i]= rm.getColumnName(i+1);

	            // la fila 0 del vector lleva los títulos de las columnas
	            regs.add(titCols);

	            // toma las filas de la consulta
	            while(rs.next()) {
	                String[] reg= new String[numCols];

	                for(int i=0; i<numCols; i++) {
	                    reg[i] = rs.getString(i + 1);
	                }

	                regs.add(reg);
	            }

	            rs.close();
	            st.close();
	            cn.close();
            }

        } catch(SQLException e) {
            regs= null;
        } catch(Exception e) {
            regs= null;
        }

        return regs;
    }

    // Retorna una sola fila
    public String[] getFila(String sql) {
        ArrayList vector = consulta(sql);
        String[] fila = null;

        if(vector!=null)				// todo OK!
            if(vector.size()>1)				// hay filas
                fila = (String[]) vector.get(1);	// en 0 están los títulos

        return fila;
    }

    // Retorna un solo campo
     public String getCampo(String sql) {
        String[] fila = getFila(sql);
        String campo = null;

        if(fila!=null)		// hay campo
            campo = fila[0];

        return campo;
    }
    

}
