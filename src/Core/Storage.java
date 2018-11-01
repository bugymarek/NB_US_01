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
            writer = new PrintWriter(path + name + ".txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
            writer = new PrintWriter(path + name + ".txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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

//    static TwoOrThreeTree<InsuranceCompany> loadInsuranceCompanies() {
//        TwoOrThreeTree<InsuranceCompany> result = new TwoOrThreeTree<InsuranceCompany>();
//        Scanner sc = null;
//        try {
//            sc = new Scanner(new FileReader(path + "InsuranceCompanies.txt"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        while (sc.hasNextLine()) {
//            String[] line = sc.nextLine().split(";");
//
//            InsuranceCompany ic = new InsuranceCompany(line[0]);
//            result.put(ic);
//        }
//        sc.close();
//
//        return result;
//    }
//
//    static TwoOrThreeTree<Patient> loadPatients() {
//        TwoOrThreeTree<Patient> result = new TwoOrThreeTree<Patient>();
//        Scanner sc = null;
//        try {
//            sc = new Scanner(new FileReader(path + "Patients.txt"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        while (sc.hasNextLine()) {
//            String[] line = sc.nextLine().split(";");
//
//            String firstName = line[0];
//            String lastName = line[1];
//            String Rc = line[2];
//            String insuranceCode = line[3];
//            Date birthDate = Core.getDateFromString(line[4]);
//
//            Patient patient = new Patient(firstName, lastName, Rc, insuranceCode, birthDate);
//            result.put(patient);
//        }
//        sc.close();
//
//        return result;
//    }
//
//    static TwoOrThreeTree<Hospital> loadHospitals() {
//        TwoOrThreeTree<Hospital> result = new TwoOrThreeTree<Hospital>();
//        Scanner sc = null;
//        try {
//            sc = new Scanner(new FileReader(path + "Hospitals.txt"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        while (sc.hasNextLine()) {
//            String[] line = sc.nextLine().split(";");
//
//            String name = line[0];
//
//            Hospital hospital = new Hospital(name);
//            result.put(hospital);
//        }
//        sc.close();
//
//        return result;
//    }
//
//    static void loadHospitalizationsOfPatient(Core core) {
//        Scanner sc = null;
//
//        ArrayList<Patient> arrPatients = core.getPatientsTree().inOrder();
//
//        for (Patient patient : arrPatients) {
//            try {
//                sc = new Scanner(new FileReader(path + "Hospitalizations" + patient.getRc() + ".txt"));
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            while (sc.hasNextLine()) {
//                String[] line = sc.nextLine().split(";");
//
//                Date dateFrom = Core.getDateFromString(line[0]);
//                Date dateTo = isNull(line[1]) ? null : Core.getDateFromString(line[1]);
//                String diagnosis = line[2];
//                Patient patientData = core.findPatient(line[3]);
//                Hospital hospital = isNull(line[4]) ? null : core.findHospital(line[4]);
//
//                core.addHospitalizationFromLoad(dateFrom, dateTo, diagnosis, hospital, patientData);
//
//            }
//
//        }
//    }
//
//    static void loadHospitalizationsOfPatientFaster(Core core) {
//        Scanner sc = null;
//
//        ArrayList<Patient> arrPatients = core.getPatientsTree().inOrder();
//        
//        try {
//                sc = new Scanner(new FileReader(path + "Hospitalizations.txt"));
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            
//            while (sc.hasNextLine()) {
//                String[] line = sc.nextLine().split(";");
//
//                Date dateFrom = Core.getDateFromString(line[0]);
//                Date dateTo = isNull(line[1]) ? null : Core.getDateFromString(line[1]);
//                String diagnosis = line[2];
//                Patient patientData = core.findPatient(line[3]);
//                Hospital hospital = isNull(line[4]) ? null : core.findHospital(line[4]);
//
//                core.addHospitalizationFromLoad(dateFrom, dateTo, diagnosis, hospital, patientData);
//
//            }
//    }
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
                writer = new PrintWriter(path + prefix + ".txt");
                writer2 = new PrintWriter(path + prefix2 + ".txt");
                writer3 = new PrintWriter(path + prefix3 + ".txt");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
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
                            writer2.append("" + cadaster.getId());// pridam informaciu o tom v ktorom katastri sa nehnutelnost nachadza
                            writer2.append(System.lineSeparator());
                        }
                    }
                }
                result = true;
            }
            writer.close();
            writer2.close();
        }
        return result;
    }

}
