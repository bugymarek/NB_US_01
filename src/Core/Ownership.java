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
    private double share;

    public Ownership(Person owner, double share) {
        this.owner = owner;
        this.share = share;
    }
    
    public Ownership(Person owner) {
        this.owner = owner;
    }

    public Person getOwner() {
        return owner;
    }

    public double getShare() {
        return share;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public void setShare(double share) {
        this.share = share;
    }

    @Override
    public int compareTo(Ownership ownership) {
        if (this.owner.getRC().compareTo(ownership.getOwner().getRC()) < 0) {
            return 1;
        } else if (this.owner.getRC().compareTo(ownership.getOwner().getRC()) > 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
