package view;

import controller.FavoriteController;
import model.Favorite;
import model.FavoritesModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class FavoriteView extends JPanel {
    private int width = 450;
    private FavoritesModel favoritesModel;
    private FavoriteController favoriteController;

    public FavoriteView(FavoritesModel favoritesModel, FavoriteController favoriteController){
        this.favoritesModel = favoritesModel;
        this.favoriteController = favoriteController;
        // Setup view
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setOpaque(true);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
        updateFavoritesView();

    }


    public void updateFavoritesView() {
        removeAll();
        for(Favorite favorite : favoritesModel){
            addLabelToFavorites(favorite);
        }
    }

    private void addLabelToFavorites(Favorite f) {
        String text = "<html><span style=\"font-size: 10px;\">" + f.getName() +
                "</span><br><span style=\"font-size: 5px;\">"+f.getAddress()+"</span></html>";
        JLabel label = new JLabel(text);
        label.setName(f.getAddress().toString());
        label.setBorder(new EmptyBorder(10, 10, 10, 10));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(favoriteController);
        
        add(label);


    }


}
