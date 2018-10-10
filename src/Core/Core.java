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
        cadaster.getRealtiesSplayTree().insert(new Realty(1));
        cadaster.getRealtiesSplayTree().insert(new Realty(2));

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

    public String selectAllCadastersByName() {
        String result = cadasterByNameSplayTree.inorder();
        if (result == null || result.isEmpty()) {
            return null;
        }
        return result;
    }

    public String selectAllCadastersById() {
        String result = cadasterSplayTree.inorder();
        if (result == null || result.isEmpty()) {
            return null;
        }
        return result;
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
        for (int i = 0; i < 30000; i++) {
            addCadaster(i, "jano" + i);
        }

    }

    private String createHeader(String[] headerArr) {
        String result = "<tr>";
        for (String s : headerArr) {
            result += "<th>";
            result += s;
            result += "</th>";
        }
        result += "</tr>";
        return result;
    }

    private String createTableHtml(String headerHtml, String rowsHtml) {
        return "<table style=\"width:100%\">" + headerHtml + rowsHtml + "</table>";
    }
}
