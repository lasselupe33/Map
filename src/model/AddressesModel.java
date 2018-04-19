package model;

import helpers.TST;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class AddressesModel implements Serializable {
    private TST<Address> addresses = new TST<>();

    public AddressesModel() {

    }

    /** Helper to add an address to the trie of addresses */
    public void add(Address address) {
        addresses.put(address.toKey(), address);
    }

    /** Helper that returns the coordinates of an address */
    public Coordinates getCoordinates(Address address) {
        Coordinates coordinates = address.getCoordinates();

        // If coordinates exists on the address, simply return this
        if (address.getCoordinates() != null) {
            return coordinates;
        } else {
            // ... else fetch the coordinates from the TST
            return addresses.get(address.toKey()).getCoordinates();
        }
    }

    public Address get(String key) {
        return addresses.get(key);
    }

    /** Helper that indicates whether or not an address exists on the map */
    public boolean contains(Address address) { return addresses.contains(address.toKey()); }

    /** Helper that returns an arrayList of addresses that matches the given prefix */
    public ArrayList<String> getMatchingAddresses(String prefixKey) {
        Iterable<String> matchingKeys = addresses.keysWithPrefix(prefixKey);
        ArrayList<String> matchingAddresses = new ArrayList<>();

        for (String key : matchingKeys) {
            matchingAddresses.add(addresses.get(key).toString());
        }

        return matchingAddresses;
    }
}
