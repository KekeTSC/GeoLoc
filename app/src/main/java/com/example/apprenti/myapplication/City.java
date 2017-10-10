package com.example.apprenti.myapplication;

import com.google.api.client.util.Key;

/**
 * Created by apprenti on 09/10/17.
 */

public class City {
    @Key
    private Integer id;
    @Key
    private String name;
    @Key
    private Coord coord;
    @Key
    private String country;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
