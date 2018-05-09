package controller;

import helpers.ColorMap;
import helpers.io.IOHandler;
import model.graph.Graph;
import view.HelpWindow;

import javax.swing.*;

public class MenuController {
    private ColorMap colorMap;
    private Graph graph;

    public MenuController(ColorMap cm, Graph g) {
        colorMap = cm;
        graph = g;
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
        MapController.repaintMap(true);
    }

    public void protanopiaMode() {
        colorMap.setMode(ColorMap.Mode.PROTANOPIA);
        MapController.repaintMap(true);
    }

    public void deuteranopiaMode() {
        colorMap.setMode(ColorMap.Mode.DEUTERANOPIA);
        MapController.repaintMap(true);
    }

    public void grayscaleMode() {
        colorMap.setMode(ColorMap.Mode.GRAYSCALE);
        MapController.repaintMap(true);
    }

    public void help() {
        HelpWindow helpWindow = new HelpWindow(HelpWindow.HelpType.HELP);
    }

    public void userManual() {
        HelpWindow helpWindow = new HelpWindow(HelpWindow.HelpType.USERMANUAL);

    }
}
