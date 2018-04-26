package view;

import controller.AutoCompleteController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AutoCompleteList extends JPanel {
    private AutoCompleteController autoCompleteController;
    private JList list;

    public AutoCompleteList(AutoCompleteController acc) {
        autoCompleteController = acc;

        this.setLayout(new BorderLayout());
        this.setVisible(false);

        list = new JList();
        list.setLayoutOrientation(JList.VERTICAL);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                autoCompleteController.onListClick(e);
            }
        });

        JScrollPane listScroller = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        listScroller.setPreferredSize(new Dimension(250, 160));
        this.add(listScroller, BorderLayout.NORTH);
    }

    public JList getList() {
        return list;
    }

    public void setVisibility(boolean isVisible) {
        this.setVisible(isVisible);
    }

    public void updatePosition() {
        setBounds(20, 52, 445, 150);
    }

    public void update() {
        if (autoCompleteController.getMatchingAddresses().size() == 0) {
            list.setListData(new String[] {"Ingen addresser fundet..."});
        } else {
            list.setListData(autoCompleteController.getMatchingAddresses().toArray());
        }
    }
}
