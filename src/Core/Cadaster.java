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
public class Cadaster implements Comparable<Cadaster>, Savable {

    private int id;
    private String name;
    private SplayTree<Realty> realtiesSplayTree;
    private SplayTree<LetterOfOwnershipById> letterOfOwnershipSplayTree;

    public Cadaster(int id) {
        this.id = id;
        this.realtiesSplayTree = new SplayTree<Realty>();
        this.letterOfOwnershipSplayTree = new SplayTree<LetterOfOwnershipById>();
    }
    
    public Cadaster(String name) {
        this.name = name;
        this.realtiesSplayTree = new SplayTree<Realty>();
        this.letterOfOwnershipSplayTree = new SplayTree<LetterOfOwnershipById>();
    }
    
    public Cadaster(int id, String name) {
        this.id = id;
        this.name = name;
        this.realtiesSplayTree = new SplayTree<Realty>();
        this.letterOfOwnershipSplayTree = new SplayTree<LetterOfOwnershipById>();
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

    public SplayTree<LetterOfOwnershipById> getLetterOfOwnershipSplayTree() {
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

    public void setLetterOfOwnershipSplayTree(SplayTree<LetterOfOwnershipById> letterOfOwnershipSplayTree) {
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
    
    

    @Override
    public String toString() {
        return String.format("id= %30s, názov= %30s, počet nehnuteľnosťí= %30s, počet listov vlastníctva= %30s",
                id,
                name,
                realtiesSplayTree.getCount(),
                letterOfOwnershipSplayTree.getCount());
    }

    @Override
    public String save() {
        return this.id + ";" +
               this.name;
    }

}
