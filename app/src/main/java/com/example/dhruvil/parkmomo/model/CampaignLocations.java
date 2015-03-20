package com.example.dhruvil.parkmomo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dhruvil on 20-03-2015.
 */
public class CampaignLocations {

    @SerializedName("Address")
    public String Address;

    @SerializedName("CampaignId")
    public long CampaignId;

    @SerializedName("CampaignLocationId")
    public long CampaignLocationId;

    @SerializedName("IsBussiness")
    public boolean IsBussiness;

    @SerializedName("Latitude")
    public String Latitude;

    @SerializedName("Logitude")
    public String Logitude;

    @SerializedName("Radius")
    public float Radius;

    @SerializedName("RadiusUnit")
    public String RadiusUnit;

    @SerializedName("ResponseCode")
    public String ResponseCode;

    @SerializedName("ResponseMsg")
    public String ResponseMsg;



}
