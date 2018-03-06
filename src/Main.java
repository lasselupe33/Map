import controller.CanvasController;
import controller.KeyboardController;
import controller.MouseController;
import model.MainModel;
import view.MainWindowView;
import view.CanvasView;

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

            CanvasController canvasController = CanvasController.getInstance();
            CanvasView cv = new CanvasView(model, canvasController);
            canvasController.addCanvas(cv);
            MainWindowView v = new MainWindowView(cv, model, canvasController);
            new KeyboardController(v, cv, model, canvasController);
            new MouseController(cv, model, canvasController);
        });
    }
}
