package model;

import java.util.ArrayList;

public class Favorites extends ArrayList<Favorite>{

    public Favorites(){
        for(int i = 0; i < 10; i++){
            Favorite favorite = new Favorite("hjem", "ituvej 28", new Coordinates(19, 19));
            add(favorite);
        }
    }

}
