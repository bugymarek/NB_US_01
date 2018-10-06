package Test;

public class Person implements Comparable<Person> {
    private int RC;
    private String name;
    private String lastName;

    public Person(int RC) {
        this.RC = RC;
    }

    Person(int RC, String name, String lastName) {
        this.RC = RC;
        this.name = name;
        this.lastName = lastName;
    }

    public void setRC(int RC) {
        this.RC = RC;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getRC() {
        return RC;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public int compareTo(Person person) {
        if (this.RC < person.getRC()) {
            return 1;
        } else if (this.RC > person.getRC()) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "Person{" +
                "RC=" + RC +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
