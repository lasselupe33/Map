package helpers;

import main.Main;
import model.IOModel;
import model.MapElements.MapElement;
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
    Class objectType;
    Object toSerialize;
    String[] names;

    public DeserializeObject(String[] names, Class objectType, Object callbackClass, Method callback) {
        this.toSerialize = toSerialize;
        this.callbackClass = callbackClass;
        this.callback = callback;
        this.names = names;
        this.objectType = objectType;

        new Thread(this, "deserializer-" + names[0]).start();
    }

    public void run() {
        
        if (names.length == 1) {
            loadSingleObject();
        } else {
            loadList();
        }
    }

    public void loadSingleObject() {
        try {
            // Setup output path
            URL path = Main.class.getResource("/data/" + names[0] + ".bin");
            InputStream stream = path.openStream();
            FSTObjectInput in = IOModel.conf.getObjectInput(stream);

            // Read given object
            callback.invoke(callbackClass, in.readObject(objectType), names[0]);


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

    public void loadList() {
        try {
            ArrayList<MapElement> loadedList = new ArrayList<>();

            for (String name : names) {
                // Setup output path
                String path = URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "data/" + name + ".bin", "UTF-8");
                InputStream stream = new FileInputStream(path);
                FSTObjectInput in = IOModel.conf.getObjectInput(stream);

                // Read given object
                loadedList.addAll((ArrayList<MapElement>) in.readObject(objectType));

                stream.close();
            }


            callback.invoke(callbackClass, loadedList, names[0].split("-")[0]);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.getCause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
