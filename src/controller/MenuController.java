package controller;

import helpers.ColorMap;
import model.IOModel;
import model.MainModel;
import view.MainWindowView;

import javax.swing.*;

public class MenuController {
    private static ColorMap.Mode mode;
    private IOModel ioModel;

    public MenuController(MainModel m, IOModel iom) {
        mode = ColorMap.Mode.STANDARD;
        ioModel = iom;
    }
    
     public void load(JFrame window) {
        // Create a fileChooser to get new OSM file
         JFileChooser fileChooser = new JFileChooser();
         fileChooser.setCurrentDirectory(new java.io.File("."));

         if (fileChooser.showOpenDialog(window) == JFileChooser.APPROVE_OPTION) {
             ioModel.load(fileChooser.getSelectedFile().toString());
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
    }

    public void colorBlindMode() {
        mode = ColorMap.Mode.COLORBLIND;
    }

    public static ColorMap.Mode getMode() {
        return mode;
    }


}
