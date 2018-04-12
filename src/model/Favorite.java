package model;

public class Favorite {
    private String name;
    private Address address;
    private Coordinates coordinates;


    public Favorite(String name, Address address, Coordinates coordinates){
        this.name = name;
        this.address = address;
        this.coordinates = coordinates;




    }

    public String getName() {
        return name;
    }


    public Address getAddress() {
        return address;
    }
}
