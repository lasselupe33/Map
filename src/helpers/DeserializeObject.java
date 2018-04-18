package helpers;

import main.Main;
import model.IOModel;
import model.MapElement;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.nustaq.serialization.util.FSTInputStream;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

public class DeserializeObject implements Runnable {
    Object callbackClass;
    Method callback;
    String name;

    public DeserializeObject(String name, Object callbackClass, Method callback) {
        this.callbackClass = callbackClass;
        this.callback = callback;
        this.name = name;

        // A new deserialization has been started, bump amount of deserializations..
        IOModel.instance.onDeserializeStart();

        new Thread(this, "deserializer-" + name).start();
    }

    public void run() {
        try {
            // Setup output path
            URL path = Main.class.getResource("/data/" + name + ".bin");
            InputStream stream = path.openStream();
            ObjectInputStream in = new ObjectInputStream(stream);

            // Read given object
            callback.invoke(callbackClass, in.readObject(), name);
            in.close();

            // Indicate that deserialization has been completed!
            IOModel.instance.onObjectDeserializationComplete();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
