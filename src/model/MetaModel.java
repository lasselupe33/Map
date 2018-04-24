package model;

import helpers.io.IOHandler;
import main.Main;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

/** This model contains required metadata of the application such as minLat and maxLat */
public class MetaModel implements Serializable{
    private double minLat, minLon, maxLat, maxLon;

    public MetaModel(){}

    /** Getters */
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
    public void setMinLat(double minLat){this.minLat = minLat;}

    public void setMinLon(double minLon){this.minLon = minLon;}

    public void setMaxLat(double maxLat){this.maxLat = maxLat;}

    public void setMaxLon(double maxLon){this.maxLon = maxLon;}

    /** Internal helper the serializses the MetaModel */
    public void serialize() {
        try {
            URL path = new URL(IOHandler.externalRootPath + "/BFST18_binary/meta.bin");
            File file = new File(path.toURI());
            OutputStream stream = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(stream);

            // Add data to model
            out.writeDouble(minLon);
            out.writeDouble(minLat);
            out.writeDouble(maxLon);
            out.writeDouble(maxLat);
            out.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /** Internal helper that deserializses the MetaModel */
    public void deserialize() {
        try {
            URL path;

            // Get source
            if (IOHandler.useExternalSource) {
                path = new URL(IOHandler.externalRootPath + "/BFST18_binary/meta.bin");
            } else {
                path = Main.class.getResource("/BFST18_binary/meta.bin");
            }

            ObjectInputStream in = new ObjectInputStream(path.openStream());

            // Add data to model
            minLon = in.readDouble();
            minLat = in.readDouble();
            maxLon = in.readDouble();
            maxLat = in.readDouble();

            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
