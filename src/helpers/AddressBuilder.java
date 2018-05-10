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
        //final String street = "(?<street>[a-zA-ZåæøÅÆØéÈÉè\\s.]*[^\\d\\s,])";
        final String street = "(?<street>\\d*[a-zA-ZåæøÅÆØéÈÉè\\s.'´üöäë-]*[^\\d\\s,])";
        final String house = "(?<house>\\d+[^\\,\\s\\.]?)";
        final String floor = "(?<floor>[\\d^]+)\\.\\s*)";
        final String side = "(?<side>[tTmM]{1}.?[VHvhfF]{1})";
        final String postCode = "(?<postcode>\\d{4})";
        final String city = "(?<city>[a-zA-ZåæøÅÆØ\\s]+)";
        
        final String regex = "(?:" + street +"\\s*" + house +"?(?:\\,*\\s*" + floor +"?(?:" + side +
                "?\\.)?)?(?:\\,+\\s*)?(?:(?:" + postCode +")?(?:\\d*)\\s*" + city + "?)?";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);

        // Regex that accepts street names with numbers last i.g. Vej 5
        final String street2 = "(?<street>\\d*[a-zA-ZåæøÅÆØéÈÉè\\s.'´üöäë-]*[^\\s,])";

        final String regex2 = "(?:" + street2 +"\\s*" + house +"?(?:\\,*\\s*" + floor +"?(?:" + side +
                "?\\.)?)?(?:\\,+\\s*)?(?:(?:" + postCode +")?(?:\\d*)\\s*" + city + "?)?";

        Pattern pattern2 = Pattern.compile(regex2);
        Matcher matcher2 = pattern2.matcher(s);

        final String street3 = "(?<street>[a-zA-ZåæøÅÆØéÈÉè\\s.\\'´üöäë\\d-]*[^\\d\\s,])";

        final String regex3 = "(?:" + street3 +"\\s*" + house +"?(?:\\,*\\s*" + floor +"?(?:" + side +
                "?\\.)?)?(?:\\,+\\s*)?(?:(?:" + postCode +")?(?:\\d*)\\s*" + city + "?)?";

        Pattern pattern3 = Pattern.compile(regex3);
        Matcher matcher3 = pattern3.matcher(s);


        AddressBuilder b = new AddressBuilder();

        if (matcher.matches()) {
            return b.city(matcher.group("city")).
                    house(matcher.group("house")).
                    postcode(matcher.group("postcode")).
                    street(matcher.group("street")).build();
        } else if (matcher2.matches()) {
            return b.city(matcher2.group("city")).
                    house(matcher2.group("house")).
                    postcode(matcher2.group("postcode")).
                    street(matcher2.group("street")).build();
        }
        else if (matcher3.matches()){
            return b.city(matcher3.group("city")).
                    house(matcher3.group("house")).
                    postcode(matcher3.group("postcode")).
                    street(matcher3.group("street")).build();
        }

        throw new Error("Address invalid!");
    }
}
