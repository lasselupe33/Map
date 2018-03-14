package view;

import javax.swing.*;
import java.awt.*;

public class NavigationView extends JPanel {
    private boolean initialRender = true;

    public NavigationView() {
        // Setup view
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
    }

    /**
     * Helper that updates the state of the navigation view
     */
    public void update() {
        if (!initialRender) {

        } else {
            initialRender = false;
        }

        add(navigationTypeInputs(), BorderLayout.NORTH);
    }

    public JPanel navigationTypeInputs() {
        JPanel navigationTypeContainer = new JPanel();


        return navigationTypeContainer;
    }
}
