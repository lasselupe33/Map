package view;

import controller.MapController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DecimalFormat;

public class FooterView extends JPanel {
    public JLabel hoverAdress = new JLabel();
    private JLabel savingStatus = new JLabel();
    private DistanceCalculation distanceIcon;

    public FooterView(MapController cc){
        distanceIcon = new DistanceCalculation(cc);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        setOpaque(true);
        hoverAdress.setText("IT-Universitetet i København Rued Langgaards Vej 7");
        hoverAdress.setBorder(new EmptyBorder(0, 10, 0, 0));
        savingStatus.setForeground(Color.decode("#a94442"));
        savingStatus.setHorizontalAlignment(JLabel.CENTER);

        add(hoverAdress, BorderLayout.WEST);
        add(savingStatus, BorderLayout.CENTER);
        add(distanceIcon, BorderLayout.EAST);
    }

    public void updateSaveStatus(double progress) {
        if (progress != 100.0) {
            DecimalFormat formatter = new DecimalFormat("#.00");
            savingStatus.setText("Gemmer... " + formatter.format(progress) + "%");
        } else {
            savingStatus.setText("");
        }
    }
}

