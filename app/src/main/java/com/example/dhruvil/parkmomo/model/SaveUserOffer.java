package com.example.dhruvil.parkmomo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 26-03-2015.
 */
public class SaveUserOffer {

    @SerializedName("Latitide")
    public String Latitide;
    @SerializedName("Longitude")
    public String Longitude;
    @SerializedName("ResponseCode")
    public String ResponseCode;
    @SerializedName("ResponseMsg")
    public String ResponseMsg;
    @SerializedName("StatusTypeValueId")
    public int StatusTypeValueId;
    @SerializedName("offerUser")
    public OfferUser offerUser;

}
