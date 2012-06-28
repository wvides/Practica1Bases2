/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Mapeo;

import java.util.ArrayList;

/**
 *
 * @author omar
 */
public class hierarchy {
    String name;
    ArrayList<String[]> fields = new ArrayList<String[]>();

    public ArrayList<String[]> getFields() {
        return fields;
    }

    public void setFields(ArrayList<String[]> fields) {
        this.fields = fields;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
