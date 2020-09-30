package com.joshua.overseer;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Scheduler {

	public Scheduler() {
		SessionManager sessionManager = new SessionManager();
		ArrayList<Session> sessionArrayList = sessionManager.getSessions();
	}
	
	public void recompute() {
		
	}
	
	public void add(String name, LocalDate deadline, LocalTime timeRequired) {
		
		// needed to fetch settings
		ConfigManager configManager = new ConfigManager();
		
		// get current date and time
		LocalTime currentTime = LocalTime.now();
		LocalDate today = LocalDate.now();
		LocalDate tomorrow = today.plusDays(1);
		
		// fetch setting data
		Integer configCombinedMinutes = configManager.getCombinedTimeLength();
		Integer configBreakMinutes = configManager.getBreakTimeLength();
		Integer configWorkMinutes = configManager.getWorkTimeLength();
		
		// fetch more setting data
		LocalTime configWorkStartTime = LocalTime.parse(configManager.getStartTime());
		LocalTime configWorkEndTime = LocalTime.parse(configManager.getEndTime());
		LocalTime oneSessionBeforeWorkEnds = configWorkEndTime.minusMinutes(configWorkMinutes);
		
		// convert timeRequired from LocalTime to Duration
		Duration hoursRequired = Duration.ofHours(timeRequired.getHour());
		int minutesRequired = timeRequired.getMinute();
		Integer durationRequiredInMinutes = (int) hoursRequired.toMinutes() + minutesRequired;
		
		// calculate number of sessions needed to finish task
		Float numberOfSessionsWithDecimal = (float) durationRequiredInMinutes / (float) configWorkMinutes;
		Integer numberOfSessions = (int) Math.ceil(numberOfSessionsWithDecimal);
		
		// number of work sessions should equal number of breaks
	    Integer durationRequiredWithBreakInBetween = durationRequiredInMinutes + configBreakMinutes * numberOfSessions;
	    
	    // how much time until work period ends
	    Duration remainderOfWorkPeriod = Duration.between(currentTime, configWorkEndTime);
	    Integer remainderOfWorkPeriodInMin = (int) remainderOfWorkPeriod.toMinutes();
		
		// if task is due tomorrow
		if (deadline == tomorrow) {
		    
		    if (currentTime.isAfter(oneSessionBeforeWorkEnds) || durationRequiredWithBreakInBetween > remainderOfWorkPeriodInMin) {
		    	JOptionPane.showMessageDialog(null, "You may not be able to finish on time. You should ask for an extension.","Schedule Conflicts", JOptionPane.WARNING_MESSAGE);
		    }
		    // the above code is finished for now
		}
		
		// create array
	    ArrayList<Session> sessionArray = new ArrayList<Session>();
	    
	    Integer sessionNumber = 0;
	    
	    Session newSession;
	    
	    while (remainderOfWorkPeriodInMin > configCombinedMinutes && durationRequiredInMinutes > 0) {
	    	
	    	// set session start and end times
	    	LocalTime sessionStartTime = currentTime.plusMinutes(configCombinedMinutes * sessionNumber);
	    	LocalTime sessionEndTime = sessionStartTime.plusMinutes(configWorkMinutes);
	    	
	    	newSession = new Session(name, sessionStartTime, sessionEndTime, today);
	    	sessionArray.add(newSession);
	    	
	    	durationRequiredInMinutes = durationRequiredInMinutes - configWorkMinutes;
	    	remainderOfWorkPeriodInMin = remainderOfWorkPeriodInMin - ((sessionNumber + 1) * configCombinedMinutes);
	    	sessionNumber++;
	    }

	    SessionManager sessionManager = new SessionManager();
	    sessionManager.addSessions(sessionArray);
	}

}
