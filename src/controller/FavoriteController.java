package controller;

import helpers.StateHandler;
import helpers.ViewStates;
import model.Address;

/**
 * This controller is responsible for handling all input related to Favorites
 */
public class FavoriteController {
    private StateHandler stateHandler;
    private SearchBoxController searchBoxController;
    private NavigationController navigationController;

    public FavoriteController(StateHandler sc, SearchBoxController sbc, NavigationController nc){
    this.stateHandler = sc;
    this.searchBoxController = sbc;
    this.navigationController = nc;
    }

    /** Helper to be called every time the favorite input changes, i.e. when a new address should be added as a fav */
    public void updateFavoriteInput(Address address){
        // Go to the previously active panel
        stateHandler.updateCurrentState(stateHandler.getPrevPanel());

        // If we go back to either the initial or address-view, then simply display the favorited address
        if (stateHandler.getCurrentState() == ViewStates.INITIAL || stateHandler.getCurrentState() == ViewStates.ADDRESS_ENTERED){
            MapController.getInstance().moveScreen(address.getCoordinates());
            searchBoxController.setInputOnLocationIcon(address);
        }

        // Else, if we came from the navigation view, then insert the favorite into the nav-inputs
        if(stateHandler.getCurrentState() == ViewStates.NAVIGATION_ACTIVE) {

            // If input in textfield is its initial name or empty...
            if ((navigationController.getStartInput().getText().equals(navigationController.getStartInput().getName()) || navigationController.getStartInput().getText().equals(""))){
                // ... then update start address field...
                navigationController.setStartAddress(address);
            }

            // ... else update end address field
            else {
                navigationController.setEndAddress(address);
            }

        }
    }

}
