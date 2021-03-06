package Core;

import Splay.SplayTree;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author Bugy
 */
public class Storage {

    private static String path = "C:/Users/Bugy/Documents/NetBeansProjects/US_sem01/src/Storage/";
    private static String fileType = ".csv";

    /**
     * Saves data from ArrayList to specified file
     *
     * @param arrayList
     * @param name
     * @param <T>
     * @return
     */
    public static <T> boolean saveArray(ArrayList<T> arrayList, String name) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(path + name + fileType);
        } catch (FileNotFoundException e) {
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement element : e.getStackTrace()) {
                sb.append(element.toString());
                sb.append("\n");
            }
            System.out.println("Chyba suboru. Subor sa nenasiel alebo je otvoreny. Alebo nastala ina chyba suboru: \n" + sb.toString());
            return false;
        }

        for (T e : arrayList) {
            writer.append(((Savable) e).save());
            writer.append(System.lineSeparator());
        }
        writer.close();
        return true;
    }

    /**
     * Saves data from Tree to specified file
     *
     * @param tree
     * @param name
     * @param <T>
     * @return
     */
    public static <T extends Comparable<T>> boolean saveSplay(SplayTree<T> splay, String name) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(path + name + fileType);
        } catch (FileNotFoundException e) {
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement element : e.getStackTrace()) {
                sb.append(element.toString());
                sb.append("\n");
            }
            System.out.println("Chyba suboru. Subor sa nenasiel alebo je otvoreny. Alebo nastala ina chyba suboru: \n" + sb.toString());
            return false;
        }

        if (splay == null) {
            writer.append("");
        } else {
            ArrayList<T> arr = splay.levelOrder();
            if (arr == null) {
                writer.append("");
            } else {
                for (T e : arr) {
                    writer.append(((Savable) e).save());
                    writer.append(System.lineSeparator());
                }
            }
        }
        writer.close();
        return true;
    }

    static boolean loadCadasters(Core core) {
        boolean result = true;
        Scanner sc = null;
        try {
            sc = new Scanner(new FileReader(path + "Cadasters" + fileType));
        } catch (FileNotFoundException e) {
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement element : e.getStackTrace()) {
                sb.append(element.toString());
                sb.append("\n");
            }
            System.out.println("Chyba suboru. Subor sa nenasiel alebo je otvoreny. Alebo nastala ina chyba suboru: \n" + sb.toString());
            return false;
        }
        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().split(";");

            String idStr = line[0];
            String name = line[1];
            try {
                Integer.parseInt(idStr);
            } catch (Exception e) {
                System.out.println("********************Neúspešne vloženie katastra. Nemožno previesť text(" + idStr + ") na číslo.*****************\n"
                        + " id: " + idStr + "\n"
                        + " názov: " + name + "\n"
                        + "*********************************************************************************************************************");
                continue;
            }
            if (!core.addCadaster(Integer.parseInt(idStr), name)) {
                System.out.println("********************Neúspešne vloženie katastra. Dublicita.*****************\n"
                        + " id: " + idStr + "\n"
                        + " názov: " + name + "\n"
                        + "*********************************************************************************************************************");
            };

        }
        sc.close();

        return result;
    }

    static boolean loadLettersOfOwnerchip(Core core) {
        boolean result = true;
        Scanner sc = null;
        try {
            sc = new Scanner(new FileReader(path + "LettersOfOwnership" + fileType));
        } catch (FileNotFoundException e) {
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement element : e.getStackTrace()) {
                sb.append(element.toString());
                sb.append("\n");
            }
            System.out.println("Chyba suboru. Subor sa nenasiel alebo je otvoreny. Alebo nastala ina chyba suboru: \n" + sb.toString());
            return false;
        }
        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().split(";");

            String idLetterStr = line[0];
            String idCadasterStr = line[1];
            try {
                Integer.parseInt(idLetterStr);
            } catch (Exception e) {
                System.out.println("********************Neúspešne vloženie listu. Nemožno previesť text(" + idLetterStr + ") na číslo.*****************\n"
                        + " číslo listu: " + idLetterStr + "\n"
                        + " číslo nehnuteľnosti: " + idCadasterStr + "\n"
                        + "*********************************************************************************************************************");
                continue;
            }
            try {
                Integer.parseInt(idCadasterStr);
            } catch (Exception e) {
                System.out.println("********************Neúspešne vloženie listu. Nemožno previesť text(" + idCadasterStr + ") na číslo.*****************\n"
                        + " číslo listu: " + idLetterStr + "\n"
                        + " číslo nehnuteľnosti: " + idCadasterStr + "\n"
                        + "*********************************************************************************************************************");
                continue;
            }

            if (core.findCadaster(Integer.parseInt(idCadasterStr)) == null) {
                System.out.println("********************Kataster sa nenasiel.*****************\n"
                        + " id: " + idCadasterStr + "\n"
                        + "*********************************************************************************************************************");
                continue;
            }

            if (!core.addLatterOfOwnership(Integer.parseInt(idCadasterStr), Integer.parseInt(idLetterStr))) {
                System.out.println("********************Neúspešne vloženie listu. Duplicita*****************\n"
                        + " číslo listu: " + idLetterStr + "\n"
                        + " číslo nehnuteľnosti: " + idCadasterStr + "\n"
                        + "*********************************************************************************************************************");
                continue;
            }
        }
        sc.close();

        return result;
    }

    static boolean loadRealties(Core core) {
        boolean result = true;
        Scanner sc = null;
        try {
            sc = new Scanner(new FileReader(path + "Realties" + fileType));
        } catch (FileNotFoundException e) {
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement element : e.getStackTrace()) {
                sb.append(element.toString());
                sb.append("\n");
            }
            System.out.println("Chyba suboru. Subor sa nenasiel alebo je otvoreny. Alebo nastala ina chyba suboru: \n" + sb.toString());
            return false;
        }
        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().split(";");

            String idRealtyStr = line[0];
            String address = line[1];
            String desc = line[2];
            String idLetterStr = isNull(line[3]) ? null : line[3];
            String idCadasterStr = line[4];
            try {
                Integer.parseInt(idRealtyStr);
            } catch (Exception e) {
                System.out.println("********************Neúspešne vloženie nehnutelnosti. Nemožno previesť text(" + idRealtyStr + ") na číslo.*****************\n"
                        + " číslo nehnuteľnosti: " + idRealtyStr + "\n"
                        + " adressa: " + address + "\n"
                        + " poipis: " + desc + "\n"
                        + " číslo listu: " + idLetterStr + "\n"
                        + " číslo katastra: " + idCadasterStr + "\n"
                        + "*********************************************************************************************************************");
                continue;
            }
            if (idLetterStr != null) {
                try {
                    Integer.parseInt(idLetterStr);
                } catch (Exception e) {
                    System.out.println("********************Neúspešne vloženie nehnutelnosti. Nemožno previesť text(" + idLetterStr + ") na číslo.*****************\n"
                            + " číslo nehnuteľnosti: " + idRealtyStr + "\n"
                            + " adressa: " + address + "\n"
                            + " poipis: " + desc + "\n"
                            + " číslo listu: " + idLetterStr + "\n"
                            + " číslo katastra: " + idCadasterStr + "\n"
                            + "*********************************************************************************************************************");
                    continue;
                }
            }
            try {
                Integer.parseInt(idCadasterStr);
            } catch (Exception e) {
                System.out.println("********************Neúspešne vloženie nehnutelnosti. Nemožno previesť text(" + idCadasterStr + ") na číslo.*****************\n"
                        + " číslo nehnuteľnosti: " + idRealtyStr + "\n"
                        + " adressa: " + address + "\n"
                        + " poipis: " + desc + "\n"
                        + " číslo listu: " + idLetterStr + "\n"
                        + " číslo katastra: " + idCadasterStr + "\n"
                        + "*********************************************************************************************************************");
                continue;
            }
            int addedResult = core.addRealty(Integer.parseInt(idCadasterStr), isNull(idLetterStr) ? -1 : Integer.parseInt(idLetterStr), Integer.parseInt(idRealtyStr), address, desc);
            String message = new String();
            switch (addedResult) {
                case 0:
                    message = "Úspešne vloženie nehnuteľnosti na list vlastníctva.\n";
                    break;
                case -1:
                    message = "Neúspešne vloženie nehnuteľnosti do katastra\n";
                    break;
                case -2:
                    message = "Neúspešne vloženie nehnuteľnosti na list vlastníctva. Ale na kataster bola vlozena\n";
                    break;
                case -3:
                    message = "List vlastníctva sa nenašiel. Ale na kataster bola nehnuteľnosť vložena.\n";
                    break;
                case -4:
                    message = "Kataster sa nenašiel\n";
                    break;
            }
            message += "******************************************************\n"
                    + " číslo nehnuteľnosti: " + idRealtyStr + "\n"
                    + " adressa: " + address + "\n"
                    + " poipis: " + desc + "\n"
                    + " číslo listu: " + idLetterStr + "\n"
                    + " číslo katastra: " + idCadasterStr + "\n"
                    + "******************************************************";
            if (addedResult < 0) {
                System.out.println(message);
                continue;
            }

        }
        sc.close();

        return result;
    }

    static boolean loadPersons(Core core) {
        boolean result = true;
        Scanner sc = null;
        try {
            sc = new Scanner(new FileReader(path + "Persons" + fileType));
        } catch (FileNotFoundException e) {
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement element : e.getStackTrace()) {
                sb.append(element.toString());
                sb.append("\n");
            }
            System.out.println("Chyba suboru. Subor sa nenasiel alebo je otvoreny. Alebo nastala ina chyba suboru: \n" + sb.toString());
            return false;
        }
        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().split(";");

            String firstName = line[0];
            String lastName = line[1];
            String rc = line[2];
            String idRealtyStr = isNull(line[3]) ? null : line[3];
            String idCadasterOfRealty = isNull(line[4]) ? null : line[4];
            Date birthDate = Core.getDateFromString(line[5]);
            if (idRealtyStr != null && idCadasterOfRealty != null) {
                try {
                    Integer.parseInt(idRealtyStr);
                    Integer.parseInt(idCadasterOfRealty);
                } catch (Exception e) {
                    System.out.println("********************Osoba sa prida, bez trvalého bydliska. Nemožno previesť text(" + idRealtyStr + ";" + idCadasterOfRealty + " ) na číslo.*****************\n"
                            + " meno: " + firstName + "\n"
                            + " priezvisko: " + lastName + "\n"
                            + " RČ: " + rc + "\n"
                            + " dátum narodenia: " + core.formatDateWithoutTime(birthDate) + "\n"
                            + " číslo nehnuteľnosti: " + idRealtyStr + "\n"
                            + " číslo katastra: " + idCadasterOfRealty + "\n"
                            + "*********************************************************************************************************************");
                    idRealtyStr = null;
                    idCadasterOfRealty = null;
                }
            }
            Person person = new Person(rc, firstName, lastName, birthDate, null);
            if (idRealtyStr != null && idCadasterOfRealty != null) {
                Cadaster findedCadaster = core.findCadaster(Integer.parseInt(idCadasterOfRealty));
                if (findedCadaster != null) {
                    Realty findedRealty = core.findRealty(findedCadaster, Integer.parseInt(idRealtyStr));
                    if (findedRealty != null) {
                        person.setPermanentResidence(findedRealty);
                        findedRealty.getPermanentResidencePersonsSplayTree().insert(person);
                    }
                }

            }

            boolean addedResult = core.addPersonFromImport(person);
            if (!addedResult) {
                String message = new String();
                message += "******************** Nepodarilo sa vložiť osobu**********************************\n"
                        + " meno: " + firstName + "\n"
                        + " priezvisko: " + lastName + "\n"
                        + " RČ: " + rc + "\n"
                        + " dátum narodenia: " + core.formatDateWithoutTime(birthDate) + "\n"
                        + " číslo nehnuteľnosti: " + idRealtyStr + "\n"
                        + " číslo katastra: " + idCadasterOfRealty + "\n"
                        + "******************************************************";
                System.out.println(message);
            }
        }
        sc.close();

        return result;
    }

    static boolean Ownerships(Core core) {
        boolean result = true;
        Scanner sc = null;
        try {
            sc = new Scanner(new FileReader(path + "Ownerships" + fileType));
        } catch (FileNotFoundException e) {
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement element : e.getStackTrace()) {
                sb.append(element.toString());
                sb.append("\n");
            }
            System.out.println("Chyba suboru. Subor sa nenasiel alebo je otvoreny. Alebo nastala ina chyba suboru: \n" + sb.toString());
            return false;
        }
        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().split(";");

            String rc = line[0];
            String share = line[1];
            String idLetterStr = line[2];
            String idCadasterStr = line[3];
            try {
                Double.parseDouble(share);
            } catch (Exception e) {
                System.out.println("********************Neúspešne vloženie podielu. Nemožno previesť text(" + share + ") na číslo.*****************\n"
                        + " RČ: " + rc + "\n"
                        + " podiel: " + share + "\n"
                        + " číslo listu: " + idLetterStr + "\n"
                        + " číslo nehnuteľnosti: " + idCadasterStr + "\n"
                        + "*********************************************************************************************************************");
                continue;
            }
            try {
                Integer.parseInt(idLetterStr);
            } catch (Exception e) {
                System.out.println("********************Neúspešne vloženie podielu. Nemožno previesť text(" + idLetterStr + ") na číslo.*****************\n"
                        + " RČ: " + rc + "\n"
                        + " podiel: " + share + "\n"
                        + " číslo listu: " + idLetterStr + "\n"
                        + " číslo nehnuteľnosti: " + idCadasterStr + "\n"
                        + "*********************************************************************************************************************");
                continue;
            }
            try {
                Integer.parseInt(idCadasterStr);
            } catch (Exception e) {
                System.out.println("********************Neúspešne vloženie podielu. Nemožno previesť text(" + idCadasterStr + ") na číslo.*****************\n"
                        + " RČ: " + rc + "\n"
                        + " podiel: " + share + "\n"
                        + " číslo listu: " + idLetterStr + "\n"
                        + " číslo katastra: " + idCadasterStr + "\n"
                        + "*********************************************************************************************************************");
                continue;
            }
            Person person = core.findPerson(rc);
            if (person == null) {
                System.out.println("********************Neúspešne vloženie podielu. Osoba sa nenansiel.*****************\n"
                        + " RČ: " + rc + "\n"
                        + " podiel: " + share + "\n"
                        + " číslo listu: " + idLetterStr + "\n"
                        + " číslo katastra: " + idCadasterStr + "\n"
                        + "*********************************************************************************************************************");
                continue;
            }
            Cadaster cadaster = core.findCadaster(Integer.parseInt(idCadasterStr));
            if (cadaster == null) {
                System.out.println("********************Neúspešne vloženie podielu. Kataster sa nenansiel.*****************\n"
                        + " RČ: " + rc + "\n"
                        + " podiel: " + share + "\n"
                        + " číslo listu: " + idLetterStr + "\n"
                        + " číslo katastra: " + idCadasterStr + "\n"
                        + "*********************************************************************************************************************");
                continue;
            }
            LetterOfOwnershipById letter = cadaster.getLetterOfOwnershipSplayTree().find(new LetterOfOwnershipById(Integer.parseInt(idLetterStr)));
            if (letter == null) {
                System.out.println("********************Neúspešne vloženie podielu. List sa nenansiel na katastri .*****************\n"
                        + " RČ: " + rc + "\n"
                        + " podiel: " + share + "\n"
                        + " číslo listu: " + idLetterStr + "\n"
                        + " číslo katastra: " + idCadasterStr + "\n"
                        + "*********************************************************************************************************************");
                continue;
            }

            letter.getOwnershipSplayTree().insert(new Ownership(person, Double.parseDouble(share)));
            person.getLetterOfOwnershipByIdAndCadasterSplayTree().insert(new LetterOfOwnershipByIdAndCadaster(letter));
        }
        sc.close();

        return result;
    }

    private static boolean isNull(String trim) {
        return trim == null || trim.equals("null") || trim.equals("");
    }

    static boolean saveLettersOfOwnershipAndRealtiesAndOwnerships(SplayTree<Cadaster> cadasatersSplay, String prefix, String prefix2, String prefix3) {
        boolean result = true;
        if (cadasatersSplay == null) {
            result = saveSplay(cadasatersSplay, prefix);
            result = saveSplay(cadasatersSplay, prefix2);
            result = saveSplay(cadasatersSplay, prefix3);
        } else {
            PrintWriter writer = null;
            PrintWriter writer2 = null;
            PrintWriter writer3 = null;
            try {
                writer = new PrintWriter(path + prefix + fileType);
                writer2 = new PrintWriter(path + prefix2 + fileType);
                writer3 = new PrintWriter(path + prefix3 + fileType);
            } catch (FileNotFoundException e) {
                StringBuilder sb = new StringBuilder();
                for (StackTraceElement element : e.getStackTrace()) {
                    sb.append(element.toString());
                    sb.append("\n");
                }
                System.out.println("Chyba suboru. Subor sa nenasiel alebo je otvoreny. Alebo nastala ina chyba suboru: \n" + sb.toString());
                return false;
            }
            for (Cadaster cadaster : cadasatersSplay.levelOrder()) {

                if (cadaster == null) {
                    writer.append("");
                    writer2.append("");
                    writer3.append("");
                } else {
                    ArrayList<LetterOfOwnershipById> arr = cadaster.getLetterOfOwnershipSplayTree().levelOrder();
                    if (arr == null) {
                        writer.append("");
                    } else {
                        for (LetterOfOwnershipById letter : arr) {
                            writer.append(((Savable) letter).save());
                            writer.append(System.lineSeparator());

                            // majetkove podiely
                            ArrayList<Ownership> arrOwnerships = letter.getOwnershipSplayTree().levelOrder();
                            if (arrOwnerships == null) {
                                writer3.append("");
                            } else {
                                for (Ownership o : arrOwnerships) {
                                    writer3.append(((Savable) o).save());
                                    writer3.append("" + letter.getId());// pridam informaciu o tom v ktorom liste sa podiel nachadza
                                    writer3.append(";" + cadaster.getId());// pridam informaciu o tom v ktorom katastri sa list nachadza
                                    writer3.append(System.lineSeparator());
                                }
                            }
                        }
                    }

                    ArrayList<Realty> arr2 = cadaster.getRealtiesSplayTree().levelOrder();
                    if (arr2 == null) {
                        writer2.append("");
                    } else {
                        for (Realty r : arr2) {
                            writer2.append(((Savable) r).save());
                            r.setIdCadastertForLoad(cadaster.getId());
                            writer2.append("" + cadaster.getId());// pridam informaciu o tom v ktorom katastri sa nehnutelnost nachadza
                            writer2.append(System.lineSeparator());
                        }
                    }
                }
                result = true;
            }
            writer.close();
            writer2.close();
            writer3.close();
        }
        return result;
    }

}
