package controller;

import helpers.ColorMap;
import model.IOModel;
import model.MetaModel;

import javax.swing.*;

public class MenuController {
    public MenuController(MetaModel m) {}
    
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

    public static void standardMode() {
        ColorMap.setMode(ColorMap.Mode.STANDARD);
        CanvasController.repaintMap();
    }

    public void protanopiaMode() {
        ColorMap.setMode(ColorMap.Mode.PROTANOPIA);
        CanvasController.repaintMap();
    }

    public void deuteranopiaMode() {
        ColorMap.setMode(ColorMap.Mode.DEUTERANOPIA);
        CanvasController.repaintMap();
    }

    public void tritanopiaMode() {
        ColorMap.setMode(ColorMap.Mode.TRITANOPIA);
        CanvasController.repaintMap();
    }

    public void grayscaleMode() {
        ColorMap.setMode(ColorMap.Mode.GRAYSCALE);
        CanvasController.repaintMap();
    }

}
