package view;

import model.Favorite;

import javax.swing.*;
import java.awt.*;

public class FavoriteView extends JPanel {
    private int width = 450;

    public FavoriteView(){
        // Setup view
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(width, 400));
        setBackground(Color.WHITE);
        setOpaque(true);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
        initFavorites();

    }

    


}
