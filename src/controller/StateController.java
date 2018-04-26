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
    private ViewStates prevPanel;

    public StateController() {
        // Setup initial state
        currentState = ViewStates.INITIAL;
        prevState = ViewStates.INITIAL;
        prevPanel = ViewStates.INITIAL;

    }

    public void addMainView(MainWindowView view) {
        mainView = view;
    }

    public ViewStates getCurrentState() {
        return currentState;
    }

    public ViewStates getPrevState() { return prevState; }

    public  ViewStates getPrevPanel() { return prevPanel; }

    public void updateCurrentState(ViewStates newState) {
        currentState = newState;
        mainView.update();
        mainView.lpane.repaint();
    }

    public void forcePrevState(ViewStates state) {
        updatePrevPanel();

        prevState = state;
    }

    public void updatePrevState() {
        updatePrevPanel();

        prevState = currentState;
    }

    public void updatePrevPanel(){
        if (prevPanel != prevState){
            prevPanel = prevState;
        }
    }
}
