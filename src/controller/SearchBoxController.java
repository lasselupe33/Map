package controller;

import helpers.AddressBuilder;
import model.Address;
import model.AddressesModel;
import model.Coordinates;

import model.graph.Graph;
import model.graph.Node;
import model.MetaModel;
import model.*;
import view.SearchBox;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class SearchBoxController extends MouseAdapter {
    private StateController stateController;
    private AddressController addressController;
    private AddressesModel addressesModel;
    private SearchBox searchBoxView;
    private Graph graph;
    private NavigationController navigationController;
    private Address address;

    public SearchBoxController(StateController sc, AddressController ac, AddressesModel am, Graph g, NavigationController nc) {
        addressesModel = am;
        addressController = ac;
        stateController = sc;
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
                if (stateController.getCurrentState() == ViewStates.INITIAL) {
                    onNavigationClick();
                } else {
                    onCloseClick();
                }
                break;
            case "favoriteButton":
                if (stateController.getCurrentState() == ViewStates.NAVIGATION_ACTIVE) navigationController.updateView();
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
        addressController.isFavoriteSaved();

        // Go to proper position on map
        Coordinates coordinates = addressesModel.getCoordinates(address);
        WayType type = addressesModel.getType(address);
        MapController.getInstance().moveScreen(coordinates, type);

        navigationController.setStartAddress(address);

        // Update view to reflect changes
        stateController.updateCurrentState(ViewStates.ADDRESS_ENTERED);
    }

    public void setSearchInput(String s){
        searchBoxView.getSearchInput().setText(s);
    }

    public void setInputOnLocationIcon(Address address) {
        setSearchInput(address.toString());
        addressController.setCurrentAddress(address);
        addressController.isFavoriteSaved();
        stateController.updateCurrentState(ViewStates.ADDRESS_ENTERED);
    }

    public String getSearchInput() { return searchBoxView.getSearchInput().getText(); }

    public void onNavigationClick() {
        stateController.updateCurrentState(ViewStates.NAVIGATION_ACTIVE);
    }

    public void onCloseClick() {
        if (stateController.getPrevPanel() == ViewStates.ADDRESS_ENTERED && stateController.getCurrentState() == ViewStates.NAVIGATION_ACTIVE) {
            stateController.updateCurrentState(ViewStates.ADDRESS_ENTERED);
            // Go to proper position on map
            Coordinates coordinates = addressesModel.getCoordinates(address);
            WayType type = addressesModel.getType(address);
            MapController.getInstance().moveScreen(coordinates, type);
        } else {
            stateController.updateCurrentState(ViewStates.INITIAL);
            MapController.deleteLocationCoordinates();
        }
        navigationController.reset();
        graph.setSourceAndDest(null, null);
        MapController.deleteStartCoordinates();
    }

    public void onFavoritesClick() {
        stateController.updateCurrentState(ViewStates.FAVORITES);
    }

    public SearchBox getSearchBoxView() {
        return searchBoxView;
    }
}
