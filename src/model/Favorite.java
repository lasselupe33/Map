package model;

public class Favorite {
    private String name;
    private Address address;

    public Favorite(String name, Address address){
        this.name = name;
        this.address = address;
    }


    public String getName() {
        return name;
    }

    public Address getAddress() { return address; }
}
