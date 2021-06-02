package com.example.the_shopping_tracker;

import com.google.firebase.storage.StorageReference;

public class Shopping_Details {
    private String sName;
    StorageReference gsReference;
    private String Shopping_Description;
    private int Shopping_Amount;

    public Shopping_Details() {
    }

    public Shopping_Details(String sName, StorageReference gsReference, String shopping_Description, int shopping_Amount) {
        this.sName = sName;
        this.gsReference = gsReference;
        this.Shopping_Description = shopping_Description;
        this.Shopping_Amount = shopping_Amount;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public StorageReference getGsReference() {
        return gsReference;
    }

    public void setGsReference(StorageReference gsReference) {
        this.gsReference = gsReference;
    }

    public String getShopping_Description() {
        return Shopping_Description;
    }

    public void setShopping_Description(String shopping_Description) {
        Shopping_Description = shopping_Description;
    }

    public String getShopping_Amount() {
        String Amount = "";
        Amount = Integer.toString(Shopping_Amount);
        return Amount;
    }

    public void setShopping_Amount(int shopping_Amount) {
        Shopping_Amount = shopping_Amount;
    }
}
