package view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SearchBox extends JPanel {
    public SearchBox() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(null);
        add(createSearchInput(), BorderLayout.WEST);
        add(createRightButton(), BorderLayout.EAST);
    }

    public JPanel createSearchInput() {
        // Setup wrapper
        JPanel searchContainer = new JPanel();
        searchContainer.setPreferredSize(new Dimension(415, 32));
        searchContainer.setLayout(new BorderLayout());
        searchContainer.setOpaque(false);
        searchContainer.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.decode("#383838")));

        // Setup margin between input and border
        Border border = searchContainer.getBorder();
        Border margin = new EmptyBorder(5, 10, 5, 10);
        searchContainer.setBorder(new CompoundBorder(border, margin));

        // Setup icon
        ImageIcon icon = new ImageIcon("assets/icons/search.png");
        JLabel iconLabel = new JLabel(icon);

        // Setup search input
        JTextField searchInput = new JTextField("Søg..", 37);
        searchInput.setFont(new Font("Myriad Pro", Font.PLAIN, 12));
        searchInput.setBorder(BorderFactory.createEmptyBorder());

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
        JPanel rightButtonContainer = new JPanel();
        rightButtonContainer.setOpaque(false);
        rightButtonContainer.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, Color.decode("#383838")));
        rightButtonContainer.setPreferredSize(new Dimension(32, 32));

        // Create button
        ImageIcon icon = new ImageIcon("assets/icons/cross.png");
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setVerticalAlignment(JLabel.CENTER);
        iconLabel.setPreferredSize(new Dimension(26, 20));
        rightButtonContainer.add(iconLabel);

        return rightButtonContainer;
    }
}

