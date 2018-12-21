package com.example.shoxrux.mealorder.Model;

import java.io.Serializable;

/**
 * Created by shoxrux on 12/17/18.
 */

public class Order implements Serializable{
    public static final int STATE_WAITING = 0;
    public static final int STATE_APPROVED = 1;
    public static final int STATE_REJECTED = 2;


    private String name;
    private String title;
    private String date;
    private String address;
    private Double price;
    private int amount;
    private Double totalPrice;
    private int status;
    private String image;
    private String key;
    private String userID;



    public Order(String name, String title, String date, Double price, int amount, Double totalPrice, String address, int status, String image, String userID) {

        this.name = name;
        this.title = title;
        this.date = date;
        this.price = price;
        this.amount = amount;
        this.totalPrice = totalPrice;
        this.status= status;
        this.image = image;
        this.address = address;
        this.userID = userID;

    }
    public Order(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }



}
