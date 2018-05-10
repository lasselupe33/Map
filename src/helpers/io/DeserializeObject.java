package helpers.io;

import main.Main;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;

/**
 * Helper responsible for deserializing a single object on a dedicated thread.
 */
public class DeserializeObject implements Runnable {
    Object callbackClass;
    Method callback;
    String name;

    public DeserializeObject(String name, Object callbackClass, Method callback) {
        this.callbackClass = callbackClass;
        this.callback = callback;
        this.name = name;

        // A new deserialization has been started, bump amount of deserializations..
        IOHandler.instance.onDeserializeStart();

        Thread thread = new Thread(this, "deserializer-" + name);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    public void run() {
        try {
            URL path;
            String folderName = "/BFST18_binary" + (IOHandler.instance.testMode ? "_test" : "") + "/";

            // Setup output path
            if (IOHandler.useExternalSource) {
                path = new URL(IOHandler.externalRootPath + folderName + name + ".bin");
            } else {
                path = Main.class.getResource(folderName + name + ".bin");
            }

            if (path != null) {
                InputStream stream = path.openStream();
                ObjectInputStream in = new ObjectInputStream(stream);

                // Read given object
                callback.invoke(callbackClass, in.readObject(), name);
                in.close();
            }

            // Indicate that deserialization has been completed!
            IOHandler.instance.onObjectDeserializationComplete();
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
