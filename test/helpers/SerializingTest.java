package helpers;

import helpers.io.SerializeObject;
import main.Main;
import org.junit.Test;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;

import static org.junit.Assert.*;

public class SerializingTest {

    @Test
    public void testSerialization() throws Exception {
        String testObject = "TESTER123";

        new SerializeObject("test", testObject);

        // Give time to save object (happens on another thread)
        Thread.sleep(100);

        // Retrieve object
        URL path = Main.class.getResource("/data/test.bin");
        InputStream stream = path.openStream();
        ObjectInputStream in = new ObjectInputStream(stream);
        String readObject = (String) in.readObject();
        in.close();

        assertEquals(testObject, readObject);
    }
}