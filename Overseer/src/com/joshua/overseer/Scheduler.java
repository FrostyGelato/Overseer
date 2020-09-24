package com.joshua.overseer;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Scheduler {

	public Scheduler() {
		
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
		Integer combinedMinutes = configManager.getCombinedTimeLength();
		Integer breakMinutes = configManager.getBreakTimeLength();
		Integer workMinutes = configManager.getWorkTimeLength();
		
		// fetch more setting data
		LocalTime workStartTime = LocalTime.parse(configManager.getStartTime());
		LocalTime workEndTime = LocalTime.parse(configManager.getEndTime());
		LocalTime enoughTimeForOneSession = workEndTime.minusMinutes(workMinutes);
		
		// convert LocalTime to Duration
		Duration hoursRequired = Duration.ofHours(timeRequired.getHour());
		int minutesRequired = timeRequired.getMinute();
		Integer durationRequiredInMinutes = (int) hoursRequired.toMinutes() + minutesRequired;
		
		Float numberOfSessionsWithDecimal = (float) durationRequiredInMinutes / (float) workMinutes;
		Integer numberOfSessions = (int) Math.ceil(numberOfSessionsWithDecimal);
		
		// number of work sessions should equal number of breaks
	    Integer durationRequiredWithBreakInBetween = durationRequiredInMinutes + breakMinutes * numberOfSessions;
	    
	    Duration remainderOfWork = Duration.between(currentTime, workEndTime);
	    Integer remainderOfWorkMin = (int) remainderOfWork.toMinutes();
		
		// if task is due tomorrow
		if (deadline == tomorrow) {
		    
		    if (currentTime.isAfter(enoughTimeForOneSession) || durationRequiredWithBreakInBetween > remainderOfWorkMin) {
		    	JOptionPane.showMessageDialog(null, "You may not be able to finish on time. You should ask for an extension.","Schedule Conflicts", JOptionPane.WARNING_MESSAGE);
		    }
		    // the above code is finished for now
		}
		
		// create array
	    ArrayList<Session> sessionArray = new ArrayList<Session>();
	    
	    Integer sessionNumber = 0;
	    
	    Session newSession;
	    
	    while (remainderOfWorkMin > combinedMinutes && remainderOfWork.toMinutes() > 0) {
	    //for (int i = 0; i < 2; i++) {
	    	LocalTime sessionStartTime = currentTime.plusMinutes(combinedMinutes * sessionNumber);
	    	LocalTime sessionEndTime = sessionStartTime.plusMinutes(workMinutes);
	    		    	
	    	newSession = new Session(name, sessionStartTime, sessionEndTime, today);
	    	
	    	sessionArray.add(newSession);
	    	
	    	sessionNumber++;
	    	remainderOfWorkMin = remainderOfWorkMin - (sessionNumber * combinedMinutes);
	    }

	    SessionManager sessionManager = new SessionManager();
	    sessionManager.addSessions(sessionArray);
			
		/*} else if (currentTime.isBefore(endTimeWithWork)) {
			
		    Period period = Period.between(currentDate, deadline);
		    int difference = period.getDays();
		    
		    int minutesRequired = timeRequired.getMinute();
		    Duration hoursRequired = Duration.ofHours(timeRequired.getHour());
		    Integer durationRequired = (int) hoursRequired.toMinutes() + minutesRequired;
		    
		    Integer numberOfPeriods = durationRequired/workMinutes;
		    Integer days = numberOfPeriods/difference;
		}*/
	}

}
