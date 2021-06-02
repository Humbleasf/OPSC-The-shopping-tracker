package com.example.the_shopping_tracker;

import com.google.firebase.storage.StorageReference;

import java.net.URI;

public class Category {


    private String cName;

    StorageReference gsReference;

    private int minCategory;
    private int goalCategory;

    public StorageReference getGsReference() {
        return gsReference;
    }

    public void setGsReference(StorageReference gsReference) {
        this.gsReference = gsReference;
    }



    public Category(String cName, StorageReference gsReference, int minCategory, int goalCategory) {
        this.cName = cName;
        this.gsReference = gsReference;
        this.minCategory = minCategory;
        this.goalCategory = goalCategory;
    }

    public Category() {
    }



    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }


    public int getMinCategory() {
        return minCategory;
    }

    public void setMinCategory(int minCategory) {
        this.minCategory = minCategory;
    }

    public int getGoalCategory() {
        return goalCategory;
    }

    public void setGoalCategory(int goalCategory) {
        this.goalCategory = goalCategory;
    }
}
