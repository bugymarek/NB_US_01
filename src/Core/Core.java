/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

import Splay.SplayTree;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
        generate();
    }

    public boolean addCadaster(int id, String name) {
        Cadaster cadaster = new Cadaster(id, name);

        if (!cadasterSplayTree.insert(cadaster)) {
            return false;
        }
        CadasterByName cadasterByName = new CadasterByName(cadaster);
        if (!cadasterByNameSplayTree.insert(cadasterByName)) {
            cadasterSplayTree.delete(cadaster);
            return false;
        }
        return true;
    }

    public JsonArray selectAllCadastersByName() {
        ArrayList<CadasterByName> arr = cadasterByNameSplayTree.inorder();
        if (arr.isEmpty()) {
            return null;
        }

        JsonArray jsonArray = new JsonArray();
        for (CadasterByName cadasterByName : arr) {
            JsonObject jobj = new JsonObject();
            jobj.addProperty("id", cadasterByName.getCadaster().getId());
            jobj.addProperty("name", cadasterByName.getCadaster().getName());
            jobj.addProperty("realtiesCount", cadasterByName.getCadaster().getRealtiesSplayTree().getCount());
            jobj.addProperty("letterOfOwnershipCount", cadasterByName.getCadaster().getLetterOfOwnershipSplayTree().getCount());
            jsonArray.add(jobj);
        }

        return jsonArray;
    }

    public JsonArray selectAllCadastersById() {
        ArrayList<Cadaster> arr = cadasterSplayTree.inorder();
        if (arr.isEmpty()) {
            return null;
        }

        JsonArray jsonArray = new JsonArray();
        for (Cadaster cadaster : arr) {
            JsonObject jobj = new JsonObject();
            jobj.addProperty("id", cadaster.getId());
            jobj.addProperty("name", cadaster.getName());
            jobj.addProperty("realtiesCount", cadaster.getRealtiesSplayTree().getCount());
            jobj.addProperty("letterOfOwnershipCount", cadaster.getLetterOfOwnershipSplayTree().getCount());
            jsonArray.add(jobj);
        }

        return jsonArray;
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

    private void generate() {
        for (int i = 0; i < 3000; i++) {
            addCadaster(i, "jano" + i);
        }

    }

    public boolean addPerson(String firstName, String lastName, String RC, Date birthDate) {
        Person person = new Person(RC, firstName, lastName, birthDate);
        if (!peronsSplayTree.insert(person)) {
            return false;
        } 
        ArrayList<Person> arrP = peronsSplayTree.inorder();
        for (Person person1 : arrP) {
            System.out.println(person1.toString());
        }
        
        return true;
    }
}
