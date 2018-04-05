package view;

import controller.SearchBoxController;
import controller.StateController;
import controller.TextController;
import controller.ViewStates;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;

public class SearchBox extends JPanel {
    private StateController stateController;
    private SearchBoxController searchBoxController;
    private JTextField searchInput;
    private JPanel searchContainer;
    private JPanel rightButtonContainer;
    private JPanel favoriteButtonContainer;
    private JPanel buttonsContainer;
    private boolean initialRender = true;

    public SearchBox(StateController sc, SearchBoxController sbc) {
        stateController = sc;
        searchBoxController = sbc;
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(null);
    }

    /**
     * Helper function that updates the component
     */
    public void update() {
        // Remove components if necessary
        if (!initialRender) {
            if (stateController.getPrevState() != ViewStates.NAVIGATION_ACTIVE) {
                remove(searchContainer);
            }
            remove(buttonsContainer);
        } else {
            initialRender = false;
        }

        // Add required components and update bounds
        if (stateController.getCurrentState() != ViewStates.NAVIGATION_ACTIVE) {
            add(createSearchInput(), BorderLayout.WEST);
            setBounds(20, 20, 477, 32);
        } else {
            setBounds(433, 20, 64, 32);
        }

        add(createButtons(), BorderLayout.EAST);

    }

    public JTextField getSearchInput() {
        return searchInput;
    }

    /**
     * Helper that creates the searchInput alongside required buttons.
     */
    public JPanel createSearchInput() {
        // Setup wrapper`
        searchContainer = new JPanel();
        searchContainer.setPreferredSize(new Dimension(415, 32));
        searchContainer.setLayout(new BorderLayout());
        searchContainer.setOpaque(false);
        searchContainer.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 0, Color.decode("#383838")));

        // Setup margin between input and border
        Border border = searchContainer.getBorder();
        Border margin = new EmptyBorder(5, 10, 5, 10);
        searchContainer.setBorder(new CompoundBorder(border, margin));

        // Setup icon
        ImageIcon icon = new ImageIcon("assets/icons/search.png");
        JLabel iconLabel = new JLabel(icon);
        iconLabel.addMouseListener(searchBoxController);
        iconLabel.setName("search");
        iconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Setup search input
        searchInput = new JTextField("Søg..");
        searchInput.setPreferredSize(new Dimension(360, searchInput.getHeight()));
        searchInput.setFont(new Font("Myriad Pro", Font.PLAIN, 14));
        searchInput.setBorder(BorderFactory.createEmptyBorder());
        searchInput.addActionListener(e -> searchBoxController.onSearchInput());
        searchInput.addFocusListener(new TextController());

        // Add to wrapper
        searchContainer.add(searchInput, BorderLayout.WEST);
        searchContainer.add(iconLabel, BorderLayout.EAST);

        return searchContainer;
    }

    public JPanel createButtons() {
     buttonsContainer = new JPanel();
     buttonsContainer.setOpaque(false);
     buttonsContainer.setLayout(new BorderLayout());
     buttonsContainer.setPreferredSize(new Dimension(64, 60));
     buttonsContainer.add(createRightButton(), BorderLayout.WEST);
     buttonsContainer.add(createFavoriteButton(), BorderLayout.EAST);

     return buttonsContainer;
    }

    /**
     * Creates the wrapper that contains the righthand-side button
     */
    public JPanel createRightButton() {
        // Setup wrapper panel
        rightButtonContainer = new JPanel();
        rightButtonContainer.setOpaque(false);
        rightButtonContainer.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.decode("#383838")));
        rightButtonContainer.setPreferredSize(new Dimension(32, 32));

        // Create button
        String imageURL;

        if (stateController.getCurrentState() == ViewStates.INITIAL) {
            imageURL = "assets/icons/navigation.png";
        } else {
            imageURL = "assets/icons/cross.png";
        }

        ImageIcon icon = new ImageIcon(imageURL);
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setVerticalAlignment(JLabel.CENTER);
        iconLabel.setPreferredSize(new Dimension(26, 20));
        iconLabel.addMouseListener(searchBoxController);
        iconLabel.setName("rightButton");
        iconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        rightButtonContainer.add(iconLabel);

        return rightButtonContainer;
    }
    public JPanel createFavoriteButton() {
        // Setup wrapper panel
        favoriteButtonContainer = new JPanel();
        favoriteButtonContainer.setOpaque(false);
        favoriteButtonContainer.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.decode("#383838")));
        favoriteButtonContainer.setPreferredSize(new Dimension(32, 32));

        // Create button
        String imageURL = "assets/icons/favorite.png";


        ImageIcon icon = new ImageIcon(imageURL);
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setVerticalAlignment(JLabel.CENTER);
        iconLabel.setPreferredSize(new Dimension(26, 20));
        iconLabel.addMouseListener(searchBoxController);
        iconLabel.setName("favoriteButton");
        iconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        favoriteButtonContainer.add(iconLabel);

        return favoriteButtonContainer;
    }


}

