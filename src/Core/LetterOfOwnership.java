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
class LetterOfOwnership implements Comparable<LetterOfOwnership> {
    private int id;
    private Cadaster cadaster;
    private SplayTree<Realty> realitiesSplayTree;
    // majetkove podiely

    public LetterOfOwnership(int id) {
        this.id = id;
        this.cadaster = null;
        this.realitiesSplayTree = new SplayTree<Realty>();
    }

    public LetterOfOwnership(int id, Cadaster cadaster) {
        this.id = id;
        this.cadaster = cadaster;
        this.realitiesSplayTree = new SplayTree<Realty>();
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

    @Override
    public int compareTo(LetterOfOwnership leafOfOwnership) {
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
    
    
    
}
