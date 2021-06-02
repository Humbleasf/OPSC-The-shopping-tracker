package com.example.the_shopping_tracker;

import com.google.firebase.storage.StorageReference;

public class ItemModel
{
    private String name, description, date;
    int Goal, progress, price;
    StorageReference image;
    //private String user_id;

    public ItemModel(String name, String description, StorageReference pic, int goal, int progress, int price) {
        this.name = name;
        this.description = description;
        this.image = pic;
        this.Goal = goal;
        this.progress = progress;
        this.price = price;
    }

    /*public ItemModel(String description, int price, int stock, int goal, String date, String image) {
        this.description = description;
        this.price = price;
        this.progress = stock;
        this.Goal = goal;
        this.date = date;
        this.pic = image;
    }*/

    public ItemModel() {
    }


    /*public ItemModel(String name, String description, String price, String stock, String goal, String date, String image, String user_id)
    {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.goal = goal;
        this.date = date;
        this.image = image;
        this.user_id = user_id;
    }

    public ItemModel()
    {

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return progress;
    }

    public void setStock(int stock) {
        this.progress = stock;
    }

    public int getGoal() {
        return Goal;
    }

    public void setGoal(int goal) {
        this.Goal = goal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public StorageReference getImage() {
        return image;
    }

    public void setImage(StorageReference image) {
        this.image = image;
    }
}
