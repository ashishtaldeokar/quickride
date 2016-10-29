package com.sspm.quickride;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.digits.sdk.android.Digits;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;

import com.digits.sdk.android.models.PhoneNumber;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private int LOCATION_REFRESH_TIME = 1000;
    private float LOCATION_REFRESH_DISTANCE = 1;
    private Criteria mCriteria = new Criteria();
    private Location mMyLocation;
    private Marker mMarker;
    private final int REQUEST_CODE = 100;
    private GoogleApiClient mGoogleApiClient;

    private final LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(final Location location) {
            //triggered when location changed.
            mMyLocation = location; //set mMyLocation to new location
            LoginActivity.fire.setLocation(location); //update it to database
            if (mMarker == null) //if first time
                setmMap();
            mMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude())); //update mMarker on mMap
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
            //triggered when GPS/Location Enabled.
            setmMap();
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            onStart();
        }

        @Override
        public void onProviderDisabled(String s) {
            //triggered when GPS/Location Disabled.
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            onStop();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //------------------------------------------------------------------------------------------
        //              Create an instance of GoogleAPIClient.
        //------------------------------------------------------------------------------------------
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        //------------------------------------------------------------------------------------------
        //              start instance of GoogleAPIClient.
        //------------------------------------------------------------------------------------------
        onStart();
        //------------------------------------------------------------------------------------------
        //              Initialise view
        //------------------------------------------------------------------------------------------
        setContentView(R.layout.activity_maps);
        //------------------------------------------------------------------------------------------
        //              Obtain the SupportMapFragment and get notified when the map is ready to be used
        //------------------------------------------------------------------------------------------
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //------------------------------------------------------------------------------------------
        //              initialise Location Manager
        //------------------------------------------------------------------------------------------
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // An unresolvable error has occurred and a connection to Google APIs
        // could not be established. Display an error message, or handle
        // the failure silently (mostly no or failed internet connection)
        Toast.makeText(getApplicationContext(), "Google Services Error", Toast.LENGTH_LONG).show();
        onStop();
    }

    public void setmMap() {
        //mMap.setMyLocationEnabled(true);
        LoginActivity.fire.setLocation(mMyLocation);
        LoginActivity.fire.becomeActive();
        if (mMyLocation != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mMyLocation.getLatitude(), mMyLocation.getLongitude()), 13));
            mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(mMyLocation.getLatitude(), mMyLocation.getLongitude())));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(mMyLocation.getLatitude(), mMyLocation.getLongitude()))      // Sets the center of the map to location user
                    .zoom(16)                   // Sets the zoom
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } else {
            Toast.makeText(getApplicationContext(), "Location Error", Toast.LENGTH_LONG).show();
            if ( !mLocationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                buildAlertMessageNoGps();
            }
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        //noinspection MissingPermission
        mLocationManager.removeUpdates(mLocationListener);
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    } //map fragment is loaded asynchronously, when ready assign in to mMap

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        //request code is explicitly specified in this class (for location permission REQUEST CODE is set to 100). it can be set to any number.
        //The array permission[] consist of requested, and grantResult[] consist of result of it accordingly
        switch (requestCode) {
            case REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty. therefore (check array size AND check result is granted)
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //If condition = true permission was granted and therefore START using Location services
                    //noinspection MissingPermission
                    mMyLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    setmMap();

            } else {
                // permission denied, boo! Prepare an activity to notify.
            }
            break;
        }
    }
}

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //mGoogleAPI was connected successfully.(APP START OR RESUME)
        //first check whether we have permission to use Location services.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //if NOT then Request for Permission.
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
            return;
        }
        //else Start using location services
        mMyLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        setmMap();
        Toast.makeText(getApplicationContext(), "just connected!", Toast.LENGTH_LONG).show();
        mLocationManager.requestLocationUpdates(LOCATION_REFRESH_TIME,LOCATION_REFRESH_DISTANCE,mCriteria,mLocationListener,null);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getApplicationContext(), "Connection Suspended", Toast.LENGTH_LONG).show();
        onStop();
    }
}
