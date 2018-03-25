package controller;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class TextController implements FocusListener {
    private String text;
    private JTextField t;
    private Boolean isUsed;

    public TextController(){
        isUsed = false;
    }


    //Klassen er kun til textfields
    @Override
    public void focusGained(FocusEvent e) {
        t = (JTextField) e.getComponent();
        if(!isUsed){
            text = t.getText();
            isUsed = true;
        }
        if(t.getText().equals(text)) {
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
