import controller.CanvasController;
import controller.KeyboardController;
import controller.MenuController;
import controller.MouseController;
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
            CanvasController canvasController = CanvasController.getInstance();

            // Views
            CanvasView cv = new CanvasView(model, canvasController);
            canvasController.addCanvas(cv);
            AddressView av = new AddressView();
            SearchBox sb = new SearchBox();
            ZoomView zv = new ZoomView();

            MainWindowView v = new MainWindowView(cv, model, canvasController, mc, av, sb, zv);
            new KeyboardController(v, cv, model, canvasController);
            new MouseController(cv, model, canvasController);
        });
    }
}
