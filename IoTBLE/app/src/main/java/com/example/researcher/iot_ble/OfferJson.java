package com.example.researcher.iot_ble;

/**
 * Created by user on 19-09-2018.
 */

public class OfferJson {

    private String item;
    private String discount;

    public String getItem(){
        return item;
    }

    public String getDiscount(){
        return discount;
    }

    public void setItem(String name){
        this.item = name;
    }

    public void setDiscount(String value){
        this.discount = value;
    }
}
