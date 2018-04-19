package view;

import controller.AutoCompleteController;

import javax.swing.*;
import java.awt.*;

public class AutoCompleteList extends JPanel {
    private AutoCompleteController autoCompleteController;
    private JList list;

    public AutoCompleteList(AutoCompleteController acc) {
        autoCompleteController = acc;

        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        list = new JList();
        list.setListData(new String[] {"1243", "24", "gmer", "ghjrt", "jgoreig", "gjr", "jgr"});
        list.setLayoutOrientation(JList.VERTICAL);

        JScrollPane listScroller = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        listScroller.setPreferredSize(new Dimension(250, 80));
        this.add(listScroller, BorderLayout.NORTH);

    }

    public void update() {
        list.setListData(autoCompleteController.getMatchingAddresses().toArray());
    }
}
