package controller;

import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class TextController implements FocusListener {
    private String text;
    private JTextField t;

    public TextController(){

    }

    //Klassen er kun til textfields
    @Override
    public void focusGained(FocusEvent e) {
        t = (JTextField) e.getComponent();
        text = t.getName();
        if(t.getText().equals(text)){
            t.setText("");
        }
   }

    @Override
    public void focusLost(FocusEvent e) {
        if(t.getText().equals("")) {
            t.setText(text);
        }

    }

}
