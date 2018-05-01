package view;

import controller.MapController;
import model.MetaModel;
import controller.MenuController;
import controller.StateController;
import controller.*;
import model.FavoritesModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * This class creates the main window to display the map in.
 */
public class MainWindowView {
    private static int height;
    private JFrame window;
    private JLayeredPane lpane = new JLayeredPane();
    private MenuController menuController;
    private MetaModel metaModel;
    private MapView mapView;
    private MapController mapController;
    private AddressView addressView;
    private SearchBox searchBox;
    private ZoomView zoomView;
    private NavigationView navigationView;
    private AutoCompleteList autoCompleteList;
    private StateController stateController;
    private boolean initialRender = true;
    private FooterView footerView;
    private FavoriteView favoriteView;
    private FavoriteController favoriteController;
    private FavoritesModel favoritesModel;
    private FavoritePopupView favoritePopupView;

    public MainWindowView(
            MapView cv,
            MetaModel m,
            MapController cc,
            MenuController mc,
            AddressView av,
            SearchBox sb,
            ZoomView zv,
            StateController sc,
            NavigationView nv,
            FooterView fv,
            FavoriteView favoriteView,
            FavoriteController favoriteController,
            AutoCompleteList al,
            FavoritesModel favoritesModel,
            FavoritePopupView favoritePopupView
    ) {
        menuController = mc;
        mapView = cv;
        metaModel = m;
        mapController = cc;
        addressView = av;
        searchBox = sb;
        zoomView = zv;
        stateController = sc;
        navigationView = nv;
        footerView = fv;
        this.favoriteView = favoriteView;
        this.favoriteController = favoriteController;
        autoCompleteList = al;
        this.favoritesModel = favoritesModel;
        this.favoritePopupView = favoritePopupView;

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

        mapController.pan(-metaModel.getMinLon(), -metaModel.getMaxLat());
        mapController.zoom(height / (metaModel.getMaxLon() - metaModel.getMinLon()), 0, 0);

        // Ensure that the initial canvas is properly centered, even on screens that are wider than they are tall.
        mapController.pan(offsetX, 0);
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
                    lpane.remove(autoCompleteList);
                    break;

                case ADDRESS_ENTERED:
                    lpane.remove(searchBox);
                    lpane.remove(autoCompleteList);
                    lpane.remove(addressView);
                    break;

                case NAVIGATION_ACTIVE:
                    mapController.removeRoute();
                    lpane.remove(searchBox);
                    lpane.remove(autoCompleteList);
                    lpane.remove(navigationView);
                    break;
                case FAVORITES:
                    lpane.remove(favoriteView);
                    lpane.remove(searchBox);
                    break;
                case FAVORITES_POPUP:
                    lpane.remove(favoritePopupView);
                    break;
            }
        }

        // Rerender components
        autoCompleteList.update();
        searchBox.update();
        addressView.update();
        navigationView.update();

        // Add components
        switch(stateController.getCurrentState()) {
            case INITIAL:
                lpane.add(searchBox, 2, 2);
                lpane.add(autoCompleteList, 3, 6);
                break;

            case ADDRESS_ENTERED:
                lpane.add(searchBox, 2, 2);
                lpane.add(autoCompleteList, 3, 6);
                lpane.add(addressView, 1, 3);
                break;

            case NAVIGATION_ACTIVE:
                lpane.add(searchBox, 2, 2);
                lpane.add(autoCompleteList, 3, 6);
                lpane.add(navigationView, 1, 4);
                break;

            case FAVORITES:
                lpane.add(searchBox, 2, 2);
                lpane.add(favoriteView, 1, 5);
                break;

            case FAVORITES_POPUP:
                favoritePopupView.addFrame(window);
                lpane.add(favoritePopupView, 3, 7);
                break;


            default:
                // No other viewStates should exist!
        }

        if (initialRender) {
            lpane.add(mapView, 0, 0);
            lpane.add(zoomView, 3, 1);
            lpane.add(footerView, 4, 5);
        }

        // Display!
        if (initialRender) {
            window.pack();
            window.setVisible(true);
            initialRender = false;
        }

        height = window.getContentPane().getHeight();
        int width = window.getContentPane().getWidth();

        // Setup bounds once the screen size has been determined
        lpane.setBounds(0, 0, width, height);
        mapView.setBounds(0, 0, width, height);
        zoomView.setBounds(width - 100,height - 130,70,70);
        footerView.setBounds(0, height - 30, width, 30);
        favoriteView.setBounds(0, 0, 450, height);
        favoriteView.updateBound(height);

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
        loadItem.addActionListener((ActionEvent e) -> menuController.load(window));
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
        ButtonGroup colorGroup = new ButtonGroup();

        JMenuItem standardItem = new JRadioButtonMenuItem("Standard");
        standardItem.addActionListener((ActionEvent e) -> menuController.standardMode());
        standardItem.setSelected(true);
        colorGroup.add(standardItem);
        subShowMenu.add(standardItem);

        JMenuItem protanopiaItem = new JRadioButtonMenuItem("Rødblind (Protanopia)");
        protanopiaItem.addActionListener((ActionEvent e) -> menuController.protanopiaMode());
        colorGroup.add(protanopiaItem);
        subShowMenu.add(protanopiaItem);

        JMenuItem deuteranopiaItem = new JRadioButtonMenuItem("Grønblind (Deuteranopia)");
        deuteranopiaItem.addActionListener((ActionEvent e) -> menuController.deuteranopiaMode());
        colorGroup.add(deuteranopiaItem);
        subShowMenu.add(deuteranopiaItem);

        JMenuItem tritanopiaItem = new JRadioButtonMenuItem("Blåblind (Tritanopia)");
        tritanopiaItem.addActionListener((ActionEvent e) -> menuController.tritanopiaMode());
        colorGroup.add(tritanopiaItem);
        subShowMenu.add(tritanopiaItem);

        JMenuItem grayscaleItem = new JRadioButtonMenuItem("Gråskala");
        grayscaleItem.addActionListener((ActionEvent e) -> menuController.grayscaleMode());
        colorGroup.add(grayscaleItem);
        subShowMenu.add(grayscaleItem);

        // create the Help menu
        JMenu helpMenu = new JMenu("Help");
        menubar.add(helpMenu);
    }

    public static int getHeight() {
        return height;
    }

    public JLayeredPane getlpane() {
        return lpane;
    }
}
