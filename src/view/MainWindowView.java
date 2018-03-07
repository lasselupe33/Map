package view;

import controller.CanvasController;
import controller.MenuController;
import model.MainModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * This class creates the main window to display the map in.
 */
public class MainWindowView {
    public JFrame window;
    private MenuController menuController;

    public MainWindowView(CanvasView cv, MainModel m, CanvasController cc, MenuController mc) {
        menuController = mc;
        window = new JFrame("Danmarkskort");
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.add(cv, BorderLayout.CENTER);
        makeMenuBar(window);
        window.pack();
        window.setVisible(true);

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
