package main;

import controller.*;
import model.*;
import view.*;

import javax.swing.*;
import java.net.URL;

public class Main {
    // Keep references to all created classes
    private static MetaModel model;
    private static MapModel mapModel;
    private static Favorites favoritesModel;
    private static MenuController mc;
    private static CanvasController cc;
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
        IOModel.instance.addModels(model, mapModel, addressesModel);
        favoritesModel = new Favorites();

        fv = new FooterView(cc);
        IOModel.instance.addView(fv);


        // Attempt to load binary file if it exists, else fallback to default .osm-map
        URL binaryData;

        if (args.length == 0) {
            binaryData = Main.class.getResource("/data/meta.bin");

            if (binaryData != null) {
                // If binary data exists, use this.
                IOModel.instance.loadFromBinary();
            } else {
                // .. else fallback to provided .zip
                URL data = Main.class.getResource("/data/small.zip");
                IOModel.instance.loadFromURL(data);
                dataLoaded = true;
            }
        } else {
            // .. or, if arguments are supplied, always use these.
            IOModel.instance.loadFromString(args[0]);
            dataLoaded = true;
        }


        // Controllers
        mc = new MenuController(model);
        cc = CanvasController.getInstance();
        sc = new StateController();
        ac = new AddressController(sc, favoritesModel);
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
            fav = new FavoriteView(favoritesModel, fc);
            favp = new FavoritePopupView(ac, sc);
            acc.addDependencies(al, sb, addressesModel);
            ac.addView(av, fav);

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
        SwingUtilities.invokeLater(() -> {
            MainWindowView v = new MainWindowView(cv, model, cc, mc, av, sb, zv, sc, nv, fv, fav, fc, al, favoritesModel, favp);
            sc.addMainView(v);

            new KeyboardController(v, cv, model, cc);
            new MouseController(cv, cc);
            new ResizeController(v);

            initialRender = false;
        });
    }
}
