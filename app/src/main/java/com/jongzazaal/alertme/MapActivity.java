package com.jongzazaal.alertme;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jongzazaal.alertme.databaseAlarm.ControlDatabase;
import com.jongzazaal.alertme.databaseAlarm.RecentClass;
import com.jongzazaal.alertme.databaseAlarm.SingleDBalarmMe;

import java.util.ArrayList;

/**
 * Created by jongzazaal on 16/12/2559.
 */

public class MapActivity extends Activity implements PlaceSelectionListener, OnMapReadyCallback,

        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,

        GoogleMap.OnMapClickListener {

//    private TextView mPlaceDetailsText;

//    private TextView mPlaceAttribution;

    private Marker mMarker;

    private GoogleMap mMap;

    private Circle circle;

    private SeekBar seekbar;

    public static Place p;

    private Switch aSwitch;

    private LinearLayout consoleMap;

    public static double latitude;
    public static double longitude;

    public static int distance;

    public static GoogleApiClient googleApiClient;

    private int role = 0;
    private LatLng latLngP;

    private RelativeLayout saveMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.map_select);
        // Get instance of Vibrator from current Context




        // Retrieve the PlaceAutocompleteFragment.
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Register a listener to receive callbacks when a place has been selected or an error has
        // occurred.
        autocompleteFragment.setOnPlaceSelectedListener(MapActivity.this);

        // Retrieve the TextViews that will display details about the selected place.
//        mPlaceDetailsText = (TextView) findViewById(R.id.place_details);
//        mPlaceAttribution = (TextView) findViewById(R.id.place_attribution);

        MapFragment mapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_map_container, mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);

        consoleMap = (LinearLayout) findViewById(R.id.consoleMap);
        consoleMap.setVisibility(View.GONE);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        aSwitch = (Switch) findViewById(R.id.switchOnOff);
        aSwitch.setVisibility(View.GONE);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                if(isChecked){
                    Toast.makeText(MapActivity.this, R.string.switchOn, Toast.LENGTH_SHORT).show();
                    ServiceLoc.context = MapActivity.this;
                    Intent intent = new Intent(MapActivity.this, ServiceLoc.class);
                    startService(intent);
                }
                else {
                    Toast.makeText(MapActivity.this, R.string.switchOff, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MapActivity.this, ServiceLoc.class);
                    stopService(intent);
                }
            }
        });
        seekbar = (SeekBar) findViewById(R.id.seekbarM);
        seekbar.setMax(2000);
        seekbar.setProgress(200);
        distance = 200;




        saveMap = (RelativeLayout) findViewById(R.id.saveMap);
        saveMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mMarker==null){
                    Toast.makeText(MapActivity.this, R.string.plz_Select_place, Toast.LENGTH_SHORT).show();
                    return;
                }
                //role 0->new date
                //role 1->recent date
                if (role == 0){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                    builder.setTitle(R.string.Plz_Enter_Name_Of_Place);
                    builder.setCancelable(true);
//                    builder.setPositiveButton(android.R.string.ok, null); //Set to null. We override the onclick
//                    builder.setNegativeButton(android.R.string.cancel, null);



// Set up the input
                    final EditText input = new EditText(MapActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT );
                    builder.setView(input);

// Set up the buttons
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            m_Text = input.getText().toString();
                            boolean nameUnique = true;
                            for (String nP: RecentClass.getInstance().getPlaceName()) {
                                if (nP.equals(input.getText().toString())){
                                    Toast.makeText(MapActivity.this, R.string.The_name_must_be_unique, Toast.LENGTH_SHORT).show();
//                                    return;
                                    nameUnique=false;
                                    dialog.dismiss();
                                }
                            }
                            if(nameUnique){
                                ControlDatabase.getInstance(MapActivity.this).saveRecord(input.getText().toString(), mMarker.getPosition(),seekbar.getProgress());
                                Toast.makeText(MapActivity.this, R.string.Save_Done, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();
                            }

                        }
                    });
                    builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            return;
                        }
                    });

                    builder.show();

                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                    builder.setTitle(R.string.saveData);
                    builder.setCancelable(true);

// Set up the input
                    final EditText input = new EditText(MapActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT );
                    input.setText(SingleDBalarmMe.getInstance().getPlaceName());
                    builder.setView(input);

// Set up the buttons
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            m_Text = input.getText().toString();
                            boolean nameUnique = true;
                            ArrayList<String> listPN = RecentClass.getInstance().getPlaceName();
                            listPN.remove(SingleDBalarmMe.getInstance().getPlaceName());
                            for (String nP: listPN) {
                                if (nP.equals(input.getText().toString())){
                                    Toast.makeText(MapActivity.this, R.string.The_name_must_be_unique, Toast.LENGTH_SHORT).show();
//                                    return;
                                    nameUnique=false;
                                    dialog.dismiss();
                                }
                            }
                            if (nameUnique){
                            ControlDatabase.getInstance(MapActivity.this).updateRecord(SingleDBalarmMe.getInstance().getId(),
                                    input.getText().toString(), mMarker.getPosition(), seekbar.getProgress());
                            Toast.makeText(MapActivity.this, R.string.Save_Done, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();}

                        }
                    });
                    builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            return;
                        }
                    });

                    builder.show();

                }
//                googleApiClient.disconnect();
//                finish();

            }
        });

    }

    /**
     * Callback invoked when a place has been selected from the PlaceAutocompleteFragment.
     */

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
        builder.setTitle(R.string.exit);
        builder.setMessage(R.string.You_not_been_saved_Discard);
        builder.setCancelable(true);

// Set up the buttons
        builder.setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                            finish();

            }
        });
        builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                return;
            }
        });

        builder.show();


//        finish();

    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }
    @Override
    public void onStop() {


        super.onStop();
        googleApiClient.disconnect();
        mMap.clear();
        p=null;
    }


    @Override
    public void onPlaceSelected(Place place) {

        p = place;
        mMap.clear();
        mMarker = mMap.addMarker(new MarkerOptions()
        .position(place.getLatLng())
        );
        circle = mMap.addCircle(new CircleOptions()
                .center(p.getLatLng())
                .radius(seekbar.getProgress())
                .strokeColor(Color.RED)
                .fillColor(getResources().getColor(R.color.colorAria))
        );


        consoleMap.setVisibility(View.VISIBLE);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p.getLatLng(), 16));
    }

    /**
     * Callback invoked when PlaceAutocompleteFragment encounters an error.
     */
    @Override
    public void onError(Status status) {
        Log.e("aaaaaaaaaaaaaaaaaaaaaa", "onError: Status = " + status.toString());

        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Helper method to format information about a place nicely.
     */
    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e("aaaaaaaaaaaaaaaaaaaaaa", res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }

    @Override
    public void onMapClick(LatLng latLng) {


        mMap.clear();

        mMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
        );
        circle = mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(seekbar.getProgress())
                .strokeColor(Color.RED)
                .fillColor(getResources().getColor(R.color.colorAria))
        );

        consoleMap.setVisibility(View.VISIBLE);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);

        mMap = googleMap;
        mMap.setOnMapClickListener(this);

        View locationButton = ((View) findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
// position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rlp.setMargins(0, 0, 0, 0);

        Intent i = getIntent();
        if(i.getIntExtra("role", 0) == 1){
            role = 1;
            SingleDBalarmMe s = SingleDBalarmMe.getInstance();

            latLngP = new LatLng(s.getLat(), s.getLng());
            seekbar.setProgress(s.getDistance());
            mMap.clear();
            mMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLngP)
            );
            circle = mMap.addCircle(new CircleOptions()
                    .center(latLngP)
                    .radius(seekbar.getProgress())
                    .strokeColor(Color.RED)
                    .fillColor(getResources().getColor(R.color.colorAria))
            );


            consoleMap.setVisibility(View.VISIBLE);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngP, 16));
        }
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                distance = seekBar.getProgress();
                LatLng latLngpp;

                latLngpp = mMarker.getPosition();
                mMap.clear();


                //role 0->new date
                //role 1->recent date
//                if(role == 1 && p == null){
//                    latLngpp = latLngP;
//                }
//                else {
//                    latLngpp = p.getLatLng();
//                }

                mMarker = mMap.addMarker(new MarkerOptions()
                        .position(latLngpp)
                );
                circle = mMap.addCircle(new CircleOptions()
                        .center(latLngpp)
                        .radius(seekbar.getProgress())
                        .strokeColor(Color.RED)
                        .fillColor(getResources().getColor(R.color.colorAria))
                );
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
}