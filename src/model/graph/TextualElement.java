package model.graph;

import model.Address;

/**
 * This class contains the information of a single action of the step-by-step navigation.
 */
public class TextualElement {
    private String name;
    private String dist;
    private String iconURL;
    private boolean isAddress;
    private Address address;

    public TextualElement(Address address) {
        isAddress = true;
        this.address = address;
    }

    public TextualElement(String name, String iconURL, String dist) {
        this.name = name;
        this.dist = dist;
        this.iconURL = iconURL;
        isAddress = false;
    }

    public String getName() {
        return name;
    }

    public String getDist() {
        return dist;
    }

    public String getIconURL() {
        return iconURL;
    }

    public boolean isAddress() {
        return isAddress;
    }

    public Address getAddress() {
        return address;
    }
}
