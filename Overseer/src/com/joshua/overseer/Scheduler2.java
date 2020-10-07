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
	Integer configCombinedMinutes = configManager.getCombinedTimeLength();
	Integer configBreakMinutes = configManager.getBreakTimeLength();
	Integer configWorkMinutes = configManager.getWorkTimeLength();
	
	LocalTime configWorkStartTime = LocalTime.parse(configManager.getStartTime());
	LocalTime configWorkEndTime = LocalTime.parse(configManager.getEndTime());
	
	LocalTime previousSessionEnds = configWorkStartTime;
	LocalTime lastSessionEnds = previousSessionEnds;
	
	public Scheduler2() {
	}
	
	public void recompute() {
		
	}
	
	public void add(String name, LocalDate deadline, LocalTime timeRequired) {
		
		// convert timeRequired from LocalTime to Duration
		Duration hoursRequired = Duration.ofHours(timeRequired.getHour());
		int minutesRequired = timeRequired.getMinute();
		Integer durationRequiredInMinutes = (int) hoursRequired.toMinutes() + minutesRequired;
		
		// create array
	    ArrayList<Session> sessionArray = new ArrayList<Session>();
	    
	    Session newSession;
	    
	    LocalDate date = today;
	    
	    // allocates sessions
	    while (durationRequiredInMinutes > 0) {
	    	
	    	//gets ALL sessions
    		ArrayList<Session> sessionArrayList = sessionManager.getSessions();
    		
    		Integer sessionNumber = 0;
	    	
	    	// spreads sessions across multiple days
    		if (!(date.equals(deadline))) {
	    		
	    		for (Session session:sessionArrayList) {
	    			
	    			if (session.date.equals(date)) {
	    				
	    				if (session.endTime.isAfter(previousSessionEnds)) {
	    					
		    				lastSessionEnds = session.endTime;
		    				previousSessionEnds = lastSessionEnds;
		    			}
	    			}
	    		}
	    		
	    		LocalTime availableTime;
	    		
	    		Duration remainderOfWorkPeriod;
	    		
	    		// only runs the day the task is added
	    		if (date.equals(today)) {
	    			if (lastSessionEnds.isAfter(currentTime)) {
			    		availableTime = lastSessionEnds.plusMinutes(5);
			    	} else {
			    		availableTime = currentTime;
			    	}
	    			
	    			// how much time until work period ends
	    			remainderOfWorkPeriod = Duration.between(currentTime, configWorkEndTime);
	    		    
	    		} else {
					availableTime = lastSessionEnds.plusMinutes(5);
					
					remainderOfWorkPeriod = Duration.between(availableTime, configWorkEndTime);
				}
	    		
	    		Integer remainderOfWorkPeriodInMin = (int) remainderOfWorkPeriod.toMinutes();
	    		
	    		// if task is due tomorrow, print warning
	    		checkIfDeadlineIsTomorrow(date, deadline, remainderOfWorkPeriodInMin, durationRequiredInMinutes);
	    		
	    		// only add session if there is empty time that day
	    		if (remainderOfWorkPeriodInMin > configCombinedMinutes) {
	    			// set session start and end times
			    	LocalTime sessionStartTime = availableTime.plusMinutes(configCombinedMinutes * sessionNumber);
			    	LocalTime sessionEndTime = sessionStartTime.plusMinutes(configWorkMinutes);
		    		
			    	// create new session
		    		newSession = new Session(name, sessionStartTime, sessionEndTime, date);
		    		
		    		// add session to array
			    	sessionArray.add(newSession);
			    	
			    	durationRequiredInMinutes = durationRequiredInMinutes - configWorkMinutes;
			    	remainderOfWorkPeriodInMin = remainderOfWorkPeriodInMin - ((sessionNumber + 1) * configCombinedMinutes);
			    	sessionNumber++;
	    		}
	    		
	    		// reset to today after cycling through all days between today and deadline
	    		if (date.equals(deadline.minusDays(1)) && durationRequiredInMinutes > 0) {
	    			date = today;
	    		}
	    		
	    		date = date.plusDays(1);
	    	}
	    }

	    sessionManager.addSessions(sessionArray);
	}
	
	// methods for use in other methods
	public void checkIfDeadlineIsTomorrow(LocalDate date, LocalDate deadline, Integer remainderOfWorkPeriodInMin, Integer durationRequiredInMinutes) {
		if (date.equals(today) && deadline == tomorrow) {
			
			LocalTime oneSessionBeforeWorkEnds = configWorkEndTime.minusMinutes(configWorkMinutes);
			
			// calculate number of sessions needed to finish task
			Float numberOfSessionsWithDecimal = (float) durationRequiredInMinutes / (float) configWorkMinutes;
			Integer numberOfSessions = (int) Math.ceil(numberOfSessionsWithDecimal);
			
			// number of work sessions should equal number of breaks
		    Integer durationRequiredWithBreakInBetween = durationRequiredInMinutes + configBreakMinutes * numberOfSessions;
		    
		    if (currentTime.isAfter(oneSessionBeforeWorkEnds) || durationRequiredWithBreakInBetween > remainderOfWorkPeriodInMin) {
		    	JOptionPane.showMessageDialog(null, "You may not be able to finish on time. You should ask for an extension.","Schedule Conflicts", JOptionPane.WARNING_MESSAGE);
		    }
		}
	}
	
	// Bugs to squash:
	// Session starts 5 mins after the start of work period
}
