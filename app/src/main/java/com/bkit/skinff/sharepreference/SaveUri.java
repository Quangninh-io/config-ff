package com.bkit.skinff.sharepreference;

import static com.bkit.skinff.utilities.Constants.CHECK;
import static com.bkit.skinff.utilities.Constants.KEY_CHOSE_MODEL;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT_MAX;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT_SHAR;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT_SHAR_MAX;
import static com.bkit.skinff.utilities.Constants.KEY_WEAPON;
import static com.bkit.skinff.utilities.Constants.KEY_WEAPON_MAX;
import static com.bkit.skinff.utilities.Constants.KEY_WEAPON_SHAR;
import static com.bkit.skinff.utilities.Constants.KEY_WEAPON_SHAR_MAX;
import static com.bkit.skinff.utilities.Constants.SHAREPRE;
import static com.bkit.skinff.utilities.Constants.SHAREPREFILE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SaveUri {
    private static SaveUri instance;
    public static SaveUri getInstance(){
        if(instance==null){
            return new SaveUri();
        }
        return instance;
    }

    public void saveData(Context context, String uriWeapon, String uriOutfit, String choseModel){
        SharedPreferences sharedPref = context.getSharedPreferences(SHAREPRE,Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_WEAPON,uriWeapon);
        editor.putString(KEY_OUTFIT,uriOutfit);
        editor.putString(KEY_CHOSE_MODEL,choseModel);
        editor.apply();
    }

    public void saveUriModelFF(Context context, String uriWeapon, String uriOutfit){
        SharedPreferences sharedPref = context.getSharedPreferences(SHAREPREFILE,Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_OUTFIT_SHAR,uriWeapon);
        editor.putString(KEY_WEAPON_SHAR,uriOutfit);

        editor.apply();
    }
    public void saveUriModelFFMAX(Context context,String uriWeaponMax, String uriOutfitMax){
        SharedPreferences sharedPref = context.getSharedPreferences(SHAREPREFILE,Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_WEAPON_SHAR_MAX,uriWeaponMax);
        editor.putString(KEY_OUTFIT_SHAR_MAX,uriOutfitMax);
        editor.apply();
    }
    public void deleteData(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(SHAREPRE,Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }
}
