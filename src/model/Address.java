package model;

import java.util.*;
import java.util.regex.*;

public class Address {
    private final String street, house, floor, side, postcode, city;
    private float lat;
    private float lon;

    public Address(String _street, String _house, String _floor, String _side, String _postcode, String _city, float lat, float lon) {
        street = _street;
        house = _house;
        floor = _floor;
        side = _side;
        postcode = _postcode;
        city = _city;
        this.lat = lat;
        this.lon = lon;
    }

    public String toString() {
        return (street != null ? street + " " : "") +
                (house != null ? house + " " : "") +
                (floor != null ? floor + " " : "") +
                (side != null ? side + " " : "") +
                (postcode != null ? postcode + " " : "") +
                (city != null ? city + " " : "");
    }
}
