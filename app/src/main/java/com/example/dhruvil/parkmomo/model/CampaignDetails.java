package com.example.dhruvil.parkmomo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dhruvil on 20-03-2015.
 */
public class CampaignDetails {

    @SerializedName("CampaignDescription")
    public String CampaignDescription;

    @SerializedName("CampaignId")
    public long CampaignId;

    @SerializedName("CampaignTitle")
    public String CampaignTitle;

    @SerializedName("EndDate")
    public String EndDate;

    @SerializedName("StartDate")
    public String StartDate;

}
