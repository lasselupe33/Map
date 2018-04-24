package view;

import controller.AddressController;
import controller.StateController;
import controller.ViewStates;


import javax.swing.*;
import java.awt.*;

public class FavoritePopupView extends JOptionPane {
    private String name;
    private AddressController addressController;
    private StateController stateController;

    public FavoritePopupView(AddressController addressController, StateController stateController){
        this.addressController = addressController;
        this.stateController = stateController;


    }

    public void addFrame(JFrame frame){
        name = (String)JOptionPane.showInputDialog(
                frame,
                "Name: ",
                "Favorite Address name",
                JOptionPane.PLAIN_MESSAGE,
                icon,
                null, "");
        while(name.isEmpty()){
            name = (String)JOptionPane.showInputDialog(
                    frame,
                    "Please insert a name \nName: ",
                    "Favorite Address name",
                    JOptionPane.PLAIN_MESSAGE,
                    icon,
                    null, "");
        }
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        addressController.saveAddress(name);
        stateController.updateCurrentState(ViewStates.FAVORITES);


    }

    public String getName(){
        return name;
    }

}
