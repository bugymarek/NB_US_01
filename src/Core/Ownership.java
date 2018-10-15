/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

/**
 *
 * @author Bugy
 */
public class Ownership implements Comparable<Ownership>{
    private Person owner;
    private int share;

    public Ownership(Person owner, int share) {
        this.owner = owner;
        this.share = share;
    }

    public Person getOwner() {
        return owner;
    }

    public int getShare() {
        return share;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public void setShare(int share) {
        this.share = share;
    }

    @Override
    public int compareTo(Ownership ownership) {
        return  ownership.getOwner().getRC().compareTo(this.owner.getRC());
    }
}
