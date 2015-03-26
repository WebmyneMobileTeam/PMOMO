package com.example.dhruvil.parkmomo.helpers;

/**
 * Created by Android on 05-12-2014.
 */
public class AppConstants {

    // Base url for the webservice
    public static String BASE_URL = "http://ws-srv-net.in.webmyne.com/Applications/ParkingWS/";
    public static String LOGIN = BASE_URL + "Merchant.svc/json/UserLoginDetails";
    public static String OFFER_LIST = BASE_URL + "Merchant.svc/json/ParkingLocationList/";
    public static String IMAGE_PATH="http://ws-srv-net.in.webmyne.com/Applications/Parking";
    public static String SAVE_OFFER ="http://ws-srv-net.in.webmyne.com/Applications/ParkingWS/Campaign.svc/json/SaveUserOffer";
    public static int OPEN=1;
    public static int PENDING=2;
    public static int VALIDATED=3;
    public static int EXPIRED=4;
    public static int RESCHEDULED=5;

}


