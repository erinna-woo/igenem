package com.ait.igenem.model;

/**
 * Created by Emily on 11/23/16.
 */
public class Blob {

    private String name;
    private boolean isPro;
    private int radius;

    public Blob(){

    }

    public Blob(String name, boolean isPro, int radius) {
        this.name = name;
        this.isPro = isPro;
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPro() {
        return isPro;
    }

    public void setPro(boolean pro) {
        isPro = pro;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
