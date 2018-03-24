package view;

import controller.CanvasController;
import model.MainModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class FooterView extends JPanel {
    private JLabel hoverAdress = new JLabel();
    private DistanceCalculation distanceIcon;

    public FooterView(CanvasController cc){
        distanceIcon = new DistanceCalculation(cc);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        setOpaque(true);
        hoverAdress.setText("IT-Universitetet i København Rued Langgaards Vej 7");
        hoverAdress.setBorder(new EmptyBorder(0, 10, 0, 0));


        add(hoverAdress, BorderLayout.WEST);
        add(distanceIcon, BorderLayout.EAST);
    }
}

