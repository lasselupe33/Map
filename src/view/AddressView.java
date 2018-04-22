package view;

import controller.AddressController;
import model.Address;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.net.URL;

/**
 * This class draws the panel that displays the information about a given street when searched for.
 */
public class AddressView extends JPanel {
    private final JLabel addressLabel = new JLabel();
    private final JLabel cityLabel = new JLabel();
    private final String fontFamily = "Myriad Pro";
    private final int topOffset = 70;
    private final int addressSize = 20;
    private final int citySize = 16;
    private final int borderSize = 20;
    private final int height = topOffset + addressSize + borderSize * 2 + citySize;
    private AddressController addressController;
    private JPanel labelsPanel;
    private JPanel buttonsPanel;
    private boolean initialRender = true;

    public AddressView(AddressController ac) {
        addressController = ac;

        // Setup view
        setName("AddressView");
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
        setBounds(0, 0, 450, height);
    }

    /** Update function to be called when the addressView should be updated */
    public void update() {
        if (!initialRender) {
            remove(labelsPanel);
            remove(buttonsPanel);
        } else {
            initialRender = false;
        }

        add(renderLabels(), BorderLayout.WEST);
        add(renderButtons(), BorderLayout.EAST);
    }

    public JPanel renderLabels() {
        labelsPanel = new JPanel();

        // Create labels container
        labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.PAGE_AXIS));
        labelsPanel.setOpaque(false);
        labelsPanel.setBorder(BorderFactory.createEmptyBorder(topOffset, borderSize, borderSize, borderSize));
        labelsPanel.setPreferredSize(new Dimension(300, height));

        // Setup address label
        Address address = addressController.getAddress();
        addressLabel.setText(address.getStreet() + " " + address.getHouse());
        addressLabel.setForeground(Color.decode("#383838"));
        addressLabel.setFont(new Font(fontFamily, Font.PLAIN, addressSize));
        addressLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Setup city label
        cityLabel.setText(address.getPostcode() + " " + address.getCity());
        cityLabel.setForeground(Color.decode("#383838"));
        cityLabel.setFont(new Font(fontFamily, Font.PLAIN, citySize));

        // Add to view and display!
        labelsPanel.add(addressLabel);
        labelsPanel.add(cityLabel);

        // Return the labels
        return labelsPanel;
    }

    public JPanel renderButtons() {
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(topOffset, 0, borderSize, borderSize));

        // Setup save button
        URL bookmarkURL = this.getClass().getResource("/icons/bookmark.png");
        ImageIcon saveIcon = new ImageIcon(bookmarkURL);
        JLabel save = new JLabel();
        save.setText("GEM");
        save.setForeground(Color.decode("#383838"));
        save.setIcon(saveIcon);
        save.setHorizontalAlignment(JLabel.LEFT);
        save.setVerticalAlignment(JLabel.TOP);
        save.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        save.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        save.setName("bookmark");
        save.addMouseListener(addressController);
        buttonsPanel.add(save);

        // Setup navigation button
        URL navigationURL = this.getClass().getResource("/icons/navigation.png");
        ImageIcon navigationIcon = new ImageIcon(navigationURL);
        JLabel navigation = new JLabel();
        navigation.setText("RUTE");
        navigation.setForeground(Color.decode("#383838"));
        navigation.setIcon(navigationIcon);
        navigation.setHorizontalAlignment(JLabel.LEFT);
        navigation.setVerticalAlignment(JLabel.TOP);
        navigation.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        navigation.setName("navigation");
        navigation.addMouseListener(addressController);
        buttonsPanel.add(navigation);

        return buttonsPanel;
    }
}
