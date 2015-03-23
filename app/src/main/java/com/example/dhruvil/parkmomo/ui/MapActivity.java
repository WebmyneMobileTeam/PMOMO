package com.example.dhruvil.parkmomo.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dhruvil.parkmomo.R;
import com.example.dhruvil.parkmomo.model.Offer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MapActivity extends ActionBarActivity implements GoogleMap.OnMapClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
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
        GoogleMap map;
        Marker marker;
        private EditText latitude, longitude;
        private TextView seeOfferes;
        private AutoCompleteTextView address;
        private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
        private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
        private static final String TYPE_DETAILS = "/details";
        private static final String OUT_JSON = "/json";
        private static final String API_KEY = "AIzaSyCVw1OuoN7tuNzHW70M4pfPEF4_F_oeBPo";
        private ArrayList<String> place_ids;
        private LatLng latLng;
        private int count = 0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_map);
                mLastUpdateTime = "";
                latitude = (EditText) findViewById(R.id.latitude);
                longitude = (EditText) findViewById(R.id.longitude);
                address = (AutoCompleteTextView) findViewById(R.id.address);
                address.setAdapter(new PlacesAutoCompleteAdapter(MapActivity.this, R.layout.auto_complete_item));
                address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @TargetApi(Build.VERSION_CODES.CUPCAKE)
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                                new AsyncTask<Void, Void, Void>() {

                                        @Override
                                        protected Void doInBackground(Void... voids) {

                                                latLng = getLatLng(place_ids.get(position));

                                                return null;
                                        }

                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                                super.onPostExecute(aVoid);

                                                //  Toast.makeText(getActivity(),ll.latitude+"    :   "+ll.longitude, Toast.LENGTH_SHORT).show();

                                                if (marker != null) {
                                                        marker.remove();
                                                }
                                                marker = map.addMarker(new MarkerOptions()
                                                        .position(latLng)
                                                        .draggable(true)
                                                        .title("YOU ARE HERE"));
                                                latitude.setText(mCurrentLocation.getLatitude() + "");
                                                longitude.setText(mCurrentLocation.getLongitude() + "");


                                        }
                                }.execute();
                        }
                });

                // Get a handle to the Map Fragment
                map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
                map.setOnMapClickListener(this);
                map.getUiSettings().setCompassEnabled(true);
                map.getUiSettings().setAllGesturesEnabled(true);

                seeOfferes = (TextView) findViewById(R.id.seeOfferes);
                seeOfferes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Intent i = new Intent(MapActivity.this, OfferlistActivity.class);
                                startActivity(i);
                        }
                });


                updateValuesFromBundle(savedInstanceState);
                buildGoogleApiClient();

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
                        LatLng latLong = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                        if (count == 0) {

                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, 10));
                                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                if (marker != null) {
                                        marker.remove();
                                }
                                marker = map.addMarker(new MarkerOptions()
                                        .position(latLong)
                                        .draggable(true)
                                        .title("YOU ARE HERE"));
                                latitude.setText(mCurrentLocation.getLatitude() + "");
                                longitude.setText(mCurrentLocation.getLongitude() + "");
                                address.setText(getAddress(MapActivity.this, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
                                count++;
                        }


                } catch (Exception e) {
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


        @Override
        public void onMapClick(LatLng latLng) {
                if (marker != null) {
                        marker.remove();
                }
                marker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .draggable(true)
                        .title("YOU ARE HERE"));
                latitude.setText(latLng.latitude + "");
                longitude.setText(latLng.longitude + "");
                address.setText(getAddress(MapActivity.this, latLng.latitude, latLng.longitude));

        }

        public String getAddress(Context ctx, double latitude, double longitude) {
                StringBuilder result = new StringBuilder();
                try {
                        Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        if (addresses.size() > 0) {
                                Address address = addresses.get(0);
                                String locality = address.getLocality();
                                String city = address.getCountryName();
                                String region_code = address.getCountryCode();
                                String zipcode = address.getPostalCode();
                                String street = address.getAddressLine(0);
                                String street2 = address.getAddressLine(1);

                                double lat = address.getLatitude();
                                double lon = address.getLongitude();

                                if (street != null) {
                                        result.append(street + " ");
                                }

                                if (street2 != null) {
                                        result.append(street2 + " ");
                                }

                                if (locality != null) {
                                        result.append(locality + " ");
                                }


                                if (city != null) {
                                        result.append(city + " " + region_code + " ");
                                }

                                if (zipcode != null) {
                                        result.append(zipcode);
                                }

                        }
                } catch (IOException e) {
                        Log.e("tag", e.getMessage());
                }

                return result.toString();
        }
        private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

                private ArrayList<String> resultList;


                public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
                        super(context, textViewResourceId);
                }

                @Override
                public int getCount() {
                        return resultList.size();
                }

                @Override
                public String getItem(int index) {
                        return resultList.get(index);
                }

                @Override
                public Filter getFilter() {

                        Filter filter = new Filter() {
                                @Override
                                protected android.widget.Filter.FilterResults performFiltering(CharSequence constraint) {
                                        FilterResults filterResults = new FilterResults();
                                        if (constraint != null) {
                                                // Retrieve the autocomplete results.
                                                resultList = autocomplete(constraint.toString());

                                                // Assign the data to the FilterResults
                                                filterResults.values = resultList;
                                                filterResults.count = resultList.size();
                                        }
                                        return filterResults;
                                }

                                @Override
                                protected void publishResults(CharSequence constraint, FilterResults results) {
                                        if (results != null && results.count > 0) {
                                                notifyDataSetChanged();
                                        }
                                        else {
                                                notifyDataSetInvalidated();
                                        }
                                }};
                        return filter;
                }
        }

        private ArrayList<String> autocomplete(String input) {


                ArrayList<String> resultList = null;
                HttpURLConnection conn = null;
                StringBuilder jsonResults = new StringBuilder();
                try {
                        StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
                        sb.append("?key=" + API_KEY);
                        sb.append("&components=country:in");
                        sb.append("&input=" + URLEncoder.encode(input, "utf8"));

//            Log.e("Log Url PlaceAPI",sb.toString());
                        URL url = new URL(sb.toString());
                        conn = (HttpURLConnection) url.openConnection();
                        InputStreamReader in = new InputStreamReader(conn.getInputStream());

                        // Load the results into a StringBuilder
                        int read;
                        char[] buff = new char[1024];
                        while ((read = in.read(buff)) != -1) {
                                jsonResults.append(buff, 0, read);
                        }
                } catch (MalformedURLException e) {
                        Log.e("error", "Error processing Places API URL", e);
                        return resultList;
                } catch (IOException e) {
                        Log.e("error", "Error connecting to Places API", e);
                        return resultList;
                } finally {
                        if (conn != null) {
                                conn.disconnect();
                        }
                }

                try {
                        // Create a JSON object hierarchy from the results
                        JSONObject jsonObj = new JSONObject(jsonResults.toString());
                        JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

                        // Extract the Place descriptions from the results
                        resultList = new ArrayList<String>(predsJsonArray.length());
                        place_ids = new ArrayList<String>(predsJsonArray.length());

                        for (int i = 0; i < predsJsonArray.length(); i++) {
                                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
                                place_ids.add(predsJsonArray.getJSONObject(i).getString("place_id"));
                        }
                } catch (JSONException e) {
                        Log.e("error", "Cannot process JSON results", e);
                }

                return resultList;
        }

        private LatLng getLatLng(String place_id){

                LatLng latLng = null;

                HttpURLConnection conn = null;
                StringBuilder jsonResults = new StringBuilder();
                try {
                        StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_DETAILS + OUT_JSON);
                        sb.append("?placeid="+place_id);
                        sb.append("&key=" + API_KEY);

                        URL url = new URL(sb.toString());
                        conn = (HttpURLConnection) url.openConnection();
                        InputStreamReader in = new InputStreamReader(conn.getInputStream());

                        // Load the results into a StringBuilder
                        int read;
                        char[] buff = new char[1024];
                        while ((read = in.read(buff)) != -1) {
                                jsonResults.append(buff, 0, read);
                        }
                } catch (MalformedURLException e) {
                        Log.e("error", "Error processing Places API URL", e);
                        return latLng;
                } catch (IOException e) {
                        Log.e("error", "Error connecting to Places API", e);
                        return latLng;
                } finally {
                        if (conn != null) {
                                conn.disconnect();
                        }
                }

                try {
                        // Create a JSON object hierarchy from the results

                        JSONObject jsonObj = new JSONObject(jsonResults.toString());
                        JSONObject predsJsonObj = jsonObj.getJSONObject("result");
                        JSONObject objGeoMet = predsJsonObj.getJSONObject("geometry");
                        JSONObject objLoc = objGeoMet.getJSONObject("location");

                        double lat = Double.parseDouble(objLoc.getString("lat"));
                        double lng = Double.parseDouble(objLoc.getString("lng"));
                        latLng = new LatLng(lat,lng);

                } catch (JSONException e) {
                        Log.e("error", "Cannot process JSON results", e);
                }


                return  latLng;

        }

}
