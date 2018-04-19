package controller;

import helpers.ColorMap;
import model.IOModel;
import model.MetaModel;

import javax.swing.*;

public class MenuController {
    private static ColorMap.Mode mode;

    public MenuController(MetaModel m) {
        mode = ColorMap.Mode.STANDARD;
    }
    
     public void load(JFrame window) {
        // Create a fileChooser to get new OSM file
         JFileChooser fileChooser = new JFileChooser();
         fileChooser.setCurrentDirectory(new java.io.File("."));

         if (fileChooser.showOpenDialog(window) == JFileChooser.APPROVE_OPTION) {
             IOModel.instance.loadFromString(fileChooser.getSelectedFile().toString());
         }
     }

    public void save() {
        System.out.println("Save");
    }

    public void quit() {
        System.exit(1);
    }

    public void standardMode() {
        mode = ColorMap.Mode.STANDARD;
        CanvasController.repaintMap();
    }

    public void protanopiaMode() {
        mode = ColorMap.Mode.PROTANOPIA;
        CanvasController.repaintMap();
    }

    public void deuteranopiaMode() {
        mode = ColorMap.Mode.DEUTERANOPIA;
        CanvasController.repaintMap();
    }

    public void tritanopiaMode() {
        mode = ColorMap.Mode.TRITANOPIA;
        CanvasController.repaintMap();
    }

    public void grayscaleMode() {
        mode = ColorMap.Mode.GRAYSCALE;
        CanvasController.repaintMap();
    }

    public static ColorMap.Mode getMode() {
        return mode;
    }

}
