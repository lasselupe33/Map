package controller;

import helpers.AddressBuilder;
import model.Address;
import model.AddressesModel;
import model.Coordinates;
import model.MetaModel;
import view.SearchBox;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SearchBoxController extends MouseAdapter {
    StateController stateController;
    AddressController addressController;
    AddressesModel addressesModel;
    SearchBox searchBoxView;

    public SearchBoxController(MetaModel m, StateController sc, AddressController ac, AddressesModel am) {
        addressesModel = am;
        addressController = ac;
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
            case "favoriteButton":
                onFavoritesClick();
                break;
        }
    }

    /** Will be called once the a user has entered a search query */
    public void onSearchInput() {
        String input = searchBoxView.getSearchInput().getText();
        Address address = AddressBuilder.parse(input);

        // Update current address and go to addressView if address exist
        if (addressesModel.contains(address)) {
            // Update address
            addressController.setCurrentAddress(address);

            // Go to proper position on map
            Coordinates coordinates = addressesModel.getCoordinates(address);
            MapController.getInstance().moveScreen(coordinates, addressesModel.getType(address));

            // Update view to reflect changes
            stateController.updateCurrentState(ViewStates.ADDRESS_ENTERED);
        } else {
            // ... else retrieve and display list of nodes that match the input.
        }
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
