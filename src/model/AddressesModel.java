package model;

import helpers.DeserializeObject;
import helpers.KDTree;
import helpers.SerializeObject;
import helpers.TST;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class AddressesModel implements Serializable {
    private int initializedFields = 0;
    private final int fields = 3; // Used when deserializing data.

    private int parsingIndex = 0;
    private ArrayList<Address> addresses = new ArrayList<>();
    private TST<Integer> searchTrie = new TST<>();
    private HashMap<Coordinates, Integer> coordToKeyMap = new HashMap<>();
    private KDTree addressTree; // Contain a reference to the tree storing all addresses

    public AddressesModel() {}

    public void createTree() {
        // Generate the address tree to quickly retrieve address based on coordinate.
        addressTree = new KDTree(getAllCoordinates());
    }

    public Address nearestNeighbour(double px, double py) {
        return addressFromCoordinate(addressTree.nearestNeighbour(px, py));
    }

    /** Helper to add an address to the trie of addresses */
    public void add(Address address) {
        addresses.add(address);
        searchTrie.put(address.toKey(), parsingIndex);
        coordToKeyMap.put(address.getCoordinates(), parsingIndex);

        parsingIndex++;
    }

    /** Helper that returns the coordinates of an address */
    public Coordinates getCoordinates(Address address) {
        Coordinates coordinates = address.getCoordinates();

        // If coordinates exists on the address, simply return this
        if (address.getCoordinates() != null) {
            return coordinates;
        } else {
            // ... else fetch the coordinates from the TST
            return addresses.get(searchTrie.get(address.toKey())).getCoordinates();
        }
    }

    /** Helper that returns an address matching the given key, if any. */
    public Address getAddress(String key) {
        return addresses.get(searchTrie.get(key));
    }

    /** Helper that indicates whether or not an address exists on the map */
    public boolean contains(Address address) { return searchTrie.contains(address.toKey()); }

    /** Helper that returns an arrayList of addresses that matches the given prefix */
    public ArrayList<String> getMatchingAddresses(String prefixKey) {
        Iterable<String> matchingKeys = searchTrie.keysWithPrefix(prefixKey);
        ArrayList<String> matchingAddresses = new ArrayList<>();

        for (String key : matchingKeys) {
            matchingAddresses.add(addresses.get(searchTrie.get(key)).toString());
        }

        return matchingAddresses;
    }

    /** Internal helper that returns all available to the addresses */
    private ArrayList<Coordinates> getAllCoordinates() {
        return new ArrayList<>(coordToKeyMap.keySet());
    }

    /**
     * Helper to retrieve an address from a given coordinate.
     * Used for nearest neighbour searching.
     */
    private Address addressFromCoordinate(Coordinates coord) {
        String key = coord.getX() + "-" + coord.getY();

        return addresses.get(coordToKeyMap.get(key));
    }

    /** Internal helper that serialises the mapModel */
    public void serialize() {
        new SerializeObject("address/tree", addressTree);
        new SerializeObject("address/coordToKeyMap", coordToKeyMap);
        new SerializeObject("address/TST", searchTrie);
        new SerializeObject("address/addresses", addresses);
    }

    /** Internal helper that deserializses the MapModel */
    public void deserialize() {
        try {
            // Setup thread callback
            Class[] parameterTypes = new Class[2];
            parameterTypes[0] = Object.class;
            parameterTypes[1] = String.class;
            Method callback = AddressesModel.class.getMethod("onThreadDeserializeComplete", parameterTypes);

            // Load all data
            new DeserializeObject("address/tree", this, callback);
            new DeserializeObject("address/coordToKeyMap", this, callback);
            new DeserializeObject("address/TST", this, callback);
            new DeserializeObject("address/addresses", this, callback);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Callback to be called once a thread has finished deserializing a mapType */
    public void onThreadDeserializeComplete(Object loadedObject, String name) {
        switch(name) {
            case "address/addresses":
                addresses = (ArrayList<Address>) loadedObject;
                break;
            case "address/tree":
                addressTree = (KDTree) loadedObject;
                break;

            case "address/coordToKeyMap":
                coordToKeyMap = (HashMap<Coordinates, Integer>) loadedObject;
                break;

            case "address/TST":
                searchTrie = (TST<Integer>) loadedObject;
                break;
        }
    }
}
