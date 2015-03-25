package com.example.dhruvil.parkmomo.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.dhruvil.parkmomo.R;
import com.example.dhruvil.parkmomo.helpers.PrefUtils;
import com.example.dhruvil.parkmomo.model.ParkingList;

import java.util.ArrayList;

public class ConfirmActivity extends ActionBarActivity {
    private ArrayList<ParkingList> parkingLists;
    private TextView freePark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        parkingLists= PrefUtils.getOffers(ConfirmActivity.this).Parkinglst;
        freePark= (TextView) findViewById(R.id.freePark);
        freePark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(parkingLists ==null || parkingLists.isEmpty()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmActivity.this);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setMessage("There is no sponsored parking around you.");
                    dialog.show();
                } else {

                    Intent intent = new Intent(ConfirmActivity.this, OfferlistActivity.class);
                    startActivity(intent);

                }
            }
        });


    }


}
