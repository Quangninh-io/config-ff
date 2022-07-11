package com.bkit.skinff.sharepreference;

import static com.bkit.skinff.utilities.Constants.APPEARED;
import static com.bkit.skinff.utilities.Constants.KEY_CHOSE_MODEL;
import static com.bkit.skinff.utilities.Constants.KEY_CODE;
import static com.bkit.skinff.utilities.Constants.KEY_LOGIN;
import static com.bkit.skinff.utilities.Constants.KEY_MODEL;
import static com.bkit.skinff.utilities.Constants.KEY_NAME;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT_SHAR;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT_SHAR_MAX;
import static com.bkit.skinff.utilities.Constants.KEY_TIME;
import static com.bkit.skinff.utilities.Constants.KEY_TYPE;
import static com.bkit.skinff.utilities.Constants.KEY_WEAPON;
import static com.bkit.skinff.utilities.Constants.KEY_WEAPON_SHAR;
import static com.bkit.skinff.utilities.Constants.KEY_WEAPON_SHAR_MAX;
import static com.bkit.skinff.utilities.Constants.SHAREPRE;
import static com.bkit.skinff.utilities.Constants.SHAREPREADMIN;
import static com.bkit.skinff.utilities.Constants.SHAREPRECODE;
import static com.bkit.skinff.utilities.Constants.SHAREPREFILE;
import static com.bkit.skinff.utilities.Constants.SHARE_PRE_GUIDE;
import static com.bkit.skinff.utilities.Constants.SHARE_PRE_STATUS_GUN;
import static com.bkit.skinff.utilities.Constants.SHARE_PRE_STATUS_OUTFIT;

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
    public String getCode(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHAREPRECODE, Context.MODE_PRIVATE);
        return sharedPref.getString(KEY_CODE, "");
    }
    public String getStatusGun(Context context,String dis) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARE_PRE_STATUS_GUN,Context.MODE_PRIVATE);
        String time = sharedPref.getString(KEY_TIME, "");
        String type = sharedPref.getString(KEY_TYPE,"");
        String model = sharedPref.getString(KEY_MODEL,"");
        if(dis.equals(KEY_TYPE)){
            return type;
        }
        if(dis.equals(KEY_MODEL)){
            return model;
        }
        if(dis.equals(KEY_TIME)){
            return time;
        }
        return null;
    }
    public String getStatusOutfit(Context context,String dis) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARE_PRE_STATUS_OUTFIT,Context.MODE_PRIVATE);
        String time = sharedPref.getString(KEY_TIME, "");
        String type = sharedPref.getString(KEY_TYPE,"");
        String model = sharedPref.getString(KEY_MODEL,"");
        if(dis.equals(KEY_TYPE)){
            return type;
        }
        if(dis.equals(KEY_MODEL)){
            return model;
        }
        if(dis.equals(KEY_TIME)){
            return time;
        }
        return null;
    }
    public String getGuide(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(SHARE_PRE_GUIDE,Context.MODE_PRIVATE);
        String guide = sharedPref.getString(KEY_NAME,"");
        return guide;
    }
}
