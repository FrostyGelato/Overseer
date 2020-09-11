package com.joshua.overseer;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;

import javax.swing.JOptionPane;

public class Scheduler {

	public Scheduler(String name, LocalDate deadline, LocalTime timeRequired) {
		
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
		
		// if task is due tomorrow
		if (deadline == tomorrow) {
		    
			Float numberOfBreaksWithDecimal = (float) durationRequiredInMinutes/ (float) workMinutes;
			Integer numberOfBreaks = (int) Math.ceil(numberOfBreaksWithDecimal);
		    Integer durationRequiredWithBreakInBetween = durationRequiredInMinutes + breakMinutes * numberOfBreaks;
		    
		    Integer numberOfPeriods = durationRequiredInMinutes/workMinutes;
		    
		    Duration remainderOfWork = Duration.between(currentTime, workEndTime);
		    Integer remainderOfWorkMin = (int) remainderOfWork.toMinutes();
		    
		    if (currentTime.isAfter(enoughTimeForOneSession) || durationRequiredWithBreakInBetween > remainderOfWorkMin) {
		    	JOptionPane.showMessageDialog(null, "You may not be able to finish on time. You should ask for an extension.","Schedule Conflicts", JOptionPane.WARNING_MESSAGE);
		    }
		    
		    Session[] sessions = new Session[numberOfPeriods];
		    Integer i = 0;
		    if (remainingDuration > 0) {
		    	Session newSession = new Session(name, start, end);
		    	sessions[i] = newSession;
		    	i++;
		    }
		    
			//SessionManager sessionManager = new SessionManager();
			//sessionManager.addSessions(sessions);
			
		}/* else if (currentTime.isBefore(endTimeWithWork)) {
			
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
