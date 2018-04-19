package helpers;

import main.Main;
import model.IOModel;
import model.MapElement;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

public class SerializeObject implements Runnable {
    Object toSerialize;
    String name;

    public SerializeObject(String name, Object toSerialize) {
        this.toSerialize = toSerialize;
        this.name = name;

        // A new serialization has been started, bump amount of serializations..
        IOModel.instance.onSerializationStart();

        // Begin serializing
        new Thread(this, "serializer-" + name).start();
    }

    public void run() {
        try {
            // Setup output path
            URL path = new URL(Main.class.getResource("/data/") + "/" + name + ".bin");
            File file = new File(path.toURI());
            OutputStream stream = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(stream);

            // Write given object
            out.writeObject(toSerialize);
            out.close();

            // Indicate that serialization has been completed!
            IOModel.instance.onObjectSerializationComplete();
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
