package com.bkit.skinff.utilities;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import com.bkit.skinff.sharepreference.SaveUri;
import java.util.Locale;

public class LanguageManager {
    private static LanguageManager instance;

    public static LanguageManager getInstance(){
        if(instance==null){
            return new LanguageManager();
        }
        return instance;
    }
    public void updateLanguage(Context context, String code) {
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        SaveUri.getInstance().saveLanguage(context,code);
    }
}
