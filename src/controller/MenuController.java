package controller;

import model.MainModel;

public class MenuController {
    public MenuController(MainModel m) {}
    
     public void load() {
         System.out.println("Load");
     }

    public void save() {
        System.out.println("Save");
    }

    public void quit() {
        System.exit(1);
    }
}
