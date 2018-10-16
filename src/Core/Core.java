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

        return true;
    }

    public boolean addLatterOfOwnership(int idCadaster, int idLetter) {
        Cadaster cadaster = cadasterSplayTree.find(new Cadaster(idCadaster));
        if (cadaster == null) {
            return false;
        }

        LetterOfOwnershipById letterOfOwnership = new LetterOfOwnershipById(idLetter, cadaster);
        if (!cadaster.getLetterOfOwnershipSplayTree().insert(letterOfOwnership)) {
            return false;
        }

        return true;
    }

    public int addRealty(int idCadaster, int idLetter, int idRealty, String address, String desc) {
        Cadaster cadaster = cadasterSplayTree.find(new Cadaster(idCadaster));
        if (cadaster == null) {
            return -4;
        }

        LetterOfOwnershipById letterOfOwnershipById = cadaster.getLetterOfOwnershipSplayTree().find(new LetterOfOwnershipById(idLetter));
        if (letterOfOwnershipById == null) {
            return -3;
        }

        Realty realty = new Realty(idRealty, address, desc, letterOfOwnershipById);
        if (!letterOfOwnershipById.getRealitiesSplayTree().insert(realty)) {
            return -2;
        }

        if (!cadaster.getRealtiesSplayTree().insert(realty)) {
            return -1;
        }

        return 0;
    }

    public int addOrChangePernamentResidence(String cadasterName, String rc, int aInt) {
        Person person = peronsSplayTree.find(new Person(rc));
        if (person == null) {
            return -3;
        }
        CadasterByName cadaster = cadasterByNameSplayTree.find(new CadasterByName(new Cadaster(cadasterName)));
        if (cadaster == null) {
            return -2;
        }

        Realty realty = cadaster.getCadaster().getRealtiesSplayTree().find(new Realty(aInt));
        if (realty == null) {
            return -1;
        }
        if (person.getPernamentResidence() != null) {
            person.getPernamentResidence().getPermanentResidencePersonsSplayTree().delete(person);
        }
        person.setPernamentResidence(realty);
        realty.getPermanentResidencePersonsSplayTree().insert(person);
        return 0;
    }
}
