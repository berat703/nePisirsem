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

    /*** CONSTRUCTOR *********/
    public Foods(){

    }

    @Override
    public String toString() {
        return "Foods{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
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
