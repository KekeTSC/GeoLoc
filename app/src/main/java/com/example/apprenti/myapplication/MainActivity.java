package com.example.apprenti.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "BITE";
    private LocationManager lm = null;
    private LocationListener locationListener;

    private double latitude;
    private double longitude;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationListener= new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            String msg = String.format(getResources().getString(R.string.new_location), latitude, longitude);
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }


        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            String newStatus = "";
            switch (i) {
                case LocationProvider.OUT_OF_SERVICE:
                    newStatus = "OUT_OF_SERVICE";
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    newStatus = "TEMPORARILY_UNAVAILABLE";
                    break;
                case LocationProvider.AVAILABLE:
                    newStatus = "AVAILABLE";
                    break;
            }
            String msg =
                    getResources().getString(R.string.provider_disabled) + s +
                            newStatus;
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProviderEnabled(String provider) {
            String msg = String.format(
                    getResources().getString(R.string.provider_enabled), provider);
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            String msg = String.format(
                    getResources().getString(R.string.provider_disabled), provider);
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    };
         if (ContextCompat.checkSelfPermission(MainActivity.this,
        Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("location permission")
                        .setMessage("Locationpermition")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
            Log.i(TAG, "permission");
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, locationListener);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 0, locationListener);
    }


    @Override
    protected void onStop() {
        super.onStop();
        lm.removeUpdates(locationListener);
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode) {
            case 123: {
                if (grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    // permission was granted, woho!
                } else {

                    Toast.makeText(getApplicationContext(),"Et bah t'es pas futé toi...",Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }

    }

}