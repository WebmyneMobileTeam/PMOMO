package com.example.dhruvil.parkmomo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 26-03-2015.
 */
public class QRcode {

    @SerializedName("IsActive")
    public boolean IsActive;
    @SerializedName("MerchantId")
    public int MerchantId;
    @SerializedName("MerchantQRId")
    public  int MerchantQRId;
    @SerializedName("QRCode")
    public String QRCode;
    @SerializedName("QRMetadata")
    public String QRMetadata;


}


