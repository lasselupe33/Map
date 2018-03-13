package view;

import controller.CanvasController;
import controller.MenuController;
import model.MainModel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * This class creates the main window to display the map in.
 */
public class MainWindowView {
    public JFrame window;
    public JLayeredPane lpane = new JLayeredPane();
    private MenuController menuController;

    public MainWindowView(CanvasView cv, MainModel m, CanvasController cc, MenuController mc, AddressView av, SearchBox sb) {
        menuController = mc;

        // Create the window
        window = new JFrame("Danmarkskort");
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Setup pane to contain layered components
        window.add(lpane, BorderLayout.CENTER);

        // Add components
        lpane.add(cv, 0, 0);
        lpane.add(av, 1, 0);
        lpane.add(sb, 2, 0);
        makeMenuBar(window);

        // Display!
        window.pack();
        window.setVisible(true);

        // Setup bounds once the screen size has been determined
        lpane.setBounds(0, 0, window.getWidth(), window.getHeight());
        cv.setBounds(0, 0,window.getWidth(), window.getHeight());
        sb.setBounds(30, 25, 470, 40);

        // put screen to correct place on canvas
        cc.pan(-m.getMinLon(), -m.getMaxLat());
        cc.zoom(window.getHeight() / (m.getMaxLon() - m.getMinLon()), 0, 0);
    }

    // Create menubar
    private void makeMenuBar(JFrame frame)
    {
        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);

        // create the File menu
        JMenu fileMenu = new JMenu("Filer");
        menubar.add(fileMenu);

        JMenuItem loadItem = new JMenuItem("Indlæs OSM-fil");
        loadItem.addActionListener((ActionEvent e) -> {menuController.load();});
        fileMenu.add(loadItem);

        JMenuItem saveItem = new JMenuItem("Gem");
        saveItem.addActionListener((ActionEvent e) -> {menuController.save();});
        fileMenu.add(saveItem);

        JMenuItem quitItem = new JMenuItem("Afslut");
        quitItem.addActionListener((ActionEvent e) -> {menuController.quit();});
        fileMenu.add(quitItem);

        // create the Show menu
        JMenu showMenu = new JMenu("Indstillinger");
        menubar.add(showMenu);

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
