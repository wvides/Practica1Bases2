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
public class Dimension {

   public  ArrayList<String> Jerarquia = new ArrayList();
   public  ArrayList<String> camposllave = new ArrayList();
    
    public ArrayList<String> Campos = new ArrayList();
    
    
    queryDim dimOriginal;
    queryDim dimFinal;
    String sSelect;
    public String NombreDim; //Nombre de la dimensión
    boolean tiempo = false;

    
    public   Dimension(queryDim o)
    {
        this.dimOriginal=o;
    }
    
    public Dimension(queryDim o, ArrayList<String> j) {
        this.Jerarquia = j;

        Iterator<String> campos = j.iterator();
        while (campos.hasNext()) {
            this.Campos.add(campos.next());
        }

        this.dimOriginal = o;
    }

    public void CrearDimensionTiempo(String campo_fecha) {

        Jerarquia = new ArrayList();

        this.Jerarquia.add("año");
        this.Jerarquia.add("trimestre");
        this.Jerarquia.add("mes");
        this.Jerarquia.add("dia");

        this.NombreDim = "dim_tiempo";
        String pk = "";

        if (this.dimOriginal.Atributos.contains(campo_fecha)) {
            Iterator<String> clave = this.dimOriginal.forenKeyOriginal.campos.iterator();

            if (clave.hasNext()) {
                String tempcamposClave = this.dimOriginal.forenKeyOriginal.tablaPadre + "_" + clave.next();
                pk = "t." + tempcamposClave;
                while (clave.hasNext()) {
                    tempcamposClave = this.dimOriginal.forenKeyOriginal.tablaPadre + "_" + clave.next();
                    pk = pk + ", t." + tempcamposClave;
                }

            }

            String encabezado = pk + ", extract(year from t." + campo_fecha + ") AS año ,extract(quarter from t." + campo_fecha + ") AS trimestre ,extract(month from t." + campo_fecha + ") AS mes ,extract(day from t." + campo_fecha + ") AS dia";
            String tablas = this.dimOriginal.Nombre + " t";
            String CrearTabla = "CREATE TABLE " + this.NombreDim + " AS SELECT " + encabezado + " FROM " + tablas + ";";

            Sql a = new Sql();
            a.ejecuta(CrearTabla);
        }
        this.tiempo = true;
    }

    public void CrearDimension(String Nombre) {
        boolean bandera = true;
        this.NombreDim = "dim_" + Nombre;
        Iterator<String> campo = this.Jerarquia.iterator();
        while (campo.hasNext()) {
            if (!dimOriginal.Atributos.contains(campo.next()) && bandera) {
                bandera = false;
            }

        }
        if (bandera) {

            String encabezado = "";
            Iterator<String> campos = this.Jerarquia.iterator();
            if (campos.hasNext()) {
                encabezado = "dim." + campos.next();
                while (campos.hasNext()) {
                    encabezado = encabezado + " , dim." + campos.next();
                }

                Iterator<String> camposClave = this.dimOriginal.forenKeyOriginal.camposPadre.iterator();
                while (camposClave.hasNext()) {
                    String tcampoc=camposClave.next();
                    String tcampoClave = this.dimOriginal.forenKeyOriginal.tablaPadre + "_" + tcampoc;
                    
                    this.camposllave.add(tcampoClave);
                    this.Campos.add(tcampoClave);
                    encabezado = encabezado + ", dim." + tcampoClave;
                }
                //this.dimOriginal.forenKeyOriginal.

                this.sSelect = "SELECT  " + encabezado + " FROM " + this.dimOriginal.Nombre + " AS dim GROUP BY " + encabezado + ";";


                String CrearTabla = "CREATE TABLE dim_" + Nombre + " AS " + this.sSelect;

                Sql a = new Sql();
                a.ejecuta(CrearTabla);


            }


        }


    }
}
