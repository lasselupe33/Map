package model;

import java.io.*;

public class Address implements Externalizable {
    private String street, house, postcode;
    private long id;
    private Coordinates coordinates;

    public Address() {}

    /** Constructor to be used while parsing OSM-files */
    public Address(long id, double lat, double lon) {
        this.id = id;
        this.coordinates = new Coordinates(lat, lon);
    }

    /** Constructor to be used when parsing addresses */

    public Address(String _street, String _house, String _postcode) {
        street = _street;
        house = _house;
        postcode = _postcode;
    }

    /** Helper to set the address of an addresses. To be used while parsing OSM-files */
    public void setAddress(String _street, String _house, String _postcode) {
        street = _street;
        house = _house;
        postcode = _postcode;
    }

    /** Helper that converts an address to a key to be used in the data-structure */
    public String toKey() {
        return (street + (house != null ? house : "") + (postcode != null ? postcode : "")).toLowerCase();
    }

    public long getId() {
        return id;
    }

    public String toString() {
        String city = getCity();

        return street + " " + (house != null ? house : "") + ", " + (postcode != null ? postcode : "") + " " + (city != null ? city : "");
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
        return AddressesModel.postcodeToCity.get(postcode);
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        street = (String) in.readObject();
        house = (String) in.readObject();
        postcode = (String) in.readObject();
        coordinates = (Coordinates) in.readObject();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(street);
        out.writeObject(house);
        out.writeObject(postcode);
        out.writeObject(coordinates);
    }
}

