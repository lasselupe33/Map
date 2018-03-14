package controller;

import view.MainWindowView;

public class StateController {
    private ViewStates currentState;
    private MainWindowView mainView;
    private ViewStates prevState;

    public StateController() {
        // Setup initial state
        currentState = ViewStates.NAVIGATION_ACTIVE;
    }

    public void addMainView(MainWindowView view) {
        mainView = view;
    }

    public ViewStates getCurrentState() {
        return currentState;
    }

    public ViewStates getPrevState() { return prevState; }

    public void updateCurrentState(ViewStates newState) {
        prevState = currentState;
        currentState = newState;
        mainView.update();
        mainView.lpane.repaint();
    }

}
