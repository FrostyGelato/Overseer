package com.joshua.overseer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;

public class Scheduler {

	public Scheduler(LocalDate deadline) throws IOException {
		ConfigManager configManager = new ConfigManager();
		
		LocalTime currentTime = LocalTime.now();
		LocalDate currentDate = LocalDate.now();
		
		String endTimeString = configManager.getEndTime();
		Integer workMinutes = configManager.getWorkTimeLength();
		LocalTime workPeriod = LocalTime.of(0, workMinutes);
		LocalTime endTime = LocalTime.parse(endTimeString);
		LocalTime endTimeWithWork = endTime.minusMinutes(workMinutes);
		
		if (currentTime.isBefore(endTimeWithWork)) {
			
		    Period period = Period.between(currentDate, deadline);
		    int diff = period.getDays();
		 
		    System.out.println(diff);
		}
	}

}
