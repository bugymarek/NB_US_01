/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

import Splay.SplayTree;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Bugy
 */
public class Core {
    private SplayTree<Cadaster> cadasterSplayTree;
    private SplayTree<Person> perSplayTree;
    
    
    public static String formatDate(Date date) {
        if (date != null) {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSS");
            return df.format(date);
        }
        return "";
    }

    public String formatDateWithoutTime(Date date) {
        if (date != null) {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            return df.format(date);
        }
        return "";
    }
}
