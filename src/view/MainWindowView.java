package view;

import controller.CanvasController;
import controller.MenuController;
import controller.StateController;
import controller.ViewStates;
import model.MainModel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * This class creates the main window to display the map in.
 */
public class MainWindowView {
    private JFrame window;
    public JLayeredPane lpane = new JLayeredPane();
    private MenuController menuController;
    private MainModel mainModel;
    private CanvasView canvasView;
    private CanvasController canvasController;
    private AddressView addressView;
    private SearchBox searchBox;
    private ZoomView zoomView;
    private NavigationView navigationView;
    private StateController stateController;
    private boolean initialRender = true;
    private FooterView footerView;

    public MainWindowView(
            CanvasView cv,
            MainModel m,
            CanvasController cc,
            MenuController mc,
            AddressView av,
            SearchBox sb,
            ZoomView zv,
            StateController sc,
            NavigationView nv,
            FooterView fv
    ) {
        menuController = mc;
        canvasView = cv;
        mainModel = m;
        canvasController = cc;
        addressView = av;
        searchBox = sb;
        zoomView = zv;
        stateController = sc;
        navigationView = nv;
        footerView = fv;

        // Create the window
        window = new JFrame("Danmarkskort");
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Setup window size
        GraphicsConfiguration gc = window.getGraphicsConfiguration();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
        int width = gc.getBounds().width - screenInsets.left - screenInsets.right;
        int height = gc.getBounds().height - screenInsets.top - screenInsets.bottom;
        window.setPreferredSize(new Dimension(width, height));

        // Setup pane to contain layered components
        window.add(lpane, BorderLayout.CENTER);

        makeMenuBar();

        // Run initial render
        update();

        // put screen to correct place on canvas
        height = window.getContentPane().getHeight();
        int offsetX = (window.getContentPane().getWidth() - height) / 2;

        canvasController.pan(-mainModel.getMinLon(), -mainModel.getMaxLat());
        canvasController.zoom(height / (mainModel.getMaxLon() - mainModel.getMinLon()), 0, 0);

        // Ensure that the initial canvas is properly centered, even on screens that are wider than they are tall.
        canvasController.pan(offsetX, 0);
    }

    public JFrame getWindow() {
        return window;
    }

    /**
     * Helper that updates the MainWindowView based on the current ViewState.
     */
    public void update() {
        // Remove old components
        if (!initialRender) {
            switch (stateController.getPrevState()) {
                case INITIAL:
                    lpane.remove(searchBox);
                    break;

                case ADDRESS_ENTERED:
                    lpane.remove(searchBox);
                    lpane.remove(addressView);
                    break;

                case NAVIGATION_ACTIVE:
                    lpane.remove(searchBox);
                    lpane.remove(navigationView);
                    break;
            }
        }

        // Rerender components
        searchBox.update();
        addressView.update();

        // Add components
        switch(stateController.getCurrentState()) {
            case INITIAL:
                lpane.add(searchBox, 2, 2);
                break;

            case ADDRESS_ENTERED:
                lpane.add(searchBox, 2, 2);
                lpane.add(addressView, 1, 3);
                break;

            case NAVIGATION_ACTIVE:
                lpane.add(searchBox, 2, 2);
                lpane.add(navigationView, 1, 4);
                break;

            default:
                // No other viewStates should exist!
        }

        if (initialRender) {
            lpane.add(canvasView, 0, 0);
            lpane.add(zoomView, 3, 1);
            lpane.add(footerView, 4, 5);
        }

        // Display!
        if (initialRender) {
            window.pack();
            window.setVisible(true);
            initialRender = false;
        }

        int height = window.getContentPane().getHeight();
        int width = window.getContentPane().getWidth();

        // Setup bounds once the screen size has been determined
        lpane.setBounds(0, 0, width, height);
        canvasView.setBounds(0, 0, width, height);
        zoomView.setBounds(width - 100,height - 130,70,70);
        navigationView.setBounds(0, 0, 450, height);
        footerView.setBounds(0, height - 30, width, 30);

        // Update the previous state after render
        stateController.updatePrevState();
    }

    /**
     * Helper that creates the menubar displayed on the top of the application.
     */
    private void makeMenuBar() {
        JMenuBar menubar = new JMenuBar();
        window.setJMenuBar(menubar);

        // create the File menu
        JMenu fileMenu = new JMenu("Filer");
        menubar.add(fileMenu);

        JMenuItem loadItem = new JMenuItem("Indlæs OSM-fil");
        loadItem.addActionListener((ActionEvent e) -> menuController.load());
        fileMenu.add(loadItem);

        JMenuItem saveItem = new JMenuItem("Gem");
        saveItem.addActionListener((ActionEvent e) -> menuController.save());
        fileMenu.add(saveItem);

        JMenuItem quitItem = new JMenuItem("Afslut");
        quitItem.addActionListener((ActionEvent e) -> menuController.quit());
        fileMenu.add(quitItem);

        // create the Show menu
        JMenu showMenu = new JMenu("Indstillinger");
        menubar.add(showMenu);

        JMenu subShowMenu = new JMenu("Farveindstillinger");
        showMenu.add(subShowMenu);

        JMenuItem standardItem = new JMenuItem("Standard");
        standardItem.addActionListener((ActionEvent e) -> menuController.standardMode());
        subShowMenu.add(standardItem);

        JMenuItem colorBlindItem = new JMenuItem("Farveblind (Protanopia)");
        colorBlindItem.addActionListener((ActionEvent e) -> menuController.colorBlindMode());
        subShowMenu.add(colorBlindItem);

        JMenuItem grayscaleItem = new JMenuItem("Gråskala");
        grayscaleItem.addActionListener((ActionEvent e) -> menuController.grayscaleMode());
        subShowMenu.add(grayscaleItem);

        /*
        JMenuItem pRoadItem = new JCheckBoxMenuItem("Primary roads", true);
        pRoadItem.addActionListener(this);
        showMenu.add(pRoadItem);

        JMenuItem sRoadItem = new JCheckBoxMenuItem("Secondary roads", true);
        sRoadItem.addActionListener(this);
        showMenu.add(sRoadItem);

        JMenuItem pathsItem = new JCheckBoxMenuItem("Paths", true);
        pathsItem.addActionListener(this);
        showMenu.add(pathsItem);

        JMenuItem buildingsItem = new JCheckBoxMenuItem("Buildings", true);
        buildingsItem.addActionListener(this);
        showMenu.add(buildingsItem);

        JMenuItem antiAItem = new JCheckBoxMenuItem("Antialiasing", true);
        antiAItem.addActionListener(this);
        showMenu.add(antiAItem);
        */

        // create the Help menu
        JMenu helpMenu = new JMenu("Help");
        menubar.add(helpMenu);
    }
}
