package controller;

import model.Address;
import view.NavigationView;

public class NavigationController {
    NavigationView navigationView;


    public NavigationController(){
    }

    public void addView(NavigationView nv){
        navigationView = nv;
    }


    public void setStartAddress(Address address){
        navigationView.getStartInput().setText(address.toString());
    }

    public void setEndAddress(Address address) {
        navigationView.getEndInput().setText(address.toString());

    }
}
