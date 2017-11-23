package com.example.neyemekpisirsem.model;

/**
 * Created by Reklamotv on 23.11.2017.
 */

public class tag_food {

    @com.google.gson.annotations.SerializedName("id")
    private String id;

    @com.google.gson.annotations.SerializedName("tag_id")
    private String tag_id;

    @com.google.gson.annotations.SerializedName("food_id")
    private String food_id;


    public tag_food(){

    }

    public tag_food(String id, String tag_id, String food_id) {
        this.id = id;
        this.tag_id = tag_id;
        this.food_id = food_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
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

        tag_food tag_food = (tag_food) o;

        return id.equals(tag_food.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
