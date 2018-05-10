package view;

import controller.MapController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DecimalFormat;

public class FooterView extends JPanel {
    public JLabel hoverAddress = new JLabel();
    private JLabel progressbar = new JLabel();
    private ScaleBar distanceIcon;

    public FooterView(MapController cc){
        distanceIcon = new ScaleBar(cc);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        setOpaque(true);
        hoverAddress.setText("Der er ikke fundet en addresse endnu.");
        hoverAddress.setBorder(new EmptyBorder(0, 10, 0, 0));
        progressbar.setForeground(Color.decode("#a94442"));
        progressbar.setHorizontalAlignment(JLabel.CENTER);

        add(hoverAddress, BorderLayout.WEST);
        add(progressbar, BorderLayout.CENTER);
        add(distanceIcon, BorderLayout.EAST);
    }
    
    public void updateHoverAddress(String address) {
        hoverAddress.setText(address);
    }

    /** Helper that updates the progressbar that is displayed in the middle of the footerView */
    public void updateProgressbar(String text, double progress) {
        if (progress != 100.0) {
            DecimalFormat formatter = new DecimalFormat("#.00");
            progressbar.setText(text + " " + formatter.format(progress) + "%");
        } else {
            progressbar.setText("");
        }
    }
}

