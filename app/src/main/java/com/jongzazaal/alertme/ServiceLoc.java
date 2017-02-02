package com.jongzazaal.alertme;

import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;


/**
 * Created by jongzazaal on 15/12/2559.
 */

public class ServiceLoc extends Service implements OnMapReadyCallback,

        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "HelloService";

    private boolean isRunning  = false;

//    private GoogleApiClient googleApiClient = MapActivity.googleApiClient;
    private GoogleApiClient googleApiClient;

    private double latitude;
    private double longitude;

    public static Context context;

//    public static String namePlace

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        isRunning = true;
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    public void stopThreadAlert(String placeName){
        for (Thread k:Thread.getAllStackTraces().keySet()) {
            if (k.getName().equals(placeName)){
//                k.stop();
//                k.interrupt();
//                k.destroy();


//                    k = null;

                Log.i("aaaaaaaaaaaaa", "stop");
            }
        }
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        googleApiClient.connect();
        Log.i(TAG, "Service onStartCommand");

        //Creating new thread for my service
        //Always write your long running tasks in a separate thread, to avoid ANR

        new Thread(new Runnable() {

            @Override
            public void run() {

                Thread.currentThread().setName(intent.getStringExtra("placeName"));




                int distance = intent.getIntExtra("distance", 0);
                //Your logic that service will perform will be placed here
                //In this example we are just looping and waits for 1000 milliseconds in each loop.
//                for (int i = 0; i < 200; i++) {
                LatLng FinLocation = new LatLng(intent.getDoubleExtra("finLat", 0), intent.getDoubleExtra("finLng", 0));
                while(isRunning) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }

                    if (isRunning) {
//                        latitude = MapActivity.latitude;
//                        longitude = MapActivity.longitude;
                        LatLng currentLocation = new LatLng(latitude, longitude);
//                        LatLng FinLocation = MapActivity.p.getLatLng();
                        double be = SphericalUtil.computeDistanceBetween(currentLocation, FinLocation);
                        Log.i(TAG, "Service running" + "//" + latitude + "//" + longitude + "//" + be);
//                        Log.i(TAG, "Service running"+i);
                        if (be <= distance) {
                            Intent intent1 = new Intent(context, AlarmMeActivity.class);
                            intent1.putExtra("placeName", intent.getStringExtra("placeName"));
                            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent1);
//                            stopSelf();
                            break;
                        }

                        if(!SwitchService.getInstace().isOnService(Thread.currentThread().getName())){
                            Log.i("aaaaaaaa", "sssssssssss");

//                            stopSelf();
                            break;
                        }
                    }
                }
//                stopSelf();

//                }

                //Stop service once it finishes its task
//                stopSelf();
            }
        }).start();

        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {

        isRunning = false;
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        Log.i(TAG, "Service onDestroy");
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient);
        if(locationAvailability.isLocationAvailable()) {
            LocationRequest locationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(5000)
                    .setFastestInterval(1000);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } else {
            // Do something when location provider not available
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
