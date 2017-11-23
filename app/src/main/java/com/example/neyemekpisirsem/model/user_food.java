package com.example.neyemekpisirsem.model;

/**
 * Created by Reklamotv on 23.11.2017.
 */

public class user_food {

    @com.google.gson.annotations.SerializedName("id")
    private String id;

    @com.google.gson.annotations.SerializedName("user_id")
    private String user_id;

    @com.google.gson.annotations.SerializedName("food_id")
    private String food_id;

    public user_food(){

    }

    public user_food(String id, String user_id, String food_id) {
        this.id = id;
        this.user_id = user_id;
        this.food_id = food_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFood_id() {
        return food_id;
    }

    public void setFood_id(String food_id) {
        this.food_id = food_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        user_food user_food = (user_food) o;

        return id.equals(user_food.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
