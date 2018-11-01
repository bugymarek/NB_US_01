/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

import Generators.RcGenerator;
import Splay.SplayTree;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
    private Random generatorSeeds;

    public Core() {
        cadasterSplayTree = new SplayTree<Cadaster>();
        cadasterByNameSplayTree = new SplayTree<CadasterByName>();
        peronsSplayTree = new SplayTree<Person>();
        generatorSeeds = new Random(500);
        RcGenerator.setRandomSeed(generatorSeeds.nextInt());
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

    public JsonObject selectAllRealitiesOfOwnerByCadaster(String Rc, int cadasterId) {
        JsonObject jobj = new JsonObject();
        Person person = peronsSplayTree.find(new Person(Rc));
        if (person == null) {
            jobj.addProperty("err", "Osoba sa nenašla");
            return jobj;
        }

        ArrayList<LetterOfOwnershipByIdAndCadaster> arr = person.getLetterOfOwnershipByIdAndCadasterSplayTree().inorder();
        if (arr.isEmpty()) {
            jobj.addProperty("err", "Osoba nevlastní žiadný majetok");
            return jobj;
        }
        JsonArray jsonArray = new JsonArray();
        for (LetterOfOwnershipByIdAndCadaster letter : arr) {
            if (letter.getLetterOfOwnershipById().getCadaster().getId() == cadasterId) {
                JsonObject jo = new JsonObject();
                jo.addProperty("idLetter", letter.getLetterOfOwnershipById().getId());
                jo.addProperty("realtiesCount", letter.getLetterOfOwnershipById().getRealitiesSplayTree().getCount());
                JsonArray jsonRealtiesArray = new JsonArray();
                for (Realty r : letter.getLetterOfOwnershipById().getRealitiesSplayTree().inorder()) {
                    JsonObject joRealty = new JsonObject();
                    joRealty.addProperty("id", r.getId());
                    joRealty.addProperty("address", r.getAddress());
                    joRealty.addProperty("desc", r.getDescription());
                    jsonRealtiesArray.add(joRealty);
                }
                jo.add("realties", jsonRealtiesArray);
                Ownership ownership = letter.getLetterOfOwnershipById().getOwnershipSplayTree().find(new Ownership(new Person(Rc)));
                jo.addProperty("share", ownership != null ? "" + ownership.getShare() : "");
                jsonArray.add(jo);
            }

        }
        jobj.add("realtiesOfOwner", jsonArray);
        return jobj;
    }

    public JsonObject selectAllRealitiesOfOwner(String Rc) {
        JsonObject jobj = new JsonObject();
        Person person = peronsSplayTree.find(new Person(Rc));
        if (person == null) {
            jobj.addProperty("err", "Osoba sa nenašla");
            return jobj;
        }

        ArrayList<LetterOfOwnershipByIdAndCadaster> arr = person.getLetterOfOwnershipByIdAndCadasterSplayTree().inorder();
        if (arr.isEmpty()) {
            jobj.addProperty("err", "Osoba nevlastní žiadný majetok");
            return jobj;
        }
        JsonArray jsonArray = new JsonArray();
        for (LetterOfOwnershipByIdAndCadaster letter : arr) {
            JsonObject jo = new JsonObject();
            jo.addProperty("idCadaster", letter.getLetterOfOwnershipById().getCadaster().getId());
            jo.addProperty("idLetter", letter.getLetterOfOwnershipById().getId());
            jo.addProperty("realtiesCount", letter.getLetterOfOwnershipById().getRealitiesSplayTree().getCount());
            JsonArray jsonRealtiesArray = new JsonArray();
            for (Realty r : letter.getLetterOfOwnershipById().getRealitiesSplayTree().inorder()) {
                JsonObject joRealty = new JsonObject();
                joRealty.addProperty("id", r.getId());
                joRealty.addProperty("address", r.getAddress());
                joRealty.addProperty("desc", r.getDescription());
                jsonRealtiesArray.add(joRealty);
            }
            jo.add("realties", jsonRealtiesArray);
            Ownership ownership = letter.getLetterOfOwnershipById().getOwnershipSplayTree().find(new Ownership(new Person(Rc)));
            jo.addProperty("share", ownership != null ? "" + ownership.getShare() : "");
            jsonArray.add(jo);
        }
        jobj.add("realtiesOfOwner", jsonArray);
        return jobj;
    }

    public static String formatDate(Date date) {
        if (date != null) {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSS");
            return df.format(date);
        }
        return "";
    }

    public static String formatDateWithoutTime(Date date) {
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

    public int addOrChangePernamentResidence(String cadasterName, String rc, int share) {
        Person person = peronsSplayTree.find(new Person(rc));
        if (person == null) {
            return -3;
        }
        CadasterByName cadaster = cadasterByNameSplayTree.find(new CadasterByName(new Cadaster(cadasterName)));
        if (cadaster == null) {
            return -2;
        }

        Realty realty = cadaster.getCadaster().getRealtiesSplayTree().find(new Realty(share));
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

    public JsonObject addOrChangeOwnershipShare(int idCadaster, int idLetter, String rc, double share) {
        JsonObject jobj = new JsonObject();
        Person person = peronsSplayTree.find(new Person(rc));
        if (person == null) {
            jobj.addProperty("err", "Neúspešne zapísanie/zmena majetkového podielu majiteľa. Osoba sa nenašla.");
            return jobj;
        }

        Cadaster cadaster = cadasterSplayTree.find(new Cadaster(idCadaster));
        if (cadaster == null) {
            jobj.addProperty("err", "Neúspešne zapísanie/zmena majetkového podielu majiteľa. Kataster sa nenašiel.");
            return jobj;
        }

        LetterOfOwnershipById letter = cadaster.getLetterOfOwnershipSplayTree().find(new LetterOfOwnershipById(idLetter));
        if (letter == null) {
            jobj.addProperty("err", "Neúspešne zapísanie/zmena majetkového podielu majiteľa. List sa nenašiel.");
            return jobj;
        }

        if (letter.getRealitiesSplayTree().isEmpty()) {
            jobj.addProperty("err", "Neúspešne zapísanie/zmena majetkového podielu majiteľa. List vlastníctva neobsahuje nehnuteľnosti.");
            return jobj;
        }

        if (letter.getOwnershipSplayTree().isEmpty()) {
            letter.getOwnershipSplayTree().insert(new Ownership(person, 100)); // ak tam nik nieje potom musi mat podiel 100 %
            person.getLetterOfOwnershipByIdAndCadasterSplayTree().insert(new LetterOfOwnershipByIdAndCadaster(letter)); // pidanie majetku
            return jobj;
        }

        Ownership findedOwnership = letter.getOwnershipSplayTree().find(new Ownership(person, share));
        if (findedOwnership != null && letter.getOwnershipSplayTree().getCount() == 1) {
            findedOwnership.setShare(100);// ak je tam len hladany majitel  potom musi mat podiel 100 %
            return jobj;
        }

        JsonArray jsonArray = new JsonArray();
        for (Ownership ownership : letter.getOwnershipSplayTree().inorder()) {
            if (!ownership.getOwner().getRC().equals(rc)) {
                JsonObject jo = new JsonObject();
                jo.addProperty("rc", ownership.getOwner().getRC());
                jo.addProperty("share", String.format("%.2f", ownership.getShare()));
                jsonArray.add(jo);
            }
        }
        JsonObject editedOwner = new JsonObject();
        editedOwner.addProperty("rc", rc);
        editedOwner.addProperty("share", String.format("%.2f", share));
        jobj.add("editedOwner", editedOwner);
        jobj.add("ownerships", jsonArray);
        return jobj;
    }

    public void generateData(int cadastersCount, int letterOfOwnershipOnCadasterCount, int ownersCount, int realtiesCountFrom, int realtiesCountTo, int personsCount, int personsCountFrom, int personsCountTo, int ownershipCountFrom, int ownershipCountTo) {
        cadasterSplayTree = new SplayTree<Cadaster>();
        cadasterByNameSplayTree = new SplayTree<CadasterByName>();
        peronsSplayTree = new SplayTree<Person>();
        Random randomGenerator = new Random(generatorSeeds.nextInt());
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

            addCadaster(getRandomId(7, 9), getRandomString(randomGenerator.nextInt(10) + 5, false));
        }
        System.out.println("pocet katastrov: " + cadasterSplayTree.getCount());

        for (int i = 0; i < personsCount; i++) {
            String RC = RcGenerator.generateRc();
            addPerson(firstNames[randomGenerator.nextInt(firstNames.length)],
                    lastNames[randomGenerator.nextInt(lastNames.length)],
                    RC, getDateFromRange(1900, 2010));
            if (i % 1000 == 0) {
                System.out.println(RC);
            }
        }
        System.out.println("pocet osob: " + peronsSplayTree.getCount());

        ArrayList<Person> arrPersons = peronsSplayTree.inorder();
        ArrayList<Person> arrOwners = new ArrayList<>(arrPersons);
        ArrayList<Cadaster> arrCadasters = cadasterSplayTree.inorder();
        LetterOfOwnershipById letterOfOwnership;
        int countRealty;
        int idRealty;
        Realty realty;
        int countResidens;
        int countOwners;
        int index;
        for (int c = 0; c < arrCadasters.size(); c++) {
            for (int i = 0; i < letterOfOwnershipOnCadasterCount; i++) {
                letterOfOwnership = new LetterOfOwnershipById(getRandomId(4, 6), arrCadasters.get(c));
                arrCadasters.get(c).getLetterOfOwnershipSplayTree().insert(letterOfOwnership);
                countRealty = randomGenerator.nextInt(realtiesCountTo - realtiesCountFrom) + realtiesCountFrom;
                for (int j = 0; j < countRealty; j++) {
                    idRealty = getRandomId(5, 9);
                    realty = new Realty(idRealty, getRandomString(20, false), getRandomString(30, true), letterOfOwnership);

                    //pridat na nehnutelnost obyvatelov
                    countResidens = randomGenerator.nextInt(personsCountTo - personsCountFrom) + personsCountFrom;
                    for (int k = 0; k < countResidens; k++) {
                        if (arrPersons.isEmpty()) {
                            break;
                        }
                        index = randomGenerator.nextInt(arrPersons.size());
                        Person person = arrPersons.get(index);
                        person.setPernamentResidence(realty);
                        realty.getPermanentResidencePersonsSplayTree().insert(person);
                        arrPersons.remove(index);
                    }

                    arrCadasters.get(c).getRealtiesSplayTree().insert(realty);
                    letterOfOwnership.getRealitiesSplayTree().insert(realty);
                }

                // pridanie majetkovych podielov
                countOwners = randomGenerator.nextInt(ownershipCountTo - ownershipCountFrom) + ownershipCountFrom;
                for (int o = 0; o < countOwners; o++) {
                    index = randomGenerator.nextInt(arrOwners.size());
                    Ownership ownership = new Ownership(arrOwners.get(index), 100.0 / (double) countOwners);
                    letterOfOwnership.getOwnershipSplayTree().insert(ownership);
                    arrOwners.get(index).getLetterOfOwnershipByIdAndCadasterSplayTree().insert(new LetterOfOwnershipByIdAndCadaster(letterOfOwnership)); // pidanie majetku
                    //addOrChangeOwnershipShare(arrCadasters.get(c).getId(), letterOfOwnership.getId(), arrOwners.get(index).getRC(), 100.0 / (double) countOwners);
                }
//                System.out.println("*************  " + letterOfOwnership.getId() + " ***************");
//                for (Ownership o : letterOfOwnership.getOwnershipSplayTree().inorder()) {
//                    System.out.println(o.getShare());
//                }
            }
            //System.out.println("Cadaster " + c);
        }

//        addRealty(
//                1, 1, 12, "Vesele", "nizny koniec");
    }

    private String getRandomString(int length, boolean numbers) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if (numbers) {
            SALTCHARS += "1234567890";
        }
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random(generatorSeeds.nextInt());
        int index;
        while (salt.length() < length) { // length of the random string.
            index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    private Date getRandomDate(int multiple) {
        Random randomGenerator = new Random(generatorSeeds.nextInt());
        int offset = randomGenerator.nextInt(99999 * multiple);
        return new Date(System.currentTimeMillis() - offset);
    }

    private int getRandomId(int fromNumeralCount, int toNumeralCount) {
        Random randomGenerator = new Random(generatorSeeds.nextInt());
        int lenth = randomGenerator.nextInt(toNumeralCount - fromNumeralCount) + fromNumeralCount;
        String strNumper = new String();
        for (int j = 0; j < lenth; j++) {
            if (j == 0) {
                strNumper += randomGenerator.nextInt(8) + 1;
            } else {
                strNumper += randomGenerator.nextInt(9);
            }
        }
        return Integer.parseInt(strNumper);
    }

    //zdroj https://stackoverflow.com/questions/3985392/generate-random-date-of-birth
    private Date getDateFromRange(int start, int end) {
        GregorianCalendar gc = new GregorianCalendar();

        int year = (start + (int) Math.round(Math.random() * (end - start)));

        gc.set(gc.YEAR, year);

        int dayOfYear = (1 + (int) Math.round(Math.random() * (gc.getActualMaximum(gc.DAY_OF_YEAR) - 1)));

        gc.set(gc.DAY_OF_YEAR, dayOfYear);

        return gc.getTime();
    }

    public JsonObject setOwnershipShares(int idCadaster, int idLetter, JsonObject owners) {
        String rc = owners.get("editedOwner").getAsJsonObject().get("rc").getAsString();
        Double share = owners.get("editedOwner").getAsJsonObject().get("share").getAsDouble();
        JsonObject jobj = new JsonObject();
        Person person = peronsSplayTree.find(new Person(rc));
        if (person == null) {
            jobj.addProperty("err", "Neúspešne zapísanie/zmena majetkového podielu majiteľa. Osoba sa nenašla.");
            return jobj;
        }

        Cadaster cadaster = cadasterSplayTree.find(new Cadaster(idCadaster));
        if (cadaster == null) {
            jobj.addProperty("err", "Neúspešne zapísanie/zmena majetkového podielu majiteľa. Kataster sa nenašiel.");
            return jobj;
        }

        LetterOfOwnershipById letter = cadaster.getLetterOfOwnershipSplayTree().find(new LetterOfOwnershipById(idLetter));
        if (letter == null) {
            jobj.addProperty("err", "Neúspešne zapísanie/zmena majetkového podielu majiteľa. List sa nenašiel.");
            return jobj;
        }

        if (letter.getRealitiesSplayTree().isEmpty()) {
            jobj.addProperty("err", "Neúspešne zapísanie/zmena majetkového podielu majiteľa. List vlastníctva neobsahuje nehnuteľnosti.");
            return jobj;
        }

        if (letter.getOwnershipSplayTree().isEmpty()) {
            letter.getOwnershipSplayTree().insert(new Ownership(person, 100)); // ak tam nik nieje potom musi mat podiel 100 %
            person.getLetterOfOwnershipByIdAndCadasterSplayTree().insert(new LetterOfOwnershipByIdAndCadaster(letter)); // pidanie majetku
            return jobj;
        }

        Ownership findedOwnership = letter.getOwnershipSplayTree().find(new Ownership(person, share));
        if (findedOwnership == null) {
            letter.getOwnershipSplayTree().insert(new Ownership(person, share));
            person.getLetterOfOwnershipByIdAndCadasterSplayTree().insert(new LetterOfOwnershipByIdAndCadaster(letter));// pidanie majetku
        } else {
            findedOwnership.setShare(share);
            for (JsonElement jsonElement : owners.get("ownerships").getAsJsonArray()) {
                JsonObject owner = (JsonObject) jsonElement;
                Ownership finded = letter.getOwnershipSplayTree().find(new Ownership(new Person(owner.get("rc").getAsString())));
                if (finded != null) {
                    finded.setShare(owner.get("share").getAsDouble());
                }
            }
        }
        return jobj;
    }

    public JsonObject selectPernamentResidenceByRC(String Rc) {
        JsonObject jobj = new JsonObject();
        Person person = peronsSplayTree.find(new Person(Rc));
        if (person == null) {
            jobj.addProperty("err", "Osoba sa nenašla.");
            return jobj;
        }
        if (person.getPernamentResidence() == null) {
            jobj.addProperty("err", "Osoba nemá trvalé bydlisko.");
            return jobj;
        }
        Realty realty = person.getPernamentResidence();
        jobj.addProperty("id", realty.getId());
        jobj.addProperty("address", realty.getAddress());
        jobj.addProperty("desc", realty.getDescription());
        jobj.addProperty("idLetter", realty.getLetterOfOwnership() != null ? "" + realty.getLetterOfOwnership().getId() : "nezapisaná");
        jobj.addProperty("idCadaster", (realty.getLetterOfOwnership() != null && realty.getLetterOfOwnership().getCadaster() != null) ? "" + realty.getLetterOfOwnership().getCadaster().getId() : "nepoznám");
        JsonArray jsonArray = new JsonArray();
        for (Person p : realty.getPermanentResidencePersonsSplayTree().inorder()) {
            JsonObject jo = new JsonObject();
            jo.addProperty("rc", p.getRC());
            jo.addProperty("firstName", p.getFirstName());
            jo.addProperty("lastName", p.getLastName());
            jo.addProperty("birthDate", formatDateWithoutTime(p.getBirthDate()));
            jsonArray.add(jo);
        }
        jobj.add("permanentResidencePersons", jsonArray);
        return jobj;
    }

    public JsonObject selectPermanentResidencePersons(int cadasterId, int letterId, int realtyId) {
        JsonObject jobj = new JsonObject();
        Cadaster cadaster = cadasterSplayTree.find(new Cadaster(cadasterId));
        if (cadaster == null) {
            jobj.addProperty("err", "Kataster sa nenašiel.");
            return jobj;
        }

        LetterOfOwnershipById letter = cadaster.getLetterOfOwnershipSplayTree().find(new LetterOfOwnershipById(letterId));
        if (letter == null) {
            jobj.addProperty("err", "List sa v katastri nenašiel.");
            return jobj;
        }

        if (letter.getRealitiesSplayTree().isEmpty()) {
            jobj.addProperty("err", "List vlastníctva neobsahuje nehnuteľnosti.");
            return jobj;
        }

        Realty realty = letter.getRealitiesSplayTree().find(new Realty(realtyId));
        if (realty == null) {
            jobj.addProperty("err", "V zadanom katastri na liste vlastníctva sa nehnuteľnosť nenašla.");
            return jobj;
        }
        JsonArray jsonArray = new JsonArray();
        for (Person p : realty.getPermanentResidencePersonsSplayTree().inorder()) {
            JsonObject jo = new JsonObject();
            jo.addProperty("rc", p.getRC());
            jo.addProperty("firstName", p.getFirstName());
            jo.addProperty("lastName", p.getLastName());
            jo.addProperty("birthDate", formatDateWithoutTime(p.getBirthDate()));
            jsonArray.add(jo);
        }
        jobj.add("permanentResidencePersons", jsonArray);
        return jobj;

    }

    public JsonObject selectRealtiesByRealtyId_CadasterIdOrName(int realtyId, String cadasterIdOrName, boolean byId) {
        JsonObject jobj = new JsonObject();
        Cadaster cadaster = null;
        if (byId) {
            cadaster = cadasterSplayTree.find(new Cadaster(Integer.parseInt(cadasterIdOrName)));
            if (cadaster == null) {
                jobj.addProperty("err", "Kataster sa nenašiel.");
                return jobj;
            }
        } else {
            CadasterByName cadasterByName = cadasterByNameSplayTree.find(new CadasterByName(new Cadaster(cadasterIdOrName)));
            if (cadasterByName == null) {
                jobj.addProperty("err", "Kataster sa nenašiel.");
                return jobj;
            }
            cadaster = cadasterByName.getCadaster();
        }

        if (cadaster.getRealtiesSplayTree().isEmpty()) {
            jobj.addProperty("err", "V katastri sa nenachádzajú žíadne nehnuteľnosti.");
            return jobj;
        }

        Realty realty = cadaster.getRealtiesSplayTree().find(new Realty(realtyId));
        if (realty == null) {
            jobj.addProperty("err", "Zadaná nehnuteľnosť sa v katastri nenachádza.");
            return jobj;
        }

        jobj.addProperty("id", realty.getId());
        jobj.addProperty("address", realty.getAddress());
        jobj.addProperty("desc", realty.getDescription());
        jobj.addProperty("idLetter", realty.getLetterOfOwnership() != null ? "" + realty.getLetterOfOwnership().getId() : "nezapisaná");
        jobj.addProperty("idCadaster", (realty.getLetterOfOwnership() != null && realty.getLetterOfOwnership().getCadaster() != null) ? "" + realty.getLetterOfOwnership().getCadaster().getId() : "nezapisaná");

        JsonArray jsonArrayPersons = new JsonArray();
        for (Person p : realty.getPermanentResidencePersonsSplayTree().inorder()) {
            JsonObject jo = new JsonObject();
            jo.addProperty("rc", p.getRC());
            jo.addProperty("firstName", p.getFirstName());
            jo.addProperty("lastName", p.getLastName());
            jo.addProperty("birthDate", formatDateWithoutTime(p.getBirthDate()));
            jsonArrayPersons.add(jo);
        }
        jobj.add("permanentResidencePersons", jsonArrayPersons);

        if (realty.getLetterOfOwnership() != null) {
            JsonObject jsonObjectLetter = new JsonObject();
            jsonObjectLetter.addProperty("idLetter", realty.getLetterOfOwnership().getId());
            jsonObjectLetter.addProperty("idCadaster", (realty.getLetterOfOwnership() != null && realty.getLetterOfOwnership().getCadaster() != null) ? "" + realty.getLetterOfOwnership().getCadaster().getId() : "nezapisaná");
            jsonObjectLetter.addProperty("nameCadaster", (realty.getLetterOfOwnership() != null && realty.getLetterOfOwnership().getCadaster() != null) ? "" + realty.getLetterOfOwnership().getCadaster().getName() : "nezapisaná");
            if (!realty.getLetterOfOwnership().getRealitiesSplayTree().isEmpty()) {
                JsonArray jsonArrayRealties = new JsonArray();
                for (Realty r : realty.getLetterOfOwnership().getRealitiesSplayTree().inorder()) {
                    JsonObject joRealty = new JsonObject();
                    joRealty.addProperty("id", r.getId());
                    joRealty.addProperty("address", r.getAddress());
                    joRealty.addProperty("desc", r.getDescription());
                    jsonArrayRealties.add(joRealty);
                }
                jsonObjectLetter.add("realties", jsonArrayRealties);
            }
            if (!realty.getLetterOfOwnership().getOwnershipSplayTree().isEmpty()) {
                JsonArray jsonArrayOwnerships = new JsonArray();
                for (Ownership ownership : realty.getLetterOfOwnership().getOwnershipSplayTree().inorder()) {
                    JsonObject jo = new JsonObject();
                    jo.addProperty("rc", ownership.getOwner().getRC());
                    jo.addProperty("share", String.format("%.2f", ownership.getShare()));
                    jsonArrayOwnerships.add(jo);
                }
                jsonObjectLetter.add("ownerships", jsonArrayOwnerships);
            }

            jobj.add("letter", jsonObjectLetter);
        }

        return jobj;
    }

    public JsonObject selectLetterOfOwnershipByLetterId_CadasterIdOrName(int letterId, String cadasterIdOrName, boolean byId) {
        JsonObject jobj = new JsonObject();
        Cadaster cadaster = null;
        if (byId) {
            cadaster = cadasterSplayTree.find(new Cadaster(Integer.parseInt(cadasterIdOrName)));
            if (cadaster == null) {
                jobj.addProperty("err", "Kataster sa nenašiel.");
                return jobj;
            }
        } else {
            CadasterByName cadasterByName = cadasterByNameSplayTree.find(new CadasterByName(new Cadaster(cadasterIdOrName)));
            if (cadasterByName == null) {
                jobj.addProperty("err", "Kataster sa nenašiel.");
                return jobj;
            }
            cadaster = cadasterByName.getCadaster();
        }

        if (cadaster.getLetterOfOwnershipSplayTree().isEmpty()) {
            jobj.addProperty("err", "V katastri sa nenachádzajú žíadne listy vltastnítva.");
            return jobj;
        }

        LetterOfOwnershipById letter = cadaster.getLetterOfOwnershipSplayTree().find(new LetterOfOwnershipById(letterId));
        if (letter == null) {
            jobj.addProperty("err", "Zadaný list vlastníctva sa v katastri nenachádza.");
            return jobj;
        }

        JsonObject jsonObjectLetter = new JsonObject();
        jsonObjectLetter.addProperty("idLetter", letter.getId());
        jsonObjectLetter.addProperty("idCadaster", (letter.getCadaster() != null) ? "" + letter.getCadaster().getId() : "nezapisaná");
        jsonObjectLetter.addProperty("nameCadaster", (letter.getCadaster() != null) ? "" + letter.getCadaster().getName() : "nezapisaná");
        if (!letter.getRealitiesSplayTree().isEmpty()) {
            JsonArray jsonArrayRealties = new JsonArray();
            for (Realty r : letter.getRealitiesSplayTree().inorder()) {
                JsonObject joRealty = new JsonObject();
                joRealty.addProperty("id", r.getId());
                joRealty.addProperty("address", r.getAddress());
                joRealty.addProperty("desc", r.getDescription());
                jsonArrayRealties.add(joRealty);
            }
            jsonObjectLetter.add("realties", jsonArrayRealties);
        }
        if (!letter.getOwnershipSplayTree().isEmpty()) {
            JsonArray jsonArrayOwnerships = new JsonArray();
            for (Ownership ownership : letter.getOwnershipSplayTree().inorder()) {
                JsonObject jo = new JsonObject();
                jo.addProperty("rc", ownership.getOwner().getRC());
                jo.addProperty("firstName", ownership.getOwner().getFirstName());
                jo.addProperty("lastName", ownership.getOwner().getLastName());
                jo.addProperty("birthDate", formatDateWithoutTime(ownership.getOwner().getBirthDate()));
                jo.addProperty("share", String.format("%.2f", ownership.getShare()));
                jsonArrayOwnerships.add(jo);
            }
            jsonObjectLetter.add("ownerships", jsonArrayOwnerships);
        }

        jobj.add("letter", jsonObjectLetter);
        return jobj;
    }

    public JsonObject changeOwner(String rcOwner, String rcNewOwner, int idCadaster, int idRealty) {
        JsonObject jobj = new JsonObject();
        Person owner = peronsSplayTree.find(new Person(rcOwner));
        if (owner == null) {
            jobj.addProperty("err", "Majiteľ sa nenašiel.");
            return jobj;
        }

        Person newOwner = peronsSplayTree.find(new Person(rcNewOwner));
        if (newOwner == null) {
            jobj.addProperty("err", "Nový majiteľ sa nenašiel.");
            return jobj;
        }

        Cadaster cadaster = cadasterSplayTree.find(new Cadaster(idCadaster));
        if (cadaster == null) {
            jobj.addProperty("err", "Kataster sa nenašiel.");
            return jobj;
        }

        Realty realty = cadaster.getRealtiesSplayTree().find(new Realty(idRealty));
        if (realty == null) {
            jobj.addProperty("err", "Nehnuteľnosť sa nenachádza v zadanom katastri.");
            return jobj;
        }

        LetterOfOwnershipById letter = realty.getLetterOfOwnership();
        if (letter == null) {
            jobj.addProperty("err", "Nehnuteľnosť nieje zapísaná na liste vlastníctva.");
            return jobj;
        }

        Ownership ownership = letter.getOwnershipSplayTree().find(new Ownership(owner));
        if (ownership == null) {
            jobj.addProperty("err", "Majiteť nevlastní / nemá podiel na danej nehnuteľnosti.");
            return jobj;
        }

        letter.getOwnershipSplayTree().delete(ownership);// odstranim majitela z podielov
        ownership.getOwner().getLetterOfOwnershipByIdAndCadasterSplayTree().delete(new LetterOfOwnershipByIdAndCadaster(letter)); // odsratnim list vlastnictva z jeho majetku

        Ownership newOwnership = letter.getOwnershipSplayTree().find(new Ownership(newOwner));
        if (newOwnership != null) { // ak sa tam uz nachadza tak mu pripocitaj podiel
            newOwnership.setShare(newOwnership.getShare() + ownership.getShare());
            jobj.addProperty("suc", "Úspešná zmena majiteľa. Novému majiteľovi sa pripočítal podiel starého majiteľa.");
        } else {
            letter.getOwnershipSplayTree().insert(new Ownership(newOwner, ownership.getShare()));
            newOwner.getLetterOfOwnershipByIdAndCadasterSplayTree().insert(new LetterOfOwnershipByIdAndCadaster(letter)); // pridam list do jeho majetku
            jobj.addProperty("suc", "Úspešná zmena majiteľa.");
        }

        return jobj;
    }

    public JsonObject deleteOwnership(String rc, int cadasterId, int letterId) {
        JsonObject jobj = new JsonObject();
        Person person = peronsSplayTree.find(new Person(rc));
        if (person == null) {
            jobj.addProperty("err", "Osoba sa nenašiel.");
            return jobj;
        }

        Cadaster cadaster = cadasterSplayTree.find(new Cadaster(cadasterId));
        if (cadaster == null) {
            jobj.addProperty("err", "Kataster sa nenašiel.");
            return jobj;
        }

        if (cadaster.getLetterOfOwnershipSplayTree().isEmpty()) {
            jobj.addProperty("err", "Kataster neobsahuje žiadne listy vlastnáctva.");
            return jobj;
        }

        LetterOfOwnershipById letter = cadaster.getLetterOfOwnershipSplayTree().find(new LetterOfOwnershipById(letterId));
        if (letter == null) {
            jobj.addProperty("err", "V katastri sa nenachádza list vlastníctva.");
            return jobj;
        }

        Ownership ownership = letter.getOwnershipSplayTree().find(new Ownership(person));
        if (ownership == null) {
            jobj.addProperty("err", "Osoba nevlastní žiadny podiel.");
            return jobj;
        }

        letter.getOwnershipSplayTree().delete(ownership);

        // rozdel jeho podiel, zvysnym majitelom rovnym dielom
        for (Ownership o : letter.getOwnershipSplayTree().inorder()) {
            o.setShare(o.getShare() + (ownership.getShare() / (double) letter.getOwnershipSplayTree().getCount()));
        }
        // odstran list z jeho majetku
        person.getLetterOfOwnershipByIdAndCadasterSplayTree().delete(new LetterOfOwnershipByIdAndCadaster(letter));
        jobj.addProperty("suc", "Úspešné odstránenié majetkového podielu");
        return jobj;
    }

    public JsonObject deleteLetter(int cadasterId, int letterId, int newLetterId) {
        JsonObject jobj = new JsonObject();

        Cadaster cadaster = cadasterSplayTree.find(new Cadaster(cadasterId));
        if (cadaster == null) {
            jobj.addProperty("err", "Kataster sa nenašiel.");
            return jobj;
        }

        if (cadaster.getLetterOfOwnershipSplayTree().isEmpty()) {
            jobj.addProperty("err", "Kataster neobsahuje žiadne listy vlastnáctva.");
            return jobj;
        }

        LetterOfOwnershipById letter = cadaster.getLetterOfOwnershipSplayTree().find(new LetterOfOwnershipById(letterId));
        if (letter == null) {
            jobj.addProperty("err", "V katastri sa nenachádza list vlastníctva.");
            return jobj;
        }

        LetterOfOwnershipById newLetter = new LetterOfOwnershipById(newLetterId);
        LetterOfOwnershipById findedNewLetter = cadaster.getLetterOfOwnershipSplayTree().find(newLetter);
        if (findedNewLetter != null) {
            jobj.addProperty("err", "V katastri sa už nachádza číslo nového listu vlastníctva.");
            return jobj;
        }

        newLetter.setCadaster(cadaster);
        newLetter.setOwnershipSplayTree(letter.getOwnershipSplayTree());
        newLetter.setRealitiesSplayTree(letter.getRealitiesSplayTree());

        // nastav vsetky smerniky nehnutelnosti na novy list
        for (Realty realty : letter.getRealitiesSplayTree().inorder()) {
            realty.setLetterOfOwnership(newLetter);
        }

        // nastav majetok každeho podielnika ukazuje na novy list
        for (Ownership ownership : letter.getOwnershipSplayTree().inorder()) {
            ownership.getOwner().getLetterOfOwnershipByIdAndCadasterSplayTree().delete(new LetterOfOwnershipByIdAndCadaster(letter));
            ownership.getOwner().getLetterOfOwnershipByIdAndCadasterSplayTree().insert(new LetterOfOwnershipByIdAndCadaster(newLetter));
        }

        cadaster.getLetterOfOwnershipSplayTree().delete(letter);
        cadaster.getLetterOfOwnershipSplayTree().insert(newLetter);

        jobj.addProperty("suc", "Úspešné odstránenié listu vlastníctva");
        return jobj;
    }

    public JsonObject deleteRealty(int cadasterId, int realtyId, int letterId) {
        JsonObject jobj = new JsonObject();

        Cadaster cadaster = cadasterSplayTree.find(new Cadaster(cadasterId));
        if (cadaster == null) {
            jobj.addProperty("err", "Kataster sa nenašiel.");
            return jobj;
        }

        if (cadaster.getLetterOfOwnershipSplayTree().isEmpty()) {
            jobj.addProperty("err", "Kataster neobsahuje žiadne listy vlastnáctva.");
            return jobj;
        }

        LetterOfOwnershipById letter = cadaster.getLetterOfOwnershipSplayTree().find(new LetterOfOwnershipById(letterId));
        if (letter == null) {
            jobj.addProperty("err", "V katastri sa nenachádza list vlastníctva.");
            return jobj;
        }

        Realty realty = letter.getRealitiesSplayTree().find(new Realty(realtyId));
        if (realty == null) {
            jobj.addProperty("err", "Nehnuteľnosť sa nenachádza na liste vlastníctva.");
            return jobj;
        }

        realty.setLetterOfOwnership(null);
        letter.getRealitiesSplayTree().delete(realty);

        jobj.addProperty("suc", "Úspešné odstránenié nehnuteľnosti z listu vlastníctva");
        return jobj;
    }

    public boolean save() {
        boolean result = true;

        if (!saveCadasters(this.cadasterSplayTree, "Cadasters")) {
            result = false;
        }

        if (!savePersons(this.peronsSplayTree, "Persons")) {
            result = false;
        }

        return result;
    }

    private boolean saveCadasters(SplayTree<Cadaster> cadasatersSplay, String prefix) {
        boolean result = true;

        if (!Storage.saveSplay(cadasatersSplay, prefix)) {
            result = false;
        }

        if (!Storage.saveLettersOfOwnershipAndRealtiesAndOwnerships(cadasatersSplay, "LettersOfOwnership", "Realties", "Ownerships")) {
            result = false;
        }

        return result;
    }

    private boolean savePersons(SplayTree<Person> personsSplay, String prefix) {
        boolean result = true;

        if (!Storage.saveSplay(personsSplay, prefix)) {
            result = false;
        }
        return result;
    }
    
    public boolean load() {
        Storage.loadCadasters(this);
//        this.patientsTree = Storage.loadPatients();
//        this.hospitalsTree = Storage.loadHospitals();
//        Storage.loadHospitalizationsOfPatientFaster(this);
        return !this.cadasterSplayTree.isEmpty();
//                && !this.patientsTree.isEmpty()
//                && !this.hospitalsTree.isEmpty();
    }

}
