package com.joshua.overseer;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class MainMenu extends JFrame {

	private JPanel contentPane;
	
	DefaultListModel<Task> model = new DefaultListModel<>();
	JList<Task> taskJList = new JList<>(model);
	
	String taskPath = System.getProperty("user.home") + File.separator + ".overseer" + File.separator + "tasks.json";

	public MainMenu() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// checks if folder for program exists
		DirectoryChecker directoryChecker = new DirectoryChecker();
		loadSchedule();
		
		TaskManager taskManager = new TaskManager();
		
		JButton addTask = new JButton("Add");
		addTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AddMenu newTask = new AddMenu();
				newTask.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				// Set window title
				newTask.setTitle("Add Task");
				newTask.setVisible(true);
			}
		});
		addTask.setBounds(0, 728, 97, 25);
		contentPane.add(addTask);
		
		JButton modifyTask = new JButton("Modify");
		modifyTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 ModifyMenu modifyTask = new ModifyMenu();
				 modifyTask.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				 modifyTask.setTitle("Modify Task");
				 modifyTask.setVisible(true);
			}
		});
		modifyTask.setBounds(95, 728, 97, 25);
		contentPane.add(modifyTask);
		
		JButton deleteTask = new JButton("Delete");
		deleteTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				taskManager.deleteTask(taskJList.getSelectedIndex());
				refreshSchedule();
			}
		});
		deleteTask.setBounds(190, 728, 97, 25);
		contentPane.add(deleteTask);
		
		JButton settingButton = new JButton("Settings");
		settingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Settings settings = new Settings();
					settings.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					settings.setVisible(true);
					settings.setTitle("Settings");
				} catch (IOException launchError) {
					launchError.printStackTrace();
				}
				
			}
		});
		settingButton.setBounds(285, 728, 97, 25);
		contentPane.add(settingButton);
	}
	
	public static class TaskListCellRenderer extends JPanel implements ListCellRenderer<Task> {
        private JLabel nameLabel;
        private JLabel timeLabel;

        public TaskListCellRenderer() {
            super(new BorderLayout());
            nameLabel = new JLabel();
            timeLabel = new JLabel();

            add(nameLabel, BorderLayout.PAGE_START);
            add(timeLabel, BorderLayout.PAGE_END);
            setOpaque(true); //for visible backgrounds
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Task> list, Task value, int index, boolean isSelected,
                boolean cellHasFocus) {
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
	
	public void loadSchedule() {
		if (new File(taskPath).isFile() == true) {
			loadData();
		
			taskJList.setCellRenderer(new TaskListCellRenderer());
			taskJList.setBounds(0, 40, 380, 680);
			contentPane.add(taskJList);
		}
	}
	
	public void refreshSchedule() {
		model.clear();
		
		loadData();
        
        taskJList.setModel(model);
	}
	
	public void loadData() {
		JSONArray taskArray = new JSONArray();
		JSONParser parser = new JSONParser();
		
		try {
			taskArray = (JSONArray) parser.parse(new FileReader(taskPath));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
        for (Object arrayTask:taskArray)
        {
          JSONObject jsonTask = (JSONObject) arrayTask;
          String name = (String) jsonTask.get("name");
          String time = (String) jsonTask.get("timeRequired");
          Task taskEvent = new Task(name, time);
          model.addElement(taskEvent);
        }
	}

}
