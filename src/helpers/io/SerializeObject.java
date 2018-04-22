package helpers.io;

import main.Main;

import java.io.*;
import java.net.URL;

public class SerializeObject implements Runnable {
    Object toSerialize;
    String name;

    public SerializeObject(String name, Object toSerialize) {
        this.toSerialize = toSerialize;
        this.name = name;

        // A new serialization has been started, bump amount of serializations..
        IOHandler.instance.onSerializationStart();

        // Begin serializing
        new Thread(this, "serializer-" + name).start();
    }

    public void run() {
        try {
            URL path;

            // Setup output path
            if (IOHandler.instance.isJar) {
                path = new URL(IOHandler.externalRootPath + "/data/" + name + ".bin");
            } else {
                path = new URL(Main.class.getResource("/data/") + "/" + name + ".bin");
            }

            File file = new File(path.toURI());
            OutputStream stream = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(stream);

            // Write given object
            out.writeObject(toSerialize);
            out.close();

            // Indicate that serialization has been completed!
            IOHandler.instance.onObjectSerializationComplete();


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
