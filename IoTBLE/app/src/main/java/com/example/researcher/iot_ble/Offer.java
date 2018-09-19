package com.example.researcher.iot_ble;

import java.util.ArrayList;

/**
 * Created by user on 19-09-2018.
 */

public class Offer {

    private int Rackno;
    private int Section;
    ArrayList<OfferJson> listOffers = new ArrayList<>();

    public int getRackno(){
        return Rackno;
    }

    public int getSection(){
        return Section;
    }

    public ArrayList<OfferJson> getListOffers(){
        return listOffers;
    }

    public void setRackno(int rackno){
        this.Rackno = rackno;
    }

    public void setSection(int section){
        this.Section = section;
    }

    public void setListOffers(ArrayList<OfferJson> offers){
        this.listOffers = offers;
    }

}
