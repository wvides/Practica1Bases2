/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Mapeo.Modelo;
import java.util.ArrayList;

/**
 *
 * @author eddytrex
 */
public class Main {
    
    public static void main(String args[])
    {
            Modelo a=new Modelo();
            a.cargarEntidades();
           // Entidad prueba=a.BuscarEntidad("agencia");
            
            
            ArrayList<String> dimensiones=a.getDimensiones("detallefactura");
            
            
            int c;
            c=1+1;
    }
    
}
