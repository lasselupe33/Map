package controller;

import view.NavigationView;

public class NavigationController {
    NavigationView navigationView;


    public NavigationController(){
    }
    public void addView(NavigationView nv){
        navigationView = nv;
    }

    public void setStartInput(String s){
        navigationView.getStartInput().setText(s);
    }
}
