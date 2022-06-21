package com.bkit.skinff.model;

import java.io.Serializable;

public class Name implements Serializable {
    private String weapon;
    private String outfit;
    private String weaponMax;
    private String outfitMax;

    public Name() {
    }

    public Name(String weapon, String outfit, String weaponMax, String outfitMax) {
        this.weapon = weapon;
        this.outfit = outfit;
        this.weaponMax = weaponMax;
        this.outfitMax = outfitMax;
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

    public String getWeaponMax() {
        return weaponMax;
    }

    public void setWeaponMax(String weaponMax) {
        this.weaponMax = weaponMax;
    }

    public String getOutfitMax() {
        return outfitMax;
    }

    public void setOutfitMax(String outfitMax) {
        this.outfitMax = outfitMax;
    }
}
