package com.example.dhruvil.parkmomo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dhruvil on 20-03-2015.
 */
public class Offer {

//this are the temporarory fields
    public String name;
    public String shortDetail;
    public String description;
    public int imgRes;
// ends

    @SerializedName("Parkinglst")
    public ArrayList<ParkingList> Parkinglst;

}
