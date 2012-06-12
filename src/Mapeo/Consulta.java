/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Mapeo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 *
 * @author guardian
 */
public class Consulta {
    public Consulta (){}
    
    public void set_carga_archivo(int num_archivo){
         String query  = new Sql().ejecuta("SELECT set_carga_archivo("+num_archivo+");");
         System.out.println(" \n SQL >>>> set_carga_archivo ( ) -->  "+num_archivo);
    }
    
    public void add_Tranaccion(String parametros, int num_archivo){  // Transaccion X transaccion
         String query  = new Sql ().ejecuta("SELECT set_transaccion( "+parametros+");");
         System.out.println(" \n SQL >>>> ADD_tRAnsaccion ( ) -->  "+parametros+"  , Tabla_Archivo --> "+num_archivo);
    }

    public boolean verificar_usuario (String usuario, String pass , int rol){
        ArrayList query   = new Sql ().consulta("SELECT verificar_login( '"+usuario+"' , '"+pass+ "' , "+rol+");");
        String [] salida = (String [])query.get(1);
            if ( salida[0].equals("SI")){
                return true;
            }
            else{
                //JOptionPane.showMessageDialog(null,salida[0],"Login",JOptionPane.ERROR_MESSAGE);
                return false;
            }
    }


    public boolean getUser (String correo, String clave){
            boolean salida = false;
            try {
                String rep_cliente= new Sql ().getCampo("SELECT correo FROM cliente WHERE correo=\""+correo+"\" AND clave=\""+clave+"\"");
                if (rep_cliente!=null && !rep_cliente.equals("")){
                           salida = true;
                }
            }
            catch (Exception e) {
            }
            return  salida;
        }

    public boolean addProducto (String id_prod, String nombre_prod, String categoria_prod, String precio_prod, String cantidad_prod){
        boolean salida = false;
            try {
                String rep_producto= new Sql ().getCampo("SELECT id_prod FROM producto WHERE id_prod=\""+id_prod+"\" ");
                if (rep_producto==null || rep_producto.equals("")){
                    new Sql().ejecuta("INSERT INTO producto (id_prod, nombre_prod, categoria_prod, precio_prod, cantidad_prod) VALUES (\""+id_prod+"\",\""+nombre_prod+"\", \""+categoria_prod+" \" , \""+precio_prod+" \" , \""+cantidad_prod+"\")");
                    salida = true;
                }
            }
            catch (Exception e) {
            }
        return salida;
    }
    public boolean addUser (String correo, String clave, String nombre, String dpi){
        boolean salida = false;
            try {
                String rep_cliente= new Sql ().getCampo("SELECT correo FROM cliente WHERE correo=\""+correo+"\" ");
                if (rep_cliente==null || rep_cliente.equals("")){
                    new Sql().ejecuta("INSERT INTO cliente (correo, clave, nombre, dpi) VALUES (\""+correo+"\",\""+clave+"\", \""+nombre+" \" , \""+dpi+"\")");
                    salida = true;
                }
            }
            catch (Exception e) {
            }
        return salida;
    }

    ///  >>>>>>>>>>>>>>>>> Fase 2

    public Map<String, String> getRoles(){
        Map<String,String> rol = new HashMap<String, String>();
        ArrayList<String[]> query   = new Sql().consulta("Select * from rol;");
        int a =212;
        a =87878;
        Iterator <String[]> i = query.iterator();
        if (i!=null && i.hasNext()){
            i.next();
            while (i.hasNext()){
                String[] actual = i.next();
                rol.put(actual[1],actual[0]);
            }
            return rol;
        }
        return null;
    }

    public boolean set_Pass (String usuario, String PassOld, String PassNew) {
        ArrayList<String[]> query   = new Sql().consulta("Select * from  usuario where id_usuario= '"+usuario+"'  and pin = '"+PassOld+"';");
        int a =012;
        a=98;
        if (query.size()>1){
            new Sql ().ejecuta("UPDATE usuario SET  pin='"+PassNew+"'  WHERE where id_usuario ='"+usuario+"' and pin = '"+PassOld+"' ;");
            return true;
        }
        else {
            return false;
        }
    }

    public  ArrayList <String[]> get_cuentas(String agencia,String cuentaHabiente ){
        ArrayList<String[]> query   = new Sql().consulta("Select * from cuenta where id_agencia ="+agencia+"  and id_cuentahabiente = "+cuentaHabiente+" ;");
        
        if (query!=null){
            query.remove(0); // Quitamos El encabezado de las columnas
        }
        
        return query;
    }


    public Map<String, String> getCuentas_CH(String id_ch, String id_agencia){
        Map<String,String> cuentas = new HashMap<String, String>();
        ArrayList<String[]> query = new Sql().consulta("select id_cuenta from cuenta where id_cuentahabiente = "+id_ch+" and id_agencia="+id_agencia+" and saldo >0;");
        Iterator <String[]> i = query.iterator();
        if (i!=null && i.hasNext()){
            i.next();
            while (i.hasNext()){
                String[] actual = i.next();
                cuentas.put(actual[0],actual[0]);
            }
            return cuentas;
        }
        return null;
    }

    public Map<String, String> getTodasCuentas(){
        Map<String,String> cuentas = new HashMap<String, String>();
        ArrayList<String[]> query = new Sql().consulta("select id_cuenta from cuenta where  saldo >0;");
        Iterator <String[]> i = query.iterator();
        if (i!=null && i.hasNext()){
            i.next();
            while (i.hasNext()){
                String[] actual = i.next();
                cuentas.put(actual[0],actual[0]);
            }
            return cuentas;
        }
        return null;
    }

    public boolean debitar (String cuentaOrigen, String cuentaDestino,String monto){
        
        ArrayList<String[]> queryOri = new Sql().consulta("select id_agencia , id_cuenta, id_cuentahabiente from cuenta where id_cuenta ="+cuentaOrigen+";");
        Iterator <String[]> i = queryOri.iterator();
        ArrayList<String[]> queryDes = new Sql().consulta("select id_agencia , id_cuenta, id_cuentahabiente from cuenta where id_cuenta ="+cuentaDestino+";");
        Iterator <String[]> j = queryDes.iterator();
        if (i!=null && i.hasNext() && j!=null && j.hasNext() ){
            i.next();
            j.next();
            
            String [] aux_Origen = i.next();
            String [] aux_Destino = j.next();
            
            ArrayList<String[]> query = new Sql().consulta("SELECT f_debitar( "+aux_Origen[0]+","+aux_Origen[1]+","+aux_Origen[2]+","+aux_Destino[0]+","+aux_Destino[1]+","+aux_Destino[2]+","+monto+");");
            String [] salida = (String [])query.get(1);
            int a = 1221;
            a=8787;
            if ( salida[0].equals("1")){
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }
}
