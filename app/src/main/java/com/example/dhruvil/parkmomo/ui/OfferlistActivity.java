package com.example.dhruvil.parkmomo.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.dhruvil.parkmomo.R;
import com.example.dhruvil.parkmomo.helpers.AppConstants;
import com.example.dhruvil.parkmomo.helpers.CallWebService;
import com.example.dhruvil.parkmomo.helpers.ComplexPreferences;
import com.example.dhruvil.parkmomo.helpers.PrefUtils;
import com.example.dhruvil.parkmomo.model.Offer;
import com.example.dhruvil.parkmomo.model.ParkingList;
import com.google.gson.GsonBuilder;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class OfferlistActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private ProgressDialog pd;
    private ListView listHomeProducts;
    private ArrayList<ParkingList> parkingLists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_list);
        
        listHomeProducts = (ListView) findViewById(R.id.listHomeProducts);

        

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Offers");
//            toolbar.setLogo(R.drawable.logo);
            setSupportActionBar(toolbar);
        }
        parkingLists=PrefUtils.getOffers(OfferlistActivity.this).Parkinglst;
        MyAppAdapter adapter = new MyAppAdapter(parkingLists,OfferlistActivity.this);
        listHomeProducts.setAdapter(adapter);

    }




    public class MyAppAdapter extends BaseAdapter {

        public class ViewHolder {
            public TextView text;
            public ImageView image;
            public TextView description;
        }

        public List<ParkingList> parkingList;
        public Context context;

        private MyAppAdapter(List<ParkingList> apps, Context context) {
            this.parkingList = apps;
            this.context = context;
        }

        @Override
        public int getCount() {
            return parkingList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View rowView = convertView;
            ViewHolder viewHolder;

            if (rowView == null) {

                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.offerlist_item_category, null);
                // configure view holder
                viewHolder = new ViewHolder();
                viewHolder.text = (TextView) rowView.findViewById(R.id.txtNameHomeProduct);
                viewHolder.image = (ImageView) rowView.findViewById(R.id.imgHomeProduct);
                viewHolder.description = (TextView) rowView.findViewById(R.id.txtDetailHomeProduct);

                rowView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

//            Log.e("image path",AppConstants.IMAGE_PATH + parkingList.get(position).CampaignArtworks.ImageArt+"");
            viewHolder.text.setText(parkingList.get(position).CampaignDetails.CampaignTitle);
            viewHolder.description.setText(parkingList.get(position).CampaignDetails.CampaignDescription);
            Glide.with(OfferlistActivity.this)
                    .load(AppConstants.IMAGE_PATH + parkingList.get(position).CampaignArtworks.ImageArt)
                    .placeholder(R.drawable.logo)
                    .centerCrop()
                    .into(viewHolder.image);
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PrefUtils.setSingleOffer(parkingList.get(position),OfferlistActivity.this);
                    PrefUtils.setValidatedOffer(false, OfferlistActivity.this);
                    Intent i = new Intent(OfferlistActivity.this, OfferDetailActivity.class);
                    startActivity(i);
                }
            });

            return rowView;
        }
    }

}
