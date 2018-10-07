/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

import Splay.SplayTree;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Bugy
 */
public class Core {
    private SplayTree<Cadaster> cadasterSplayTree;
    private SplayTree<CadasterByName> cadasterByNameSplayTree;
    private SplayTree<Person> peronsSplayTree;

    public Core() {
        cadasterSplayTree = new SplayTree<Cadaster>();
        cadasterByNameSplayTree = new SplayTree<CadasterByName>();
        peronsSplayTree = new SplayTree<Person>();
    }
    
    public boolean addCadaster(int id, String name){
        Cadaster cadaster = new Cadaster(id, name);
        if(!cadasterSplayTree.insert(cadaster)){
            return false;
        }
        CadasterByName cadasterByName = new CadasterByName(cadaster);
        if(!cadasterByNameSplayTree.insert(cadasterByName)){
            cadasterSplayTree.delete(cadaster);
            return false;
        }
        return true;
    }
    
    public String selectAllCadastersByName(){
       ArrayList<CadasterByName> arr = cadasterByNameSplayTree.inorder();
       Gson gson = new GsonBuilder().setPrettyPrinting().create();
       if(arr.isEmpty()){
           return null;
       }
       return gson.toJson(arr);
    }
    
    public String selectAllCadastersById(){
       ArrayList<Cadaster> arr = cadasterSplayTree.inorder();
       Gson gson = new GsonBuilder().setPrettyPrinting().create();
       if(arr.isEmpty()){
           return null;
       }
       return gson.toJson(arr);
    }
    
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
