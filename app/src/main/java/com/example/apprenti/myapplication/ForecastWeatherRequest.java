package com.example.apprenti.myapplication;

import android.content.Context;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.gson.GsonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import roboguice.util.temp.Ln;

public class ForecastWeatherRequest extends GoogleHttpClientSpiceRequest<ForecastWeatherModel> {

    private String baseUrl;
    private String APIKey;
    private double lat;
    private double lon;

    public ForecastWeatherRequest (double lat, double lon, Context context){
        super(ForecastWeatherModel.class);
        this.lat=lat;
        this.lon=lon;
        this.APIKey=context.getString(R.string.apikey);
        this.baseUrl = String.format( "http://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&APPID=%s",
                String.valueOf(lat), String.valueOf(lon), APIKey);
    }

    @Override
    public ForecastWeatherModel loadDataFromNetwork() throws Exception {
        Ln.d( "Call web service " + baseUrl );
        HttpRequest request = getHttpRequestFactory()//
                .buildGetRequest(new GenericUrl(baseUrl) );
        request.setParser(new GsonFactory().createJsonObjectParser());
        return request.execute().parseAs(ForecastWeatherModel.class);
    }

    public String createCacheKey() {
        return "lat." + String.valueOf(lat) + " lon." + String.valueOf(lon);
    }
}

