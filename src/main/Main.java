package main;

import controller.*;
import helpers.io.IOHandler;
import model.*;
import view.*;

import javax.swing.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    // Keep references to all created classes
    private static AddressesModel am;
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
        am = new AddressesModel();
        model = new MetaModel();
        mapModel = new MapModel(model);
        IOHandler.instance.addModels(model, mapModel, am);
        favoritesModelModel = new FavoritesModel();

        fv = new FooterView(cc);
        IOHandler.instance.addView(fv);


        // dataSource Priority:
        // 1. Program arguments
        // 2. (if .jar) external .bin
        // 3. Internal .bin
        if (args.length == 0) {
            if (IOHandler.instance.isJar) {
                try {
                    if (Files.exists(Paths.get(new URI(IOHandler.externalRootPath + "/data")))) {
                        IOHandler.instance.loadFromBinary(true);
                    } else {
                        IOHandler.instance.loadFromBinary(false);
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            } else {
                IOHandler.instance.loadFromBinary(false);
            }
        } else {
            // .. or, if arguments are supplied, always use these.
            IOHandler.instance.loadFromString(args[0]);
        }


        // Controllers
        mc = new MenuController(model);
        cc = MapController.getInstance();
        sc = new StateController();
        ac = new AddressController(sc, favoritesModelModel);
        sbc = new SearchBoxController(model, sc, ac, am);
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
            nc.addView(nv);
            al = new AutoCompleteList(acc);
            fav = new FavoriteView(favoritesModelModel, fc);
            favp = new FavoritePopupView(ac, sc);
            acc.addDependencies(al, sb, am);
            ac.addView(av, fav);

            // Indicate application MVC has been initialized
            hasInitialized = true;

            // Run application if data is ready
            if (dataLoaded) {
                Main.run();
            }
        });
    }

    /** Function to be run after all MVC classes have been initilized and data loaded */
    public static void run() {
        SwingUtilities.invokeLater(() -> {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

            MainWindowView v = new MainWindowView(cv, model, cc, mc, av, sb, zv, sc, nv, fv, fav, fc, al, favoritesModelModel, favp);
            sc.addMainView(v);

            new KeyboardController(v, cv, model, cc);
            new MouseController(cv, cc, am, fv);
            new ResizeController(v);

            initialRender = false;
        });
    }
}
