package controller;

import helpers.AddressBuilder;
import model.Address;
import model.AddressesModel;
import view.AutoCompleteList;
import view.NavigationView;
import view.SearchBox;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class AutoCompleteController extends KeyAdapter {
    private AutoCompleteList list;
    private SearchBoxController searchBoxController;
    private NavigationController navigationController;
    private AddressesModel addresses;
    private StateController stateController;
    private String currentInput;

    ArrayList<Address> matchingAddresses = new ArrayList<>();

    public AutoCompleteController(StateController sc, NavigationController nc) {
        stateController = sc;
        navigationController = nc;
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
            switch (currentInput) {
                case "searchBox":
                    searchBoxController.updateAddress(address);
                    break;

                case "Fra:":
                    navigationController.setStartAddress(address);
                    break;

                case "Til:":
                    navigationController.setEndAddress(address);
                    break;
            }

            list.setVisibility(false);
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
                currentInput = "searchBox";
                list.setBounds(20, 52, 477, 160);
                text = searchBoxController.getSearchBoxView().getSearchInput().getText();
                break;

            case NAVIGATION_ACTIVE:
                JTextField source = (JTextField) e.getSource();
                text = source.getText();
                currentInput = source.getName();

                if (currentInput.equals("Fra:")) {
                    list.setBounds(20, 92, 409, 160);
                } else {
                    // Only the endInput will be reached here.
                    list.setBounds(20, 130, 409, 160);
                }
                break;
        }

        if (text.length() < 3) {
            list.setVisibility(false);
        } else {
            list.setVisibility(true);
            Address prefixAddress = AddressBuilder.parse(text);
            matchingAddresses = addresses.getMatchingAddresses(prefixAddress.toKey());
        }

        list.update();
    }
}
