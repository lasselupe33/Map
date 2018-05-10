package view;

import controller.*;
import helpers.StateHandler;
import helpers.ViewStates;
import model.Address;
import model.graph.RouteType;
import model.graph.TextualElement;
import model.graph.VehicleType;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;

/**
 * View for navigation search
 */
public class NavigationView extends JPanel {
    private boolean initialRender = true;
    private int width = 450;
    private JTextField startInput;
    private JTextField endInput;
    private NavigationController navigationController;
    private JPanel topPanel;
    private JPanel navigationTypeContainer;
    private String startInputText = "Fra:";
    private String endInputText = "Til:";
    private AutoCompleteController autoCompleteController;
    private StateHandler stateHandler;
    private JPanel bottomPanel;
    private JScrollPane scroll;

    public NavigationView(NavigationController nc, AutoCompleteController acc, StateHandler sc) {
        navigationController = nc;
        autoCompleteController = acc;
        stateHandler = sc;

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
            remove(topPanel);

            if (scroll != null) {
                bottomPanel.removeAll();
                bottomPanel.revalidate();
                remove(scroll);
            }
        } else {
            initialRender = false;
        }

        add(topPanel());

        // Set correct dimensions based on whether or not a navigation is active
        if (navigationController.isNavigationActive() && stateHandler.getCurrentState() == ViewStates.NAVIGATION_ACTIVE) {
            int height = MainWindowView.getHeight();
            setBounds(0, 0, 450, height-25);
            add(textualNavigation());
        } else {
            setBounds(0, 0, 450, 200);
        }

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

        if (navigationController.isNavigationActive()) {
            topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#a7a7a7")));
        } else {
            topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#000000")));
        }

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
        navigationTypeContainer.setLayout(new GridLayout(1, 7));
        navigationTypeContainer.setBorder(new EmptyBorder(20, 25, 0, 25));

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

        // Create spacing between vehicleType and routeType by inserting an empty label into the grid
        JLabel spacing = new JLabel();
        navigationTypeContainer.add(spacing);
        navigationTypeContainer.add(spacing);

        //RouteType buttons
        // fastest
        URL fastestURL;
        if (navigationController.getRouteType() == RouteType.FASTEST) {
            fastestURL = this.getClass().getResource("/icons/flash-blue.png");
        } else {
            fastestURL = this.getClass().getResource("/icons/flash.png");
        }
        ImageIcon fastestIcon = new ImageIcon(fastestURL);
        JLabel fastestRoute = new JLabel();
        fastestRoute.setIcon(fastestIcon);
        fastestRoute.setName("fastest");
        fastestRoute.setBackground(Color.WHITE);
        fastestRoute.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        fastestRoute.addMouseListener(navigationController);
        navigationTypeContainer.add(fastestRoute);

        //shortest
        URL nearbyURL;
        if (navigationController.getRouteType() == RouteType.SHORTEST) {
            nearbyURL = this.getClass().getResource("/icons/nearby-blue.png");
        } else {
            nearbyURL = this.getClass().getResource("/icons/nearby.png");
        }
        ImageIcon nearbyIcon = new ImageIcon(nearbyURL);
        JLabel nearbyRoute = new JLabel();
        nearbyRoute.setIcon(nearbyIcon);
        nearbyRoute.setName("shortest");
        nearbyRoute.setBackground(Color.WHITE);
        nearbyRoute.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nearbyRoute.addMouseListener(navigationController);
        navigationTypeContainer.add(nearbyRoute);

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
        startInput.setName("Fra:");
        startInput.setFont(new Font("Myriad Pro", Font.PLAIN, 16));
        startInput.addFocusListener(new TextController(startInput.getName()));
        startInput.addKeyListener(autoCompleteController);
        inputContainer.add(startInput);

        // Add margin between inputs
        inputContainer.add(Box.createVerticalStrut(10));

        // End input
        endInput = new JTextField(endInputText);
        endInput.setName("Til:");
        endInput.setFont(new Font("Myriad Pro", Font.PLAIN, 16));
        endInput.addFocusListener(new TextController(endInput.getName()));
        endInput.addKeyListener(autoCompleteController);
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
    private JPanel renderMiddlePanel() {
        JPanel middlePanel = new JPanel();
        middlePanel.setOpaque(false);
        middlePanel.setLayout(new BorderLayout());

        // Time label
        if (navigationController.getLength() != null && navigationController.getTime() != null && !navigationController.didError()) {
            JLabel timeLabel = new JLabel("<html><span style='font-size:12px;color:#383838;'>" + navigationController.getTime() + "</span> <span style='font-size:12px;color:#4285F4;'>(" + navigationController.getLength() + ")</span></html>");
            middlePanel.add(timeLabel, BorderLayout.WEST);
        } else if (navigationController.didError()) {
            JLabel errorLabel = new JLabel("<html><span style='color:#a94442;'>Ingen rute fundet med givne indstillinger!</span></html>");
            middlePanel.add(errorLabel, BorderLayout.WEST);
        }

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
        switchFromTo.setBackground(Color.WHITE);
        switchFromTo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        switchFromTo.addActionListener((e) -> navigationController.switchFromAndTo());
        switchAndSubmitPanel.add(switchFromTo, BorderLayout.WEST);

        // Submit-button
        JButton submitButton = new JButton("RUTE");
        submitButton.setForeground(Color.decode("#4285F4"));
        submitButton.setBackground(Color.WHITE);
        submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        submitButton.addActionListener((e) -> navigationController.routeClicked());
        switchAndSubmitPanel.add(submitButton, BorderLayout.EAST);

        return switchAndSubmitPanel;
    }

    private JScrollPane textualNavigation() {
        bottomPanel = new JPanel();
        bottomPanel.setOpaque(true);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        int height = MainWindowView.getHeight();
        bottomPanel.setMaximumSize(new Dimension(width, height-225));
        bottomPanel.setBackground(Color.WHITE);

        // Add textual navigation
        for (TextualElement textualElement : navigationController.getTextualNavigation()) {
            if (textualElement.isAddress()) {
                addNavigationAddress(textualElement.getAddress());
            } else {
                addNavigationText(textualElement.getName(), textualElement.getIconURL(), textualElement.getDist());
            }
        }

        scroll = new JScrollPane(bottomPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setMaximumSize(new Dimension(width, height-225));

        return scroll;
    }

    public void addNavigationAddress(Address address) {
        String text = "<html><span style=\"font-size: 12px;\">" + address.getStreet() + " " + address.getHouse() +
                "</span><br><span style=\"font-size: 10px;\">"+ address.getCity() + address.getPostcode() +"</span></html>";

        JLabel addressLabel = new JLabel(text);
        Border padding = BorderFactory.createEmptyBorder(10, 20, 10, 20);
        Border margin = BorderFactory.createEmptyBorder(5, 0, 5, 0);
        addressLabel.setBorder(BorderFactory.createCompoundBorder(margin, padding));

        bottomPanel.add(addressLabel);
    }

    public void addNavigationText(String navText, String iconURL, String length) {
        String text = "<html><span style=\"font-size: 10px;\">" + navText +
                (length != null ? "</span><br><span style=\"font-size: 8px;\">" + length + "</span></html>" : "");

        JLabel navigationText = new JLabel(text);
        navigationText.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        URL navigationURL = this.getClass().getResource(iconURL);
        ImageIcon navigationIcon = new ImageIcon(navigationURL);
        navigationText.setIcon(navigationIcon);
        navigationText.setIconTextGap(20);
        bottomPanel.add(navigationText);
    }

    public JTextField getStartInput() {
        return startInput;
    }
    public JTextField getEndInput() {
        return endInput;
    }

    public void setStartInputText(String text) { startInputText = text; }
    public void setEndInputText(String text) { endInputText = text; }
}
