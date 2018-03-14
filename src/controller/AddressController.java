package controller;

import view.AddressView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddressController extends MouseAdapter {
    private StateController stateController;

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

    public void addView(AddressView av) {
        // Intercept all mouse events to ensure canvas commands won't be called when clicking on the addressView
        av.addMouseListener(this);
        av.addMouseWheelListener(this);
        av.addMouseMotionListener(this);
    }

    public void goToNavigation() {
        stateController.updateCurrentState(ViewStates.NAVIGATION_ACTIVE);
    }
}
