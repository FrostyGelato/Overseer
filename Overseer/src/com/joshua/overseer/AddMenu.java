package com.joshua.overseer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.JSpinner;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.awt.event.ActionEvent;

import com.github.lgooddatepicker.components.*;
import java.awt.Font;
import java.awt.Color;

public class AddMenu extends JDialog {

	private final JPanel contentPanel = new JPanel();
	
	private JTextField taskName;
	
	Font standardFont = new Font("Tahoma", Font.PLAIN, 16);

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
			lblNewLabel.setFont(standardFont);
			lblNewLabel.setBounds(24, 13, 60, 16);
			contentPanel.add(lblNewLabel);
		}
		{
			taskName = new JTextField();
			taskName.setFont(standardFont);
			taskName.setBounds(98, 4, 304, 34);
			contentPanel.add(taskName);
			taskName.setColumns(10);
		}
		
		JLabel lblNewLabel_1 = new JLabel("Time Required:");
		lblNewLabel_1.setFont(standardFont);
		lblNewLabel_1.setBounds(24, 58, 115, 16);
		contentPanel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_4 = new JLabel("Deadline:");
		lblNewLabel_4.setFont(standardFont);
		lblNewLabel_4.setBounds(24, 108, 67, 16);
		contentPanel.add(lblNewLabel_4);
		
		JSpinner timeSpinner = new JSpinner(new SpinnerDateModel());
		timeSpinner.setFont(standardFont);
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
        
        dateSettings.setFontMonthAndYearMenuLabels(standardFont);
        dateSettings.setFontCalendarDateLabels(standardFont);
        dateSettings.setFontValidDate(standardFont);
        dateSettings.setFontTodayLabel(standardFont);
        
		DateTimePicker deadlinePicker = new DateTimePicker(dateSettings, timeSettings);
		deadlinePicker.getTimePicker().getComponentTimeTextField().setFont(standardFont);
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
				addButton.setFont(standardFont);
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
						
						LocalDateTime deadline = deadlinePicker.getDateTimePermissive();
						
						TimeChecker timeChecker = new TimeChecker();
						
						if (deadline.isBefore(LocalDateTime.now())) {
							
							JLabel message = new JLabel("<html>The deadline date cannot be prior to today.<br/>Please select a later date.</html>", SwingConstants.CENTER);
					    	message.setFont(standardFont);
							JOptionPane.showMessageDialog(null, message, "Illegal Date",JOptionPane.WARNING_MESSAGE);
							
						} else if (timeChecker.checkIfEnoughTime(deadline.toLocalDate(), length) == false){
							
							JLabel label = new JLabel(
									"<html>You may not be able to finish on time.<br/>You should ask for an extension.<br/>The task will not be added.</html>",
									SwingConstants.CENTER);
							label.setFont(standardFont);
							JOptionPane.showMessageDialog(null, label, "Schedule Conflicts", JOptionPane.WARNING_MESSAGE);
							
						} else {
							
							// localTime is automatically set to midnight if not chosen
							taskManager.addTask(taskName.getText(), length, deadline);
							
							// refreshes schedule in parent JFrame
							parentJFrame.removeText();
							parentJFrame.refreshSchedule();
							parentJFrame.refresh();
							
							dispose();
						}					
					}
				});
				addButton.setActionCommand("OK");
				buttonPane.add(addButton);
				getRootPane().setDefaultButton(addButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setFont(standardFont);
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