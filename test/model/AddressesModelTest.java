package model;

import helpers.AddressBuilder;
import helpers.io.IOHandler;
import model.graph.Graph;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class AddressesModelTest {
    private static MetaModel m;
    private static MapModel mm;
    private static AddressesModel addressesModel;
    private static Graph graph;

    @Before
    public void setUp() throws Exception {
        graph = new Graph();
        m = new MetaModel();
        mm = new MapModel(m, graph);
        addressesModel = new AddressesModel();

        IOHandler.instance.addModels(m, mm, addressesModel, graph);
        IOHandler.instance.loadFromString("./test/data/tiny.osm");

        // Give time to parse osm on another thread
        Thread.sleep(1000);

    }



    @Test
    public void getCoordinates() {
        Address addressWithBuilder = AddressBuilder.parse("Strandgade 93, 1401 København K");
        Coordinates coordinates = new Coordinates((float)7.1034446, (float)-55.6779044);
        assertEquals(coordinates.getX(), addressesModel.getCoordinates(addressWithBuilder).getX());
        assertEquals(coordinates.getY(), addressesModel.getCoordinates(addressWithBuilder).getY());

        Address addressWithNew = new Address(1, 10, 10);
        assertEquals(addressWithNew.getCoordinates(), addressesModel.getCoordinates(addressWithNew));



    }


    @Test
    public void getMatchingAddresses() {
        ArrayList<Address> addresses = addressesModel.getMatchingAddresses("st");
        ArrayList<String> expectedAddresses = new ArrayList<>();
        expectedAddresses.add("strandgade931401");
        expectedAddresses.add("strandvej122100");
        expectedAddresses.add("strøget41100");

        for(int i = 0; i < addresses.size(); i++) {
            String addressToKey = addresses.get(i).toKey();
            assertEquals(expectedAddresses.get(i), addressToKey);
        }
    }

    @Test
    public void setPostcodeToCity() {
        HashMap<String, String> newPostCodeToCity = new HashMap<String, String>();
        newPostCodeToCity.put("2100", "København Ø");
        newPostCodeToCity.put("2605", "Brøndby");
        addressesModel.setPostcodeToCity(newPostCodeToCity);
        assertEquals(newPostCodeToCity, AddressesModel.postcodeToCity);
    }
}