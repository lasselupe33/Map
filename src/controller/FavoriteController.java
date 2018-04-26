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
    public void updateFavoriteInput(Address address){
        stateController.updateCurrentState(stateController.getPrevPanel());
        if(stateController.getCurrentState() == ViewStates.INITIAL){
            searchBoxController.setSearchInput(address.toString());
            MapController.getInstance().moveScreen(address.getCoordinates(), address.getType());

        }
        if(stateController.getCurrentState() == ViewStates.NAVIGATION_ACTIVE){
            navigationController.setStartInput(address.toString());
        }
    }

}
