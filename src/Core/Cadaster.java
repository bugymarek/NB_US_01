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
class Cadaster implements Comparable<Cadaster>{
    private int id;
    private String name;
    private SplayTree<Realty> realtiesSplayTree;
    private SplayTree<LetterOfOwnership> letterOfOwnershipSplayTree;

    public Cadaster(int id, String name, SplayTree<Realty> realtiesSplayTree, SplayTree<LetterOfOwnership> letterOfOwnershipSplayTree) {
        this.id = id;
        this.name = name;
        this.realtiesSplayTree = realtiesSplayTree;
        this.letterOfOwnershipSplayTree = letterOfOwnershipSplayTree;
    }
    

    public Cadaster(int id, String name) {
        this.id = id;
        this.name = name;
        this.realtiesSplayTree = null;
        this.letterOfOwnershipSplayTree = null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public SplayTree<Realty> getRealtiesSplayTree() {
        return realtiesSplayTree;
    }

    public SplayTree<LetterOfOwnership> getLetterOfOwnershipSplayTree() {
        return letterOfOwnershipSplayTree;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRealtiesSplayTree(SplayTree<Realty> realtiesSplayTree) {
        this.realtiesSplayTree = realtiesSplayTree;
    }

    public void setLetterOfOwnershipSplayTree(SplayTree<LetterOfOwnership> letterOfOwnershipSplayTree) {
        this.letterOfOwnershipSplayTree = letterOfOwnershipSplayTree;
    }

    @Override
    public int compareTo(Cadaster cadaster) {
        if (this.id < cadaster.getId()) {
            return 1;
        } else if (this.id > cadaster.getId()) {
            return -1;
        } else {
            return 0;
        }
    }
    
    
}
