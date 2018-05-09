package view;

import javafx.geometry.VerticalDirection;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.net.URL;

public class HelpWindow {
    public HelpWindow(HelpType helpType) {
        // Create the window
        JFrame window = new JFrame();
        //window.setLayout(new BorderLayout());
        window.setPreferredSize(new Dimension(530, 530));
        window.setResizable(false);

        switch (helpType) {
            case HELP:
                window.add(addHelpPanel());
                break;
            case USERMANUAL:
                window.add(addUserManualPanel());
                break;
            default:
                // Other cases should never occur
                break;
        }

        window.setVisible(true);
        window.pack();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation(dimension.width/2-window.getSize().width/2, dimension.height/2-window.getSize().height/2);
    }

    private JScrollPane addHelpPanel() {
        JPanel helpPanel = new JPanel();
        helpPanel.setLayout(new BoxLayout(helpPanel, BoxLayout.Y_AXIS));
        helpPanel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        helpPanel.setBackground(Color.WHITE);
        helpPanel.setOpaque(true);


        // Add title
        JLabel panelTitle = new JLabel("Hjælp");
        panelTitle.setFont(new Font("Myriad Pro", Font.PLAIN, 26));
        helpPanel.add(panelTitle);

        // Add text about loading in osm file
        heading("Indlæs OSM-fil", helpPanel);
        String osmText = "<html>Klik på 'Indlæs OSM-fil' under 'Filer' og vælg den ønskede OSM-fil.<br/>" +
                "Vent på det nye kort loades.</html>";
        text(osmText, helpPanel);

        // Add text about saving to a binary file
        heading("Gem til binær fil", helpPanel);
        String saveText = "<html>Klik på 'Gem' under 'Filer'. I bunden af skærmen vises, hvor langt<br/>" +
                                "programmet er med at gemme.</html>";
        text(saveText, helpPanel);

        /*
        // Add text about loading a binary file
        JLabel loadHeading = heading("Indlæs fra binær fil");
        String lText = "";
        JLabel loadText = text(lText);
        helpPanel.add(loadHeading);
        helpPanel.add(loadText);
        */

        // Add zoom text
        heading("Zoom", helpPanel);
        String zoomText = "<html>Zoom på kortet foregår via zoom-knapperne i nederste højre hjørne<br/>" +
                             "eller via musescroll. Knapperne zoomer til kortets midte, mens<br/>" +
                             "scroll zoomer til det punkt, hvor musen er placeret. Der kan også<br/>" +
                             "zoomes ind ved dobbeltklik. Genvejstaster: + (zoom ind), - (zoom ud).</html>";
        text(zoomText, helpPanel);

        // Add pan text
        heading("Panorer", helpPanel);
        String panText = "<html>Hold musen inde og træk i kortet for at panorere. Genvejstaster:<br/>" +
                "w (op), a (venstre), s (ned), d (højre)</html>";
        text(panText, helpPanel);


        // Add text about changing color settings
        heading("Vælg farveindstillinger", helpPanel);
        String colorText = "<html>Vælg farveindstillinger under 'Farveindstillinger' under menupunktet<br/>" +
                                 "'Indstillinger'. Vælg mellem standardfarver, rødfarveblind, grønfarve-<br/>" +
                                 "blind og sort/hvid.</html>";
        text(colorText, helpPanel);

        // Add text about antialiasing
        heading("Antialiasing", helpPanel);
        text("<html>Antialiasing slås automatisk til når kortet er stillestående. Kan slås<br/>til og fra med genvejstast x.</html>", helpPanel);

        // Add text about icons
        heading("Ikonforklaring", helpPanel);
        iconText("Søg efter indtastet adresse", "/icons/search.png", helpPanel);
        iconText("Gå til rutevejledning", "/icons/navigation.png", helpPanel);
        iconText("Gå til liste over favoritter", "/icons/favorite.png", helpPanel);
        iconText("Gem adresse som en favorit", "/icons/bookmark.png", helpPanel);
        iconText("Bilrute", "/icons/car.png", helpPanel);
        iconText("Cykelrute", "/icons/cycle.png", helpPanel);
        iconText("Gårute", "/icons/pedestrian.png", helpPanel);
        iconText("Hurtigste rute", "/icons/flash-blue.png", helpPanel);
        iconText("Korteste rute", "/icons/nearby-blue.png", helpPanel);

        JScrollPane scroll = new JScrollPane(helpPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        return scroll;
    }

    private JScrollPane addUserManualPanel() {
        JPanel userManualPanel = new JPanel();
        userManualPanel.setLayout(new BoxLayout(userManualPanel, BoxLayout.Y_AXIS));
        userManualPanel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        userManualPanel.setBackground(Color.WHITE);
        userManualPanel.setOpaque(true);

        // Add title
        JLabel panelTitle = new JLabel("Brugervejledning");
        panelTitle.setFont(new Font("Myriad Pro", Font.PLAIN, 26));
        userManualPanel.add(panelTitle);

        // Add text about searching for an adress
        heading("Søg efter adresse", userManualPanel);
        String addressText = "<html>Søg efter en adresse ved at skrive den i søgefeltet i øverste venstre<br/>" +
                                    "hjørne. Vælg en adresse fra auto complete listen ved at dobbeltklik-<br/>" +
                                    "ke på den eller skriv hele adressen og søg ved enter eller klik på<br/>" +
                                    "søgeikonet. Det er også muligt at vælge et sted på kortet ved at<br/>" +
                                    "højreklikke. Adressen nærmest musen vises i nederste venstre<br/>" +
                                    "hjørne.</html>";
        text(addressText,userManualPanel);

        // Add test about searching for a route
        heading("Søg efter rutevejledning", userManualPanel);
        String routeText = "<html>Gå til rutenavigation via ruteikonet til højre for søgebaren eller<br/>" +
                                "søg på en adresse og klik derefter på ruteikonet for at finde en rute<br/>" +
                                "fra denne adresse. Det er muligt at vælge mellem bilrute, cykelrute<br/>" +
                                "eller gårute samt at vælge mellem hurtigste eller korteste rute.</html>";
        text(routeText,userManualPanel);

        // Add text about favorites
        heading("Favoritter", userManualPanel);
        String favoritesText = "<html>En adresse kan gemmes som favorit, så den er nem at tilgå igen. En<br/>" +
                                    "adresse kan gemmes under et valgfrit navn ved at trykke på bogmær-<br/>" +
                                    "ket, der vises ud fra en adresse, når man har søgt på den. Favoritter<br/>" +
                                    "vises på kortet med blå ikoner, og en liste over alle favoritter kan<br/>" +
                                    "ses ved ved at klikke på favoritikonet yderst til venstre i søgebaren.<br/>" +
                                    "En favorit kan vælges ved at klikke på den i listen eller på kortikonet.</html>";
        text(favoritesText, userManualPanel);

        JScrollPane scroll = new JScrollPane(userManualPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        return scroll;
    }

    /*
    private JPanel addShortcutsPanel() {
        JPanel shortcutsPanel = new JPanel();
        shortcutsPanel.setLayout(new BoxLayout(shortcutsPanel, BoxLayout.Y_AXIS));
        shortcutsPanel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        shortcutsPanel.setBackground(Color.WHITE);
        shortcutsPanel.setOpaque(true);


        // Add title
        JLabel panelTitle = new JLabel("Shortcuts");
        panelTitle.setFont(new Font("Myriad Pro", Font.PLAIN, 24));
        shortcutsPanel.add(panelTitle);

        text("x: slå antialising til/fra", shortcutsPanel);
        text("w: panorer op", shortcutsPanel);
        text("a: panorer til venstre", shortcutsPanel);
        text("s: panorer ned", shortcutsPanel);
        text("d: panorer til højre", shortcutsPanel);
        text("+: zoom ind", shortcutsPanel);
        text("-: zoom ud", shortcutsPanel);

        return shortcutsPanel;
    }*/

    private void heading(String title, JPanel p) {
        JLabel heading = new JLabel(title);
        heading.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        heading.setFont(new Font("Myriad Pro", Font.PLAIN, 18));
        p.add(heading);
    }

    private void text(String text, JPanel p) {
        JLabel textLabel = new JLabel(text);
        textLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        textLabel.setFont(new Font("Calibri", Font.PLAIN, 16));
        p.add(textLabel);
    }

    private void iconText(String text, String iconURL, JPanel p) {
        JLabel textLabel = new JLabel(text);
        textLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        textLabel.setFont(new Font("Calibri", Font.PLAIN, 16));
        URL url = this.getClass().getResource(iconURL);
        ImageIcon icon = new ImageIcon(url);
        textLabel.setIcon(icon);
        textLabel.setIconTextGap(20);
        p.add(textLabel);
    }

    public enum HelpType {
        HELP, USERMANUAL
    }
}
