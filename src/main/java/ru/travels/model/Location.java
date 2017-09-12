package ru.travels.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Arrays;
import java.util.List;

@JsonSerialize
public class Location {
    private Integer id;
    private String place;
    private String country;
    private String city;
    private Integer distance;

    public Location() {
    }

    public Location(Integer id, String place, String country, String city, Integer distance) {
        this.id = id;
        this.place = place;
        this.country = country;
        this.city = city;
        this.distance = distance;
    }

    public List<Object> simpleList() {
        return Arrays.asList(id, place, country, city, distance);
    }

    public Integer getId() {
        return id;
    }

    public String getPlace() {
        return place;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Location{");
        sb.append("id=").append(id);
        sb.append(", place='").append(place).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", distance=").append(distance);
        sb.append('}');
        return sb.toString();
    }
}
