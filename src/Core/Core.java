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

    public JsonObject selectAllRealitiesInCadaster(String cadasterName) {
        JsonObject jobj = new JsonObject();
        CadasterByName cadaster = cadasterByNameSplayTree.find(new CadasterByName(new Cadaster(cadasterName)));
        if (cadaster == null) {
            jobj.addProperty("err", "Kataster sa nenašiel");
            return jobj;
        }
        ArrayList<Realty> arr = cadaster.getCadaster().getRealtiesSplayTree().inorder();
        if (arr.isEmpty()) {
            jobj.addProperty("err", "V katastry sa nenachádzajú žiadne nehnuteľnosti");
            return jobj;
        }

        JsonArray jsonArray = new JsonArray();
        for (Realty realty : arr) {
            JsonObject jo = new JsonObject();
            jo.addProperty("id", realty.getId());
            jo.addProperty("address", realty.getAddress());
            jo.addProperty("desc", realty.getDescription());
            jsonArray.add(jo);
        }
        jobj.add("realties", jsonArray);
        return jobj;
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
            addCadaster(i, "kataster" + i);
            addPerson("Marek" + i, "Bugaj" + i, "" + i, new Date());
        }
        for (int i = 0; i < 3000; i++) {
            addLatterOfOwnership(i, i);
        }
        addRealty(1, 1, 12, "Vesele", "nizny koniec");

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

    public int addOrChangeOwnershipShare(int idCadaster, int idLetter, String rc, double share) {
        System.out.println("******************************");
        Person person = peronsSplayTree.find(new Person(rc));
        if (person == null) {
            return -3;
        }

        Cadaster cadaster = cadasterSplayTree.find(new Cadaster(idCadaster));
        if (cadaster == null) {
            return -2;
        }

        LetterOfOwnershipById letter = cadaster.getLetterOfOwnershipSplayTree().find(new LetterOfOwnershipById(idLetter));
        if (letter == null) {
            return -1;
        }

        if (letter.getRealitiesSplayTree().isEmpty()) {
            return -4;
        }

        if (letter.getOwnershipSplayTree().isEmpty()) {
            letter.getOwnershipSplayTree().insert(new Ownership(person, 100)); // ak tam nik nieje potom musi mat podiel 100 %
            person.getLetterOfOwnershipByIdAndCadasterSplayTree().insert(new LetterOfOwnershipByIdAndCadaster(letter)); // pidanie majetku
            for (Ownership o : letter.getOwnershipSplayTree().inorder()) {
                System.out.println(o.getShare());
            }
            return 0;
        }

        Ownership findedOwnership = letter.getOwnershipSplayTree().find(new Ownership(person, share));
        if (findedOwnership != null) {
            if (letter.getOwnershipSplayTree().getCount() > 1) {
                double difference = share - findedOwnership.getShare();
                if (difference != 0) {
                    for (Ownership o : letter.getOwnershipSplayTree().inorder()) {
                        if (!o.getOwner().getRC().equals(rc)) {
                            o.setShare(o.getShare() - (difference / (letter.getOwnershipSplayTree().getCount() - 1)));
                        }
                    }
                }
                findedOwnership.setShare(share);
            }
        } else {
            letter.getOwnershipSplayTree().insert(new Ownership(person, share));
            person.getLetterOfOwnershipByIdAndCadasterSplayTree().insert(new LetterOfOwnershipByIdAndCadaster(letter));// pidanie majetku
            // kazdemu rovnako zmenim podiel rovnym dielom => vkladany podiel / (pocet vlastnikov - 1)
            for (Ownership o : letter.getOwnershipSplayTree().inorder()) {
                if (!o.getOwner().getRC().equals(rc)) {
                    o.setShare(o.getShare() - (share / (letter.getOwnershipSplayTree().getCount() - 1)));
                }
            }
        }
        for (Ownership o : letter.getOwnershipSplayTree().inorder()) {
            System.out.println(o.getShare());
        }

        return 0;
    }
}
