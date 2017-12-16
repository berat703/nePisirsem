package com.example.neyemekpisirsem.model;

/**
 * Created by Reklamotv on 23.11.2017.
 */

public class Foods {

    @com.google.gson.annotations.SerializedName("id")
    private String id;

    @com.google.gson.annotations.SerializedName("name")
    private String name;

    @com.google.gson.annotations.SerializedName("description")
    private String description;

    @com.google.gson.annotations.SerializedName("content")
    private String content;

    @com.google.gson.annotations.SerializedName("photo_url")
    private String photo;

    @com.google.gson.annotations.SerializedName("tag_name")
    private String tag;


    @com.google.gson.annotations.SerializedName("category")
    private String category;

    @com.google.gson.annotations.SerializedName("clock")
    private String clock;

    /*** CONSTRUCTOR *********/
    public Foods(){

    }

    @Override
    public String toString() {
        return "Foods{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                ", photo='" + photo + '\'' +
                ", tag='" + tag + '\'' +
                ", category='" + category + '\'' +
                ", clock='" + clock + '\'' +
                '}';
    }

    public Foods(String id, String name, String description, String content) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.content = content;
    }

    /*** GETTER & SETTER METHODS *********/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getClock() {
        return clock;
    }

    public void setClock(String clock) {
        this.clock = clock;
    }

    /*** EQUALS & HASH *********/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Foods foods = (Foods) o;

        return id.equals(foods.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
