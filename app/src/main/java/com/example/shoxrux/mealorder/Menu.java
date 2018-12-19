package com.example.shoxrux.mealorder;

import java.io.Serializable;

/**
 * Created by shoxrux on 12/14/18.
 */

public class Menu implements Serializable {
    private String imageURL;
    private String title;
    private String description;
    private String ingredients;
    private Double price;
    private String key;

    public Menu(String imageURL, String title, String description, String ingredients ,Double price) {
        this.imageURL = imageURL;
        this.title = title;
        this.description = description;
        this.price = price;
        this.ingredients = ingredients;
    }
    public Menu(){

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

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
