package helpers;

import model.IOModel;
import model.MapElements.MapElement;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.nustaq.serialization.util.FSTInputStream;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;

public class DeserializeObject implements Runnable {
    Object callbackClass;
    Method callback;
    Class objectType;
    Object toSerialize;
    String name;

    public DeserializeObject(String name, Class objectType, Object callbackClass, Method callback) {
        this.toSerialize = toSerialize;
        this.callbackClass = callbackClass;
        this.callback = callback;
        this.name = name;
        this.objectType = objectType;

        new Thread(this, "deserializer-" + name).start();
    }

    public void run() {
        try {
            // Setup output path
            String path = URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "data/" + name + ".bin", "UTF-8");
            InputStream stream = new FileInputStream(path);
            FSTObjectInput in = IOModel.conf.getObjectInput(stream);

            // Read given object
            callback.invoke(callbackClass, in.readObject(objectType), name);

            stream.close();
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
