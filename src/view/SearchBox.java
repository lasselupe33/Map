package view;

import javax.swing.*;
import java.awt.*;

public class SearchBox extends JPanel {
    private JTextField searchInput;
    private JButton enterSearch;
    private JButton toggleRute;


    public SearchBox() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));

        createSearchInput();
        createButtons();
        add(searchInput);
        add(enterSearch);
        add(toggleRute);



    }

    public void createSearchInput(){
        searchInput = new JTextField("Søg..", 30);
        searchInput.setFont(new Font("Myriad Pro", Font.PLAIN, 12));


    }
    public void createButtons(){
        enterSearch = new JButton("S");
        toggleRute = new JButton("R");
    }


}

