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
class LetterOfOwnershipById implements Comparable<LetterOfOwnershipById>, Savable {
    private int id;
    private Cadaster cadaster;
    private SplayTree<Realty> realitiesSplayTree;
    private SplayTree<Ownership> ownershipSplayTree;

    public LetterOfOwnershipById(int id) {
        this.id = id;
        this.cadaster = null;
        this.realitiesSplayTree = new SplayTree<Realty>();
        this.ownershipSplayTree = new SplayTree<Ownership>();
    }

    public LetterOfOwnershipById(int id, Cadaster cadaster) {
        this.id = id;
        this.cadaster = cadaster;
        this.realitiesSplayTree = new SplayTree<Realty>();
        this.ownershipSplayTree = new SplayTree<Ownership>();
    }

    public int getId() {
        return id;
    }

    public Cadaster getCadaster() {
        return cadaster;
    }

    public SplayTree<Realty> getRealitiesSplayTree() {
        return realitiesSplayTree;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCadaster(Cadaster cadaster) {
        this.cadaster = cadaster;
    }

    public void setRealitiesSplayTree(SplayTree<Realty> realitiesSplayTree) {
        this.realitiesSplayTree = realitiesSplayTree;
    }

    public SplayTree<Ownership> getOwnershipSplayTree() {
        return ownershipSplayTree;
    }

    public void setOwnershipSplayTree(SplayTree<Ownership> ownershipSplayTree) {
        this.ownershipSplayTree = ownershipSplayTree;
    }

    @Override
    public int compareTo(LetterOfOwnershipById leafOfOwnership) {
        if (this.id < leafOfOwnership.getId()) {
            return 1;
        } else if (this.id > leafOfOwnership.getId()) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "LetterOfOwnership{" + "id=" + id + ", cadaster=" + cadaster + ", realitiesSplayTree=" + realitiesSplayTree + '}';
    }

    @Override
    public String save() {
        return this.id + ";" +
               ((this.cadaster != null) ? this.cadaster.getId(): null);
    }
    
    
    
}
