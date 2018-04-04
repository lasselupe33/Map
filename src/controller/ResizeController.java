package controller;

import view.MainWindowView;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


public class ResizeController extends ComponentAdapter{
    private JFrame window;
    private MainWindowView mainWindow;

    public ResizeController(MainWindowView mv){
        mainWindow = mv;
        window = mv.getWindow();
        window.addComponentListener(this);


    }

    public void componentResized(ComponentEvent e) {
        mainWindow.update();
    }



}
