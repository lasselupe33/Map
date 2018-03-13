package view;

import javax.swing.*;
import java.awt.*;

/**
 * This class draws the panel that displays the information about a given street when searched for.
 */
public class AddressView extends JPanel {
    JLabel addressLabel = new JLabel();
    JLabel cityLabel = new JLabel();

    public AddressView() {
        setLayout(new );
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));

        addressLabel.setText("Rued Langaardsvej 7");
        cityLabel.setText("2300 København S");

        addressLabel.setFont(new Font("Myriad Pro", Font.PLAIN, 12));

        add(addressLabel);
        add(cityLabel);
    }
}
