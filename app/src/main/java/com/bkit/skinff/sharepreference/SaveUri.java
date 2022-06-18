package com.bkit.skinff.sharepreference;

import static com.bkit.skinff.utilities.Constants.CHECK;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT;
import static com.bkit.skinff.utilities.Constants.KEY_WEAPON;
import static com.bkit.skinff.utilities.Constants.SHAREPRE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SaveUri {
    public void saveData(Context context, String uriWeapon, String uriOutfit){
        SharedPreferences sharedPref = context.getSharedPreferences(SHAREPRE,Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_WEAPON,uriWeapon);
        editor.putString(KEY_OUTFIT,uriOutfit);
        editor.apply();
    }
}
