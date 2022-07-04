package com.bkit.skinff.utilities;

import android.content.Context;
import com.bkit.skinff.sharepreference.GetUri;
import java.util.Locale;

public class SetLanguage {

    private static SetLanguage instance;
    public static SetLanguage getInstance(){
        if (instance==null){
            return new SetLanguage();
        }
        return instance;
    }

    public void configLanguage(Context context){
        String code = GetUri.getInstance().getCode(context);
        if(!code.equals("")){
            LanguageManager.getInstance().updateLanguage(context,code);
        }else{
            String language = Locale.getDefault().getDisplayLanguage();
            if(language.equals("Tiếng Việt")){
                LanguageManager.getInstance().updateLanguage(context,"vi");
            }else{
                LanguageManager.getInstance().updateLanguage(context,"en");
            }
        }
    }
}
