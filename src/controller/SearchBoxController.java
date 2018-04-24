package controller;

import helpers.AddressBuilder;
import model.Address;
import model.AddressesModel;
import model.Coordinates;
import model.MainModel;
import model.graph.Graph;
import model.graph.Node;
import view.SearchBox;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SearchBoxController extends MouseAdapter {
    StateController stateController;
    AddressController addressController;
    SearchBox searchBoxView;
    AddressesModel addresses;
    Graph graph;

    Node test = null;

    public SearchBoxController(MainModel m, StateController sc, AddressController ac, Graph g) {
        addresses = m.getAddresses();
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
        }
    }

    /** Will be called once the a user has entered a search query */
    public void onSearchInput() {
        String input = searchBoxView.getSearchInput().getText();
        Address address = AddressBuilder.parse(input);

        address = addresses.get(address.toKey());

        // Update current address and go to addressView if address exist
        if (addresses.contains(address)) {
            // Update address
            addressController.setCurrentAddress(address);

            // Go to proper position on map
            Coordinates coordinates = addresses.getCoordinates(address);

            // Update view to reflect changes
            stateController.updateCurrentState(ViewStates.ADDRESS_ENTERED);
        } else {
            // ... else retrieve and display list of nodes that match the input.
        }
    }

    public void onNavigationClick() {
        stateController.updateCurrentState(ViewStates.NAVIGATION_ACTIVE);
    }

    public void onCloseClick() {
        stateController.updateCurrentState(ViewStates.INITIAL);
    }
}
