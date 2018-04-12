package controller;

import model.Address;
import model.Coordinates;
import model.Favorite;
import model.Favorites;
import view.AddressView;
import view.FavoriteView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddressController extends MouseAdapter {
    private StateController stateController;
    private Address currAddress = new Address(0, 0);
    private Favorites favorites;
    private FavoriteView favoriteView;

    public AddressController(StateController sc, Favorites favorites)  {
        stateController = sc;
        this.favorites = favorites;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println(e.getComponent().getName());
        switch (e.getComponent().getName()) {
            case "navigation":
                goToNavigation();
                break;
            case "bookmark":
                bookmarkAdress();
                break;
        }
    }





    /** To be called as soon as the AddressView has been created in order to setup listeners */
    public void addView(AddressView av, FavoriteView favoriteView) {
        // Intercept all mouse events to ensure canvas commands won't be called when clicking on the addressView
        av.addMouseListener(this);
        av.addMouseWheelListener(this);
        av.addMouseMotionListener(this);
        this.favoriteView = favoriteView;
    }

    /** Helper that sets the currently entered address */
    public void setCurrentAddress(Address address) {
        currAddress = address;
    }

    /** To be called when the user wishes to go to the navigation view */
    public void goToNavigation() {
        stateController.updateCurrentState(ViewStates.NAVIGATION_ACTIVE);
    }

    private void bookmarkAdress() {
        //todo save real address
        Favorite test = new Favorite("hjem", getAddress(), new Coordinates(10, 10));
        favorites.add(test);
        favoriteView.updateFavoritesView();
    }

    /** Helper that returns the current address */
    public Address getAddress() {
        return currAddress;
    }
}
