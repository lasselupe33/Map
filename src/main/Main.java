package main;

import controller.*;
import helpers.io.IOHandler;
import model.*;
import view.*;

import javax.swing.*;
import java.net.URL;

public class Main {
    // Keep references to all created classes
    private static MetaModel model;
    private static MapModel mapModel;
    private static FavoritesModel favoritesModelModel;
    private static MenuController mc;
    private static MapController cc;
    private static StateController sc;
    private static AddressController ac;
    private static SearchBoxController sbc;
    private static AutoCompleteController acc;
    private static FavoriteController fc;
    private static NavigationController nc;
    private static CanvasView cv;
    private static AddressView av;
    private static SearchBox sb;
    private static FooterView fv;
    private static ZoomView zv;
    private static NavigationView nv;
    private static AutoCompleteList al;
    private static FavoriteView fav;
    private static FavoritePopupView favp;

    // Boolean to ensure application won't be booted twice
    public static boolean hasInitialized = false;
    public static boolean dataLoaded = false;
    public static boolean initialRender = true;

    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "True");

        // Models
        AddressesModel addressesModel = new AddressesModel();
        model = new MetaModel();
        mapModel = new MapModel(model);
        IOHandler.instance.addModels(model, mapModel, addressesModel);
        favoritesModelModel = new FavoritesModel();

        fv = new FooterView(cc);
        IOHandler.instance.addView(fv);


        // Attempt to load binary file if it exists, else fallback to default .osm-map
        URL binaryData;

        if (args.length == 0) {
            binaryData = Main.class.getResource("/data/meta.bin");

            if (binaryData != null) {
                // If binary data exists, use this.
                IOHandler.instance.loadFromBinary();
            } else {
                // .. else fallback to provided .zip
                URL data = Main.class.getResource("/data/small.zip");
                IOHandler.instance.loadFromURL(data);
                dataLoaded = true;
            }
        } else {
            // .. or, if arguments are supplied, always use these.
            IOHandler.instance.loadFromString(args[0]);
            dataLoaded = true;
        }


        // Controllers
        mc = new MenuController(model);
        cc = MapController.getInstance();
        sc = new StateController();
        ac = new AddressController(sc, favoritesModelModel);
        sbc = new SearchBoxController(model, sc, ac, addressesModel);
        acc = new AutoCompleteController();
        nc = new NavigationController();
        fc = new FavoriteController(sc, sbc, nc);

        // Ensure views are being invoked on proper thread!
        SwingUtilities.invokeLater(() -> {
            // Views
            cv = new CanvasView(cc);
            cc.addDependencies(cv, mapModel, model);
            av = new AddressView(ac);
            sb = new SearchBox(sc, sbc, acc);
            sbc.addView(sb);
            zv = new ZoomView(cc);
            nv = new NavigationView(sc);
            al = new AutoCompleteList(acc);
            fav = new FavoriteView(favoritesModelModel, fc);
            favp = new FavoritePopupView(ac, sc);
            acc.addDependencies(al, sb, addressesModel);
            ac.addView(av, fav);

            // Indicate application MVC has been initialized
            hasInitialized = true;

            // Run application if data is ready
            if (dataLoaded) {
                System.out.println(dataLoaded);
                Main.run();
            }
        });
    }

    /** Function to be run after all MVC classes have been initilized and data loaded */
    public static void run() {
        SwingUtilities.invokeLater(() -> {
            MainWindowView v = new MainWindowView(cv, model, cc, mc, av, sb, zv, sc, nv, fv, fav, fc, al, favoritesModelModel, favp);
            sc.addMainView(v);

            new KeyboardController(v, cv, model, cc);
            new MouseController(cv, cc);
            new ResizeController(v);

            initialRender = false;
        });
    }
}
