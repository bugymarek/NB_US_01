package Test;

import Splay.SplayTree;

import java.util.ArrayList;
import java.util.Random;

public class Test<T extends Comparable<T>> {

    private SplayTree<T> splayTree;
    private final int RANGE_DELETE = 100000;
    private final int RANGE_ISERT = 10000000;

    Test(SplayTree<T> splayTree) {
        this.splayTree = splayTree;
    }

    public void checkInsertbasic() {
        splayTree = (SplayTree<T>) new SplayTree<Person>();
        Person person = new Person(8, "Marek", "Bugaj");
        splayTree.insert((T) person);

        person = new Person(5, "Andrej", "Bugaj");
        splayTree.insert((T) person);

        person = new Person(2, "Andrej", "Bugaj");
        splayTree.insert((T) person);

        person = new Person(6, "Andrej", "Bugaj");
        splayTree.insert((T) person);

        person = new Person(2, "Andrej", "Bugaj");
        splayTree.insert((T) person);

        splayTree.delete((T) new Person(6, "Andrej", "Bugaj"));
        System.out.println("koniec");

    }

    public boolean checkInsert() {
        Random randomGenerator = new Random();//seed: 63 - prvkov : 10  // seed: 1 - prvkov : 5
        ArrayList<Person> arr = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            randomGenerator.setSeed(i);
            System.out.println("Seed:******************************************************************************** " + i);
            arr = new ArrayList<>();
            splayTree = (SplayTree<T>) new SplayTree<Person>();
            int duplicite = 0;
            for (int j = 0; j < 300000; j++) {
                int Id = randomGenerator.nextInt(RANGE_ISERT);
                Person person = new Person(Id, "Marek", "Bugaj");
                arr.add(person);
                //System.out.println("Vkladam: " + person);
                boolean result = splayTree.insert((T) person);

                if (!result) {
                    duplicite++;
                    //System.out.println("Prvok s rovnakym klucom sa uz v strome nachadza");
                }
            }

            if ((arr.size() - duplicite) == splayTree.getCount()) {
                System.out.println("Arr count: " + arr.size() + " - dublicites: " + duplicite + "; splay count: " + splayTree.getCount());
            } else {
                System.out.println("Arr count: " + arr.size() + "splay count: " + splayTree.getCount());
            }

            //skus hladat par prvkov ktore sa v strome nenachadzaju
            for (int n = 0; n < 5000; n++) {

                int id = randomGenerator.nextInt(50000) + RANGE_ISERT;
                if (splayTree.find((T) new Person(id, "", "")) != null) {
                    System.out.println("Nasiel sa prvok, ktory v strome nemal byt");
                    return false;
                }


                id = randomGenerator.nextInt(50000) + RANGE_ISERT;
                if (splayTree.find((T) new Person(id, "", "")) != null) {
                    System.out.println("Nasiel sa prvok, ktory v strome nemal byt");
                    return false;
                }

                id = id - RANGE_ISERT - RANGE_ISERT;
                if (splayTree.find((T) new Person(id, "", "")) != null) {
                    System.out.println("Nasiel sa prvok, ktory v strome nemal byt");
                    return false;
                }
                if (splayTree.find((T) new Person(id, "", "")) != null) {
                    System.out.println("Nasiel sa prvok, ktory v strome nemal byt");
                    return false;
                }
            }

//            int n = 0;
//            for (T p : splayTree.inorder()) {
//                System.out.println(n + ". " + ((Person) p).toString());
//                n++;
//            }

            for (int k = 0; k < arr.size(); k++) {
                Person personArr = arr.get(k);
                T personTree = splayTree.find((T) personArr);
                if (personTree != null) {
                    //System.out.println("true: DataArr: (" + personArr.getRC() + ")   " + "DataTree: (" + personTree.toString() + ")");
                    //System.out.println("Seed: " + i);
                } else {
                    System.out.println("false, prvok sa v strome nenasiel: " + personTree.toString());
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkDelete() {
        Random randomGenerator = new Random(0);//seed: 173 - prvkov : 173
        ArrayList<Person> arr = new ArrayList<>();
        ArrayList<Integer> arrID = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            randomGenerator.setSeed(i);
            arr = new ArrayList<>();
            arrID = new ArrayList<>();
            splayTree = new SplayTree<>();

            for (int j = 0; j < 10000; j++) {
                int Id = randomGenerator.nextInt(RANGE_DELETE);
                Person person = new Person(Id, "Marek", "Bugaj");
                if (!arrID.contains(person.getRC())) {
                    arrID.add(person.getRC());
                    arr.add(person);
                }
                splayTree.insert((T) person);
            }
            int sizeBefore = splayTree.getCount();

            //skus vymazat par prvkov ktore sa v strome nenachadzaju
            for (int n = 0; n < 5000; n++) {

                int id = randomGenerator.nextInt(50000) + RANGE_DELETE;
                splayTree.delete((T) new Person(id, "", ""));

                id = randomGenerator.nextInt(50000) + RANGE_DELETE;
                splayTree.delete((T) new Person(id, "", ""));

                id = id - RANGE_DELETE - RANGE_DELETE;
                splayTree.delete((T) new Person(id, "", ""));
                splayTree.delete((T) new Person(id, "", ""));
            }

            for (int k = 0; k < arr.size(); k++) {
                Person personArr = arr.get(k);
                System.out.println("Deleting id: " + personArr.getRC());
                boolean deleted = splayTree.delete((T) personArr);
                if (deleted) {
                    System.out.println("Deleted: true ");
                } else {
                    System.out.print("Deleted: false, prvok sa v strome nenachadza ");
                    return false;
                }

                int sizeAfter = splayTree.getCount();
                if (sizeBefore == sizeAfter + (k + 1)) {
                    System.out.println("true: SizeBefore: " + sizeBefore + "/ SizeAfeter: " + sizeAfter);
                    // System.out.println("Seed: " + i);
                } else {
                    System.out.println("false: SizeBefore: " + sizeBefore + "/ SizeAfeter: " + sizeAfter);
                    return false;
                }
            }
        }
        return true;
    }
}
