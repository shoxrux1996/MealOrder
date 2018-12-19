package com.example.shoxrux.mealorder;

import java.io.Serializable;

/**
 * Created by shoxrux on 12/14/18.
 */

public class Favorite  implements Serializable{
    private String imageURL;
    private String title;
    private String description;
    private String ingredients;
    private Double price;
    private String key;

    public Favorite(String imageURL, String title, String description, Double price, String ingredients) {
        this.imageURL = imageURL;
        this.title = title;
        this.description = description;
        this.price = price;
        this.ingredients = ingredients;
    }
    public Favorite(){

    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
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

    public static Menu castToMenu(Favorite favorite){
        Menu menu = new Menu(favorite.getImageURL(), favorite.getTitle(),favorite.getDescription(),favorite.getIngredients(),favorite.getPrice());
        return menu;
    }
}
