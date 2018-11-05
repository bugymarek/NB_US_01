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
public class LetterOfOwnershipByIdAndCadaster implements Comparable<LetterOfOwnershipByIdAndCadaster>{
    private LetterOfOwnershipById letterOfOwnershipById;

    public LetterOfOwnershipByIdAndCadaster(LetterOfOwnershipById letterOfOwnershipById) {
        this.letterOfOwnershipById = letterOfOwnershipById;
    }

    public LetterOfOwnershipById getLetterOfOwnershipById() {
        return letterOfOwnershipById;
    }

    public void setLetterOfOwnershipById(LetterOfOwnershipById letterOfOwnershipById) {
        this.letterOfOwnershipById = letterOfOwnershipById;
    }

    @Override
    public int compareTo(LetterOfOwnershipByIdAndCadaster letterOfOwnershipByIdAndCadaster) {
        Cadaster cadaster = letterOfOwnershipByIdAndCadaster.getLetterOfOwnershipById().getCadaster();
        if(cadaster == null){
            System.out.println("cdd");
        }   
        int result = this.letterOfOwnershipById.getCadaster()
                .compareTo(letterOfOwnershipByIdAndCadaster.getLetterOfOwnershipById().getCadaster());
        if (result == 0){
            result = this.letterOfOwnershipById
                .compareTo(letterOfOwnershipByIdAndCadaster.getLetterOfOwnershipById());
        }
        return result;
    }
    
}
