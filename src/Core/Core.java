/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

import Generators.RcGenerator;
import Splay.SplayTree;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

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

    public boolean addPerson(String firstName, String lastName, String RC, Date birthDate) {
        Person person = new Person(RC, firstName, lastName, birthDate);
        if (!peronsSplayTree.insert(person)) {
            //System.out.println(peronsSplayTree.getCount() + " false: " + person.toString());
            return false;
        }
        //System.out.println(peronsSplayTree.getCount() + " true: " + person.toString());
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

    public void generateData(int cadastersCount, int letterOfOwnershipOnCadasterCount, int ownersCount, int realtiesCountFrom, int realtiesCountTo, int personsCount,int personsCountFrom, int personsCountTo ) {
        cadasterSplayTree = new SplayTree<Cadaster>();
        cadasterByNameSplayTree = new SplayTree<CadasterByName>();
        peronsSplayTree = new SplayTree<Person>();
        Random randomGenerator = new Random();
        final String[] firstNames = {"Kr", "Ca", "Ra", "Mrok", "Cru",
            "Ray", "Bre", "Zed", "Drak", "Mor", "Jag", "Mer", "Jar", "Mjol",
            "Zork", "Mad", "Cry", "Zur", "Creo", "Azak", "Azur", "Rei", "Cro",
            "Mar", "Luk"};
        final String[] lastNames = {"Bugaj", "Gofa", "Melek", "Jonas", "Artur",
            "Demos", "Pralok", "Gabalova", "Mrtek", "Vonsak", "Egres", "Stankovianska", "Albinikova", "Anat",
            "Chrobak", "Niekto", "Nikdova", "Lahka", "Velky", "Nizky", "Stary", "Mlady", "Velka",
            "Mar", "Luk"};
        final long year = (long) 365 * 1000 * 24 * 60 * 60;
        final long day = (long) 1000 * 24 * 60 * 60;

        for (int i = 0; i < cadastersCount; i++) {

            addCadaster(getRandomId(7,9), getRandomString(randomGenerator.nextInt(10)+5, false));
        }
        System.out.println("pocet katastrov: " + cadasterSplayTree.getCount());

        for (int i = 0; i < personsCount; i++) {
            String RC = RcGenerator.generateRc();
            addPerson(firstNames[randomGenerator.nextInt(firstNames.length)],
                    lastNames[randomGenerator.nextInt(lastNames.length)],
                    RC, getDateFromRange(1900, 2010));
            
        }
        ArrayList<Person> arrPersons = peronsSplayTree.inorder();
        for (Cadaster cadaster : cadasterSplayTree.inorder()) {
            for (int i = 0; i < letterOfOwnershipOnCadasterCount; i++) {
                LetterOfOwnershipById letterOfOwnership = new LetterOfOwnershipById(getRandomId(3, 6), cadaster);
                cadaster.getLetterOfOwnershipSplayTree().insert(letterOfOwnership);
            
                int countRealty = randomGenerator.nextInt(realtiesCountTo-realtiesCountFrom) + realtiesCountFrom;
                for (int j = 0; j < countRealty; j++) {
                    int idRealty = getRandomId(5, 9);
                    Realty realty = new  Realty(idRealty, getRandomString(20, false), getRandomString(30, true), letterOfOwnership);
                    //pridat na nehnutelnost obyvatelov
                    int countResidens = randomGenerator.nextInt(personsCountTo-personsCountFrom) + personsCountFrom;
                   
                    for (int k = 0; k < countResidens; k++) {
                        System.out.println(arrPersons.size()); 
                        if(arrPersons.isEmpty()) break;
                        int index = randomGenerator.nextInt(arrPersons.size());
                        Person person = arrPersons.get(index);
                        person.setPernamentResidence(realty);
                        realty.getPermanentResidencePersonsSplayTree().insert(person);
                        arrPersons.remove(index);
                    }
                    cadaster.getRealtiesSplayTree().insert(realty);
                    letterOfOwnership.getRealitiesSplayTree().insert(realty);
                }
            }
        }

//        addRealty(
//                1, 1, 12, "Vesele", "nizny koniec");
    }

    private String getRandomString(int length, boolean numbers) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if(numbers){
            SALTCHARS += "1234567890";
        }      
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    private Date getRandomDate(int multiple) {
        Random randomGenerator = new Random();
        int offset = randomGenerator.nextInt(99999 * multiple);
        return new Date(System.currentTimeMillis() - offset);
    }

    private int getRandomId(int fromNumeralCount, int toNumeralCount) {
        Random randomGenerator = new Random();
        int lenth = randomGenerator.nextInt(toNumeralCount-fromNumeralCount) + fromNumeralCount;
        String strNumper = new String();
        for (int j = 0; j < lenth; j++) {
            if(j==0){
                strNumper += randomGenerator.nextInt(8) +1;
            }else {
                strNumper += randomGenerator.nextInt(9);
            }
        }
        return Integer.parseInt(strNumper);
    }
    
    //zdroj https://stackoverflow.com/questions/3985392/generate-random-date-of-birth
    private Date getDateFromRange(int start, int end){
        GregorianCalendar gc = new GregorianCalendar();

        int year = (start + (int)Math.round(Math.random() * (end - start)));

        gc.set(gc.YEAR, year);

        int dayOfYear =  (1 + (int)Math.round(Math.random() * (gc.getActualMaximum(gc.DAY_OF_YEAR) - 1)));

        gc.set(gc.DAY_OF_YEAR, dayOfYear);

        return gc.getTime();
    }    
}
