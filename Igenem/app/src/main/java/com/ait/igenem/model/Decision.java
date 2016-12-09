package com.ait.igenem.model;

import java.io.Serializable;

/**
 * Created by Erinna on 11/23/16.
 */

public class Decision implements Serializable {

    private String name;
    private String color;
    private String owner;
    private String ownerId;

    public Decision() {}

    public Decision(String name, String color, String owner, String ownerId) {
        this.name = name;
        this.color = color;
        this.owner = owner;
        this.ownerId = ownerId;
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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
