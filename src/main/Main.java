package main;

import controller.*;
import model.IOModel;
import model.MainModel;
import model.MapModel;
import view.*;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

public class Main {
    // Keep references to all created classes
    private static MainModel model;
    private static MapModel mapModel;
    private static IOModel ioModel;
    private static MenuController mc;
    private static CanvasController cc;
    private static StateController sc;
    private static AddressController ac;
    private static SearchBoxController sbc;
    private static CanvasView cv;
    private static AddressView av;
    private static SearchBox sb;
    private static FooterView fv;
    private static ZoomView zv;
    private static NavigationView nv;

    // Boolean to ensure application won't be booted twice
    public static boolean hasInitialized = false;
    public static boolean dataLoaded = false;

    // Debugging
    private static long timeA;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            System.setProperty("sun.java2d.opengl", "True");

            timeA = System.currentTimeMillis();

            // Models
            model = new MainModel();
            mapModel = new MapModel(model);
            // Attempt to load binary file if it exists, else fallback to default .osm-map
            URL binaryData;

            if (args.length == 0) {
                binaryData = Main.class.getResource("/data/main.bin");

                if (binaryData != null) {
                    // If binary data exists, use this.
                    ioModel = new IOModel(model, mapModel);
                } else {
                    // .. else fallback to provided .zip
                    URL data = Main.class.getResource("/data/small.zip");
                    ioModel = new IOModel(model, mapModel, data);
                    dataLoaded = true;
                }
            } else {
                // .. or, if arguments are supplied, always use these.
                ioModel = new IOModel(model, mapModel, args[0]);
                dataLoaded = true;
            }

            // Controllers
            mc = new MenuController(model);
            cc = CanvasController.getInstance();
            sc = new StateController();
            ac = new AddressController(sc);
            sbc = new SearchBoxController(model, sc, ac);

            // Views
            cv = new CanvasView(cc);
            cc.addDependencies(cv, mapModel);
            av = new AddressView(ac);
            ac.addView(av);
            sb = new SearchBox(sc, sbc);
            sbc.addView(sb);
            fv = new FooterView(cc);
            zv = new ZoomView(cc);
            nv = new NavigationView();

            // Run application if data is ready
            if (dataLoaded) {
                Main.run();
            }

            // Indicate application MVC has been initialized
            hasInitialized = true;
        });
    }

    /** Function to be run after all MVC classes have been initilized and data loaded */
    public static void run() {
        MainWindowView v = new MainWindowView(cv, model, cc, mc, av, sb, zv, sc, nv, fv);

        new KeyboardController(v, cv, model, cc, ioModel);
        new MouseController(cv, cc);
        new ResizeController(v);

        long timeB = System.currentTimeMillis();
        System.out.println("Elapsed time:" + (timeB - timeA));
    }
}
