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
public class RealtyDesc implements Comparable<RealtyDesc> {

    private Realty realty;

    public RealtyDesc(Realty realty) {
        this.realty = realty;
    }

    public Realty getRealty() {
        return realty;
    }

    @Override
    public int compareTo(RealtyDesc realtyDesc) {
        int result = realtyDesc.getRealty().getDescription().compareTo(this.realty.getDescription());
        if (result == 0) {
            result = this.realty.compareTo(realtyDesc.getRealty());
        }
        return result;
    }
}
