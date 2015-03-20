package com.example.dhruvil.parkmomo.ui;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.dhruvil.parkmomo.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.util.Date;


public class MapActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{
        protected static final String TAG = "location-updates-sample";
        public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 100;
        public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
                UPDATE_INTERVAL_IN_MILLISECONDS / 2;
        protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
        protected final static String LOCATION_KEY = "location-key";
        protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";
        protected GoogleApiClient mGoogleApiClient;
        protected LocationRequest mLocationRequest;
        protected Location mCurrentLocation;
        protected String mLastUpdateTime;
        TextView seeOfferes;
        GoogleMap map;

        private double myLatitude,myLongitude;

        private int count=0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_map);
                mLastUpdateTime = "";
                // Get a handle to the Map Fragment
                map= ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

                seeOfferes = (TextView)findViewById(R.id.seeOfferes);

                updateValuesFromBundle(savedInstanceState);
                buildGoogleApiClient();

            seeOfferes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MapActivity.this,OfferlistActivity.class);
                    startActivity(i);
                }
            });

        }

        private void updateValuesFromBundle(Bundle savedInstanceState) {
                Log.i(TAG, "Updating values from bundle");
                if (savedInstanceState != null) {

                        if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
                        }
                        if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                                mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
                        }
                        updateUI();
                }
        }

        protected synchronized void buildGoogleApiClient() {
                Log.i(TAG, "Building GoogleApiClient");
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
                createLocationRequest();
        }

        protected void createLocationRequest() {
                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
                mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }

        public void startUpdatesButtonHandler(View view) {

                startLocationUpdates();

        }

        public void stopUpdatesButtonHandler(View view) {

                stopLocationUpdates();

        }

        protected void startLocationUpdates() {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);
        }

        private void setButtonsEnabledState() {

        }

        private void updateUI() {
                try {
                        LatLng latLong = new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
                        if(count==0){

                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, 10));
                                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                map.addMarker(new MarkerOptions()
                                        .position(latLong)
                                        .draggable(true)
                                        .title("YOU ARE HERE"));

                                count++;
                        }


                        map.setMyLocationEnabled(true);
                        map.getUiSettings().setZoomControlsEnabled(true);
                        map.getUiSettings().setCompassEnabled(true);
                        map.getUiSettings().setAllGesturesEnabled(true);

                } catch (Exception e){
                        e.printStackTrace();
                }
        }

        protected void stopLocationUpdates() {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

        @Override
        protected void onStart() {
                super.onStart();
                mGoogleApiClient.connect();
        }

        @Override
        public void onResume() {
                super.onResume();
                if (mGoogleApiClient.isConnected()) {
                        startLocationUpdates();
                }
        }

        @Override
        protected void onPause() {
                super.onPause();
                stopLocationUpdates();
        }

        @Override
        protected void onStop() {
                super.onStop();
                if (mGoogleApiClient.isConnected()) {
                        mGoogleApiClient.disconnect();
                }
        }

        @Override
        public void onConnected(Bundle connectionHint) {
                Log.i(TAG, "Connected to GoogleApiClient");
                if (mCurrentLocation == null) {
                        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                        updateUI();
                }
                startLocationUpdates();
        }

        @Override
        public void onLocationChanged(Location location) {
                mCurrentLocation = location;
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateUI();
//        Toast.makeText(this, getResources().getString(R.string.location_updated_message),
//                Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onConnectionSuspended(int cause) {
                Log.i(TAG, "Connection suspended");
                mGoogleApiClient.connect();
        }

        @Override
        public void onConnectionFailed(ConnectionResult result) {
                Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
        }

        public void onSaveInstanceState(Bundle savedInstanceState) {
                savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
                savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
                super.onSaveInstanceState(savedInstanceState);
        }


}
