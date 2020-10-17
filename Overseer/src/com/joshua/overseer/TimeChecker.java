package com.joshua.overseer;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class TimeChecker {
	
	LocalDate today = LocalDate.now();
	
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

	public TimeChecker() {
		
	}
	
	public boolean checkIfEnoughTime(LocalDate deadline, LocalTime timeRequired) {
		
		// convert timeRequired from LocalTime to Duration
		Duration hoursRequired = Duration.ofHours(timeRequired.getHour());
		int minutesRequired = timeRequired.getMinute();
		Integer durationRequiredInMinutes = (int) hoursRequired.toMinutes() + minutesRequired;
		
		// calculate number of sessions needed to finish task
		Float numberOfSessionsWithDecimal = (float) durationRequiredInMinutes / (float) configWorkMinutes;
		Integer numberOfSessions = (int) Math.ceil(numberOfSessionsWithDecimal);
		
		// number of work sessions should equal number of breaks
	    Integer durationRequiredWithBreakInBetween = durationRequiredInMinutes + configBreakMinutes * numberOfSessions;
	    
	    Integer timeAvailable = 0;
	    
	    ArrayList<Session> sessionArrayListForCheck = sessionManager.getSessions();
	    
	    // loop is for calculating timeAvailable
	    // loop through every day from now to day before deadline
	    for (LocalDate date = today; !(date.equals(deadline)); date = date.plusDays(1)) {
	    	
	    	if (date.equals(today) && LocalTime.now().isAfter(configWorkEndTime)) {
	    		
	    		timeAvailable = 0;
	    	} else {
	    		for (Session session:sessionArrayListForCheck) {
	    			
	    			if (session.date.equals(date)) {
	    				
	    				if (session.endTime.isAfter(previousSessionEnds)) {
	    					
		    				lastSessionEnds = session.endTime;
		    				previousSessionEnds = lastSessionEnds;
		    			}
	    			}
	    		}
		    	
		    	Duration durationAvailable = Duration.between(lastSessionEnds, configWorkEndTime);
		    	
		    	Integer timeAvailableForDate = (int) durationAvailable.toMinutes();
		    	
		    	timeAvailable = timeAvailable + timeAvailableForDate;
	    	}
	    }
	    
	    if (durationRequiredWithBreakInBetween > timeAvailable) {
	    	return false;
	    } else {
	    	return true;
	    }
	}
}
