package model;

import helpers.TST;

public class Addresses {
    private TST<Coordinates> addresses = new TST<>();

    public Addresses() {

    }

    /** Helper to add an address to the trie of addresses */
    public void add(Address address) {
        addresses.put(address.toKey(), address.getCoordinates());
    }

    /** Helper that returns the coordinates of an address */
    public Coordinates getCoordinates(Address address) {
        Coordinates coordinates = address.getCoordinates();

        // If coordinates exists on the address, simply return this
        if (address.getCoordinates() != null) {
            return coordinates;
        } else {
            // ... else fetch the coordinates from the TST
            return addresses.get(address.toKey());
        }
    }

    /** Helper that indicates whether or not an address exists on the map */
    public boolean contains(Address address) { return addresses.contains(address.toKey()); }
}
