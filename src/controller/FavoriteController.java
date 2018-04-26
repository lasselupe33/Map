package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FavoriteController extends MouseAdapter{
    StateController stateController;
    SearchBoxController searchBoxController;
    NavigationController navigationController;


    public FavoriteController(StateController sc, SearchBoxController sbc, NavigationController nc){
    this.stateController = sc;
    this.searchBoxController = sbc;
    this.navigationController = nc;

    }
    public void mouseClicked(MouseEvent e){
        String address = e.getComponent().getName();
        stateController.updateCurrentState(stateController.getPrevPanel());
        if(stateController.getCurrentState() == ViewStates.INITIAL){
            searchBoxController.setSearchInput("search");
        }
        if(stateController.getCurrentState() == ViewStates.NAVIGATION_ACTIVE){
            navigationController.setStartInput("navigation");
        }
    }

}
