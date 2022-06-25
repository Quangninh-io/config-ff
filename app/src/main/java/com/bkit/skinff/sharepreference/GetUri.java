package com.bkit.skinff.sharepreference;

import static com.bkit.skinff.utilities.Constants.KEY_CHOSE_MODEL;
import static com.bkit.skinff.utilities.Constants.KEY_LOGIN;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT_SHAR;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT_SHAR_MAX;
import static com.bkit.skinff.utilities.Constants.KEY_WEAPON;
import static com.bkit.skinff.utilities.Constants.KEY_WEAPON_SHAR;
import static com.bkit.skinff.utilities.Constants.KEY_WEAPON_SHAR_MAX;
import static com.bkit.skinff.utilities.Constants.SHAREPRE;
import static com.bkit.skinff.utilities.Constants.SHAREPREADMIN;
import static com.bkit.skinff.utilities.Constants.SHAREPREFILE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

// use to get value form share preference
// if data exist, use haven't to choose uri again
public class GetUri {
    static GetUri instance;

    public static GetUri getInstance() {
        if (instance == null) {
            return new GetUri();
        }
        return instance;
    }

    public boolean getAccountAdmin(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHAREPREADMIN, Context.MODE_PRIVATE);
        boolean statusLogin = sharedPref.getBoolean(KEY_LOGIN,false);
        return statusLogin;
    }

    // handle get data
    // return uri corresponding with passed data
    public String getData(Context context, String type) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHAREPRE, Context.MODE_PRIVATE);
        String uriWeapon = sharedPref.getString(KEY_WEAPON, "");
        String uriOutfit = sharedPref.getString(KEY_OUTFIT, "");
        String keyChoseModel = sharedPref.getString(KEY_CHOSE_MODEL, "");
        if (type.equals(KEY_WEAPON)) {
            return uriWeapon;
        }
        if (type.equals(KEY_OUTFIT)) {
            return uriOutfit;
        }
        if (type.equals(KEY_CHOSE_MODEL)) {
            return keyChoseModel;
        }
        return null;
    }

    // get uri of model ff
    public String getDataFF(Context context, String type) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHAREPREFILE, Context.MODE_PRIVATE);
        String uriWeapon = sharedPref.getString(KEY_WEAPON_SHAR, "");
        String uriOutfit = sharedPref.getString(KEY_OUTFIT_SHAR, "");
        if (type.equals(KEY_WEAPON)) {
            return uriWeapon;
        }
        if (type.equals(KEY_OUTFIT)) {
            return uriOutfit;
        }
        return null;
    }

    // get uri of model ff max
    public String getDataFFMAX(Context context, String type) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHAREPREFILE, Context.MODE_PRIVATE);
        String uriWeapon = sharedPref.getString(KEY_WEAPON_SHAR_MAX, "");
        String uriOutfit = sharedPref.getString(KEY_OUTFIT_SHAR_MAX, "");
        if (type.equals(KEY_WEAPON)) {
            return uriWeapon;
        }
        if (type.equals(KEY_OUTFIT)) {
            return uriOutfit;
        }
        return null;
    }

}
