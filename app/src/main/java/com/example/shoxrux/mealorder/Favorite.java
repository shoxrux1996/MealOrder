package com.example.shoxrux.mealorder;

/**
 * Created by shoxrux on 12/14/18.
 */

public class Favorite {
    private String imageURL;
    private String title;
    private String description;
    private Double price;
    private String key;

    public Favorite(String imageURL, String title, String description, Double price) {
        this.imageURL = imageURL;
        this.title = title;
        this.description = description;
        this.price = price;
    }
    public Favorite(){

    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
