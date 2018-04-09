import controller.*;
import model.IOModel;
import model.MainModel;
import model.MapModel;
import view.*;

import javax.swing.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

public class Main {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl","True");

        SwingUtilities.invokeLater(() -> {
            long timeA = System.currentTimeMillis();

            // Models
            MainModel model = new MainModel();
            MapModel mapModel = new MapModel(model);
            IOModel ioModel;

            if (args.length == 0) {
                // Attempt to load binary file if it exists, else fallback to default .osm-map
                File binaryData = null;

                try {
                    binaryData = new File(URLDecoder.decode(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "data/map.bin", "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (binaryData.exists()) {
                    // If binary data exists, use this.
                    ioModel = new IOModel(model, mapModel);
                } else {
                    // .. else fallback to provided .zip
                    URL data = Main.class.getResource("/data/small.zip");
                    ioModel = new IOModel(model, mapModel, data);
                }
            } else {
                // .. or, if arguments are supplied, always use these.
                ioModel = new IOModel(model, mapModel, args[0]);
            }

            // Controllers
            MenuController mc = new MenuController(model);
            CanvasController cc = CanvasController.getInstance();
            StateController sc = new StateController();
            AddressController ac = new AddressController(sc);
            SearchBoxController sbc = new SearchBoxController(model, sc, ac);

            // Views
            CanvasView cv = new CanvasView(cc);
            cc.addDependencies(cv, mapModel);
            AddressView av = new AddressView(ac);
            ac.addView(av);
            SearchBox sb = new SearchBox(sc, sbc);
            sbc.addView(sb);
            FooterView fv = new FooterView(cc);
            ZoomView zv = new ZoomView(cc);
            NavigationView nv = new NavigationView();

            MainWindowView v = new MainWindowView(cv, model, cc, mc, av, sb, zv, sc, nv, fv);
            sc.addMainView(v);

            new KeyboardController(v, cv, model, cc, ioModel);
            new MouseController(cv, cc);
            new ResizeController(v);

            long timeB = System.currentTimeMillis();
            System.out.println("Elapsed time:" + (timeB - timeA));
        });
    }
}
