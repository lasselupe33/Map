package model;

import java.io.Serializable;

public class Address implements Serializable {
    private String street, house, postcode, city;
    private Coordinates coordinates;

    /** Constructor to be used while parsing OSM-files */
    public Address(double lat, double lon) {
        this.coordinates = new Coordinates(lat, lon);
    }

    /** Constructor to be used when parsing addresses */
    public Address(String _street, String _house, String _postcode, String _city) {
        street = _street;
        house = _house;
        postcode = _postcode;
        city = _city;
    }

    /** Helper to set the address of an addresses. To be used while parsing OSM-files */
    public void setAddress(String _street, String _house, String _postcode) {
        street = _street;
        house = _house;
        postcode = _postcode;
    }

    /** Helper that converts an address to a key to be used in the data-structure */
    public String toKey() {
        return (street + postcode + (house != null ? house : "")).toLowerCase();
    }

    /** Getters */
    public String getStreet() {
        return street;
    }

    public String getHouse() {
        return house;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCity() {
        return city;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }
}
