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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class MainMenu extends JFrame {

	private JPanel contentPane;
	
	DefaultListModel<Task> model = new DefaultListModel<>();
	JList<Task> taskJList = new JList<>(model);
	
	String sessionPath = System.getProperty("user.home") + File.separator + ".overseer" + File.separator + "session.json";
	
	MainMenu thisFrame = this;
	
	LocalDate dateShown = LocalDate.now();
	
	JSONArray arrayForTimer;
	
	ArrayList<SessionForTimer> arrayListForTime = new ArrayList<SessionForTimer>();
	
	boolean sessionExists;

	public MainMenu() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// checks if folder for program exists
		DirectoryChecker directoryChecker = new DirectoryChecker();
		sessionExists = directoryChecker.doesSessionExists();
	
		JButton addTask = new JButton("Add");
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
		modifyTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (taskJList.getSelectedValue() != null) {
					ModifyMenu modifyTask = new ModifyMenu(thisFrame, taskJList.getSelectedValue().name);
					 modifyTask.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					 modifyTask.setTitle("Modify Task");
					 modifyTask.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "Please select a task before modifying.","No Task Selected", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		modifyTask.setBounds(95, 728, 97, 25);
		contentPane.add(modifyTask);
		
		JButton deleteTask = new JButton("Delete");
		deleteTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//needed
				//loads tasks from disk so they can be deleted
				if (taskJList.getSelectedValue() != null) {
					TaskManager taskManager = new TaskManager();
					
					taskManager.deleteTask(taskJList.getSelectedValue().name);
					
					refreshSchedule();
				} else {
					JOptionPane.showMessageDialog(null, "Please select a task before deleting.","No Task Selected", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		deleteTask.setBounds(190, 728, 97, 25);
		contentPane.add(deleteTask);
		
		JButton settingButton = new JButton("Settings");
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
		
		checkAndLoadSchedule();
		
		//must go after schedule is loaded
		JLabel lblNewLabel = new JLabel("Click Add to add a task");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(120, 340, 165, 30);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel(dateShown.toString());
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(95, 0, 192, 20);
		contentPane.add(lblNewLabel_1);
		
		JButton btnNewButton = new JButton(">");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dateShown = dateShown.plusDays(1);
				checkAndRefreshSchedule();
				lblNewLabel_1.setText(dateShown.toString());
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnNewButton.setBounds(285, -1, 53, 25);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("<");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dateShown = dateShown.minusDays(1);
				checkAndRefreshSchedule();
				lblNewLabel_1.setText(dateShown.toString());
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnNewButton_1.setBounds(40, -1, 53, 25);
		contentPane.add(btnNewButton_1);
		
		if (sessionExists == false) {
			modifyTask.setEnabled(false);
			deleteTask.setEnabled(false);
		}
		
		checkAndLoadTimer();
	}
	
	// secondary functions with checks
	public void checkAndLoadSchedule() {
		if (new File(sessionPath).isFile() == true) {
			
			loadData();
		
			taskJList.setCellRenderer(new TaskListCellRenderer());
			taskJList.setBounds(0, 40, 380, 680);
			contentPane.add(taskJList);
		}
	}
	
	public void checkAndRefreshSchedule() {
		if (new File(sessionPath).isFile() == true) {
			refreshSchedule();
		}
	}
	
	public void checkAndLoadTimer() {
		if (new File(sessionPath).isFile() == true) {
			loadTimer();
		}
	}
	
	public void refreshSchedule() {
		model.clear();
		
		loadData();
        
        taskJList.setModel(model);
	}
	
	public void loadData() {
		
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
              String time = startTime + " - " + endTime;
              Task taskEvent = new Task(name, time);
              model.addElement(taskEvent);
          }
        }
        
        arrayForTimer = sessionArray;
	}
	
	public void loadTimer() {
		
		SessionForTimer sessionForTimer;
		
		for (Object session: arrayForTimer) {
			JSONObject jsonTask = (JSONObject) session;
			
			LocalDate taskDate = LocalDate.parse((String) jsonTask.get("date"));
			LocalTime startTime = LocalTime.parse((String) jsonTask.get("startTime"));
			LocalTime endTime = LocalTime.parse((String) jsonTask.get("endTime"));
			
			if (taskDate.equals(LocalDate.now())) {
				sessionForTimer = new SessionForTimer((String) jsonTask.get("name"), startTime, endTime);
				arrayListForTime.add(sessionForTimer);
			}
		}
		
		try {
			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			Timer timer = new Timer();
			
			for (SessionForTimer i: arrayListForTime) {
				
				Date scheduledWorkTime = dateFormatter.parse(LocalDate.now() + " " + i.startTime.toString());
				Date scheduledBreakTime = dateFormatter.parse(LocalDate.now() + " " + i.endTime.toString());
				
				timer.schedule(new Notification(i.name), scheduledWorkTime);
				timer.schedule(new BreakNotification(), scheduledBreakTime);
			}
			
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
	}
}
