package controller;

import java.awt.event.MouseAdapter;

public class SearchBoxController extends MouseAdapter {
    StateController stateController;

    public SearchBoxController(StateController sc) {
        stateController = sc;
    }

    /** Will be called once the a user has entered a search query */
    public void onSearchInput() {
        stateController.updateCurrentState(ViewStates.ADDRESS_ENTERED);
    }

    public void onCloseClick() {
        stateController.updateCurrentState(ViewStates.INITIAL);
    }
}
