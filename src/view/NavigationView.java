package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NavigationView extends JPanel {
    private boolean initialRender = true;
    private int width = 450;

    public NavigationView() {
        // Setup view
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setOpaque(true);
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

        add(topPanel());

    }

    /**
     * Helper that renders the top panel of the navigation panel, containing the input to search for a route
     * @return
     */
    public JPanel topPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setMaximumSize(new Dimension(width, 200));
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.decode("#a7a7a7")));

        topPanel.add(navigationTypeInputs());
        topPanel.add(renderInputs());

        return topPanel;
    }

    /**
     * Helper that renders the navigationType inputs. I.e. the panel that contains the icons that specifies the chosen
     * type of travel.
     */
    public JPanel navigationTypeInputs() {
        JPanel navigationTypeContainer = new JPanel();
        navigationTypeContainer.setOpaque(false);
        navigationTypeContainer.setLayout(new GridLayout(1, 3));
        navigationTypeContainer.setBorder(new EmptyBorder(20, 150, 0, 150));

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
        inputContainer.setOpaque(false);
        inputContainer.setLayout(new BoxLayout(inputContainer, BoxLayout.Y_AXIS));
        inputContainer.setBorder(new EmptyBorder(20, 20, 20, 20));
        inputContainer.setPreferredSize(new Dimension(width, 125));

        // Start input
        JTextField startInput = new JTextField("Fra:");
        startInput.setFont(new Font("Myriad Pro", Font.PLAIN, 16));
        inputContainer.add(startInput);

        // Add margin between inputs
        inputContainer.add(Box.createVerticalStrut(10));

        // End input
        JTextField endInput = new JTextField("Til:");
        endInput.setFont(new Font("Myriad Pro", Font.PLAIN, 16));
        inputContainer.add(endInput);

        // Add margin between inputs
        inputContainer.add(Box.createVerticalStrut(10));

        inputContainer.add(renderMiddlePanel());

        return inputContainer;
    }

    /**
     * Helper that renders the middle panel that shows the time it takes to transport to the location and the submit-
     * button.
     */
    public JPanel renderMiddlePanel() {
        JPanel middlePanel = new JPanel();
        middlePanel.setOpaque(false);
        middlePanel.setLayout(new BorderLayout());

        // Time label
        JLabel timeLabel = new JLabel("<html><span style='font-size:12px;color:#383838;'>5 min</span> <span style='font-size:12px;color:#4285F4;'>(1,9 km)</span></html>");
        middlePanel.add(timeLabel, BorderLayout.WEST);


        // Submit-button
        JButton submitButton = new JButton("RUTE");
        submitButton.setForeground(Color.decode("#4285F4"));
        submitButton.setBackground(Color.WHITE);
        submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        middlePanel.add(submitButton, BorderLayout.EAST);

        return middlePanel;
    }
}
