package helpers;

import model.Address;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressBuilder {

    private String street = "Unknown", house, postcode, city;

    public AddressBuilder() {}

    public AddressBuilder street(String _street) { street = _street; return this; }
    public AddressBuilder house(String _house)   { house = _house;   return this; }
    public AddressBuilder postcode(String _postcode) { postcode = _postcode; return this; }
    public AddressBuilder city(String _city)     { city = _city;     return this; }
    public Address build() {
        return new Address(street, house, postcode);
    }

    public static Address parse(String s) {
        if (s.equals("")) {
            throw new IllegalArgumentException("Cannot parse empty string " + s);
        }
        final String street = "(?<street>[a-zA-ZåæøÅÆØéÈÉè\\s.]*[^\\d\\s,])";
        final String house = "(?<house>\\d+[^\\,\\s\\.]?)";
        final String floor = "(?<floor>[\\d^]+)\\.\\s*)";
        final String side = "(?<side>[tTmM]{1}.?[VHvhfF]{1})";
        final String postCode = "(?<postcode>\\d{4})";
        final String city = "(?<city>[a-zA-ZåæøÅÆØ\\s]+)"; 



        final String regex = "(?:" + street +"\\s*" + house +"?(?:\\,*\\s*" + floor +"?(?:" + side +
                "?\\.)?)?(?:\\,+\\s*)?(?:(?:" + postCode +")?(?:\\d*)\\s*" + city + "?)?";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);

        AddressBuilder b = new AddressBuilder();

        if (matcher.matches()) {
            return b.city(matcher.group("city")).
                    house(matcher.group("house")).
                    postcode(matcher.group("postcode")).
                    street(matcher.group("street")).build();
        }

        throw new Error("Address invalid!");
    }
}
