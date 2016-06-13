package com.example.omigayy.comp590hw3;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
                   GoogleApiClient.OnConnectionFailedListener,com.google.android.gms.location.LocationListener{


    private static GoogleApiClient mGoogleApiClient = null;
    private static String add;
    private static LocationRequest mLocationRequest;
    private static MediaPlayer mediaPlayer = null;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            Location loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Log.v("LOC", "" + loc.getLatitude() + ", " + loc.getLongitude());
            String s = "Coordination: " + loc.getLatitude() + ", " + loc.getLongitude();
            TextView coor = (TextView) findViewById(R.id.coor);
            coor.setText(s);

//            new Thread(new Runnable(){
//                public void run(){
//                    Geocoder g = new Geocoder(this, Locale.getDefault());
//                    try {
//                        List<Address> la = g.getFromLocation(loc.getLatitude(),
//                                loc.getLongitude(), 1);
//                        add = la.get(0).getAddressLine(0).toString();
//                        String address = "Address: " + add;
//                        TextView addr = (TextView) findViewById(R.id.addr);
//                        addr.setText(address);
//                    } catch(Exception ex){}
//                }
//            }).start();

            Geocoder g = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> la = g.getFromLocation(loc.getLatitude(),
                                                    loc.getLongitude(), 1);
                add = la.get(0).getAddressLine(0).toString();
                String address = "Address: " + add;
                TextView addr = (TextView) findViewById(R.id.addr);
                addr.setText(address);
            } catch(Exception ex){}

            createLocationRequest();

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);// startLocationRequests

            ImageView iv = (ImageView) findViewById(R.id.fixedMap);
            if (add.equals("201 S Columbia St")){
                iv.setImageResource(R.drawable.map_sn);

                mediaPlayer =  MediaPlayer.create(this, R.raw.cool_kids);
                mediaPlayer.start();
            }
            else if(add.equals("250 E Cameron Ave")){
                iv.setImageResource(R.drawable.map_ow);
                mediaPlayer = MediaPlayer.create(this, R.raw.sugar);
                mediaPlayer.start();
            }
            else if(add.equals("200 S Rd")){
                iv.setImageResource(R.drawable.map_polk);

                mediaPlayer = MediaPlayer.create(this, R.raw.sunshine);
                mediaPlayer.start();
            }
            else{
                iv.setImageResource(R.drawable.map);
                if (mediaPlayer!=null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            }

        }catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart(){
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
        super.onStart();
//        try {
//            LocationServices.FusedLocationApi.requestLocationUpdates(
//                    mGoogleApiClient, mLocationRequest, this);// startLocationRequests
//        }catch (SecurityException ex) {
//            ex.printStackTrace();
//        }

    }

    @Override
    protected void onStop(){
        mGoogleApiClient.disconnect();
        super.onStop();
        if (mediaPlayer!=null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        String s = "Coordination: " + location.getLatitude() + ", " + location.getLongitude();
        TextView coor = (TextView) findViewById(R.id.coor);
        coor.setText(s);
        Geocoder g = new Geocoder(this, Locale.getDefault());
        try{
            List<Address> la = g.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            add = la.get(0).getAddressLine(0).toString();
            String address = "Address: " + add;
            TextView addr = (TextView) findViewById(R.id.addr);
            addr.setText(address);
        }catch (Exception ex){

        }
        ImageView iv = (ImageView) findViewById(R.id.fixedMap);
        if (add.equals("201 S Columbia St")){
            iv.setImageResource(R.drawable.map_sn);
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.cool_kids);
                mediaPlayer.start();
            }
        }
        else if(add.equals("256-264 E Cameron Ave")){
            iv.setImageResource(R.drawable.map_ow);
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.sugar);
                mediaPlayer.start();
            }
        }
        else if(add.equals("200 S Rd")){
            iv.setImageResource(R.drawable.map_polk);
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.sunshine);
                mediaPlayer.start();
            }
        }
        else{
            iv.setImageResource(R.drawable.map);
            if (mediaPlayer!=null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        mGoogleApiClient.connect();
        if (mGoogleApiClient.isConnected()) {
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);// startLocationRequests
            } catch (SecurityException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient,this);
        }catch (SecurityException ex) {
            ex.printStackTrace();
        }
        if (mediaPlayer!=null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}
