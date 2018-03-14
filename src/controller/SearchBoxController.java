package controller;

import view.SearchBox;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SearchBoxController extends MouseAdapter {
    StateController stateController;
    SearchBox searchBoxView;

    public SearchBoxController(StateController sc) {
        stateController = sc;
    }

    public void addView(SearchBox view) {
        searchBoxView = view;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch(e.getComponent().getName()) {
            case "search":
                onSearchInput();
                break;

            case "rightButton":
                if (stateController.getCurrentState() == ViewStates.INITIAL) {
                    onNavigationClick();
                } else {
                    onCloseClick();
                }
                break;
        }
    }

    /** Will be called once the a user has entered a search query */
    public void onSearchInput() {
        System.out.println(searchBoxView.getSearchInput().getText());
        stateController.updateCurrentState(ViewStates.ADDRESS_ENTERED);
    }

    public void onNavigationClick() {
        stateController.updateCurrentState(ViewStates.NAVIGATION_ACTIVE);
    }

    public void onCloseClick() {
        stateController.updateCurrentState(ViewStates.INITIAL);
    }
}
