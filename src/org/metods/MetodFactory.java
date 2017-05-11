package org.metods;

public class MetodFactory {

    public static final String ID3 = "ID3";
    public static final String C45 = "C45";
    public static final String CART = "CART";

    public static Metod getMetod(String metodName){
        if(metodName.equals(ID3))
            return new ID3();
        else if(metodName.equals(C45))
            return new C45();
        else if(metodName.equals(CART))
            return new CART();
        return null;
    }
}
