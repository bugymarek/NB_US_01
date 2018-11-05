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
class Realty implements Comparable<Realty>, Savable{
    private int id;
    private String address;
    private int idCadastertForLoad;
    private String description;
    private LetterOfOwnershipById letterOfOwnership;
    private SplayTree<Person> permanentResidencePersonsSplayTree;

    public Realty(int id, String address, String description, LetterOfOwnershipById letterOfOwnership) {
        this.id = id;
        this.address = address;
        this.description = description;
        this.letterOfOwnership = letterOfOwnership;
        this.idCadastertForLoad = -1;
        this.permanentResidencePersonsSplayTree = new SplayTree<Person>();
    }

    public Realty(int id) {
        this.id = id;
        this.address = null;
        this.description = null;
        this.letterOfOwnership = null;
        this.idCadastertForLoad = -1;
        this.permanentResidencePersonsSplayTree = new SplayTree<Person>();
    }

    public Realty(int id, String address, String description, LetterOfOwnershipById letterOfOwnership, SplayTree<Person> permanentResidencePersonsSplayTree) {
        this.id = id;
        this.address = address;
        this.idCadastertForLoad = -1;
        this.description = description;
        this.letterOfOwnership = letterOfOwnership;
        this.permanentResidencePersonsSplayTree = permanentResidencePersonsSplayTree;
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

    public LetterOfOwnershipById getLetterOfOwnership() {
        return letterOfOwnership;
    }

    public SplayTree<Person> getPermanentResidencePersonsSplayTree() {
        return permanentResidencePersonsSplayTree;
    }

    public int getIdCadastertForLoad() {
        return idCadastertForLoad;
    }

    public void setIdCadastertForLoad(int idCadastertForLoad) {
        this.idCadastertForLoad = idCadastertForLoad;
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

    public void setLetterOfOwnership(LetterOfOwnershipById leafOfOwnership) {
        this.letterOfOwnership = leafOfOwnership;
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
    
    @Override
    public String save() {
        return this.id + ";" +
               this.address + ";" +
               this.description + ";" +
               ((this.letterOfOwnership != null) ? this.letterOfOwnership.getId(): null) + ";";
    }
}
