package controller;

import model.Address;
import model.Favorite;
import model.FavoritesModel;
import view.AddressView;
import view.FavoriteView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddressController extends MouseAdapter {
    private StateController stateController;
    private Address currAddress = new Address(0, 0, 0);
    private FavoritesModel favoritesModel;
    private FavoriteView favoriteView;
    private NavigationController navigationController;

    public AddressController(StateController sc, FavoritesModel favoritesModel, NavigationController nc)  {
        stateController = sc;
        this.favoritesModel = favoritesModel;
        navigationController = nc;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
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
        navigationController.setStartAddress(currAddress);
        stateController.updateCurrentState(ViewStates.NAVIGATION_ACTIVE);
    }

    private void bookmarkAdress() {
        stateController.updateCurrentState(ViewStates.FAVORITES_POPUP);
    }

    public void saveAddress(String name){
        Favorite newFavorite = new Favorite(name, getAddress());
        System.out.println(newFavorite.getAddress().getCoordinates());
        favoritesModel.add(newFavorite);
        favoriteView.updateFavoritesView();
    }

    /** Helper that returns the current address */
    public Address getAddress() {
        return currAddress;
    }
}
