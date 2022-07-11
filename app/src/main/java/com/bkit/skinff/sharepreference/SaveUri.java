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

// use to save uri
public class SaveUri {
    static SaveUri instance;
    public static SaveUri getInstance(){
        if(instance==null){
            return new SaveUri();
        }
        return instance;
    }
    public void saveAccountAdmin(Context context,boolean statusLogin){
        SharedPreferences sharedPref = context.getSharedPreferences(SHAREPREADMIN,Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(KEY_LOGIN,statusLogin);
        editor.apply();
    }
    // save uri user chose
    public void saveData(Context context, String uriWeapon, String uriOutfit, String choseModel){
        SharedPreferences sharedPref = context.getSharedPreferences(SHAREPRE,Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_WEAPON,uriWeapon);
        editor.putString(KEY_OUTFIT,uriOutfit);
        editor.putString(KEY_CHOSE_MODEL,choseModel);
        editor.apply();
    }
    // save uri of model ff if exist
    public void saveUriModelFF(Context context, String uriWeapon, String uriOutfit){
        SharedPreferences sharedPref = context.getSharedPreferences(SHAREPREFILE,Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_WEAPON_SHAR,uriWeapon);
        editor.putString(KEY_OUTFIT_SHAR,uriOutfit);
        editor.apply();
    }
    //save uri of model ff max if exist
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
    public void saveLanguage(Context context, String code){
        SharedPreferences sharedPref = context.getSharedPreferences(SHAREPRECODE,Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_CODE,code);
        editor.apply();
    }
    public void saveStatusGun(Context context,String model, String type, String time){
        SharedPreferences sharedPref = context.getSharedPreferences(SHARE_PRE_STATUS_GUN,Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_MODEL,model);
        editor.putString(KEY_TYPE,type);
        editor.putString(KEY_TIME,time);
        editor.apply();
    }

    public void saveStatusOutfit(Context context,String model, String type, String time){
        SharedPreferences sharedPref = context.getSharedPreferences(SHARE_PRE_STATUS_OUTFIT,Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_MODEL,model);
        editor.putString(KEY_TYPE,type);
        editor.putString(KEY_TIME,time);
        editor.apply();
    }

    public void saveStatusClearDataGun(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(SHARE_PRE_STATUS_GUN,Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }
    public void saveStatusClearDataOutFit(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(SHARE_PRE_STATUS_OUTFIT,Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }
    public void saveGuide(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(SHARE_PRE_GUIDE,Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_NAME,APPEARED);
        editor.apply();
    }
}
