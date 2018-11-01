package Core;

import Splay.SplayTree;
import java.util.Date;

public class Person implements Comparable<Person>, Savable{
    private String RC;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private Realty pernamentResidence;
    private SplayTree<LetterOfOwnershipByIdAndCadaster> letterOfOwnershipByIdAndCadasterSplayTree;
    

    public Person(String RC) {
        this.RC = RC;
        this.firstName = null;
        this.lastName = null;
        this.birthDate = null;
        this.pernamentResidence = null;
        this.letterOfOwnershipByIdAndCadasterSplayTree = new SplayTree<LetterOfOwnershipByIdAndCadaster>();
    }

    public Person(String RC, String firstName, String lastName, Date birthDate) {
        this.RC = RC;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.letterOfOwnershipByIdAndCadasterSplayTree = new SplayTree<LetterOfOwnershipByIdAndCadaster>();
    }
    
    public Person(String RC, String firstName, String lastName, Date birthDate, Realty pernamentResidence) {
        this.RC = RC;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.pernamentResidence = pernamentResidence;
        this.letterOfOwnershipByIdAndCadasterSplayTree = new SplayTree<LetterOfOwnershipByIdAndCadaster>();
    }

    public void setRC(String RC) {
        this.RC = RC;
    }

    public void setName(String name) {
        this.firstName = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRC() {
        return RC;
    }

    public String getName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public int compareTo(Person person) {
        if (this.RC.compareTo(person.getRC()) < 0) {
            return 1;
        } else if (this.RC.compareTo(person.getRC()) > 0) {
            return -1;
        } else {
            return 0;
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Realty getPernamentResidence() {
        return pernamentResidence;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setPernamentResidence(Realty pernamentResidence) {
        this.pernamentResidence = pernamentResidence;
    }

    public SplayTree<LetterOfOwnershipByIdAndCadaster> getLetterOfOwnershipByIdAndCadasterSplayTree() {
        return letterOfOwnershipByIdAndCadasterSplayTree;
    }

    @Override
    public String toString() {
        return "Person{" + "RC=" + RC + ", firstName=" + firstName + ", lastName=" + lastName + ", birthDate=" + birthDate + ", pernamentResidence=" + pernamentResidence + '}';
    }

    @Override
    public String save() {
        return this.firstName + ";" +
               this.lastName + ";" +
               this.RC + ";" +
               ((this.pernamentResidence != null) ? this.pernamentResidence.getId(): null) + ";" +
               Core.formatDateWithoutTime(this.birthDate);
    }

}
