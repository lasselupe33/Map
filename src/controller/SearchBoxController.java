package controller;

import helpers.AddressBuilder;
import model.Address;
import model.AddressesModel;
import model.Coordinates;

import model.graph.Graph;
import model.graph.Node;
import model.MetaModel;
import view.SearchBox;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class SearchBoxController extends MouseAdapter {
    StateController stateController;
    AddressController addressController;
    AddressesModel addressesModel;
    SearchBox searchBoxView;
    AddressesModel addresses;
    Graph graph;


    public SearchBoxController(MetaModel m, StateController sc, AddressController ac, AddressesModel am, Graph g) {
        addressesModel = am;
        addressController = ac;
        stateController = sc;
        graph = g;
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
        // Update address
        addressController.setCurrentAddress(address);

        // Go to proper position on map
        Coordinates coordinates = addressesModel.getCoordinates(address);

        // Update view to reflect changes
        stateController.updateCurrentState(ViewStates.ADDRESS_ENTERED);
    }

    public void setSearchInput(String s){
        searchBoxView.getSearchInput().setText(s);
    }

    public String getSearchInput() {return searchBoxView.getSearchInput().getText();}

    public void onNavigationClick() {
        stateController.updateCurrentState(ViewStates.NAVIGATION_ACTIVE);
    }

    public void onCloseClick() {
        stateController.updateCurrentState(ViewStates.INITIAL);
    }

    public void onFavoritesClick() {
        stateController.updateCurrentState(ViewStates.FAVORITES);
    }
}
