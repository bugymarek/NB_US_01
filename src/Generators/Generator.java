///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package Generators;
//
//import Tree.TwoOrThreeTree;
//import java.util.Date;
//import java.util.Random;
//import Core.Hospital;
//import Core.Hospitalization;
//import Core.InsuranceCompany;
//import Core.Patient;
//import Core.PatientFullName;
//import Core.PatientInsuranceCode;
//import java.util.ArrayList;
//import java.util.concurrent.ThreadLocalRandom;
//
///**
// *
// * @author Bugy
// */
//public class Generator {
//
//    private TwoOrThreeTree<Patient> patientsTree;
//    private TwoOrThreeTree<Hospital> hospitalsTree;
//    private TwoOrThreeTree<InsuranceCompany> insuranceCompaniesTree;
//    private Random randomGenerator;
//    private String[] names;
//    private String[] lastNames;
//    private ArrayList<Hospital> arrHospitals;
//    private ArrayList<Patient> arrPatients;
//    private ArrayList<InsuranceCompany> arrInsuranceCompanies;
//    private final long year = (long) 365*1000*24*60*60;
//    private final long day = (long) 1000*24*60*60;
//    
//    public Generator() {
//        this.patientsTree = new TwoOrThreeTree<>();
//        this.hospitalsTree = new TwoOrThreeTree<>();
//        this.insuranceCompaniesTree = new TwoOrThreeTree<>();
//        this.randomGenerator = new Random();
//        this.names = getNames();
//        this.lastNames = getLastNames();
//        this.arrHospitals = new ArrayList<>();
//        this.arrPatients = new ArrayList<>();
//        this.arrInsuranceCompanies = new ArrayList<>();
//    }
//
//    public TwoOrThreeTree<Patient> generatePatients(int count) {
//        for (int i = 0; i < count; i++) {
//            String RC = RcGenerator.generateRc();
//            Patient patient = new Patient(names[randomGenerator.nextInt(names.length)],
//                    lastNames[randomGenerator.nextInt(lastNames.length)],
//                    RC,
//                    arrInsuranceCompanies.get(randomGenerator.nextInt(arrInsuranceCompanies.size())).getCode(),
//                    getRandomDate(6));
//            if (patientsTree.put(patient)) {
//                arrPatients.add(patient);
//            }
//        }
//
//        return patientsTree;
//    }
//
//    public TwoOrThreeTree<Hospital> generateHospitals(int count) {
//        for (int i = 0; i < count; i++) {
//            Hospital hospital = new Hospital(getRandomString(4));
//            if (hospitalsTree.put(hospital)) {
//                arrHospitals.add(hospital);
//            }
//        }
//
//        return hospitalsTree;
//    }
//
//    public TwoOrThreeTree<InsuranceCompany> generateInsuranceCompanies(int count) {
//        for (int i = 0; i < count; i++) {
//            String code = getRandomString(5);
//            InsuranceCompany insuranceCompany = new InsuranceCompany(code);
//            if (insuranceCompaniesTree.put(insuranceCompany)) {
//                arrInsuranceCompanies.add(insuranceCompany);
//            }
//        }
//
//        return insuranceCompaniesTree;
//    }
//
//    public void generateHospitalization(int hospitalCount, int patientCount, int hospitalizationCount) {
//        ArrayList<Hospital> arrNewHospitals = new ArrayList<>(arrHospitals);
//        for (int i = 0; i < hospitalCount; i++) {
//            ArrayList<Patient> arrNewPatients = new ArrayList<>(arrPatients);
//            if (arrNewHospitals.size() > 0) {
//                Hospital randomHospital = arrNewHospitals.get(randomGenerator.nextInt(arrNewHospitals.size()));
//                arrNewHospitals.remove(randomHospital);// vymaz ju  z arrNewHospitals pretoze chcem vkladat do inej nemocnice
//                Hospital randomHospitalFided = hospitalsTree.find(randomHospital);
//                //System.out.println("Nemocnica: " + randomHospital.getName());
//                for (int j = 0; j < patientCount; j++) {
//                    if (arrNewPatients.size() > 0) {                      
//                        Patient randomPatient = arrNewPatients.get(randomGenerator.nextInt(arrNewPatients.size()));
//                        arrNewPatients.remove(randomPatient); // vymaz ho z arrNewPatients pretoze do dalsej nemocnice ho uz nechces ako aktualne hospitalizovaneho pretoze nemoze byt aktualn hospital v dvoch nemocniciach naraz
//                        Patient randomPatientTree = patientsTree.find(randomPatient);
//                        //System.out.println("Pacient: " + randomPatient.getRc());
//                        for (int k = 0; k < hospitalizationCount; k++) {
//                            long dateFrom = getRandomTimeMSInYear();
//                            long dateTo = dateFrom + ThreadLocalRandom.current().nextLong(day*10);
//                            Hospitalization hospitalization = new Hospitalization(
//                                    new Date(dateFrom),
//                                    names[randomGenerator.nextInt(names.length)],
//                                    randomPatientTree,
//                                    randomHospital);
//                            hospitalization.setDateTo(new Date(dateTo));
//                            addHospitalization(hospitalization, randomHospitalFided, randomPatientTree);
//                        }
//                        
//                    }
//                }   
//            }
//
//        }
//    }
//
//    public void generateActualHospitalization(int hospitalCount, int patientCount) {
//        ArrayList<Hospital> arrNewHospitals = new ArrayList<>(arrHospitals);
//        ArrayList<Patient> arrNewPatients = new ArrayList<>(arrPatients);
//        for (int i = 0; i < hospitalCount; i++) {
//            if (arrNewHospitals.size() > 0) {
//                Hospital randomHospital = arrNewHospitals.get(randomGenerator.nextInt(arrNewHospitals.size()));
//                arrNewHospitals.remove(randomHospital);// vymaz ju  z arrNewHospitals pretoze chcem vkladat do inej nemocnice
//                Hospital randomHospitalFided = hospitalsTree.find(randomHospital);
//                for (int j = 0; j < patientCount; j++) {
//                    if (arrNewPatients.size() > 0) {
//                        Patient randomPatient = arrNewPatients.get(randomGenerator.nextInt(arrNewPatients.size()));
//                        arrNewPatients.remove(randomPatient); // vymaz ho z arrNewPatients pretoze do dalsej nemocnice ho uz nechces ako aktualne hospitalizovaneho pretoze nemoze byt aktualn hospital v dvoch nemocniciach naraz
//                        Patient randomPatientTree = patientsTree.find(randomPatient);
//                        Hospitalization hospitalization = new Hospitalization(
//                                getRandomDate(1),
//                                names[randomGenerator.nextInt(names.length)],
//                                randomPatientTree,
//                                randomHospital);
//                        addActualHospitalization(hospitalization, randomHospitalFided, randomPatientTree);
//                    }
//                }
////                System.out.println("Nemocnica: " + randomHospital.getName());
////
////                if (hospitalsTree.find(randomHospital).getPatiantsFullNameTree() != null) {
////                    ArrayList<PatientFullName> arrPatientFullName = hospitalsTree.find(randomHospital).getPatiantsFullNameTree().traverse(hospitalsTree.find(randomHospital).getPatiantsFullNameTree().getRoot(), t -> t.getPatient() != null);
////                    if (arrPatientFullName != null) {
////                        for (PatientFullName patient : arrPatientFullName) {
////                            System.out.println(patient.getPatient().getFullName());
////                        }
////                    }
////                }
//            }
//
//        }
//
//    }
//
//    public boolean addActualHospitalization(Hospitalization hospitalization, Hospital hospital, Patient patient) {
//        boolean isActualHospital = hospital.getActuaPatientsTree().put(patient); // ak uz je hospitalizovany vrati false
//        if (isActualHospital) {
//            patient.setActualHospitalizataion(hospitalization);
//            hospital.getPatiantsTree().put(patient); //pridanie pacienta do nemocnice
//            hospital.getPatiantsFullNameTree().put(new PatientFullName(patient));
//            hospital.getActualPatiantsInsuranceCodeTree().put(new PatientInsuranceCode(patient));
//            patient.getHospitalizationTree().put(hospitalization); // pridanie hospitalizacie do konkretneho pacienta;
//            hospital.getHospitalizationsTree().put(hospitalization);// vsetky hospitalizacie;
//            InsuranceCompany insuranceCompany = insuranceCompaniesTree.find(new InsuranceCompany(patient.getInsuranceCode()));
//            insuranceCompany.getHospitalizationTree().put(hospitalization);// pridanie hospitalizeie pre konkretnu poistovnu.  
//            return true;
//        } else {
//            return false;
//        }
//    }
//    
//    public void addHospitalization(Hospitalization hospitalization, Hospital hospital, Patient patient) {
//            hospital.getPatiantsTree().put(patient); //pridanie pacienta do nemocnice
//            hospital.getPatiantsFullNameTree().put(new PatientFullName(patient));
//            patient.getHospitalizationTree().put(hospitalization); // pridanie hospitalizacie do konkretneho pacienta;
//            hospital.getHospitalizationsTree().put(hospitalization);// vsetky hospitalizacie;
//            InsuranceCompany insuranceCompany = insuranceCompaniesTree.find(new InsuranceCompany(patient.getInsuranceCode()));
//            insuranceCompany.getHospitalizationTree().put(hospitalization);// pridanie hospitalizeie pre konkretnu poistovnu.  
//    }
//
//    public TwoOrThreeTree<Patient> getPatientsTree() {
//        return patientsTree;
//    }
//
//    public TwoOrThreeTree<Hospital> getHospitalsTree() {
//        return hospitalsTree;
//    }
//
//    private String[] getNames() {
//        String[] names = {"Kr", "Ca", "Ra", "Mrok", "Cru",
//            "Ray", "Bre", "Zed", "Drak", "Mor", "Jag", "Mer", "Jar", "Mjol",
//            "Zork", "Mad", "Cry", "Zur", "Creo", "Azak", "Azur", "Rei", "Cro",
//            "Mar", "Luk"};
//        return names;
//    }
//
//    private String[] getLastNames() {
//        String[] lastNames = {"Bugaj", "Gofa", "Melek", "Jonas", "Artur",
//            "Demos", "Pralok", "Gabalova", "Mrtek", "Vonsak", "Egres", "Stankovianska", "Albinikova", "Anat",
//            "Chrobak", "Niekto", "Nikdova", "Lahka", "Velky", "Nizky", "Stary", "Mlady", "Velka",
//            "Mar", "Luk"};
//        return lastNames;
//    }
//
//    public TwoOrThreeTree<InsuranceCompany> getInsuranceCompaniesTree() {
//        return insuranceCompaniesTree;
//    }
//
//    private String[] getInsuranceCode() {
//        String[] lastNames = {"Alianz", "Slovenska", "Koopera", "Zilinska", "Horehronska",
//            "Slavna", "Prva", "Daleka"};
//        return lastNames;
//    }
//
//    private String getRandomString(int length) {
//        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
//        StringBuilder salt = new StringBuilder();
//        Random rnd = new Random();
//        while (salt.length() < length) { // length of the random string.
//            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
//            salt.append(SALTCHARS.charAt(index));
//        }
//        String saltStr = salt.toString();
//        return saltStr;
//
//    }
//
//    private Date getRandomDate(int multiple) {
//        int offset = randomGenerator.nextInt(99999*multiple);
//        return new Date(System.currentTimeMillis() - offset);
//    }
//    
//    private long getRandomTimeMSInYear(){
//        return (System.currentTimeMillis() - year) + ThreadLocalRandom.current().nextLong(year);
//    }
//
//    private Date getrandomDateBetween(long start, long end) {
//        Date date = new Date((long) (start + Math.random() * (end - start)));
//        return date;
//    }
//}
