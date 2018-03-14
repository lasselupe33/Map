import controller.*;
import model.MainModel;
import view.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainModel model;

            if (args.length == 0) {
                model = new MainModel();
            } else {
                model = new MainModel(args[0]);
            }

            // Controllers
            MenuController mc = new MenuController(model);
            CanvasController cc = CanvasController.getInstance();
            StateController sc = new StateController();
            SearchBoxController sbc = new SearchBoxController(sc);

            // Views
            CanvasView cv = new CanvasView(model, cc);
            cc.addCanvas(cv);
            AddressView av = new AddressView();
            SearchBox sb = new SearchBox(sc, sbc);

            ZoomView zv = new ZoomView(cc);

            sbc.addView(sb);


            MainWindowView v = new MainWindowView(cv, model, cc, mc, av, sb, zv, sc);
            sc.addMainView(v);
            new KeyboardController(v, cv, model, cc);
            new MouseController(cv, cc);
        });
    }
}
