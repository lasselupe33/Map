package controller;

import view.MainWindowView;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentListener;

public class ResizeController extends ComponentAdapter{
    private MainWindowView window;

    public ResizeController(MainWindowView mv){
        window = mv.getWindow();



    }

    public void componentResized(){


    }



}
