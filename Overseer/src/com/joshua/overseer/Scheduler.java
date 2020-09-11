package com.joshua.overseer;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;

import javax.swing.JOptionPane;

public class Scheduler {

	public Scheduler(String name, LocalDate deadline, LocalTime timeRequired) {
		ConfigManager configManager = new ConfigManager();
		
		LocalTime currentTime = LocalTime.now();
		LocalDate currentDate = LocalDate.now();
		
		Integer workMinutes = configManager.getWorkTimeLength();
		LocalTime workPeriod = LocalTime.of(0, workMinutes);
		
		Integer breakMinutes = configManager.getBreakTimeLength();
		
		Integer combinedMinutes = configManager.getCombinedTimeLength();
		
		LocalTime startTime = LocalTime.parse(configManager.getStartTime());
		LocalTime endTime = LocalTime.parse(configManager.getEndTime());
		LocalTime endTimeWithWork = endTime.minusMinutes(workMinutes);
		
		// if task is due tomorrow
		if (deadline == currentDate.plusDays(1) || currentTime.isBefore(endTimeWithWork)) {
			
			int minutesRequired = timeRequired.getMinute();
			Duration hoursRequired = Duration.ofHours(timeRequired.getHour());
		    Integer durationRequired = (int) hoursRequired.toMinutes() + minutesRequired;
		    Integer durationRequiredWithBreak = durationRequired + breakMinutes * (durationRequired/combinedMinutes);
		    
		    Integer numberOfPeriods = durationRequired/workMinutes;
		    
		    Duration remainderOfWork = Duration.between(currentTime, endTime);
		    Integer remainderOfWorkMin = (int) remainderOfWork.toMinutes();
		    
		    // if durationRequired is greater than the time remaining before work time ends
		    if (durationRequiredWithBreak > remainderOfWorkMin) {
		    // print not enough time, please ask for deadline
		    	JOptionPane.showMessageDialog(null, "You may not be able to finish on time. You should ask for an extension.","Schedule Conflicts", JOptionPane.WARNING_MESSAGE);
		    }
		    
			//Session session = new Session(start, end);
			
		} else if (currentTime.isBefore(endTimeWithWork)) {
			
		    Period period = Period.between(currentDate, deadline);
		    int difference = period.getDays();
		    
		    int minutesRequired = timeRequired.getMinute();
		    Duration hoursRequired = Duration.ofHours(timeRequired.getHour());
		    Integer durationRequired = (int) hoursRequired.toMinutes() + minutesRequired;
		    
		    Integer numberOfPeriods = durationRequired/workMinutes;
		    Integer days = numberOfPeriods/difference;
		}
	}
	
	public Session[] getSessions() {
		return null;
		
	}

}
