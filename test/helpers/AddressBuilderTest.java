package helpers;

import model.Address;
import org.junit.Test;

import static org.junit.Assert.*;

public class AddressBuilderTest {

    @Test
    public void testParseValidInput1() {
        Address assertAddress = new Address("Rued Langgaards Vej", "7", "2300");
        String stringAddress = "Rued Langgaards Vej 7, 2300 København S Danmark";
        Address testAddress = AddressBuilder.parse(stringAddress);
        assertEquals(assertAddress.getCoordinates(), testAddress.getCoordinates());
        assertEquals(assertAddress.getCity(), testAddress.getCity());
        assertEquals(assertAddress.toKey(), testAddress.toKey());
        assertEquals(assertAddress.toString(), testAddress.toString());

    }

    @Test
    public void testParseValidInput2() {
        Address assertAddress = new Address("vejnavn med mellemrum", "919293", "1231");
        String stringAddress = "vejnavn med mellemrum 919293,,,,,,, 1231 kbh";
        Address testAddress = AddressBuilder.parse(stringAddress);
        assertEquals(assertAddress.getCoordinates(), testAddress.getCoordinates());
        assertEquals(assertAddress.getCity(), testAddress.getCity());
        assertEquals(assertAddress.toKey(), testAddress.toKey());
        assertEquals(assertAddress.toString(), testAddress.toString());

    }



    @Test(expected = Error.class)
    public void testParseInvalidHouse(){
        String stringAddress = "Rued Langgaards Vej 7 7, 2300 København S Danmark";
        Address testAddress = AddressBuilder.parse(stringAddress);
    }

    @Test(expected = Error.class)
    public void testParseInvalidPostCode(){
        String stringAddresLength5 = "streetname 1923, 89374 city of Danmark";
        Address testAddress = AddressBuilder.parse(stringAddresLength5);
        //todo regex shouldn't accept postcode with length != 4
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyInput(){
        Address testAddress = AddressBuilder.parse("");
    }

    //todo regex should be able to parse "Rued Langgaards Vej 7 7, 2300 København S, Danmark"


}