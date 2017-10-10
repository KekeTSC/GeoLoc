package com.example.apprenti.myapplication;

import com.google.api.client.util.Key;

import java.util.Date;

/**
 * Created by apprenti on 09/10/17.
 */

public class Coord {
    @Key
    private Double lon;
    @Key
    private Double lat;

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

}
