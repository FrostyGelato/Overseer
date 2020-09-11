package com.joshua.overseer;

import java.time.LocalDate;
import java.time.LocalTime;

public class Session {
	
	String name;
	LocalTime startTime;
	LocalTime endTime;
	LocalDate date;
	
	public Session(String taskName, LocalTime start, LocalTime end, LocalDate taskDate) {
		name = taskName;
		startTime = start;
		endTime = end;
		date = taskDate;
	}

}
