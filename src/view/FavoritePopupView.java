package view;

import controller.AddressController;
import controller.MapController;
import controller.StateController;
import controller.ViewStates;


import javax.swing.*;
import java.awt.*;

public class FavoritePopupView extends JOptionPane {
    private String name;
    private AddressController addressController;
    private StateController stateController;

    public FavoritePopupView(AddressController addressController, StateController stateController, AddressView av){
        this.addressController = addressController;
        this.stateController = stateController;

    }

    public void addFrame(JFrame frame){
        ViewStates prevState = stateController.getPrevPanel();
        name = (String)JOptionPane.showInputDialog(
                frame,
                "Name: ",
                "Favorite Address name",
                JOptionPane.PLAIN_MESSAGE,
                icon,
                null, "");

        while(name != null && name.isEmpty()){
            name = (String)JOptionPane.showInputDialog(
                    frame,
                    "Please insert a name \nName: ",
                    "Favorite Address name",
                    JOptionPane.PLAIN_MESSAGE,
                    icon,
                    null, "");
        }

        if(name != null){
            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
            addressController.saveAddress(name);
            stateController.updateCurrentState(ViewStates.FAVORITES);
            stateController.forcePrevState(ViewStates.INITIAL);
            MapController.deleteLocationCoordinates();
        } else {
            stateController.updateCurrentState(stateController.getPrevState());
            stateController.forcePrevState(prevState);
        }



    }

    public String getName(){
        return name;
    }

}
