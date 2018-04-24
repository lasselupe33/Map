package controller;

import helpers.ColorMap;
import helpers.io.IOHandler;
import model.MetaModel;
import view.LoadingScreen;

import javax.swing.*;

public class MenuController {
    private ColorMap colorMap;

    public MenuController(ColorMap cm) {
        colorMap = cm;
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
        colorMap.setMode(ColorMap.Mode.STANDARD);
        MapController.repaintMap();
    }

    public void protanopiaMode() {
        colorMap.setMode(ColorMap.Mode.PROTANOPIA);
        MapController.repaintMap();
    }

    public void deuteranopiaMode() {
        colorMap.setMode(ColorMap.Mode.DEUTERANOPIA);
        MapController.repaintMap();
    }

    public void tritanopiaMode() {
        colorMap.setMode(ColorMap.Mode.TRITANOPIA);
        MapController.repaintMap();
    }

    public void grayscaleMode() {
        colorMap.setMode(ColorMap.Mode.GRAYSCALE);
        MapController.repaintMap();
    }

}
