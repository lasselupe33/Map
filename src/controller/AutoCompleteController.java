package controller;

import helpers.AddressBuilder;
import model.Address;
import model.AddressesModel;
import view.AutoCompleteList;
import view.SearchBox;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class AutoCompleteController extends KeyAdapter {
    AutoCompleteList list;
    SearchBoxController searchBoxController;
    AddressesModel addresses;
    StateController stateController;

    ArrayList<Address> matchingAddresses = new ArrayList<>();

    public AutoCompleteController(StateController sc) {
        stateController = sc;
    }

    /** Helper to add dependencies once they have been created */
    public void addDependencies(AutoCompleteList list, SearchBoxController search, AddressesModel a) {
        this.list = list;
        this.searchBoxController = search;
        this.addresses = a;
    }

    /** Helper to be called once a listElement has been clicked */
    public void onListClick(MouseEvent e) {
        if (e.getClickCount() == 2) {
            // On double click we want to do something based on the current viewstate
            int index = list.getList().locationToIndex(e.getPoint());
            Address address = matchingAddresses.get(index);

            // Based on where we currently are, act accordingly
            switch (stateController.getCurrentState()) {
                case INITIAL:
                case ADDRESS_ENTERED:
                    searchBoxController.updateAddress(address);
                    break;
            }
        }
    }

    /** Helper that returns the addresses that matches the current input */
    public ArrayList<Address> getMatchingAddresses() {
        return matchingAddresses;
    }

    /** Helper that reset the AutoCompleteController */
    public void reset() {
        this.matchingAddresses = new ArrayList<>();
        list.setVisibility(false);
    }

    /** Function to be called every time the user inputs text into the searchBox */
    @Override
    public void keyReleased(KeyEvent e) {
        String text = null;

        switch (stateController.getCurrentState()) {
            case INITIAL:
            case ADDRESS_ENTERED:
                list.setBounds(20, 52, 477, 160);
                text = searchBoxController.searchBoxView.getSearchInput().getText();
                break;
        }

        if (text.length() == 0) {
            list.setVisibility(false);
        } else {
            list.setVisibility(true);
            Address prefixAddress = AddressBuilder.parse(text);
            matchingAddresses = addresses.getMatchingAddresses(prefixAddress.toKey());
        }

        list.update();
    }
}
