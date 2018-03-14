package view;

import javax.swing.*;
import java.awt.*;

public class FooterView extends JPanel {
    private JLabel hoverAdress = new JLabel();

    public FooterView(){
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK));
        setOpaque(true);
        hoverAdress.setText("IT-Universitetet i København Rued Langgaards Vej 7");
        hoverAdress.setForeground(Color.decode("#383838"));
        hoverAdress.setBackground(Color.BLACK);
        add(hoverAdress, BorderLayout.WEST);
    }
}
