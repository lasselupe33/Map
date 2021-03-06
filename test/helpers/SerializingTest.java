package helpers;

import helpers.io.IOHandler;
import helpers.io.SerializeObject;
import main.Main;
import model.AddressesModel;
import model.FavoritesModel;
import model.MapModel;
import model.MetaModel;
import model.graph.Graph;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;

import static org.junit.Assert.*;

public class SerializingTest {
    private static MetaModel m;
    private static MapModel mm;
    private static AddressesModel am;
    private static Graph g;
    private static FavoritesModel fm;

    @BeforeClass
    public static void setup() throws Exception {
        g = new Graph();
        m = new MetaModel();
        mm = new MapModel(m, g);
        am = new AddressesModel();
        fm = new FavoritesModel();

        IOHandler.instance.testMode = true;
        IOHandler.instance.addModels(m, mm, am, g, fm);
        IOHandler.instance.loadFromString("./test/data/tiny.osm");

        // Give time to parse osm on another thread
        Thread.sleep(3000);
    }

    @Test
    public void testSerialization() throws Exception {
        // Save actual data
        IOHandler.instance.save();

        // Save test object
        String testObject = "TESTER123";
        new SerializeObject("test", testObject);

        // Give time to save object (happens on another thread)
        Thread.sleep(500);

        // Ensure load function doesn't fail either.
        IOHandler.instance.loadFromBinary(false);

        // Retrieve actual test object
        URL path = Main.class.getResource("/BFST18_binary_test/test.bin");
        InputStream stream = path.openStream();
        ObjectInputStream in = new ObjectInputStream(stream);
        String readObject = (String) in.readObject();
        in.close();

        assertEquals(testObject, readObject);
    }
}