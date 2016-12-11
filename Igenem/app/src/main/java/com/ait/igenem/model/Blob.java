package com.ait.igenem.model;

/**
 * Created by Emily on 11/23/16.
 */
public class Blob {

    private String name;
    private boolean isPro;
    private int radius;
    private int posx;
    private int posy;

    public Blob(){

    }

    public Blob(String name, boolean isPro, int radius) {
        this.name = name;
        this.isPro = isPro;
        this.radius = radius;
    }

    public int getPosx() {
        return posx;
    }

    public void setPosx(int posx) {
        this.posx = posx;
    }

    public int getPosy() {
        return posy;
    }

    public void setPosy(int posy) {
        this.posy = posy;
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

    public void increaseRadius() {
        if (this.radius < 100)
            this.radius++;
    }

    public void decreaseRadius() {
        if (this.radius > 1)
            this.radius--;
    }
}
