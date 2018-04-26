package view;

import controller.FavoriteController;
import model.Favorite;
import model.FavoritesModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class FavoriteView extends JPanel {
    private FavoritesModel favoritesModel;
    private FavoriteController favoriteController;
    private JPanel panel;
    private JScrollPane scroll;

    public FavoriteView(FavoritesModel favoritesModel, FavoriteController favoriteController){
        this.favoritesModel = favoritesModel;
        this.favoriteController = favoriteController;
        panel = new JPanel();
        // Setup view
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setOpaque(true);
        updateFavoritesView();
        scroll = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);



    }

    public void updateBound(int height){
        int width = 450;
        scroll.setPreferredSize(new Dimension(width, height-25));
    }


    public void updateFavoritesView() {
        panel.removeAll();
        for(Favorite favorite : favoritesModel){

            addLabelToFavorites(favorite);
        }
    }

    private void addLabelToFavorites(Favorite f) {
        String text = "<html><span style=\"font-size: 12px;\">" + f.getName() +
                "</span><br><span style=\"font-size: 10px;\">"+f.getAddress()+"</span></html>";
        JLabel label = new JLabel(text);
        label.setName(f.getAddress().toString());
        label.setBorder(new EmptyBorder(10, 10, 10, 10));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(favoriteController);
        
        panel.add(label);


    }


}
