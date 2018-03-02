package com.example.michaelshiel.fyp;

import java.util.List;

/**
 * Created by michaelshiel on 02/03/2018.
 */

public class TescoPrice {
    String name;
    String currency;
    String price;
    List amount;
    public TescoPrice(){
    }

    public TescoPrice(String name, List amounts, String currency, String price)
    {
        this.name = name;
        this.currency = currency;
        this.amount = amounts;
        this.price = price;

    }

    public String getName() {
        return name;
    }
    public String getCurrency() {
        return currency;
    }
    public String getPrice() {
        return price;
    }
    public List getAmount() {
        return this.amount;
    }

}
