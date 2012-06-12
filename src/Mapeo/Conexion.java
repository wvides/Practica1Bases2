/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Mapeo;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;

/**
    Document   : index
    Created on : 7/07/2011, 09:33:52 PM
    Author     : stuardo
 */
public class Conexion {

   //Creamos las siguiente variables

    private String	driver= "org.postgresql.Driver";
    private String 	url= "jdbc:postgresql://127.0.0.1:5432/prueba1";
    private String 	login= "postgres";
    private String 	password= "postgres";


    // creamos un metodo de tipo Connection

    public Connection getConnection() {
    Connection cn= null;
        try{
            Class.forName(driver).newInstance();
            cn= DriverManager.getConnection(url, login, password);//Conectamos a nuestra data
        } catch(SQLException e) {
            System.out.println(e.toString());
            cn= null;
        } catch(Exception e) {
            System.out.println(e.toString());
            cn= null;
        }
    

        
        return cn;
    }



}
