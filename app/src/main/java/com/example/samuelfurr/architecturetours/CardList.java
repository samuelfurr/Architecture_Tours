/*
The CardList class is the main activity for the app.  It lists the buildings of architectural interest nearby the user.
The 2 big jobs that CardList has are to format the data nicely, and to get location information.
 */

package com.example.samuelfurr.architecturetours;


import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;

import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class CardList extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    public static RecyclerView rv;
    public static RecyclerView.Adapter mAdapter;
    public static RecyclerView.LayoutManager mLayoutManager;
    private GoogleApiClient googleApiClient;
    static String PLACES_SEARCH_URL =  "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    static ArrayList<String> listed = new ArrayList<>();
    Location lastLocation;
    LocationRequest locationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        rv = (RecyclerView)findViewById(R.id.rv);
        mLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(mLayoutManager);

        googleApiClient = new GoogleApiClient.Builder(this) //Create a new Google Api Client - the app uses the google api client to get place and location data
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        //This method is called if the google api client connects correctly - it sets up a repeating location request in order to get the user's location
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000); //10 seconds
        locationRequest.setFastestInterval(5000); //5 seconds
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        try
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this); //actually request the location updates
        }
        catch (SecurityException se) {
            //Okay, this is the biggest flaw with the app at the moment - we don't check to see if the user has location enabled for the app - I need to do this
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {
        //This method is called when the user's location changes
        lastLocation = location;
        //AIzaSyCZthYKl5KHAFezBEhB2luPuoqCzjNFz6A
        PLACES_SEARCH_URL =  "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
        PLACES_SEARCH_URL = PLACES_SEARCH_URL + "location=" + String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()) + "&radius=1&key=AIzaSyCZthYKl5KHAFezBEhB2luPuoqCzjNFz6A";
        //This URL requests places within a 1 meter radius from our user's location using the Google Places API
        //The one meter radius means that we only get the city and neighborhood from google places - you'll see why later

        PullNearbyPlaces pnp = new PullNearbyPlaces();
        pnp.execute();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //We don't need to do anything here, mainly because I wouldn't know what to do
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Hopefully this doesn't ever get called, as it would be a surprise to the user - this method displays the error code
        new AlertDialog.Builder(this)
                .setTitle("Test")
                .setMessage(String.valueOf(connectionResult.getErrorCode()))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    protected void onStart() {
        googleApiClient.connect(); //connects to our google api client, onStart is called after onCreate
        super.onStart();
    }

    @Override
    protected void onStop() { //called when the app terminates
        googleApiClient.disconnect();
        super.onStop();
    }

}
