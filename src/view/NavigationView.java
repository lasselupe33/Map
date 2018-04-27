package view;

import controller.NavigationController;
import controller.StateController;
import controller.TextController;
import model.graph.VehicleType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseListener;
import java.net.URL;

public class NavigationView extends JPanel {
    private boolean initialRender = true;
    private int width = 450;
    private JTextField startInput;
    private JTextField endInput;
    private StateController stateController;
    private NavigationController navigationController;
    private JPanel topPanel;
    private JPanel navigationTypeContainer;
    private String startInputText = "Fra:";
    private String endInputText = "Til:";

    public NavigationView(StateController stateController, NavigationController nc) {
        // Setup view
        this.stateController = stateController;
        navigationController = nc;
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
            remove(topPanel);
        } else {
            initialRender = false;
        }
        add(topPanel());
        revalidate();
        repaint();
    }

    /**
     * Helper that renders the top panel of the navigation panel, containing the input to search for a route
     * @return
     */
    public JPanel topPanel() {
        topPanel = new JPanel();
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
        navigationTypeContainer = new JPanel();
        navigationTypeContainer.setOpaque(false);
        navigationTypeContainer.setLayout(new GridLayout(1, 3));
        navigationTypeContainer.setBorder(new EmptyBorder(20, 150, 0, 150));

        // Setup Car button
        URL carURL;
        if(navigationController.getVehicleType() == VehicleType.CAR) {
            carURL = this.getClass().getResource("/icons/car-blue.png");
        } else {
            carURL = this.getClass().getResource("/icons/car.png");
        }
        ImageIcon carIcon = new ImageIcon(carURL);
        JLabel car = new JLabel();
        car.setIcon(carIcon);
        car.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        car.setName("car");
        car.setHorizontalAlignment(SwingConstants.CENTER);
        car.addMouseListener(navigationController);
        navigationTypeContainer.add(car);

        // Setup cycle button
        URL cycleURL;
        if(navigationController.getVehicleType() == VehicleType.BICYCLE) {
            cycleURL = this.getClass().getResource("/icons/cycle-blue.png");
        } else {
            cycleURL = this.getClass().getResource("/icons/cycle.png");
        }
        ImageIcon cycleIcon = new ImageIcon(cycleURL);
        JLabel cycle = new JLabel();
        cycle.setIcon(cycleIcon);
        cycle.setHorizontalAlignment(SwingConstants.CENTER);
        cycle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cycle.setName("cycle");
        cycle.addMouseListener(navigationController);
        navigationTypeContainer.add(cycle);

        // Setup pedestrian button
        URL pedestrianURL;
        if(navigationController.getVehicleType() == VehicleType.PEDESTRIAN) {
            pedestrianURL = this.getClass().getResource("/icons/pedestrian-blue.png");
        } else {
            pedestrianURL = this.getClass().getResource("/icons/pedestrian.png");
        }
        ImageIcon pedestrianIcon = new ImageIcon(pedestrianURL);
        JLabel pedestrian = new JLabel();
        pedestrian.setIcon(pedestrianIcon);
        pedestrian.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        pedestrian.setHorizontalAlignment(SwingConstants.CENTER);
        pedestrian.setName("pedestrian");
        pedestrian.addMouseListener(navigationController);
        navigationTypeContainer.add(pedestrian);

        return navigationTypeContainer;
    }

    public JPanel getNavigationTypeContainer() {
        return navigationTypeContainer;
    }

    public JPanel renderInputs() {
        JPanel inputContainer = new JPanel();
        inputContainer.setOpaque(false);
        inputContainer.setLayout(new BoxLayout(inputContainer, BoxLayout.Y_AXIS));
        inputContainer.setBorder(new EmptyBorder(20, 20, 20, 20));
        inputContainer.setPreferredSize(new Dimension(width, 125));

        // Start input
        startInput = new JTextField(startInputText);
        //startInput.setText(startInputText);
        startInput.setName(startInput.getText());
        startInput.setFont(new Font("Myriad Pro", Font.PLAIN, 16));
        startInput.addFocusListener(new TextController());
        inputContainer.add(startInput);

        // Add margin between inputs
        inputContainer.add(Box.createVerticalStrut(10));

        // End input
        endInput = new JTextField(endInputText);
        //endInput.setText(endInputText);
        endInput.setName(endInput.getText());
        endInput.setFont(new Font("Myriad Pro", Font.PLAIN, 16));
        endInput.addFocusListener(new TextController());
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

        middlePanel.add(renderSwitchAndSubmitButtons(), BorderLayout.EAST);

        return middlePanel;
    }

    private JPanel renderSwitchAndSubmitButtons(){
        JPanel switchAndSubmitPanel = new JPanel();
        switchAndSubmitPanel.setOpaque(false);
        switchAndSubmitPanel.setLayout(new BorderLayout());

        //SwitchFromTo button
        URL switchURL = this.getClass().getResource("/icons/arrow.jpg");
        ImageIcon switchIcon = new ImageIcon(switchURL);
        JButton switchFromTo = new JButton(switchIcon);
        //switchFromTo.setForeground(Color.decode("#4285F4"));
        switchFromTo.setBackground(Color.WHITE);
        switchFromTo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        switchFromTo.addActionListener((e) -> switchFromAndTo());
        switchAndSubmitPanel.add(switchFromTo, BorderLayout.WEST);

        // Submit-button
        JButton submitButton = new JButton("RUTE");
        submitButton.setForeground(Color.decode("#4285F4"));
        submitButton.setBackground(Color.WHITE);
        submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        submitButton.addActionListener((e) -> navigationController.onRouteSearch());
        switchAndSubmitPanel.add(submitButton, BorderLayout.EAST);


        return switchAndSubmitPanel;
    }

    private void switchFromAndTo() {
        String startTextHolder = startInput.getText();
        String endTextHolder = endInput.getText();
        if(startTextHolder.equals("Fra:") && endTextHolder.equals("Til:")){
            //nothing happens
        } else if (startTextHolder.equals("Fra:")){
            startInput.setText(endTextHolder);
            endInput.setText("Til:");
        } else if (endTextHolder.equals("Til:")){
            endInput.setText(startTextHolder);
            startInput.setText("Fra:");
        } else {
            startInput.setText(endTextHolder);
            endInput.setText(startTextHolder);
        }
    }

    public JTextField getStartInput() {
        return startInput;
    }
    public JTextField getEndInput() {
        return endInput;
    }
    public void setStartInputText(String text) { startInputText = text; }
    public void setEndInputText(String text) { endInputText = text; }
    public void setDefault() {
        startInputText = "Fra:";
        endInputText = "Til:";
        navigationController.setVehicleType(VehicleType.CAR);
        update();
    }
}
