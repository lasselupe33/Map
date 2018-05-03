package controller;

import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * This class is only used for text fields
 * Used for removing and setting text in text fields when focus is gained and lost
 */
public class TextController implements FocusListener {
    private String label;
    private JTextField t;

    public TextController(String label){
        this.label = label;
    }

    /**
     * Remove text from input field when focus is gained if the current text equals label
     * @param e FocusEvent, input field clicked
     */
    @Override
    public void focusGained(FocusEvent e) {
        t = (JTextField) e.getComponent();

        if(t.getText().equals(label)){
            t.setText("");
        }
    }

    /**
     * If the input field is empty when focus is lost set the input text to equal label
     * @param e FocusEvent
     */
    @Override
    public void focusLost(FocusEvent e) {
        if(t.getText().equals("")) {
            t.setText(label);
        }
    }
}
