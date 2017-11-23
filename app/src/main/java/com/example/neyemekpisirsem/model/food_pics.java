package com.example.neyemekpisirsem.model;

/**
 * Created by Reklamotv on 23.11.2017.
 */

public class food_pics {

    @com.google.gson.annotations.SerializedName("id")
    private String id;

    @com.google.gson.annotations.SerializedName("food_id")
    private String food_id;

    @com.google.gson.annotations.SerializedName("pic_id")
    private String pic_id;

    public food_pics(){

    }

    public food_pics(String id, String food_id, String pic_id) {
        this.id = id;
        this.food_id = food_id;
        this.pic_id = pic_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFood_id() {
        return food_id;
    }

    public void setFood_id(String food_id) {
        this.food_id = food_id;
    }

    public String getPic_id() {
        return pic_id;
    }

    public void setPic_id(String pic_id) {
        this.pic_id = pic_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        food_pics food_pics = (food_pics) o;

        return id.equals(food_pics.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
