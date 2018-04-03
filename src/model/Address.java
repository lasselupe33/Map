package model;

import java.util.*;
import java.util.regex.*;

public class Address {
    private String street, house, postcode, city;
    private double lat;
    private double lon;

    public Address(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public Address(String _street, String _house, String _postcode, String _city, double lat, double lon) {
        street = _street;
        house = _house;
        postcode = _postcode;
        city = _city;
        this.lat = lat;
        this.lon = lon;
    }

    public void setAddress(String _street, String _house, String _postcode) {
        street = _street;
        house = _house;
        postcode = _postcode;
    }

    public String toKey() {
        return (street + postcode + (house != null ? house : "")).toLowerCase();
    }
}
