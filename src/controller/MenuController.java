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

    /**
     * Allow the user to specify an OSM file to use for the map
     * Load the data and draw map based on it
     * @param window
     */
     public void load(JFrame window) {
        // Create a fileChooser to get new OSM file
         JFileChooser fileChooser = new JFileChooser();
         fileChooser.setCurrentDirectory(new java.io.File("."));

         if (fileChooser.showOpenDialog(window) == JFileChooser.APPROVE_OPTION) {
             IOHandler.instance.loadFromString(fileChooser.getSelectedFile().toString());
         }
     }

    /**
     * Save the map to a binary file
     */
    public void save() {
        IOHandler.instance.save();
    }

    /**
     * Quit the program
     */
    public void quit() {
        System.exit(1);
    }

    /**
     * Set color settings to standard colors
     */
    public void standardMode() {
        colorMap.setMode(ColorMap.Mode.STANDARD);
        MapController.repaintMap(true);
    }

    /**
     * Set color settings to protanopia
     */
    public void protanopiaMode() {
        colorMap.setMode(ColorMap.Mode.PROTANOPIA);
        MapController.repaintMap(true);
    }

    /**
     * Set color settings to deuteranopia
     */
    public void deuteranopiaMode() {
        colorMap.setMode(ColorMap.Mode.DEUTERANOPIA);
        MapController.repaintMap(true);
    }

    /**
     * Set color settings to grayscale
     */
    public void grayscaleMode() {
        colorMap.setMode(ColorMap.Mode.GRAYSCALE);
        MapController.repaintMap(true);
    }

    /**
     * Open help window with generel information about how to use the program
     */
    public void help() {
        HelpWindow helpWindow = new HelpWindow(HelpWindow.HelpType.HELP);
    }

    /**
     * Open help window with basic user manual
     */
    public void userManual() {
        HelpWindow helpWindow = new HelpWindow(HelpWindow.HelpType.USERMANUAL);

    }
}
