package com.example.apprenti.myapplication;

import com.google.api.client.util.Key;

/**
 * Created by apprenti on 09/10/17.
 */

public class Sys {
    @Key
    private Double message;
    @Key
    private String country;
    @Key
    private Integer sunrise;
    @Key
    private Integer sunset;
    @Key
    private String pod;

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public Double getMessage() {
        return message;
    }

    public void setMessage(Double message) {
        this.message = message;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getSunrise() {
        return sunrise;
    }

    public void setSunrise(Integer sunrise) {
        this.sunrise = sunrise;
    }

    public Integer getSunset() {
        return sunset;
    }

    public void setSunset(Integer sunset) {
        this.sunset = sunset;
    }

}
