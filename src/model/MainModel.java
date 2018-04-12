package model;


import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

import java.io.*;
import java.net.URLDecoder;

public class MainModel implements Serializable{
    private AddressesModel addresses;
    private double minLat, minLon, maxLat, maxLon;

    public MainModel(AddressesModel am){
        addresses = am;
    }

    /** Getters */
    public AddressesModel getAddresses() {
        return addresses;
    }

    public double getMinLat() {
        return minLat;
    }

    public double getMinLon() {
        return minLon;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public double getMaxLon() {
        return maxLon;
    }

    /** Setters */
    public void setAddresses(AddressesModel addresses) {
        this.addresses = addresses;
    }

    public void setMinLat(double minLat){this.minLat = minLat;}

    public void setMinLon(double minLon){this.minLon = minLon;}

    public void setMaxLat(double maxLat){this.maxLat = maxLat;}

    public void setMaxLon(double maxLon){this.maxLon = maxLon;}

    /** Internal helper the serializses the MainModel */
    public void serialize() {
        try {
            String path = URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "data/main.bin", "UTF-8");
            FSTObjectOutput out = new FSTObjectOutput(new FileOutputStream(path));

            // Add data to model
            out.writeObject(minLon);
            out.writeObject(minLat);
            out.writeObject(maxLon);
            out.writeObject(maxLat);
            out.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Internal helper that deserializses the MainModel */
    public void deserialize() {
        try {
            String path = URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "data/main.bin", "UTF-8");
            FSTObjectInput in = new FSTObjectInput(new FileInputStream(path));

            // Add data to model
            minLon = (double) in.readObject();
            minLat = (double) in.readObject();
            maxLon = (double) in.readObject();
            maxLat = (double) in.readObject();

            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
