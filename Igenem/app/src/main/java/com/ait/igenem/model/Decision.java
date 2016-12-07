package com.ait.igenem.model;

import java.io.Serializable;

/**
 * Created by Erinna on 11/23/16.
 */

public class Decision implements Serializable {

    private String name;
    private String color;
    private String owner;
    private String id;

    public Decision(String name, String color, String owner) {
        this.name = name;
        this.color = color;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
