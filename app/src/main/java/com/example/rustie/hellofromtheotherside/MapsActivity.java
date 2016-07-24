package com.example.rustie.hellofromtheotherside;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.data.Device;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;


import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import com.google.maps.android.SphericalUtil;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private GoogleMap mMap;
    private LocationManager mLocationManager;

    public MarkerOptions momo;

    public ArrayList<Marker> friendArrayList = new ArrayList<>();
    public HashMap<String,Marker> friendHash= new HashMap<>();

    public Long mKey;

    public SharedPreferences shared;

    public boolean mVibratedBefore = false;



    ImageButton floatButton;

    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    Location mLastLocation;
    Marker mCurrLocationMarker;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            //You can add here other case statements according to your requirement.
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        shared = getSharedPreferences("pref2", Context.MODE_PRIVATE);
        String name = shared.getString("full_name", "");

        if (name.equals("")) {
            shared = getSharedPreferences("ActivityPREF", MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();
            editor.remove("activity_executed");
            editor.apply();

            Intent intent = new Intent(MapsActivity.this, HelloNameActivity.class);
            startActivity(intent);
            finish();


        }


        floatButton = (ImageButton) findViewById(R.id.imageButton);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MapsActivity.this, CreateJoinPartyActivity.class);
                startActivity(intent);
                finish();

            }
        });

        Firebase.setAndroidContext(this);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }



    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {


        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        // shared preferences stuff with name and key
        SharedPreferences shared = getSharedPreferences("pref2", Context.MODE_PRIVATE);
        String name = shared.getString("full_name", "");

        mKey = (Long) (long) shared.getInt("key", 0);

        //Sends info to database
        Firebase fire =  new Firebase("https://hellofromtheotherside-5eb21.firebaseio.com/");

        fire.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int numChildren = (int) dataSnapshot.getChildrenCount();

                int j = 0;

                // prevent infinite markers.....gets rid of previous data
                for (int i = 0; i < friendArrayList.size(); i++) {
                    friendArrayList.get(i).remove();
                }
                friendArrayList.clear();

                int i = 0;
                //friendHash.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String name = snapshot.getKey();

                    Double lat = (Double) snapshot.child("lat").getValue();
                    Double lng = (Double) snapshot.child("long").getValue();
                    Long keyLong = (Long) snapshot.child("key").getValue();

                    if (j >= numChildren)
                        break;



                    if (lat instanceof Double && lng instanceof Double  && keyLong instanceof Long
                            && keyLong.equals(mKey)) {



                        Log.v("Marker Placed ", name + lat + " AND " + lng);


                        momo = new MarkerOptions();
                        momo.position(new LatLng(lat, lng));
                        momo.title(name);

                        Marker tm;
                        friendArrayList.add(i, tm = mMap.addMarker(momo));
                        i++;
                        j++;

                        // ADDED DIST
                        SharedPreferences shared = getSharedPreferences("pref2", Context.MODE_PRIVATE);
                        String full_name = shared.getString("full_name", "");

                        LatLng from = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                        if (!name.equals(full_name)) {
                            LatLng to = new LatLng(lat, lng);


                            Double distance = SphericalUtil.computeDistanceBetween(from, to);

                            if(distance < 5.0 && !mVibratedBefore) {
                                Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                // Vibrate for 500 milliseconds
                                v.vibrate(1000);
                                mVibratedBefore = true;
                            } else if (distance >= 5) {
                                mVibratedBefore = false;
                            }
                        }


                    }

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        String username  = name;
        fire.child(username).child("lat").setValue(location.getLatitude());
        fire.child(username).child("long").setValue(location.getLongitude());
        fire.child(username).child("key").setValue(mKey);


        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

//
//
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}