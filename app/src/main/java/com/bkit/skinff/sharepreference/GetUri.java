package com.bkit.skinff.sharepreference;

import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT;
import static com.bkit.skinff.utilities.Constants.KEY_WEAPON;
import static com.bkit.skinff.utilities.Constants.SHAREPRE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class GetUri {
    private static GetUri instance;
    public static GetUri getInstance(){
        if(instance==null){
            return new GetUri();
        }
        return instance;
    }
    public String getData(Context context, String type){
        SharedPreferences sharedPref = context.getSharedPreferences(SHAREPRE,Context.MODE_PRIVATE);
        String uriWeapon = sharedPref.getString(KEY_WEAPON,"");
        String uriOutfit = sharedPref.getString(KEY_OUTFIT,"");
        if (type.equals(KEY_WEAPON)){
            return uriWeapon;
        }
        if(type.equals(KEY_OUTFIT)){
            return uriOutfit;
        }
        return null;
    }
}
