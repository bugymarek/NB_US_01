/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

import Splay.SplayTree;

/**
 *
 * @author Bugy
 */
class Realty implements Comparable<Realty>{
    private int id;
    private String address;
    private String description;
    private LetterOfOwnership leafOfOwnership;
    private SplayTree<Person> permanentResidencePersonsSplayTree;

    public Realty(int id, String address, String description, LetterOfOwnership leafOfOwnership, SplayTree<Person> permanentResidencePersonsSplayTree) {
        this.id = id;
        this.address = address;
        this.description = description;
        this.leafOfOwnership = leafOfOwnership;
        this.permanentResidencePersonsSplayTree = permanentResidencePersonsSplayTree;
    }

    public Realty(int id) {
        this.id = id;
        this.address = null;
        this.description = null;
        this.leafOfOwnership = null;
        this.permanentResidencePersonsSplayTree = null;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public LetterOfOwnership getLeafOfOwnership() {
        return leafOfOwnership;
    }

    public SplayTree<Person> getPermanentResidencePersonsSplayTree() {
        return permanentResidencePersonsSplayTree;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLeafOfOwnership(LetterOfOwnership leafOfOwnership) {
        this.leafOfOwnership = leafOfOwnership;
    }

    public void setPermanentResidencePersonsSplayTree(SplayTree<Person> permanentResidencePersonsSplayTree) {
        this.permanentResidencePersonsSplayTree = permanentResidencePersonsSplayTree;
    }
    
    @Override
    public int compareTo(Realty realty) {
        if (this.id < realty.getId()) {
            return 1;
        } else if (this.id > realty.getId()) {
            return -1;
        } else {
            return 0;
        }
    }
    
}
