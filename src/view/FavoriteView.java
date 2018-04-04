package view;

import model.Favorite;
import model.Favorites;

import javax.swing.*;
import java.awt.*;

public class FavoriteView extends JPanel {
    private int width = 450;
    private Favorites favorites;

    public FavoriteView(Favorites favorites){
        this.favorites = favorites;
        // Setup view
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setOpaque(true);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
        initFavorites();

    }

    private void initFavorites() {
        for(Favorite f : favorites){
            addLabelToFavorites(f);
        }
    }

    private void addLabelToFavorites(Favorite f) {
        JLabel label = new JLabel(f.getName());
        
        add(label);


    }


}
