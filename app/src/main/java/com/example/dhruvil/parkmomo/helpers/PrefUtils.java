package com.example.dhruvil.parkmomo.helpers;

import android.content.Context;
import android.graphics.Typeface;

import com.example.dhruvil.parkmomo.model.User;

/**
 * Created by xitij on 17-03-2015.
 */
public class PrefUtils {

    /*  public static boolean isEnglish(Context ctx){
        boolean isEnglish = true;
        int selectedLanguage = Prefs.with(ctx).getInt("selected_language",0);
        if(selectedLanguage == 0){
            isEnglish = true;
        }else{
            isEnglish = false;
        }
        return isEnglish;
    }*/





    public static void setCurrentUser(User user, Context ctx){

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_pref", 0);
        complexPreferences.putObject("current_user", user);
        complexPreferences.commit();

    }

    public static User getCurrentUser(Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_pref", 0);
        User user = complexPreferences.getObject("current_user", User.class);
        return user;
    }




}
