package com.example.neyemekpisirsem.model;

/**
 * Created by Reklamotv on 23.11.2017.
 */

public class Pictures {

    @com.google.gson.annotations.SerializedName("id")
    private String id;

    @com.google.gson.annotations.SerializedName("url")
    private String url;


    /*** Constructor *********/
    public Pictures(){

    }

    @Override
    public String toString() {
        return "Pictures{" +
                "url='" + url + '\'' +
                '}';
    }

    public Pictures(String id, String url) {
        this.id = id;
        this.url = url;
    }


    /*** GETTER & SETTER METHODS *********/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /*** hash & equals *********/


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pictures pictures = (Pictures) o;

        return id.equals(pictures.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
