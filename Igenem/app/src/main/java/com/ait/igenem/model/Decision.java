package com.ait.igenem.model;

import java.util.List;

/**
 * Created by Erinna on 11/23/16.
 */

public class Decision {

    private String name;
    private String color;
    private List<Factor> factorList;

    public Decision(String name, String color, List<Factor> factorList) {
        this.name = name;
        this.color = color;
        this.factorList = factorList;
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

    public List<Factor> getFactorList() {
        return factorList;
    }

    public void setFactorList(List<Factor> factorList) {
        this.factorList = factorList;
    }
}
