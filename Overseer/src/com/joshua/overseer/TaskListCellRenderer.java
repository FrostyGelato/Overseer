package com.joshua.overseer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class TaskListCellRenderer extends JPanel implements ListCellRenderer<TaskForList> {
	
    private JLabel nameLabel;
    private JLabel timeLabel;

    public TaskListCellRenderer() {
    	
        super(new BorderLayout());
        nameLabel = new JLabel();
        timeLabel = new JLabel();
        
        nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        timeLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));

        add(nameLabel, BorderLayout.PAGE_START);
        add(timeLabel, BorderLayout.PAGE_END);
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends TaskForList> list, TaskForList value, int index, boolean isSelected, boolean cellHasFocus) {
        nameLabel.setText(value.name);
        timeLabel.setText(value.time);
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }

}
