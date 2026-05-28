package com.example.starsgallery.beans;

/*
 * MODELE MVC
 * ----------
 * This class is deliberately small.
 * It represents exactly one row in the RecyclerView:
 * an id, a display name, a local image resource, and a rating from 0 to 5.
 */
public class Star {

    // The counter creates simple unique ids for the in-memory data set.
    private static int counter = 0;

    private final int id;
    private String name;
    private int img;
    private float rating;

    public Star(String name, int img, float rating) {
        // Pre-increment: first star gets id = 1, not id = 0.
        this.id = ++counter;
        this.name = name;
        this.img = img;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getImg() {
        return img;
    }

    public float getRating() {
        return rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
