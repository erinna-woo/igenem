package com.ait.igenem.model;

import java.io.Serializable;

/**
 * Created by Erinna on 11/23/16.
 */

public class Decision implements Serializable {

    private String name;
    private int color;
    private String owner;
    private String ownerId;
    private int totalScore;
    private int proScore;

    public Decision() {
    }

    public Decision(String name, int color, String owner, String ownerId) {
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
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

    public void updateScoreNewBlob(int addedValue, boolean isPro) {
        this.totalScore += addedValue;
        if (isPro) {
            this.proScore += addedValue;
        }
    }

    public float getPercentPro() {
        if (this.totalScore == 0) {
            return 0;
        } else {
            return (float) this.proScore / this.totalScore;
        }
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getProScore() {
        return proScore;
    }

    public void increase(boolean isPro) {
        this.totalScore++;
        if (isPro) {
            this.proScore++;
        }
    }

    public void decrease(boolean isPro) {
        this.totalScore--;
        if (isPro) {
            this.proScore--;
        }
    }

    public void updateDecisionScoreDeleteBlob(int removedValue, boolean isPro) {
        this.totalScore -= removedValue;
        if (isPro) {
            this.proScore -= removedValue;
        }
    }
}