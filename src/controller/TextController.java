package controller;

import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class TextController implements FocusListener {
    private String label;
    private JTextField t;

    public TextController(String label){
        this.label = label;
    }

    //Klassen er kun til textfields
    @Override
    public void focusGained(FocusEvent e) {
        t = (JTextField) e.getComponent();

        if(t.getText().equals(label)){
            t.setText("");
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if(t.getText().equals("")) {
            t.setText(label);
        }
    }
}
