package helpers;

import controller.AutoCompleteController;
import view.MainWindowView;

/**
 * Handler that takes care of handling the current state of the application, i.e. which view should currently
 * be visible.
 * This will often be called from other controllers.
 */
public class StateHandler {
    private ViewStates currentState;
    private MainWindowView mainView;
    private ViewStates prevState;
    private ViewStates prevPanel;
    private AutoCompleteController autoCompleteController;

    public StateHandler() {
        // Setup initial state
        currentState = ViewStates.INITIAL;
        prevState = ViewStates.INITIAL;
        prevPanel = ViewStates.INITIAL;
    }

    public void addDependencies(MainWindowView view, AutoCompleteController acl) {
        mainView = view;
        this.autoCompleteController = acl;
    }

    /**
     * Return the current state
     * @return current state
     */
    public ViewStates getCurrentState() {
        return currentState;
    }

    /**
     * Return the previous view state
     * @return prev state
     */
    public ViewStates getPrevState() { return prevState; }

    /**
     * Return previous panel
     * @return panel
     */
    public  ViewStates getPrevPanel() { return prevPanel; }

    /**
     * Update the current state
     * @param newState the state to update to
     */
    public void updateCurrentState(ViewStates newState) {
        // AutoCompleteList will always be reset on state change
        autoCompleteController.reset();

        currentState = newState;
        mainView.update();
        mainView.getlpane().repaint();
    }

    /**
     * For the previous state to be something specific
     * @param state the state the previous state is forced to be
     */
    public void forcePrevState(ViewStates state) {
        updatePrevPanel();

        prevState = state;
    }

    /**
     * Update the previous state
     */
    public void updatePrevState() {
        updatePrevPanel();

        prevState = currentState;
    }

    /**
     * Update the previous panel
     */
    public void updatePrevPanel(){
        if (prevPanel != prevState){
            prevPanel = prevState;
        }
    }
}
