package controller;

import helpers.AddressBuilder;
import helpers.StateHandler;
import helpers.ViewStates;
import model.Address;
import model.AddressesModel;
import model.Coordinates;

import model.graph.Graph;
import view.SearchBox;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class SearchBoxController extends MouseAdapter {
    private StateHandler stateHandler;
    private AddressController addressController;
    private AddressesModel addressesModel;
    private SearchBox searchBoxView;
    private Graph graph;
    private NavigationController navigationController;
    private Address address;

    public SearchBoxController(StateHandler sc, AddressController ac, AddressesModel am, Graph g, NavigationController nc) {
        addressesModel = am;
        addressController = ac;
        stateHandler = sc;
        graph = g;
        navigationController = nc;
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
                if (stateHandler.getCurrentState() == ViewStates.INITIAL) {
                    onNavigationClick();
                } else {
                    onCloseClick();
                }
                break;
            case "favoriteButton":
                if (stateHandler.getCurrentState() == ViewStates.NAVIGATION_ACTIVE) navigationController.updateView();
                onFavoritesClick();
                break;
        }
    }

    /** Will be called once the a user has entered a search query */
    public void onSearchInput() {
        String input = searchBoxView.getSearchInput().getText();

        // Bail out if no input has been entered
        if (input.length() == 0) {
            return;
        }

        // Get matching addresses based on input
        Address inputAddress = AddressBuilder.parse(input);
        ArrayList<Address> matchingAddresses = addressesModel.getMatchingAddresses(inputAddress.toKey());



        if (matchingAddresses.size() != 0) {
            // If addresses match, then always choose the first address found
            Address address = matchingAddresses.get(0);
            updateAddress(address);
        } else {
            // ... else do nothing. AutoCompleteList will print error message
        }
    }

    /** Helper that updates the currently entered address */
    public void updateAddress(Address address) {
        this.address = address;
        // Update address
        addressController.setCurrentAddress(address);
        addressController.setBookmarkURL();

        // Go to proper position on map
        Coordinates coordinates = addressesModel.getCoordinates(address);
        MapController.getInstance().moveScreen(coordinates);

        navigationController.setStartAddress(address);


        // Update view to reflect changes
        stateHandler.updateCurrentState(ViewStates.ADDRESS_ENTERED);
    }

    /** Set search input */
    public void setSearchInput(String s){
        searchBoxView.getSearchInput().setText(s);
    }

    public void setInputOnLocationIcon(Address address) {
        setSearchInput(address.toString());
        addressController.setCurrentAddress(address);
        addressController.setBookmarkURL();
        stateHandler.updateCurrentState(ViewStates.ADDRESS_ENTERED);
    }

    /** Go to navigation view */
    public void onNavigationClick() {
        stateHandler.updateCurrentState(ViewStates.NAVIGATION_ACTIVE);
    }

    /** Helper to be called once the SearchBox cross is clicked */
    public void onCloseClick() {

        if (stateHandler.getPrevPanel() == ViewStates.ADDRESS_ENTERED && stateHandler.getCurrentState() == ViewStates.NAVIGATION_ACTIVE) {
            // If closing navigation view where previous panel was address entered...
            stateHandler.updateCurrentState(ViewStates.ADDRESS_ENTERED);
            // ... Go to proper position on map ...
            Coordinates coordinates = addressesModel.getCoordinates(address);
            MapController.getInstance().moveScreen(coordinates);
        } else {
            // ... else just go to initial state
            stateHandler.updateCurrentState(ViewStates.INITIAL);
            MapController.getInstance().deleteLocationCoordinates();
        }

        navigationController.reset();
        graph.setSourceAndDest(null, null);

        MapController.getInstance().deleteStartCoordinates();
    }

    /** Go to favorites */
    public void onFavoritesClick() {
        stateHandler.updateCurrentState(ViewStates.FAVORITES);
    }

    public SearchBox getSearchBoxView() {
        return searchBoxView;
    }
}
