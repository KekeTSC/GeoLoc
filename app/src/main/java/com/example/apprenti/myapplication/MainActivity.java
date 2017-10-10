package com.example.apprenti.myapplication;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.octo.android.robospice.GsonGoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;


public class MainActivity extends AppCompatActivity {
    private String TAG = "Location";

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private String mProvider;
    TextView mTextView;
    private SpiceManager mSpiceManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textViewCityName);

        mSpiceManager = new SpiceManager(GsonGoogleHttpClientSpiceService.class);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mProvider = mLocationManager.getBestProvider(new Criteria(), false);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "onLocationChanged() called with: location = [" + location + "]");
                String text = "Latitude : " + location.getLatitude() + " Longitude : " + location.getLongitude();
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                mTextView.setText(text);
                downloadWeatherData(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                Log.d(TAG, "onStatusChanged() called with: s = [" + s + "], i = [" + i + "], bundle = [" + bundle + "]");
            }

            @Override
            public void onProviderEnabled(String s) {
                Log.d(TAG, "onProviderEnabled() called with: s = [" + s + "]");
            }

            @Override
            public void onProviderDisabled(String s) {
                Log.d(TAG, "onProviderDisabled() called with: s = [" + s + "]");
            }
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }

        }
        else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, mLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1, mLocationListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSpiceManager.start(this);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        mSpiceManager.shouldStop();
        mLocationManager.removeUpdates(mLocationListener);
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
                    }
                }
                return;
            }
        }
    }

    private void downloadWeatherData(Double lat, Double lon) {
        CurrentWeatherRequest currentWeatherRequest = new CurrentWeatherRequest(lat, lon, getApplicationContext());
        mSpiceManager.execute(currentWeatherRequest, new CurrentWeatherRequestListener());

        ForecastWeatherRequest forecastWeatherRequest = new ForecastWeatherRequest(lat, lon, getApplicationContext());
        mSpiceManager.execute(forecastWeatherRequest, new ForecastWeatherRequestListener());

    }
    private class CurrentWeatherRequestListener implements RequestListener<CurrentWeatherModel> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {

        }
        @Override
        public void onRequestSuccess(CurrentWeatherModel currentWeatherModel) {
            String iconUrl = String.format("http://openweathermap.org/img/w/%s.png", currentWeatherModel.getWeather().get(0).getIcon());
            ImageView imageView = findViewById(R.id.imageViewCurrentWeather);
            Glide.with(getApplicationContext()).load(iconUrl).into(imageView);
            String cityName = currentWeatherModel.getName();
            mTextView.setText(cityName);
        }
    }

    private class ForecastWeatherRequestListener implements RequestListener<ForecastWeatherModel> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {

        }
        @Override
        public void onRequestSuccess(ForecastWeatherModel forecastWeatherModel) {
            ImageView imageViewTomorrowWheater = findViewById(R.id.imageViewTomorrowWeather);
            String iconUrl = String.format("http://openweathermap.org/img/w/%s.png", forecastWeatherModel.getList().get(1).getWeather().get(0).getIcon());
            Glide.with(getApplicationContext()).load(iconUrl).into(imageViewTomorrowWheater);
        }
    }
}