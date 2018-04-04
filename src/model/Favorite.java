package model;

public class Favorite {
    private String name;
    private String address;
    private Coordinates coordinates;


    public Favorite(String name, String address, Coordinates coordinates){
        this.name = name;
        this.address = address;
        this.coordinates = coordinates;




    }

    public String getName() {
        return name;
    }


    public String getAddress() {
        return address;
    }
}
