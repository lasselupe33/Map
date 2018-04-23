package controller;

import helpers.ColorMap;
import model.IOModel;
import model.MetaModel;

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
        colorMap.setMode(ColorMap.Mode.STANDARD);
        CanvasController.repaintMap();
    }

    public void protanopiaMode() {
        colorMap.setMode(ColorMap.Mode.PROTANOPIA);
        CanvasController.repaintMap();
    }

    public void deuteranopiaMode() {
        colorMap.setMode(ColorMap.Mode.DEUTERANOPIA);
        CanvasController.repaintMap();
    }

    public void tritanopiaMode() {
        colorMap.setMode(ColorMap.Mode.TRITANOPIA);
        CanvasController.repaintMap();
    }

    public void grayscaleMode() {
        colorMap.setMode(ColorMap.Mode.GRAYSCALE);
        CanvasController.repaintMap();
    }

}
