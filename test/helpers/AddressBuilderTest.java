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
        assertEquals(assertAddress.getStreet(), testAddress.getStreet());
        assertEquals(assertAddress.getHouse(), testAddress.getHouse());
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
        assertEquals(assertAddress.getStreet(), testAddress.getStreet());
        assertEquals(assertAddress.getHouse(), testAddress.getHouse());
        assertEquals(assertAddress.toKey(), testAddress.toKey());
        assertEquals(assertAddress.toString(), testAddress.toString());

    }
    @Test
    public void testParseSpecialCharacters() {
        Address assertAddress = new Address("Mü-Sö's Mäëé.", "11", "1231");
        String stringAddress = "Mü-Sö's Mäëé. 11, 1231 København S";
        Address testAddress = AddressBuilder.parse(stringAddress);
        assertEquals(assertAddress.getCoordinates(), testAddress.getCoordinates());
        assertEquals(assertAddress.getCity(), testAddress.getCity());
        assertEquals(assertAddress.getStreet(), testAddress.getStreet());
        assertEquals(assertAddress.getHouse(), testAddress.getHouse());
        assertEquals(assertAddress.toKey(), testAddress.toKey());
        assertEquals(assertAddress.toString(), testAddress.toString());
    }

    @Test
    public void testStreetNameWithNumberFirst() {
        Address assertAddress = new Address("10. februar vej", "11", "1231");
        String stringAddress = "10. februar vej 11, 1231 København S";
        Address testAddress = AddressBuilder.parse(stringAddress);
        assertEquals(assertAddress.getCoordinates(), testAddress.getCoordinates());
        assertEquals(assertAddress.getCity(), testAddress.getCity());
        assertEquals(assertAddress.getStreet(), testAddress.getStreet());
        assertEquals(assertAddress.getHouse(), testAddress.getHouse());
        assertEquals(assertAddress.toKey(), testAddress.toKey());
        assertEquals(assertAddress.toString(), testAddress.toString());
    }
    @Test
    public void testStreetNameWithNumberInTheMiddle() {
        Address assertAddress = new Address("Christian 10. Gade", "11", "1231");
        String stringAddress = "Christian 10. Gade 11, 1231 København S";
        Address testAddress = AddressBuilder.parse(stringAddress);
        assertEquals(assertAddress.getCoordinates(), testAddress.getCoordinates());
        assertEquals(assertAddress.getCity(), testAddress.getCity());
        assertEquals(assertAddress.getStreet(), testAddress.getStreet());
        assertEquals(assertAddress.getHouse(), testAddress.getHouse());
        assertEquals(assertAddress.toKey(), testAddress.toKey());
        assertEquals(assertAddress.toString(), testAddress.toString());
    }
    @Test
    public void testStreetNameWithNumberLast(){
        Address assertAddress = new Address("Vej 5", "9", "2000");
        String stringAddress = "Vej 5 9, 2000 Frederiksberg";
        Address testAddress = AddressBuilder.parse(stringAddress);
        assertEquals(assertAddress.getCoordinates(), testAddress.getCoordinates());
        assertEquals(assertAddress.getCity(), testAddress.getCity());
        assertEquals(assertAddress.getStreet(), testAddress.getStreet());
        assertEquals(assertAddress.getHouse(), testAddress.getHouse());
        assertEquals(assertAddress.toKey(), testAddress.toKey());
        assertEquals(assertAddress.toString(), testAddress.toString());
    }

    @Test
    public void testTooLongPostCode(){
        Address assertAddress = new Address("Islands Brygge", "10", "2300");
        String stringAddresLength5 = "Islands Brygge 10, 23000 city of Danmark";
        Address testAddress = AddressBuilder.parse(stringAddresLength5);
        assertEquals(assertAddress.getCoordinates(), testAddress.getCoordinates());
        assertEquals(assertAddress.getCity(), testAddress.getCity());
        assertEquals(assertAddress.getStreet(), testAddress.getStreet());
        assertEquals(assertAddress.getHouse(), testAddress.getHouse());
        assertEquals(assertAddress.toKey(), testAddress.toKey());
        assertEquals(assertAddress.toString(), testAddress.toString());
    }
    @Test
    public void testLetterInHouseNumber() {
        Address assertAddress = new Address("Islands Brygge", "10B", "2300");
        String stringAddresLength5 = "Islands Brygge 10B, 2300 København S";
        Address testAddress = AddressBuilder.parse(stringAddresLength5);
        assertEquals(assertAddress.getCoordinates(), testAddress.getCoordinates());
        assertEquals(assertAddress.getCity(), testAddress.getCity());
        assertEquals(assertAddress.getStreet(), testAddress.getStreet());
        assertEquals(assertAddress.getHouse(), testAddress.getHouse());
        assertEquals(assertAddress.toKey(), testAddress.toKey());
        assertEquals(assertAddress.toString(), testAddress.toString());
    }
    @Test
    public void testStrangeHouseNumber() {
        Address assertAddress = new Address("Levantkaj", "4-14", "2150");
        String stringAddresLength5 = "Levantkaj 4-14, 2150 Nordhavn";
        Address testAddress = AddressBuilder.parse(stringAddresLength5);
        assertEquals(assertAddress.getCoordinates(), testAddress.getCoordinates());
        assertEquals(assertAddress.getCity(), testAddress.getCity());
        assertEquals(assertAddress.getStreet(), testAddress.getStreet());
        assertEquals(assertAddress.getHouse(), testAddress.getHouse());
        assertEquals(assertAddress.toKey(), testAddress.toKey());
        assertEquals(assertAddress.toString(), testAddress.toString());
    }


//    @Test(expected = Error.class)
//    public void testParseInvalidPostCode(){
//        String stringAddresLength5 = "Islands Brygge 10, 99999 city of Danmark";
//        Address testAddress = AddressBuilder.parse(stringAddresLength5);
//        assertEquals(assertAddress.getCity(), testAddress.getCity());
//        //todo regex shouldn't accept postcode with length != 4
//    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyInput(){
        Address testAddress = AddressBuilder.parse("");
    }




}