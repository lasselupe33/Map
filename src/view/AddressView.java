package view;

import javax.swing.*;
import java.awt.*;

/**
 * This class draws the panel that displays the information about a given street when searched for.
 */
public class AddressView extends JPanel {
    private final JLabel addressLabel = new JLabel();
    private final JLabel cityLabel = new JLabel();
    private final String fontFamily = "Myriad Pro";
    private final int topOffset = 90;
    private final int addressSize = 20;
    private final int citySize = 16;
    private final int borderSize = 20;
    private final int height = topOffset + addressSize + borderSize * 2 + citySize;

    private JPanel labelsPanel = new JPanel();
    private JPanel buttonsPanel = new JPanel();

    public AddressView() {
        // Setup view
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
        setBounds(0, 0, 450, height);

        add(renderLabels(), BorderLayout.WEST);
        add(renderButtons(), BorderLayout.EAST);
    }

    public JPanel renderLabels() {
        // Create labels container
        labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.PAGE_AXIS));
        labelsPanel.setOpaque(false);
        labelsPanel.setBorder(BorderFactory.createEmptyBorder(topOffset, borderSize, borderSize, borderSize));
        labelsPanel.setPreferredSize(new Dimension(300, height));

        // Setup address label
        addressLabel.setText("Rued Langaardsvej 7 gj egr erg erg erg erg egr  rge");
        addressLabel.setForeground(Color.decode("#383838"));
        addressLabel.setFont(new Font(fontFamily, Font.PLAIN, addressSize));
        addressLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Setup city label
        cityLabel.setText("2300 København S");
        cityLabel.setForeground(Color.decode("#383838"));
        cityLabel.setFont(new Font(fontFamily, Font.PLAIN, citySize));

        // Add to view and display!
        labelsPanel.add(addressLabel);
        labelsPanel.add(cityLabel);

        // Return the labels
        return labelsPanel;
    }

    public JPanel renderButtons() {
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(topOffset, 0, borderSize, borderSize));

        // Setup save button
        ImageIcon saveIcon = new ImageIcon("assets/icons/bookmark.png");
        JLabel save = new JLabel();
        save.setText("GEM");
        save.setForeground(Color.decode("#383838"));
        save.setIcon(saveIcon);
        save.setHorizontalAlignment(JLabel.LEFT);
        save.setVerticalAlignment(JLabel.TOP);
        save.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        buttonsPanel.add(save);

        // Setup navigation button
        ImageIcon navigationIcon = new ImageIcon("assets/icons/navigation.png");
        JLabel navigation = new JLabel();
        navigation.setText("RUTE");
        navigation.setForeground(Color.decode("#383838"));
        navigation.setIcon(navigationIcon);
        navigation.setHorizontalAlignment(JLabel.LEFT);
        navigation.setVerticalAlignment(JLabel.TOP);
        buttonsPanel.add(navigation);

        return buttonsPanel;
    }
}
