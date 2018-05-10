package view;

import controller.AddressController;
import controller.MapController;
import helpers.StateHandler;
import helpers.ViewStates;


import javax.swing.*;

public class FavoritePopupView extends JOptionPane {
    private String name;
    private AddressController addressController;
    private StateHandler stateHandler;

    public FavoritePopupView(AddressController addressController, StateHandler stateHandler){
        this.addressController = addressController;
        this.stateHandler = stateHandler;
    }

    public void addFrame(JFrame frame){
        ViewStates prevState = stateHandler.getPrevPanel();
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
            stateHandler.updateCurrentState(ViewStates.FAVORITES);
            stateHandler.forcePrevState(ViewStates.INITIAL);
            MapController.getInstance().deleteLocationCoordinates();
        } else {
            stateHandler.updateCurrentState(stateHandler.getPrevState());
            stateHandler.forcePrevState(prevState);
        }



    }

    public String getName(){
        return name;
    }

}
