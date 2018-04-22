package controller;

import helpers.ColorMap;
import helpers.io.IOHandler;
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
             IOHandler.instance.loadFromString(fileChooser.getSelectedFile().toString());
         }
     }

    public void save() {
        IOHandler.instance.save();
    }

    public void quit() {
        System.exit(1);
    }

    public void standardMode() {
        mode = ColorMap.Mode.STANDARD;
        MapController.repaintMap();
    }

    public void protanopiaMode() {
        mode = ColorMap.Mode.PROTANOPIA;
        MapController.repaintMap();
    }

    public void deuteranopiaMode() {
        mode = ColorMap.Mode.DEUTERANOPIA;
        MapController.repaintMap();
    }

    public void tritanopiaMode() {
        mode = ColorMap.Mode.TRITANOPIA;
        MapController.repaintMap();
    }

    public void grayscaleMode() {
        mode = ColorMap.Mode.GRAYSCALE;
        MapController.repaintMap();
    }

    public static ColorMap.Mode getMode() {
        return mode;
    }

}
