package com.joshua.overseer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.Font;

public class Settings extends JDialog {

	private final JPanel contentPanel = new JPanel();
	
	int breakSpinnerValue;
	int workSpinnerValue;

	public Settings(MainMenu parentFrame) throws IOException {
		
		ConfigManager configManager = new ConfigManager();
		
		setBounds(100, 100, 450, 600);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		// the labels
		JLabel lblNewLabel = new JLabel("Break time length:");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		lblNewLabel.setBounds(12, 155, 134, 19);
		contentPanel.add(lblNewLabel);
			
		JLabel lblNewLabel_1 = new JLabel("minutes");
		lblNewLabel_1.setFont(new Font("Arial", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(215, 156, 63, 17);
		contentPanel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Allocated Time:");
		lblNewLabel_2.setFont(new Font("Arial", Font.PLAIN, 16));
		lblNewLabel_2.setBounds(12, 13, 114, 19);
		contentPanel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("From:");
		lblNewLabel_3.setFont(new Font("Arial", Font.PLAIN, 16));
		lblNewLabel_3.setBounds(12, 45, 56, 19);
		contentPanel.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("To:");
		lblNewLabel_4.setFont(new Font("Arial", Font.PLAIN, 16));
		lblNewLabel_4.setBounds(12, 79, 45, 16);
		contentPanel.add(lblNewLabel_4);
		
		JLabel lblNewLabel_6 = new JLabel("Work Time Length:");
		lblNewLabel_6.setFont(new Font("Arial", Font.PLAIN, 16));
		lblNewLabel_6.setBounds(12, 123, 134, 19);
		contentPanel.add(lblNewLabel_6);
		
		JLabel lblNewLabel_7 = new JLabel("minutes");
		lblNewLabel_7.setFont(new Font("Arial", Font.PLAIN, 16));
		lblNewLabel_7.setBounds(216, 124, 63, 16);
		contentPanel.add(lblNewLabel_7);
		
		//break time spinner
		{
		//sets minimum and maximum values
			int min = 1;
			int max = 60;
			int step = 1;
			int i = configManager.getBreakTimeLength();
			SpinnerModel breakValue = new SpinnerNumberModel(i, min, max, step);
			JSpinner breakSpinner = new JSpinner(breakValue);
			breakSpinner.setBounds(158, 154, 45, 22);
			breakSpinner.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent arg0) {
					breakSpinnerValue = (Integer) breakSpinner.getValue();
				}
			});
			contentPanel.add(breakSpinner);
		}
		
		int min = 1;
		int max = 60;
		int step = 1;
		int i = configManager.getWorkTimeLength();
		SpinnerModel workValue = new SpinnerNumberModel(i, min, max, step);
		JSpinner workSpinner = new JSpinner(workValue);
		workSpinner.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				workSpinnerValue = (Integer) workSpinner.getValue();
			}
		});
		workSpinner.setBounds(159, 122, 45, 22);
		contentPanel.add(workSpinner);
		
		//spinners for allocated work time
		JSpinner startTimeSpinner = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor startTimeEditor = new JSpinner.DateEditor(startTimeSpinner, "HH:mm");
		startTimeSpinner.setEditor(startTimeEditor);
		SimpleDateFormat startTime = new SimpleDateFormat("HH:mm");
		try {
			startTimeSpinner.setValue(startTime.parseObject(configManager.getStartTime()));
		} catch (ParseException startTimeError) {
			startTimeError.printStackTrace();
		}
		startTimeSpinner.setBounds(66, 45, 60, 22);
		contentPanel.add(startTimeSpinner, "HH:mm");
		
		JSpinner endTimeSpinner = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor endTimeEditor = new JSpinner.DateEditor(endTimeSpinner, "HH:mm");
		endTimeSpinner.setEditor(endTimeEditor);
		SimpleDateFormat endTime = new SimpleDateFormat("HH:mm");
		try {
			endTimeSpinner.setValue(endTime.parseObject(configManager.getEndTime()));
		} catch (ParseException endTimeError) {
			endTimeError.printStackTrace();
		}
		endTimeSpinner.setBounds(66, 77, 60, 22);
		contentPanel.add(endTimeSpinner);
		
		//the buttons
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						Object startObj = startTimeSpinner.getValue();
						LocalTime startTime = LocalTime.now();
						if (startObj instanceof java.util.Date) {
						    java.util.Date theDate = (java.util.Date) startObj;
						    java.time.Instant inst = theDate.toInstant();
						    java.time.ZoneId theZone = java.time.ZoneId.systemDefault();
						    startTime = LocalTime.from(ZonedDateTime.ofInstant(inst, theZone));
						}
						
						Object endObj = endTimeSpinner.getValue();
						LocalTime endTime = LocalTime.now();
						if (endObj instanceof java.util.Date) {
						    java.util.Date theDate = (java.util.Date) endObj;
						    java.time.Instant inst = theDate.toInstant();
						    java.time.ZoneId theZone = java.time.ZoneId.systemDefault();
						    endTime = LocalTime.from(ZonedDateTime.ofInstant(inst, theZone));
						}
						
						try {
							configManager.saveData(breakSpinnerValue, workSpinnerValue, startTime, endTime);
							configManager.saveProperties();
						} catch (IOException saveError) {
							saveError.printStackTrace();
						}
						
						Scheduler3 scheduler = new Scheduler3();
						scheduler.recompute();
						
						parentFrame.checkAndRefreshSchedule();
						
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
