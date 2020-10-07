package com.joshua.overseer;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Scheduler2 {
	
	// get current date and time
	LocalTime currentTime = LocalTime.now();
	LocalDate today = LocalDate.now();
	LocalDate tomorrow = today.plusDays(1);

	SessionManager sessionManager = new SessionManager();
	// needed to fetch settings
	ConfigManager configManager = new ConfigManager();
	
	// fetch setting data
	LocalTime configWorkStartTime = LocalTime.parse(configManager.getStartTime());
	
	LocalTime previousSessionEnds = configWorkStartTime;
	LocalTime lastSessionEnds;
	
	public Scheduler2() {
	}
	
	public void recompute() {
		
	}
	
	public void add(String name, LocalDate deadline, LocalTime timeRequired) {
		
		// fetch setting data
		Integer configCombinedMinutes = configManager.getCombinedTimeLength();
		Integer configBreakMinutes = configManager.getBreakTimeLength();
		Integer configWorkMinutes = configManager.getWorkTimeLength();
		
		// fetch more setting data
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
		
		// if task is due tomorrow, print warning
		checkIfDeadlineIsTomorrow(deadline, oneSessionBeforeWorkEnds, durationRequiredWithBreakInBetween, remainderOfWorkPeriodInMin);
		
		// create array
	    ArrayList<Session> sessionArray = new ArrayList<Session>();
	    
	    Integer sessionNumber = 0;
	    
	    Session newSession;
	    
	    while (remainderOfWorkPeriodInMin > configCombinedMinutes && durationRequiredInMinutes > 0) {
	    	
	    	/*LocalTime availableTime;
	    	
	    	if (lastSessionEnds.isAfter(currentTime)) {
	    		availableTime = lastSessionEnds.plusMinutes(5);
	    	} else {
	    		availableTime = currentTime;
	    	}
	    	
	    	// set session start and end times
	    	LocalTime sessionStartTime = availableTime.plusMinutes(configCombinedMinutes * sessionNumber);
	    	LocalTime sessionEndTime = sessionStartTime.plusMinutes(configWorkMinutes);
	    	
	    	newSession = new Session(name, sessionStartTime, sessionEndTime, today);
	    	sessionArray.add(newSession);
	    	
	    	durationRequiredInMinutes = durationRequiredInMinutes - configWorkMinutes;
	    	remainderOfWorkPeriodInMin = remainderOfWorkPeriodInMin - ((sessionNumber + 1) * configCombinedMinutes);
	    	sessionNumber++;*/
	    	
	    	//gets ALL sessions
    		ArrayList<Session> sessionArrayList = sessionManager.getSessions();
	    	
	    	// spreads sessions across multiple days
	    	for (LocalDate date = LocalDate.now(); !(date.equals(deadline)); date = date.plusDays(1)) {
	    		
	    		for (Session session:sessionArrayList) {
	    			
	    			if (session.date.equals(date)) {
	    				
	    				if (session.endTime.isAfter(previousSessionEnds)) {
	    					
		    				lastSessionEnds = session.endTime;
		    				previousSessionEnds = lastSessionEnds;
		    			}
	    			}
	    		}
	    		
	    		// only runs the day the task is added
	    		if (date.equals(LocalDate.now())) {
	    			if (lastSessionEnds.isAfter(currentTime)) {
			    		LocalTime availableTime = lastSessionEnds.plusMinutes(5);
			    	} else {
			    		LocalTime availableTime = currentTime;
			    	}
	    		}
	    		
	    		newSession = new Session(name, sessionStartTime, sessionEndTime, date);
	    		
	    		// add session to array
		    	sessionArray.add(newSession);
	    	}
	    }

	    sessionManager.addSessions(sessionArray);
	}
	
	public void checkIfDeadlineIsTomorrow(LocalDate deadline, LocalTime oneSessionBeforeWorkEnds, Integer durationRequiredWithBreakInBetween, Integer remainderOfWorkPeriodInMin) {
		if (deadline == tomorrow) {
		    
		    if (currentTime.isAfter(oneSessionBeforeWorkEnds) || durationRequiredWithBreakInBetween > remainderOfWorkPeriodInMin) {
		    	JOptionPane.showMessageDialog(null, "You may not be able to finish on time. You should ask for an extension.","Schedule Conflicts", JOptionPane.WARNING_MESSAGE);
		    }
		}
	}
}
