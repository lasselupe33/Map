package controller;

import view.MainWindowView;

/**
 * Global controller that takes care of handling the current state of the application, i.e. which view should currently
 * be visible.
 * This will often be called from other controllers.
 */
public class StateController {
    private ViewStates currentState;
    private MainWindowView mainView;
    private ViewStates prevState;

    public StateController() {
        // Setup initial state
        currentState = ViewStates.INITIAL;
        prevState = ViewStates.INITIAL;
    }

    public void addMainView(MainWindowView view) {
        mainView = view;
    }

    public ViewStates getCurrentState() {
        return currentState;
    }

    public ViewStates getPrevState() { return prevState; }

    public void updateCurrentState(ViewStates newState) {
        currentState = newState;
        mainView.update();
        mainView.lpane.repaint();
    }

    public void updatePrevState() {
        prevState = currentState;
    }
}
