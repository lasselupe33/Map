package model;

import helpers.AddressBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AddressTest {
    Address address;


    @Test
    public void testToKey() {
        address = AddressBuilder.parse("HolGer DaNskes VEj 69, 2000 FredeRiksberg");
        assertEquals("holger danskes vej692000" , address.toKey());
        address = AddressBuilder.parse("road without house and postcode");
        assertEquals("road without house and postcode", address.toKey());
        address = AddressBuilder.parse("road without postcode 89");
        assertEquals("road without postcode89", address.toKey());
        address = AddressBuilder.parse("road without postcode , 1231");
        assertEquals("1231", address.getPostcode());
        assertEquals("road without postcode1231", address.toKey());
    }

    @Test
    public void testToString() {
        address = AddressBuilder.parse("Tagensvej 22,,,,, 2200 København");
        assertEquals("Tagensvej 22, 2200 København", address.toString());
        //todo there is an error with "city"
    }
}