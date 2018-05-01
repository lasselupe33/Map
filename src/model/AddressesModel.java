package model;

import helpers.io.DeserializeObject;
import helpers.structures.KDTree;
import helpers.io.SerializeObject;
import helpers.structures.TST;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class AddressesModel implements Serializable {
    // Expose postcodeToCity mapping
    public static HashMap<String, String> postcodeToCity = new HashMap<>();

    // Used while parsing OSM-files
    private int parsingIndex = 0;

    // Contain address specific data.
    private ArrayList<Address> addresses = new ArrayList<>();
    private TST<Integer> searchTrie = new TST<>();
    private HashMap<String, Integer> coordToKeyMap = new HashMap<>();
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
        coordToKeyMap.put(address.getCoordinates().toString(), parsingIndex);

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

    public WayType getType(Address address) {
        WayType type = address.getType();

        // If coordinates exists on the address, simply return this
        if (address.getType() != null) {
            return type;
        } else {
            // ... else fetch the coordinates from the TST
            return addresses.get(searchTrie.get(address.toKey())).getType();
        }
    }

    /** Helper that returns an address matching the given key, if any. */
    public Address getAddress(String key) {
        return addresses.get(searchTrie.get(key));
    }

    /** Helper that indicates whether or not an address exists on the map */
    public boolean contains(Address address) { return searchTrie.contains(address.toKey()); }

    /** Helper that returns an arrayList of addresses that matches the given prefix */
    public ArrayList<Address> getMatchingAddresses(String prefixKey) {
        Iterable<String> matchingKeys = searchTrie.keysWithPrefix(prefixKey);
        ArrayList<Address> matchingAddresses = new ArrayList<>();

        for (String key : matchingKeys) {
            matchingAddresses.add(addresses.get(searchTrie.get(key)));
        }

        return matchingAddresses;
    }

    /** Helper that sets the postcode to city map after OSM-parsing has been completed */
    public void setPostcodeToCity(HashMap<String, String> postcodeToCity) {
        this.postcodeToCity = postcodeToCity;
    }

    /** Internal helper that returns all available to the addresses */
    private ArrayList<Coordinates> getAllCoordinates() {
        ArrayList<Coordinates> coordinates = new ArrayList<>();

        for (Address address : addresses) {
            coordinates.add(address.getCoordinates());
        }

        return coordinates;
    }

    /**
     * Helper to retrieve an address from a given coordinate.
     * Used for nearest neighbour searching.
     */
    private Address addressFromCoordinate(Coordinates coord) {
        return addresses.get(coordToKeyMap.get(coord.toString()));
    }

    /** Internal helper that serialises the mapModel */
    public void serialize() {
        new SerializeObject("address/tree", addressTree);
        new SerializeObject("address/coordToKeyMap", coordToKeyMap);
        new SerializeObject("address/TST", searchTrie);
        new SerializeObject("address/addresses", addresses);
        new SerializeObject("address/postcodeToCity", postcodeToCity);
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
            new DeserializeObject("address/postcodeToCity", this, callback);
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
                coordToKeyMap = (HashMap<String, Integer>) loadedObject;
                break;

            case "address/TST":
                searchTrie = (TST<Integer>) loadedObject;
                break;

            case "address/postcodeToCity":
                postcodeToCity = (HashMap<String, String>) loadedObject;
        }
    }
}
