package com.example.dhruvil.parkmomo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 26-03-2015.
 */
public class OfferUser {


    @SerializedName("CampaignId")
    public int CampaignId;
    @SerializedName("CustomerId")
    public int CustomerId;
    @SerializedName("OfferUserId")
    public int OfferUserId;
    @SerializedName("QRCodeMetaString")
    public String QRCodeMetaString;
    @SerializedName("VehicleId")
    public String VehicleId;

}
