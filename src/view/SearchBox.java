package view;

import controller.SearchBoxController;
import controller.StateController;
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
        if (!initialRender) {
            if (stateController.getPrevState() != ViewStates.NAVIGATION_ACTIVE) {
                remove(searchContainer);
            }

            remove(rightButtonContainer);
        } else {
            initialRender = false;
        }

        if (stateController.getCurrentState() != ViewStates.NAVIGATION_ACTIVE) {
            add(createSearchInput(), BorderLayout.WEST);
        }

        add(createRightButton(), BorderLayout.EAST);
    }

    public JTextField getSearchInput() {
        return searchInput;
    }

    public JPanel createSearchInput() {
        // Setup wrapper
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
        searchInput.setFont(new Font("Myriad Pro", Font.PLAIN, 12));
        searchInput.setBorder(BorderFactory.createEmptyBorder());
        searchInput.addActionListener(e -> searchBoxController.onSearchInput());

        // Add to wrapper
        searchContainer.add(searchInput, BorderLayout.WEST);
        searchContainer.add(iconLabel, BorderLayout.EAST);

        return searchContainer;
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
}

