package com.example.dhruvil.parkmomo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dhruvil on 20-03-2015.
 */
public class ParkingList {

    @SerializedName("CampaignArtworks")
    public ArrayList<CampaignArtworks> CampaignArtworks;

    @SerializedName("CampaignDetails")
    public ArrayList<CampaignDetails> CampaignDetails;

    @SerializedName("CampaignLocations")
    public ArrayList<CampaignLocations> CampaignLocations;

}
