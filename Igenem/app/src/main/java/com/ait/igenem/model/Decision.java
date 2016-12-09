package com.ait.igenem.model;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by Erinna on 11/23/16.
 */

public class Decision implements Serializable {

    private String name;
    private String color;
    private String owner;
    private String ownerId;
    private int totalScore;
    private int proScore;

    public Decision() {}

    public Decision(String name, String color, String owner, String ownerId) {
        this.name = name;
        this.color = color;
        this.owner = owner;
        this.ownerId = ownerId;
        this.totalScore = 0;
        this.proScore = 0;
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

    public void updateScore(int addedValue, boolean isPro){
        this.totalScore += addedValue;
        if(isPro){
            this.proScore += addedValue;
        }
    }

    public double getPercentPro() {
        if(this.totalScore == 0){
            return 0.0;
        }
        else {
            return (double) this.proScore / this.totalScore;
        }
    }
    public int getTotalScore() {
        return totalScore;
    }

    public int getProScore() {
        return proScore;
    }

    public void increase(boolean isPro){
        this.totalScore++;
        if(isPro){
            this.proScore++;
        }
    }

    public void decrease(boolean isPro){
        this.totalScore--;
        if(isPro){
            this.proScore--;
        }
    }

}