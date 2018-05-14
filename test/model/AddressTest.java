package model;

import helpers.AddressBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AddressTest {
    Address address;


    @Test
    public void testToKey() {
        address = AddressBuilder.parse("Holger danskes vej 69, 2000 FredeRiksberg");
        assertEquals("holger danskes vej692000" , address.toKey());
    }

    @Test
    public void testToString() {
        address = AddressBuilder.parse("Tagensvej 22,,,,, 2200 København");
        assertEquals("Tagensvej 22, 2200 ", address.toString());
    }
}