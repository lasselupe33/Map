package controller;

import model.Address;
import view.AddressView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddressController extends MouseAdapter {
    private StateController stateController;
    private Address currAddress = new Address(0, 0);

    public AddressController(StateController sc) {
        stateController = sc;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (e.getComponent().getName()) {
            case "navigation":
                goToNavigation();
                break;
        }
    }

    /** To be called as soon as the AddressView has been created in order to setup listeners */
    public void addView(AddressView av) {
        // Intercept all mouse events to ensure canvas commands won't be called when clicking on the addressView
        av.addMouseListener(this);
        av.addMouseWheelListener(this);
        av.addMouseMotionListener(this);
    }

    /** Helper that sets the currently entered address */
    public void setCurrentAddress(Address address) {
        currAddress = address;
    }

    /** To be called when the user wishes to go to the navigation view */
    public void goToNavigation() {
        stateController.updateCurrentState(ViewStates.NAVIGATION_ACTIVE);
    }

    /** Helper that returns the current address */
    public Address getAddress() {
        return currAddress;
    }
}
