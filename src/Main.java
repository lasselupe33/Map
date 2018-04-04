import controller.*;
import model.Favorites;
import model.IOModel;
import model.MainModel;
import view.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Models
            MainModel model = new MainModel();
            IOModel ioModel;

            if (args.length == 0) {
                ioModel = new IOModel(model);
            } else {
                ioModel = new IOModel(model, args[0]);
            }

            // Controllers
            MenuController mc = new MenuController(model);
            CanvasController cc = CanvasController.getInstance();
            StateController sc = new StateController();
            AddressController ac = new AddressController(sc);
            SearchBoxController sbc = new SearchBoxController(model, sc, ac);

            // Views
            CanvasView cv = new CanvasView(model, cc);
            cc.addCanvas(cv);
            AddressView av = new AddressView(ac);
            ac.addView(av);
            SearchBox sb = new SearchBox(sc, sbc);
            sbc.addView(sb);
            FooterView fv = new FooterView(cc);
            ZoomView zv = new ZoomView(cc);
            NavigationView nv = new NavigationView();
            FavoriteView favoriteView = new FavoriteView(new Favorites());

            MainWindowView v = new MainWindowView(cv, model, cc, mc, av, sb, zv, sc, nv, fv, favoriteView);
            sc.addMainView(v);

            new KeyboardController(v, cv, model, cc, ioModel);
            new MouseController(cv, cc);
            new ResizeController(v);
        });
    }
}
