package com.example.dhruvil.parkmomo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dhruvil on 20-03-2015.
 */
public class User {

    @SerializedName("EmailId")
    public String email;

    @SerializedName("MobileCountryCode")
    public String countryCode;

    @SerializedName("MobileNo")
    public String mobile;

    @SerializedName("Password")
    public String password;

    @SerializedName("UserId")
    public int userID;

    @SerializedName("UserRole")
    public int role;

}
