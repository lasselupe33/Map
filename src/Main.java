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
            CanvasView cv = new CanvasView(model);
            MainWindowView v = new MainWindowView(cv, model);
            new KeyboardController(v, cv, model);
            new MouseController(cv, model);
        });
    }
}
