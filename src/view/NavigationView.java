package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NavigationView extends JPanel {
    private boolean initialRender = true;

    public NavigationView() {
        // Setup view
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));

        update();
    }

    /**
     * Helper that updates the state of the navigation view
     */
    public void update() {
        if (!initialRender) {

        } else {
            initialRender = false;
        }

        add(navigationTypeInputs());
        add(renderInputs());
    }

    public JPanel navigationTypeInputs() {
        JPanel navigationTypeContainer = new JPanel();
        navigationTypeContainer.setOpaque(false);
        navigationTypeContainer.setLayout(new GridLayout(1, 3));
        navigationTypeContainer.setBorder(new EmptyBorder(52, 150, 0, 150));
        navigationTypeContainer.setPreferredSize(new Dimension(getWidth(), 150));

        // Setup Car button
        ImageIcon carIcon = new ImageIcon("assets/icons/car.png");
        JLabel car = new JLabel();
        car.setIcon(carIcon);
        car.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        car.setName("bookmark");
        car.setHorizontalAlignment(SwingConstants.CENTER);
        navigationTypeContainer.add(car);

        // Setup cycle button
        ImageIcon cycleIcon = new ImageIcon("assets/icons/cycle.png");
        JLabel cycle = new JLabel();
        cycle.setIcon(cycleIcon);
        cycle.setHorizontalAlignment(SwingConstants.CENTER);
        cycle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cycle.setName("cycle");
        navigationTypeContainer.add(cycle);

        // Setup pedestrian button
        ImageIcon pedestrianIcon = new ImageIcon("assets/icons/pedestrian.png");
        JLabel pedestrian = new JLabel();
        pedestrian.setIcon(pedestrianIcon);
        pedestrian.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        pedestrian.setHorizontalAlignment(SwingConstants.CENTER);
        pedestrian.setName("pedestrian");
        navigationTypeContainer.add(pedestrian);

        return navigationTypeContainer;
    }

    public JPanel renderInputs() {
        JPanel inputContainer = new JPanel();
        inputContainer.setOpaque(true);
        inputContainer.setLayout(new GridLayout(3, 1));

        // Start input
        JTextField startInput = new JTextField("Søg..");
        startInput.setPreferredSize(new Dimension(inputContainer.getWidth(), 30));
        startInput.setFont(new Font("Myriad Pro", Font.PLAIN, 12));
        inputContainer.add(startInput);

        // End input
        JTextField endInput = new JTextField("Søg..");
        endInput.setPreferredSize(new Dimension(inputContainer.getWidth(), 30));
        endInput.setFont(new Font("Myriad Pro", Font.PLAIN, 12));
        inputContainer.add(endInput);

        // Submit-button
        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(Color.WHITE);
        inputContainer.add(submitButton);

        return inputContainer;
    }
}
