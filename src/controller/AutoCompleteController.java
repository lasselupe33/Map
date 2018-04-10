package controller;

import helpers.AddressBuilder;
import model.Address;
import model.AddressesModel;
import view.AutoCompleteList;
import view.SearchBox;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class AutoCompleteController extends KeyAdapter {
    AutoCompleteList list;
    SearchBox searchBox;
    AddressesModel addresses;

    ArrayList<String> matchingAddresses = new ArrayList<>();

    public AutoCompleteController() {}

    /** Helper to add dependencies once they have been created */
    public void addDependencies(AutoCompleteList list, SearchBox search, AddressesModel a) {
        this.list = list;
        this.searchBox = search;
        this.addresses = a;
    }

    /** Helper that returns the addresses that matches the current input */
    public ArrayList<String> getMatchingAddresses() {
        return matchingAddresses;
    }

    /** Function to be called every time the user inputs text into the searchBox */
    @Override
    public void keyReleased(KeyEvent e) {
        String text = searchBox.getSearchInput().getText();

        // Wait until at least three characters have been entered before showing recommendations
        if (text.length() > 3) {
            Address prefixAddress = AddressBuilder.parse(text);
            matchingAddresses = addresses.getMatchingAddresses(prefixAddress.toKey());
        } else {
            matchingAddresses = new ArrayList<>();
        }

        list.update();
    }
}
