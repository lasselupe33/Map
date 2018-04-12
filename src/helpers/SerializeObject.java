package helpers;

import main.Main;
import model.IOModel;
import model.MapElements.MapElement;
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
    String[] names;

    public SerializeObject(String[] names, Object toSerialize) {
        this.toSerialize = toSerialize;
        this.names = names;

        new Thread(this, "serializer-" + names[0]).start();
    }

    public void run() {
        try {
            for (String name : names) {
                // Setup output path
                URL path = new URL(Main.class.getResource("/data/") + "/" + name + ".bin");
                File file = new File(path.toURI());
                OutputStream stream = new FileOutputStream(file);
                FSTObjectOutput out = IOModel.conf.getObjectOutput(stream);

                // Write given object
                out.writeObject(toSerialize, toSerialize.getClass());
                out.flush();

                stream.close();
            }
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
