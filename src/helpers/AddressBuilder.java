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

        final String street = "(?<street>\\d*[a-zA-ZåæøÅÆØéÈÉè\\s.'´üöäë-]*[^\\d\\s,])";
        final String house = "(?<house>\\d+[A-ZÆÅØ]?[^\\,\\s\\.]?)";
        final String floor = "(?<floor>[\\d^]+)\\.\\s*)";
        final String side = "(?<side>[tTmM]{1}.?[VHvhfF]{1})";
        final String postCode = "(?<postcode>\\d{4})";
        final String city = "(?<city>[a-zA-ZåæøÅÆØ\\s]+)";
        
        final String regex = "(?:" + street +"\\s*" + house +"?\\s?(?:\\,*\\s*" + floor +"?(?:" + side +
                "?\\.)?)?(?:\\,+\\s*)?(?:(?:" + postCode +")?\\s(?:\\d*)\\s*" + city + "?)?";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);

        // Regex that accepts street names with numbers last i.g. Vej 5
        final String street2 = "(?<street>\\d*[a-zA-ZåæøÅÆØéÈÉè\\s.'´üöäë-]*[^\\s,])";

        final String regex2 = "(?:" + street2 +"\\s*" + house +"?(?:\\,*\\s*" + floor +"?(?:" + side +
                "?\\.)?)?(?:\\,+\\s*)?(?:(?:" + postCode +")?(?:\\d*)\\s*" + city + "?)?";

        Pattern pattern2 = Pattern.compile(regex2);
        Matcher matcher2 = pattern2.matcher(s);

        // Reges that accepts numbers in the middle of street name i.g. Christian 10. Gade
        final String street3 = "(?<street>[a-zA-ZåæøÅÆØéÈÉè\\s.\\'´üöäë\\d-]*[^\\d\\s,])";

        final String regex3 = "(?:" + street3 +"\\s*" + house +"?(?:\\,*\\s*" + floor +"?(?:" + side +
                "?\\.)?)?(?:\\,+\\s*)?(?:(?:" + postCode +")?(?:\\d*)\\s*" + city + "?)?";

        Pattern pattern3 = Pattern.compile(regex3);
        Matcher matcher3 = pattern3.matcher(s);

        // Regex that accepts interval house numbers i.g. Levantkaj 4-14
        final String street4 = "(?<street>[a-zA-ZåæøÅÆØéÈÉè\\s.\\'´üöäë-]*[^\\d\\s,])";
        final String house4 = "(?<house>[\\d-]+[^\\,\\s\\.]?)";

        final String regex4 = "(?:" + street4 +"\\s*" + house4 +"?(?:\\,*\\s*" + floor +"?(?:" + side +
                "?\\.)?)?(?:\\,+\\s*)?(?:(?:" + postCode +")?(?:\\d*)\\s*" + city + "?)?";

        Pattern pattern4 = Pattern.compile(regex4);
        Matcher matcher4 = pattern4.matcher(s);

        // Regex that accepts floor and side
        final String house5 = "(?<house>[\\d,]+[\\skldstvh1floor.]*[^\\,\\s]?)";

        final String regex5 = "(?:" + street4 +"\\s*" + house5 +"?(?:\\,*\\s*" + floor +"?(?:" + side +
                "?\\.)?)?(?:\\,+\\s*)?(?:(?:" + postCode +")?(?:\\d*)\\s*" + city + "?)?";

        Pattern pattern5 = Pattern.compile(regex5);
        Matcher matcher5 = pattern5.matcher(s);

        AddressBuilder b = new AddressBuilder();

        if (matcher.matches()) {
            if (matcher.group("postcode") == null) {
                throw new RuntimeException("Address invalid!");
            }
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
        else if (matcher4.matches()){
            return b.city(matcher4.group("city")).
                    house(matcher4.group("house")).
                    postcode(matcher4.group("postcode")).
                    street(matcher4.group("street")).build();
        }
        else if (matcher5.matches()){
            return b.city(matcher5.group("city")).
                    house(matcher5.group("house")).
                    postcode(matcher5.group("postcode")).
                    street(matcher5.group("street")).build();
        }
        else if (matcher3.matches()){
            return b.city(matcher3.group("city")).
                    house(matcher3.group("house")).
                    postcode(matcher3.group("postcode")).
                    street(matcher3.group("street")).build();
        }

        throw new RuntimeException("Address invalid!");
    }
}
