package com.example.dhruvil.parkmomo.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

import com.example.dhruvil.parkmomo.model.Latitudelongitude;
import com.example.dhruvil.parkmomo.model.Offer;
import com.example.dhruvil.parkmomo.model.ParkingList;
import com.example.dhruvil.parkmomo.model.SaveUserOffer;
import com.example.dhruvil.parkmomo.model.UserClass;

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

    public static void setSingleOffer(ParkingList offer, Context ctx){

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "single_offer_pref", 0);
        complexPreferences.putObject("single_current_offer", offer);
        complexPreferences.commit();

    }

    public static ParkingList getSingleOffer(Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "single_offer_pref", 0);
        ParkingList offer = complexPreferences.getObject("single_current_offer", ParkingList.class);
        return offer;
    }



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

    public static void setCurrentUser(UserClass currentUser, Context ctx){

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_prefs", 0);
        complexPreferences.putObject("current_user_value", currentUser);
        complexPreferences.commit();
    }

    public static UserClass getCurrentUser(Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_prefs", 0);
        UserClass currentUser = complexPreferences.getObject("current_user_value", UserClass.class);
        return currentUser;
    }

    public static Typeface getTypeFace(Context ctx){
        Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "candara.TTF");
        return  typeface;
    }


    public static void setLatLng(Latitudelongitude latLng, Context ctx){

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "latlng_pref", 0);
        complexPreferences.putObject("current_latlng", latLng);
        complexPreferences.commit();

    }

    public static Latitudelongitude getLatLng(Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "latlng_pref", 0);
        Latitudelongitude latlong = complexPreferences.getObject("current_latlng", Latitudelongitude.class);
        return latlong;
    }

    public static void setLogin(boolean login, Context ctx){

        SharedPreferences  preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLogin", login);
        editor.commit();

    }

    public static boolean getLogin(Context ctx){
        SharedPreferences  preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean login=preferences.getBoolean("isLogin", false);
        return  login;
    }


    public static void setSaveUserOffer(SaveUserOffer userOffer, Context ctx){

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "save_user_offer", 0);
        complexPreferences.putObject("save_user_offer_value", userOffer);
        complexPreferences.commit();
    }

    public static SaveUserOffer getSaveUserOffer(Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "save_user_offer", 0);
        SaveUserOffer userOffer = complexPreferences.getObject("save_user_offer_value", SaveUserOffer.class);
        return userOffer;
    }


}
