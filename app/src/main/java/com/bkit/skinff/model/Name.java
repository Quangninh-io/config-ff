package com.bkit.skinff.model;

import java.io.Serializable;

public class Name implements Serializable {
    private String weapon;
    private String outfit;

    public Name() {
    }

    public Name(String weapon, String outfit) {
        this.weapon = weapon;
        this.outfit = outfit;
    }

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }

    public String getOutfit() {
        return outfit;
    }

    public void setOutfit(String outfit) {
        this.outfit = outfit;
    }
}
