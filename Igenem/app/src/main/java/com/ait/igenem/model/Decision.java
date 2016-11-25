package com.ait.igenem.model;

/**
 * Created by Erinna on 11/23/16.
 */

public class Decision {

    private String name;
    private String color;

    public Decision(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {  this.name = name;   }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
