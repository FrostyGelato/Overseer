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
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.JSpinner;

public class ModifyMenu extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField nameField;
	
	TaskManager taskManager = new TaskManager();

	public ModifyMenu(MainMenu parentJFrame, String taskName) {
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Name:");
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
			lblNewLabel.setBounds(12, 13, 56, 16);
			contentPanel.add(lblNewLabel);
		}
		
		nameField = new JTextField();
		nameField.setBounds(70, 8, 197, 28);
		nameField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		nameField.setText(taskName);
		contentPanel.add(nameField);
		nameField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Time Required:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(12, 59, 115, 20);
		contentPanel.add(lblNewLabel_1);
		
		JSpinner timeRequiredSpinner = new JSpinner(new SpinnerDateModel());
		timeRequiredSpinner.setFont(new Font("Tahoma", Font.PLAIN, 16));
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
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_2.setBounds(12, 109, 76, 16);
		contentPanel.add(lblNewLabel_2);
		
		DatePickerSettings dateSettings = new DatePickerSettings();
        TimePickerSettings timeSettings = new TimePickerSettings();
        dateSettings.setAllowEmptyDates(false);
        timeSettings.setAllowEmptyTimes(false);
		DateTimePicker deadlinePicker = new DateTimePicker(dateSettings, timeSettings);
		deadlinePicker.getTimePicker().getComponentTimeTextField().setFont(new Font("Tahoma", Font.PLAIN, 16));
		deadlinePicker.getDatePicker().getComponentDateTextField().setFont(new Font("Tahoma", Font.PLAIN, 16));
		deadlinePicker.setBounds(90, 101, 300, 34);
		contentPanel.add(deadlinePicker);
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
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
						
						taskManager.modifyTask(nameField.getText(), length, deadlinePicker.getDateTimePermissive());
						
						Scheduler scheduler = new Scheduler();
						scheduler.recompute();
						
						parentJFrame.refreshSchedule();
						
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
