package com.example.neyemekpisirsem.model;

/**
 * Created by Reklamotv on 23.11.2017.
 */

public class Tags {

    @com.google.gson.annotations.SerializedName("id")
    private String id;


    @com.google.gson.annotations.SerializedName("tag_name")
    private String tag_name;


    public Tags(){

    }

    @Override
    public String toString() {
        return "Tags{" +
                "tag_name='" + tag_name + '\'' +
                '}';
    }

    public Tags(String id, String tag_name) {
        this.id = id;
        this.tag_name = tag_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tags tags = (Tags) o;

        return id.equals(tags.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
