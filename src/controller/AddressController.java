package controller;

import helpers.StateHandler;
import helpers.ViewStates;
import model.Address;
import model.Favorite;
import model.FavoritesModel;
import view.AddressView;
import view.FavoriteView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class AddressController extends MouseAdapter {
    private StateHandler stateHandler;
    private Address currAddress = new Address(0, 0, 0);
    private Favorite currFavorite;
    private FavoritesModel favoritesModel;
    private FavoriteView favoriteView;
    private AddressView addressView;
    private NavigationController navigationController;
    private URL bookmarkURL = this.getClass().getResource("/icons/bookmark.png");

    public AddressController(StateHandler sc, FavoritesModel favoritesModel, NavigationController nc)  {
        stateHandler = sc;
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
                if(isFavoriteSaved()) deleteFavorite();
                else bookmarkAddress();
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
        addressView = av;
    }

    /** Helper that sets the currently entered address */
    public void setCurrentAddress(Address address) {
        currAddress = address;
    }

    /** To be called when the user wishes to go to the navigation view */
    public void goToNavigation() {
        navigationController.setStartAddress(currAddress);
        stateHandler.updateCurrentState(ViewStates.NAVIGATION_ACTIVE);
    }

    /** Ask for a name to save the address under */
    private void bookmarkAddress() {
        stateHandler.updateCurrentState(ViewStates.FAVORITES_POPUP);
    }

    /**
     * Save the address as a favorite and show it on the map
     * @param name of favorite
     */
    public void saveAddress(String name){
        Favorite newFavorite = new Favorite(name, getAddress());
        System.out.println(newFavorite.getAddress().getCoordinates());
        favoritesModel.add(newFavorite);
        favoriteView.updateFavoritesView();
    }

    /**
     * Delete the address as an favorite
     */
    public void deleteFavorite() {
        favoritesModel.remove(currFavorite);
        setBookmarkURL();
        addressView.update();
    }

    /** Get the url for the bookmark icon */
    public URL getURl() {
        return bookmarkURL;
    }

    /** Set the bookmark icon based on wether the address is saved as an address*/
    public void setBookmarkURL() {
        if (isFavoriteSaved()) bookmarkURL = this.getClass().getResource("/icons/bookmark-filled.png");
        else bookmarkURL = this.getClass().getResource("/icons/bookmark.png");
    }

    /**
     * Check wether the favorite is saved
     * @return true if saved; else false
     */
    public boolean isFavoriteSaved() {
        for (Favorite favorite : favoritesModel.getFavorites()) {
            if (favorite.getAddress().toKey().equals(currAddress.toKey())) {
                currFavorite = favorite;
                return true;
            }
        }
        return false;
    }

    /** Helper that returns the current address */
    public Address getAddress() {
        return currAddress;
    }

}
