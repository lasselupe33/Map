package model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * A favorite is a user specified address that they would like to have an easy reference to.
 */
public class Favorite implements Externalizable {
    private String name;
    private Address address;

    public Favorite() {}
    public Favorite(String name, Address address){
        this.name = name;
        this.address = address;
    }

    /**
     * Get name of favorite
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get address of favorite
     * @return address
     */
    public Address getAddress() { return address; }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
        out.writeObject(address);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = (String) in.readObject();
        address = (Address) in.readObject();
    }
}
