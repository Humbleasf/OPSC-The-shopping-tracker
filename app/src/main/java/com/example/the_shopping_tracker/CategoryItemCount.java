package com.example.the_shopping_tracker;

public class CategoryItemCount {
    private int[] iCount;
    private int x = 0;

    public int[] getiCount() {
        return iCount;
    }

    public void setiCount(int iCount) {
        this.iCount[x] = iCount;
        x++;
    }
}
