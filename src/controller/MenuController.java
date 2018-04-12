package controller;

import helpers.ColorMap;
import model.MainModel;

public class MenuController {
    private static ColorMap.Mode mode;
    public MenuController(MainModel m) { mode = ColorMap.Mode.STANDARD; }
    
     public void load() {
         System.out.println("Load");
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

    public void colorBlindMode() {
        mode = ColorMap.Mode.COLORBLIND;
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
