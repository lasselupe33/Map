package model;

import helpers.io.DeserializeObject;
import helpers.io.SerializeObject;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class FavoritesModel {
    private ArrayList<Favorite> favorites;

    public FavoritesModel() {
        this.favorites = new ArrayList<>();
    }

    public ArrayList<Favorite> getFavorites() {
        return favorites;
    }

    public void add(Favorite fav) {
        favorites.add(fav);

        /** Save favorites every time they're altered */
        this.serialize();
    }

    public Favorite get(int index) {
        return favorites.get(index);
    }

    /** Serializes all data necessary to load and display the map */
    public void serialize() {
        new SerializeObject("favorites", favorites);
    }

    /** Internal helper that deserializses the MapModel */
    public void deserialize() {
        try {
            // Setup thread callback
            Class[] parameterTypes = new Class[2];
            parameterTypes[0] = ArrayList.class;
            parameterTypes[1] = String.class;
            Method callback = FavoritesModel.class.getMethod("onThreadDeserializeComplete", parameterTypes);

            new DeserializeObject("favorites", this, callback);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Callback to be called once a thread has finished deserializing a mapType */
    public void onThreadDeserializeComplete(ArrayList<Favorite> favorites, String name) {
        this.favorites = favorites;
    }
}
