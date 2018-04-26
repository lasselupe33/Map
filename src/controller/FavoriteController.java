package controller;

import model.Address;


public class FavoriteController {
    StateController stateController;
    SearchBoxController searchBoxController;
    NavigationController navigationController;


    public FavoriteController(StateController sc, SearchBoxController sbc, NavigationController nc){
    this.stateController = sc;
    this.searchBoxController = sbc;
    this.navigationController = nc;

    }
    public void mouseClicked(Address address){
        stateController.updateCurrentState(stateController.getPrevPanel());
        if(stateController.getCurrentState() == ViewStates.INITIAL){
            searchBoxController.setSearchInput("search");
        }
        if(stateController.getCurrentState() == ViewStates.NAVIGATION_ACTIVE){
            navigationController.setStartInput("navigation");
        }
    }

}
