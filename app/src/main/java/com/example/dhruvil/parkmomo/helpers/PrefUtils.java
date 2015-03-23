package com.example.dhruvil.parkmomo.helpers;

import android.content.Context;
import android.graphics.Typeface;

import com.example.dhruvil.parkmomo.model.Offer;
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

    public static void setOffers(Offer offer, Context ctx){

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "offer_pref", 0);
        complexPreferences.putObject("current_offer", offer);
        complexPreferences.commit();

    }

    public static Offer getOffers(Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "offer_pref", 0);
        Offer offer = complexPreferences.getObject("current_offer", Offer.class);
        return offer;
    }

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

    public static Typeface getTypeFace(Context ctx){
        Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "candara.TTF");
        return  typeface;
    }



}
