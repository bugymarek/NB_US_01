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
public class CadasterByName implements Comparable<CadasterByName> {

    private Cadaster cadaster;

    public CadasterByName(Cadaster cadaster) {
        this.cadaster = cadaster;
    }

    public Cadaster getCadaster() {
        return cadaster;
    }

    @Override
    public int compareTo(CadasterByName cadasterByName) {
        return cadasterByName.getCadaster().getName().compareTo(this.cadaster.getName());
    }

    @Override
    public String toString() {
        return String.format("id: %s\n"
                + "názov: %s\n"
                + "počet nehnuteľnosťí: %s\n"
                + "počet listov vlastníctva: %s\n",
                cadaster.getId(),
                cadaster.getName(),
                cadaster.getRealtiesSplayTree().getCount(),
                cadaster.getRealtiesSplayTree().getCount());
    }
}
