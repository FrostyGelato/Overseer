package com.joshua.overseer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Component;

public class MainMenu extends JFrame {
	
	MainMenu thisFrame = this;

	private JPanel contentPane;
	
	String sessionPath = System.getProperty("user.home") + File.separator + ".overseer" + File.separator + "session.json";
	
	// for schedule
	DefaultListModel<TaskForList> listModel = new DefaultListModel<>();
	JList<TaskForList> taskJList = new JList<>(listModel);
	
	LocalDate dateShown = LocalDate.now();
	
	JSONArray arrayForTimer;
	ArrayList<SessionForTimer> arrayListForTime = new ArrayList<SessionForTimer>();
	
	Font standardFont = new Font("Tahoma", Font.PLAIN, 16);

	public MainMenu() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 800);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 255, 243));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// checks if folder for program exists
		DirectoryChecker directoryChecker = new DirectoryChecker();
		directoryChecker.checkForSession();
		
		JButton addTask = new JButton("Add");
		addTask.setFont(standardFont);
		addTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AddMenu newTask = new AddMenu(thisFrame);
				newTask.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				newTask.setTitle("Add Task");
				newTask.setVisible(true);
			}
		});
		addTask.setBounds(0, 728, 97, 25);
		contentPane.add(addTask);
		
		JButton modifyTask = new JButton("Modify");
		modifyTask.setFont(standardFont);
		modifyTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (taskJList.getSelectedValue() != null) {
					ModifyMenu modifyTask = new ModifyMenu(thisFrame, taskJList.getSelectedValue().name);
					 modifyTask.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					 modifyTask.setTitle("Modify Task");
					 modifyTask.setVisible(true);
				} else {
					JLabel modifyMessage = new JLabel("<html>Please select a task before modifying.</html>", SwingConstants.CENTER);
			    	modifyMessage.setFont(standardFont);
					JOptionPane.showMessageDialog(null, modifyMessage, "No Task Selected",JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		modifyTask.setBounds(95, 728, 97, 25);
		contentPane.add(modifyTask);
		
		JButton deleteTask = new JButton("Delete");
		deleteTask.setFont(standardFont);
		deleteTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//needed
				//loads tasks from disk so they can be deleted
				if (taskJList.getSelectedValue() != null) {
					TaskManager taskManager = new TaskManager();
					
					taskManager.deleteTaskWithRefresh(taskJList.getSelectedValue().name, thisFrame);
					
					refreshSchedule();
				} else {
					JLabel deleteMessage = new JLabel("<html>Please select a task before deleting.</html>", SwingConstants.CENTER);
			    	deleteMessage.setFont(standardFont);
					JOptionPane.showMessageDialog(null, deleteMessage, "No Task Selected",JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		deleteTask.setBounds(190, 728, 97, 25);
		contentPane.add(deleteTask);
		
		JButton settingButton = new JButton("Settings");
		settingButton.setFont(standardFont);
		settingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Settings settings = new Settings(thisFrame);
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
		
		JButton btnNewButton_2 = new JButton("Mark as Finished");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if (taskJList.getSelectedValue() != null) {
					
					String sessionName = taskJList.getSelectedValue().name;
					
					SessionManager sessionManager = new SessionManager();
					sessionManager.deleteSingleSession(thisFrame, sessionName, taskJList.getSelectedValue().id);
					
					TaskManager taskManager = new TaskManager();
					taskManager.taskMinusOneSession(sessionName);
					
					refreshSchedule();
				} else {
					JLabel modifyMessage = new JLabel("<html>Please select a session before marking as finished.</html>", SwingConstants.CENTER);
			    	modifyMessage.setFont(standardFont);
					JOptionPane.showMessageDialog(null, modifyMessage, "No Task Selected",JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnNewButton_2.setFont(standardFont);
		btnNewButton_2.setBounds(0, 703, 382, 25);
		contentPane.add(btnNewButton_2);
		
		shouldLoadAddText();
		
		loadSchedule();
		
		JLabel lblNewLabel_1 = new JLabel(dateShown.toString());
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(standardFont);
		lblNewLabel_1.setBounds(95, 4, 192, 20);
		contentPane.add(lblNewLabel_1);
		
		JButton btnNewButton = new JButton(">");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dateShown = dateShown.plusDays(1);
				refreshSchedule();
				lblNewLabel_1.setText(dateShown.toString());
			}
		});
		btnNewButton.setFont(standardFont);
		btnNewButton.setBounds(285, 2, 53, 26);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("<");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dateShown = dateShown.minusDays(1);
				refreshSchedule();
				lblNewLabel_1.setText(dateShown.toString());
			}
		});
		btnNewButton_1.setFont(standardFont);
		btnNewButton_1.setBounds(40, 2, 53, 26);
		contentPane.add(btnNewButton_1);
		
		loadTimer();
	}
	
	// secondary functions with checks
	public void loadSchedule() {
		loadData();
		
		taskJList.setCellRenderer(new TaskListCellRenderer());
		// sets the size of the schedule
		taskJList.setBounds(6, 30, 370, 667);
		contentPane.add(taskJList);
	}
	
	public void refreshSchedule() {
		listModel.clear();
		
		loadData();
        
        taskJList.setModel(listModel);
	}
	
	private void loadData() {
		
		JSONParser parser = new JSONParser();
		
		JSONArray sessionArray = new JSONArray();
		
		try {
			sessionArray = (JSONArray) parser.parse(new FileReader(sessionPath));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
        for (Object session: sessionArray)
        {
          JSONObject jsonTask = (JSONObject) session;
          
          LocalDate taskDate = LocalDate.parse((String) jsonTask.get("date"));
          
          if (taskDate.equals(dateShown)) {
        	  
        	  String name = (String) jsonTask.get("name");
              String startTime = (String) jsonTask.get("startTime");
              String endTime = (String) jsonTask.get("endTime");
              Integer id = Math.toIntExact((long) jsonTask.get("id"));
              String time = startTime + " - " + endTime;
              TaskForList taskEvent = new TaskForList(name, time, id);
              listModel.addElement(taskEvent);
          }
        }
        
        arrayForTimer = sessionArray;
	}
	
	//set ups notifications
	private void loadTimer() {
		
		SessionForTimer sessionForTimer;
		
		for (Object session: arrayForTimer) {
			JSONObject jsonTask = (JSONObject) session;
			
			LocalDate taskDate = LocalDate.parse((String) jsonTask.get("date"));
			LocalTime startTime = LocalTime.parse((String) jsonTask.get("startTime"));
			LocalTime endTime = LocalTime.parse((String) jsonTask.get("endTime"));
			
			boolean startsNowOrAFewMinutesBefore = false;
			
			if (startTime.isAfter(LocalTime.now())) {
				
				startsNowOrAFewMinutesBefore = true;
				
			} else { //if session starts before current time
				
				Duration howMuchTimeBefore = Duration.between(startTime, LocalTime.now());
				long howManyMinutesBefore = howMuchTimeBefore.toMinutes();
				
				if (howManyMinutesBefore < 5) {
					
					startsNowOrAFewMinutesBefore = true;
				}
			}
			
			// a notification will only play if start time is not before now
			if (taskDate.equals(LocalDate.now()) && startsNowOrAFewMinutesBefore) {
				sessionForTimer = new SessionForTimer((String) jsonTask.get("name"), startTime, endTime);
				arrayListForTime.add(sessionForTimer);
			}
		}
		
		try {
			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			Timer timer = new Timer();
			
			for (SessionForTimer i: arrayListForTime) {
				
				//changes localtime to date
				Date scheduledWorkTime = dateFormatter.parse(LocalDate.now() + " " + i.startTime.toString());
				Date scheduledBreakTime = dateFormatter.parse(LocalDate.now() + " " + i.endTime.toString());
				
				timer.schedule(new Notification(i.name), scheduledWorkTime);
				timer.schedule(new BreakNotification(), scheduledBreakTime);
			}
			
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void shouldLoadAddText() {
		SessionManager sessionManager = new SessionManager();
		
		if (sessionManager.checkIfEmpty()) {
			
			JLabel lblNewLabel = new JLabel("Click Add to add a task");
			lblNewLabel.setFont(standardFont);
			lblNewLabel.setBounds(120, 340, 165, 30);
			contentPane.add(lblNewLabel);
		}
	}
	
	// only for AddMenu
	public void removeText() {
		// get the components in the panel
		Component[] componentList = contentPane.getComponents();

		// loop through the components
		for(Component c : componentList){

		    // find Jlabel with certain text and remove it
		    if(c instanceof JLabel){
		    	
		    	if (((JLabel) c).getText().equals("Click Add to add a task")) {
		    		contentPane.remove(c);
		    	}
		    }
		}
	}
	
	// updates MainMenu when ListCellRenderer is added
	public void refresh() {
		this.validate();
		this.repaint();
	}
}
