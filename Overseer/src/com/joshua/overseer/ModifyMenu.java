package com.joshua.overseer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;

import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.JSpinner;
import java.awt.Color;

public class ModifyMenu extends JDialog {

	private final JPanel contentPanel = new JPanel();
	
	Font standardFont = new Font("Tahoma", Font.PLAIN, 16);
	
	TaskManager taskManager = new TaskManager();

	public ModifyMenu(MainMenu parentJFrame, String taskName) {
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(240, 255, 243));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Name:");
			lblNewLabel.setFont(standardFont);
			lblNewLabel.setBounds(12, 13, 56, 16);
			contentPanel.add(lblNewLabel);
		}
		
		JLabel nameLabel = new JLabel(taskName);
		nameLabel.setFont(standardFont);
		nameLabel.setBounds(67, 7, 197, 28);
		contentPanel.add(nameLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Time Required:");
		lblNewLabel_1.setFont(standardFont);
		lblNewLabel_1.setBounds(12, 59, 115, 20);
		contentPanel.add(lblNewLabel_1);
		
		JSpinner timeRequiredSpinner = new JSpinner(new SpinnerDateModel());
		timeRequiredSpinner.setFont(standardFont);
		JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeRequiredSpinner, "HH:mm");
		timeRequiredSpinner.setEditor(timeEditor);
		SimpleDateFormat time = new SimpleDateFormat("HH:mm");
		try {
			timeRequiredSpinner.setValue(time.parseObject(taskManager.getTimeRequired(taskName)));
		} catch (ParseException timeError) {
			timeError.printStackTrace();
		}
		
		timeRequiredSpinner.setBounds(139, 56, 76, 28);
		contentPanel.add(timeRequiredSpinner);
		
		JLabel lblNewLabel_2 = new JLabel("Deadline:");
		lblNewLabel_2.setFont(standardFont);
		lblNewLabel_2.setBounds(12, 109, 76, 16);
		contentPanel.add(lblNewLabel_2);
		
		DatePickerSettings dateSettings = new DatePickerSettings();
        TimePickerSettings timeSettings = new TimePickerSettings();
        dateSettings.setAllowEmptyDates(false);
        timeSettings.setAllowEmptyTimes(false);
        
        dateSettings.setFontMonthAndYearMenuLabels(standardFont);
        dateSettings.setFontCalendarDateLabels(standardFont);
        dateSettings.setFontValidDate(standardFont);
        dateSettings.setFontTodayLabel(standardFont);
        
		DateTimePicker deadlinePicker = new DateTimePicker(dateSettings, timeSettings);
		deadlinePicker.setDateTimeStrict(taskManager.getDeadline(taskName));
		deadlinePicker.getTimePicker().getComponentTimeTextField().setFont(standardFont);
		deadlinePicker.setBounds(90, 101, 300, 34);
		contentPanel.add(deadlinePicker);
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(new Color(240, 255, 243));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setFont(standardFont);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						
						Object obj = timeRequiredSpinner.getValue();
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
							
							JLabel message = new JLabel("<html>The deadline date cannot be set in the past.<br/>Please select a later date.</html>", SwingConstants.CENTER);
					    	message.setFont(standardFont);
							JOptionPane.showMessageDialog(null, message, "Illegal Time",JOptionPane.WARNING_MESSAGE);
							
						} else if (timeChecker.checkIfEnoughTime(deadline.toLocalDate(), length) == false){
							
							JLabel label = new JLabel(
									"<html>You may not be able to finish on time.<br/>You should ask for an extension.<br/>The task will not be added.</html>",
									SwingConstants.CENTER);
							label.setFont(standardFont);
							JOptionPane.showMessageDialog(null, label, "Schedule Conflicts", JOptionPane.WARNING_MESSAGE);
							
						} else {
							
							taskManager.modifyTask(taskName, length, deadlinePicker.getDateTimePermissive());
							
							Scheduler3 scheduler = new Scheduler3();
							scheduler.recompute();
							
							parentJFrame.refreshSchedule();
							
							dispose();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
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
