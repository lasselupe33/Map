package model;

import helpers.AddressBuilder;
import helpers.structures.TST;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AddressesModelTest {
    AddressesModel addressesModel;
    @Before
    public void setUp() throws Exception {
        addressesModel = new AddressesModel();
        TST<Integer> searchTrie = new TST<>();
        searchTrie.put("hej", 1);
        System.out.println(searchTrie.get("hej"));
    }



    @Test
    public void getCoordinates() {
        Address address = new Address(1, 10, 10);
        assertEquals(address.getCoordinates(), addressesModel.getCoordinates(address));

        Address addressBuilder = AddressBuilder.parse("Karlslundevej 3, 2700 København");
        //todo make osm file to test this

    }


    @Test
    public void getMatchingAddresses() {
        //todo
    }

    @Test
    public void setPostcodeToCity() {
        //todo
    }


}