package com.joshua.overseer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.JSpinner;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.awt.event.ActionEvent;

import com.github.lgooddatepicker.components.*;
import java.awt.Font;
import java.awt.Color;

public class AddMenu extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField taskName;

	public AddMenu(MainMenu parentJFrame) {
		TaskManager taskManager = new TaskManager();
		
		setBounds(100, 100, 450, 600);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(240, 255, 243));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Name:");
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
			lblNewLabel.setBounds(24, 13, 60, 16);
			contentPanel.add(lblNewLabel);
		}
		{
			taskName = new JTextField();
			taskName.setFont(new Font("Tahoma", Font.PLAIN, 16));
			taskName.setBounds(98, 4, 304, 34);
			contentPanel.add(taskName);
			taskName.setColumns(10);
		}
		
		JLabel lblNewLabel_1 = new JLabel("Time Required:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(24, 58, 115, 16);
		contentPanel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_4 = new JLabel("Deadline:");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_4.setBounds(24, 108, 67, 16);
		contentPanel.add(lblNewLabel_4);
		
		JSpinner timeSpinner = new JSpinner(new SpinnerDateModel());
		timeSpinner.setFont(new Font("Tahoma", Font.PLAIN, 16));
		JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
		timeSpinner.setEditor(timeEditor);
		SimpleDateFormat time = new SimpleDateFormat("HH:mm");
		try {
			timeSpinner.setValue(time.parseObject("1:00"));
		} catch (ParseException timeError) {
			timeError.printStackTrace();
		}
		
		timeSpinner.setBounds(151, 53, 73, 25);
		contentPanel.add(timeSpinner);
		
		DatePickerSettings dateSettings = new DatePickerSettings();
        TimePickerSettings timeSettings = new TimePickerSettings();
        dateSettings.setAllowEmptyDates(false);
        timeSettings.setAllowEmptyTimes(false);
		DateTimePicker deadlinePicker = new DateTimePicker(dateSettings, timeSettings);
		deadlinePicker.getTimePicker().getComponentTimeTextField().setFont(new Font("Tahoma", Font.PLAIN, 16));
		deadlinePicker.getDatePicker().getComponentDateTextField().setFont(new Font("Tahoma", Font.PLAIN, 16));
		deadlinePicker.setBounds(102, 101, 300, 34);
		contentPanel.add(deadlinePicker);
		
		// the buttons
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(new Color(240, 255, 243));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton addButton = new JButton("Add Task");
				addButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						// converts value of spinner to LocalTime
						Object obj = timeSpinner.getValue();
						LocalTime length = LocalTime.now();
						if (obj instanceof java.util.Date) {
						    java.util.Date theDate = (java.util.Date) obj;
						    java.time.Instant inst = theDate.toInstant();
						    java.time.ZoneId theZone = java.time.ZoneId.systemDefault();
						    length = LocalTime.from(ZonedDateTime.ofInstant(inst, theZone));
						}
						
						// localTime is automatically set to midnight if not chosen
						taskManager.addTask(taskName.getText(), length, deadlinePicker.getDateTimePermissive());
						// refreshes schedule in parent JFrame
						parentJFrame.checkAndLoadSchedule();
						parentJFrame.checkAndLoadTimer();
						parentJFrame.refresh();
						dispose();
					}
				});
				addButton.setActionCommand("OK");
				buttonPane.add(addButton);
				getRootPane().setDefaultButton(addButton);
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
