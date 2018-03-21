package view;

import controller.CanvasController;
import model.MainModel;

import javax.swing.*;
import java.awt.*;

public class FooterView extends JPanel {
    private JLabel hoverAdress = new JLabel();
    private JLabel hoverAdress2 = new JLabel();
    private DistanceCalculation distanceIcon;

    public FooterView(CanvasController cc, MainModel mm){
        distanceIcon = new DistanceCalculation(cc, mm);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        setOpaque(true);
        hoverAdress.setPreferredSize(new Dimension(200, 200));
        hoverAdress.setText("IT-Universitetet i København Rued Langgaards Vej 7");


        add(hoverAdress, BorderLayout.LINE_START);
        add(distanceIcon, BorderLayout.CENTER);




    }
}

