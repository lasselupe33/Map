package controller;

import view.MainWindowView;

public class StateController {
    private ViewStates currentState;
    private MainWindowView mainView;

    public StateController() {
        // Setup initial state
        currentState = ViewStates.INITIAL;
    }

    public void addMainView(MainWindowView view) {
        mainView = view;
    }

    public ViewStates getCurrentState() {
        return currentState;
    }

    public void updateCurrentState(ViewStates newState) {
        currentState = newState;
        mainView.update();
        mainView.lpane.repaint();
    }
}
