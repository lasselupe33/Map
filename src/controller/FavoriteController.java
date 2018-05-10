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
        if(stateController.getCurrentState() == ViewStates.INITIAL || stateController.getCurrentState() == ViewStates.ADDRESS_ENTERED){
            MapController.getInstance().moveScreen(address.getCoordinates(), address.getType());
            searchBoxController.setInputOnLocationIcon(address);
        }
        if(stateController.getCurrentState() == ViewStates.NAVIGATION_ACTIVE) {
            if ((navigationController.getStartInput().getText().equals(navigationController.getStartInput().getName()) || navigationController.getStartInput().getText().equals(""))){
                navigationController.setStartAddress(address);
            }
            else if (!(navigationController.getStartInput().getText().equals(navigationController.getStartInput().getName()))){
                navigationController.setEndAddress(address);
            }

        }
    }

}
